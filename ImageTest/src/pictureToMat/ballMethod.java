package pictureToMat;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import java.util.ArrayList;

import java.util.List;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

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
		Mat webcam_image;
		if(findRobot == true){
			pictureToMat("billed0.png");
			webcam_image = Highgui.imread("readyForBallMethod.png");  //billede der skal findes bolde på.
			System.out.println("IN TRUE");
		}
		else{
			pictureToMat2("billed0.png");
			webcam_image = Highgui.imread("readyForBallMethodGrey.png");  //billede der skal findes bolde på.
			System.out.println("IN FALSE");
		}
		/************************SLUT**********************/
		
//		VideoCapture capture =new VideoCapture(0);  
		//Mat webcam_image = Highgui.imread("readyForBallMethod.png");  //billede der skal findes bolde på.
		Mat hsv_image=new Mat();  
		Mat thresholded=new Mat();  
		Mat thresholded2=new Mat();  
		//capture.read(webcam_image);  
		Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
		array255.setTo(new Scalar(255));  
		/*Mat S=new Mat();  
	     S.ones(new Size(hsv_image.width(),hsv_image.height()),CvType.CV_8UC1);  
	     Mat V=new Mat();  
	     V.ones(new Size(hsv_image.width(),hsv_image.height()),CvType.CV_8UC1);  
	         Mat H=new Mat();  
	     H.ones(new Size(hsv_image.width(),hsv_image.height()),CvType.CV_8UC1);*/  
		Mat distance=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
		//new Mat();//new Size(webcam_image.width(),webcam_image.height()),CvType.CV_8UC1);  
		List<Mat> lhsv = new ArrayList<Mat>(3);      
		Mat circles = new Mat(); // No need (and don't know how) to initialize it.  
		// The function later will do it... (to a 1*N*CV_32FC3)  
		Scalar hsv_min = new Scalar(0, 0, 0, 50);  
		Scalar hsv_max = new Scalar(60, 60, 255, 0);  
		Scalar hsv_min2 = new Scalar(0, 0, 100, 0);  
		Scalar hsv_max2 = new Scalar(70, 70, 255, 0);   
		double[] data=new double[3];  

		if( !webcam_image.empty() )  
		{  
			System.out.println("IMAGE IS PRESENT");
			// One way to select a range of colors by Hue  
			Imgproc.cvtColor(webcam_image, hsv_image, Imgproc.COLOR_BGR2HSV);  
			Core.inRange(hsv_image, hsv_min, hsv_max, thresholded);           
			Core.inRange(hsv_image, hsv_min2, hsv_max2, thresholded2);  
			Core.bitwise_or(thresholded, thresholded2, thresholded);  
			// Notice that the thresholds don't really work as a "distance"  
			// Ideally we would like to cut the image by hue and then pick just  
			// the area where S combined V are largest.  
			// Strictly speaking, this would be something like sqrt((255-S)^2+(255-V)^2)>Range  
			// But if we want to be "faster" we can do just (255-S)+(255-V)>Range  
			// Or otherwise 510-S-V>Range  
			// Anyhow, we do the following... Will see how fast it goes...  
			Core.split(hsv_image, lhsv); // We get 3 2D one channel Mats  
			Mat S = lhsv.get(1);  
			Mat V = lhsv.get(2);  
			Core.subtract(array255, S, S);  
			Core.subtract(array255, V, V);  
			S.convertTo(S, CvType.CV_32F);  
			V.convertTo(V, CvType.CV_32F);  
			Core.magnitude(S, V, distance);  
			Core.inRange(distance,new Scalar(0.0), new Scalar(255.0), thresholded2);  
			Core.bitwise_and(thresholded, thresholded2, thresholded);  
			// Apply the Hough Transform to find the circles  
			Imgproc.GaussianBlur(thresholded, thresholded, new Size(9,9),0,0);
			// STANDARD: Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 2, thresholded.height()/4, 500, 50, 0, 0);
			Imgproc.HoughCircles(thresholded, circles, Imgproc.CV_HOUGH_GRADIENT, dp, mindist, param1, param2, minRadius, maxRadius);   
			//dp = 2
			//minDist =1
			//param1 = 50
			//param2 = 5
			
			
			//Imgproc.Canny(thresholded, thresholded, 500, 250);  
			
		/*	
			 4. Add some info to the image  
			Core.line(webcam_image, new Point(150,50), new Point(202,200), new Scalar(100,10,10)/*CV_BGR(100,10,10), 3);  
			Core.circle(webcam_image, new Point(210,210), 10, new Scalar(100,10,10),3);  
			data=webcam_image.get(210, 210);  
			Core.putText(webcam_image,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
					,1.0,new Scalar(100,10,10,255),3);  
*/
			int rows = circles.rows();
						
			int elemSize = (int)circles.elemSize(); // Returns 12 (3 * 4bytes in a float)  
			float[] data2 = new float[rows * elemSize/4];  
			if (data2.length>0){ 
					for(int c=0; c<numberOfCircles; c++)
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

			Core.line(hsv_image, new Point(150,50), new Point(202,200), new Scalar(100,10,10)/*CV_BGR(100,10,10)*/, 3);  
			Core.circle(hsv_image, new Point(210,210), 10, new Scalar(100,10,10),3);  
			data=hsv_image.get(210, 210);  
			Core.putText(hsv_image,String.format("("+String.valueOf(data[0])+","+String.valueOf(data[1])+","+String.valueOf(data[2])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
					,1.0,new Scalar(100,10,10,255),3);  
			distance.convertTo(distance, CvType.CV_8UC1);  
			Core.line(distance, new Point(150,50), new Point(202,200), new Scalar(100)/*CV_BGR(100,10,10)*/, 3);  
			Core.circle(distance, new Point(210,210), 10, new Scalar(100),3);  
			data=(double[])distance.get(210, 210);  
			Core.putText(distance,String.format("("+String.valueOf(data[0])+")"),new Point(30, 30) , 3 //FONT_HERSHEY_SCRIPT_SIMPLEX  
					,1.0,new Scalar(100),3);   
			//-- 5. Display the image  

			Highgui.imwrite(name+".png", webcam_image); // Gemmer billedet i roden

		}  
		else  
		{  
			System.out.println(" --(!) No captured frame -- Break!"); 
		}  
		
		return Coordi;  
	}

	public static void pictureToMat2(String image) {
		// int[] test = new int[10];
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat m = Highgui.imread(image);

		for (int j = 0; j < m.rows(); j++) {
			for (int b = 0; b < m.cols(); b++) {
				double[] rgb = m.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) {
					double blue = rgb[i];
					double green = rgb[i + 1];
					double red = rgb[i + 2];
					if (blue > 65 && green > 65 && red > 65) { // fandt kanten
																// og farver
																// hvid: blue >
																// 80 && green >
																// 80 && red >
																// 80 // farver
						// m.put(j, b, 255, 255, 255);
						break;
					} else {
						m.put(j, b, 0, 0, 0); // farver alt andet sort
						break;
					}
				}

			}
		}

		Mat frame = new Mat();
		frame = m.clone();
		Highgui.imwrite("AfterColorConvert.png", frame); // Gemmer billedet i
															// roden

		// load image
		IplImage img = cvLoadImage("AfterColorConvert.png");

		// create grayscale IplImage of the same dimensions, 8-bit and 1 channel
		IplImage imageGray = cvCreateImage(cvSize(img.width(), img.height()),
				IPL_DEPTH_8U, 1);

		// convert image to grayscale
		cvCvtColor(img, imageGray, CV_BGR2GRAY);
		// cvAdaptiveThreshold(imageGray, imageGray, 255,
		// CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY_INV, 11, 4);

		// Save
		cvSaveImage("readyForBallMethodGrey.png", imageGray);
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

		Mat frame = new Mat();
		frame = m.clone();
		Highgui.imwrite("readyForBallMethod.png", frame); // Gemmer billedet i
															// roden
	}

}
