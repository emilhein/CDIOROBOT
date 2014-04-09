package Coordinates;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;        
import org.opencv.highgui.VideoCapture; 

public class FictiveCoordinates {
	public static void main (int[] args) {

	}

	public int [] GetFictiveCoordi (){

		int[] Coordi;
		Coordi = new int[20];

		//maks 630 (x) - de lige + 0
		//maks 470 (y) - de ulige

		Coordi[0] = 10;	//x
		Coordi[1] = 170;	//y
		Coordi[2] = 430;   	//x
		Coordi[3] = 10;	//y
		Coordi[4] = 355;	//x
		Coordi[5] = 211;	//y
		Coordi[6] = 198;	//x
		Coordi[7] = 398;	//y
		Coordi[8] = 222;	//x
		Coordi[9] = 122;	//y
		Coordi[10] = 544;	//x
		Coordi[11] = 369;	//y
		Coordi[12] = 123;	//x
		Coordi[13] = 321;	//y
		Coordi[14] = 220;	//x
		Coordi[15] = 400;	//y
		Coordi[16] = 455;	//x
		Coordi[17] = 444;	//y
		Coordi[18] = 332;	//x
		Coordi[19] = 223;	//y
		return Coordi;
	}

}
