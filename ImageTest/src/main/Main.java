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

public class Main {

	public static void main(String[] args) {
		Punkt minPunkt;

		TakePicture.main(args); //tager et billed og gemmer i roden af projektet.

		ballMethod balls = new ballMethod();

		/*
		 *Standardværdier for disse argumenter plejer at være 4,8,19 eller 30,40,2
		 */
		float[] RoboCoor = balls.findCircle(17,20, 2);//minradius, maxrdius, antalbolde

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
			roboBagPunkt.setY(Math.round(RoboCoor[4]));
			System.out.println("red");
		} else if (red2 > 253){
			roboFrontPunkt.setX(Math.round(RoboCoor[3]));
			roboFrontPunkt.setY(Math.round(RoboCoor[4]));
			roboBagPunkt.setX(Math.round(RoboCoor[0]));
			roboBagPunkt.setY(Math.round(RoboCoor[1]));
			System.out.println("red2");
		}

		float[] ballCoor = balls.findCircle(6, 12, 6); // finder bolde 6,12,6
		minPunkt = RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet

		System.out.println("koordinaterne til frontpunkt er (" + roboBagPunkt.getX() +","+roboBagPunkt.getY()+")");
		System.out.println("koordinaterne til bagpunkt er (" + roboFrontPunkt.getX() +","+roboFrontPunkt.getY()+")");
		
		Punkt nyRoboFront = new Punkt(roboFrontPunkt.getX()-roboBagPunkt.getX(),roboFrontPunkt.getY()-roboBagPunkt.getY());
		Punkt nyRoboBag = new Punkt(0,0);
		Punkt nyMinPunkt = new Punkt(minPunkt.getX()-roboBagPunkt.getX(),minPunkt.getY()-roboBagPunkt.getY());
		
		CalcAngle Angle = new CalcAngle();
		int BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		int RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		int TurnAngle = BallAngle - RoboAngle;
		
		CalcDist dist = new CalcDist();
		int minLength = dist.Calcdist(roboFrontPunkt, minPunkt);
		
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
			
			int Case;
			int i;
			int angle = TurnAngle/5;	//vinkel konvertering
			if(angle < 0) 				//vælger retning der skal drejes
				Case = 11;				
			else Case = 22;

			dos.write(Case);			//sender case
			dos.flush();
			dos.write(angle);			//sender vinkel
			dos.flush();

			//venter på at motorerne ikke kører længere
			int u = dis.read();			
			while(u==1){
				u = dis.read();
			}

			//kører robot frem
			int distance = (minLength * 3) - 150;	//længde konvertering
			dos.write(51);
			dos.flush();
			i = distance;
			dos.write(i);
			dos.flush();

			//samler bold op
			dos.write(31);				
			dos.flush();
			dos.write(31);
			dos.flush();			

		}
		catch(Exception ex){System.out.println(ex);}
	}
}


