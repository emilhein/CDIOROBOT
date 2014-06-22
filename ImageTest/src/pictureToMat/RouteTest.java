package pictureToMat;

import data.Pitch;
import dist.CalcDist;



import java.util.ArrayList;
import java.util.List;



import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class RouteTest {
	private List<Integer> xCoor = new ArrayList<Integer>();
	private List<Integer> yCoor = new ArrayList<Integer>();
	private float minLength;
	private Pitch pitch;
	CvPoint minPunkt = new CvPoint(300,450);
	
	
	public RouteTest(Pitch pitch)
	{
		this.pitch = pitch;
	}
	
	
	public CvPoint drawBallMap(ArrayList<Float> Coordi, CvPoint roboBagPunkt, CvPoint roboFrontPunkt) {
	CalcDist dist = new CalcDist();
		minLength = 10000;
		
		CvPoint goalA = pitch.getGoalA();
		CvPoint midOfImg = pitch.getMidOfImg();
		CvPoint south = pitch.getSouth();
		CvPoint north = pitch.getNorth();
		CvPoint east = pitch.getEast();
		CvPoint west = pitch.getWest();
		float ppcm = pitch.getPixPerCm();

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
		paintPoint(frame,new CvPoint(roboFrontPunkt.x(), roboFrontPunkt.y()), 0, 255, 0,40); // farver robot forpunkt
		Core.line(frame, new Point(roboBagPunkt.x(), roboBagPunkt.y()),	new Point(roboFrontPunkt.x() , roboFrontPunkt.y()),	new Scalar(27, 12, 45), 4);
		Core.line(frame, new Point(roboBagPunkt.x(), roboBagPunkt.y()),	new Point(minPunkt.x(), minPunkt.y() ),	new Scalar(200, 120, 45), 4);
		//		Core.rectangle(frame, new Point(100,100), new Point(300,300), null, 1);
		paintPoint(frame,new CvPoint(midOfImg.x(), midOfImg.y()), 0, 128, 128,30); // midten af billedet
		paintPoint(frame,new CvPoint((goalA.x()-((int)(90*ppcm))), goalA.y()), 39, 127, 255,20); // midten af banen

		//********************* Draw the corners of the world ***************************
		if(north.x() != 0){
			paintPoint(frame,new CvPoint(north.x(), north.y()), 0 ,0, 0,40); // farver robot bagpunkt
			paintPoint(frame,new CvPoint(south.x(), south.y()), 0, 0, 0,40); // farver robot bagpunkt
			paintPoint(frame,new CvPoint(east.x(),east.y()), 0, 0, 0,40); // farver robot bagpunkt
			paintPoint(frame,new CvPoint(west.x(), west.y()), 0, 0, 0,40); // farver robot bagpunkt
		}
		Highgui.imwrite("RouteTest3.png", frame); // Gemmer billedet i roden

		return minPunkt;
	}



	public float getMinLength() {
		return minLength;
	}



	public void setMinLength(float minLength) {
		this.minLength = minLength;
	}

	public void paintPoint(Mat frame, CvPoint p, int re, int gr, int bl, int size) {
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


	public boolean blockingObstruction (CvPoint roboBagPunkt, CvPoint minPunkt) {
		System.out.println("Bagpunkt: " + roboBagPunkt.x()+","+roboBagPunkt.y());
		System.out.println("MinPunkt: " + minPunkt.x()+","+minPunkt.y());
		if(minPunkt.x() == 0 || minPunkt.y() == 0 || roboBagPunkt.x() == 0 || roboBagPunkt.y() == 0){
			return false;
		}
		Float a;
		Float b;

		a = (float)(minPunkt.y()-roboBagPunkt.y())/(float)(minPunkt.x()-roboBagPunkt.x());
		b = (float)(roboBagPunkt.y()-a*roboBagPunkt.x());

		int northSouthCollition;
		int westEastCollition;

		westEastCollition = (int) (a*pitch.getMiner1().x()+b);
		northSouthCollition = (int) ((pitch.getMiner1().y()-b)/a);

		if (westEastCollition >= pitch.getMiner1().y() && westEastCollition <= pitch.getMiner4().y()) 
		{
			CvPoint westCollition = new CvPoint(pitch.getMiner1().x(), westEastCollition);
			CvPoint eastCollition = new CvPoint(pitch.getMiner4().x(), westEastCollition);
			
			if(insideRect(westCollition, roboBagPunkt, minPunkt) || insideRect(eastCollition, roboBagPunkt, minPunkt))
			{
				System.out.println("Obstacle in the way");
				return true;
			}
		}
		else if (northSouthCollition >= pitch.getMiner1().x() && northSouthCollition <= pitch.getMiner4().x()) 
		{
			CvPoint northCollition = new CvPoint(northSouthCollition, pitch.getMiner1().y());
			CvPoint southCollition = new CvPoint(northSouthCollition, pitch.getMiner4().y());
			
			if(insideRect(northCollition, roboBagPunkt, minPunkt) || insideRect(southCollition, roboBagPunkt, minPunkt))
			{
				System.out.println("Returning true");

				return true;
			}
		}
		System.out.println("Returning false");

		return false;		
	}
	
	public boolean insideRect(CvPoint point, CvPoint cornerA, CvPoint cornerB)
	{
		if(cornerA.x() <= point.x() && point.x() <= cornerB.x())
		{
			if(cornerA.y() <= point.y() && point.y() <= cornerB.y())
			{
				return true;
			}
			else if(cornerB.y() <= point.y() && point.y() <= cornerA.y())
			{
				return true;
			}
		}
		else if(cornerB.x() <= point.x() && point.x() <= cornerA.x())
		{
			if(cornerA.y() <= point.y() && point.y() <= cornerB.y())
			{
				return true;
			}
			else if(cornerB.y() <= point.y() && point.y() <= cornerA.y())
			{
				return true;
			}
		}
		return false;
	}

}
