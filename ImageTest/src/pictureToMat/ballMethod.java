package pictureToMat;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import java.awt.Color;
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
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import java.util.ArrayList;

public class ballMethod {
	
	private Mat ballMat;
	private Mat roboMat;

	public ArrayList<Float> findCircle(int minRadius, int maxRadius,int dp,int mindist, int param1, int param2, String name, Boolean findRobot){ 
		
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
		
		return Coordi;  
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
						
						// Til fremhævning af robot
						if (blue > 20 && blue < 110 && green > 130 && red < 160) { // finder grøn
							roboMat.put(j, b, 0, 255, 0);
							break;
						}
						else if (red > 130 && green < 60 && blue < 60) { // finder rød
							roboMat.put(j, b, 0, 0, 255);
							break;
						}
						else if (blue + red + green > 500 && blue > 120 && green > 120 && red > 120) { // finder hvid
							roboMat.put(j, b, 255, 255, 255);
							break;
						}
						else {
							roboMat.put(j, b, 0, 0, 0); // resten bliver sort
						}
						
						// Til fremhævning af bolde
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
}
