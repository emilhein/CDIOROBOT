package CallibratorGUI;

import javax.swing.*;

import data.GUIInfo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import pictureToMat.*;
import Controller.PrimaryController;

public class CallibratorGUI {

	private JFrame frame1;
	private Container pane;
	private JButton btnApply, btnConnect, btnDeliver, btnQuit;
	private JLabel lblDP, lblCirkleDIst, lblParameter1, lblRoboDP,
	jlsep, lblParameter2, lblMinradius, lblMaxradius, lblRoboMinDist,
	lblRoboPar1, lblRoboPar2, jlcircleDP, jlcircleDist, jlcirclePar1, jlcirclePar2, jlcircleMinRadius, jlcircleMaxRadius, jlroboDP, jlroboMinDist,
	jlroboPar1, jlroboPar2, jlroboMin, jlroboMax, lblimg, lblfindb, lblbh, lbledge,
	lbltxt, lbltxtBallAngle, lbltxtRoboAngle, lbltxtTurnAngle, lblromin, lblromax,
	lbllm, lblluk, lblpov, jlLengthMultiply, jlClose, jlPoV;
	private JTextField txtDP, txtRoboDP, txtCirkleDIst, txtParameter1, txtParameter2,
	txtMinradius, txtMaxradius, txtromin, txtromax, txtRoboMinDist,
	txtRoboPar1, txtRoboPar2, txtlm, txtluk, txtpov;
	//	private ImageIcon img, findb, bh, edge;
	private Insets insets;
	private JTextArea txtArea1;

	//private int TurnAngle = 0;
	//	private Float minLength;
	//	private float ppcm = 0;
	//	private ArrayList<Float> RoboCoor;
	//	private ArrayList<Float> ballCoor;
	//private int BallAngle;
	//private int RoboAngle;


	private DetectRects findEdge;
	private PrimaryController control;
	private GUIInfo info;

	private boolean first = true;

	public void startGUI() throws IOException {

		//Opretter objekter af andre klasser
		info = new GUIInfo();
		findEdge = new DetectRects();
		control = new PrimaryController(findEdge);

		// Opretter rammen
		frame1 = new JFrame("CallibratorGUI");

		// Sætter størrelsen af rammen i pixelx
		frame1.setSize(1300, 718);


		// Gør panelet klar
		pane = frame1.getContentPane();
		insets = pane.getInsets();

		pane.setLayout (null);
		pane.setBackground(Color.LIGHT_GRAY);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImageIcon edge = new ImageIcon("edge.png");
		lbledge = new JLabel(edge, JLabel.CENTER);

		ImageIcon findb = new ImageIcon("robo.png");
		lblfindb = new JLabel(findb, JLabel.CENTER);

		ImageIcon bh = new ImageIcon("balls.png"); // readyForBallMethodGrey
		lblbh = new JLabel(bh, JLabel.CENTER);

		ImageIcon img = new ImageIcon("RouteTest3.png");
		lblimg = new JLabel(img, JLabel.CENTER);


		btnDeliver = new JButton("Deliver");						btnConnect = new JButton("Send");
		btnApply = new JButton("Apply");							lblDP = new JLabel("DP:");
		lblromin = new JLabel("RoboMin");							lblromax = new JLabel("RoboMax");
		lblCirkleDIst = new JLabel("Cirkle Dist:");					lblParameter1 = new JLabel("Parameter 1:");
		lblParameter2 = new JLabel("Parameter 2:");					lblMinradius = new JLabel("Min radius:");
		lblMaxradius = new JLabel("Max radius:");					lblRoboDP = new JLabel("RoboDP:");
		lblRoboMinDist = new JLabel("RoboMinDist");					lblRoboPar1 = new JLabel("RoboParameter1");
		lblRoboPar2 = new JLabel("RoboParameter2");					jlsep = new JLabel("=================");
		lbllm = new JLabel("LM");									lblluk = new JLabel("Luk");									
		lblpov = new JLabel("PowM");
		btnQuit = new JButton("Quit");

		btnConnect.setBackground(Color.PINK);
		btnApply.setBackground(Color.PINK);
		btnDeliver.setBackground(Color.PINK);
		btnQuit.setBackground(Color.RED);
		
		txtArea1 = new JTextArea(1, 1);								lbltxt = new JLabel();
		lbltxtBallAngle = new JLabel();								lbltxtRoboAngle = new JLabel();
		lbltxtTurnAngle = new JLabel();								jlcircleDP = new JLabel();
		jlcircleDist = new JLabel();								jlcirclePar1 = new JLabel();
		jlcirclePar2 = new JLabel();								jlcircleMinRadius = new JLabel();
		jlcircleMaxRadius = new JLabel();							jlroboDP = new JLabel();
		jlroboMinDist = new JLabel();								jlroboPar1 = new JLabel();
		jlroboPar2 = new JLabel();									jlroboMin = new JLabel();
		jlroboMax = new JLabel();									
										
		jlLengthMultiply = new JLabel();							jlClose = new JLabel();
		jlPoV = new JLabel();										txtDP = new JTextField(10);
		txtCirkleDIst = new JTextField(10);							txtParameter1 = new JTextField(10);
		txtParameter2 = new JTextField(5);							txtMinradius = new JTextField(10);
		txtMaxradius = new JTextField(10);							txtRoboDP = new JTextField(10);
		txtromin = new JTextField(10);								txtromax = new JTextField(10);
		txtRoboMinDist = new JTextField(10);						txtRoboPar1 = new JTextField(10);
		txtRoboPar2 = new JTextField(10);							
		txtlm = new JTextField(10);									txtluk = new JTextField(10);						
		txtpov = new JTextField(10);

		Float ppcm = (float) 6.2;
		/*if (findEdge.getPixPerCm() > 0) {
			ppcm = findEdge.getPixPerCm();
		}*/
		
		
		//		udregning af diverse for bolde
		int ballsMinRadius = (int)(1.15*ppcm);
		int ballsMaxRadius = (int)(2.1*ppcm);

		//		udregning af diverse for robot
		int roboMinRadius = (int)(4.8*ppcm);
		int roboMaxRadius = (int)(5.5*ppcm);
		
		txtMinradius.setText("" + ballsMinRadius);
		txtMaxradius.setText("" + ballsMaxRadius);
		txtromin.setText("" + roboMinRadius);
		txtromax.setText("" + roboMaxRadius);

		// BOLDE
		txtDP.setText("1");																						
		txtCirkleDIst.setText("12");
		txtParameter1.setText("35");
		txtParameter2.setText("16");
	
		// ROBOT
		txtRoboDP.setText("1");
		txtRoboMinDist.setText("15");
		txtRoboPar1.setText("40");
		txtRoboPar2.setText("18");
	
		// NYT TIL ROBOT
		txtlm.setText("2.48");
		txtluk.setText("5.5");
		txtpov.setText("0.05");		

		// Tilføjer alle komponenter i panelet
		pane.add(jlcircleDP);							pane.add(jlcircleDist);
		pane.add(jlcirclePar1);							pane.add(jlcirclePar2);
		pane.add(jlcircleMinRadius);					pane.add(jlcircleMaxRadius);
		pane.add(jlroboDP);								pane.add(jlroboMinDist);
		pane.add(jlroboPar1);							pane.add(lblDP);
		pane.add(lblCirkleDIst);						pane.add(lblParameter1);
		pane.add(lblParameter2);						pane.add(lblMinradius);
		pane.add(lblMaxradius);							pane.add(lblRoboDP);
		pane.add(lbllm);
		pane.add(lblluk);								pane.add(txtDP);
		pane.add(txtCirkleDIst);						pane.add(txtParameter1);
		pane.add(txtParameter2);						pane.add(txtMinradius);
		pane.add(txtMaxradius);							pane.add(txtRoboDP);
		pane.add(txtpov);								pane.add(btnApply);
		pane.add(btnConnect);							pane.add(btnDeliver);

		pane.add(lblimg);								pane.add(lblfindb);
		pane.add(lblbh);								pane.add(lbltxt);
		pane.add(lbltxtBallAngle);						pane.add(lbltxtBallAngle);
		pane.add(lbltxtTurnAngle);						pane.add(txtArea1);
		pane.add(lbledge);								pane.add(lblromin);
		pane.add(lblromax);								pane.add(txtromin);
		pane.add(txtromax);								pane.add(lblRoboMinDist);
		pane.add(lblRoboPar1);							pane.add(lblRoboPar2);
		pane.add(txtRoboMinDist);						pane.add(txtRoboPar1);
		pane.add(txtRoboPar2);							pane.add(jlsep);
		pane.add(lblpov);														
		pane.add(txtluk);
		pane.add(jlLengthMultiply);						pane.add(jlClose);
		pane.add(jlPoV);								pane.add(txtlm);							
		pane.add(btnQuit);
		
		// Placerer alle kompoenter og tildeler actionPerformed

		lblDP.setBounds(lblDP.getX() + lblDP.getWidth() + 5, insets.top + 5, lblDP.getPreferredSize().width, lblDP.getPreferredSize().height);
		txtDP.setBounds(txtDP.getX() + txtDP.getWidth() + 5, insets.top + 20, txtDP.getPreferredSize().width, txtDP.getPreferredSize().height);

		lblCirkleDIst.setBounds(lblCirkleDIst.getX() + lblCirkleDIst.getWidth() + 5, insets.top + 50, lblCirkleDIst.getPreferredSize().width, lblCirkleDIst.getPreferredSize().height);
		txtCirkleDIst.setBounds(txtCirkleDIst.getX() + txtCirkleDIst.getWidth() + 5, insets.top + 65, txtCirkleDIst.getPreferredSize().width, txtCirkleDIst.getPreferredSize().height);

		lblParameter1.setBounds(lblParameter1.getX() + lblParameter1.getWidth() + 5, insets.top + 95, lblParameter1.getPreferredSize().width, lblParameter1.getPreferredSize().height);
		txtParameter1.setBounds(txtParameter1.getX() + txtParameter1.getWidth() + 5, insets.top + 110, txtParameter1.getPreferredSize().width, txtParameter1.getPreferredSize().height);

		lblParameter2.setBounds(lblParameter2.getX() + lblParameter2.getWidth() + 5, insets.top + 140, lblParameter2.getPreferredSize().width, lblParameter2.getPreferredSize().height);
		txtParameter2.setBounds(txtParameter2.getX() + txtParameter2.getWidth() + 5, insets.top + 155, txtParameter2.getPreferredSize().width, txtParameter2.getPreferredSize().height);

		lblMinradius.setBounds(lblMinradius.getX() + lblMinradius.getWidth() + 5, insets.top + 185, lblMinradius.getPreferredSize().width, lblMinradius.getPreferredSize().height);
		txtMinradius.setBounds(txtMinradius.getX() + txtMinradius.getWidth() + 5, insets.top + 200, txtMinradius.getPreferredSize().width, txtMinradius.getPreferredSize().height);

		lblMaxradius.setBounds(lblMaxradius.getX() + lblMaxradius.getWidth() + 5, insets.top + 230, lblMaxradius.getPreferredSize().width, lblMaxradius.getPreferredSize().height);
		txtMaxradius.setBounds(txtMaxradius.getX() + txtMaxradius.getWidth() + 5, insets.top + 245, txtMaxradius.getPreferredSize().width, txtMaxradius.getPreferredSize().height);

		// deler robotdelen fra det andet
		jlsep.setBounds(jlsep.getX() + jlsep.getWidth() + 2, insets.top + 268, jlsep.getPreferredSize().width, jlsep.getPreferredSize().height);

		frame1.add(jlsep);

		lblRoboDP.setBounds(lblRoboDP.getX() + lblRoboDP.getWidth() + 5, insets.top + 285, lblRoboDP.getPreferredSize().width, lblRoboDP.getPreferredSize().height);
		txtRoboDP.setBounds(txtRoboDP.getX() + txtRoboDP.getWidth() + 5, insets.top + 300, txtRoboDP.getPreferredSize().width, txtRoboDP.getPreferredSize().height);

		lblRoboMinDist.setBounds(lblromin.getX() + lblRoboMinDist.getWidth() + 5, insets.top + 320, lblRoboMinDist.getPreferredSize().width, lblRoboMinDist.getPreferredSize().height);
		txtRoboMinDist.setBounds(txtromin.getX() + txtRoboMinDist.getWidth() + 5, insets.top + 335, txtRoboMinDist.getPreferredSize().width, txtRoboMinDist.getPreferredSize().height);

		lblRoboPar1.setBounds(lblRoboPar1.getX() + lblRoboPar1.getWidth() + 5, insets.top + 365, lblRoboPar1.getPreferredSize().width, lblRoboPar1.getPreferredSize().height);
		txtRoboPar1.setBounds(txtRoboPar1.getX() + txtRoboPar1.getWidth() + 5, insets.top + 380, txtRoboPar1.getPreferredSize().width, txtRoboPar1.getPreferredSize().height);

		lblRoboPar2.setBounds(lblRoboPar2.getX() + lblRoboPar2.getWidth() + 5, insets.top + 410, lblRoboPar2.getPreferredSize().width, lblRoboPar2.getPreferredSize().height);
		txtRoboPar2.setBounds(txtRoboPar2.getX() + txtRoboPar2.getWidth() + 5, insets.top + 425, txtRoboPar2.getPreferredSize().width, txtRoboPar2.getPreferredSize().height);

		lblromin.setBounds(lblromin.getX() + lblromin.getWidth() + 5, insets.top + 455, lblromin.getPreferredSize().width, lblromin.getPreferredSize().height);
		txtromin.setBounds(txtromin.getX() + txtromin.getWidth() + 5, insets.top + 470, txtromin.getPreferredSize().width, txtromin.getPreferredSize().height);

		lblromax.setBounds(lblromax.getX() + lblromax.getWidth() + 5, insets.top + 500, lblromax.getPreferredSize().width, lblromax.getPreferredSize().height);
		txtromax.setBounds(txtromax.getX() + txtromax.getWidth() + 5, insets.top + 515, txtromax.getPreferredSize().width, txtromax.getPreferredSize().height);

		lbllm.setBounds(lbllm.getX() + lbllm.getWidth() + 1010, insets.top + 451, lbllm.getPreferredSize().width, lbllm.getPreferredSize().height);
		txtlm.setBounds(txtlm.getX() + txtlm.getWidth() + 1010, insets.top + 466, txtlm.getPreferredSize().width, txtlm.getPreferredSize().height);

		lblluk.setBounds(lblluk.getX() + lblluk.getWidth() + 1010, insets.top + 496, lblluk.getPreferredSize().width, lblluk.getPreferredSize().height);
		txtluk.setBounds(txtluk.getX() + txtluk.getWidth() + 1010, insets.top + 511, txtluk.getPreferredSize().width, txtluk.getPreferredSize().height);

		lblpov.setBounds(lblpov.getX() + lblpov.getWidth() + 1010, insets.top + 536, lblpov.getPreferredSize().width, lblpov.getPreferredSize().height);
		txtpov.setBounds(txtpov.getX() + txtpov.getWidth() + 1010, insets.top + 551, txtpov.getPreferredSize().width, txtpov.getPreferredSize().height);

		btnApply.setBounds(btnApply.getX() + btnApply.getWidth() + 5, insets.top + 550, btnApply.getPreferredSize().width, btnApply.getPreferredSize().height);

		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			

				String input1 = txtDP.getText();
				jlcircleDP.setText(input1);
				jlcircleDP.setBounds(150, insets.top + 20, jlcircleDP.getPreferredSize().width, jlcircleDP.getPreferredSize().height);

				String input2 = txtCirkleDIst.getText();
				jlcircleDist.setText(input2);
				jlcircleDist.setBounds(150, insets.top + 65, jlcircleDist.getPreferredSize().width, jlcircleDist.getPreferredSize().height);

				String input3 = txtParameter1.getText();
				jlcirclePar1.setText(input3);
				jlcirclePar1.setBounds(150, insets.top + 110, jlcirclePar1.getPreferredSize().width, jlcirclePar1.getPreferredSize().height);

				String input4 = txtParameter2.getText();
				jlcirclePar2.setText(input4);
				jlcirclePar2.setBounds(150, insets.top + 155, jlcirclePar2.getPreferredSize().width, jlcirclePar2.getPreferredSize().height);

				String input5 = txtMinradius.getText();
				jlcircleMinRadius.setText(input5);
				jlcircleMinRadius.setBounds(150, insets.top + 200, jlcircleMinRadius.getPreferredSize().width, jlcircleMinRadius.getPreferredSize().height);

				String input6 = txtMaxradius.getText();
				jlcircleMaxRadius.setText(input6);
				jlcircleMaxRadius.setBounds(150, insets.top + 240, jlcircleMaxRadius.getPreferredSize().width, jlcircleMaxRadius.getPreferredSize().height);

				String input7 = txtRoboDP.getText();
				jlroboDP.setText(input7);
				jlroboDP.setBounds(150, insets.top + 300, jlroboDP.getPreferredSize().width, jlroboDP.getPreferredSize().height);

				// dist
				String input8 = txtRoboMinDist.getText();
				jlroboMinDist.setText(input8);
				jlroboMinDist.setBounds(150, insets.top + 335, jlroboMinDist.getPreferredSize().width, jlroboMinDist.getPreferredSize().height);

				// 1
				String input9 = txtRoboPar1.getText();
				jlroboPar1.setText(input9);
				jlroboPar1.setBounds(150, insets.top + 380, jlroboPar1.getPreferredSize().width, jlroboPar1.getPreferredSize().height);

				// 2
				String input10 = txtRoboPar2.getText();
				jlroboPar2.setText(input10);
				jlroboPar2.setBounds(150, insets.top + 425, jlroboPar2.getPreferredSize().width, jlroboPar2.getPreferredSize().height);

				// min
				String input11 = txtromin.getText();
				jlroboMin.setText(input11);
				jlroboMin.setBounds(150, insets.top + 470, jlroboMin.getPreferredSize().width, jlroboMin.getPreferredSize().height);

				// max
				String input12 = txtromax.getText();
				jlroboMax.setText(input12);
				jlroboMax.setBounds(150, insets.top + 515, jlroboMax.getPreferredSize().width, jlroboMax.getPreferredSize().height);

				String input18 = txtlm.getText();
				jlLengthMultiply.setText(input18);
				jlLengthMultiply.setBounds(1155, insets.top + 466, jlLengthMultiply.getPreferredSize().width, jlLengthMultiply.getPreferredSize().height);

				String input19 = txtluk.getText();
				jlClose.setText(input19);
				jlClose.setBounds(1155, insets.top + 511, jlClose.getPreferredSize().width, jlClose.getPreferredSize().height);

				String input20 = txtpov.getText();
				jlPoV.setText(input20);
				jlPoV.setBounds(1155, insets.top + 551, jlPoV.getPreferredSize().width, jlPoV.getPreferredSize().height);

				//				Benytter sig af de setter metoder der er oprettet i GUIInfo
				info.setJlcircleDP(jlcircleDP);									info.setJlcircleDist(jlcircleDist);
				info.setJlcirclePar1(jlcirclePar1);								info.setJlcirclePar2(jlcirclePar2);
				info.setJlcircleMinRadius(jlcircleMinRadius);					info.setJlcircleMaxRadius(jlcircleMaxRadius);
				info.setJlroboDP(jlroboDP);										info.setJlroboMinDist(jlroboMinDist);
				info.setJlroboPar1(jlroboPar1);									info.setJlroboPar2(jlroboPar2);
				info.setJlroboMin(jlroboMin);									info.setJlroboMax(jlroboMax);
				info.setJlPoV(jlPoV);
				info.setclose(Float.parseFloat(jlClose.getText()));
				info.setlengthMultiply(Float.parseFloat(jlLengthMultiply.getText()));

				if(first)
				{
					control.start();						
					first = false;
				}
				while(true){
				info = control.loopRound(info,0);
				
				pane.repaint();
			
				pane.add(lblimg);								pane.add(lblfindb);
				pane.add(lblbh);

				lbltxtBallAngle.setText("BallAngle = " + info.getBallAngle());
				lbltxtRoboAngle.setText("RoboAngle = " + info.getRoboAngle());
				lbltxtTurnAngle.setText("TurnAngle = " + info.getTurnAngle());

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
				lblfindb.setIcon(findb);
				lblbh.setIcon(bh);
				lbledge.setIcon(edge);

				lblimg.setBounds(200, insets.top + 6,
						lblimg.getPreferredSize().width,
						lblimg.getPreferredSize().height);
				lblfindb.setBounds(200, insets.top + 236,
						lblfindb.getPreferredSize().width,
						lblfindb.getPreferredSize().height);
				lblbh.setBounds(605, insets.top + 6,
						lblbh.getPreferredSize().width,
						lblbh.getPreferredSize().height);
				lbledge.setBounds(1010, insets.top + 6,
						lbledge.getPreferredSize().width,
						lbledge.getPreferredSize().height);

				lbltxt.setBounds(200, insets.top + 500,
						lbltxt.getPreferredSize().width, 10);
				lbltxtBallAngle.setBounds(200, insets.top + 515,
						lbltxtBallAngle.getPreferredSize().width, 10);
				lbltxtRoboAngle.setBounds(200, insets.top + 530,
						lbltxtRoboAngle.getPreferredSize().width, 10);
				lbltxtTurnAngle.setBounds(200, insets.top + 545,
						lbltxtTurnAngle.getPreferredSize().width, 10);
				frame1.add(jlcircleDP);					frame1.add(jlcircleDist);
				frame1.add(jlcirclePar1);				frame1.add(jlcirclePar2);
				frame1.add(jlcircleMinRadius);			frame1.add(jlcircleMaxRadius);
				frame1.add(jlroboDP);					frame1.add(jlroboMinDist);
				frame1.add(jlroboPar1);					frame1.add(jlroboPar2);
				frame1.add(jlroboMin);					frame1.add(jlroboMax);
//				frame1.add(jlLengthMultiply); // lenght
				frame1.add(jlClose); // luk				frame1.add(jlPoV); //pov
				frame1.add(lblimg);						frame1.add(lblbh);
				frame1.add(lbltxt);						frame1.add(lbltxtBallAngle);
				frame1.add(lbltxtRoboAngle);			frame1.add(lbltxtTurnAngle);
				frame1.add(txtArea1);					frame1.add(lblfindb);
				frame1.add(lbledge);

				
				frame1.setVisible(true);

			}
			}
		});


		btnDeliver.setBounds(btnDeliver.getX() + btnDeliver.getWidth() + 5, insets.top + 590, btnDeliver.getPreferredSize().width, btnDeliver.getPreferredSize().height);

		btnDeliver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Delivering balls");

				info = control.loopRound(info,1);
				info = control.loopRound(info,1);


			}
		});
		
		btnQuit.setBounds(btnQuit.getX() + btnQuit.getWidth() + 85, insets.top + 590, btnQuit.getPreferredSize().width, btnQuit.getPreferredSize().height);
		
		btnQuit.addActionListener (new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}			
		});
		
		btnConnect.setBounds(btnConnect.getX() + btnConnect.getWidth() + 80, insets.top + 550, btnConnect.getPreferredSize().width, btnConnect.getPreferredSize().height);

		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("SEND BUTTON");
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
