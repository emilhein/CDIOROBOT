package pictureToMat;

import dist.CalcDist;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class RouteTest {
	private List<Integer> xCoor = new ArrayList<Integer>();
	private List<Integer> yCoor = new ArrayList<Integer>();
	private float minLength;
	CvPoint minPunkt = new CvPoint(300,450);

	
	public CvPoint drawBallMap(ArrayList<Float> Coordi, CvPoint roboBagPunkt, CvPoint roboFrontPunkt, CvPoint goalA, float ppcm, CvPoint north, CvPoint south, CvPoint east, CvPoint west) {
		CalcDist dist = new CalcDist();
		minLength = 100000;

		

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat frame = Highgui.imread("billed0.png"); /// SKal bruges til at lave et blank lærred..

		for (int c = 0; c < Coordi.size(); c = c + 3)
		{
		
			// tegner firkant på koordinatplads i sort
			for (int i = 0; i < 20; i++)
			{
				for (int g = 0; g < 20; g++)
				{
					frame.put(((Math.round(Coordi.get(c+1))-10) + i), ((Math.round(Coordi.get(c))-10) + g), 0, 0, 0);

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
				tempLength = dist.Calcdist(roboBagPunkt, punkt2);

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

		paintPoint(frame, new CvPoint(minPunkt.x(), minPunkt.y()), 255, 0, 0,20); // farver tætteste bold rød

		paintPoint(frame,new CvPoint(roboBagPunkt.x(), roboBagPunkt.y()), 0, 128, 255,20); // farver robot bagpunkt
		paintPoint(frame,new CvPoint(roboFrontPunkt.x(), roboFrontPunkt.y()), 0, 255, 0,60); // farver robot forpunkt
		Core.line(frame, new Point(roboBagPunkt.x(), roboBagPunkt.y()),	new Point(roboFrontPunkt.x() + 10, roboFrontPunkt.y() + 10),	new Scalar(27, 12, 45), 4);
		Core.line(frame, new Point(roboBagPunkt.x(), roboBagPunkt.y()),	new Point(minPunkt.x() +10, minPunkt.y() + 10),	new Scalar(200, 120, 45), 4);
		
		
		paintPoint(frame,new CvPoint(1600/2, 900/2), 0, 128, 128,30); // midten af billedet
		paintPoint(frame,new CvPoint((goalA.x()-((int)(90*ppcm))), goalA.y()), 39, 127, 255,20); // midten af banen

		//********************* Draw the corners of the world ***************************
		
		paintPoint(frame,new CvPoint(north.x(), north.y()), 0 ,0, 0,40); // farver robot bagpunkt
		paintPoint(frame,new CvPoint(south.x(), south.y()), 0, 0, 0,40); // farver robot bagpunkt
		paintPoint(frame,new CvPoint(east.x(),east.y()), 0, 0, 0,40); // farver robot bagpunkt
		paintPoint(frame,new CvPoint(west.x(), west.y()), 0, 0, 0,40); // farver robot bagpunkt
	
		Highgui.imwrite("RouteTest3.png", frame); // Gemmer billedet i roden

		
		///*************************** SKAL TEGNE EN RECTANGLE over robotten*************
		File imageFile = new File("RouteTest3.png");
        BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
			   Graphics2D graph = img.createGraphics();
//		       
		       
		        graph.rotate(Math.toRadians(45), 1055, 435);
		        graph.setColor(Color.BLACK);
		        graph.fillRect(820, 150, (int)(15.5*ppcm), (int)(30*ppcm));// Draw robo rect
		        
		        graph.dispose();
		        
		        ImageIO.write(img, "png", new File("RouteTest3.png"));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
     

		//Core.rectangle(frame, new Point(100,100), new Point(250,150),new Scalar(200, 120, 45),4);
		
	/*	DetectRects findEdge = new DetectRects();
		float ppcm = findEdge.getPixPerCm();
		int height = findEdge.getInnerRect().height() + (60 * (int)ppcm);
		int width = findEdge.getInnerRect().width() + (90 * (int)ppcm);
		
		paintPoint(frame,new CvPoint(width, height), 0, 128, 128,30); // midten af banen
*/


		return minPunkt;
	}



	public float getMinLength() {
		return minLength;
	}



	public void setMinLength(float minLength) {
		this.minLength = minLength;
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
