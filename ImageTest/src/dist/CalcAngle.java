package dist;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class CalcAngle {
	CvPoint p1, p2;
	public int Calcangle(CvPoint punkt1, CvPoint punkt2)
	{
		this.p1 = punkt1;
		this.p2 = punkt2;

		int delta_x = p2.x() - p1.x();
		int delta_y = p2.y() - p1.y();
		int angle = (int) Math.toDegrees(Math.atan2(delta_y, delta_x));

		//if(angle < 0) angle += 360;
		
		return angle;
	} 
}
