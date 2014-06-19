package pictureToMat;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

















import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import data.Pitch;
import dist.CalcAngle;
import dist.CalcDist;


public class BallMethod {

	private Mat imgMat;
	private ArrayList<Float> roboCoordi;
	private ArrayList<Float> ballCoordi;
	private CvPoint roboFrontPunkt;
	private CvPoint roboBagPunkt;
	private Random random;
	private Pitch pitch;
	
	
	public BallMethod(Pitch pitch)
	{
		this.pitch = pitch;
	}
	

	public void findCircle(int minRadius, int maxRadius,int dp,int mindist, int param1, int param2, String name, Boolean findRobot){ 

		ArrayList<Float> Coordi = new ArrayList<Float>();

		System.loadLibrary("opencv_java248");
		
		if(findRobot)
			imgMat = Highgui.imread("billed0.png");
		else
		{
			imgMat = Highgui.imread("pixToMat.png");
		}

		Mat circles = new Mat();
		if( !imgMat.empty() )  
		{  
			
			Mat b8ch1 = new Mat(imgMat.height(),imgMat.width(),CvType.CV_8UC1);

			imgMat.convertTo(imgMat, CvType.CV_8UC1);

			Highgui.imwrite("TEST.png", imgMat);

			b8ch1 = Highgui.imread("TEST.png", CvType.CV_8UC1);

			Imgproc.HoughCircles(b8ch1, circles, Imgproc.CV_HOUGH_GRADIENT, dp, mindist, param1, param2, minRadius, maxRadius);   

			float[] data2 = new float[3];  
			if (data2.length>0){ 
				for(int c=0; c<circles.cols(); c++)
				{
					circles.get(0, c, data2); // Points to the first element and reads the whole thing  // into data2
					Coordi.add(data2[0]); // x -koordinate
					Coordi.add(data2[1]); //y - koordinate
					Coordi.add(data2[2]); //radius
					Point center= new Point(data2[0], data2[1]);  
					Core.ellipse( imgMat, center , new Size(data2[2],data2[2]), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );  	
				}

			}
			//-- 5. Display the image  

			Highgui.imwrite(name+".png", imgMat); // Gemmer billedet i roden
			

			if(findRobot)
			{
				roboCoordi = Coordi;
			}
			else
			{
				System.out.println("Balls found: " + Coordi.size()/3);
				ballCoordi = Coordi;
			}
		}  
		else  
		{  
			System.out.println(" --(!) No captured frame -- Break!"); 
		}
	}
	
/*	public void eliminateObstruction()
	{
		CvRect obstruction = pitch.getObstruction();
		float ppcm = pitch.getPixPerCm();
		
		Mat pic0 = Highgui.imread("pixToMat.png");
		for(int y = obstruction.y(); y < obstruction.y() + obstruction.height(); y++)
		{
			for(int x = obstruction.x(); x < obstruction.x() + obstruction.width(); x++)
			{
				if((obstruction.x() + 8.4*ppcm < x && x < obstruction.x() + 11.6*ppcm) || (obstruction.y() + 8.4*ppcm < y && y < obstruction.y() + 11.6*ppcm))
				{
					pic0.put(y, x, 0, 0, 0);
				}
			}
		}
		
		Highgui.imwrite("pixToMat.png", pic0);
	}*/
	
	
	public void rotateRobot()
	{
		float pixPerCm = pitch.getPixPerCm();
		CvPoint midPoint = new CvPoint();
		CalcAngle angleCalculator = new CalcAngle();
		
		int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2);
		int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2);
		midPoint.x(roboBagPunkt.x()+diffX);
		midPoint.y(roboBagPunkt.y()+diffY);
		
		Float rotationAngle = angleCalculator.Calcangle(midPoint, roboFrontPunkt);
		
		///*************************** SKAL TEGNE EN RECTANGLE over robotten*************
		File imageFile = new File("billed0.png");
        BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
			   Graphics2D graph = img.createGraphics();
//		       
		       
		        graph.rotate(Math.toRadians(rotationAngle), midPoint.x(), midPoint.y());
		        graph.setColor(Color.BLACK);
		        graph.fillRect(midPoint.x() - (int)Math.round(17*pixPerCm), midPoint.y() - (int)Math.round(11*pixPerCm), (int)(34*pixPerCm), (int)(22*pixPerCm));// Draw robo rect
		        
		        graph.dispose();
		        
		        ImageIO.write(img, "png", new File("pixToMat.png"));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public boolean determineDirection(){

		double green = 0;
		double red= 0;
		double blue = 0;													
		double blue2 = 0;													
		double green2= 0;
		double red2= 0;

		System.out.println(1);
		if (roboCoordi.size() >= 6){

			System.out.println(2);
			roboFrontPunkt = new CvPoint(-1,-1);
			roboBagPunkt = new CvPoint(-1,-1);

			try {
				double[] front = imgMat.get(Math.round(roboCoordi.get(1)),	Math.round(roboCoordi.get(0))); // /Y OG X ER BYTTET OM
				blue = front[0];
				green = front[1];
				red = front[2];

				double[] back = imgMat.get(Math.round(roboCoordi.get(4)),	Math.round(roboCoordi.get(3))); // /

				blue2 = back[0];
				green2 = back[1];
				red2 = back[2];
			} catch (IndexOutOfBoundsException e1) {
				System.out.println("INDEX OUT OF BOUND");
				return false;
			}


			if ((((green - blue)/blue) > 0.3) && (green > red)) { // finder gr�n
				roboFrontPunkt.x(Math.round(roboCoordi.get(3)));
				roboFrontPunkt.y(Math.round(roboCoordi.get(4)));
				roboBagPunkt.x(Math.round(roboCoordi.get(0)));
				roboBagPunkt.y(Math.round(roboCoordi.get(1)));
			}

			if ((((red - blue)/blue) > 1.4) && (red > green)) { // finder r�d
				roboFrontPunkt.x(Math.round(roboCoordi.get(0)));
				roboFrontPunkt.y(Math.round(roboCoordi.get(1)));
				roboBagPunkt.x(Math.round(roboCoordi.get(3)));
				roboBagPunkt.y(Math.round(roboCoordi.get(4)));
			}

			if ((((green2 - blue2)/blue2) > 0.3) && (green2 > red2)) { // finder gr�n
				roboFrontPunkt.x(Math.round(roboCoordi.get(0)));
				roboFrontPunkt.y(Math.round(roboCoordi.get(1)));
				roboBagPunkt.x(Math.round(roboCoordi.get(3)));
				roboBagPunkt.y(Math.round(roboCoordi.get(4)));
			}
			if ((((red2 - blue2)/blue2) > 1.4) && (red2 > green2)) { // finder r�d
				roboFrontPunkt.x(Math.round(roboCoordi.get(3)));
				roboFrontPunkt.y(Math.round(roboCoordi.get(4)));
				roboBagPunkt.x(Math.round(roboCoordi.get(0)));
				roboBagPunkt.y(Math.round(roboCoordi.get(1)));
			}

			if (roboFrontPunkt.x() == -1)
			{
				return false;
			}
			else 
			{ 
				return true;
			}
		}
		else
		{
			return false;
		}

	}

	
	public void changePerspective (Float PoV) {
		
		CvPoint midpunkt = pitch.getMidOfImg();
		
		int diffXFront = midpunkt.x() - roboFrontPunkt.x();
		int diffYFront = midpunkt.y() - roboFrontPunkt.y();
		int diffXBag = midpunkt.x() - roboBagPunkt.x();
		int diffYBag = midpunkt.y() - roboBagPunkt.y();
		
		roboFrontPunkt.x(roboFrontPunkt.x() + (int)(diffXFront * PoV));
		roboFrontPunkt.y(roboFrontPunkt.y() + (int)(diffYFront * PoV));
		roboBagPunkt.x(roboBagPunkt.x() + (int)(diffXBag * PoV));
		roboBagPunkt.y(roboBagPunkt.y() + (int)(diffYBag * PoV));
	}
	
	
	public void calculateRotationPoint()
	{
		int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2.4);
		int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2.4);
		roboBagPunkt.x(roboBagPunkt.x()+diffX);
		roboBagPunkt.y(roboBagPunkt.y()+diffY);
	}

	
	public ArrayList<Float> getBallCoordi () {
		return ballCoordi;
	}
	
	
	public CvPoint getRoboFrontPunkt(){
		return roboFrontPunkt;
	}
	
	
	public CvPoint getRoboBagPunkt(){
		return roboBagPunkt;
	}
}
