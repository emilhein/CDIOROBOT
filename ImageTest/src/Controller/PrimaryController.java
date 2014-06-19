package Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import CallibratorGUI.CallibratorGUI;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

import data.GUIInfo;
import data.Pitch;
import dist.CalcAngle;
import dist.CalcDist;
import pictureToMat.DetectRects;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;
import pictureToMat.BallMethod;

public class PrimaryController {
	private CvPoint goalB;
	private CvPoint minPunkt;
	private CvPoint roboBagPunkt;
	private CvPoint roboFrontPunkt;
	private int toGoal = 0;
	private Float ppcm;
	private DetectRects findEdge;
	private TakePicture takepic;
	private BallMethod balls;
	private CalcDist dist;
	private final OutputStream dos;
	private RouteTest route;
	private int moveBack = 0;
	private int backMove = 0;
	private int ifTemp = 0;
	private Pitch pitch;
	private int behindCross = 0;
	private int movingAround = 0;
	
	public PrimaryController(DetectRects findEdge) {
		this.findEdge = findEdge;
		takepic = new TakePicture();

		NXTInfo nxtInfo = new NXTInfo(2, "G9 awesome!", "0016530918D4");
		NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");// robot nr 2
																	
		NXTConnector connt = new NXTConnector();
		System.out.println("trying to connect");
		connt.connectTo(nxtInfo, NXTComm.LCP);
		System.out.println("connected"); // forbundet
		dos = connt.getOutputStream();// åbner stream
	}
	
	public void start() {
		long timePicStart = System.currentTimeMillis();
		takepic.takePicture();
		long timePicSlut = System.currentTimeMillis();
		System.out.println("take picture tid: " + (timePicSlut-timePicStart));
		
		long timeFindEdgeStart = System.currentTimeMillis();
		pitch = findEdge.detectPitch();
		long timeFindEdgeSlut = System.currentTimeMillis();
		System.out.println("find edge tid: " + (timeFindEdgeSlut-timeFindEdgeStart));
		
		ppcm = pitch.getPixPerCm();

		balls = new BallMethod(pitch);
		route = new RouteTest(pitch);
	}
	public GUIInfo loopRound(GUIInfo calliData, int deliverButtom) {

		/*
		 * char firstRun = 'a'; int ballCount = 0; int count = 0;
		 */
		int xFactorOfCut = 2;
		int yFactorOfCut = 4;

		CalcDist dist = new CalcDist();

		//################# Calculate corners ########################
		
		int intppcm = (int)(Math.round(ppcm));

		//################## Take picture until robot is found #########
		do {
			takepic.takePicture();	

			// ################## Cut image ####################################
			pitch.cutOrigImg();
			pitch.adjustToCut(xFactorOfCut, yFactorOfCut);

			// ################### Find Robot #######################################

			balls.findCircle(

			calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),
					calliData.getIntJlroboDP(),
					calliData.getIntJlroboMinDist(),
					calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),
					"robo", true);

		} while (balls.determineDirection() == false);

		balls.rotateRobot();
		//balls.eliminateObstruction();
		
		// ################### Find Balls #####################################
		balls.findCircle(

		calliData.getIntJlcircleMinRadius(),
				calliData.getIntJlcircleMaxRadius(),
				calliData.getIntJlcircleDP(), calliData.getIntJlcircleDist(),
				calliData.getIntJlcirclePar1(), calliData.getIntJlcirclePar2(),
				
				"balls", false);

		ArrayList<Float> ballCoor = balls.getBallCoordi();
		
		// ################### Nearest Ball
		balls.calculateRotationPoint();
		balls.changePerspective(calliData.getPoV());
		
		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();



		minPunkt = route.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet

		System.out.println("minpunkt = " + minPunkt.x() + " " + minPunkt.y());
		
		
		

		String text1 = "Antal bolde fundet: " + (ballCoor.size() / 3);
		JTextArea txtArea1 = new JTextArea(text1, 1, 1);
		calliData.setTxtArea1(txtArea1);

		JLabel lbltxt = new JLabel();
		lbltxt.setText(text1);

		calliData.setLbltxt(lbltxt);
			
		// ***************************** Deliver balls *******************************
		if(deliverButtom == 1){
			if(toGoal == 0){
				toGoal = 1;
				goalB = pitch.getGoalB();
				minPunkt.x(goalB.x()+400);
				minPunkt.y(goalB.y());
				System.out.println("minpunkt x,y: " +minPunkt.x() +","+minPunkt.y() );
				System.out.println("robobagpunkt x,y: " +roboBagPunkt.x() +","+roboBagPunkt.y() );
				CvPoint tempGoal = new CvPoint(minPunkt.x(), minPunkt.y());
				angleCal(calliData, tempGoal);
				float minl = dist.Calcdist(roboBagPunkt, minPunkt)+20 * ppcm;
				route.setMinLength(minl);
				//send(calliData);
			} else {
				toGoal = 2;

				minPunkt.x(goalB.x() + 120);
				minPunkt.y(goalB.y());

				CvPoint tempGoal2 = new CvPoint(minPunkt.x(), minPunkt.y());
				
				angleCal(calliData, tempGoal2);

				route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, tempGoal2)+6 * ppcm));
				
			}
		
//			angleCal(calliData, minPunkt);
	
			deliverBalls(calliData, dist);
		}
		
		// ***************************** Avoid cross *******************************
		int middelX = (pitch.getObstruction().position()+pitch.getObstruction().width());
		int middelY = (pitch.getObstruction().position()+pitch.getObstruction().height());
		if(behindCross == 1){
			findWayAround(calliData, dist, middelX, middelY);
		}		

		// ***************************** Avoid edge*******************************
		
		CvPoint corner1 = pitch.getCorner1();
		CvPoint corner2 = pitch.getCorner2();
		CvPoint corner3 = pitch.getCorner3();
		CvPoint corner4 = pitch.getCorner4();

		if (minPunkt.x() > corner1.x() + (int)(6 *ppcm)&& minPunkt.x() < corner2.x() - (int)(6 * ppcm)&& minPunkt.y() > corner1.y()	&& minPunkt.y() < corner1.y() + (int)(9 * ppcm)) 
		{
			CvPoint tempPoint = new CvPoint(minPunkt.x(),(minPunkt.y()+(int)(30*ppcm)));
			tempCalculater(calliData, dist, tempPoint);
			System.out.println("side A");
			
		} else if (minPunkt.x() > corner3.x() + (int)(6 * ppcm)	&& minPunkt.x() < corner4.x() - (int)(6 * ppcm) && minPunkt.y() < corner3.y() && minPunkt.y() > corner3.y() - (int)(9 * ppcm)) 
		{
			System.out.println("side B");
			CvPoint tempPoint = new CvPoint(minPunkt.x(),(minPunkt.y()-(int)(30*ppcm)));
			tempCalculater(calliData, dist, tempPoint);
		}
		 else if (minPunkt.y() > corner1.y() + (int)(6 * ppcm)	&& minPunkt.y() < corner3.y() - (int)(6 * ppcm)	&& minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (int)(9 * ppcm)) 
		{
			System.out.println("side C");
		} else if (minPunkt.y() > corner2.y() + (int) (6 * ppcm)&& minPunkt.y() < corner4.y() - (int) (6 * ppcm)&& minPunkt.x() < corner2.x()&& minPunkt.x() > corner2.x() - (int) (9 * ppcm)) {
			System.out.println("side D");
		}	
		
		// ***************************** Corner*******************************
		else if(minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (6*intppcm) && minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()+(20*intppcm),minPunkt.y()+(20*intppcm));
			System.out.println("corner1"); 
			tempCalculater(calliData, dist, tempPoint);
			} 
		else if(minPunkt.x() < corner2.x() && minPunkt.x() > corner2.x() - (6*intppcm) && minPunkt.y() > corner2.y() && minPunkt.y() < corner2.y() +(6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()-(20*intppcm),minPunkt.y()+(20*intppcm));
			System.out.println("corner2"); 
			tempCalculater(calliData, dist, tempPoint);
			} 
		else if(minPunkt.x() > corner3.x() && minPunkt.x() < corner3.x() + (10*intppcm) &&	minPunkt.y()-10 < corner3.y() && minPunkt.y() > corner3.y() -(6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()+(20*intppcm),minPunkt.y()-(20*intppcm));
			System.out.println("corner3"); 
			tempCalculater(calliData, dist, tempPoint);
			} 
		else if(minPunkt.x() <corner4.x() && minPunkt.x() > corner4.x() - (6*intppcm) && minPunkt.y() < corner4.y() && minPunkt.y() > corner4.y() - (6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()-(20*intppcm),minPunkt.y()-(20*intppcm));
			System.out.println("corner4");
			tempCalculater(calliData, dist, tempPoint);
			} 
		
		else {
			backMove = 0;
			angleCal(calliData, minPunkt); // udregner vinkel 
		}
		send(calliData);	
		return calliData;
	}
	private void deliverBalls(GUIInfo calliData, CalcDist dist) {
		if(toGoal == 0){
			toGoal = 1;
			goalB = pitch.getGoalB();
			minPunkt.x(goalB.x()+400);
			minPunkt.y(goalB.y());
			System.out.println("minpunkt x,y: " +minPunkt.x() +","+minPunkt.y() );
			System.out.println("robobagpunkt x,y: " +roboBagPunkt.x() +","+roboBagPunkt.y() );
			CvPoint tempGoal = new CvPoint(minPunkt.x(), minPunkt.y());
			angleCal(calliData, tempGoal);
			route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)+20 * ppcm);
		} else {
			toGoal = 2;
			minPunkt.x(goalB.x() + 120);
			minPunkt.y(goalB.y());
			CvPoint tempGoal2 = new CvPoint(minPunkt.x(), minPunkt.y());
			angleCal(calliData, tempGoal2);
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, tempGoal2)+6 * ppcm));	
		}
	}
	private void findWayAround(GUIInfo calliData, CalcDist dist, int middelX, int middelY) {
		if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() < middelY)||movingAround ==4){
			System.out.println("robo near corner 1, moving around cross");
			CvPoint tempPoint = new CvPoint (pitch.getNorth().x(),pitch.getNorth().y());
			aroundCross(calliData, dist, tempPoint);
			if(movingAround ==4)movingAround =0;
			else movingAround = 1;
		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() < middelY)||movingAround == 1){
			System.out.println("robo near corner 2, moving around cross");
			CvPoint tempPoint = new CvPoint (pitch.getEast().x(),pitch.getEast().y());
			aroundCross(calliData, dist, tempPoint);
			if(movingAround ==1)movingAround =0;
			else movingAround = 2;
		}
		else if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() > middelY)||movingAround ==2){
			System.out.println("robo near corner 3, moving around cross");
			CvPoint tempPoint = new CvPoint (pitch.getSouth().x(),pitch.getSouth().y());
			aroundCross(calliData, dist, tempPoint);
			if(movingAround ==2)movingAround =0;
			else movingAround = 3;
		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() > middelY)||movingAround ==3){
			System.out.println("robo near corner 4, moving around cross");
			CvPoint tempPoint = new CvPoint (pitch.getWest().x(),pitch.getWest().y());
			aroundCross(calliData, dist, tempPoint);
			if(movingAround ==3)movingAround =0;
			else movingAround = 4;
		}
	}
	public void aroundCross(GUIInfo calliData, CalcDist dist, CvPoint tempPoint) {
		angleCal(calliData, tempPoint);
		route.setMinLength(dist.Calcdist(roboBagPunkt, tempPoint)+6*ppcm);
		ifTemp = 1; //betyder den skal ikke grappe
		System.out.println("tempPunkt = " + tempPoint.x() + "," + tempPoint.y());
	}
	public void tempCalculater(GUIInfo calliData, CalcDist dist, CvPoint tempPoint) {
		angleCal(calliData, tempPoint);
		route.setMinLength(dist.Calcdist(roboBagPunkt, tempPoint)+6*ppcm);
		ifTemp = 1; //betyder den skal ikke grappe
		System.out.println("tempPunkt = " + tempPoint.x() + "," + tempPoint.y());
		send(calliData); // kører til første punkt
		calPosition(roboFrontPunkt,  roboBagPunkt, tempPoint); // udregner destination på robot efter den er kørt til temp
		angleCal(calliData, minPunkt);
		route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)-2*ppcm);
		ifTemp = 0;
		backMove = 1; 	
	}
	public void angleCal(GUIInfo calliData, CvPoint destination) {
		CalcAngle Angle = new CalcAngle();
		CvPoint nyRoboFront = new CvPoint(roboFrontPunkt.x()- roboBagPunkt.x(), roboFrontPunkt.y()- roboBagPunkt.y());
		CvPoint nyRoboBag = new CvPoint(0, 0);
		CvPoint nyMinPunkt = new CvPoint(destination.x()- roboBagPunkt.x(), destination.y()	- roboBagPunkt.y());

		Float BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		Float RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
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
		int angle = Math.round(Float.parseFloat(""+ calliData.getTurnAngle()));// *
		System.out.println("TurnAngle = " + calliData.getTurnAngle());
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
				angle = Math.abs(angle);
				i = angle;
				dosSend(Case, i);
				Thread.sleep(700);
			}
			angle = Math.abs(angle);
			i = angle;
			dosSend(Case, i);

			Thread.sleep(1200);
			// kører robot frem
/*
			System.out.println("Lenghtmulti " + calliData.getlengthMultiply());
			System.out.println("minmulti " + route.getMinLength());
			System.out.println("ppcm  " + findEdge.getPixPerCm());
*/
			int distance = (int) ((route.getMinLength()* Math.round(calliData.getlengthMultiply()) / pitch.getPixPerCm())); // længde konvertering
			
			System.out.println("dist = " + distance);
			if(ifTemp == 0){
				distance -= 6 * ppcm; // for at lande foran bolden
			}
			Thread.sleep(600);
			Case = 81;
			i = distance / 10;
			dosSend(Case, i);

			Thread.sleep((int) Math.round((Float.parseFloat(""	+ route.getMinLength()))* Float.parseFloat("" + calliData.getclose())));

			if (toGoal == 0 && ifTemp == 0) {
					// samler bold op
					Case = 41;
					i = 41;
					dosSend(Case, i);
					Thread.sleep(1200);
					ifTemp = 0;
				}

			if (toGoal == 2) {
				Case = 31;
				i = 31;
				dosSend(Case, i);
				Thread.sleep(1200);
				toGoal = 0;
			}

			if (moveBack == 1 || backMove ==1) {
				Case = 80;
				i = 80;
				dosSend(Case, i);
				Thread.sleep(1200);
				moveBack = 0;
				backMove = 0;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	public void dosSend(int Case, int i) throws IOException {
		dos.write(Case);
		dos.flush();
		dos.write(i);
		dos.flush();
	}
	public void calPosition(CvPoint roboFrontPunkt, CvPoint roboBagPunkt,CvPoint tempPoint) {
		int diffX = (roboBagPunkt.x()-roboFrontPunkt.x());
		int diffY = (roboBagPunkt.y()-roboFrontPunkt.y());
		roboFrontPunkt.x(tempPoint.x());
		roboFrontPunkt.y(tempPoint.y());
		roboBagPunkt.x(tempPoint.x()+diffX);
		roboBagPunkt.y(tempPoint.y()+diffY);
		System.out.println("front " + roboFrontPunkt.x() + " " + roboFrontPunkt.y());
		System.out.println("bag " + roboBagPunkt.x() + " " + roboBagPunkt.y());
			
		}
}
