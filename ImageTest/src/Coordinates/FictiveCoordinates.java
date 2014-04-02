package Coordinates;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;        
import org.opencv.highgui.VideoCapture; 

public class FictiveCoordinates {
	public static void main (int[] args) {

	}

	public int [] GetFictiveCoordi (){

		int[] Coordi;
		Coordi = new int[19];

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
		return Coordi;
	}

}
