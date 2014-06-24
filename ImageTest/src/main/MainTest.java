package main;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;

import data.Pitch;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;

public class MainTest {
	
	public static void main(String args[])
	{
		Pitch pitch = new Pitch(6.7F, new CvRect(), new CvRect());
		
		CvPoint minor1 = new CvPoint(516,259);
		CvPoint minor4 = new CvPoint(758,501);	
		
		pitch.setMinor1(minor1);
		pitch.setMinor4(minor4);
		
		RouteTest route = new RouteTest(pitch);
		
		CvPoint minPunkt = new CvPoint(1193,419);
		CvPoint roboBagPunkt = new CvPoint(542,455);	
		
		System.out.println(route.blockingObstruction(roboBagPunkt, minPunkt));
	}
}