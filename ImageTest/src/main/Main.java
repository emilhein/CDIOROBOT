package main;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import dist.CalcAngle;
import dist.CalcDist;
import dist.Punkt;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;
import pictureToMat.ballMethod;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

import java.io.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Punkt minPunkt;

		TakePicture.main(args); //tager et billed og gemmer i roden af projektet.

		ballMethod balls = new ballMethod();

		/*
		 *Standardv�rdier for disse argumenter plejer at v�re 4,8,19 eller 30,40,2
		 */
		float[] RoboCoor = balls.findCircle(6,12,2);//minradius, maxrdius, antalbolde

		Mat frame = Highgui.imread("AfterColorConvert.jpg"); // henter det konverterede billlede
		for(int i = 0; i < RoboCoor.length;i=i+3){
			System.out.println("Bold nr " + i +" ligger p� "+Math.round(RoboCoor[i]) + ","+Math.round(RoboCoor[i+1]));
		}

		double[] front = frame.get(Math.round(RoboCoor[1]), Math.round(RoboCoor[0])); ///X OG Y ER FUCKED
		//double red = front[2]; //henter en r�d farver fra den ene cirkel
		double blue = front[0];
		double green = front[1];
		double red = front[2];

		System.out.println("Har farverne = "+(int)blue + (int)red+(int)green);

		double[] back = frame.get(Math.round(RoboCoor[4]), Math.round(RoboCoor[3])); /// X OG Y ER FUCKED
		double red2 = back[2]; // henter en r�d farve ([2]) fra den anden cirkel

		Punkt roboFrontPunkt = new Punkt(0,0);
		Punkt roboBagPunkt = new Punkt(0,0);
		// herunder s�ttes robotpunket, alt efter hvilken cirkel der er r�d.
		if(red > 245){
			roboFrontPunkt.setX(Math.round(RoboCoor[0]));
			roboFrontPunkt.setY(Math.round(RoboCoor[1]));
			roboBagPunkt.setX(Math.round(RoboCoor[3]));
			roboBagPunkt.setY(Math.round(RoboCoor[4]));
			System.out.println("red");
		} else if (red2 > 245){
			roboFrontPunkt.setX(Math.round(RoboCoor[3]));
			roboFrontPunkt.setY(Math.round(RoboCoor[4]));
			roboBagPunkt.setX(Math.round(RoboCoor[0]));
			roboBagPunkt.setY(Math.round(RoboCoor[1]));
			System.out.println("red2");
		}
		/* 
		System.out.println("Dette er r�d1 farven = " + red);
		System.out.println("Dette er r�d2 farven = " + red2);
		 */

		float[] ballCoor = balls.findCircle(2, 6, 13); // finder bolde 6,12,6

		RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet

		minPunkt = RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet
		int tempx=minPunkt.getY();
		int tempy=minPunkt.getX();
		minPunkt.setX(tempx);
		minPunkt.setY(tempy);
		
		System.out.println("koordinaterne til Bagpunkt er (" + roboBagPunkt.getX() +","+roboBagPunkt.getY()+")");
		System.out.println("koordinaterne til Frontpunkt er (" + roboFrontPunkt.getX() +","+roboFrontPunkt.getY()+")");
		System.out.println("koordinaterne til MinPunkt er (" + minPunkt.getX() +","+minPunkt.getY()+")");

		Punkt nyRoboFront = new Punkt(roboFrontPunkt.getX()-roboBagPunkt.getX(),roboFrontPunkt.getY()-roboBagPunkt.getY());
		Punkt nyRoboBag = new Punkt(0,0);
		Punkt nyMinPunkt = new Punkt(minPunkt.getX()-roboBagPunkt.getX(),minPunkt.getY()-roboBagPunkt.getY());
		System.out.println("koordinaterne til nyBagpunkt er (" + nyRoboBag.getX() +","+nyRoboBag.getY()+")");
		System.out.println("koordinaterne til nyFrontpunkt er (" + nyRoboFront.getX() +","+nyRoboFront.getY()+")");
		System.out.println("koordinaterne til nyMinpunkt er (" + nyMinPunkt.getX() +","+nyMinPunkt.getY()+")");
		CalcAngle Angle = new CalcAngle();
		int BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		System.out.println("BallAngle = " + BallAngle);
		int RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		System.out.println("RoboAngle = " + RoboAngle);
		int TurnAngle = BallAngle - RoboAngle;

		CalcDist dist = new CalcDist();
		int minLength = Math.abs(dist.Calcdist(roboFrontPunkt, minPunkt));

		try{ 
			//pr�ver at forbinde til vores robot
			NXTInfo nxtInfo = new NXTInfo(2,"G9 awesome!","0016530918D4");
			NXTConnector connt = new NXTConnector();
			System.out.println("trying to connect");
			connt.connectTo(nxtInfo, NXTComm.LCP);
			System.out.println("connected");		//forbundet
			//�bner streams
			OutputStream dos = connt.getOutputStream();
			InputStream dis = connt.getInputStream();

			Scanner scan = new Scanner(System.in);
			while(true){
				System.out.println("Waiting for your go!");	
				int input = scan.nextInt();

				int Case;
				int i;
				System.out.println("TurnAngle = " + TurnAngle);
				int angle = TurnAngle*2;	//vinkel konvertering
				System.out.println("angle " + angle);
				if(angle > 0) 				//v�lger retning der skal drejes
					Case = 11;				
				else Case = 22;

				dos.write(Case);			//sender case
				dos.flush();
				dos.write(angle);			//sender vinkel
				dos.flush();

				//				//venter p� at motorerne ikke k�rer l�ngere
				//				int u = dis.read();			
				//				while(u==1){
				//					u = dis.read();
				//				}

				Thread.sleep(2000);
				//k�rer robot frem
				int distance = (minLength * 2) - 250;	//l�ngde konvertering
				System.out.println("dist = " + distance);
				dos.write(81);
				dos.flush();
				i = distance;
				dos.write(i);
				dos.flush();

				//				//venter p� at motorerne ikke k�rer l�ngere
				//				int j = dis.read();			
				//				while(j==1){
				//					j = dis.read();
				//				}

				Thread.sleep(2000);

				//samler bold op
				dos.write(51);				
				dos.flush();
				dos.write(51);
				dos.flush();	
				Thread.sleep(2000);
			}
		}
		catch(Exception ex){System.out.println(ex);}
	}

}



