package dist;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class CalcDist {
	CvPoint p1, p2;
	public int Calcdist(CvPoint punkt1, CvPoint punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		int length = (int) Math.sqrt((p1.x()-p2.x()) * (p1.x()-p2.x())+
								  (p1.y()-p2.y()) * (p1.y()-p2.y()));

		return length;
	}
	@Override
	public String toString() {
		return "(" + p1 + "," + p2 + ")";
	}
	public int CalcAngel(CvPoint punkt1, CvPoint punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		int delta_x = p2.x() - p1.x();
		int delta_y = p2.y() - p1.y();
		int angle = (int) Math.atan2(delta_y, delta_x);

		return angle;
	}
}



