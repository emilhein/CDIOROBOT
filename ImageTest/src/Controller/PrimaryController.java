package Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

import data.GUIInfo;
import dist.CalcAngle;
import dist.CalcDist;
import pictureToMat.DetectRects;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;
import pictureToMat.ballMethod;

public class PrimaryController {
	CvPoint goalA;
	CvPoint minPunkt;
	private Float minLength;
	private int toGoal = 0;
	private Float ppcm;
	private DetectRects findEdge;
	private TakePicture takepic;
	private ballMethod balls;
	private CalcDist dist;
	final OutputStream dos;

	public PrimaryController (DetectRects findEdge){
		this.findEdge = findEdge;
		takepic = new TakePicture();
		balls = new ballMethod();
		dist = new CalcDist();

		NXTInfo nxtInfo = new NXTInfo(2, "G9 awesome!", "0016530918D4");
		NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");//robot nr 2
		NXTConnector connt = new NXTConnector();
//		System.out.println("trying to connect");
		connt.connectTo(nxtInfo, NXTComm.LCP);
//		System.out.println("connected"); // forbundet
		// åbner streams}
		dos = connt.getOutputStream();
		
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

				goalA = findEdge.getGoalB();

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


		angleCal(calliData, nyRoboFront, nyRoboBag, nyMinPunkt);
		//				###########################################################

		calliData.setMinLength(Math.abs(dist.Calcdist(roboFrontPunkt, minPunkt)));


		//				#############################################################

		
		//if("You want to party")deliverBalls(calliData, nyRoboFront, nyRoboBag, nyMinPunkt);
		//else{
		
		send(calliData); 
		//}
		
		return calliData;
	}

	public void angleCal(GUIInfo calliData, CvPoint nyRoboFront,
			CvPoint nyRoboBag, CvPoint nyMinPunkt) {
		CalcAngle Angle = new CalcAngle();
		Float BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		//System.out.println("BallAngle = " + BallAngle);
		Float RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		//System.out.println("RoboAngle = " + RoboAngle);
		calliData.setTurnAngle(BallAngle-RoboAngle);

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
	}

	public void send(GUIInfo calliData) {
		int Case;
		int i;
		System.out.println("TurnAngle = " + calliData.getTurnAngle());

		int angle = (int)Math.round(Float.parseFloat("" + calliData.getTurnAngle()));// * (Float.parseFloat(jl17.getText()))); // vinkel

		// konvertering
		System.out.println("angle " + angle);
		
		System.out.println("turnAngle" + angle);

		try {
			if (Math.abs(angle) < 250) {
				if (angle > 0) // vælger retning der skal drejes
					Case = 21;
				else
					Case = 12;
			} else {
				angle = angle / 2;
				if (angle > 0) // vælger retning der skal drejes
					Case = 21;
				else
					Case = 12;
				dos.write(Case); // sender case
				dos.flush();
				dos.write(angle); // sender vinkel
				dos.flush();
				Thread.sleep(700);
			}
			angle = Math.abs(angle);

			dos.write(Case); // sender case
			dos.flush();
			dos.write(angle); // sender vinkel
			dos.flush();

			Thread.sleep(1200);
			if(toGoal < 1){
			dos.write(61); // sender case
			dos.flush();
			dos.write(61); // sender vinkel
			dos.flush();
			Thread.sleep(500);
			}
			// kører robot frem

		
			
			minLength = calliData.getMinLength();
			System.out.println("Lenghtmulti " + calliData.getlengthMultiply());
			System.out.println("minmulti " + calliData.getMinLength());
			System.out.println("ppcm  " + findEdge.getPixPerCm());

			int distance = (int) ((minLength * Math.round(calliData.getlengthMultiply()) / findEdge.getPixPerCm())); // længde konvertering

			System.out.println("dist = " + distance);
		

			
			dos.write(81);
			dos.flush();
			if (angle > 180)
				distance -= 30;
			i = distance / 10;
			dos.write(i);
			dos.flush();

		
			Thread.sleep((int) Math.round((Float.parseFloat("" +calliData.getMinLength())) * Float.parseFloat("" +calliData.getclose())));

			if(toGoal < 1){
			// samler bold op
			dos.write(51);
			dos.flush();
			dos.write(51);
			dos.flush();
			Thread.sleep(1200);
			}
			
			if(toGoal == 2){
				dos.write(71);
				dos.flush();
				dos.write(71);
				dos.flush();
				Thread.sleep(1200);
				toGoal = 0;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void deliverBalls(GUIInfo calliData, CvPoint nyRoboFront, CvPoint nyRoboBag, CvPoint nyMinPunkt) {
		if(toGoal == 0){toGoal = 1;
		
		minPunkt.x(goalA.x()-500);
		minPunkt.y(goalA.y());
		}
		else{ toGoal = 2;
		minPunkt.x(goalA.x()-100);
		minPunkt.y(goalA.y());
		}
		angleCal(calliData, nyRoboFront, nyRoboBag, nyMinPunkt);
	}
}
