package dist;

public class Angletest {

	public static void main(String[] args) {
		Punkt roboFrontPunkt = new Punkt(0,1);
		Punkt roboBagPunkt = new Punkt(0,0);
		Punkt minPunkt = new Punkt(1,0);
		
		CalcAngle Angle = new CalcAngle();
		int BallAngle = Angle.Calcangle(roboBagPunkt, minPunkt);
		int RoboAngle = Angle.Calcangle(roboBagPunkt, roboFrontPunkt);
		int TurnAngle = BallAngle - RoboAngle;
		System.out.println("turnangle = " + TurnAngle);
	}
}
