package pictureToMat;

import dist.CalcDist;
import dist.CalcAngle;
import dist.Punkt;

import java.util.ArrayList;
import java.util.List;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

public class RouteTest {
	static List<Integer> xCoor = new ArrayList<>();
	static List<Integer> yCoor = new ArrayList<>();
	static int total = 0;
	static Punkt goal = new Punkt(0, 240);
	static int ballNumber = 0;
	static int minLength = 1000000;
	static int minLength2 = 1000000;
	static Punkt minPunkt;
	static Punkt minPunkt2;
	
	
	public static void drawBallMap(float[] Coordi, Punkt roboBagPunkt, Punkt roboFrontPunkt) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat frame = Highgui.imread("Billed0.jpg"); /// SKal bruges til at lave et blank l�rred..

		// farver hele matricen hvid
		for (int j = 0; j < frame.rows(); j++) {
			for (int b = 0; b < frame.cols(); b++) {
				frame.put(j, b, 255, 255, 255);

			}
		}

	
		for (int c = 0; c < Coordi.length; c = c + 3) {
			// tegner firkant p� koordinatplads i sort
			for (int i = 0; i < 10; i++) {
				for (int g = 0; g < 10; g++) {
					frame.put(((Math.round(Coordi[c + 1])) + i), ((Math.round(Coordi[c])) + g), 0, 0, 0);
				}
			}
			// l�gger alle koordinater ind i en liste a x og en liste af y - her blot brugt de fiktive koordinater fra pakken Coordinates.
			yCoor.add(Math.round(Coordi[c]));
			xCoor.add(Math.round(Coordi[c + 1]));
		}
		
		//Dette for-loop finder det t�tteste ppunkt p� robotens front
		for (int i = 0; i < xCoor.size()-1; i++) {
			int tempLength = 0;	
		//	Core.line(frame, new Point(yCoor.get(i) + 5, xCoor.get(i) + 5),	new Point(yCoor.get(i + 1) + 5, xCoor.get(i + 1) + 5),	new Scalar((i*2) * 27, i * 12, i * 45), 2);
			
			CalcDist dist = new CalcDist();
			Punkt punkt2 = new Punkt(xCoor.get(i) + 5, yCoor.get(i) + 5);
			tempLength = dist.Calcdist(roboFrontPunkt, punkt2);
			total = total + tempLength;

			if (tempLength < minLength) {
				minLength = tempLength;
				minPunkt = punkt2;
			}
		}
		//Finder nr. 2 punkt
		for (int i = 0; i < xCoor.size(); i++) {
			int tempLength2 = 0;
			CalcDist dist = new CalcDist();
			Punkt punkt3 = new Punkt(xCoor.get(i) + 5, yCoor.get(i) + 5);
			tempLength2 = dist.Calcdist(minPunkt, punkt3);
			
			if (tempLength2 < minLength2 && punkt3.getX() != (minPunkt.getX()) && punkt3.getY() != (minPunkt.getY())) {
				minLength2 = tempLength2;
				minPunkt2 = punkt3;
			}
		}
		
		
		CalcAngle Angle = new CalcAngle();
		int BallAngle = Angle.Calcangle(roboBagPunkt, minPunkt);
		int RoboAngle = Angle.Calcangle(roboBagPunkt, roboFrontPunkt);
		int TurnAngle = BallAngle - RoboAngle;
		//TODO brug til at dreje robot
		
		
		paintPoint(frame, minPunkt, 255, 0, 0); // farver t�tteste r�d
		paintPoint(frame, minPunkt2, 0, 0, 255); // farver n�stt�tteste bl�
		
		
		
		
		Highgui.imwrite("RouteTest3.jpg", frame); // Gemmer billedet i roden
		
		System.out.println("Closest to robo is (" + minPunkt.getX() + ","+ minPunkt.getY() + ")");
		System.out.println("Closest to ball is (" + minPunkt2.getX() + ","+ minPunkt2.getY() + ")");
		System.out.println("Total line = " + total);
	}

	
	public static void paintPoint(Mat frame, Punkt p, int re, int gr, int bl) {
		for (int a = 0; a < 10; a++) {
			for (int b = 0; b < 10; b++) {
				frame.put(((p.getX() - 5) + a), ((p.getY() + b) - 5), bl, gr,
						re);

			}
		}
	}
}
