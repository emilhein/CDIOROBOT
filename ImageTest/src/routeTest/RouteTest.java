package routeTest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class RouteTest {

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat frame = Highgui.imread("camera0.jpg");

		//farver hele matricen hvid
		for (int j = 0; j < frame.rows(); j++) {
			for (int b = 0; b < frame.cols(); b++) {
				frame.put(j, b, 255, 255, 255);

			}
		}
		
		//tegner firkant på koordinatplads i sort
		for (int i = 0; i < 10; i++) {
			for (int g = 0; g < 10; g++) {
				frame.put(((frame.rows() / 2) + g), ((frame.cols() / 2) + i),
						0, 0, 0);
			}
		}
		Highgui.imwrite("RouteTest.jpg", frame);
		System.out.println("DONE");

	}

}
