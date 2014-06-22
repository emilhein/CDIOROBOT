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
		
		CvPoint minor1 = new CvPoint(692,355);
		CvPoint minor4 = new CvPoint(813,484);	
		
		pitch.setMinor1(minor1);
		pitch.setMinor4(minor4);
		
		RouteTest route = new RouteTest(pitch);
		
		CvPoint minPunkt = new CvPoint(298,638);
		CvPoint roboBagPunkt = new CvPoint(987,302);	
		
		System.out.println(route.blockingObstruction(roboBagPunkt, minPunkt));
	}
}