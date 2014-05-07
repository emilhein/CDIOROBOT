package main;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import dist.Punkt;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;
import pictureToMat.ballMethod;

public class Main {
	
	
	
	public static void main(String[] args) {
		
		
		TakePicture.main(args); //tager et billed og gemmer i roden af projektet.
		
		ballMethod balls = new ballMethod();
		
		/*
		*Standardværdier for disse argumenter plejer at være 4,8,19 eller 30,40,2
		*/
		float[] RoboCoor = balls.findCircle(16,20, 2);//minradius, maxrdius, antalbolde
		
		Mat frame = Highgui.imread("AfterColorConvert.jpg"); // henter det konverterede billlede
		
		double[] front = frame.get(Math.round(RoboCoor[0]), Math.round(RoboCoor[1]));
		double red = front[2]; //henter en rød farver fra den ene cirkel
		double green = front[1];
		
		double[] back = frame.get(Math.round(RoboCoor[3]), Math.round(RoboCoor[4]));
		double red2 = back[2]; // henter en rød farve fra den anden cirkel
		double green2 = back[1];
		
		Punkt roboFrontPunkt = new Punkt(0,0);
		Punkt roboBagPunkt = new Punkt(0,0);
		// herunder sættes robotpunket, alt efter hvilken cirkel der er rød.
		if(red > 253){
			roboFrontPunkt.setX(Math.round(RoboCoor[0]));
			roboFrontPunkt.setY(Math.round(RoboCoor[1]));
			roboBagPunkt.setX(Math.round(RoboCoor[3]));
			roboBagPunkt.setY(Math.round(RoboCoor[4]));
			System.out.println("red");
		} else if (red2 > 253){
			roboFrontPunkt.setX(Math.round(RoboCoor[3]));
			roboFrontPunkt.setY(Math.round(RoboCoor[4]));
			roboBagPunkt.setX(Math.round(RoboCoor[0]));
			roboBagPunkt.setY(Math.round(RoboCoor[1]));
			System.out.println("red2");
		}
		
		
		
		float[] ballCoor = balls.findCircle(4, 10, 5); // finder bolde 6,12,6
		RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet
		
		System.out.println("koordinaterne til bagpunkt er (" + roboBagPunkt.getX() +","+roboBagPunkt.getY()+")");
		System.out.println("koordinaterne til frontpunkt er (" + roboFrontPunkt.getX() +","+roboFrontPunkt.getY()+")");
		
	}

}
