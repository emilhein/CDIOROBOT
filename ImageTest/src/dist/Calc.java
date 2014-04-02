package dist;

public class Calc {
	Punkt p1, p2;
	public double Calcdist(Punkt punkt1, Punkt punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		double length = Math.sqrt((p1.getX()-p2.getX()) * (p1.getX()-p2.getX())+
								  (p1.getY()-p2.getY()) * (p1.getY()-p2.getY()));

		return length;
	}
	public double CalcAngel(Punkt punkt1, Punkt punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		double delta_x = p2.getX() - p1.getX();
		double delta_y = p2.getY() - p1.getY();
		double angle = Math.atan2(delta_y, delta_x);

		return angle;
	}
}



