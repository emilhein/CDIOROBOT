package data;

import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class Pitch {
	private final float pixPerCm;
	private CvRect border = new CvRect();
	private CvRect obstruction = new CvRect();
	private CvPoint north = new CvPoint();
	private CvPoint south = new CvPoint();
	private CvPoint east = new CvPoint();
	private CvPoint west = new CvPoint();
	private CvPoint goalA = new CvPoint();
	private CvPoint goalB = new CvPoint();
	private CvPoint midOfImg = new CvPoint();
	private CvPoint midOfObs = new CvPoint();
	private final CvPoint origCorner1, origCorner4;
	private CvPoint miner1, miner2, miner3, miner4;

	public Pitch(float pixPerCm, CvRect border, CvRect obstruction)
	{
		this.pixPerCm = pixPerCm;
		this.border = border;
		this.obstruction = obstruction;
		
		origCorner1 = new CvPoint(border.x(), border.y());
		origCorner4 = new CvPoint(border.x() + border.width(), border.y() + border.height());
	}
	
	public void setMinor1(CvPoint minor1)
	{
		this.miner1 = minor1;
	}
	
	public void setMinor4(CvPoint minor4)
	{
		this.miner4 = minor4;
	}
	
	public float getPixPerCm() {
		return pixPerCm;
	}

	public CvRect getObstruction() {
		return obstruction;
	}

	public CvPoint getNorth() {
		return north;
	}

	public CvPoint getSouth() {
		return south;
	}

	public CvPoint getEast() {
		return east;
	}

	public CvPoint getWest() {
		return west;
	}

	public CvPoint getGoalA() {
		return goalA;
	}

	public CvPoint getGoalB() {
		return goalB;
	}

	public CvPoint getOrigCorner1() {
		return origCorner1;
	}

	public CvPoint getOrigCorner4() {
		return origCorner4;
	}

	public CvPoint getMidOfImg() {
		return midOfImg;
	}
	
	public CvPoint getMidOfObs() {
		return midOfObs;
	}

	public CvPoint getMiner1() {
		return miner1;
	}

	public CvPoint getMiner2() {
		return miner2;
	}

	public CvPoint getMiner3() {
		return miner3;
	}

	public CvPoint getMiner4() {
		return miner4;
	}

	public CvPoint getCorner1()
	{
		return new CvPoint(border.x(), border.y());
	}
	
	public CvPoint getCorner2()
	{
		return new CvPoint(border.x() + border.width(), border.y());
	}
	
	public CvPoint getCorner3()
	{
		return new CvPoint(border.x(), border.y() + border.height());
	}
	
	public CvPoint getCorner4()
	{
		return new CvPoint(border.x() + border.width(), border.y() + border.height());
	}
	
	public void adjustToCut(int xFactorOfCut, int yFactorOfCut)
	{
		int xDistToObstruction = obstruction.x() - border.x();
		int yDistToObstruction = obstruction.y() - border.y();
		
		border.x((int)(pixPerCm*xFactorOfCut));
		border.y((int)(pixPerCm*yFactorOfCut));
		obstruction.x(border.x() + xDistToObstruction);
		obstruction.y(border.y() + yDistToObstruction);
		
		goalA.x(border.x() + border.width());
	    goalA.y(border.y() + (border.height()/2));
	    
	    goalB.x(border.x());
	    goalB.y(border.y() + (border.height()/2));
	    
	    midOfObs = new CvPoint(obstruction.x() + (obstruction.width()/2), obstruction.y() + (obstruction.height()/2));
	    midOfImg = new CvPoint(border.x() + (border.width()/2), border.y() + (border.height()/2));
	    
	    findMiners();
	    findMajors();
	    /*
	    System.out.println("miner1 : " + miner1.x() +"," + miner1.y());
	    System.out.println("miner2 : " + miner2.x() +"," + miner2.y());
	    System.out.println("miner3 : " + miner3.x() +"," + miner3.y());
	    System.out.println("miner4 : " + miner4.x() +"," + miner4.y());
	    
	    System.out.println("North : " + north.x() +"," + north.y());
	    System.out.println("South : " + south.x() +"," + south.y());
	    System.out.println("West : " + west.x() +"," + west.y());
	    System.out.println("East : " + east.x() +"," + east.y());
	   */
	}
	
	public void findMiners() {

		int margin = (int)(10*pixPerCm);

		miner1 = new CvPoint(obstruction.x()-margin, obstruction.y()-margin);
		miner2 = new CvPoint(obstruction.x()+obstruction.width()+margin, obstruction.y()-margin);
		miner3 = new CvPoint(obstruction.x()-margin, obstruction.y()+obstruction.height()+margin);
		miner4 = new CvPoint(obstruction.x()+obstruction.width()+margin, obstruction.y()+obstruction.height()+margin);
	}

	public void findMajors() {
		int marginX = ((int)(15*pixPerCm));
		int marginY = ((int)(15*pixPerCm));
		
		north = new CvPoint (miner1.x()+((miner2.x()-miner1.x())/2),(miner1.y()-marginY)); 
		south = new CvPoint (miner1.x()+((miner2.x()-miner1.x())/2),(miner4.y()+marginY));
		east = new CvPoint (miner2.x()+marginX,miner2.y()+((miner4.y()-miner2.y())/2));
		west =  new CvPoint (miner1.x()-marginX,miner3.y()+((miner1.y()-miner3.y())/2));
	}
	
	public void cutOrigImg() {
		
		Mat pic0 = Highgui.imread("billed0.png");
		pic0 = pic0.submat(origCorner1.y()-(int)(pixPerCm*8), origCorner4.y()+(int)(pixPerCm*8), origCorner1.x()-(int)(pixPerCm*8), origCorner4.x()+(int)(pixPerCm*8));
		
		Highgui.imwrite("billed0.png", pic0);
	}
	
	
}
