package dist;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class Angletest {

	public static void main(String[] args) {
		CvPoint roboFrontPunkt = new CvPoint(1,0);
		CvPoint roboBagPunkt = new CvPoint(0,0);
		CvPoint minPunkt = new CvPoint(1,1);
		
		CvPoint nyRoboFront = new CvPoint(roboFrontPunkt.x()-roboBagPunkt.x(),roboFrontPunkt.y()-roboBagPunkt.y());
		CvPoint nyRoboBag = new CvPoint(0,0);
		CvPoint nyMinPunkt = new CvPoint(minPunkt.x()-roboBagPunkt.x(),minPunkt.y()-roboBagPunkt.y());
		
		CalcAngle Angle = new CalcAngle();
		float BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		float RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		float TurnAngle = BallAngle - RoboAngle;
		System.out.println("turnangle = " + TurnAngle);
	}
}
