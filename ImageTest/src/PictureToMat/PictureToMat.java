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

		// Mat image = Highgui.imread("test.JPG"); // BGR
		Mat m = Highgui.imread("robotFarve.jpg"); // BGR
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
					// rgb[i] == blå
					// rgb[i+1] == grøn
					// rgb[i+2] == rød
					if (rgb[i] < 170 && rgb[i + 1] > 140 && rgb[i + 2] < 150) { // finder
																				// grøne
																				// farver
						m.put(j, b, 0, 255, 0); // grøn
						break;
					}

					// rgb[i] == blå
					// rgb[i+1] == grøn
					// rgb[i+2] == rød
					else if (rgb[i + 2] > 165 && rgb[i + 1] > 20
							&& rgb[i + 1] < 130 && rgb[i] > 30 && rgb[i] < 180) { // finder
																					// røde
																					// farver
						m.put(j, b, 0, 0, 255); // rød
						break;
					} else if (rgb[i] < 150 && rgb[i + 1] > 25
							&& rgb[i + 2] > 50) { // finder brune farver

						m.put(j, b, 63, 133, 205); // brun
						// m.put(j, b, 0,0,255);
						break;
					}

					else if (rgb[i] + rgb[i + 1] + rgb[i + 2] > 585
							|| rgb[i] > 160 && rgb[i+1] > 160 && rgb[i+2] > 160) { // filrer
						// drawrect(j,b,m);
						m.put(j, b, 255, 255, 255);// / hvid
						// count++;
						// if(count > 1000)
						// System.out.println("GOTO " + j +"," +b);
						break;
					} else {
						m.put(j, b, 63, 133, 205);
						// m.put(j, b, 0,0,255);
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

	public static void drawrect(int a, int b, Mat m) {
		for (int i = a; i < a + 6; i++) {
			for (int j = b; j < b + 16; j++) {
				m.put(i, j, 0, 10, 0);
				System.out.println("HEEEJ");
			}
		}
	}
}
