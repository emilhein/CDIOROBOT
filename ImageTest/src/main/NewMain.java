package main;

import java.io.IOException;

import CallibratorGUI.CallibratorGUI;

public class NewMain {

	public static void main(String[] args) {
		CallibratorGUI GUI = new CallibratorGUI();
		try {
			GUI.startGUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
