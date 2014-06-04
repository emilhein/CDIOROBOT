package pictureToMat;

import dist.CalcDist;
import dist.Punkt;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

public class RouteTest {
	static List<Integer> xCoor = new ArrayList<Integer>();
	static List<Integer> yCoor = new ArrayList<Integer>();

	static int minLength = 1000000;
	static int minLength2 = 1000000;
	static Punkt minPunkt;
	//static Punkt minPunkt2 = new Punkt(1,1);
	
	
	public static Punkt drawBallMap(ArrayList<Float> Coordi, Punkt roboBagPunkt, Punkt roboFrontPunkt) {
		minPunkt = new Punkt(0,0);
		CalcDist dist = new CalcDist();

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat frame = Highgui.imread("White.png"); /// SKal bruges til at lave et blank lærred..
/*
		// farver hele matricen hvid
		for (int j = 0; j < frame.rows(); j++) {
			for (int b = 0; b < frame.cols(); b++) {
				frame.put(j, b, 255, 255, 255);

			}
		}
*/	
		for (int c = 0; c < Coordi.size(); c = c + 3) {
			// tegner firkant på koordinatplads i sort
			for (int i = 0; i < 20; i++) {
				for (int g = 0; g < 20; g++) {
					frame.put(((Math.round(Coordi.get(c+1))) + i), ((Math.round(Coordi.get(c))) + g), 0, 0, 0);

				}
			}
			// lægger alle koordinater ind i en liste a x og en liste af y - her blot brugt de fiktive koordinater fra pakken Coordinates.
			xCoor.add(Math.round(Coordi.get(c)));
			yCoor.add(Math.round(Coordi.get(c + 1)));
		}
		
		//Dette for-loop finder det tætteste ppunkt på robotens front
		for (int i = 0; i < xCoor.size(); i++) {
			int tempLength = 0;	
		//	Core.line(frame, new Point(yCoor.get(i) + 5, xCoor.get(i) + 5),	new Point(yCoor.get(i + 1) + 5, xCoor.get(i + 1) + 5),	new Scalar((i*2) * 27, i * 12, i * 45), 2);
			
			Punkt punkt2 = new Punkt(xCoor.get(i), yCoor.get(i));
			tempLength = dist.Calcdist(new Punkt(roboFrontPunkt.getX(),roboFrontPunkt.getY()), punkt2);
			
			if (tempLength < minLength) {
				minLength = tempLength;
				minPunkt = punkt2;
				minPunkt.setX(punkt2.getX());
				minPunkt.setY(punkt2.getY());
			}
			xCoor.clear();yCoor.clear();
		}
		//Finder nr. 2 punkt
		/*
		for (int i = 0; i < xCoor.size(); i++) {
			int tempLength2 = 0;
			CalcDist dist = new CalcDist();
			Punkt punkt3 = new Punkt(xCoor.get(i) + 10, yCoor.get(i) + 10);
			tempLength2 = dist.Calcdist(minPunkt, punkt3);
			
			if (tempLength2 < minLength2 && punkt3.getX() != (minPunkt.getX()) && punkt3.getY() != (minPunkt.getY())) {
				minLength2 = tempLength2;
				minPunkt2 = punkt3;
			}
		}
				*/
		
		paintPoint(frame, new Punkt(minPunkt.getX()+10, minPunkt.getY()+10), 255, 0, 0,20); // farver tætteste bold rød
	//	paintPoint(frame, new Punkt(minPunkt2.getX(), minPunkt2.getY()), 0, 0, 255,20); // farver næsttætteste bold blå
		
		paintPoint(frame,new Punkt(roboBagPunkt.getX() + 10, roboBagPunkt.getY() + 10), 0, 128, 255,20); //
		paintPoint(frame,new Punkt(roboFrontPunkt.getX() + 10, roboFrontPunkt.getY() + 10), 0, 255, 0,60); //
		
		Core.line(frame, new Point(roboBagPunkt.getX() + 10, roboBagPunkt.getY() + 10),	new Point(roboFrontPunkt.getX() + 10, roboFrontPunkt.getY() + 10),	new Scalar(27, 12, 45), 4);
		
		Highgui.imwrite("RouteTest3.png", frame); // Gemmer billedet i roden
		
		System.out.println("Closest to robo is (" + minPunkt.getX() + ","+ minPunkt.getY() + ")");
	//	System.out.println("Closest to ball is (" + minPunkt2.getX() + ","+ minPunkt2.getY() + ")");
		
		return minPunkt;
	}

	
	public static void paintPoint(Mat frame, Punkt p, int re, int gr, int bl, int size) {
		for (int a = 0; a < size; a++) {
			for (int b = 0; b < size; b++) {
				frame.put(((p.getY() - size/2) + a), ((p.getX() + b) - size/2), bl, gr, re);///KRÆVER Y FØR X

			}
		}
	}
}
