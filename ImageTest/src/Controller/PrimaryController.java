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
import pictureToMat.Takepicture;
import pictureToMat.RouteTest;
import pictureToMat.BallMethod;

public class PrimaryController {
	private CvPoint minPunkt;
	private CvPoint roboBagPunkt = new CvPoint();
	private CvPoint roboFrontPunkt = new CvPoint();
	private CvPoint tempPunkt;
	private CvPoint oldRoboBagPunkt = new CvPoint();
	private CvPoint oldRoboFrontPunkt = new CvPoint();



	private int toGoal = 0;
	private Float ppcm;
	private DetectRects findEdge;
	private Takepicture takepic;
	private BallMethod balls;
	private final OutputStream dos;
	private RouteTest route;
	private int backMove = 0;
	private Pitch pitch;
	private boolean minIsTemp = false;
	private ArrayList<Float> ballCoor = new ArrayList<Float>();
	private int NGrabs = 0;


	public PrimaryController(DetectRects findEdge) {
		this.findEdge = findEdge;
		takepic = new Takepicture();

		NXTInfo nxtInfo = new NXTInfo(2, "G9 awesome!", "0016530918D4");	//info til tilslutning af bluetooth
		NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");		//robot nr 2

		NXTConnector connt = new NXTConnector();
		System.out.println("trying to connect");
		connt.connectTo(nxtInfo, NXTComm.LCP);	// venter på forbinelse
		System.out.println("connected"); 		// forbundet
		dos = connt.getOutputStream();			// åbner stream
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
		CalcDist dist = new CalcDist(); // nyt objekt til udregning af længder
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
		if(NGrabs != 4){
			balls.rotateRobot(); // tegner over robotten, så bolde ikke findes der
			balls.eliminateObstruction(); // tegner over forhindring, så bolde ikke findes der
			balls.findCircle(calliData.getIntJlcircleMinRadius(),calliData.getIntJlcircleMaxRadius(),calliData.getIntJlcircleDP(), calliData.getIntJlcircleDist(),calliData.getIntJlcirclePar1(), calliData.getIntJlcirclePar2(),"balls", false);
			ballCoor = balls.getBallCoordi();
			 
			if(ballCoor.isEmpty()){
				NGrabs = 4;
			}
		}
		System.out.println("minIsTemp: " + minIsTemp);
		System.out.println("NGrabs: " + NGrabs);
		// ################### Nearest Ball ####################################
		//System.out.println("Robobagpunkt before adjustment: " + balls.getRoboBagPunkt().x()+","+balls.getRoboBagPunkt().y());
		balls.calculateRotationPoint(); //udregner robottens bagpunkt til at være over hjulene, hvor den roterer
		balls.changePerspective(calliData.getPoV()); //udregner hvor robotten faktisk står, da kameraet kan se den fra siden
		
		oldRoboBagPunkt = roboBagPunkt;
		oldRoboFrontPunkt = roboFrontPunkt;
		
		//System.out.println("Robobagpunkt after adjustment: " + balls.getRoboBagPunkt().x()+","+balls.getRoboBagPunkt().y());
		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();

		

		if(NGrabs != 4){//hvis robotten ikke skal køre til mål
			minPunkt = route.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet
			System.out.println("minpunkt = " + minPunkt.x() + " " + minPunkt.y());
		} else if (NGrabs == 4){//hvis robotten skal køre til mål
			minPunkt = pitch.getGoalA();
		}
		

		//tjekker om forhindringen er imellem robot og bold,mål,temppoint etc.
		if(route.blockingObstruction(roboBagPunkt, minPunkt)/*|| route.blockingObstruction(roboBagPunkt, tempGoal)|| route.blockingObstruction(roboBagPunkt, tempGoal2)*/){
			System.out.println("trying to move around cross, because true");
			System.out.println("MIN___:..:_"+route.blockingObstruction(roboBagPunkt, minPunkt));

			findWayAround(calliData, dist, pitch.getMidOfObs().x(), pitch.getMidOfObs().y()); //logik for at køre rundt om kors
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

			edgeCheck(calliData, dist, pitch.getPixPerCm(), corner1, corner2, corner3,corner4); //Tjekker om boldene ligger i kant eller hjørne
			if(NGrabs == 4){//hvis robotten skal køre til mål
				// ***************************** Deliver balls *******************************
				System.out.println("RoBOT HAS GRABBED 3 TIMES");
				deliverBalls(calliData, dist); //logik for at køre til mål 
				// pitch.getMidOfImg().x(), pitch.getMidOfImg().y()
			}

		}
		
		
		if(route.blockingObstruction(roboBagPunkt, minPunkt)){
			System.out.println("trying to move around cross, because true");
			System.out.println("MIN___:..:_"+route.blockingObstruction(roboBagPunkt, minPunkt));

			findWayAround(calliData, dist, pitch.getMidOfObs().x(), pitch.getMidOfObs().y()); //logik for at køre rundt om kors
			toGoal = 0;
		}
		
		whatToSend(calliData,0); //metode til at sende til robot
		return calliData;
	}

	public void edgeCheck(GUIInfo calliData, CalcDist dist, float intppcm,
			CvPoint corner1, CvPoint corner2, CvPoint corner3, CvPoint corner4) {
		
		// ***************************** Sides *******************************
		if (minPunkt.x() > corner1.x() + (int)(20 *ppcm)&& minPunkt.x() < corner2.x() - (int)(20 * ppcm)&& minPunkt.y() > corner1.y()	&& minPunkt.y() < corner1.y() + (int)(10 * ppcm)) 
		{
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x()); 						//sætter midlertidigt punkt lidt ude fra kanten
			minPunkt.y(minPunkt.y()+(int)(25.5*ppcm));		// 30
			}
			tempCalculater2(calliData, dist); 				//logik for at køre til bold i kant
			System.out.println("side A");

		} else if (minPunkt.x() > corner3.x() + (int)(20 * ppcm)	&& minPunkt.x() < corner4.x() - (int)(20 * ppcm) && minPunkt.y() < corner3.y() && minPunkt.y() > corner3.y() - (int)(10 * ppcm)) 
		{
			System.out.println("side B");
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x());						//sætter midlertidigt punkt lidt ude fra kanten
			minPunkt.y(minPunkt.y()-(int)(25.5*ppcm));
			}
			tempCalculater2(calliData, dist);				//logik for at køre til bold i kant
		}
		 else if (minPunkt.y() > corner1.y() + (int)(20 * ppcm) && minPunkt.y() < corner3.y() - (int)(20 * ppcm) && minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (int)(10 * ppcm)/*&& minPunkt.y() > goalA.y()+ (int)(3 * ppcm) && minPunkt.y() < goalA.y()- (int)(3 * ppcm)*/) 
		{
			System.out.println("side C");
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x()+(int)(25.5*ppcm));		//sætter midlertidigt punkt lidt ude fra kanten
			minPunkt.y(minPunkt.y());
			}
			tempCalculater2(calliData, dist);				//logik for at køre til bold i kant
			
		} else if (minPunkt.y() > corner2.y() + (int) (20 * ppcm)&& minPunkt.y() < corner4.y() - (int) (20 * ppcm)&& minPunkt.x() < corner2.x()&& minPunkt.x() > corner2.x() - (int) (10 * ppcm)/*&& minPunkt.y() > pitch.getGoalA().y()+ (int)(2 * ppcm) && minPunkt.y() < pitch.getGoalA().y()- (int)(2 * ppcm)*/) {
			System.out.println("side D");
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x()-(int)(25.5*ppcm));		//sætter midlertidigt punkt lidt ude fra kanten
			minPunkt.y(minPunkt.y());
			}
			tempCalculater2(calliData, dist);				//logik for at køre til bold i kant
		}	

		// ***************************** Corner*******************************
		else if(minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (20*intppcm) && minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (20*intppcm)){ 
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x()+((int)(15*intppcm)));	//sætter midlertidigt punkt lidt ude fra hjørne
			minPunkt.y(minPunkt.y()+((int)(15*intppcm)));
			}
			tempCalculater(calliData, dist);				//logik for at køre til bold i hjørne
			System.out.println("corner1"); 
		} 
		else if(minPunkt.x() < corner2.x() && minPunkt.x() > corner2.x() - (20*intppcm) && minPunkt.y() > corner2.y() && minPunkt.y() < corner2.y() +(20*intppcm)){ 
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x()-((int)(15*intppcm)));	//sætter midlertidigt punkt lidt ude fra hjørne
			minPunkt.y(minPunkt.y()+((int)(15*intppcm)));
			}
			tempCalculater(calliData, dist);				//logik for at køre til bold i hjørne
			System.out.println("corner2"); 
		} 
		else if(minPunkt.x() > corner3.x() && minPunkt.x() < corner3.x() + (20*intppcm) &&	minPunkt.y()-10 < corner3.y() && minPunkt.y() > corner3.y() -(20*intppcm)){ 
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x()+((int)(15*intppcm)));	//sætter midlertidigt punkt lidt ude fra hjørne
			minPunkt.y(minPunkt.y()-((int)(15*intppcm)));
			}
			tempCalculater(calliData, dist);				//logik for at køre til bold i hjørne
			System.out.println("corner3"); 
		} 
		else if(minPunkt.x() <corner4.x() && minPunkt.x() > corner4.x() - (20*intppcm) && minPunkt.y() < corner4.y() && minPunkt.y() > corner4.y() - (20*intppcm)){ 
			if(!minIsTemp){									//hvis den ikke har været ved et midlertidigt punkt
			minPunkt.x(minPunkt.x()-((int)(15*intppcm)));	//sætter midlertidigt punkt lidt ude fra hjørne
			minPunkt.y(minPunkt.y()-((int)(15*intppcm)));
			}
			tempCalculater(calliData, dist);				//logik for at køre til bold i hjørne
			System.out.println("corner4");
		} 
		

		else {
			backMove = 0; 					// Skal ikke bakke
			minIsTemp = false;				// Ikke ved et midlertidigt punkt
			angleCal(calliData, minPunkt); 	// Udregner vinkel 
		}
	}
	private void deliverBalls(GUIInfo calliData, CalcDist dist) {// Logik til at aflevere til mål 
		// , int middelX, int middelY


		if(toGoal == 0){										// Hvis det er første gang mod mål
			toGoal = 1;
			System.out.println("Driving to first point ____________________________________");
			minPunkt.x(pitch.getGoalA().x()-(int)(45*ppcm)); 	// Sætter første punkt 45 cm fra mål
			minPunkt.y(pitch.getGoalA().y());
			angleCal(calliData, minPunkt);						// Udregner vinkler
			route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)+3 * ppcm); // Udregner afstand til punkt
			minIsTemp = true;													// Kører til midlertidigt punkt
		} else {												// Anden gang mod mål
			toGoal = 2;
			System.out.println(" ____________________________________Driving to second point");
			minPunkt.x(pitch.getGoalA().x() - (int)(30*ppcm));	// Sætter andet punkt mod mål
			minPunkt.y(pitch.getGoalA().y());
			angleCal(calliData, minPunkt);						// Udregner vinkler
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, minPunkt)+3 * ppcm));	// Udregner afstand til punkt
			minIsTemp = true;													// Kører til midlertidigt punkt
		}

	}
	private void findWayAround(GUIInfo calliData, CalcDist dist, int middelX, int middelY) { // Logik for at køre rundt om kors
		if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() < middelY)){ // Robot er øverst venstre på bane
			System.out.println("robo near corner 1, moving around cross");
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);
			minPunkt.x(pitch.getNorth().x());			// Kører til nord for kors
			minPunkt.y(pitch.getNorth().y());
			aroundCross(calliData, dist, minPunkt);		// Metode for at køre rundt om kors

		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() < middelY)){ // Robot er øverst højre på bane
			System.out.println("robo near corner 2, moving around cross");
			minPunkt.x(pitch.getEast().x());			// Kører øst for kors
			minPunkt.y(pitch.getEast().y());
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);
			aroundCross(calliData, dist, minPunkt);		// Metode for at køre rundt om kors

		}
		else if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() > middelY)){ // Robot er nederst venstre på bane
			System.out.println("robo near corner 3, moving around cross");
			minPunkt.x(pitch.getWest().x());			// Kører til vest for kors
			minPunkt.y(pitch.getWest().y());
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);
			aroundCross(calliData, dist, minPunkt);		// Metode for at køre rundt om kors

		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() > middelY)){ // Robot er nederst højre på bane
			System.out.println("robo near corner 4, moving around cross");
			minPunkt.x(pitch.getSouth().x());			// kører til syd
			minPunkt.y(pitch.getSouth().y());
			System.out.println("frontpunkt: " + roboFrontPunkt.x() + ","+ roboFrontPunkt.y());
			System.out.println("middle: " + middelX +"" + middelY);
			aroundCross(calliData, dist, minPunkt);		// Metode for at køre rundt om kors

		}
	}
	public void aroundCross(GUIInfo calliData, CalcDist dist, CvPoint tempPoint) { // Metode for at køre rundt om kors
		angleCal(calliData, tempPoint);					// Udregner vinkler
		route.setMinLength(dist.Calcdist(roboBagPunkt, tempPoint)+6*ppcm);	// Udregner afstand til punkt
		minIsTemp = true; 								//betyder den skal ikke grappe
		System.out.println("tempPunkt = " + tempPoint.x() + "," + tempPoint.y());
	}
	public boolean borderIsGrapped(CvPoint currentPoint)
	{ // Logik for at se om robot hænger fast i kant
		System.out.println("BORDER IS GRAPPED!_______________________________");	

		if(currentPoint.x() <= pitch.getCorner1().x() + 6*pitch.getPixPerCm() ||
				currentPoint.x() >= pitch.getCorner4().x() - 6*pitch.getPixPerCm() ||
				currentPoint.y() <= pitch.getCorner1().y() + 6*pitch.getPixPerCm() ||
				currentPoint.y() >= pitch.getCorner4().y() - 6*pitch.getPixPerCm())
		{
			if(pitch.getGoalA().y() - 5*pitch.getPixPerCm() <= currentPoint.y() &&
			   pitch.getGoalA().y() + 5*pitch.getPixPerCm() >= currentPoint.y())
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return false;
	}
	
	
	
	public void tempCalculater(GUIInfo calliData, CalcDist dist) { // Metode til at køre til midlertidigt punkt ved hjørne
		if(!minIsTemp){														// Hvis robot skal til midlertidigt punkt
		angleCal(calliData, minPunkt);										// Udregner vinkler
		route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)+1*ppcm);	// Udregner afstand til punkt
		minIsTemp = true; 													// Betyder den skal ikke grappe
		System.out.println("IN FIRST TEMPpoint:::::::::::::::::::::::::::");
		}
		else{
		angleCal(calliData, minPunkt);										// Udregner vinkler
		route.setMinLength((float) (0));									// Sætter afstand til 0
		minIsTemp = false;													// Robot skal køre til bold
		backMove = 1;														// Robot skal bakke ud af hjørne
		System.out.println("IN SECOND TEMPPOINT::::............................_________");

		}
	}
	public void tempCalculater2(GUIInfo calliData, CalcDist dist) { // Metode til at køre til midlertidigt punkt ved kant
		if(!minIsTemp){														// Hvis robot skal til midlertidigt punkt
		angleCal(calliData, minPunkt);										// Udregner vinkler
		route.setMinLength((dist.Calcdist(roboBagPunkt, minPunkt))+2*ppcm); // Udregner afstand til punkt
		minIsTemp = true; 													//betyder den skal ikke grappe
		System.out.println("IN FIRST TEMPpoint in calc22222:::::::::::::::::::::::::::");
		}
		else{
		angleCal(calliData, minPunkt);										// Udregner vinkler
		route.setMinLength((float) (0));									// Sætter afstand til 0
		minIsTemp = false;													// Robot skal køre til bold
		backMove = 1;														// Robot skal bakke ud af hjørne
		System.out.println("IN SECOND TEMPPOINT in calc22222::::............................_________");

		}
	}
	public void angleCal(GUIInfo calliData, CvPoint destination) { 	// Metode til at regner vinkel mellem robot og punkt 
		CalcAngle Angle = new CalcAngle();							// Nyt Vinkel udregnings objekt
		// Flytter punkter så robot bagpunkt kommer til at ligge i origo
		CvPoint nyRoboFront = new CvPoint(roboFrontPunkt.x()- roboBagPunkt.x(), roboFrontPunkt.y()- roboBagPunkt.y());	
		CvPoint nyRoboBag = new CvPoint(0, 0);
		CvPoint nyMinPunkt = new CvPoint(destination.x()- roboBagPunkt.x(), destination.y()	- roboBagPunkt.y());
		
		Float BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);	// Udregner vinkel mellem x-aksen og bold
		Float RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);	// Udregner vinkel mellem x-aksen og robot
		calliData.setTurnAngle(BallAngle - RoboAngle);				// Udrenger forskel
		// Sørger for at robotten ikke drejer mere end 180 grader
		if (calliData.getTurnAngle() > 180) {
			calliData.setTurnAngle(calliData.getTurnAngle() - 360);
		}
		if (calliData.getTurnAngle() < -180) {
			calliData.setTurnAngle(calliData.getTurnAngle() + 360);
		}
		
		System.out.println("TURNANGLE_:_:_: " + calliData.getTurnAngle());
		calliData.setBallAngle(BallAngle); 							// Sætter vinkel
		calliData.setRoboAngle(RoboAngle);							// Sætter vinkel
	}
	public void whatToSend(GUIInfo calliData, int subtractionlenght) {	// Metode for at sende til robot
		int Case;		// Hvilken Case der skal sendes til robot 
		int i;			// Hvilken eventuel distance/vinkel der skal sendes
		
		
		if(borderIsGrapped(roboFrontPunkt)){	// Giver slip på kant og bakker
			try {
				dosSend(71, 71);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
		
		int angle = Math.round(Float.parseFloat(""+ calliData.getTurnAngle()));// Aflæser vinkel
		
		System.out.println("TurnAngle = " + calliData.getTurnAngle());
		try {
			if (Math.abs(angle) < 250) { 	// Hvis vinkel er mindre end 250 grader
				if (angle > 0) 				// Vælger retning der skal drejes
					Case = 21; 				// Drejer højre
				else
					Case = 12; 				// Drejer venstre
			} else { 						// Vinkel over 250 grader sendes 2 gange
				angle = angle / 2; 			// Vinkel deles i 2
				if (angle > 0) 				// vælger retning der skal drejes
					Case = 21; 				// Drejer højre
				else
					Case = 12; 				// Drejer venstre
				angle = Math.abs(angle); 	// Tager numerisk størrelse af vinkel
				
				i = angle;
				dosSend(Case, i);			// Metode til at sende data
				Thread.sleep(700);
			}
			angle = Math.abs(angle);
			// Drejer ikke ind i kors
			if ((roboFrontPunkt.x() < pitch.getMidOfObs().x() && roboFrontPunkt.y() < pitch.getMidOfObs().y())
					|| (roboFrontPunkt.x() > pitch.getMidOfObs().x() && roboFrontPunkt.y() < pitch.getMidOfObs().y())
					|| (roboFrontPunkt.x() < pitch.getMidOfObs().x() && roboFrontPunkt.y() > pitch.getMidOfObs().y())
					|| (roboFrontPunkt.x() > pitch.getMidOfObs().x() && roboFrontPunkt.y() > pitch.getMidOfObs().y())) {
				if (angle > 60) {
					angle = 360 - angle;
					Case = 11;
				}
			}
			
			i = angle; 			// Vinkel sættes til "i" der sendes til robot
			dosSend(Case, i); 	// Metode til at sende data

			Thread.sleep(1200);
			
			// kører robot frem
			int distance = (int) (((route.getMinLength()* Math.round(calliData.getlengthMultiply()) / pitch.getPixPerCm()))-0.5*ppcm); // længde konvertering

			System.out.println("dist = " + distance);
			if(!minIsTemp && backMove == 0){ // Hvis den skal køre til en bold
				distance -= 6 * ppcm;		 // for at lande foran bolden
				System.out.println("dist after minus = " + distance);
			}
			
			Thread.sleep(600);
			Case = 81; 			// Sætter Case der skal sendes
			i = distance / 10; 	// Sætter distance
			dosSend(Case, i);	// Metode til at sende data

			Thread.sleep(Math.abs((int) Math.round((Float.parseFloat(""	+ route.getMinLength()))* Float.parseFloat("" + calliData.getclose())))); // Sleep afhængig af hvor langt den skal køre
			
			if (toGoal == 0 && !minIsTemp) { // Hvis den skal køre til bold
				turnBeforeGrab(calliData, angle); // Drejer til bold inden opsamling
					// samler bold op
					Case = 41; 			// Sætter Case der skal sendes
					i = 41;	   			// Sætter tal 
					dosSend(Case, i); 	// Metode til at sende data
					Thread.sleep(1200);
					NGrabs++; 			// Tæller antal af grabs op, hvis det når 4 skal der køres til mål
				}

			if (toGoal == 2) { // Hvis der skal køres til mål
				turnBeforeGrab(calliData, angle); // justerer vinkel
				Case = 31;			// Sætter Case der skal sendes
				i = 31;				// Sætter tal
				dosSend(Case, i);	// Metode til at sende data
				Thread.sleep(1200);
				toGoal = 0;			// Til mål sættes til 0
				minIsTemp = false;
				NGrabs = 0;			// antallet af grabs nulstilles

			}

			if (backMove ==1) { 	// hvis den skal bakke
				Case = 80;			// Sætter case der skal sendes
				i = 3; 				// Sætter distance
				dosSend(Case, i);	// Metode til at sende data
				Thread.sleep(1200);
				backMove = 0; 		// Nulstiller bakke variable
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		}
	}

	private void turnBeforeGrab(GUIInfo calliData, int angle)throws IOException, InterruptedException {
		// Metode der tager et nyt billede og indstiller vinkel inden der skal køre det sidste til punkt
		int Case;
		int i;
		do { // Tager et nyt billede indtil den har fundet robotten
			takepic.takePicture();	
			// ################## Cut image ####################################
			pitch.cutOrigImg();
			pitch.adjustToCut(2, 4);
			// ################### Find Robot #######################################
			balls.findCircle(calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),	calliData.getIntJlroboDP(),calliData.getIntJlroboMinDist(),calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),"robo", true);

		} while (balls.determineDirection() == false);
		// Tilpasser robottens placering
		balls.calculateRotationPoint(); 
		balls.changePerspective(calliData.getPoV());
		// Henter robottens placering ind
		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();
		if(toGoal == 2){ 		// Udregner vinkel til mål
			angleCal(calliData, pitch.getGoalA());
		}else{ 					// Udregner vinkel til minPunkt
		angleCal(calliData, minPunkt);
		}
		angle = Math.round(Float.parseFloat(""+ calliData.getTurnAngle()));// Klargører angel variablen
		if (Math.abs(angle) < 250) { // Hvis vinkel er mindre end 250 grader
			if (angle > 0) 		// Vælger retning der skal drejes
				Case = 21; 		// Drejer højre
			else
				Case = 12; 		// Drejer venstre
		} else { 				// Vinkel over 250 grader sendes 2 gange
			angle = angle / 2; 	// Vinkel deles i 2
			if (angle > 0) 		// vælger retning der skal drejes
				Case = 21; 		// Drejer højre
			else
				Case = 12; 		// Drejer venstre
			angle = Math.abs(angle); // Tager numerisk størrelse af vinkel

			i = angle;
			dosSend(Case, i);	// Metode til at sende data
			Thread.sleep(700);
		}
		angle = Math.abs(angle); // Tager numeriske værdi af vinkelen
		i = angle;
		dosSend(Case, i); 		// Metode til at sende data

		Thread.sleep(1200);
	}
	public void dosSend(int Case, int i) throws IOException { // Metode til at sende data
		dos.write(Case); // Sender Case robot skal udføre
		dos.flush();	// Flusher
		dos.write(i);	// Sender vinkel/distance
		dos.flush();	// Flusher
	}
}