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
	CvPoint tempGoal;
	private int toGoal = 0;
	private Float ppcm;
	private DetectRects findEdge;
	private TakePicture takepic;
	private ballMethod balls;
//	private CalcDist dist;
	private final OutputStream dos;
	private RouteTest route;
	private int moveBack = 0;
	private int backMove = 0;
	private int ifTemp = 0;

	public PrimaryController(DetectRects findEdge) {
		this.findEdge = findEdge;
		takepic = new TakePicture();
		balls = new ballMethod();
	//	dist = new CalcDist();
		route = new RouteTest();

		NXTInfo nxtInfo = new NXTInfo(2, "G9 awesome!", "0016530918D4");
		NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");// robot nr
																	// 2
		NXTConnector connt = new NXTConnector();
		System.out.println("trying to connect");
		connt.connectTo(nxtInfo, NXTComm.LCP);
		 System.out.println("connected"); // forbundet
		// åbner streams}
		dos = connt.getOutputStream();
	}

	public void start() {

		takepic.takePicture();
		findEdge.detectAllRects();
		ppcm = findEdge.getPixPerCm();
		findEdge.findMiners();
		findEdge.findMajors();

	}

	public GUIInfo loopRound(GUIInfo calliData, int deliverButtom) {
		/*
		 * char firstRun = 'a'; int ballCount = 0; int count = 0;
		 */
		
		//################# Calculate corners ########################
		
		int intppcm = (int)(Math.round(ppcm));
		//CvPoint middle = new CvPoint(findEdge.getGoalB().x()+(90*intppcm),findEdge.getGoalB().y()); // in the middle of field
		CvPoint corner3 = new CvPoint(findEdge.getGoalB().x(),findEdge.getGoalB().y()+(60*intppcm));//3
		CvPoint corner1 = new CvPoint(findEdge.getGoalB().x(),findEdge.getGoalB().y()-(60*intppcm));//1
		CvPoint corner4 = new CvPoint(findEdge.getGoalA().x(),findEdge.getGoalA().y()+(60*intppcm));//4
		CvPoint corner2 = new CvPoint(findEdge.getGoalA().x(),findEdge.getGoalA().y()-(60*intppcm));//2 
		
		System.out.println("Corner1: (" + corner1.x() + "," + corner1.y() + ")");
		System.out.println("Corner2: (" + corner2.x() + "," + corner2.y() + ")");
		System.out.println("Corner3: (" + corner3.x() + "," + corner3.y() + ")");
		System.out.println("Corner4: (" + corner4.x() + "," + corner4.y() + ")");

		
		//################## Take picture until robot is found #########
		do {
			takepic.takePicture();	

			// ################# Pic to Mat ############

			balls.pictureToMat("billed0.png");

			// ################### Find Robot #######################################

			balls.findCircle(

			calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),
					calliData.getIntJlroboDP(),
					calliData.getIntJlroboMinDist(),
					calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),
					"robo", true, corner1, corner4);

		} while (balls.determineDirection() == false);

		// ################### Find Balls #####################################
		balls.findCircle(

		calliData.getIntJlcircleMinRadius(),
				calliData.getIntJlcircleMaxRadius(),
				calliData.getIntJlcircleDP(), calliData.getIntJlcircleDist(),
				calliData.getIntJlcirclePar1(), calliData.getIntJlcirclePar2(),
				
				"balls", false, corner1, corner4);

		ArrayList<Float> ballCoor = balls.getBallCoordi();

		// ################### Nearest Ball
		
		balls.changePerspective(calliData.getPoV());
		balls.calculateRotationPoint();

		CvPoint roboBagPunkt = balls.getRoboBagPunkt();
		CvPoint roboFrontPunkt = balls.getRoboFrontPunkt();

		System.out.println("coordinates 0 := +" + findEdge.getNorth().x()+findEdge.getNorth().y()+findEdge.getSouth().x()+findEdge.getSouth().y()+findEdge.getEast().x()+findEdge.getEast().y()+findEdge.getWest().x()+findEdge.getWest().y());

		minPunkt = route.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt,
				findEdge.getGoalA(), ppcm,findEdge.getNorth(),findEdge.getSouth(), findEdge.getEast(), findEdge.getWest()); // tegner dem i testprogrammet
		System.out.println("minpunkt = " + minPunkt.x() + " " + minPunkt.y());

		
		// ############################# Calc Angle ###################
		String text1 = "Antal bolde fundet: " + (ballCoor.size() / 3);
		JTextArea txtArea1 = new JTextArea(text1, 1, 1);
		calliData.setTxtArea1(txtArea1);

		JLabel lbltxt = new JLabel();
		lbltxt.setText(text1);

		calliData.setLbltxt(lbltxt);
			
		System.out.println("1");
		if(deliverButtom == 1){
			System.out.println("2");
			if(toGoal == 0){
				System.out.println("3");
				toGoal = 1;
				goalA = findEdge.getGoalA();

				minPunkt.x(goalA.x() - 500);
				minPunkt.y(goalA.y());
				tempGoal = new CvPoint(minPunkt.x(), minPunkt.y());
				angleCal(calliData, tempGoal);
				CalcDist dist = new CalcDist();
				route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, tempGoal)));
				System.out.println("4");
				send(calliData);
				System.out.println("5");
			} else {
				System.out.println("6");
				toGoal = 2;

				minPunkt.x(goalA.x() - 180);
				minPunkt.y(goalA.y());
				int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2.4);
				int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2.4);
				roboFrontPunkt.x(tempGoal.x());
				roboFrontPunkt.y(tempGoal.y());
				roboBagPunkt.x(tempGoal.x()+Math.abs(diffX));
				roboBagPunkt.y(tempGoal.y()+Math.abs(diffY));
				System.out.println("front " + roboFrontPunkt.x() + " " + roboFrontPunkt.y());
				System.out.println("bag " + roboBagPunkt.x() + " " + roboBagPunkt.y());
				CvPoint tempGoal = new CvPoint(minPunkt.x(), minPunkt.y());
				angleCal(calliData, tempGoal);
				CalcDist dist = new CalcDist();
				route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, minPunkt)));
				System.out.println("7");
				send(calliData);
				System.out.println("7");

			}
			System.out.println("9");
			angleCal(calliData, minPunkt);
			System.out.println("10");
		}

		// ***************************** Avoid edge*******************************
		
		System.out.println("obstruction is at: " + findEdge.getObstruction().x() +"," + findEdge.getObstruction().y());

		int l1 = corner1.y()+(int)(5*ppcm); 
		int l2 = corner4.y()-(int)(5*ppcm);
		int l3 = corner1.x()+(int)(5*ppcm);
		int l4 = corner4.x()-(int)(5*ppcm);

/*

		if (minPunkt.x() < l1 || minPunkt.x() > l4 || minPunkt.y() < l3|| minPunkt.y() > l2) {
			// bold under L1!
			CvPoint tempPoint = new CvPoint(balls.getRoboBagPunkt().x()	- (balls.getRoboBagPunkt().x() - minPunkt.x()), balls.getRoboBagPunkt().y());
			angleCal(calliData, tempPoint);
			route.setMinLength(Math.abs(balls.getRoboBagPunkt().x()	- tempPoint.x()));
			ifTemp = 1;
			System.out.println("Før send 1");
			send(calliData); // robot skal gerne være kørt til temporary point (vinkelret på bolden og kanten)
			calliData.setTurnAngle(90F);

			if( minPunkt.y() > l2){
				System.out.println("BALL IS IN L2");
				calliData.setTurnAngle(-90F);
			}
			if( minPunkt.y() > l3){
				System.out.println("BALL IS IN L3");
				calliData.setTurnAngle(-90F);
			}
			if( minPunkt.x() > l4){
				System.out.println("BALL IS IN L4");
				calliData.setTurnAngle(-90F);
			}
			if( minPunkt.x() > l1){
				System.out.println("BALL IS IN L1");
				calliData.setTurnAngle(-90F);
			}
			
			calliData.setTurnAngle(90F);
			route.setMinLength(Math.abs(balls.getRoboBagPunkt().y()- minPunkt.y()));
			moveBack = 1;

		}*/
		// ***************************** Corner*******************************
		if(minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (18*intppcm) && minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (18*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()+(15*intppcm),minPunkt.y()+(15*intppcm));
			System.out.println("corner1"); 
			angleCal(calliData, tempPoint);
			CalcDist dist = new CalcDist();
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, tempPoint)));
			ifTemp = 1;
			send(calliData);
			int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2.4);
			int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2.4);
			roboFrontPunkt.x(tempPoint.x());
			roboFrontPunkt.y(tempPoint.y());
			roboBagPunkt.x(tempPoint.x()+Math.abs(diffX));
			roboBagPunkt.y(tempPoint.y()+Math.abs(diffY));
			System.out.println("front " + roboFrontPunkt.x() + " " + roboFrontPunkt.y());
			System.out.println("bag " + roboBagPunkt.x() + " " + roboBagPunkt.y());
			angleCal(calliData, minPunkt);
			calliData.setTurnAngle(calliData.getTurnAngle());
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, minPunkt)));
			backMove = 1; 
			ifTemp = 0;
			} 
		else if(minPunkt.x() < corner2.x() && minPunkt.x() > corner2.x() - (18*intppcm) && minPunkt.y() > corner2.y() && minPunkt.y() < corner2.y() +(18*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()-(15*intppcm),minPunkt.y()+(15*intppcm));
			System.out.println("corner2"); 
			angleCal(calliData, tempPoint);
			CalcDist dist = new CalcDist();
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, tempPoint)));
			ifTemp = 1;
			send(calliData);
			int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2.4);
			int diffY = (int) ((roboFrontPunkt.y()+roboBagPunkt.y())/2.4);
			roboFrontPunkt.x(tempPoint.x());
			roboFrontPunkt.y(tempPoint.y());
			roboBagPunkt.x(tempPoint.x()+Math.abs(diffX));
			roboBagPunkt.y(tempPoint.y()-Math.abs(diffY));
			System.out.println("front " + roboFrontPunkt.x() + " " + roboFrontPunkt.y());
			System.out.println("bag " + roboBagPunkt.x() + " " + roboBagPunkt.y());
			angleCal(calliData, minPunkt);
			calliData.setTurnAngle(calliData.getTurnAngle());
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, minPunkt)));
			backMove = 1; 
			ifTemp = 0;
			} 
		else if(minPunkt.x() > corner3.x() && minPunkt.x() < corner3.x() + (100*intppcm) &&	minPunkt.y()-10 < corner3.y() && minPunkt.y() > corner3.y() -(18*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()+(15*intppcm),minPunkt.y()-(15*intppcm));
			System.out.println("corner3"); 
			angleCal(calliData, tempPoint);
			route.setMinLength(Math.abs((balls.getRoboBagPunkt().x() - Math.abs(tempPoint.x()))));
			ifTemp = 1;
			send(calliData);
			int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2.4);
			int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2.4);
			roboFrontPunkt.x(tempPoint.x());
			roboFrontPunkt.y(tempPoint.y());
			roboBagPunkt.x(tempPoint.x()+Math.abs(diffX));
			roboBagPunkt.y(tempPoint.y()-Math.abs(diffY));
			System.out.println("front " + roboFrontPunkt.x() + " " + roboFrontPunkt.y());
			System.out.println("bag " + roboBagPunkt.x() + " " + roboBagPunkt.y());
			angleCal(calliData, minPunkt);
			calliData.setTurnAngle(calliData.getTurnAngle());
			route.setMinLength(Math.abs((balls.getRoboBagPunkt().x() - minPunkt.x())+65));
			backMove = 1; 
			ifTemp = 0;
			} 
		else if(minPunkt.x() <corner4.x() && minPunkt.x() > corner4.x() - (18*intppcm) && minPunkt.y() < corner4.y() && minPunkt.y() > corner4.y() - (18*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()-(15*intppcm),minPunkt.y()-(15*intppcm));
			System.out.println("corner4");
			angleCal(calliData, tempPoint);
			CalcDist dist = new CalcDist();
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, tempPoint)));
			ifTemp = 1;
			send(calliData);
			int diffX = (int) ((roboFrontPunkt.x()-roboBagPunkt.x())/2.4);
			int diffY = (int) ((roboFrontPunkt.y()-roboBagPunkt.y())/2.4);
			roboFrontPunkt.x(tempPoint.x());
			roboFrontPunkt.y(tempPoint.y());
			roboBagPunkt.x(tempPoint.x()-Math.abs(diffX));
			roboBagPunkt.y(tempPoint.y()-Math.abs(diffY));
			System.out.println("front " + roboFrontPunkt.x() + " " + roboFrontPunkt.y());
			System.out.println("bag " + roboBagPunkt.x() + " " + roboBagPunkt.y());
			angleCal(calliData, minPunkt);
			calliData.setTurnAngle(calliData.getTurnAngle());
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, minPunkt)));
			backMove = 1; 
			ifTemp = 0;
			} 
		
		else {
			backMove = 0;
			angleCal(calliData, minPunkt);
		}
		System.out.println("Før send 2");

		send(calliData);

		
		return calliData;
	}

	public void angleCal(GUIInfo calliData, CvPoint destination) {
		CalcAngle Angle = new CalcAngle();

		CvPoint nyRoboFront = new CvPoint(balls.getRoboFrontPunkt().x()
				- balls.getRoboBagPunkt().x(), balls.getRoboFrontPunkt().y()
				- balls.getRoboBagPunkt().y());
		CvPoint nyRoboBag = new CvPoint(0, 0);
		CvPoint nyMinPunkt = new CvPoint(destination.x()
				- balls.getRoboBagPunkt().x(), destination.y()
				- balls.getRoboBagPunkt().y());

		Float BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		// System.out.println("BallAngle = " + BallAngle);
		Float RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		// System.out.println("RoboAngle = " + RoboAngle);
		calliData.setTurnAngle(BallAngle - RoboAngle);

		if (calliData.getTurnAngle() > 180) {
			calliData.setTurnAngle(calliData.getTurnAngle() - 360);
		}
		if (calliData.getTurnAngle() < -180) {
			calliData.setTurnAngle(calliData.getTurnAngle() + 360);
		}

		calliData.setBallAngle(BallAngle);
		calliData.setRoboAngle(RoboAngle);
	}

	public void send(GUIInfo calliData) {
		int Case;
		int i;
		System.out.println("TurnAngle = " + calliData.getTurnAngle());

		int angle = (int) Math.round(Float.parseFloat(""
				+ calliData.getTurnAngle()));// *
												// (Float.parseFloat(jl17.getText())));
												// // vinkel

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
			// kører robot frem

			System.out.println("Lenghtmulti " + calliData.getlengthMultiply());
			System.out.println("minmulti " + route.getMinLength());
			System.out.println("ppcm  " + findEdge.getPixPerCm());

			int distance = (int) ((route.getMinLength()
					* Math.round(calliData.getlengthMultiply()) / findEdge
					.getPixPerCm())); // længde konvertering

			
			System.out.println("dist = " + distance);
			if(ifTemp == 0 ){
				System.out.println(" Efter iftemp");
				distance -= 6 * ppcm; // for at lande foran bolden
			}
			Thread.sleep(600);
			dos.write(81);
			dos.flush();
			if (angle > 180)
				distance -= 30;
			i = distance / 10;
			dos.write(i);
			dos.flush();
			System.out.println(" Efter kørfrem");

			Thread.sleep((int) Math.round((Float.parseFloat(""	+ route.getMinLength()))* Float.parseFloat("" + calliData.getclose())));
			System.out.println("Efter en wait");
			
			/*
			 * if(toGoal < 1){ // samler bold op dos.write(51); dos.flush();
			 * dos.write(51); dos.flush(); Thread.sleep(1200); }
			 */

				if (toGoal == 0 && ifTemp == 0) {
					// samler bold op
					dos.write(41);
					dos.flush();
					dos.write(41);
					dos.flush();
					Thread.sleep(1200);
					ifTemp = 0;
				}

			if (toGoal == 2) {
				dos.write(31);
				dos.flush();
				dos.write(31);
				dos.flush();
				Thread.sleep(1200);
				toGoal = 0;
			}

			if (moveBack == 1 || backMove ==1) {
				dos.write(80);
				dos.flush();
				dos.write(5);
				dos.flush();
				Thread.sleep(1200);
				moveBack = 0;
				backMove = 0;
			}
			System.out.println("I slut send");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	//public void deliverBalls(GUIInfo calliData) {
		// if(toGoal == 0){
		// toGoal = 1;
		// goalA = findEdge.getGoalA();
		// minPunkt.x(goalA.x()-500);
		// minPunkt.y(goalA.y());
		// }
		// else{ toGoal = 2;
		// minPunkt.x(goalA.x()-180);
		//
		// minPunkt.y(goalA.y());
		// }
		// angleCal(calliData, minPunkt);
	//}
}
