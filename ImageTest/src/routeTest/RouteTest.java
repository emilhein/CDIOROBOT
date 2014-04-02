package routeTest;
//import Coordinates.FictiveCoordinates;


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
		int[] Coordi;
		Coordi = new int[20];

		//maks 640 (x) - de lige + 0
		//maks 480 (y) - de ulige

		Coordi[0] = 590;	//x
		Coordi[1] = 362;	//y
		Coordi[2] = 610;   	//x
		Coordi[3] = 420;	//y
		Coordi[4] = 355;	//x
		Coordi[5] = 211;	//y
		Coordi[6] = 598;	//x
		Coordi[7] = 398;	//y
		Coordi[8] = 222;	//x
		Coordi[9] = 222;	//y
		Coordi[10] = 544;	//x
		Coordi[11] = 478;	//y
		Coordi[12] = 123;	//x
		Coordi[13] = 321;	//y
		Coordi[14] = 600;	//x
		Coordi[15] = 400;	//y
		Coordi[16] = 555;	//x
		Coordi[17] = 444;	//y
		Coordi[18] = 332;	//x
		Coordi[19] = 223;	//y
	
		
		for(int c =0;c<Coordi.length; c++){
		//tegner firkant på koordinatplads i sort
		for (int i = 0; i < 10; i++) {
			for (int g = 0; g < 10; g++) {
				frame.put(((Coordi[c]) + g), ((Coordi[c]+1) + i),
						0, 0, 0);
			}
		}
		}
		Highgui.imwrite("RouteTest.jpg", frame);
		System.out.println("DONE");

	}

}
