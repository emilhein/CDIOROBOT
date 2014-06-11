package CallibratorGUI;

import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

import dist.CalcAngle;
import dist.CalcDist;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import pictureToMat.*;

public class CallibratorGUI {

	static JFrame frame1;
	static Container pane;
	static JButton btnApply, btnConnect, btnDeliver;
	static JLabel lblDP, lblmaxgrøn, lblmaxblå, lblmaxrød, lblminrød,
			lblCirkleDIst, jl13, jl14, jl15, jl16, lblParameter1, lblRoboDP,
			jlsep, lblParameter2, lblMinradius, lblMaxradius, lblRoboMinDist,
			lblRoboPar1, lblRoboPar2, jl1, jl2, jl3, jl4, jl5, jl6, jl7, jl8,
			jl9, jl10, jl11, jl12, lblimg, lblafterc, lblfindb, lblbh, lbledge,
			lbltxt, lbltxt2, lbltxt3, lbltxt4, lblromin, lblromax, lblvinkel,
			lbllm, lblluk, lblpov, jl17, jl18, jl19, jl20;
	static JTextField txtDP, txtmaxgrøn, txtmaxblå, txtmaxrød, txtminrød,
			txtRoboDP, txtCirkleDIst, txtParameter1, txtParameter2,
			txtMinradius, txtMaxradius, txtromin, txtromax, txtRoboMinDist,
			txtRoboPar1, txtRoboPar2, txtvinkel, txtlm, txtluk, txtpov;
	static ImageIcon img, afterc, findb, bh, edge;
	static Insets insets;
	static JTextArea txtArea1;

	static int TurnAngle = 0;
	static int minLength = 0;
	static float ppcm = 0;
	static int ballCount = 0;
	static int count = 0;
	static char firstRun = 'a';
	

	public static void main(String args[]) throws IOException {

		// try{

		
		// prøver at forbinde til vores robot
		NXTInfo nxtInfo = new NXTInfo(2, "G9 awesome!", "0016530918D4");
		NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");//robot nr 2
		NXTConnector connt = new NXTConnector();
		System.out.println("trying to connect");
		connt.connectTo(nxtInfo, NXTComm.LCP);
		System.out.println("connected"); // forbundet
		// åbner streams}
		final OutputStream dos = connt.getOutputStream();

		// Scanner scan = new Scanner(System.in);
		// while(true){

		// Opretter rammen
		frame1 = new JFrame("CallibratorGUI");

		// Sætter størrelsen af rammen i pixelx
		frame1.setSize(1300, 718);
		frame1.setBackground(Color.lightGray);

		// Prepare panel
		pane = frame1.getContentPane();
		insets = pane.getInsets();

		// pane.setLayout (null);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImageIcon afterc = new ImageIcon("billed0.png");
		lblafterc = new JLabel(afterc, JLabel.CENTER);

		ImageIcon img = new ImageIcon("RouteTest3.png");
		lblimg = new JLabel(img, JLabel.CENTER);

		ImageIcon findb = new ImageIcon("robo.png");
		lblfindb = new JLabel(findb, JLabel.CENTER);

		ImageIcon bh = new ImageIcon("balls.png"); // readyForBallMethodGrey
		lblbh = new JLabel(bh, JLabel.CENTER);

		ImageIcon edge = new ImageIcon("edge.png");
		lbledge = new JLabel(edge, JLabel.CENTER);

		btnDeliver = new JButton("Deliver");
		btnConnect = new JButton("Send");
		btnApply = new JButton("Apply");
		lblDP = new JLabel("DP:");
		lblromin = new JLabel("RoboMin");
		lblromax = new JLabel("RoboMax");
		lblCirkleDIst = new JLabel("Cirkle Dist:");
		lblParameter1 = new JLabel("Parameter 1:");
		lblParameter2 = new JLabel("Parameter 2:");
		lblMinradius = new JLabel("Min radius:");
		lblMaxradius = new JLabel("Max radius:");
		lblRoboDP = new JLabel("RoboDP:");
		lblRoboMinDist = new JLabel("RoboMinDist");
		lblRoboPar1 = new JLabel("RoboParameter1");
		lblRoboPar2 = new JLabel("RoboParameter2");
		jlsep = new JLabel("=================");
		lblmaxgrøn = new JLabel("MaxGrøn");
		lblmaxblå = new JLabel("MaxBlå");
		lblmaxrød = new JLabel("MaxRød");
		lblminrød = new JLabel("MinRød");
		lblvinkel = new JLabel("Vinkel");
		lbllm = new JLabel("LM");
		lblluk = new JLabel("Luk");
		lblpov = new JLabel("PowM");

		btnConnect.setBackground(Color.PINK);
		btnApply.setBackground(Color.PINK);
		btnDeliver.setBackground(Color.PINK);

		txtArea1 = new JTextArea(1, 1);
		lbltxt = new JLabel();
		lbltxt2 = new JLabel();
		lbltxt3 = new JLabel();
		lbltxt4 = new JLabel();
		jl1 = new JLabel();
		jl2 = new JLabel();
		jl3 = new JLabel();
		jl4 = new JLabel();
		jl5 = new JLabel();
		jl6 = new JLabel();
		jl7 = new JLabel();
		jl8 = new JLabel();
		jl9 = new JLabel();
		jl10 = new JLabel();
		jl11 = new JLabel();
		jl12 = new JLabel();
		jl13 = new JLabel();
		jl14 = new JLabel();
		jl15 = new JLabel();
		jl16 = new JLabel();
		jl17 = new JLabel();
		jl18 = new JLabel();
		jl19 = new JLabel();
		jl20 = new JLabel();
		txtDP = new JTextField(10);
		txtCirkleDIst = new JTextField(10);
		txtParameter1 = new JTextField(10);
		txtParameter2 = new JTextField(5);
		txtMinradius = new JTextField(10);
		txtMaxradius = new JTextField(10);
		txtRoboDP = new JTextField(10);
		txtromin = new JTextField(10);
		txtromax = new JTextField(10);
		txtRoboMinDist = new JTextField(10);
		txtRoboPar1 = new JTextField(10);
		txtRoboPar2 = new JTextField(10);
		txtmaxgrøn = new JTextField(10);
		txtmaxblå = new JTextField(10);
		txtmaxrød = new JTextField(10);
		txtminrød = new JTextField(10);
		txtvinkel = new JTextField(10);
		txtlm = new JTextField(10);
		txtluk = new JTextField(10);
		txtpov = new JTextField(10);

		// BOLDE
		txtDP.setText("1");
		txtCirkleDIst.setText("10");
		txtParameter1.setText("50");
		txtParameter2.setText("14");
		txtMinradius.setText("13");
		txtMaxradius.setText("18");
		// ROBOT
		txtRoboDP.setText("1");
		txtromin.setText("19");
		txtromax.setText("28");
		txtRoboMinDist.setText("60");
		txtRoboPar1.setText("50");
		txtRoboPar2.setText("13");
		// FARVE
		txtmaxgrøn.setText("65");
		txtmaxblå.setText("40");
		txtmaxrød.setText("160");
		txtminrød.setText("40");
		// NYT TIL ROBOT
		txtvinkel.setText("2.133");
		txtlm.setText("2.4");
		txtluk.setText("5");
		txtpov.setText("0");
		// Tilføjer alle komponenter
		pane.add(jl1);
		pane.add(jl2);
		pane.add(jl3);
		pane.add(jl4);
		pane.add(jl5);
		pane.add(jl6);
		pane.add(jl7);
		pane.add(jl8);
		pane.add(jl9);
		pane.add(lblDP);
		pane.add(lblCirkleDIst);
		pane.add(lblParameter1);
		pane.add(lblParameter2);
		pane.add(lblMinradius);
		pane.add(lblMaxradius);
		pane.add(lblRoboDP);
		pane.add(lblvinkel);
		pane.add(lbllm);
		pane.add(lblluk);
		pane.add(txtDP);
		pane.add(txtCirkleDIst);
		pane.add(txtParameter1);
		pane.add(txtParameter2);
		pane.add(txtMinradius);
		pane.add(txtMaxradius);
		pane.add(txtRoboDP);
		pane.add(txtpov);
		pane.add(btnApply);
		pane.add(btnConnect);
		pane.add(btnDeliver);

		pane.add(lblimg);
		pane.add(lblafterc);
		pane.add(lblfindb);
		pane.add(lblbh);
		pane.add(lbltxt);
		pane.add(lbltxt2);
		pane.add(lbltxt2);
		pane.add(lbltxt4);
		pane.add(txtArea1);
		pane.add(lbledge);
		pane.add(lblromin);
		pane.add(lblromax);
		pane.add(txtromin);
		pane.add(txtromax);
		pane.add(lblRoboMinDist);
		pane.add(lblRoboPar1);
		pane.add(lblRoboPar2);
		pane.add(txtRoboMinDist);
		pane.add(txtRoboPar1);
		pane.add(txtRoboPar2);
		pane.add(jlsep);
		pane.add(lblmaxgrøn);
		pane.add(txtmaxgrøn);
		pane.add(lblmaxblå);
		pane.add(txtmaxblå);
		pane.add(lblmaxrød);
		pane.add(txtmaxrød);
		pane.add(lblminrød);
		pane.add(txtminrød);
		pane.add(lblpov);
		pane.add(jl13);
		pane.add(jl14);
		pane.add(jl15);
		pane.add(jl16);
		pane.add(jl17);
		pane.add(jl18);
		pane.add(jl19);
		pane.add(jl20);
		pane.add(txtvinkel);
		pane.add(txtlm);
		pane.add(txtluk);

		// //Placerer alle kompoenter

		lblmaxgrøn.setBounds(lblmaxgrøn.getX() + lblmaxgrøn.getWidth() + 1010,
				insets.top + 236, lblmaxgrøn.getPreferredSize().width,
				lblmaxgrøn.getPreferredSize().height);
		txtmaxgrøn.setBounds(txtmaxgrøn.getX() + txtmaxgrøn.getWidth() + 1010,
				insets.top + 251, txtmaxgrøn.getPreferredSize().width,
				txtmaxgrøn.getPreferredSize().height);

		txtmaxgrøn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input13 = txtmaxgrøn.getText();
				jl13.setText(input13);
				jl13.setBounds(1155, insets.top + 251,
						jl13.getPreferredSize().width,
						jl13.getPreferredSize().height);
			}
		});
		frame1.add(jl13);

		lblmaxblå.setBounds(lblmaxblå.getX() + lblmaxblå.getWidth() + 1010,
				insets.top + 281, lblmaxblå.getPreferredSize().width,
				lblmaxblå.getPreferredSize().height);
		txtmaxblå.setBounds(txtmaxblå.getX() + txtmaxblå.getWidth() + 1010,
				insets.top + 296, txtmaxblå.getPreferredSize().width,
				txtmaxblå.getPreferredSize().height);

		txtmaxblå.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input14 = txtmaxblå.getText();
				jl14.setText(input14);
				jl14.setBounds(1155, insets.top + 296,
						jl14.getPreferredSize().width,
						jl14.getPreferredSize().height);
			}
		});
		frame1.add(jl14);

		lblmaxrød.setBounds(lblmaxrød.getX() + lblmaxrød.getWidth() + 1010,
				insets.top + 321, lblmaxrød.getPreferredSize().width,
				lblmaxrød.getPreferredSize().height);
		txtmaxrød.setBounds(txtmaxrød.getX() + txtmaxrød.getWidth() + 1010,
				insets.top + 336, txtmaxrød.getPreferredSize().width,
				txtmaxrød.getPreferredSize().height);

		txtmaxrød.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input15 = txtmaxrød.getText();
				jl15.setText(input15);
				jl15.setBounds(1155, insets.top + 336,
						jl15.getPreferredSize().width,
						jl15.getPreferredSize().height);
			}
		});
		frame1.add(jl15);

		lblminrød.setBounds(lblminrød.getX() + lblminrød.getWidth() + 1010,
				insets.top + 366, lblminrød.getPreferredSize().width,
				lblminrød.getPreferredSize().height);
		txtminrød.setBounds(txtminrød.getX() + txtminrød.getWidth() + 1010,
				insets.top + 381, txtminrød.getPreferredSize().width,
				txtminrød.getPreferredSize().height);

		txtminrød.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String input16 = txtminrød.getText();
				jl16.setText(input16);
				jl16.setBounds(1155, insets.top + 381,
						jl16.getPreferredSize().width,
						jl16.getPreferredSize().height);
			}
		});
		frame1.add(jl16);

		lblDP.setBounds(lblDP.getX() + lblDP.getWidth() + 5, insets.top + 5,
				lblDP.getPreferredSize().width, lblDP.getPreferredSize().height);
		txtDP.setBounds(txtDP.getX() + txtDP.getWidth() + 5, insets.top + 20,
				txtDP.getPreferredSize().width, txtDP.getPreferredSize().height);

		txtDP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtDP.getText();
				jl1.setText(input);
				jl1.setBounds(150, insets.top + 20,
						jl1.getPreferredSize().width,
						jl1.getPreferredSize().height);
			}
		});
		frame1.add(jl1);

		lblCirkleDIst.setBounds(lblCirkleDIst.getX() + lblCirkleDIst.getWidth()
				+ 5, insets.top + 50, lblCirkleDIst.getPreferredSize().width,
				lblCirkleDIst.getPreferredSize().height);
		txtCirkleDIst.setBounds(txtCirkleDIst.getX() + txtCirkleDIst.getWidth()
				+ 5, insets.top + 65, txtCirkleDIst.getPreferredSize().width,
				txtCirkleDIst.getPreferredSize().height);

		txtCirkleDIst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtCirkleDIst.getText();
				jl2.setText(input);
				jl2.setBounds(150, insets.top + 65,
						jl2.getPreferredSize().width,
						jl2.getPreferredSize().height);
			}
		});
		frame1.add(jl2);

		lblParameter1.setBounds(lblParameter1.getX() + lblParameter1.getWidth()
				+ 5, insets.top + 95, lblParameter1.getPreferredSize().width,
				lblParameter1.getPreferredSize().height);
		txtParameter1.setBounds(txtParameter1.getX() + txtParameter1.getWidth()
				+ 5, insets.top + 110, txtParameter1.getPreferredSize().width,
				txtParameter1.getPreferredSize().height);

		txtParameter1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtParameter1.getText();
				jl3.setText(input);
				jl3.setBounds(150, insets.top + 110,
						jl3.getPreferredSize().width,
						jl3.getPreferredSize().height);
			}
		});
		frame1.add(jl3);

		lblParameter2.setBounds(lblParameter2.getX() + lblParameter2.getWidth()
				+ 5, insets.top + 140, lblParameter2.getPreferredSize().width,
				lblParameter2.getPreferredSize().height);
		txtParameter2.setBounds(txtParameter2.getX() + txtParameter2.getWidth()
				+ 5, insets.top + 155, txtParameter2.getPreferredSize().width,
				txtParameter2.getPreferredSize().height);

		txtParameter2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtParameter2.getText();
				jl4.setText(input);
				jl4.setBounds(150, insets.top + 155,
						jl4.getPreferredSize().width,
						jl4.getPreferredSize().height);
			}
		});
		frame1.add(jl4);

		lblMinradius.setBounds(lblMinradius.getX() + lblMinradius.getWidth()
				+ 5, insets.top + 185, lblMinradius.getPreferredSize().width,
				lblMinradius.getPreferredSize().height);
		txtMinradius.setBounds(txtMinradius.getX() + txtMinradius.getWidth()
				+ 5, insets.top + 200, txtMinradius.getPreferredSize().width,
				txtMinradius.getPreferredSize().height);

		txtMinradius.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtMinradius.getText();
				jl5.setText(input);
				jl5.setBounds(150, insets.top + 200,
						jl5.getPreferredSize().width,
						jl5.getPreferredSize().height);
			}
		});
		frame1.add(jl5);

		lblMaxradius.setBounds(lblMaxradius.getX() + lblMaxradius.getWidth()
				+ 5, insets.top + 230, lblMaxradius.getPreferredSize().width,
				lblMaxradius.getPreferredSize().height);
		txtMaxradius.setBounds(txtMaxradius.getX() + txtMaxradius.getWidth()
				+ 5, insets.top + 245, txtMaxradius.getPreferredSize().width,
				txtMaxradius.getPreferredSize().height);

		txtMaxradius.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtMaxradius.getText();
				jl6.setText(input);
				jl6.setBounds(150, insets.top + 240,
						jl6.getPreferredSize().width,
						jl6.getPreferredSize().height);
			}
		});
		frame1.add(jl6);

		// deler robotdelen fra det andet
		jlsep.setBounds(jlsep.getX() + jlsep.getWidth() + 2, insets.top + 268,
				jlsep.getPreferredSize().width, jlsep.getPreferredSize().height);

		frame1.add(jlsep);

		lblRoboDP.setBounds(lblRoboDP.getX() + lblRoboDP.getWidth() + 5,
				insets.top + 285, lblRoboDP.getPreferredSize().width,
				lblRoboDP.getPreferredSize().height);
		txtRoboDP.setBounds(txtRoboDP.getX() + txtRoboDP.getWidth() + 5,
				insets.top + 300, txtRoboDP.getPreferredSize().width,
				txtRoboDP.getPreferredSize().height);

		txtRoboDP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txtRoboDP.getText();
				jl7.setText(input);
				jl7.setBounds(150, insets.top + 300,
						jl7.getPreferredSize().width,
						jl7.getPreferredSize().height);
			}
		});
		frame1.add(jl7);

		lblRoboMinDist.setBounds(lblromin.getX() + lblRoboMinDist.getWidth()
				+ 5, insets.top + 320, lblRoboMinDist.getPreferredSize().width,
				lblRoboMinDist.getPreferredSize().height);
		txtRoboMinDist.setBounds(txtromin.getX() + txtRoboMinDist.getWidth()
				+ 5, insets.top + 335, txtRoboMinDist.getPreferredSize().width,
				txtRoboMinDist.getPreferredSize().height);

		txtRoboMinDist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input8 = txtRoboMinDist.getText();
				jl8.setText(input8);
				jl8.setBounds(150, insets.top + 335,
						jl8.getPreferredSize().width,
						jl8.getPreferredSize().height);
			}
		});
		frame1.add(jl8);

		lblRoboPar1.setBounds(lblRoboPar1.getX() + lblRoboPar1.getWidth() + 5,
				insets.top + 365, lblRoboPar1.getPreferredSize().width,
				lblRoboPar1.getPreferredSize().height);
		txtRoboPar1.setBounds(txtRoboPar1.getX() + txtRoboPar1.getWidth() + 5,
				insets.top + 380, txtRoboPar1.getPreferredSize().width,
				txtRoboPar1.getPreferredSize().height);

		txtRoboPar1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input9 = txtRoboPar1.getText();
				jl9.setText(input9);
				jl9.setBounds(150, insets.top + 380,
						jl9.getPreferredSize().width,
						jl9.getPreferredSize().height);
			}
		});
		frame1.add(jl9);

		lblRoboPar2.setBounds(lblRoboPar2.getX() + lblRoboPar2.getWidth() + 5,
				insets.top + 410, lblRoboPar2.getPreferredSize().width,
				lblRoboPar2.getPreferredSize().height);
		txtRoboPar2.setBounds(txtRoboPar2.getX() + txtRoboPar2.getWidth() + 5,
				insets.top + 425, txtRoboPar2.getPreferredSize().width,
				txtRoboPar2.getPreferredSize().height);

		txtRoboPar2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input10 = txtRoboPar2.getText();
				jl10.setText(input10);
				jl10.setBounds(150, insets.top + 425,
						jl10.getPreferredSize().width,
						jl10.getPreferredSize().height);
			}
		});
		frame1.add(jl10);

		lblromin.setBounds(lblromin.getX() + lblromin.getWidth() + 5,
				insets.top + 455, lblromin.getPreferredSize().width,
				lblromin.getPreferredSize().height);
		txtromin.setBounds(txtromin.getX() + txtromin.getWidth() + 5,
				insets.top + 470, txtromin.getPreferredSize().width,
				txtromin.getPreferredSize().height);

		txtromin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input11 = txtromin.getText();
				jl11.setText(input11);
				jl11.setBounds(150, insets.top + 470,
						jl11.getPreferredSize().width,
						jl11.getPreferredSize().height);
			}
		});
		frame1.add(jl11);

		lblromax.setBounds(lblromax.getX() + lblromax.getWidth() + 5,
				insets.top + 500, lblromax.getPreferredSize().width,
				lblromax.getPreferredSize().height);
		txtromax.setBounds(txtromax.getX() + txtromax.getWidth() + 5,
				insets.top + 515, txtromax.getPreferredSize().width,
				txtromax.getPreferredSize().height);

		txtromax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input12 = txtromax.getText();
				jl12.setText(input12);
				jl12.setBounds(150, insets.top + 515,
						jl12.getPreferredSize().width,
						jl12.getPreferredSize().height);
			}
		});
		frame1.add(jl12);

		lblvinkel.setBounds(lblvinkel.getX() + lblvinkel.getWidth() + 1010,
				insets.top + 411, lblvinkel.getPreferredSize().width,
				lblvinkel.getPreferredSize().height);
		txtvinkel.setBounds(txtvinkel.getX() + txtvinkel.getWidth() + 1010,
				insets.top + 426, txtvinkel.getPreferredSize().width,
				txtvinkel.getPreferredSize().height);

		txtvinkel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input17 = txtvinkel.getText();
				jl17.setText(input17);
				jl17.setBounds(1155, insets.top + 426,
						jl17.getPreferredSize().width,
						jl17.getPreferredSize().height);
			}
		});
		frame1.add(jl17);

		
		lbllm.setBounds(lbllm.getX() + lbllm.getWidth() + 1010,
				insets.top + 451, lbllm.getPreferredSize().width,
				lbllm.getPreferredSize().height);
		txtlm.setBounds(txtlm.getX() + txtlm.getWidth() + 1010,
				insets.top + 466, txtlm.getPreferredSize().width,
				txtlm.getPreferredSize().height);

		txtlm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input18 = txtlm.getText();
				jl18.setText(input18);
				jl18.setBounds(1155, insets.top + 466,
						jl18.getPreferredSize().width,
						jl18.getPreferredSize().height);

			}
		});
		frame1.add(jl18);
		
		
		lblluk.setBounds(lblluk.getX() + lblluk.getWidth() + 1010,
				insets.top + 496, lblluk.getPreferredSize().width,
				lblluk.getPreferredSize().height);
		txtluk.setBounds(txtluk.getX() + txtluk.getWidth() + 1010,
				insets.top + 511, txtluk.getPreferredSize().width,
				txtluk.getPreferredSize().height);

		txtluk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input19 = txtluk.getText();
				jl19.setText(input19);
				jl19.setBounds(1155, insets.top + 511,
						jl19.getPreferredSize().width,
						jl19.getPreferredSize().height);

			}
		});
		frame1.add(jl19);
		
		
		lblpov.setBounds(lblpov.getX() + lblpov.getWidth() + 1010, insets.top + 536, lblpov.getPreferredSize().width, lblpov.getPreferredSize().height);
		txtpov.setBounds(txtpov.getX() + txtpov.getWidth() + 1010, insets.top + 551, txtpov.getPreferredSize().width, txtpov.getPreferredSize().height);

		txtpov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input20 = txtpov.getText();
				jl20.setText(input20);
				jl20.setBounds(1155, insets.top + 551,
						jl20.getPreferredSize().width,
						jl20.getPreferredSize().height);

			}
		});
		frame1.add(jl20);
		
		
		btnApply.setBounds(btnApply.getX() + btnApply.getWidth() + 5,
				insets.top + 550, btnApply.getPreferredSize().width,
				btnApply.getPreferredSize().height);

		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//for(int j = 0; j<2;j++){
			//	while(true){
				long startbutton = System.currentTimeMillis();

				CvPoint minPunkt;
				long startbuttoncheck = System.currentTimeMillis();

				String input1 = txtDP.getText();
				jl1.setText(input1);
				jl1.setBounds(150, insets.top + 20,
						jl1.getPreferredSize().width,
						jl1.getPreferredSize().height);

				String input2 = txtCirkleDIst.getText();
				jl2.setText(input2);
				jl2.setBounds(150, insets.top + 65,
						jl2.getPreferredSize().width,
						jl2.getPreferredSize().height);

				String input3 = txtParameter1.getText();
				jl3.setText(input3);
				jl3.setBounds(150, insets.top + 110,
						jl3.getPreferredSize().width,
						jl3.getPreferredSize().height);

				String input4 = txtParameter2.getText();
				jl4.setText(input4);
				jl4.setBounds(150, insets.top + 155,
						jl4.getPreferredSize().width,
						jl4.getPreferredSize().height);

				String input5 = txtMinradius.getText();
				jl5.setText(input5);
				jl5.setBounds(150, insets.top + 200,
						jl5.getPreferredSize().width,
						jl5.getPreferredSize().height);

				String input6 = txtMaxradius.getText();
				jl6.setText(input6);
				jl6.setBounds(150, insets.top + 240,
						jl6.getPreferredSize().width,
						jl6.getPreferredSize().height);

				String input7 = txtRoboDP.getText();
				jl7.setText(input7);
				jl7.setBounds(150, insets.top + 300,
						jl7.getPreferredSize().width,
						jl7.getPreferredSize().height);

				// dist
				String input8 = txtRoboMinDist.getText();
				jl8.setText(input8);
				jl8.setBounds(150, insets.top + 335,
						jl8.getPreferredSize().width,
						jl8.getPreferredSize().height);

				// 1
				String input9 = txtRoboPar1.getText();
				jl9.setText(input9);
				jl9.setBounds(150, insets.top + 380,
						jl9.getPreferredSize().width,
						jl9.getPreferredSize().height);

				// 2
				String input10 = txtRoboPar2.getText();
				jl10.setText(input10);
				jl10.setBounds(150, insets.top + 425,
						jl10.getPreferredSize().width,
						jl10.getPreferredSize().height);

				// min
				String input11 = txtromin.getText();
				jl11.setText(input11);
				jl11.setBounds(150, insets.top + 470,
						jl11.getPreferredSize().width,
						jl11.getPreferredSize().height);

				// max
				String input12 = txtromax.getText();
				jl12.setText(input12);
				jl12.setBounds(150, insets.top + 515,
						jl12.getPreferredSize().width,
						jl12.getPreferredSize().height);

				String input13 = txtmaxgrøn.getText();
				jl13.setText(input13);
				jl13.setBounds(1155, insets.top + 251,
						jl13.getPreferredSize().width,
						jl13.getPreferredSize().height);

				String input14 = txtmaxblå.getText();
				jl14.setText(input14);
				jl14.setBounds(1155, insets.top + 296,
						jl14.getPreferredSize().width,
						jl14.getPreferredSize().height);

				String input15 = txtmaxrød.getText();
				jl15.setText(input15);
				jl15.setBounds(1155, insets.top + 336,
						jl15.getPreferredSize().width,
						jl15.getPreferredSize().height);

				String input16 = txtminrød.getText();
				jl16.setText(input16);
				jl16.setBounds(1155, insets.top + 381,
						jl16.getPreferredSize().width,
						jl16.getPreferredSize().height);

				String input17 = txtvinkel.getText();
				jl17.setText(input17);
				jl17.setBounds(1155, insets.top + 426,
						jl17.getPreferredSize().width,
						jl17.getPreferredSize().height);

				String input18 = txtlm.getText();
				jl18.setText(input18);
				jl18.setBounds(1155, insets.top + 466,
						jl18.getPreferredSize().width,
						jl18.getPreferredSize().height);

				String input19 = txtluk.getText();
				jl19.setText(input19);
				jl19.setBounds(1155, insets.top + 511,
						jl19.getPreferredSize().width,
						jl19.getPreferredSize().height);
				
				String input20 = txtpov.getText();
				jl20.setText(input20);
				jl20.setBounds(1155, insets.top + 551,
						jl20.getPreferredSize().width,
						jl20.getPreferredSize().height);
				
//
				long endbuttoncheck = System.currentTimeMillis();
				System.out.println("buttoncheck = " +(endbuttoncheck-startbuttoncheck));

				
				
				long startpicture = System.currentTimeMillis();

				TakePicture takepic = new TakePicture();
				takepic.takePicture();
				long endpicture = System.currentTimeMillis();
				System.out.println("taking and saving a picture took = " +(endpicture-startpicture));
				
				// BufferedImage src = ImageIO.read(new File("Billed0.png"));
				DetectRects findEdge = new DetectRects();
				long startedge = System.currentTimeMillis();
				
				findEdge.detectAllRects();
				ppcm = findEdge.getPixPerCm();
				
				long endedge = System.currentTimeMillis();
				System.out.println("detectALLRects took = " +(endedge-startedge));
				
				ballMethod balls = new ballMethod();
				
				long startpictomat = System.currentTimeMillis();
				balls.pictureToMat("billed0.png");
				long endpictomat = System.currentTimeMillis();
				System.out.println("PicToMat took = " +(endpictomat-startpictomat));
				
				long startfindrobo = System.currentTimeMillis();
				ArrayList<Float> RoboCoor = balls.findCircle(
						Integer.parseInt(jl11.getText()),
						Integer.parseInt(jl12.getText()),
						Integer.parseInt(jl7.getText()),
						Integer.parseInt(jl8.getText()),
						Integer.parseInt(jl9.getText()),
						Integer.parseInt(jl10.getText()), "robo", true);// minradius,
																		// maxrdius,
																		// antalbolde
				long endfindrobo = System.currentTimeMillis();
				System.out.println("find robo took = " +(endfindrobo-startfindrobo));

				// float[] RoboCoor = balls.findCircle(19, 28,
				// 1,5,50,5,2,"robo", true); // finder robo
				// for(int j = 0; j<RoboCoor.size();j=j+3){
				// }

				Mat frame = Highgui.imread("robo.png"); // henter
																		// det
																		// konverterede
																		// billlede
				double green;
				double red;
				double green2;
				double red2;
				CvPoint roboFrontPunkt = new CvPoint(10, 10);
				CvPoint roboBagPunkt = new CvPoint(20, 20);
				
				
					double[] front = frame.get(Math.round(RoboCoor.get(1)),	Math.round(RoboCoor.get(0))); // /Y OG X ER BYTTET OM
					green = front[1];
					red = front[2];

					double[] back = frame.get(Math.round(RoboCoor.get(4)),	Math.round(RoboCoor.get(3))); // /
					green2 = back[1];
					red2 = back[2];

				
					long startdirection = System.currentTimeMillis();

					determineDirection(RoboCoor, green, red, green2, red2,roboFrontPunkt, roboBagPunkt);

					long enddirection = System.currentTimeMillis();
					System.out.println("direction took = " +(enddirection-startdirection));

				
					long startballs = System.currentTimeMillis();

				ArrayList<Float> ballCoor = balls.findCircle(
						Integer.parseInt(jl5.getText()),
						Integer.parseInt(jl6.getText()),
						Integer.parseInt(jl1.getText()),
						Integer.parseInt(jl2.getText()),
						Integer.parseInt(jl3.getText()),
						Integer.parseInt(jl4.getText()), "balls", false);// minradius,
																			// maxrdius,
																			// antalbolde
				long endballs = System.currentTimeMillis();
				System.out.println("finding balls took = " +(endballs-startballs));

				
				
				
				long startroute = System.currentTimeMillis();

				minPunkt = RouteTest.drawBallMap(ballCoor, roboBagPunkt,
						roboFrontPunkt); // tegner dem i testprogrammet

				long endroute = System.currentTimeMillis();
				System.out.println("drawing ballmap took = " +(endroute-startroute));
				
				
				
				long startangle = System.currentTimeMillis();

				if(firstRun == 'a'){
				ballCount = (ballCoor.size() / 3);
				firstRun = 'b';
				}
				int tempCount = (ballCoor.size() / 3);
				//System.out.println("tempcount = " + tempCount);
				//System.out.println("Ballcount = " + ballCount);
				
				if (tempCount == ballCount - 1) {
					System.out.println("HEJ1");
					count++;
					ballCount = tempCount;
					if (count == 2) {
						System.out.println("HEJ2");

						CvPoint goalA = findEdge.getGoalB();

						minPunkt.x(goalA.x());
						minPunkt.y(goalA.y());
						count = 0;
					//	System.out.println("koordinaterne til Minpunkt er ("
					//			+ minPunkt.x() + "," + minPunkt.y() + ")");
						
					}
				}

				txtArea1 = new JTextArea("Antal bolde fundet: "
						+ (ballCoor.size() / 3), 1, 1);
				String text1 = txtArea1.getText();
				lbltxt.setText(text1);

				CvPoint nyRoboFront = new CvPoint(roboFrontPunkt.x()
						- roboBagPunkt.x(), roboFrontPunkt.y()
						- roboBagPunkt.y());
				CvPoint nyRoboBag = new CvPoint(0, 0);
				CvPoint nyMinPunkt = new CvPoint(minPunkt.x()- roboBagPunkt.x(), minPunkt.y()- roboBagPunkt.y());	
				
				
				CalcAngle Angle = new CalcAngle();
				int BallAngle = Angle.Calcangle(nyMinPunkt, nyRoboBag);
				//System.out.println("BallAngle = " + BallAngle);
				int RoboAngle = Angle.Calcangle(nyRoboFront, nyRoboBag);
				//System.out.println("RoboAngle = " + RoboAngle);
				
				TurnAngle = RoboAngle - BallAngle;
				
				CalcDist dist = new CalcDist();

				
				/*
				
				CvPoint middle = new CvPoint(findEdge.getGoalA().x()+(90*(int)ppcm),findEdge.getGoalA().y()); // in the middle of field

				CvPoint corner3 = new CvPoint(findEdge.getGoalA().x(),findEdge.getGoalA().y()+(60*(int)ppcm));//3
				CvPoint corner1 = new CvPoint(findEdge.getGoalA().x(),findEdge.getGoalA().y()-(60*(int)ppcm));//1
				CvPoint corner4 = new CvPoint(findEdge.getGoalB().x(),findEdge.getGoalB().y()+(60*(int)ppcm));//4
				CvPoint corner2 = new CvPoint(findEdge.getGoalB().x(),findEdge.getGoalB().y()-(60*(int)ppcm));//2 
				
				
				1 												2
				 -------------------------------------------|
				 |											|
				 |											|
				 |											|
				 |											|
				 |					X = middle				|
				 |											|
				 |											|
				 |											|
				 |											|
				 -------------------------------------------|	
				 3												4
				 */
			
				/*
				int distance1  = dist.Calcdist(roboFrontPunkt, corner1);
				int distance2  = dist.Calcdist(roboFrontPunkt, corner2);
				int distance3  = dist.Calcdist(roboFrontPunkt, corner3);
				int distance4  = dist.Calcdist(roboFrontPunkt, corner4);
				double x = 0.02;
				
				if(distance1 < 150){
					System.out.println("Dist1");
					if(RoboAngle > 196){
					TurnAngle = TurnAngle+(int)x*(dist.Calcdist(roboFrontPunkt, middle));
					}
				}
				if(distance2 < 150){
					System.out.println("Dist2");
					System.out.println("TurnAngle = " + TurnAngle);
					if(RoboAngle > 196){
					TurnAngle -= (int)(x*(dist.Calcdist(roboFrontPunkt, middle)));
					}
					System.out.println(((int)x*(dist.Calcdist(roboFrontPunkt, middle))));
					System.out.println("TurnAngle2 = " + TurnAngle);

				}
				if(distance3 < 150){
					System.out.println("Dist3");
					if(RoboAngle > 196){
					TurnAngle = TurnAngle+(int)x*(dist.Calcdist(roboFrontPunkt, middle));
					}
				}
				if(distance4 < 150){
					System.out.println("Dist4");
					System.out.println("TurnAngle = " + TurnAngle);

					if(RoboAngle > 196){
					TurnAngle = TurnAngle-(int)(x*(dist.Calcdist(roboFrontPunkt, middle)));
					System.out.println("TurnAngle = " + TurnAngle);

					}
				}
					
				*/
				long endangle = System.currentTimeMillis();
				System.out.println("ALL angle calculation took = " +(endangle-startangle));
				
				
				
				long startgui = System.currentTimeMillis();

				lbltxt2.setText("BallAngle = " + BallAngle);
				lbltxt3.setText("RoboAngle = " + RoboAngle);
				lbltxt4.setText("TurnAngle = " + (RoboAngle - BallAngle));

				minLength = Math.abs(dist.Calcdist(roboFrontPunkt, minPunkt));

				ImageIcon afterc = new ImageIcon("billed0.png");
				Image image1 = afterc.getImage(); // transform it
				Image afimage = image1.getScaledInstance(400, 225,
						java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
				afterc = new ImageIcon(afimage); // transform it back
				// lblafterc = new JLabel (afterc, JLabel.CENTER);

				ImageIcon img = new ImageIcon("RouteTest3.png");
				Image image2 = img.getImage(); // transform it
				Image dimage = image2.getScaledInstance(400, 225,
						java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
				img = new ImageIcon(dimage); // transform it back
				// lblimg = new JLabel (img, JLabel.CENTER);

				ImageIcon findb = new ImageIcon("robo.png");
				Image image3 = findb.getImage(); // transform it
				Image abimage = image3.getScaledInstance(400, 225,
						java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
				findb = new ImageIcon(abimage); // transform it back
				// lblfindb = new JLabel (findb, JLabel.CENTER);

				ImageIcon bh = new ImageIcon("balls.png"); // readyForBallMethodGrey
				Image image4 = bh.getImage(); // transform it
				Image acimage = image4.getScaledInstance(400, 225,
						java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
				bh = new ImageIcon(acimage); // transform it back
				// lblbh = new JLabel (bh, JLabel.CENTER);

				ImageIcon edge = new ImageIcon("edge.png");
				Image image5 = edge.getImage(); // transform it
				Image edimage = image5.getScaledInstance(300, 169,
						java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
				edge = new ImageIcon(edimage); // transform it back

				lblimg.setIcon(img);
				lblafterc.setIcon(afterc);
				lblfindb.setIcon(findb);
				lblbh.setIcon(bh);
				lbledge.setIcon(edge);

				lblimg.setBounds(200, insets.top + 6,
						lblimg.getPreferredSize().width,
						lblimg.getPreferredSize().height);
				lblafterc.setBounds(605, insets.top + 6,
						lblafterc.getPreferredSize().width,
						lblafterc.getPreferredSize().height);
				lblfindb.setBounds(200, insets.top + 236,
						lblfindb.getPreferredSize().width,
						lblfindb.getPreferredSize().height);
				lblbh.setBounds(605, insets.top + 236,
						lblbh.getPreferredSize().width,
						lblbh.getPreferredSize().height);
				lbledge.setBounds(1010, insets.top + 6,
						lbledge.getPreferredSize().width,
						lbledge.getPreferredSize().height);

				lbltxt.setBounds(200, insets.top + 500,
						lbltxt.getPreferredSize().width, 10);
				lbltxt2.setBounds(200, insets.top + 515,
						lbltxt2.getPreferredSize().width, 10);
				lbltxt3.setBounds(200, insets.top + 530,
						lbltxt3.getPreferredSize().width, 10);
				lbltxt4.setBounds(200, insets.top + 545,
						lbltxt4.getPreferredSize().width, 10);

				
				long endgui = System.currentTimeMillis();
				System.out.println("showing gui took = " +(endgui-startgui));
				
				
				// ballCoor.clear();

				/*
				  System.out.println("In CONNECT");
				  
				  
				  System.out.println("Waiting for your go!");
				  
				//  Scanner scan = new Scanner(System.in); int input =
				//  scan.nextInt();
				  try {
					Thread.sleep(5000);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				  
				  int Case; int i; System.out.println("TurnAngle = " +
				  TurnAngle); int angle = (int)
				  (TurnAngle*(Float.parseFloat(jl17.getText()))); //vinkel konvertering 
				  if(Math.abs(angle) < 250){ if(angle > 0) //vælger retning der  skal drejes 
					  Case = 11; 
				  else Case = 22; } 
				  else{ angle =
				  angle/10; 
				  if(angle > 0) //vælger retning der skal drejes Case
				  Case = 31;
				  else Case = 42; } 
				  angle = Math.abs(angle);
				  
				  try {
				  dos.write(Case); //sender case 
				  dos.flush(); 
				  dos.write(angle);
				  //sender vinkel 
				  dos.flush();
				  
				  Thread.sleep(1500); dos.write(61); //sender case dos.flush();
				  dos.write(61); //sender vinkel dos.flush();
				  Thread.sleep(500);
				  
				  //kører robot frem System.out.println("minlength " +minLength); 
				  int distance =   (int)((minLength*(Float.parseFloat(jl18.getText())))/ppcm);
				  //længde konvertering System.out.println("dist = " + distance); dos.write(81); dos.flush(); if(angle > 180)
				  distance -= 50; i = distance/10; dos.write(i); dos.flush();
				  
				  Thread.sleep((int)(minLength*(Float.parseFloat(jl19.getText())
				  ))); //Thread.sleep((int)minLength*2); //samler bold op
				  dos.write(51); dos.flush(); dos.write(51); dos.flush();
				  
				  
				  } catch (IOException e1) { // TODO Auto-generated catch block
				  e1.printStackTrace(); } 
				  catch (InterruptedException e1) { //
				  }
				  
				
				  
				  
				  }*/
				long endbutton = System.currentTimeMillis();
				System.out.println("The entire apply button took = " + (endbutton-startbutton));
			}

	public void determineDirection(ArrayList<Float> RoboCoor,
					double green, double red, double green2, double red2,
					CvPoint roboFrontPunkt, CvPoint roboBagPunkt) {
				if (red > 245) {
					roboFrontPunkt.x(Math.round(RoboCoor.get(0)));
					roboFrontPunkt.y(Math.round(RoboCoor.get(1)));
					roboBagPunkt.x(Math.round(RoboCoor.get(3)));
					roboBagPunkt.y(Math.round(RoboCoor.get(4)));
				} else if (red2 > 245) {
					roboFrontPunkt.x(Math.round(RoboCoor.get(3)));
					roboFrontPunkt.y(Math.round(RoboCoor.get(4)));
					roboBagPunkt.x(Math.round(RoboCoor.get(0)));
					roboBagPunkt.y(Math.round(RoboCoor.get(1)));
				} else if (green > 245) {
					roboFrontPunkt.x(Math.round(RoboCoor.get(3)));
					roboFrontPunkt.y(Math.round(RoboCoor.get(4)));
					roboBagPunkt.x(Math.round(RoboCoor.get(0)));
					roboBagPunkt.y(Math.round(RoboCoor.get(1)));
				} else if (green2 > 245) {
					roboFrontPunkt.x(Math.round(RoboCoor.get(0)));
					roboFrontPunkt.y(Math.round(RoboCoor.get(1)));
					roboBagPunkt.x(Math.round(RoboCoor.get(3)));
					roboBagPunkt.y(Math.round(RoboCoor.get(4)));
				}
			}
		});

		frame1.add(jl1);
		frame1.add(jl2);
		frame1.add(jl3);
		frame1.add(jl4);
		frame1.add(jl5);
		frame1.add(jl6);
		frame1.add(jl7);
		frame1.add(jl8);
		frame1.add(jl9);
		frame1.add(jl10);
		frame1.add(jl11);
		frame1.add(jl12);
		frame1.add(jl13);
		frame1.add(jl14);
		frame1.add(jl15);
		frame1.add(jl16);
		frame1.add(jl17); // vinkel
		frame1.add(jl18); // lenght
		frame1.add(jl19); // luk
		frame1.add(jl20); //pov
		frame1.add(lblimg);
		frame1.add(lblafterc);
		frame1.add(lblbh);
		frame1.add(lbltxt);
		frame1.add(lbltxt2);
		frame1.add(lbltxt3);
		frame1.add(lbltxt4);
		frame1.add(txtArea1);

		btnDeliver.setBounds(btnDeliver.getX() + btnDeliver.getWidth() + 5,
				insets.top + 590, btnDeliver.getPreferredSize().width,
				btnDeliver.getPreferredSize().height);
		btnDeliver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Delivering balls");
			}
		});

		btnConnect.setBounds(btnConnect.getX() + btnConnect.getWidth() + 80,
				insets.top + 550, btnConnect.getPreferredSize().width,
				btnConnect.getPreferredSize().height);

		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// int input = scan.nextInt();

				int Case;
				int i;
				System.out.println("TurnAngle = " + TurnAngle);
				int angle = (int) (TurnAngle * (Float.parseFloat(jl17.getText()))); // vinkel
																					// konvertering
				System.out.println("angle " + angle);
				try {
					if (Math.abs(angle) < 250) {
						if (angle > 0) // vælger retning der skal drejes
							Case = 11;
						else
							Case = 22;
					} else {
						angle = angle / 2;
						if (angle > 0) // vælger retning der skal drejes
							Case = 11;
						else
							Case = 22;
						dos.write(Case); // sender case
						dos.flush();
						dos.write(angle); // sender vinkel
						dos.flush();
					}
					// else{
					// angle = angle/10;
					// if(angle > 0) //vælger retning der skal drejes
					// Case = 31;
					// else Case = 42;
					// }
					angle = Math.abs(angle);

					dos.write(Case); // sender case
					dos.flush();
					dos.write(angle); // sender vinkel
					dos.flush();

					Thread.sleep(1200);
					dos.write(61); // sender case
					dos.flush();
					dos.write(61); // sender vinkel
					dos.flush();
					Thread.sleep(500);

					// kører robot frem
					System.out.println("minlength " + minLength);
					int distance = (int) ((minLength * (Float.parseFloat(jl18
							.getText()))) / ppcm); // længde konvertering
					System.out.println("dist = " + distance);
					dos.write(81);
					dos.flush();
					if (angle > 180)
						distance -= 50;
					i = distance / 10;
					dos.write(i);
					dos.flush();

					Thread.sleep((int) (minLength * (Float.parseFloat(jl19
							.getText()))));
					// Thread.sleep((int)minLength*2);
					// samler bold op
					dos.write(51);
					dos.flush();
					dos.write(51);
					dos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// }

			}
		});

		// Gør rammen synlig
		frame1.setVisible(true);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}

	}
}
