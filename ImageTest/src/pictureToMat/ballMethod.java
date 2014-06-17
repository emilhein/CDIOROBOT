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

import dist.CalcAngle;
import dist.CalcDist;


public class ballMethod {

	private Mat ballMat;
	private Mat roboMat;
	private ArrayList<Float> roboCoordi;
	private ArrayList<Float> ballCoordi;
	private CvPoint roboFrontPunkt;
	private CvPoint roboBagPunkt;
	private Random random;

	public void findCircle(int minRadius, int maxRadius,int dp,int mindist, int param1, int param2, String name, Boolean findRobot, CvPoint corner1, CvPoint corner4, float pixPerCm){ 

		ArrayList<Float> Coordi = new ArrayList<Float>();

		System.loadLibrary("opencv_java248");  

		Mat pic0 = Highgui.imread("billed0.png");
		ballMat = pic0.clone();
		roboMat = pic0.clone();
		
		Mat webcam_image;
		if(findRobot)
			webcam_image = roboMat;
		else
		{
			pictureToMat2("billed0.png", corner1, corner4, pixPerCm);
			webcam_image = ballMat;
		}

		Mat circles = new Mat();
		if( !webcam_image.empty() )  
		{  

			Mat b8ch1 = new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);

			webcam_image.convertTo(webcam_image, CvType.CV_8UC1);
			

			Highgui.imwrite("TEST.png", webcam_image);

			// load image
			//IplImage img = cvLoadImage("TEST.png");

			// create grayscale IplImage of the same dimensions, 8-bit and 1 channel
			//IplImage imageGray = cvCreateImage(cvSize(img.width(), img.height()), IPL_DEPTH_8U, 1);

			// convert image to grayscale
			//cvCvtColor(img, imageGray, CV_BGR2GRAY);

			//cvSaveImage("TEST.png", img);

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
					Core.ellipse( webcam_image, center , new Size(data2[2],data2[2]), 0, 0, 360, new Scalar( 255, 0, 255 ), 4, 8, 0 );  	
				}

			}
			//-- 5. Display the image  

			Highgui.imwrite(name+".png", webcam_image); // Gemmer billedet i roden
			

			if(findRobot)
				roboCoordi = Coordi;
			else
			{
				ballCoordi = Coordi;
			}
		}  
		else  
		{  
			System.out.println(" --(!) No captured frame -- Break!"); 
		}
	}
	
	public void pictureToMat2(String image, CvPoint corner1, CvPoint corner4, float pixPerCm) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		random = new Random();

		Mat pic0 = Highgui.imread(image);
		ballMat = pic0.clone();
		roboMat = pic0.clone();
		
		CvPoint onRoboPoint = new CvPoint(roboBagPunkt.x() + (roboFrontPunkt.x() - roboBagPunkt.x())/2, roboBagPunkt.y() + (roboFrontPunkt.y() - roboBagPunkt.y())/2);
		//!! TEST
		onRoboPoint.x(onRoboPoint.x() + 15);
		onRoboPoint.y(onRoboPoint.y() + 15);
		//!! TEST SLUT
		
		double[] whiteCompare = pic0.get(onRoboPoint.y(), onRoboPoint.x());
		double whiteCompareR = whiteCompare[2];
		double whiteCompareG = whiteCompare[1];
		double whiteCompareB = whiteCompare[0];
		
		System.out.println("---------------------------------------");
		System.out.println("RED compare: " + whiteCompareR);
		System.out.println("GREEN compare: " + whiteCompareG);
		System.out.println("BLUE compare: " + whiteCompareB);
		System.out.println("---------------------------------------");

		paintPoint(pic0, onRoboPoint, 255, 0, 0,20);
		
		double randRed, randGreen, randBlue;

		ArrayList<CvPoint> robotCorners = new ArrayList<>();
		ArrayList<Float> cornerDistances = new ArrayList<>();
		CalcDist distCalculator = new CalcDist();
		float distBagPunkt, distFrontPunkt, distCollected;
		CvPoint currentPoint = new CvPoint();
		boolean addPoint = true;
		
		try
		{
			for (int j = 0; j < pic0.rows(); j++) {
				
				randRed = random.nextInt(80);
				randGreen= random.nextInt(80);
				randBlue = random.nextInt(80);
				
				for (int b = 0; b < pic0.cols(); b++) {
					double[] rgb = pic0.get(j, b);
					for (int i = 0; i < rgb.length; i = i + 3) {
						double blue = rgb[i];
						double green = rgb[i + 1];
						double red = rgb[i + 2];
					
						/*
						if ((((blue - red)/red) > 1.2) && (((blue - green)/green) > 0.3) && blue > 50) // finder bl�
						{ 
							
							currentPoint.x(b);
							currentPoint.y(j);
							distBagPunkt = distCalculator.Calcdist(currentPoint, roboBagPunkt);
							distFrontPunkt = distCalculator.Calcdist(currentPoint, roboFrontPunkt);
							
							addPoint = true;
							
							
							if(distBagPunkt <= 9.3 * pixPerCm)
							{	
								for(int ia = 0; ia < robotCorners.size(); ia++)
								{
									CvPoint collectedPoint = robotCorners.get(ia);
									
									if(collectedPoint != null)
									{
										distCollected = distCalculator.Calcdist(collectedPoint, currentPoint);
										if(distCollected <= 2 * pixPerCm)
										{
											if(distBagPunkt > cornerDistances.get(ia))
											{
												robotCorners.set(ia, currentPoint);
												cornerDistances.set(ia, distBagPunkt);
												addPoint = false;
												break;
											}
										}
									}
									else
									{
										break;
									}
								}

								if(addPoint)
								{
									robotCorners.add(currentPoint);
									cornerDistances.add(distBagPunkt);
								}
							}
							else if(distFrontPunkt <= 9.3 * pixPerCm)
							{	
								for(int ia = 0; ia < robotCorners.size(); ia++)
								{
									CvPoint collectedPoint = robotCorners.get(ia);
									
									if(collectedPoint != null)
									{
										distCollected = distCalculator.Calcdist(collectedPoint, currentPoint);
										if(distCollected <= 2 * pixPerCm)
										{
											if(distFrontPunkt > cornerDistances.get(ia))
											{
												robotCorners.set(ia, currentPoint);
												cornerDistances.set(ia, distFrontPunkt);
												addPoint = false;
												break;
											}
										}
									}
									else
									{
										break;
									}
								}

								if(addPoint)
								{
									robotCorners.add(currentPoint);
									cornerDistances.add(distFrontPunkt);
								}
							}
							
							ballMat.put(j, b, 0, 255, 0);
						}
						*/
						if(b < corner1.x() || corner4.x() < b || j < corner1.y() || corner4.y() < j)
						{
							ballMat.put(j, b, randRed, randGreen, randBlue);
						}
						else if(whiteCompareR/red < 2.2 && whiteCompareG/green < 2.2 && whiteCompareB/blue < 2.2)
						{
							//ballMat.put(j, b, 255, 255, 255);
						}
						else
						{
							ballMat.put(j, b, randRed, randGreen, randBlue);
						}
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("Could not convert image properly");
		}

		Highgui.imwrite("BOT.png", pic0);
		calcRobotCorners2();
		
		/*!!
		try {
			BufferedImage buffTest = ImageIO.read(new File("billed0.png"));
			Graphics2D g2dTest = buffTest.createGraphics();
			g2dTest.setBackground(Color.red);
			g2dTest.setColor(Color.cyan);
			
			Polygon polygon = new Polygon();
			
			for(CvPoint corner: robotCorners)
			{
				polygon.addPoint(corner.x(), corner.y());
			}
			
			polygon.addPoint(231, 643);
			polygon.addPoint(800, 23);
			polygon.addPoint(732, 111);
			polygon.addPoint(321, 402);
			g2dTest.drawPolygon(polygon);
			g2dTest.fillPolygon(polygon);
			
			g2dTest.dispose();
			
			IplImage ipTest = IplImage.createFrom(buffTest);
			cvSaveImage("firkantTest.png", ipTest);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		!!*/
	}
	
	public void calcRobotCorners2()
	{
		float constPpcm = 6.7F;
		CvPoint midPoint = new CvPoint();
		CvPoint corner1, corner2, corner3, corner4 = new CvPoint();
		CalcAngle angleCalculator = new CalcAngle();
		
		int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2);
		int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2);
		midPoint.x(roboBagPunkt.x()+diffX);
		midPoint.y(roboBagPunkt.y()+diffY);
		
		Float rotationAngle = angleCalculator.Calcangle(midPoint, roboFrontPunkt);
		
		System.out.println("rotation ANGLE: " + rotationAngle);
		System.out.println("1: " + (rotationAngle - 25));
		System.out.println("2: " + (rotationAngle + 25));
		System.out.println("3: " + (rotationAngle + 180 - 25));
		System.out.println("1: " + (rotationAngle + 180 + 25));
		
		System.out.println();
		
		/*corner1 = calcRobotCorner(midPoint, 17 * constPpcm, rotationAngle - 30);
		corner2 = calcRobotCorner(midPoint, 17 * constPpcm, rotationAngle + 30);
		corner3 = calcRobotCorner(midPoint, 17 * constPpcm, rotationAngle + 180 - 30);
		corner4 = calcRobotCorner(midPoint, 17 * constPpcm, rotationAngle + 180 + 30);*/
		
		///*************************** SKAL TEGNE EN RECTANGLE over robotten*************
		File imageFile = new File("billed0.png");
        BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
			   Graphics2D graph = img.createGraphics();
//		       
		       System.out.println("MID" + midPoint.x());
		       System.out.println("MID" + midPoint.y());
		       System.out.println("Ro" + rotationAngle);
		        graph.rotate(Math.toRadians(rotationAngle), midPoint.x(), midPoint.y());
		        graph.setColor(Color.BLACK);
		        graph.fillRect(820, 150, (int)(15.5*constPpcm), (int)(30*constPpcm));// Draw robo rect
		        
		        graph.dispose();
		        
		        ImageIO.write(img, "png", new File("dow.png"));

		        System.out.println("HHEEEEEEEEEEEEEEEEEEEEEEEEEEEEEY");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public void paintPoint(Mat frame, CvPoint p, int re, int gr, int bl, int size) {
		for (int a = 0; a < size; a++)
		{
			for (int b = 0; b < size; b++)
			{
				try
				{
					frame.put(((p.y() - size/2) + a), ((p.x() + b) - size/2), bl, gr, re);///KR�VER Y F�R X
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					System.out.println("Could not paint requested point");
				}
			}
		}
	}


	public void pictureToMat(String image) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat pic0 = Highgui.imread(image);
		ballMat = pic0.clone();
		roboMat = pic0.clone();

		try
		{
			for (int j = 0; j < pic0.rows(); j++) {
				for (int b = 0; b < pic0.cols(); b++) {
					double[] rgb = pic0.get(j, b);
					for (int i = 0; i < rgb.length; i = i + 3) {
						double blue = rgb[i];
						double green = rgb[i + 1];
						double red = rgb[i + 2];
						/*
						// Til fremhÃ¦vning af robot
						if ((((green - blue)/blue) > 0.3) && (green > red)) { // finder gr�n
							roboMat.put(j, b, 0, 255, 0);
							break;
						}
						else if ((((red - blue)/blue) > 1.4) && (red > green)) { // finder rÃ¸d
							roboMat.put(j, b, 0, 0, 255);
							break;
						}
						//else if (blue + red + green > 500 && blue > 120 && green > 120 && red > 120) { // finder hvid
						//	roboMat.put(j, b, 255, 255, 255);
						//	break;
						//}
						else {
							roboMat.put(j, b, 0, 0, 0); // resten bliver sort
						}

						

						// Til fremhÃ¦vning af bolde


						// Til fremhævning af bolde
						if ((blue > 100 || green > 100 || red > 100) && !(blue > 130 && green > 130 && red > 130)) {
							ballMat.put(j, b, 0, 0, 0);
							break;
						} */
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("Could not convert image properly");
		}
	}


	public boolean determineDirection(){


		double green = 0;
		double red= 0;
		double blue = 0;													
		double blue2 = 0;													
		double green2= 0;
		double red2= 0;

		System.out.println("Balls found: " + roboCoordi.size());
		if (roboCoordi.size() >= 6){

			roboFrontPunkt = new CvPoint(-1,-1);
			roboBagPunkt = new CvPoint(-1,-1);


			try {
				double[] front = roboMat.get(Math.round(roboCoordi.get(1)),	Math.round(roboCoordi.get(0))); // /Y OG X ER BYTTET OM
				blue = front[0];
				green = front[1];
				red = front[2];

				double[] back = roboMat.get(Math.round(roboCoordi.get(4)),	Math.round(roboCoordi.get(3))); // /

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
		else return false;

	}

	public void changePerspective (Float PoV) {
		
		CvPoint midpunkt = new CvPoint(800,450);
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

		System.out.println("Robot frontpunkt = (" + roboFrontPunkt.x() + "," + roboFrontPunkt.y() +")");
		System.out.println("Robot bagpunkt = (" + roboBagPunkt.x() + "," + roboBagPunkt.y() +")");
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
