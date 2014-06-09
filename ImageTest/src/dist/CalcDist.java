package dist;

public class CalcDist {
	Punkt p1, p2;
	public int Calcdist(Punkt punkt1, Punkt punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		int length = (int) Math.sqrt((p1.getX()-p2.getX()) * (p1.getX()-p2.getX())+
								  (p1.getY()-p2.getY()) * (p1.getY()-p2.getY()));

		return length;
	}
	@Override
	public String toString() {
		return "(" + p1 + "," + p2 + ")";
	}
	public int CalcAngel(Punkt punkt1, Punkt punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		int delta_x = p2.getX() - p1.getX();
		int delta_y = p2.getY() - p1.getY();
		int angle = (int) Math.atan2(delta_y, delta_x);

		return angle;
	}
}



