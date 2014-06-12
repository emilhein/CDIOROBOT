package Controller;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

import data.GUIInfo;
import dist.CalcAngle;
import dist.CalcDist;
import pictureToMat.DetectRects;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;
import pictureToMat.ballMethod;

public class PrimaryController {

	private Float ppcm;
	private DetectRects findEdge;
	private TakePicture takepic;
	private ballMethod balls;
	private CalcDist dist;

	public PrimaryController (DetectRects findEdge){
		this.findEdge = findEdge;
		takepic = new TakePicture();
		balls = new ballMethod();
		dist = new CalcDist();
	}

	public void start() {
		takepic.takePicture();
		findEdge.detectAllRects();
		ppcm = findEdge.getPixPerCm();
	}

	public GUIInfo loopRound(GUIInfo calliData) {
		char firstRun = 'a';
		int ballCount = 0;
		int count = 0;


		do {

						takepic.takePicture();	

			//				#################  Pic to Mat  ############

			balls.pictureToMat("billed0.png");

			//				################### Find Robot #######################################

			balls.findCircle(

					calliData.getIntJlroboMin(),
					calliData.getIntJlroboMax(),
					calliData.getIntJlroboDP(),
					calliData.getIntJlroboMinDist(),
					calliData.getIntJlroboPar1(),
					calliData.getIntJlroboPar2(), "robo", true);


		}
		while (balls.determineDirection()==false);

		balls.changePerspective(calliData.getPoV());
		
		
		//				################### Find Balls #####################################
		balls.findCircle(


				calliData.getIntJlcircleMinRadius(),
				calliData.getIntJlcircleMaxRadius(),
				calliData.getIntJlcircleDP(),
				calliData.getIntJlcircleDist(),
				calliData.getIntJlcirclePar1(),
				calliData.getIntJlcirclePar2(), "balls", false);

		ArrayList<Float> ballCoor = balls.getBallCoordi();

		//				################### Nearest Ball #######################################

		CvPoint roboBagPunkt = balls.getRoboBagPunkt();
		CvPoint roboFrontPunkt = balls.getRoboFrontPunkt();
		CvPoint minPunkt;

		minPunkt = RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet
		System.out.println("minpunkt = " + minPunkt.x() + " " +minPunkt.y());

		//				##########################################################################
		if(firstRun == 'a'){
			ballCount = (ballCoor.size() / 3);
			firstRun = 'b';
		}
		int tempCount = (ballCoor.size() / 3);
		//System.out.println("tempcount = " + tempCount);
		//System.out.println("Ballcount = " + ballCount);

		if (tempCount == ballCount - 1) {
			System.out.println("HEJ1");
			count++;
			ballCount = tempCount;
			if (count == 2) {
				System.out.println("HEJ2");

				CvPoint goalA = findEdge.getGoalB();

				minPunkt.x(goalA.x());
				minPunkt.y(goalA.y());
				count = 0;
				System.out.println("koordinaterne til Minpunkt er ("+ minPunkt.x() + "," + minPunkt.y() + ")");

			}
		}

		//				############################# Calc Angle ###################
		String text1 = "Antal bolde fundet: " + (ballCoor.size() / 3);
		JTextArea txtArea1 = new JTextArea(text1, 1, 1);
		calliData.setTxtArea1(txtArea1);

		JLabel lbltxt = new JLabel();
		lbltxt.setText(text1);

		calliData.setLbltxt(lbltxt);

		CvPoint nyRoboFront = new CvPoint(roboFrontPunkt.x()
				- roboBagPunkt.x(), roboFrontPunkt.y()
				- roboBagPunkt.y());
		CvPoint nyRoboBag = new CvPoint(0, 0);
		CvPoint nyMinPunkt = new CvPoint(minPunkt.x()- roboBagPunkt.x(), minPunkt.y()- roboBagPunkt.y());	


		CalcAngle Angle = new CalcAngle();
		Float BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		//System.out.println("BallAngle = " + BallAngle);
		Float RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		//System.out.println("RoboAngle = " + RoboAngle);
		calliData.setTurnAngle(RoboAngle - BallAngle);

		if(calliData.getTurnAngle() > 180)
		{
			calliData.setTurnAngle(calliData.getTurnAngle()-360);
		}
		if(calliData.getTurnAngle() < -180)
		{
			calliData.setTurnAngle(calliData.getTurnAngle()+360);
		}
		
		calliData.setBallAngle(BallAngle);
		calliData.setRoboAngle(RoboAngle);
		//				###########################################################

		calliData.setMinLength(Math.abs(dist.Calcdist(roboFrontPunkt, minPunkt)));


		//				#############################################################

		// ballCoor.clear();

		/*
		  System.out.println("In CONNECT");


		  System.out.println("Waiting for your go!");

		//  Scanner scan = new Scanner(System.in); int input =
		//  scan.nextInt();
		  try {
			Thread.sleep(5000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		  int Case; int i; System.out.println("TurnAngle = " +
		  TurnAngle); int angle = (int)
		  (TurnAngle*(Float.parseFloat(jl17.getText()))); //vinkel konvertering 
		  if(Math.abs(angle) < 250){ if(angle > 0) //vælger retning der  skal drejes 
			  Case = 11; 
		  else Case = 22; } 
		  else{ angle =
		  angle/10; 
		  if(angle > 0) //vælger retning der skal drejes Case
		  Case = 31;
		  else Case = 42; } 
		  angle = Math.abs(angle);

		  try {
		  dos.write(Case); //sender case 
		  dos.flush(); 
		  dos.write(angle);
		  //sender vinkel 
		  dos.flush();

		  Thread.sleep(1500); dos.write(61); //sender case dos.flush();
		  dos.write(61); //sender vinkel dos.flush();
		  Thread.sleep(500);

		  //kører robot frem System.out.println("minlength " +minLength); 
		  int distance =   (int)((minLength*(Float.parseFloat(jl18.getText())))/ppcm);
		  //længde konvertering System.out.println("dist = " + distance); dos.write(81); dos.flush(); if(angle > 180)
		  distance -= 50; i = distance/10; dos.write(i); dos.flush();

		  Thread.sleep((int)(minLength*(Float.parseFloat(jl19.getText())
		  ))); //Thread.sleep((int)minLength*2); //samler bold op
		  dos.write(51); dos.flush(); dos.write(51); dos.flush();


		  } catch (IOException e1) { // TODO Auto-generated catch block
		  e1.printStackTrace(); } 
		  catch (InterruptedException e1) { //
		  }

		  }*/

		return calliData;
	}



}
