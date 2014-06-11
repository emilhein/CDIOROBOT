package test;

import pictureToMat.*;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		Detect detecter = new Detect();
		detecter.detectAllRects();
		//detecter.getObstructionCoordis();
		try {
			detecter.getBorderCoordis();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
