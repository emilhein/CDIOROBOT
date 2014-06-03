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

public class ballMethod {

	public float[] findCircle(int minRadius, int maxRadius,int dp,int mindist, int param1, int param2, int numberOfCircles, String name, Boolean findRobot){ 
		
		float[] Coordi;
		Coordi = new float[numberOfCircles*3];
		int ballnr = 0;
				
		// Load the native library.  
		System.loadLibrary("opencv_java248");  
		// It is better to group all frames together so cut and paste to  
		// create more frames is easier  
		
				//-- 2. Read the video stream
		 
		/**********************TESTKODE********************/
	//	CanvasFrame cnvs=new CanvasFrame("Polygon");
    //    cnvs.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		
		Mat webcam_image;
		if(findRobot == true){
			pictureToMat("billed0.png");
			webcam_image = Highgui.imread("AfterColorConvert.png");  //billede der skal findes robot på.
			System.out.println("IN TRUE");
		}
		else{
			pictureToMat3("billed0.png");
			webcam_image = Highgui.imread("AfterColorConvert.png");  //billede der skal findes bolde på.

			// Save
			System.out.println("IN FALSE");
		}
		/************************SLUT**********************/
 
		//capture.read(webcam_image);  
		Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
		array255.setTo(new Scalar(255));     
		Mat circles = new Mat(); // No need (and don't know how) to initialize it.

		if( !webcam_image.empty() )  
		{  
			System.out.println("IMAGE IS PRESENT");
			
			Mat b8ch1 = new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1); //webcam_image.height(),webcam_image.width(),CvType.CV_8UC1
			
			
			webcam_image.convertTo(webcam_image, CvType.CV_8UC1); //32S
			
			
			Highgui.imwrite("TEST.png", webcam_image); // Gemmer billedet i
			// roden

			// load image
			IplImage img = cvLoadImage("TEST.png");

			// create grayscale IplImage of the same dimensions, 8-bit and 1 channel
			IplImage imageGray = cvCreateImage(cvSize(img.width(), img.height()), IPL_DEPTH_8U, 1);

			// convert image to grayscale
			cvCvtColor(img, imageGray, CV_BGR2GRAY);
			
			cvSaveImage("TEST2.png", imageGray);
			
			b8ch1 = Highgui.imread("TEST2.png", CvType.CV_8UC1);
			
			System.out.println("CHANNELS: " + webcam_image.channels());
			System.out.println("CHANNELS: " + b8ch1.channels());
			Imgproc.HoughCircles(b8ch1, circles, Imgproc.CV_HOUGH_GRADIENT, dp, mindist, param1, param2, minRadius, maxRadius);   
			System.out.println("lll");
			
			int rows = circles.rows();
			
			System.out.println("Cols: " + circles.cols());
						
			int elemSize = (int)circles.elemSize(); // Returns 12 (3 * 4bytes in a float)  
			float[] data2 = new float[rows * elemSize/4];  
			if (data2.length>0){ 
					for(int c=0; c<circles.cols(); c++)
					{
						circles.get(0, c, data2); // Points to the first element and reads the whole thing  // into data2
						Coordi[ballnr] = data2[0]; // x -koordinate
						Coordi[ballnr+1] = data2[1]; //y - koordinate
						Coordi[ballnr+2] = data2[2]; //radius
						ballnr = ballnr+3; // radius
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
		
		System.out.println("ok");
		
		return Coordi;  
	}

	public static void pictureToMat2(String image) {
		// int[] test = new int[10];
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat m = Highgui.imread(image);

		for (int j = 0; j < m.rows(); j++) 
		{
			for (int b = 0; b < m.cols(); b++) 
			{
				double[] rgb = m.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) 
				{
					double blue = rgb[i];
					double green = rgb[i + 1];
					double red = rgb[i + 2];
					if (blue <= 65 && green <= 65 && red <= 65) 
					{
						m.put(j, b, 0, 0, 0); // farver alt andet sort
						break;
					}
				}

			}
		}

		Highgui.imwrite("AfterColorConvert.png", m); // Gemmer billedet i
															// roden

		// load image
		IplImage img = cvLoadImage("AfterColorConvert.png");

		// create grayscale IplImage of the same dimensions, 8-bit and 1 channel
		//IplImage imageGray = cvCreateImage(cvSize(img.width(), img.height()), IPL_DEPTH_8U, 1);

		// convert image to grayscale
		//cvCvtColor(img, imageGray, CV_BGR2GRAY);
		// cvAdaptiveThreshold(imageGray, imageGray, 255,
		// CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY_INV, 11, 4);

		// Save
		//cvSaveImage("readyForBallMethodGrey.png", imageGray);
	}
	
	public static void pictureToMat3(String image) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat m = Highgui.imread(image);

		for (int j = 0; j < m.rows(); j++) {
			for (int b = 0; b < m.cols(); b++) {
				double[] rgb = m.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) {
					double blue = rgb[i];
					double green = rgb[i + 1];
					double red = rgb[i + 2];

					/*if (blue <= 100 && green <= 100 && red <= 100) // for m�rkt
					{
						m.put(j, b, 0, 0, 0);
						break;
					}*/
					if ((blue > 80 || green > 80 || red > 80) && !(blue > 130 && green > 130 && red > 130)) { // for gr�nt
						m.put(j, b, 0, 0, 0);
						break;
					}
				}
			}
		}

		Highgui.imwrite("AfterColorConvert.png", m); // Gemmer billedet i
															// roden
	}

	public static void pictureToMat(String image) {
		// int[] test = new int[10];
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// Mat image = Highgui.imread("test.png"); // BGR
		Mat m = Highgui.imread(image); // BGR
		// System.out.println(m.dump());
		// int count = 0;
		// System.out.println("Pixelnr " + "("+j+","+b+") " + "red:" + rgb[i+2]
		// + " green:" + rgb[i + 1] + " blue:" + rgb[i]);

		for (int j = 0; j < m.rows(); j++) {
			for (int b = 0; b < m.cols(); b++) {
				double[] rgb = m.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) {
					double blue = rgb[i];
					double green = rgb[i + 1];
					double red = rgb[i + 2];
					/*
					 * if (blue < 50 && green > 25 && red > 100 && red < 180) {
					 * // finder brune farver m.put(j, b, 63, 133, 205); // brun
					 * // m.put(j, b, 0,0,0); break; } else
					 */
					if (blue > 20 && blue < 110 && green > 130 && red < 160) { // finder
																				// grønne
																				// farver
																				// //
																				// farver
						m.put(j, b, 0, 255, 0);
						break;
					}

					else if (red > 130 && green < 60 && blue < 60) { // finder
																		// røde
																		// farver
						m.put(j, b, 0, 0, 255); // rød
						break;
					} else if (blue + red + green > 500 && blue > 120
							&& green > 120 && red > 120) { // finder hvid
						// drawrect(j,b,m);

						m.put(j, b, 255, 255, 255);// / hvid
						// count++;
						// if(count > 1000)
						// System.out.println("GOTO " + j +"," +b);
						break;
					} else {
						// m.put(j, b, 63, 133, 205); // resten bliver brun
						m.put(j, b, 0, 0, 0); // resten bliver sort
					}

				}
			}
		}

		Highgui.imwrite("AfterColorConvert.png", m); // Gemmer billedet i
															// roden
	}
	
	
    public void invertImage(String imageName) {
    	BufferedImage inputFile;
    	try {
    		inputFile = ImageIO.read(new File(imageName));


    		for (int x = 0; x < inputFile.getWidth(); x++) {
    			for (int y = 0; y < inputFile.getHeight(); y++) {
    				int rgba = inputFile.getRGB(x, y);
    				Color col = new Color(rgba, true);
    				col = new Color(255 + col.getRed(),
    								255 + col.getGreen(),
    								255 + col.getBlue());
    				inputFile.setRGB(x, y, col.getRGB());
    			}
    		}
    		File outputFile = new File("invert-"+imageName);
    		ImageIO.write(inputFile, "png", outputFile);
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
}
