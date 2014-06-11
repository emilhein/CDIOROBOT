package dist;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class Angletest {

	public static void main(String[] args) {
		CvPoint roboFrontPunkt = new CvPoint(3,2);
		CvPoint roboBagPunkt = new CvPoint(2,2);
		CvPoint minPunkt = new CvPoint(2,3);
		
		CvPoint nyRoboFront = new CvPoint(roboFrontPunkt.x()-roboBagPunkt.x(),roboFrontPunkt.y()-roboBagPunkt.y());
		CvPoint nyRoboBag = new CvPoint(0,0);
		CvPoint nyMinPunkt = new CvPoint(minPunkt.x()-roboBagPunkt.x(),minPunkt.y()-roboBagPunkt.y());
		
		CalcAngle Angle = new CalcAngle();
		int BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		int RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		int TurnAngle = BallAngle - RoboAngle;
		System.out.println("turnangle = " + TurnAngle);
	}
}
