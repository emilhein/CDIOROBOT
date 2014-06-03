package main;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import dist.CalcAngle;
import dist.CalcDist;
import dist.Punkt;
import pictureToMat.DetectBorder;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;
import pictureToMat.ballMethod;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Main {

	public static void main(String[] args) {
		Punkt minPunkt;

		TakePicture takepic = new TakePicture(); //tager et billed og gemmer i roden af projektet.
		takepic.takePicture();
		DetectBorder findEdge = new DetectBorder();
		try {

			findEdge.getRectCoordis("Billed0.png");
		} catch (IOException e) {
			System.out.println("WIHIIHIHHIIH");
		}

		float ppcm = (int) findEdge.getPixPerCm(); 
		ballMethod balls = new ballMethod();




		/*
		 *Standardværdier for disse argumenter plejer at være 4,8,19 eller 30,40,2
		 */

		float[] RoboCoor = balls.findCircle(6,13,2,2,1,50,2,"RoboMain",true);//minradius, maxrdius, antalbolde


		Mat frame = Highgui.imread("AfterColorConvert.png"); // henter det konverterede billlede
		for(int i = 0; i < RoboCoor.length;i=i+3){
			System.out.println("Bold nr " + i +" ligger på "+Math.round(RoboCoor[i]) + ","+Math.round(RoboCoor[i+1]));
		}

		double[] front = frame.get(Math.round(RoboCoor[1]), Math.round(RoboCoor[0])); ///X OG Y ER FUCKED
		//double red = front[2]; //henter en rød farver fra den ene cirkel
		double red = front[2];

		double[] back = frame.get(Math.round(RoboCoor[4]), Math.round(RoboCoor[3])); /// X OG Y ER FUCKED
		double red2 = back[2]; // henter en rød farve ([2]) fra den anden cirkel

		Punkt roboFrontPunkt = new Punkt(0,0);
		Punkt roboBagPunkt = new Punkt(0,0);
		// herunder sættes robotpunket, alt efter hvilken cirkel der er rød.
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
			System.out.println("Dette er rød1 farven = " + red);
			System.out.println("Dette er rød2 farven = " + red2);
		 */


		float[] ballCoor = balls.findCircle(2, 6,2,1,50,5, 3,"ballMain",false); // finder bolde 6,12,6


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
			//prøver at forbinde til vores robot
			NXTInfo nxtInfo = new NXTInfo(2,"G9 awesome!","0016530918D4");
			NXTConnector connt = new NXTConnector();
			System.out.println("trying to connect");
			connt.connectTo(nxtInfo, NXTComm.LCP);
			System.out.println("connected");		//forbundet
			//åbner streams
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
				if(Math.abs(angle) < 250){
					if(angle > 0) 				//vælger retning der skal drejes
						Case = 11;				
					else Case = 22;
				}
				else{
					if(angle > 0) 				//vælger retning der skal drejes
						Case = 31;				
					else Case = 42;
				}
				angle = Math.abs(angle);
				dos.write(Case);			//sender case
				dos.flush();
				dos.write(angle);			//sender vinkel
				dos.flush();

				//				//venter på at motorerne ikke kører længere
				//				int u = dis.read();			
				//				while(u==1){
				//					u = dis.read();
				//				}

				Thread.sleep(2000);
				//kører robot frem
				int distance = (int)((minLength/2)/ppcm);	//længde konvertering
				System.out.println("dist = " + distance);
				dos.write(81);
				dos.flush();
				i = distance;
				dos.write(i);
				dos.flush();

				//				//venter på at motorerne ikke kører længere
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
