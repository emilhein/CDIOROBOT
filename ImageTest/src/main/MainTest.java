package main;
import pictureToMat.DetectRects2;

public class MainTest {
	
	public static void main(String args[])
	{
		DetectRects2 DT = new DetectRects2();

		for(int i = 0; i < 100; i++)
		{
			System.out.println(i);
			DT.detectAllRects();;
			System.out.println(DT.getPixPerCm());
			System.out.println(DT.getGoalA());
			System.out.println(DT.getGoalB());
			System.out.println(DT.getInnerRect());
			System.out.println(DT.getObstruction());
		}
	}
}
