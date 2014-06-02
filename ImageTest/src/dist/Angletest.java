package dist;

public class Angletest {

	public static void main(String[] args) {
		Punkt roboFrontPunkt = new Punkt(3,2);
		Punkt roboBagPunkt = new Punkt(2,2);
		Punkt minPunkt = new Punkt(2,3);
		
		Punkt nyRoboFront = new Punkt(roboFrontPunkt.getX()-roboBagPunkt.getX(),roboFrontPunkt.getY()-roboBagPunkt.getY());
		Punkt nyRoboBag = new Punkt(0,0);
		Punkt nyMinPunkt = new Punkt(minPunkt.getX()-roboBagPunkt.getX(),minPunkt.getY()-roboBagPunkt.getY());
		
		CalcAngle Angle = new CalcAngle();
		int BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
		int RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
		int TurnAngle = BallAngle - RoboAngle;
		System.out.println("turnangle = " + TurnAngle);
	}
}
