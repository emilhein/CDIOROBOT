package main;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import dist.Punkt;
import pictureToMat.RouteTest;
import pictureToMat.ballMethod;

public class Main {
	
	
	
	public static void main(String[] args) {
		
		ballMethod balls = new ballMethod();
		
		/*
		*Standardværdier for disse argumenter plejer at være 4,8,19 eller 30,40,2
		*
		*/
		float[] RoboCoor = balls.findCircle(20, 30, 2);//minradius, maxrdius, antalbolde
		
		Mat frame = Highgui.imread("AfterColorConvert.jpg"); // henter det konverterede billlede
		
		double[] front = frame.get(Math.round(RoboCoor[0]), Math.round(RoboCoor[1]));
		double red = front[2]; //henter en rød farver fra den ene cirkel
		
		double[] back = frame.get(Math.round(RoboCoor[3]), Math.round(RoboCoor[4]));
		double red2 = back[2]; // henter en rød farve fra den anden cirkel
		
		
		Punkt roboFrontPunkt = new Punkt(0,0);
		Punkt roboBagPunkt = new Punkt(0,0);
		// herunder sættes robotpunket, alt efter hvilken cirkel der er rød.
		if(red > 253){
			roboFrontPunkt.setX(Math.round(RoboCoor[0]));
			roboFrontPunkt.setY(Math.round(RoboCoor[1]));
			roboBagPunkt.setX(Math.round(RoboCoor[3]));
			roboBagPunkt.setX(Math.round(RoboCoor[4]));
		} else if (red2 > 253){
			roboFrontPunkt.setX(Math.round(RoboCoor[3]));
			roboFrontPunkt.setY(Math.round(RoboCoor[4]));
			roboBagPunkt.setX(Math.round(RoboCoor[0]));
			roboBagPunkt.setX(Math.round(RoboCoor[1]));
		}
		
		
		
		float[] ballCoor = balls.findCircle(10, 20, 6); // finder bolde
		RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet
		
		System.out.println("koordinaterne til frontpunkt er (" + roboBagPunkt.getX() +","+roboBagPunkt.getY()+")");
		System.out.println("koordinaterne til bagpunkt er (" + roboFrontPunkt.getX() +","+roboFrontPunkt.getY()+")");
		
	}

}
