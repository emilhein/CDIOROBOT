package pictureToMat;

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
		


		
		// Mat image = Highgui.imread("test.JPG"); // BGR
		Mat m = Highgui.imread("camera0.jpg"); // BGR
		System.out.println("The picture has a total of " + m.total()
				+ " pixels");
		// System.out.println(m.dump());
		// int count = 0;
		// System.out.println("Pixelnr " + "("+j+","+b+") " + "red:" + rgb[i+2]
		// + " green:" + rgb[i + 1] + " blue:" + rgb[i]);

		for (int j = 0; j < m.rows(); j++) {
			for (int b = 0; b < m.cols(); b++) {
				double[] rgb = m.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) {
					double blue = rgb[i];
					double green = rgb[i+1];
					double red = rgb[i+2];
					
					if (blue < 50 && green > 25  && red > 100 && red < 180) { // finder brune farver
						m.put(j, b, 63, 133, 205); // brun
						// m.put(j, b, 0,0,0);
						break;
					}
					else if (blue > 12 && blue < 110 && green > 140 && red < 150) { // finder gr�nne farver															// farver
						m.put(j, b, 0, 255, 0); 
						break;
					}
					
					else if (red > 180 && green < 130 && blue < 120) { // finder r�de farver																
						m.put(j, b, 0, 0, 255); // r�d
						break;
					} 
					
					
					

					
					

					else if (blue + red + green > 500 && blue > 120 && green > 120 && red > 120) { // finder hvid 
						// drawrect(j,b,m);
						
						m.put(j, b, 255, 255, 255);// / hvid
						// count++;
						// if(count > 1000)
						// System.out.println("GOTO " + j +"," +b);
						break;
					} else {
						//m.put(j, b, 63, 133, 205); // resten bliver brun
						m.put(j, b, 0,0,0); // resten bliver sort
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
