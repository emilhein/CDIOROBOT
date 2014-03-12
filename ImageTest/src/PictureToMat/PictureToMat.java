package PictureToMat;

//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import javax.imageio.ImageIO;


import org.opencv.core.*;//Mat
//import org.opencv.core.MatOfRect;
//import org.opencv.core.Point;
//import org.opencv.core.Rect;
//import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
//import org.opencv.objdetect.CascadeClassifier;

public class PictureToMat {

	public static void main(String[] args) {
		// int[] test = new int[10];
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("\nRunning FaceDetector");

		// Mat image = Highgui.imread("test.JPG"); //  BGR
		Mat m = Highgui.imread("baneJpg.jpg"); //  BGR
		System.out.println("The picture has a total of " + m.total()
				+ " pixels");
		// System.out.println(m.dump());
		
		
		for (int j = 0; j < m.rows(); j++) {
			for (int b = 0; b < m.cols(); b++) {
				double[] rgb = m.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) {
					//System.out.println("Pixelnr " + "("+j+","+b+") " + "red:" + rgb[i+2]
					//		+ " green:" + rgb[i + 1] + " blue:" + rgb[i]);
					
					
					//rgb[i] == bl�
					//rgb[i+1] == gr�n
					// rgb[i+2] == r�d
					
				if(rgb[i]<135 && rgb[i+1] > 25 && rgb[i+2] > 50){				
						m.put(j, b, 63,133,205); // Brun
					}
					
					if(rgb[i]<170 && rgb[i+1] > 140 && rgb[i+2] <189){				
						m.put(j, b, 0,255,0); //gr�n
					 }
					 
					if(rgb[i+2]>165 && rgb[i+1] > 20 && rgb[i+1] < 130 && rgb[i] > 30 && rgb[i] < 180){				
						m.put(j, b, 0,0,255); // r�d
					 }
					if(rgb[i]+rgb[i+1]+rgb[i+2]>654){ 				
						m.put(j, b, 255,255,255);/// hvid
					}
					
					
				}
			}
		}

		Mat frame = new Mat();
		frame = m.clone();
		Highgui.imwrite("AfterConvert.jpg", frame);
		System.out.println("DONE");
		// System.out.println(image.dump());
		
	}

}
