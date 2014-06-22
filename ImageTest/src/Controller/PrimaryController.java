package Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;





import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

import data.GUIInfo;
import data.Pitch;
import dist.CalcAngle;
import dist.CalcDist;
import pictureToMat.DetectRects;
import pictureToMat.NewTakepicture;
import pictureToMat.RouteTest;
import pictureToMat.BallMethod;

public class PrimaryController {
	private CvPoint minPunkt;
	private CvPoint roboBagPunkt;
	private CvPoint roboFrontPunkt;

	private int toGoal = 0;
	private Float ppcm;
	private DetectRects findEdge;
	private NewTakepicture takepic;
	private BallMethod balls;
	private final OutputStream dos;
	private RouteTest route;
	private int backMove = 0;
	private Pitch pitch;
	private int movingAround = 0;
	private boolean minIsTemp = false;
	private ArrayList<Float> ballCoor = new ArrayList<Float>();
	private int NGrabs = 0;


	public PrimaryController(DetectRects findEdge) {
		this.findEdge = findEdge;
		takepic = new NewTakepicture();

		NXTInfo nxtInfo = new NXTInfo(2, "G9 awesome!", "0016530918D4");
		NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");// robot nr 2

		NXTConnector connt = new NXTConnector();
		System.out.println("trying to connect");
		connt.connectTo(nxtInfo, NXTComm.LCP);
		System.out.println("connected"); // forbundet
		dos = connt.getOutputStream();// åbner stream
	}

	public void start() {
//		long timePicStart = System.currentTimeMillis();
		takepic.takePicture();
//		long timePicSlut = System.currentTimeMillis();
//		System.out.println("take picture tid: " + (timePicSlut-timePicStart));

//		long timeFindEdgeStart = System.currentTimeMillis();
		pitch = findEdge.detectPitch();
//		long timeFindEdgeSlut = System.currentTimeMillis();
//		System.out.println("find edge tid: " + (timeFindEdgeSlut-timeFindEdgeStart));
		ppcm = pitch.getPixPerCm();

		balls = new BallMethod(pitch);
		route = new RouteTest(pitch);
	}
	public GUIInfo loopRound(GUIInfo calliData, int deliverButtom) {
		int xFactorOfCut = 2;
		int yFactorOfCut = 4;
		//tempPoint = new CvPoint();
		CalcDist dist = new CalcDist();
//		System.out.println("tempgoal: " + tempGoal.x()+","+tempGoal.y());
//		System.out.println("tempgoal: " + tempGoal2.x()+","+tempGoal2.y());
		//################# Calculate corners ########################


		//################## Take picture until robot is found #########
		do {
			takepic.takePicture();	
			// ################## Cut image ####################################
			pitch.cutOrigImg();
			pitch.adjustToCut(xFactorOfCut, yFactorOfCut);
			
			// ################### Find Robot #######################################
			balls.findCircle(calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),	calliData.getIntJlroboDP(),calliData.getIntJlroboMinDist(),calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),"robo", true);

		} while (balls.determineDirection() == false);



		// ################### Find Balls #####################################
		if(NGrabs != 3){
			balls.rotateRobot(); // tegner over robotten, så bolde ikke findes der
			balls.eliminateObstruction(); // tegner over forhindring, så bolde ikke findes der
			balls.findCircle(calliData.getIntJlcircleMinRadius(),calliData.getIntJlcircleMaxRadius(),calliData.getIntJlcircleDP(), calliData.getIntJlcircleDist(),calliData.getIntJlcirclePar1(), calliData.getIntJlcirclePar2(),"balls", false);
			ballCoor = balls.getBallCoordi();
			if(ballCoor.isEmpty()){
				return calliData;
			}
		}
		System.out.println("minIsTemp: " + minIsTemp);
		System.out.println("NGrabs: " + NGrabs);
		// ################### Nearest Ball ####################################
		//System.out.println("Robobagpunkt before adjustment: " + balls.getRoboBagPunkt().x()+","+balls.getRoboBagPunkt().y());
		balls.calculateRotationPoint(); 
		balls.changePerspective(calliData.getPoV());

		//System.out.println("Robobagpunkt after adjustment: " + balls.getRoboBagPunkt().x()+","+balls.getRoboBagPunkt().y());
		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();



		
/*			
			if(!minIsTemp){
			deliverBalls(calliData, dist); // pitch.getMidOfImg().x(), pitch.getMidOfImg().y()
			System.out.println("RoBOT HAS GRABBED 3 TIMES");
			}
			else {
				deliverBalls(calliData, dist); // pitch.getMidOfImg().x(), pitch.getMidOfImg().y()
//				minIsTemp = false;
				System.out.println("SHOULD DELIVER");
			}
*/
		

		if(NGrabs != 3){
			minPunkt = route.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet
			System.out.println("minpunkt = " + minPunkt.x() + " " + minPunkt.y());
		} else if (NGrabs == 3){
			minPunkt = pitch.getGoalA();
		}
		

		//tjekker om forhindringen er imellem robot og bold,mål,temppoint etc.
		if(route.blockingObstruction(roboBagPunkt, minPunkt)/*|| route.blockingObstruction(roboBagPunkt, tempGoal)|| route.blockingObstruction(roboBagPunkt, tempGoal2)*/){
			System.out.println("trying to move around because true");
			System.out.println("MIN___:..:_"+route.blockingObstruction(roboBagPunkt, minPunkt));

			findWayAround(calliData, dist, pitch.getMidOfObs().x(), pitch.getMidOfObs().y());
			toGoal = 0;
		}
		else
		{
			System.out.println("PATH NOT BLOCKED BY OBSTACLE");
			CvPoint corner1 = pitch.getCorner1();
			CvPoint corner2 = pitch.getCorner2();
			CvPoint corner3 = pitch.getCorner3();
			CvPoint corner4 = pitch.getCorner4();
			/*System.out.println("Corner1: " + corner1.x()+","+ corner1.y());
			System.out.println("Corner2: " + corner2.x()+","+ corner2.y());
			System.out.println("Corner3: " + corner3.x()+","+ corner3.y());
			System.out.println("Corner4: " + corner4.x()+","+ corner4.y());
			*/
			// ***************************** Avoid cross *******************************

			edgeCheck(calliData, dist, pitch.getPixPerCm(), corner1, corner2, corner3,corner4); //
			if(NGrabs == 3){	
				// ***************************** Deliver balls *******************************
				System.out.println("RoBOT HAS GRABBED 3 TIMES");
				deliverBalls(calliData, dist); // pitch.getMidOfImg().x(), pitch.getMidOfImg().y()
			}

		}
		
		
		if(route.blockingObstruction(roboBagPunkt, minPunkt)){
			System.out.println("trying to move around because true");
			System.out.println("MIN___:..:_"+route.blockingObstruction(roboBagPunkt, minPunkt));

			findWayAround(calliData, dist, pitch.getMidOfObs().x(), pitch.getMidOfObs().y());
			toGoal = 0;
		}

		whatToSend(calliData,0);
		return calliData;
	}

	public void edgeCheck(GUIInfo calliData, CalcDist dist, float intppcm,
			CvPoint corner1, CvPoint corner2, CvPoint corner3, CvPoint corner4) {
		if (minPunkt.x() > corner1.x() + (int)(6 *ppcm)&& minPunkt.x() < corner2.x() - (int)(20 * ppcm)&& minPunkt.y() > corner1.y()	&& minPunkt.y() < corner1.y() + (int)(20 * ppcm)) 
		{
			minPunkt.x(minPunkt.x());
			minPunkt.y(minPunkt.y()+(int)(18*ppcm)); // 30
			tempCalculater(calliData, dist);
			System.out.println("side A");

		} else if (minPunkt.x() > corner3.x() + (int)(20 * ppcm)	&& minPunkt.x() < corner4.x() - (int)(20 * ppcm) && minPunkt.y() < corner3.y() && minPunkt.y() > corner3.y() - (int)(20 * ppcm)) 
		{
			System.out.println("side B");
			minPunkt.x(minPunkt.x());
			minPunkt.y(minPunkt.y()-(int)(18*ppcm));
			tempCalculater(calliData, dist);
		}
		 else if (minPunkt.y() > corner1.y() + (int)(20 * ppcm) && minPunkt.y() < corner3.y() - (int)(20 * ppcm) && minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (int)(9 * ppcm)/*&& minPunkt.y() > goalA.y()+ (int)(3 * ppcm) && minPunkt.y() < goalA.y()- (int)(3 * ppcm)*/) 
		{
			System.out.println("side C");
			minPunkt.x(minPunkt.x()+(int)(18*ppcm));
			minPunkt.y(minPunkt.y());
			tempCalculater(calliData, dist);
			
		} else if (minPunkt.y() > corner2.y() + (int) (20 * ppcm)&& minPunkt.y() < corner4.y() - (int) (20 * ppcm)&& minPunkt.x() < corner2.x()&& minPunkt.x() > corner2.x() - (int) (9 * ppcm)&& minPunkt.y() > pitch.getGoalA().y()+ (int)(2 * ppcm) && minPunkt.y() < pitch.getGoalA().y()- (int)(2 * ppcm)) {
			System.out.println("side D");
			minPunkt.x(minPunkt.x()-(int)(18*ppcm));
			minPunkt.y(minPunkt.y());
			tempCalculater(calliData, dist);
		}	

		// ***************************** Corner*******************************
		else if(minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (20*intppcm) && minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (20*intppcm)){ 
			minPunkt.x(minPunkt.x()+((int)(20*intppcm)));
			minPunkt.y(minPunkt.y()+((int)(20*intppcm)));
			tempCalculater(calliData, dist);
			System.out.println("corner1"); 
		} 
		else if(minPunkt.x() < corner2.x() && minPunkt.x() > corner2.x() - (20*intppcm) && minPunkt.y() > corner2.y() && minPunkt.y() < corner2.y() +(20*intppcm)){ 
			minPunkt.x(minPunkt.x()-((int)(20*intppcm)));
			minPunkt.y(minPunkt.y()+((int)(20*intppcm)));
			tempCalculater(calliData, dist);
			System.out.println("corner2"); 
		} 
		else if(minPunkt.x() > corner3.x() && minPunkt.x() < corner3.x() + (20*intppcm) &&	minPunkt.y()-10 < corner3.y() && minPunkt.y() > corner3.y() -(20*intppcm)){ 
			minPunkt.x(minPunkt.x()+((int)(20*intppcm)));
			minPunkt.y(minPunkt.y()-((int)(20*intppcm)));
			tempCalculater(calliData, dist);
			System.out.println("corner3"); 
		} 
		else if(minPunkt.x() <corner4.x() && minPunkt.x() > corner4.x() - (20*intppcm) && minPunkt.y() < corner4.y() && minPunkt.y() > corner4.y() - (20*intppcm)){ 
			minPunkt.x(minPunkt.x()-((int)(20*intppcm)));
			minPunkt.y(minPunkt.y()-((int)(20*intppcm)));
			tempCalculater(calliData, dist);
			System.out.println("corner4");
		} 
		

		else {
			backMove = 0;
			minIsTemp = false;
			angleCal(calliData, minPunkt); // udregner vinkel 
		}
	}
	private void deliverBalls(GUIInfo calliData, CalcDist dist) { // , int middelX, int middelY


		if(toGoal == 0){
			toGoal = 1;
			System.out.println("Driving to first point ____________________________________");
			minPunkt.x(pitch.getGoalA().x()-(int)(45*ppcm));
			minPunkt.y(pitch.getGoalA().y());
			//System.out.println("minpunkt x,y: " +minPunkt.x() +","+minPunkt.y() );
			//System.out.println("robobagpunkt x,y: " +roboBagPunkt.x() +","+roboBagPunkt.y() );
			angleCal(calliData, minPunkt);
			//System.out.println("ppcm: " + ppcm);
			//System.out.println("dist =:=  "+ dist.Calcdist(roboBagPunkt, minPunkt));
			route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)+0 * ppcm); // 26 normalt
			minIsTemp = true;
		} else {
			toGoal = 2;
			System.out.println(" ____________________________________Driving to second point");

			minPunkt.x(pitch.getGoalA().x() - (int)(35*ppcm));
			minPunkt.y(pitch.getGoalA().y());
			angleCal(calliData, minPunkt);
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, minPunkt)+6 * ppcm));
			minIsTemp = true;
		}

	}
	private void findWayAround(GUIInfo calliData, CalcDist dist, int middelX, int middelY) {
		if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() < middelY)){
			System.out.println("robo near corner 1, moving around cross");
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);

			minPunkt.x(pitch.getNorth().x());
			minPunkt.y(pitch.getNorth().y());
			aroundCross(calliData, dist, minPunkt);

		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() < middelY)){
			System.out.println("robo near corner 2, moving around cross");
			minPunkt.x(pitch.getEast().x());
			minPunkt.y(pitch.getEast().y());
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);
			aroundCross(calliData, dist, minPunkt);

		}
		else if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() > middelY)){
			System.out.println("robo near corner 3, moving around cross");
			minPunkt.x(pitch.getWest().x());
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);
			minPunkt.y(pitch.getWest().y());

			
			aroundCross(calliData, dist, minPunkt);

		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() > middelY)){
			System.out.println("robo near corner 4, moving around cross");
			minPunkt.x(pitch.getSouth().x());
			minPunkt.y(pitch.getSouth().y());
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);
			
			aroundCross(calliData, dist, minPunkt);

		}
	}
	public void aroundCross(GUIInfo calliData, CalcDist dist, CvPoint tempPoint) {
		angleCal(calliData, tempPoint);
		route.setMinLength(dist.Calcdist(roboBagPunkt, tempPoint)+6*ppcm);
		minIsTemp = true; //betyder den skal ikke grappe
		System.out.println("tempPunkt = " + tempPoint.x() + "," + tempPoint.y());
	}
	public void tempCalculater(GUIInfo calliData, CalcDist dist) {
		if(!minIsTemp){
		angleCal(calliData, minPunkt);
		route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)+1*ppcm);
		minIsTemp = true; //betyder den skal ikke grappe
//		System.out.println("tempPunkt = " + tempPoint.x() + "," + tempPoint.y());
		}
		//send(calliData); // kører til første punkt
		else{
		angleCal(calliData, minPunkt);
		route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)-9*ppcm);
		minIsTemp = false;
		backMove = 1;
		}
	}
	public void angleCal(GUIInfo calliData, CvPoint destination) { /// calculates angel between robo bagpunkt and destination
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
	public void whatToSend(GUIInfo calliData, int subtractionlenght) {
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
			if(!minIsTemp){
				distance -= 6 * ppcm; // for at lande foran bolden
			}
			Thread.sleep(600);
			Case = 81;
			i = distance / 10;
			dosSend(Case, i);

			Thread.sleep(Math.abs((int) Math.round((Float.parseFloat(""	+ route.getMinLength()))* Float.parseFloat("" + calliData.getclose()))));

			if (toGoal == 0 && !minIsTemp) {
				turnBeforeGrab(calliData, angle);
					// samler bold op
					Case = 41;
					i = 41;
					dosSend(Case, i);
					Thread.sleep(1200);
					NGrabs++;
				}

			if (toGoal == 2) {
				Case = 31;
				i = 31;
				turnBeforeGrab(calliData, distance);
				dosSend(Case, i);
				Thread.sleep(1200);
				toGoal = 0;
				minIsTemp = false;
				NGrabs = 0;

			}

			if (backMove ==1) {
				Case = 80;
				i = 3;
				dosSend(Case, i);
				Thread.sleep(1200);
				
				backMove = 0;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	private void turnBeforeGrab(GUIInfo calliData, int angle)throws IOException, InterruptedException {
		int Case;
		int i;
		do {
			takepic.takePicture();	
			// ################## Cut image ####################################
			pitch.cutOrigImg();
			pitch.adjustToCut(2, 4);
			// ################### Find Robot #######################################
			balls.findCircle(calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),	calliData.getIntJlroboDP(),calliData.getIntJlroboMinDist(),calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),"robo", true);

		} while (balls.determineDirection() == false);
		//System.out.println("Robobagpunkt before adjustment: " + balls.getRoboBagPunkt().x()+","+balls.getRoboBagPunkt().y());
		balls.calculateRotationPoint(); 
		balls.changePerspective(calliData.getPoV());

		//System.out.println("Robobagpunkt after adjustment: " + balls.getRoboBagPunkt().x()+","+balls.getRoboBagPunkt().y());
		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();
		if(toGoal == 2){
			angleCal(calliData, pitch.getGoalA());

		}else{
		angleCal(calliData, minPunkt);
		}
		angle = Math.round(Float.parseFloat(""+ calliData.getTurnAngle()));// *
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
	}
	public void dosSend(int Case, int i) throws IOException {
		dos.write(Case);
		dos.flush();
		dos.write(i);
		dos.flush();
	}
}