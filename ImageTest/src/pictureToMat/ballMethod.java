package pictureToMat;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;


import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class ballMethod {

	private Mat ballMat;
	private Mat roboMat;
	private ArrayList<Float> roboCoordi;
	private ArrayList<Float> ballCoordi;
	private CvPoint roboFrontPunkt;
	private CvPoint roboBagPunkt;

	public void findCircle(int minRadius, int maxRadius,int dp,int mindist, int param1, int param2, String name, Boolean findRobot){ 

		ArrayList<Float> Coordi = new ArrayList<Float>();

		System.loadLibrary("opencv_java248");  

		Mat webcam_image;
		if(findRobot)
			webcam_image = roboMat;
		else
			webcam_image = ballMat;

		Mat circles = new Mat();
		if( !webcam_image.empty() )  
		{  

			Mat b8ch1 = new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);

			webcam_image.convertTo(webcam_image, CvType.CV_8UC1);


			Highgui.imwrite("TEST.png", webcam_image);

			// load image
			IplImage img = cvLoadImage("TEST.png");

			// create grayscale IplImage of the same dimensions, 8-bit and 1 channel
			IplImage imageGray = cvCreateImage(cvSize(img.width(), img.height()), IPL_DEPTH_8U, 1);

			// convert image to grayscale
			cvCvtColor(img, imageGray, CV_BGR2GRAY);

			cvSaveImage("TEST.png", imageGray);

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

		}  
		else  
		{  
			System.out.println(" --(!) No captured frame -- Break!"); 
		}

		if (findRobot)
		{
			roboCoordi=Coordi;
		}
		else 
		{
			ballCoordi=Coordi;		
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
						if ((((green - blue)/blue) > 0.3) && (green > red)) { // finder grÃ¸n
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
						 */

						// Til fremhÃ¦vning af bolde
						if ((blue > 100 || green > 100 || red > 100) && !(blue > 130 && green > 130 && red > 130)) {
							ballMat.put(j, b, 0, 0, 0);
							break;
						}
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
		if (roboCoordi.size()==6){

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

			if ((((green - blue)/blue) > 0.3) && (green > red)) { // finder grøn
				roboFrontPunkt.x(Math.round(roboCoordi.get(3)));
				roboFrontPunkt.y(Math.round(roboCoordi.get(4)));
				roboBagPunkt.x(Math.round(roboCoordi.get(0)));
				roboBagPunkt.y(Math.round(roboCoordi.get(1)));
			}

			if ((((red - blue)/blue) > 1.4) && (red > green)) { // finder rød
				roboFrontPunkt.x(Math.round(roboCoordi.get(0)));
				roboFrontPunkt.y(Math.round(roboCoordi.get(1)));
				roboBagPunkt.x(Math.round(roboCoordi.get(3)));
				roboBagPunkt.y(Math.round(roboCoordi.get(4)));
			}

			if ((((green2 - blue2)/blue2) > 0.3) && (green2 > red2)) { // finder grøn
				roboFrontPunkt.x(Math.round(roboCoordi.get(0)));
				roboFrontPunkt.y(Math.round(roboCoordi.get(1)));
				roboBagPunkt.x(Math.round(roboCoordi.get(3)));
				roboBagPunkt.y(Math.round(roboCoordi.get(4)));
			}
			if ((((red2 - blue2)/blue2) > 1.4) && (red2 > green2)) { // finder rød
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
		
	

		int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2.4);
		int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2.4);
		roboBagPunkt.x(roboBagPunkt.x()+diffX);
		roboBagPunkt.y(roboBagPunkt.y()+diffY);

		System.out.println("Robot frontpunkt = (" + roboFrontPunkt.x() + "," + roboFrontPunkt.y() +")");
		System.out.println("Robot bagpunkt = (" + roboBagPunkt.x() + "," + roboBagPunkt.y() +")");

	
		CvPoint midpunkt = new CvPoint(800,450);
		int PovFrontX = roboFrontPunkt.x() - midpunkt.x();
		int PovFrontY = roboFrontPunkt.y() - midpunkt.y();
		int PovBagX = roboBagPunkt.x() - midpunkt.x();
		int PovBagY = roboBagPunkt.y() - midpunkt.y();
		if(PovFrontX < 0)
		{
			if(PovFrontY<0)
			{
				roboFrontPunkt.x((int)(roboFrontPunkt.x()+PoV*Math.abs(PovFrontX)));
				roboFrontPunkt.y((int)(roboFrontPunkt.y()+PoV*Math.abs(PovFrontY)));
			}
			else
			{

				roboFrontPunkt.x((int)(roboFrontPunkt.x()+PoV*Math.abs(PovFrontX)));
				roboFrontPunkt.y((int)(roboFrontPunkt.y()-PoV*Math.abs(PovFrontY)));
			}
		}
		else
		{
			if(PovFrontY<0)
			{
				roboFrontPunkt.x((int)(roboFrontPunkt.x()-PoV*Math.abs(PovFrontX)));
				roboFrontPunkt.y((int)(roboFrontPunkt.y()+PoV*Math.abs(PovFrontY)));
			}
			else 
			{
				roboFrontPunkt.x((int)(roboFrontPunkt.x()-PoV*Math.abs(PovFrontX)));
				roboFrontPunkt.y((int)(roboFrontPunkt.y()-PoV*Math.abs(PovFrontY)));
			}
		}
		if(PovBagX < 0)
		{
			if(PovBagY<0)
			{
				roboBagPunkt.x((int)(roboBagPunkt.x()+PoV*Math.abs(PovBagX)));
				roboBagPunkt.y((int)(roboBagPunkt.y()+PoV*Math.abs(PovBagY)));
			}
			else
			{
				roboBagPunkt.x((int)(roboBagPunkt.x()+PoV*Math.abs(PovBagX)));
				roboBagPunkt.y((int)(roboBagPunkt.y()-PoV*Math.abs(PovBagY)));
			}
		}
		else
		{
			if(PovBagY<0)
			{
				roboBagPunkt.x((int)(roboBagPunkt.x()-PoV*Math.abs(PovBagX)));
				roboBagPunkt.y((int)(roboBagPunkt.y()+PoV*Math.abs(PovBagY)));
			}
			else
			{
				roboBagPunkt.x((int)(roboBagPunkt.x()-PoV*Math.abs(PovBagX)));
				roboBagPunkt.y((int)(roboBagPunkt.y()-PoV*Math.abs(PovBagY)));
			}
		}
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

