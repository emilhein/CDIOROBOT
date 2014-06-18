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
import dist.CalcAngle;
import dist.CalcDist;
import pictureToMat.DetectRects;
import pictureToMat.RouteTest;
import pictureToMat.TakePicture;
import pictureToMat.ballMethod;

public class PrimaryController {
	private CvPoint goalB;
	private CvPoint minPunkt;
	private CvPoint tempGoal;
	private CvPoint roboBagPunkt;
	private CvPoint roboFrontPunkt;
	private int toGoal = 0;
	private Float ppcm;
	private DetectRects findEdge;
	private TakePicture takepic;
	private ballMethod balls;
	private CalcDist dist;
	private final OutputStream dos;
	private RouteTest route;
	private int moveBack = 0;
	private int backMove = 0;
	private int ifTemp = 0;
	private CvPoint originalCorner1, originalCorner4;

	public PrimaryController(DetectRects findEdge) {
		this.findEdge = findEdge;
		takepic = new TakePicture();
		balls = new ballMethod();
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

		long timePicStart = System.currentTimeMillis();
		takepic.takePicture();
		long timePicSlut = System.currentTimeMillis();
		System.out.println("take picture tid: " + (timePicSlut-timePicStart));
		
		long timeFindEdgeStart = System.currentTimeMillis();
		findEdge.detectAllRects();
		long timeFindEdgeSlut = System.currentTimeMillis();
		System.out.println("find edge tid: " + (timeFindEdgeSlut-timeFindEdgeStart));
		
		ppcm = findEdge.getPixPerCm();
		
		//CvPoint originalCorner3 = new CvPoint(findEdge.getInnerRect().x(), findEdge.getInnerRect().y() + findEdge.getInnerRect().height());
		originalCorner1 = new CvPoint(findEdge.getInnerRect().x(), findEdge.getInnerRect().y());
		originalCorner4 = new CvPoint(findEdge.getInnerRect().x() + findEdge.getInnerRect().width(), findEdge.getInnerRect().y() + findEdge.getInnerRect().height());
		//CvPoint originalCorner2 = new CvPoint(findEdge.getInnerRect().x(), findEdge.getInnerRect().y() + findEdge.getInnerRect().height());
	}

	public GUIInfo loopRound(GUIInfo calliData, int deliverButtom) {
		/*
		 * char firstRun = 'a'; int ballCount = 0; int count = 0;
		 */
		CvPoint corner1, corner2, corner3, corner4;
		
		CalcDist dist = new CalcDist();

		//################# Calculate corners ########################
		
		int intppcm = (int)(Math.round(ppcm));

		//################## Take picture until robot is found #########
		do {
			takepic.takePicture();	

			// ################## Cut image ####################################
			balls.pictureToMat2(originalCorner1, originalCorner4, ppcm);
			System.out.println("ORG1.x: " + originalCorner1.x());
			System.out.println("ORG1.y: " + originalCorner1.y());
			System.out.println("ORG4.x: " + originalCorner4.x());
			System.out.println("ORG4.y: " + originalCorner4.y());
			findEdge.adjustToCuttedImg(ppcm, 2, 4);
			
			corner1 = new CvPoint(findEdge.getInnerRect().x(), findEdge.getInnerRect().y());
			corner2 = new CvPoint(findEdge.getInnerRect().x()+findEdge.getInnerRect().width(), findEdge.getInnerRect().y());
			corner3 = new CvPoint(findEdge.getInnerRect().x(), findEdge.getInnerRect().y() + findEdge.getInnerRect().height());
			corner4 = new CvPoint(findEdge.getInnerRect().x() + findEdge.getInnerRect().width(), findEdge.getInnerRect().y() + findEdge.getInnerRect().height());
			
			System.out.println("Corner1 = " + corner1.x() + "," + corner1.y());
			System.out.println("Corner2 = " + corner2.x() + "," + corner2.y());
			System.out.println("Corner3 = " + corner3.x() + "," + corner3.y());
			System.out.println("Corner4 = " + corner4.x() + "," + corner4.y());

			
			
			// ################### Find Robot #######################################

			balls.findCircle(

			calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),
					calliData.getIntJlroboDP(),
					calliData.getIntJlroboMinDist(),
					calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),
					"robo", true, corner1, corner4, ppcm);

		} while (balls.determineDirection() == false);

		balls.rotateRobot(ppcm);
		balls.eliminateObstruction(findEdge.getObstruction(), ppcm);
		
		// ################### Find Balls #####################################
		balls.findCircle(

		calliData.getIntJlcircleMinRadius(),
				calliData.getIntJlcircleMaxRadius(),
				calliData.getIntJlcircleDP(), calliData.getIntJlcircleDist(),
				calliData.getIntJlcirclePar1(), calliData.getIntJlcirclePar2(),
				
				"balls", false, corner1, corner4, ppcm);

		ArrayList<Float> ballCoor = balls.getBallCoordi();
		
		
		// ################### Nearest Ball
		balls.calculateRotationPoint();
		balls.changePerspective(calliData.getPoV(), findEdge.getMidOfImg());
		

		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();


		minPunkt = route.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt,
				findEdge.getGoalA(), ppcm,findEdge.getNorth(),findEdge.getSouth(), findEdge.getEast(), findEdge.getWest(), findEdge.getMidOfImg()); // tegner dem i testprogrammet
		System.out.println("minpunkt = " + minPunkt.x() + " " + minPunkt.y());

		// ############################# Calc Angle ###################
		String text1 = "Antal bolde fundet: " + (ballCoor.size() / 3);
		JTextArea txtArea1 = new JTextArea(text1, 1, 1);
		calliData.setTxtArea1(txtArea1);

		JLabel lbltxt = new JLabel();
		lbltxt.setText(text1);

		calliData.setLbltxt(lbltxt);
			
		if(deliverButtom == 1){
			if(toGoal == 0){
				toGoal = 1;
				goalB = findEdge.getGoalB();
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
	
		}

		// ***************************** Avoid edge*******************************
		
		//System.out.println("obstruction is at: " + findEdge.getObstruction().x() +"," + findEdge.getObstruction().y());*/

		/*
		int l1 = corner1.y()+(int)(5*ppcm); 
		int l2 = corner4.y()-(int)(5*ppcm);
		int l3 = corner1.x()+(int)(5*ppcm);
		int l4 = corner4.x()-(int)(5*ppcm);
				

		if (minPunkt.x() < l1 || minPunkt.x() > l4 || minPunkt.y() < l3|| minPunkt.y() > l2) {
			// bold under L1!
			CvPoint tempPoint = new CvPoint(balls.getRoboBagPunkt().x()	- (balls.getRoboBagPunkt().x() - minPunkt.x()), balls.getRoboBagPunkt().y());
			angleCal(calliData, tempPoint);
			route.setMinLength(Math.abs(balls.getRoboBagPunkt().x()	- tempPoint.x()));
			ifTemp = 1;
			send(calliData); // robot skal gerne være kørt til temporary point (vinkelret på bolden og kanten)
	//		calliData.setTurnAngle(90F);
			/*

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

		}
		*/
		// ***************************** Kant*******************************
		if (minPunkt.x() > corner1.x() + (int)(6 *ppcm)
				&& minPunkt.x() < corner2.x() - (int)(6 * ppcm)
				&& minPunkt.y() > corner1.y()
				&& minPunkt.y() < corner1.y() + (int)(6 * ppcm)) 
		{
//			CvPoint tempPoint = new CvPoint(roboBagPunkt.x()	- (roboBagPunkt.x() - minPunkt.x()),roboBagPunkt.y());
			CvPoint tempPoint = new CvPoint(minPunkt.x(),(minPunkt.y()+(int)(15*ppcm)));

			
			tempCalculater(calliData, dist, tempPoint);
			System.out.println("side A");
			
		} else if (minPunkt.x() > corner3.x() + (int)(6 * ppcm)
				&& minPunkt.x() < corner4.x() - (int)(6 * ppcm)
				&& minPunkt.y() < corner3.y()
				&& minPunkt.y() > corner3.y() - (int)(6 * ppcm)) 
		{
			System.out.println("side B");
			CvPoint tempPoint = new CvPoint(minPunkt.x(),(minPunkt.y()-(int)(15*ppcm)));
			tempCalculater(calliData, dist, tempPoint);

		} else if (minPunkt.x() > corner1.y() + (int)(6 * ppcm)
				&& minPunkt.y() < corner3.y() - (int)(6 * ppcm)
				&& minPunkt.x() > corner1.x()
				&& minPunkt.x() < corner1.x() + (int)(6 * ppcm)) 
		{
			System.out.println("side C");
		} else if (minPunkt.x() > corner2.y() + (int)(6 * ppcm)
				&& minPunkt.y() < corner4.y() - (int)(6 * ppcm)
				&& minPunkt.x() > corner2.x()
				&& minPunkt.x() < corner2.x() - (int)(6 * ppcm)) 
		{
			System.out.println("side D");
		}	
		
		// ***************************** Corner*******************************
		else if(minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (6*intppcm) && minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()+(15*intppcm),minPunkt.y()+(15*intppcm));
			System.out.println("corner1"); 
			tempCalculater(calliData, dist, tempPoint);
			} 
		else if(minPunkt.x() < corner2.x() && minPunkt.x() > corner2.x() - (6*intppcm) && minPunkt.y() > corner2.y() && minPunkt.y() < corner2.y() +(6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()-(15*intppcm),minPunkt.y()+(15*intppcm));
			System.out.println("corner2"); 
			tempCalculater(calliData, dist, tempPoint);
			} 
		else if(minPunkt.x() > corner3.x() && minPunkt.x() < corner3.x() + (10*intppcm) &&	minPunkt.y()-10 < corner3.y() && minPunkt.y() > corner3.y() -(6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()+(15*intppcm),minPunkt.y()-(15*intppcm));
			System.out.println("corner3"); 
			tempCalculater(calliData, dist, tempPoint);
			} 
		else if(minPunkt.x() <corner4.x() && minPunkt.x() > corner4.x() - (6*intppcm) && minPunkt.y() < corner4.y() && minPunkt.y() > corner4.y() - (6*intppcm)){ 
			CvPoint tempPoint = new CvPoint(minPunkt.x()-(15*intppcm),minPunkt.y()-(15*intppcm));
			System.out.println("corner4");
			tempCalculater(calliData, dist, tempPoint);
			} 
		
		else {
			backMove = 0;
			angleCal(calliData, minPunkt);
		}


		send(calliData);
		
		return calliData;
	}

	public void tempCalculater(GUIInfo calliData, CalcDist dist, CvPoint tempPoint) {
		angleCal(calliData, tempPoint);
		route.setMinLength(dist.Calcdist(roboBagPunkt, tempPoint));
		ifTemp = 1;
		System.out.println("tempPunkt = " + tempPoint.x() + "," + tempPoint.y());
//		send(calliData);
		calPosition(roboFrontPunkt,  roboBagPunkt, tempPoint);
//		angleCal(calliData, minPunkt);
//		route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)-2*ppcm);
//		backMove = 1; 
//		ifTemp = 0;
	}

	public void angleCal(GUIInfo calliData, CvPoint destination) {
		CalcAngle Angle = new CalcAngle();

		CvPoint nyRoboFront = new CvPoint(roboFrontPunkt.x()
				- roboBagPunkt.x(), roboFrontPunkt.y()
				- roboBagPunkt.y());
		CvPoint nyRoboBag = new CvPoint(0, 0);
		CvPoint nyMinPunkt = new CvPoint(destination.x()- roboBagPunkt.x(), destination.y()	- roboBagPunkt.y());

		Float BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		// System.out.println("BallAngle = " + BallAngle);
		Float RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		// System.out.println("RoboAngle = " + RoboAngle);
		calliData.setTurnAngle(BallAngle - RoboAngle);

		if (calliData.getTurnAngle() > 180) {
			calliData.setTurnAngle(calliData.getTurnAngle() - 360);
		}
		if (calliData.getTurnAngle() < -180) {
			calliData.setTurnAngle(calliData.	getTurnAngle() + 360);
		}

		calliData.setBallAngle(BallAngle);
		calliData.setRoboAngle(RoboAngle);
	}

	public void send(GUIInfo calliData) {
		int Case;
		int i;
		

		int angle = (int) Math.round(Float.parseFloat(""+ calliData.getTurnAngle()));// *
												// (Float.parseFloat(jl17.getText())));
												// // vinkel

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
				dos.write(Case); // sender case
				dos.flush();
				dos.write(Math.abs(angle)); // sender vinkel
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
/*
			System.out.println("Lenghtmulti " + calliData.getlengthMultiply());
			System.out.println("minmulti " + route.getMinLength());
			System.out.println("ppcm  " + findEdge.getPixPerCm());
*/
			int distance = (int) ((route.getMinLength()* Math.round(calliData.getlengthMultiply()) / findEdge
					.getPixPerCm())); // længde konvertering

			
			System.out.println("dist = " + distance);
			if(ifTemp == 0 ){
				distance -= 6 * ppcm; // for at lande foran bolden
			}
			Thread.sleep(600);
			dos.write(81);
			dos.flush();
			i = distance / 10;
			dos.write(i);
			dos.flush();

			Thread.sleep((int) Math.round((Float.parseFloat(""	+ route.getMinLength()))* Float.parseFloat("" + calliData.getclose())));
		
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
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void calPosition(CvPoint roboFrontPunkt, CvPoint roboBagPunkt,CvPoint tempPoint) {
		int diffX = (int) (roboBagPunkt.x()-roboFrontPunkt.x());
		int diffY = (int) (roboBagPunkt.y()-roboFrontPunkt.y());
		roboFrontPunkt.x(tempPoint.x());
		roboFrontPunkt.y(tempPoint.y());
		roboBagPunkt.x(tempPoint.x()+diffX);
		roboBagPunkt.y(tempPoint.y()+diffY);
		System.out.println("front " + roboFrontPunkt.x() + " " + roboFrontPunkt.y());
		System.out.println("bag " + roboBagPunkt.x() + " " + roboBagPunkt.y());
			
		}
}
