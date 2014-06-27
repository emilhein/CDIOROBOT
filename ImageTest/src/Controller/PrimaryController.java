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
	private int isTemp = 0;


	public PrimaryController(DetectRects findEdge) {
		// opret objekter til kant findning og billedtagning
		this.findEdge = findEdge;
		takepic = new Takepicture();

		// forbereder kommunikation med robot
		NXTInfo nxtInfo = new NXTInfo(2, "G9 awesome!", "0016530918D4");
		NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");// robot nr 2

		NXTConnector connt = new NXTConnector();
		connt.connectTo(nxtInfo, NXTComm.LCP);
		dos = connt.getOutputStream();
	}


	public void start() {
		// tag billed
		takepic.takePicture();

		// opdag kant + kors, gem info om banen i objekt
		pitch = findEdge.detectPitch();
		ppcm = pitch.getPixPerCm();

		// send banen med til de nyoprettede find bolde/rute objekter
		balls = new BallMethod(pitch);
		route = new RouteTest(pitch);
	}


	public GUIInfo loopRound(GUIInfo calliData, int deliverButtom) {
		// margin rundt om banen, som skal med på det tilskærede billede (i pixels) 
		int xFactorOfCut = 8;
		int yFactorOfCut = 8;

		// object til beregninger af vinkler/længder
		CalcDist dist = new CalcDist();


		// ################## Take picture indtil robotten er fundet ##################
		do {
			takepic.takePicture();	
			// ------------- Cut image -------------
			pitch.cutOrigImg();

			// beregn nye coordinater efter billedet er cuttet til
			pitch.adjustToCut(xFactorOfCut, yFactorOfCut);

			// ------------- Find robot -------------
			balls.findCircle(calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),	calliData.getIntJlroboDP(),calliData.getIntJlroboMinDist(),calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),"robo", true);

		} while (balls.determineDirection() == false);


		// ################## Find bolde ##################

		// 4 x forsøg på at fange bold
		if(NGrabs != 4)
		{
			balls.rotateRobot(); // tegner over robotten, så bolde ikke findes der
			balls.eliminateObstruction(); // tegner over forhindring, så bolde ikke findes der
			// find bolde og gem deres koordinater
			balls.findCircle(calliData.getIntJlcircleMinRadius(),calliData.getIntJlcircleMaxRadius(),calliData.getIntJlcircleDP(), calliData.getIntJlcircleDist(),calliData.getIntJlcirclePar1(), calliData.getIntJlcirclePar2(),"balls", false);
			ballCoor = balls.getBallCoordi();

			// blev ingen bolde fundet, køres til mål (der kan være nogle i gribearmen)
			if(ballCoor.isEmpty()){
				NGrabs = 4;
			}
		}


		// ################## Tætteste bold ##################
		// beregn det punkt, robotten drejer om og gem som bagpunkt
		balls.calculateRotationPoint(); 
		// "flyt" robottens punkter med midten pga. robotten er høj og ses i fugleperspektiv
		balls.changePerspective(calliData.getPoV());

		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();

		if(NGrabs != 4) // flere bolde skal samles
		{
			// find tætteste bold/temporary point og tegn rute dertil
			minPunkt = route.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt);
		} 
		else if (NGrabs == 4) // nok bolde er samlet
		{
			minPunkt = pitch.getGoalA(); // punktet, vi kører til, bliver mål A's midte
		}

		// tjekker om forhindringen er imellem robot og bold,mål,temppoint etc.
		if(route.blockingObstruction(roboBagPunkt, minPunkt))
		{
			// sætter et punkt på vej rundt om korset, som der skal køres til
			findWayAround(calliData, dist, pitch.getMidOfObs().x(), pitch.getMidOfObs().y());
			// vi kører ikke direkte til mål
			toGoal = 0;
		}
		else
		{
			CvPoint corner1 = pitch.getCorner1();
			CvPoint corner2 = pitch.getCorner2();
			CvPoint corner3 = pitch.getCorner3();
			CvPoint corner4 = pitch.getCorner4();

			// ################## Avoid cross ##################

			// tjek om bolden ligger i hjørnet (/kanten) og sæt i så fald et temporary point udfor
			edgeCheck(calliData, dist, pitch.getPixPerCm(), corner1, corner2, corner3,corner4); //

			if(NGrabs == 4) // klar til at køre i mål?
			{	
				// ------------- Deliver balls -------------
				deliverBalls(calliData, dist);
			}

		}

		// er forhindringen mellem robotten og et (evt. nyt) minpunkt
		if(route.blockingObstruction(roboBagPunkt, minPunkt))
		{
			// find punkt at køre til på vej rundt om korset
			findWayAround(calliData, dist, pitch.getMidOfObs().x(), pitch.getMidOfObs().y());
			// kør ikke direkte til mål
			toGoal = 0;
		}

		// bestem, hvad der sendes til robotten
		whatToSend(calliData,0);

		// return resultater relevante for GUI
		return calliData;
	}

	public void edgeCheck(GUIInfo calliData, CalcDist dist, float intppcm, CvPoint corner1, CvPoint corner2, CvPoint corner3, CvPoint corner4) 
	{
		/*
		 * Denne funktion tjekker om bolden lægger i en af banens kanter/hjørner.
		 * Ligger bolden i en kant, sættes et nyt minpunkt vinkelret ud for boldens placering. 
		 * Står robotten allerede på dette punkt, er minpunkt stadig bolden.
		 * Ligger bolden i et hjørne, sættes et nyt minpunkt skråt ud for boldens placering.
		 * Står robotten allerede på dette punkt, er minpunkt stadig bolden.
		 */

		if (minPunkt.x() > corner1.x() + (int)(20 *ppcm)&& minPunkt.x() < corner2.x() - (int)(20 * ppcm)&& minPunkt.y() > corner1.y()	&& minPunkt.y() < corner1.y() + (int)(10 * ppcm)) 
		{
			// side A
			if(!minIsTemp){
				minPunkt.x(minPunkt.x());
				minPunkt.y(minPunkt.y()+(int)(26.5*ppcm));
			}
			tempCalculater2(calliData, dist);

		} else if (minPunkt.x() > corner3.x() + (int)(20 * ppcm)	&& minPunkt.x() < corner4.x() - (int)(20 * ppcm) && minPunkt.y() < corner3.y() && minPunkt.y() > corner3.y() - (int)(10 * ppcm)) 
		{
			// side B
			if(!minIsTemp){
				minPunkt.x(minPunkt.x());
				minPunkt.y(minPunkt.y()-(int)(26.5*ppcm));
			}
			tempCalculater2(calliData, dist);
		}
		else if (minPunkt.y() > corner1.y() + (int)(20 * ppcm) && minPunkt.y() < corner3.y() - (int)(20 * ppcm) && minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (int)(10 * ppcm)/*&& minPunkt.y() > goalA.y()+ (int)(3 * ppcm) && minPunkt.y() < goalA.y()- (int)(3 * ppcm)*/) 
		{
			// side C
			if(!minIsTemp){
				minPunkt.x(minPunkt.x()+(int)(26.5*ppcm));
				minPunkt.y(minPunkt.y());
			}
			tempCalculater2(calliData, dist);

		} else if (minPunkt.y() > corner2.y() + (int) (20 * ppcm)&& minPunkt.y() < corner4.y() - (int) (20 * ppcm)&& minPunkt.x() < corner2.x()&& minPunkt.x() > corner2.x() - (int) (10 * ppcm)/*&& minPunkt.y() > pitch.getGoalA().y()+ (int)(2 * ppcm) && minPunkt.y() < pitch.getGoalA().y()- (int)(2 * ppcm)*/) {
			// side D
			if(!minIsTemp){
				minPunkt.x(minPunkt.x()-(int)(26.5*ppcm));
				minPunkt.y(minPunkt.y());
			}
			tempCalculater2(calliData, dist);
		}	

		// ***************************** Corner*******************************
		else if(minPunkt.x() > corner1.x() && minPunkt.x() < corner1.x() + (20*intppcm) && minPunkt.y() > corner1.y() && minPunkt.y() < corner1.y() + (20*intppcm))
		{ 
			// corner 1
			if(!minIsTemp){
				minPunkt.x(minPunkt.x()+((int)(14*intppcm)));
				minPunkt.y(minPunkt.y()+((int)(14*intppcm)));
			}
			tempCalculater(calliData, dist); 
		} 
		else if(minPunkt.x() < corner2.x() && minPunkt.x() > corner2.x() - (20*intppcm) && minPunkt.y() > corner2.y() && minPunkt.y() < corner2.y() +(20*intppcm))
		{ 
			// corner 2
			if(!minIsTemp){
				minPunkt.x(minPunkt.x()-((int)(14*intppcm)));
				minPunkt.y(minPunkt.y()+((int)(14*intppcm)));
			}
			tempCalculater(calliData, dist);
		} 
		else if(minPunkt.x() > corner3.x() && minPunkt.x() < corner3.x() + (20*intppcm) &&	minPunkt.y()-10 < corner3.y() && minPunkt.y() > corner3.y() -(20*intppcm))
		{ 
			// corner3
			if(!minIsTemp){
				minPunkt.x(minPunkt.x()+((int)(14*intppcm)));
				minPunkt.y(minPunkt.y()-((int)(14*intppcm)));
			}
			tempCalculater(calliData, dist); 
		} 
		else if(minPunkt.x() <corner4.x() && minPunkt.x() > corner4.x() - (20*intppcm) && minPunkt.y() < corner4.y() && minPunkt.y() > corner4.y() - (20*intppcm))
		{ 
			// corner4
			if(!minIsTemp){
				minPunkt.x(minPunkt.x()-((int)(14*intppcm)));
				minPunkt.y(minPunkt.y()-((int)(14*intppcm)));
			}
			tempCalculater(calliData, dist);
		} 
		else
		{
			// der er ikke sat et temporary point, for bolden ligger ikke i et hjørne
			backMove = 0;
			minIsTemp = false;
			angleCal(calliData, minPunkt); // udregner vinkel 
		}
	}


	private void deliverBalls(GUIInfo calliData, CalcDist dist) { // , int middelX, int middelY
		/*
		 * Bolde afleveres ved i 1. runde at køre til et punkt udfra midten af mål A,
		 * i anden runde at køre til et punkt tættere på og udfor mål A.
		 * Nu burde robotten pege på mål A. 
		 */

		if(toGoal == 0){
			toGoal = 1;
			minPunkt.x(pitch.getGoalA().x()-(int)(45*ppcm));
			minPunkt.y(pitch.getGoalA().y());
			angleCal(calliData, minPunkt);
			route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)+3 * ppcm);
			minIsTemp = true;
		} else {
			toGoal = 2;
			minPunkt.x(pitch.getGoalA().x() - (int)(30*ppcm));
			minPunkt.y(pitch.getGoalA().y());
			angleCal(calliData, minPunkt);
			route.setMinLength(Math.abs(dist.Calcdist(roboBagPunkt, minPunkt)+3 * ppcm));
			minIsTemp = true;
		}
	}


	private void findWayAround(GUIInfo calliData, CalcDist dist, int middelX, int middelY) {
		/*
		 * minpunkt sættes til nærmeste "verdenshjørne" (1 af de 4 punkter ud for korset)
		 */

		if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() < middelY))
		{
			// near corner1
			minPunkt.x(pitch.getNorth().x());
			minPunkt.y(pitch.getNorth().y());
			aroundCross(calliData, dist, minPunkt);

		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() < middelY))
		{
			// near corner2
			minPunkt.x(pitch.getEast().x());
			minPunkt.y(pitch.getEast().y());
			aroundCross(calliData, dist, minPunkt);

		}
		else if((roboFrontPunkt.x() < middelX && roboFrontPunkt.y() > middelY))
		{
			// near corner3
			minPunkt.x(pitch.getWest().x());
			minPunkt.y(pitch.getWest().y());
			aroundCross(calliData, dist, minPunkt);

		}
		else if((roboFrontPunkt.x() > middelX && roboFrontPunkt.y() > middelY))
		{
			// near corner4
			minPunkt.x(pitch.getSouth().x());
			minPunkt.y(pitch.getSouth().y());
			aroundCross(calliData, dist, minPunkt);
		}
	}


	public void aroundCross(GUIInfo calliData, CalcDist dist, CvPoint tempPoint) {
		angleCal(calliData, tempPoint);
		route.setMinLength(dist.Calcdist(roboBagPunkt, tempPoint)+6*ppcm);
		minIsTemp = true; //betyder den skal ikke grappe
	}


	public boolean borderIsGrapped(CvPoint currentPoint)
	{
		/*
		 * Tjek om robotten har fået fat i kanten (er "for langt ude" på billedet, til at andet kan være tilfældet)
		 */

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



	public void tempCalculater(GUIInfo calliData, CalcDist dist) {
		/*
		 * Bruges af edgeCheck() til at hjørner
		 */
		
		if(isTemp==0)
		{
			angleCal(calliData, minPunkt);
			route.setMinLength(dist.Calcdist(roboBagPunkt, minPunkt)+1*ppcm);
			minIsTemp = true; //betyder den skal ikke grappe
			isTemp = 1;
		}
		else
		{
			angleCal(calliData, minPunkt);
			route.setMinLength((float) (0));
			minIsTemp = false;
			isTemp = 0;
			backMove = 1;
		}
	}
	
	
	public void tempCalculater2(GUIInfo calliData, CalcDist dist) {
		/*
		 * Bruges af edgeCheck() til at hjørner
		 */
		
		if(isTemp==0)
		{
			angleCal(calliData, minPunkt);
			route.setMinLength((dist.Calcdist(roboBagPunkt, minPunkt))+2*ppcm);
			minIsTemp = true; //betyder den skal ikke grappe
			isTemp = 1;
		}
		else
		{
			angleCal(calliData, minPunkt);
			route.setMinLength(0);
			minIsTemp = false;
			isTemp = 0;
			backMove = 1;
		}
	}
	
	public void angleCal(GUIInfo calliData, CvPoint destination) 
	{ 
		// calculates angel between robo bagpunkt and destination
		
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

		/*
		 * Bestemmer hvilke kommandoer, der skal sendes til robotten alt efter værdien af borderIsGrapped(), minIsTemp, toGoal og backMove
		 * Beregner hvilke værdier, der skal sendes til robotten udfra vinkel og distance 
		 */

		if(borderIsGrapped(roboFrontPunkt)){
			try {
				dosSend(71, 71);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{

			int angle = Math.round(Float.parseFloat(""+ calliData.getTurnAngle()));

			try {

				if (Math.abs(angle) < 250) {
					if (angle > 0) // vælger retning der skal drejes
						Case = 21;
					else
						Case = 12;
				}
				else
				{
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
				
				int distance = (int) (((route.getMinLength()* Math.round(calliData.getlengthMultiply()) / pitch.getPixPerCm()))); // længde konvertering


				if(distance > 2000){
					distance = 0;
				}

				if(!minIsTemp && backMove == 0){
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
					turnBeforeGrab(calliData, angle);

					Case = 31;
					i = 31;
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
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	private void turnBeforeGrab(GUIInfo calliData, int angle)throws IOException, InterruptedException {
		int Case;
		int i;
		
		/*
		 * Retter ind lige før fx bold fanges, så vinklen bliver mere præcis
		 */
		
		do {
			takepic.takePicture();	
			// ################## Cut image ####################################
			pitch.cutOrigImg();
			pitch.adjustToCut(2, 4);
			// ################### Find Robot #######################################
			balls.findCircle(calliData.getIntJlroboMin(), calliData.getIntJlroboMax(),	calliData.getIntJlroboDP(),calliData.getIntJlroboMinDist(),calliData.getIntJlroboPar1(), calliData.getIntJlroboPar2(),"robo", true);

		} while (balls.determineDirection() == false);
		balls.calculateRotationPoint(); 
		balls.changePerspective(calliData.getPoV());

		roboBagPunkt = balls.getRoboBagPunkt();
		roboFrontPunkt = balls.getRoboFrontPunkt();
		
		if(toGoal == 2)
		{
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
	
	
	public void dosSend(int Case, int i) throws IOException
	{
		/*
		 * Sender til robotten
		 */
		
		dos.write(Case);
		dos.flush();
		dos.write(i);
		dos.flush();
	}
}