package main;

import java.io.IOException;

import CallibratorGUI.CallibratorGUI;


public class Main {


	public static void main (String[] args){

		CallibratorGUI GUI = new CallibratorGUI();
		try {
			GUI.startGUI();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}


