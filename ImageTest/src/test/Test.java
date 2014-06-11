package test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class Test {

	public static void pictureToMat(String image) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat m = Highgui.imread(image);

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
}
