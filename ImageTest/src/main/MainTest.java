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
		
		RouteTest route = new RouteTest(pitch);
		
		CvPoint minPunkt = new CvPoint(861,416);
		CvPoint roboBagPunkt = new CvPoint(1000,416);
		CvPoint minor1 = new CvPoint(692,355);
		CvPoint minor4 = new CvPoint(813,484);		
		
		System.out.println(route.blockingObstruction(roboBagPunkt, minPunkt, minor1, minor4));
	}
}