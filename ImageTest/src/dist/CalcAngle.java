package dist;

public class CalcAngle {
	Punkt p1, p2;
	public int Calcangel(Punkt punkt1, Punkt punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		int delta_x = p2.getX() - p1.getX();
		int delta_y = p2.getY() - p1.getY();
		int angle = (int) Math.atan2(delta_y, delta_x);

		return angle;
	}
}