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
	private int toGoal = 0;
	private Float ppcm;
	private DetectRects findEdge;
	private TakePicture takepic;
	private ballMethod balls;
	private CalcDist dist;
	private final OutputStream dos;
	private RouteTest route;
	private int moveBack = 0;

	public PrimaryController (DetectRects findEdge){
		this.findEdge = findEdge;
		takepic = new TakePicture();
		balls = new ballMethod();
		dist = new CalcDist();
		route = new RouteTest();

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

	public GUIInfo loopRound(GUIInfo calliData, int deliverButtom) {
	/*	char firstRun = 'a';
		int ballCount = 0;
		int count = 0;
*/
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
		balls.calculateRotationPoint();
		
		
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
	

		minPunkt = route.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt, findEdge.getGoalA(), ppcm); // tegner dem i testprogrammet
		System.out.println("minpunkt = " + minPunkt.x() + " " +minPunkt.y());

		//				##########################################################################
		
		
		int intppcm = (int)(Math.round(ppcm));
		//CvPoint middle = new CvPoint(findEdge.getGoalB().x()+(90*intppcm),findEdge.getGoalB().y()); // in the middle of field
//CvPoint corner3 = new CvPoint(findEdge.getGoalB().x(),findEdge.getGoalB().y()+(60*intppcm));//3
		CvPoint corner1 = new CvPoint(findEdge.getGoalB().x(),findEdge.getGoalB().y()-(60*intppcm));//1
		CvPoint corner4 = new CvPoint(findEdge.getGoalA().x(),findEdge.getGoalA().y()+(60*intppcm));//4
		//CvPoint corner2 = new CvPoint(findEdge.getGoalA().x(),findEdge.getGoalA().y()-(60*intppcm));//2 

		int l1 = corner1.y()+(int)(5*ppcm); 
		int l2 = corner4.y()-(int)(5*ppcm);
		int l3 = corner1.x()+(int)(5*ppcm);
		int l4 = corner4.x()-(int)(5*ppcm);
		
		
		
		/*
		1 												2
		 -------------------------------------------|
		 |											|
		 |											|
		 |											|
		 |											|
		 |					X = middle				|
		 |											|
		 |											|
		 |											|
		 |											|
		 -------------------------------------------|	
		 3												4
		 */
/*
		System.out.println(corner1.x() + " corner1 " + corner1.y());
		System.out.println(corner2.x() + " corner2 " + corner2.y());
		System.out.println(corner3.x() + " corner3 " + corner3.y());
		System.out.println(corner4.x() + " corner4 " + corner4.y());
		System.out.println(minPunkt.x() + " minPunkt " + minPunkt.y());
		if(minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (18*intppcm) && minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (18*intppcm) && moveBack == 0){
			minPunkt = new CvPoint (minPunkt.x()+(25*intppcm),minPunkt.y()+(25*intppcm));
			System.out.println("corner1");
			moveBack = 1;
			}
		else if(minPunkt.x() < corner2.x() && minPunkt.x() > corner2.x() - (18*intppcm) && minPunkt.y() > corner2.y() && minPunkt.y() < corner2.y() + (18*intppcm)&& moveBack == 0){
			minPunkt = new CvPoint (minPunkt.x()-(25*intppcm),minPunkt.y()+(25*intppcm));
			System.out.println("corner2");
			moveBack = 1;
			}
		else if(minPunkt.x() > corner3.x() && minPunkt.x() < corner3.x() + (100*intppcm) && minPunkt.y()-10 < corner3.y() && minPunkt.y() > corner3.y() - (18*intppcm)&& moveBack == 0){
			minPunkt = new CvPoint (minPunkt.x()+(25*intppcm),minPunkt.y()-(25*intppcm));
			System.out.println("corner3");
			moveBack = 1;
			}
		else if(minPunkt.x() < corner4.x() && minPunkt.x() > corner4.x() - (18*intppcm) && minPunkt.y() < corner4.y() && minPunkt.y() > corner4.y() - (18*intppcm)&& moveBack == 0){
			minPunkt = new CvPoint (minPunkt.x()-(25*intppcm),minPunkt.y()-(25*intppcm));
			System.out.println("corner4");
			moveBack = 1;
			}
		else if(moveBack == 1)moveBack++;
		else moveBack = 0;
		
		if(minPunkt.x() > corner1.x() + (18*intppcm) && minPunkt.x() < corner2.x() - (18*intppcm)
				&& minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (18*intppcm))
		{
			minPunkt = new CvPoint(minPunkt.x(), minPunkt.y()+(36*intppcm));
			System.out.println("side A");
		}
		else if(minPunkt.x() > corner3.x() + (18*intppcm) && minPunkt.x() < corner4.x() - (18*intppcm) 
				&& minPunkt.y() > corner3.y() && minPunkt.y() < corner3.y() - (18*intppcm))
		{
			minPunkt = new CvPoint(minPunkt.x(), minPunkt.y()-(36*intppcm));
			System.out.println("side B");
		}
		else if(minPunkt.x() > corner1.y() + (18*intppcm) && minPunkt.y() < corner3.y() - (18*intppcm) 
				&& minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (18*intppcm)) 
		{
			minPunkt = new CvPoint(minPunkt.y(), minPunkt.x()+(36*intppcm));
			System.out.println("side C");
		}
		else if(minPunkt.x() > corner2.y() + (18*intppcm) && minPunkt.y() < corner4.y() - (18*intppcm) 
				&& minPunkt.x() > corner2.x() && minPunkt.x() < corner2.x()-(18*intppcm))
		{
			minPunkt = new CvPoint(minPunkt.y(), minPunkt.x()-(36*intppcm));
			System.out.println("side D");
		}
		*/
/*
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

				goalA = findEdge.getGoalA();

				minPunkt.x(goalA.x());
				minPunkt.y(goalA.y());
				count = 0;
				System.out.println("koordinaterne til Minpunkt er ("+ minPunkt.x() + "," + minPunkt.y() + ")");

			}
		}
*/
		//				############################# Calc Angle ###################
		String text1 = "Antal bolde fundet: " + (ballCoor.size() / 3);
		JTextArea txtArea1 = new JTextArea(text1, 1, 1);
		calliData.setTxtArea1(txtArea1);

		JLabel lbltxt = new JLabel();
		lbltxt.setText(text1);

		calliData.setLbltxt(lbltxt);



		//				#############################################################
		
		if(deliverButtom == 1){
			deliverBalls(calliData);
		}
		else{
			if(minPunkt.x()<l3 || minPunkt.x()>l4 || minPunkt.y()<l1 || minPunkt.y()>l2){
				// bold under L1!
				CvPoint tempPoint  = new CvPoint(balls.getRoboBagPunkt().x()-(balls.getRoboBagPunkt().x()-minPunkt.x()),balls.getRoboBagPunkt().y());
				angleCal(calliData, tempPoint);
				route.setMinLength(Math.abs(balls.getRoboBagPunkt().x()-minPunkt.x()));
				send(calliData);
				calliData.setTurnAngle(90F);
				route.setMinLength(Math.abs(balls.getRoboBagPunkt().y()-minPunkt.y()));
				moveBack = 1;
				
			}
			else
			{
				angleCal(calliData, minPunkt);
			}

			send(calliData); 

		}
		
		return calliData;
	}

	public void angleCal(GUIInfo calliData, CvPoint destination) {
		CalcAngle Angle = new CalcAngle();
		
		CvPoint nyRoboFront = new CvPoint(balls.getRoboFrontPunkt().x()
				- balls.getRoboBagPunkt().x(), balls.getRoboFrontPunkt().y()
				- balls.getRoboBagPunkt().y());
		CvPoint nyRoboBag = new CvPoint(0, 0);
		CvPoint nyMinPunkt = new CvPoint(destination.x()- balls.getRoboBagPunkt().x(), destination.y()- balls.getRoboBagPunkt().y());	

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
			/*
			if(toGoal < 1){
			dos.write(61); // sender case
			dos.flush();
			dos.write(61); // sender vinkel
			dos.flush();
			Thread.sleep(500);
			}*/
			
			
			
			
			// kører robot frem

		
			
		
			System.out.println("Lenghtmulti " + calliData.getlengthMultiply());
			System.out.println("minmulti " + route.getMinLength());
			System.out.println("ppcm  " + findEdge.getPixPerCm());

			int distance = (int) ((route.getMinLength() * Math.round(calliData.getlengthMultiply()) / findEdge.getPixPerCm())); // længde konvertering

			System.out.println("dist = " + distance);
		
			distance -= 6*ppcm; // for at lande foran bolden
			Thread.sleep(600);
			/*
			

			
			dos.write(91);
			dos.flush();
			dos.write(91);//random tal
			dos.flush();
			
			 */
			
			dos.write(81);
			dos.flush();
			if (angle > 180)
				distance -= 30;
			i = distance / 10;
			dos.write(i);
			dos.flush();
			
		
			Thread.sleep((int) Math.round((Float.parseFloat("" +route.getMinLength())) * Float.parseFloat("" +calliData.getclose())));
			/*
			if(toGoal < 1){
			// samler bold op
			dos.write(51);
			dos.flush();
			dos.write(51);
			dos.flush();
			Thread.sleep(1200);
			}
			*/
			if(toGoal < 1){
				// samler bold op
				dos.write(41);
				dos.flush();
				dos.write(41);
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
			
			if(moveBack == 1){
				dos.write(80);
				dos.flush();
				dos.write(5);
				dos.flush();
				Thread.sleep(1200);
				moveBack = 0;
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void deliverBalls(GUIInfo calliData) {
		if(toGoal == 0){toGoal = 1;
		goalA = findEdge.getGoalA();
		minPunkt.x(goalA.x()-500);
		minPunkt.y(goalA.y());
		}
		else{ toGoal = 2;
		minPunkt.x(goalA.x()-180);

		minPunkt.y(goalA.y());
		}
		angleCal(calliData, minPunkt);
	}
}
