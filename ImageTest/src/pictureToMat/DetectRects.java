package pictureToMat;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_core.IplImage;



public class DetectRects {
	
	private float externalHeight = 135;
	private float externalWidth = 195;
	private float innerHeight = 120;
	private float innerWidth = 180;
	CvPoint goalA;
	CvPoint goalB;
	CvPoint midOfImg = new CvPoint();
	private float pixPerCm = -1;
	private CvSeq contoursPointer;
	private CvSeq contoursPointer2;
	private CvRect obstruction;
	private CvRect innerRect;
	private CvPoint north;
	private CvPoint south;
	private CvPoint east;
	private CvPoint west;
	private int miner1, miner2, miner3, miner4;
	
	public CvPoint getMidOfImg()
	{
		return midOfImg;
	}
	
	public int getMiner1() {
		return miner1;
	}
	public void setMiner1(int miner1) {
		this.miner1 = miner1;
	}

	public int getMiner2() {
		return miner2;
	}

	public void setMiner2(int miner2) {
		this.miner2 = miner2;
	}

	public int getMiner3() {
		return miner3;
	}

	public void setMiner3(int miner3) {
		this.miner3 = miner3;
	}

	public int getMiner4() {
		return miner4;
	}

	public void setMiner4(int miner4) {
		this.miner4 = miner4;
	}

	
	public float pixPerCm(int pixBorderWidth, int pixBorderHeight)
	{
		float widthPixPrCm = pixBorderWidth / externalWidth;
		float heightPixPrCm = pixBorderHeight / externalHeight;
		float pixPrCm = (widthPixPrCm + heightPixPrCm) / 2;
		
		return pixPrCm;
		//return (float) 6.761823; // er fastsat for test!
	}
	
	
	public CvPoint getNorth () {
		return north;
	}
	public void setNorth (CvPoint north) {
		this.north = north;
	}
	public CvPoint getSouth () {
		return south;
	}
	public void setSouth (CvPoint south) {
		this.south = south;
	}
	public CvPoint getEast () {
		return east;
	}
	public void setEast (CvPoint east) {
		this.east = east;
	}
	public CvPoint getWest () {
		return west;
	}
	public void setWest (CvPoint west) {
		this.west = west;
	}
	public float getPixPerCm()
	{
		return pixPerCm;
	}
	public CvPoint getGoalA()
	{
		return goalA;
	}
	
	public CvPoint getGoalB()
	{
		return goalB;
	}
	
	public CvRect getInnerRect()
	{
		return innerRect;
	}
	
	public CvRect getObstruction()
	{ 
		return obstruction;
	}
	
	public void detectAllRects()
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
    	Mat imgOrig = Highgui.imread("billed0.png");
    	Mat img = imgOrig.clone();
		
		for (int j = 0; j < img.rows(); j++)
		{
			for (int b = 0; b < img.cols(); b++)
			{
				double[] rgb = img.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3)
				{
					double blue = rgb[i];
					double green = rgb[i+1];
					double red = rgb[i+2];
					
	 				if(((((red - blue)/blue) < 0.5) || (((red - green)/green) < 0.5)) || (red < 20 && green > 0 && blue > 0) || red < 18)
					{
						img.put(j, b, 0, 0, 0);
					}
					else
					{
						img.put(j, b, 255, 255, 255);
					}
				}

			}
		}
		
		Highgui.imwrite("BrownThreshold.png", img);
		Highgui.imwrite("Orig.png", imgOrig);
		
        BufferedImage brownThresholded;
        try {
        	brownThresholded = ImageIO.read(new File("BrownThreshold.png"));

        	IplImage thresholdImg = IplImage.createFrom(brownThresholded);

        	CvSize cvSize = cvSize(thresholdImg.width(), thresholdImg.height());
        	IplImage grayImg = cvCreateImage(cvSize, thresholdImg.depth(), 1);
        	cvCvtColor(thresholdImg, grayImg, CV_BGR2GRAY);

        	IplImage grayImg2 = grayImg.clone();
        	CvMemStorage storage = CvMemStorage.create();
        	CvMemStorage storage2 = CvMemStorage.create();
        	CvSeq contours = new CvContour(null);
        	CvSeq contours2 = new CvContour(null);

        	cvFindContours(grayImg, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new CvPoint(0,0));
        	cvFindContours(grayImg2, storage2, contours2, Loader.sizeof(CvContour.class), CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new CvPoint(0,0));
        	
        	contoursPointer = contours;
        	contoursPointer2 = contours2;
        	
    	    // ----- Detect border
    		
    		CvSeq ptr = new CvSeq();
    		CvPoint p1 = new CvPoint(0,0), p2 = new CvPoint(0,0);
    	    CvRect greatest = new CvRect(0,0,0,0);

    	    for (ptr = contoursPointer; ptr != null; ptr = ptr.h_next()) {

    	        CvRect sq = cvBoundingRect(ptr, 0);

    	        if(sq.width() > greatest.width() && sq.height() > greatest.height())
    	        {
    	        	greatest = sq;
    	            p1.x(sq.x());
    	            p2.x(sq.x()+sq.width());
    	            p1.y(sq.y());
    	            p2.y(sq.y()+sq.height());
    	        }
    	    }

    	    BufferedImage buffImg = ImageIO.read(new File("billed0.png"));
        	IplImage edge = IplImage.createFrom(buffImg);
        	
    	    cvRectangle(edge, p1,p2, CV_RGB(0, 255, 0), 2, 8, 0);

    	    pixPerCm = pixPerCm(greatest.width(), greatest.height());
    	    
    	    innerRect = new CvRect(0,0,0,0);
    	    int widthDifference = greatest.width() - (int)(innerWidth * pixPerCm);
    	    int heightDifference = greatest.height() - (int)(innerHeight * pixPerCm);
    	    innerRect.width(greatest.width() - widthDifference);
    	    innerRect.height(greatest.height() - heightDifference);
    	    innerRect.x(greatest.x() + widthDifference/2);
    	    innerRect.y(greatest.y() + heightDifference/2);
    	   
    	    p1.x(innerRect.x());
            p2.x(innerRect.x()+innerRect.width());
            p1.y(innerRect.y());
            p2.y(innerRect.y()+innerRect.height());
    	    
    	    cvRectangle(edge, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
    	    
          // cnvs.showImage(img);
    	    
    	    goalA = new CvPoint(innerRect.x() + innerRect.width(), innerRect.y() + (innerRect.height()/2));
    	    goalB = new CvPoint(innerRect.x(), innerRect.y() + (innerRect.height()/2));
    	    
    	    cvLine(edge, goalA, goalB, CV_RGB(0,200,255), 3,0,0);
    	    
    	    
    	    // ----- Detect obstruction
    	    
    	    ptr = new CvSeq();
    		p1 = new CvPoint(0,0);
    		p2 = new CvPoint(0,0);
    	    obstruction = new CvRect(0,0,0,0);
    	    CvRect sq;


    	    for (ptr = contoursPointer2; ptr != null; ptr = ptr.h_next()) {

    	    	sq = cvBoundingRect(ptr, 0);

    	        if(sq.width() > (20 * pixPerCm - 20) && sq.width() < (20 * pixPerCm + 20) && sq.height() > (20 * pixPerCm - 20) && sq.height() < (20 * pixPerCm + 20))
    	        {
    	        	obstruction = sq;
    	            p1.x(sq.x());
    	            p2.x(sq.x()+sq.width());
    	            p1.y(sq.y());
    	            p2.y(sq.y()+sq.height());
    	            cvRectangle(edge, p1,p2, CV_RGB(0, 0, 255), 2, 8, 0);
    	        }
    	    }
    	    
    	    
        	cvSaveImage("BrownThreshold2.png", thresholdImg);
        	cvSaveImage("edge.png", edge);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}

	public void findMiners () {

		int margin = (int)(8*pixPerCm);

		miner1 = obstruction.x()- margin;
		miner2 = obstruction.x()+obstruction.width()+margin;
		miner3 = obstruction.y()-margin;
		miner4 = obstruction.y()+obstruction.height()+margin;
	}

	public void findMajors () {
		int margin = ((int)(8*pixPerCm));
		
		north = new CvPoint (miner1+((miner2-miner1)/2),(miner3-margin)); 
		
		south = new CvPoint (miner1+((miner2-miner1)/2),(miner4+margin)); 

		east = new CvPoint (miner2+margin,miner3+((miner4-miner3)/2));
		
		west =  new CvPoint (miner1-margin,miner3+((miner4-miner3)/2));
	}
	
	public void adjustToCuttedImg(float ppcm, int xFactor, int yFactor)
	{
		int xDistToObstruction = obstruction.x() - innerRect.x();
		int yDistToObstruction = obstruction.y() - innerRect.y();
		innerRect.x((int)(ppcm*xFactor));
		innerRect.y((int)(ppcm*yFactor));
		obstruction.x(innerRect.x() + xDistToObstruction);
		obstruction.y(innerRect.y() + yDistToObstruction);
	    
	    goalA.x(innerRect.x() + innerRect.width());
	    goalA.y(innerRect.y() + (innerRect.height()/2));
	    
	    goalB.x(innerRect.x());
	    goalB.y(innerRect.y() + (innerRect.height()/2));
	    
	    midOfImg.x(innerRect.x() + (innerRect.width()/2));
	    midOfImg.y(innerRect.y() + (innerRect.height()/2));
	    
	    findMiners();
	    findMajors();
	}
}
