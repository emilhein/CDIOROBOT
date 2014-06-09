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

public class DetectBorder {

		private float externalHeight = 135;
		private float externalWidth = 195;
		private float innerHeight = 120;
		private float innerWidth = 180;
		private CvPoint goalA;
		private CvPoint goalB;

		private static float pixPerCm = -1;
		
		public CvRect getRectCoordis(String src, int blueMax, int greenMax, int redMin, int redMax) throws IOException
		{					
			//CanvasFrame cnvs=new CanvasFrame("Polygon");
	        //cnvs.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	           
			brownThreshold(src);
	        
	        BufferedImage brownThresholded = ImageIO.read(new File("BrownThreshold.png"));
	        IplImage img = IplImage.createFrom(brownThresholded);
	        
		    CvSize cvSize = cvSize(img.width(), img.height());
		    IplImage gry=cvCreateImage(cvSize, img.depth(), 1);
		    cvCvtColor(img, gry, CV_BGR2GRAY);

			
		    CvMemStorage storage = CvMemStorage.create();
		    CvSeq contours = new CvContour(null);

		    cvFindContours(gry, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_CCOMP, CV_CHAIN_APPROX_NONE, new CvPoint(0,0));

		    CvSeq ptr = new CvSeq();

		    CvPoint p1 = new CvPoint(0,0),p2 = new CvPoint(0,0);
		    CvRect greatest = new CvRect(0,0,0,0);


		    for (ptr = contours; ptr != null; ptr = ptr.h_next()) {

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

		    cvRectangle(img, p1,p2, CV_RGB(0, 255, 0), 2, 8, 0);

		    pixPerCm = pixPerCm(greatest.width(), greatest.height());
		    
		    CvRect innerRect = new CvRect(0,0,0,0);
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
		    
		    cvRectangle(img, p1,p2, CV_RGB(255, 0, 0), 2, 8, 0);
		    
	      // cnvs.showImage(img);
		    
		    goalA = new CvPoint(innerRect.x(), innerRect.y() + (innerRect.height()/2));
		    goalB= new CvPoint(innerRect.x() + innerRect.width(), innerRect.y() + (innerRect.height()/2));
		    
		    cvLine(img, goalA, goalB, CV_RGB(0,200,255), 3,0,0);  
		    
		    cvSaveImage("edge.png", img);
		    
		    return innerRect;

	        
		}
		
		public float pixPerCm(int pixBorderWidth, int pixBorderHeight)
		{
			float widthPixPrCm = pixBorderWidth / externalWidth;
			float heightPixPrCm = pixBorderHeight / externalHeight;
			float pixPrCm = (widthPixPrCm + heightPixPrCm) / 2;
			
			return pixPrCm;
			//return (float) 6.761823; // er fastsat for test!
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
		
		public void brownThreshold(String image)
		{
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
	    	Mat img = Highgui.imread("billed0.png");
			
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
		}
}
