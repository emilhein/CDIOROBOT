package main;

import java.io.IOException;

import CallibratorGUI.CallibratorGUI;


public class Main {


	public static void main (String[] args){

		CallibratorGUI GUI = new CallibratorGUI();	// Laver nyt GUI objekt
		try {
			GUI.startGUI();							// Starter GUI, som starter resten
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}


