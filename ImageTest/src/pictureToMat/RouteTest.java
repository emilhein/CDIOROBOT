package pictureToMat;

import dist.CalcDist;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class RouteTest {
	static List<Integer> xCoor = new ArrayList<Integer>();
	static List<Integer> yCoor = new ArrayList<Integer>();

	public static CvPoint drawBallMap(ArrayList<Float> Coordi, CvPoint roboBagPunkt, CvPoint roboFrontPunkt) {
		CvPoint minPunkt = new CvPoint(0,0);
		float minLength = 1000000;
		CalcDist dist = new CalcDist();

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat frame = Highgui.imread("billed0.png"); /// SKal bruges til at lave et blank lærred..

		for (int c = 0; c < Coordi.size(); c = c + 3)
		{
		
			// tegner firkant på koordinatplads i sort
			for (int i = 0; i < 20; i++)
			{
				for (int g = 0; g < 20; g++)
				{
					frame.put(((Math.round(Coordi.get(c+1))) + i), ((Math.round(Coordi.get(c))) + g), 0, 0, 0);

				}
			}
			// lægger alle koordinater ind i en liste a x og en liste af y - her blot brugt de fiktive koordinater fra pakken Coordinates.
			xCoor.add(Math.round(Coordi.get(c)));
			yCoor.add(Math.round(Coordi.get(c + 1)));
		}

		//Dette for-loop finder det tætteste ppunkt på robotens front
		try {
			for (int i = 0; i < xCoor.size(); i++)
			{
				float tempLength = 0;	
				CvPoint punkt2 = new CvPoint(xCoor.get(i), yCoor.get(i));
				tempLength = dist.Calcdist(roboFrontPunkt, punkt2);

				if (tempLength < minLength)
				{
					minLength = tempLength;
					minPunkt = punkt2;
					minPunkt.x(punkt2.x());
					minPunkt.y(punkt2.y());
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("Closest balls can't be calculated, due to previous error...");
		}
		xCoor.clear();yCoor.clear();

		paintPoint(frame, new CvPoint(minPunkt.x()+10, minPunkt.y()+10), 255, 0, 0,20); // farver tætteste bold rød

		paintPoint(frame,new CvPoint(roboBagPunkt.x(), roboBagPunkt.y()), 0, 128, 255,20); //
		paintPoint(frame,new CvPoint(roboFrontPunkt.x(), roboFrontPunkt.y()), 0, 255, 0,60); //

		Core.line(frame, new Point(roboBagPunkt.x(), roboBagPunkt.y()),	new Point(roboFrontPunkt.x() + 10, roboFrontPunkt.y() + 10),	new Scalar(27, 12, 45), 4);
		Core.line(frame, new Point(roboBagPunkt.x(), roboBagPunkt.y()),	new Point(minPunkt.x() +10, minPunkt.y() + 10),	new Scalar(200, 120, 45), 4);
		
		
		paintPoint(frame,new CvPoint(1600/2, 900/2), 0, 128, 128,30); // midten af billedet
		
	/*	DetectRects findEdge = new DetectRects();
		float ppcm = findEdge.getPixPerCm();
		int height = findEdge.getInnerRect().height() + (60 * (int)ppcm);
		int width = findEdge.getInnerRect().width() + (90 * (int)ppcm);
		
		paintPoint(frame,new CvPoint(width, height), 0, 128, 128,30); // midten af banen
*/
		Highgui.imwrite("RouteTest3.png", frame); // Gemmer billedet i roden


		return minPunkt;
	}



	public static void paintPoint(Mat frame, CvPoint p, int re, int gr, int bl, int size) {
		for (int a = 0; a < size; a++)
		{
			for (int b = 0; b < size; b++)
			{
				try
				{
					frame.put(((p.y() - size/2) + a), ((p.x() + b) - size/2), bl, gr, re);///KRÆVER Y FØR X
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					System.out.println("Could not paint requested point");				}
			}
		}
	}
}
