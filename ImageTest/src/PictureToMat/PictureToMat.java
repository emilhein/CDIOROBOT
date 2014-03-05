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

		// Mat image = Highgui.imread("test.JPG"); // ge BGR
		Mat m = Highgui.imread("test2.bmp"); // ge BGR
		System.out.println("The picture has a total of " + m.total()
				+ " pixels");
		// System.out.println(m.dump());
		/*
		 * int cols = m.cols(); int rows = m.rows(); int elemSize =
		 * (int)m.elemSize(); byte[] data = new byte[cols * rows * elemSize];
		 */
		// double[] rgb = m.get(0, 0);

		// System.out.println(m.dump());

		for (int j = 0; j < m.cols(); j++) {
			for (int b = 0; b < m.rows(); b++) {
				double[] rgb = m.get(j, b);
				for (int i = 0; i < rgb.length; i = i + 3) {
					System.out.println("Pixelnr " + "("+j+","+b+") " + "red:" + rgb[i]
							+ " green:" + rgb[i + 1] + " blue:" + rgb[i + 2]);
					if(rgb[i+2]==255){
						m.put(b, j, 100,100,100);
					 	System.out.println("hej");
					}
				}
			}
		}

		Mat frame = new Mat();
		Highgui.imwrite("test2Afterconvert.bmp", frame);
		// System.out.println(image.dump());

	}

}