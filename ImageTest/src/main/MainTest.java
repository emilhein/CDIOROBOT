package main;
import pictureToMat.TakePicture;

public class MainTest {
	
	public static void main(String args[])
	{
		TakePicture tk = new TakePicture();

		for(int i = 0; i < 100; i++)
		{
			tk.takePicture();
		}
	}
}