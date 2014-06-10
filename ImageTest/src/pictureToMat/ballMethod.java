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

	public ArrayList<Float> findCircle(int minRadius, int maxRadius,int dp,int mindist, int param1, int param2, String name, Boolean findRobot){ 
		
		ArrayList<Float> Coordi = new ArrayList<Float>();
				
		// Load the native library.  
		System.loadLibrary("opencv_java248");  
		// It is better to group all frames together so cut and paste to  
		// create more frames is easier  
		
				//-- 2. Read the video stream
		 
	//	CanvasFrame cnvs=new CanvasFrame("Polygon");
    //    cnvs.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		
		
		Mat webcam_image;
		pictureToMat("billed0.png", findRobot);
		webcam_image = Highgui.imread("AfterColorConvert.png");  //billede der skal findes robot pÃ¥.
 
		//capture.read(webcam_image);  
		Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
		array255.setTo(new Scalar(255));     
		Mat circles = new Mat(); // No need (and don't know how) to initialize it.

		if( !webcam_image.empty() )  
		{  
			
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
			
			cvSaveImage("TEST.png", imageGray);
			
			b8ch1 = Highgui.imread("TEST.png", CvType.CV_8UC1);

			Imgproc.HoughCircles(b8ch1, circles, Imgproc.CV_HOUGH_GRADIENT, dp, mindist, param1, param2, minRadius, maxRadius);   
			
		//	int rows = circles.rows();
			
						
			//int elemSize = (int)circles.elemSize(); // Returns 12 (3 * 4bytes in a float)  
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
		
		System.out.println("ok");
		
		return Coordi;  
	}
	
	
	
	
	public static void pictureToMat(String image, boolean robo) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat ballMat = Highgui.imread(image);
		Mat roboMat = ballMat.clone();

		for (int j = 0; j < ballMat.rows(); j++) {
			for (int b = 0; b < ballMat.cols(); b++) {
				double[] rgb = ballMat.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) {
					double blue = rgb[i];
					double green = rgb[i + 1];
					double red = rgb[i + 2];
					
					// Til fremhævning af bolde
					if ((blue > 100 || green > 100 || red > 100) && !(blue > 130 && green > 130 && red > 130)) {
						ballMat.put(j, b, 0, 0, 0);
						break;
					}
					
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
				}
			}
		}

		if(robo == true)
			Highgui.imwrite("AfterColorConvert.png", roboMat);			
		else
			Highgui.imwrite("AfterColorConvert.png", ballMat);
	}
		


	
    public void invertImage(String imageName) {
    	BufferedImage inputFile;
    	
    	try {
    		inputFile = ImageIO.read(new File(imageName));


    		for (int x = 0; x < inputFile.getWidth(); x++) {
    			for (int y = 0; y < inputFile.getHeight(); y++) {
    				int rgba = inputFile.getRGB(x, y);
    				Color col = new Color(rgba, true);
    				col = new Color(255 - col.getRed(),
    								255 - col.getGreen(),
    								255);
    				inputFile.setRGB(x, y, col.getRGB());
    			}
    		}
    		File outputFile = new File("inverted.png");
    		
    		ImageIO.write(inputFile, "png", outputFile);
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }
}
