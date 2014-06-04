package CallibratorGUI;

import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import dist.CalcAngle;
import dist.CalcDist;
import dist.Punkt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import pictureToMat.*;

public class CallibratorGUI  {


	static JFrame frame1;
	static Container pane;
	static JButton btnApply, btnConnect, btnSend;
	static JLabel lblDP, lblCirkleDIst, lblParameter1,lblRoboDP, jlsep, lblParameter2, lblMinradius, lblMaxradius, lblRoboMinDist, lblRoboPar1, lblRoboPar2,  jl1, jl2, jl3, jl4, jl5, jl6,jl7, jl8, jl9, jl10, jl11, jl12, lblimg, lblafterc, lblfindb, lblbh, lbledge, lbltxt, lbltxt2, lbltxt3, lbltxt4, lblromin, lblromax;
	static JTextField txtDP, txtRoboDP,txtCirkleDIst, txtParameter1, txtParameter2, txtMinradius, txtMaxradius, txtromin, txtromax, txtRoboMinDist, txtRoboPar1, txtRoboPar2;
	static ImageIcon img, afterc, findb, bh, edge;
	static Insets insets;
	static JTextArea txtArea1;
	
	static int TurnAngle = 0;
	static int minLength = 0;
	static float ppcm = 0;
	static int firstRun = 0;
	
	
	public static void main (String args[]) throws IOException{
		
		if(firstRun == 0){
			//try{ 
				
				//prøver at forbinde til vores robot
				NXTInfo nxtInfo = new NXTInfo(2,"G9 awesome!","0016530918D4");
				NXTConnector connt = new NXTConnector();
				System.out.println("trying to connect");
				connt.connectTo(nxtInfo, NXTComm.LCP);
				System.out.println("connected");		//forbundet
				//åbner streams}
				final OutputStream dos = connt.getOutputStream();
				
				firstRun = 1;
		
			//	Scanner scan = new Scanner(System.in);
			//	while(true){
		
		//Opretter rammen
		frame1 = new JFrame ("CallibratorGUI");

		//Sætter størrelsen af rammen i pixelx 
		frame1.setSize (1300,718);

		//Prepare panel
		pane = frame1.getContentPane();

		insets = pane.getInsets();

		//tilføj layout for null
		pane.setLayout (null);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ImageIcon afterc = new ImageIcon("billed0.png");
		lblafterc = new JLabel (afterc, JLabel.CENTER);

		ImageIcon img = new ImageIcon("RouteTest3.png");
		lblimg = new JLabel (img, JLabel.CENTER);


		ImageIcon findb = new ImageIcon("robo.png");
		lblfindb = new JLabel (findb, JLabel.CENTER);

		ImageIcon bh = new ImageIcon("balls.png"); //readyForBallMethodGrey
		lblbh = new JLabel (bh, JLabel.CENTER);
		
		ImageIcon edge = new ImageIcon("edge.png");
		lbledge = new JLabel (edge, JLabel.CENTER);


		btnConnect = new JButton ("Connect");
		btnSend = new JButton ("Send");
		btnApply = new JButton ("Apply");
		lblDP = new JLabel ("DP:");
		lblromin = new JLabel ("RoboMin");
		lblromax = new JLabel ("RoboMax");
		lblCirkleDIst = new JLabel ("Cirkle Dist:");
		lblParameter1 = new JLabel ("Parameter 1:");
		lblParameter2 = new JLabel ("Parameter 2:");
		lblMinradius = new JLabel ("Min radius:");
		lblMaxradius = new JLabel ("Max radius:");
		lblRoboDP = new JLabel ("RoboDP:");
		lblRoboMinDist = new JLabel ("RoboMinDist");
		lblRoboPar1 = new JLabel ("RoboParameter1");
		lblRoboPar2 = new JLabel ("RoboParameter2");
		jlsep = new JLabel("=================");
		
		
		txtArea1 = new JTextArea (1, 1);
		lbltxt = new JLabel ();
		lbltxt2 = new JLabel ();
		lbltxt3 = new JLabel ();
		lbltxt4 = new JLabel ();
		jl1 = new JLabel ();
		jl2 = new JLabel ();
		jl3 = new JLabel ();
		jl4 = new JLabel ();
		jl5 = new JLabel ();
		jl6 = new JLabel ();
		jl7 = new JLabel();
		jl8 = new JLabel();
		jl9 = new JLabel();
		jl10 = new JLabel();
		jl11 = new JLabel ();
		jl12 = new JLabel ();
		txtDP = new JTextField (10);
		txtCirkleDIst = new JTextField  (10);
		txtParameter1 = new JTextField  (10);
		txtParameter2 = new JTextField  (5);
		txtMinradius = new JTextField  (10);
		txtMaxradius = new JTextField  (10);
		txtRoboDP = new JTextField (10);
		txtromin = new JTextField (10);
		txtromax = new JTextField (10);
		txtRoboMinDist = new JTextField (10);
		txtRoboPar1 = new JTextField (10);
		txtRoboPar2 = new JTextField (10);
		
		
		
		txtDP.setText("1");
		txtCirkleDIst.setText("10");
		txtParameter1.setText("50");
		txtParameter2.setText("2");
		txtMinradius.setText("8");
		txtMaxradius.setText("18");
		txtRoboDP.setText("13");
		txtromin.setText("19");
		txtromax.setText("28");
		txtRoboMinDist.setText("indæt");
		txtRoboPar1.setText("indsæt");
		txtRoboPar2.setText("indsæt");
		
		
		//Tilføjer alle komponenter
		pane.add (jl1);
		pane.add (jl2);
		pane.add (jl3);
		pane.add (jl4);
		pane.add (jl5);
		pane.add (jl6);
		pane.add (jl7);
		pane.add(jl8);
		pane.add(jl9);
		pane.add (lblDP);
		pane.add (lblCirkleDIst);
		pane.add (lblParameter1);
		pane.add (lblParameter2);
		pane.add (lblMinradius);
		pane.add (lblMaxradius);
		pane.add(lblRoboDP);
		pane.add (txtDP);
		pane.add (txtCirkleDIst);
		pane.add (txtParameter1);
		pane.add (txtParameter2);
		pane.add (txtMinradius);
		pane.add (txtMaxradius);
		pane.add(txtRoboDP);
		pane.add (btnApply);
		pane.add (btnConnect);
		pane.add (btnSend);

		pane.add (lblimg);
		pane.add (lblafterc);
		pane.add (lblfindb);
		pane.add (lblbh);
		pane.add (lbltxt);
		pane.add (lbltxt2);
		pane.add (lbltxt2);
		pane.add (lbltxt4);
		pane.add (txtArea1);
		pane.add (lbledge);
		pane.add (lblromin);
		pane.add (lblromax);
		pane.add (txtromin);
		pane.add (txtromax);
		pane.add(lblRoboMinDist);
		pane.add (lblRoboPar1);
		pane.add(lblRoboPar2);
		pane.add (txtRoboMinDist);
		pane.add(txtRoboPar1);
		pane.add(txtRoboPar2);
		pane.add(jlsep);
		
		
		//		//Placerer alle kompoenter
		lblDP.setBounds (lblDP.getX() + lblDP.getWidth() + 5, insets.top + 5, lblDP.getPreferredSize().width, lblDP.getPreferredSize().height);
		txtDP.setBounds (txtDP.getX() + txtDP.getWidth() + 5, insets.top + 20, txtDP.getPreferredSize().width, txtDP.getPreferredSize().height);


		txtDP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtDP.getText();
				jl1.setText(input);
				jl1.setBounds(150, insets.top + 20, jl1.getPreferredSize().width, jl1.getPreferredSize().height);	
			}		
		});
		frame1.add(jl1);


		lblCirkleDIst.setBounds (lblCirkleDIst.getX() + lblCirkleDIst.getWidth() + 5, insets.top + 50, lblCirkleDIst.getPreferredSize().width, lblCirkleDIst.getPreferredSize().height);
		txtCirkleDIst.setBounds (txtCirkleDIst.getX() + txtCirkleDIst.getWidth() + 5, insets.top + 65, txtCirkleDIst.getPreferredSize().width, txtCirkleDIst.getPreferredSize().height);

		txtCirkleDIst.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtCirkleDIst.getText();
				jl2.setText(input);
				jl2.setBounds(150, insets.top + 65, jl2.getPreferredSize().width, jl2.getPreferredSize().height);	
			}		
		});
		frame1.add(jl2);

		lblParameter1.setBounds (lblParameter1.getX() + lblParameter1.getWidth() + 5, insets.top + 95, lblParameter1.getPreferredSize().width, lblParameter1.getPreferredSize().height);
		txtParameter1.setBounds (txtParameter1.getX() + txtParameter1.getWidth() + 5, insets.top + 110, txtParameter1.getPreferredSize().width, txtParameter1.getPreferredSize().height);

		txtParameter1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtParameter1.getText();
				jl3.setText(input);
				jl3.setBounds(150, insets.top + 110, jl3.getPreferredSize().width, jl3.getPreferredSize().height);	
			}		
		});
		frame1.add(jl3);

		lblParameter2.setBounds (lblParameter2.getX() + lblParameter2.getWidth() + 5, insets.top + 140, lblParameter2.getPreferredSize().width, lblParameter2.getPreferredSize().height);
		txtParameter2.setBounds (txtParameter2.getX() + txtParameter2.getWidth() + 5, insets.top + 155, txtParameter2.getPreferredSize().width, txtParameter2.getPreferredSize().height);

		txtParameter2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtParameter2.getText();
				jl4.setText(input);
				jl4.setBounds(150, insets.top + 155, jl4.getPreferredSize().width, jl4.getPreferredSize().height);	
			}		
		});
		frame1.add(jl4);

		lblMinradius.setBounds (lblMinradius.getX() + lblMinradius.getWidth() + 5, insets.top + 185, lblMinradius.getPreferredSize().width, lblMinradius.getPreferredSize().height);
		txtMinradius.setBounds (txtMinradius.getX() + txtMinradius.getWidth() + 5, insets.top + 200, txtMinradius.getPreferredSize().width, txtMinradius.getPreferredSize().height);

		txtMinradius.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtMinradius.getText();
				jl5.setText(input);
				jl5.setBounds(150, insets.top + 200, jl5.getPreferredSize().width, jl5.getPreferredSize().height);	
			}		
		});
		frame1.add(jl5);

		lblMaxradius.setBounds (lblMaxradius.getX() + lblMaxradius.getWidth() + 5, insets.top + 230, lblMaxradius.getPreferredSize().width, lblMaxradius.getPreferredSize().height);
		txtMaxradius.setBounds (txtMaxradius.getX() + txtMaxradius.getWidth() + 5, insets.top + 245, txtMaxradius.getPreferredSize().width, txtMaxradius.getPreferredSize().height);

		txtMaxradius.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				String input = txtMaxradius.getText();
				jl6.setText(input);
				jl6.setBounds(150, insets.top + 240, jl6.getPreferredSize().width, jl6.getPreferredSize().height);	
			}		
		});
		frame1.add(jl6);

		//deler robotdelen fra det andet
		jlsep.setBounds (jlsep.getX() + jlsep.getWidth() + 2, insets.top + 268, jlsep.getPreferredSize().width, jlsep.getPreferredSize().height);
		
		frame1.add(jlsep);
		
		
		
		lblRoboDP.setBounds (lblRoboDP.getX() + lblRoboDP.getWidth() + 5, insets.top + 285, lblRoboDP.getPreferredSize().width, lblRoboDP.getPreferredSize().height);
		txtRoboDP.setBounds (txtRoboDP.getX() + txtRoboDP.getWidth() + 5, insets.top + 300, txtRoboDP.getPreferredSize().width, txtRoboDP.getPreferredSize().height);


		txtRoboDP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtRoboDP.getText();
				jl7.setText(input);
				jl7.setBounds(150, insets.top + 300, jl7.getPreferredSize().width, jl7.getPreferredSize().height);	
			}		
		});
		frame1.add(jl7);


		lblromin.setBounds (lblromin.getX() + lblromin.getWidth() + 5, insets.top + 320, lblromin.getPreferredSize().width, lblromin.getPreferredSize().height);
		txtromin.setBounds (txtromin.getX() + txtromin.getWidth() + 5, insets.top + 335, txtromin.getPreferredSize().width, txtromin.getPreferredSize().height);

		
		txtromin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input8 = txtromin.getText();
				jl8.setText(input8);
				jl8.setBounds(150, insets.top + 335, jl8.getPreferredSize().width, jl8.getPreferredSize().height);	
			}		
		});
		frame1.add(jl8);

		
		lblromax.setBounds (lblromax.getX() + lblromax.getWidth() + 5, insets.top + 365, lblromax.getPreferredSize().width, lblromax.getPreferredSize().height);
		txtromax.setBounds (txtromax.getX() + txtromax.getWidth() + 5, insets.top + 380, txtromax.getPreferredSize().width, txtromax.getPreferredSize().height);

		
		txtromax.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input9 = txtromax.getText();
				jl9.setText(input9);
				jl9.setBounds(150, insets.top + 380, jl9.getPreferredSize().width, jl9.getPreferredSize().height);	
			}		
		});
		frame1.add(jl9);
		
		
		
		lblRoboMinDist.setBounds (lblRoboMinDist.getX() + lblRoboMinDist.getWidth() + 5, insets.top + 410, lblRoboMinDist.getPreferredSize().width, lblRoboMinDist.getPreferredSize().height);
		txtRoboMinDist.setBounds (txtRoboMinDist.getX() + txtRoboMinDist.getWidth() + 5, insets.top + 425, txtRoboMinDist.getPreferredSize().width, txtRoboMinDist.getPreferredSize().height);

		
		txtRoboMinDist.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input10 = txtRoboMinDist.getText();
				jl10.setText(input10);
				jl10.setBounds(150, insets.top + 425, jl10.getPreferredSize().width, jl10.getPreferredSize().height);	
			}		
		});
		frame1.add(jl10);

		
		
		//RoboPar1

		
		lblRoboPar1.setBounds (lblRoboPar1.getX() + lblRoboPar1.getWidth() + 5, insets.top + 455, lblRoboPar1.getPreferredSize().width, lblRoboPar1.getPreferredSize().height);
		txtRoboPar1.setBounds (txtRoboPar1.getX() + txtRoboPar1.getWidth() + 5, insets.top + 470, txtRoboPar1.getPreferredSize().width, txtRoboPar1.getPreferredSize().height);

		
		txtRoboPar1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input11 = txtRoboPar1.getText();
				jl11.setText(input11);
				jl11.setBounds(150, insets.top + 470, jl11.getPreferredSize().width, jl11.getPreferredSize().height);	
			}		
		});
		frame1.add(jl11);
		
		//RoboPar2


		lblRoboPar2.setBounds (lblRoboPar2.getX() + lblRoboPar2.getWidth() + 5, insets.top + 500, lblRoboPar2.getPreferredSize().width, lblRoboPar2.getPreferredSize().height);
		txtRoboPar2.setBounds (txtRoboPar2.getX() + txtRoboPar2.getWidth() + 5, insets.top + 515, txtRoboPar2.getPreferredSize().width, txtRoboPar2.getPreferredSize().height);

		
		txtRoboPar2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input12 = txtRoboPar2.getText();
				jl12.setText(input12);
				jl12.setBounds(150, insets.top + 515, jl12.getPreferredSize().width, jl12.getPreferredSize().height);	
			}		
		});
		frame1.add(jl12);

		
//		
		btnApply.setBounds (btnApply.getX() + btnApply.getWidth() + 5, insets.top + 550, btnApply.getPreferredSize().width, btnApply.getPreferredSize().height);

		btnApply.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

				Punkt minPunkt;

				
				String input1 = txtDP.getText();
				jl1.setText(input1);
				jl1.setBounds(150, insets.top + 20, jl1.getPreferredSize().width, jl1.getPreferredSize().height);

				String input2 = txtCirkleDIst.getText();
				jl2.setText(input2);
				jl2.setBounds(150, insets.top + 65, jl2.getPreferredSize().width, jl2.getPreferredSize().height);	

				String input3 = txtParameter1.getText();
				jl3.setText(input3);
				jl3.setBounds(150, insets.top + 110, jl3.getPreferredSize().width, jl3.getPreferredSize().height);	

				String input4 = txtParameter2.getText();
				jl4.setText(input4);
				jl4.setBounds(150, insets.top + 155, jl4.getPreferredSize().width, jl4.getPreferredSize().height);	

				String input5 = txtMinradius.getText();
				jl5.setText(input5);
				jl5.setBounds(150, insets.top + 200, jl5.getPreferredSize().width, jl5.getPreferredSize().height);	

				String input6 = txtMaxradius.getText();
				jl6.setText(input6);
				jl6.setBounds(150, insets.top + 240, jl6.getPreferredSize().width, jl6.getPreferredSize().height);	

				String input7 = txtRoboDP.getText();
				jl7.setText(input7);
				jl7.setBounds(150, insets.top + 300, jl7.getPreferredSize().width, jl7.getPreferredSize().height);
				
				String input8 = txtromin.getText();
				jl8.setText(input8);
				jl8.setBounds(150, insets.top + 335, jl8.getPreferredSize().width, jl8.getPreferredSize().height);	

				String input9 = txtromax.getText();
				jl9.setText(input9);
				jl9.setBounds(150, insets.top + 380, jl9.getPreferredSize().width, jl9.getPreferredSize().height);	

				String input10 = txtRoboMinDist.getText();
				jl10.setText(input10);
				jl10.setBounds(150, insets.top + 425, jl10.getPreferredSize().width, jl10.getPreferredSize().height);
				
				String input11 = txtRoboPar1.getText();
				jl11.setText(input11);
				jl11.setBounds(150, insets.top + 470, jl11.getPreferredSize().width, jl11.getPreferredSize().height);
				
				String input12 = txtRoboPar2.getText();
				jl12.setText(input12);
				jl12.setBounds(150, insets.top + 515, jl12.getPreferredSize().width, jl12.getPreferredSize().height);	

				TakePicture takepic = new TakePicture();
				takepic.takePicture();
 
				
					//BufferedImage src = ImageIO.read(new File("Billed0.png"));
					DetectBorder findEdge = new DetectBorder();
					try {
						findEdge.getRectCoordis("billed0.png");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ppcm = findEdge.getPixPerCm();
				
				
				ballMethod balls = new ballMethod();


				
				ArrayList<Float> RoboCoor = balls.findCircle(Integer.parseInt(jl8.getText()),Integer.parseInt(jl9.getText()),Integer.parseInt(jl1.getText()),Integer.parseInt(jl2.getText()),Integer.parseInt(jl3.getText()),Integer.parseInt(jl4.getText()),"robo",true);//minradius, maxrdius, antalbolde

				
//				float[] RoboCoor = balls.findCircle(19, 28, 1,5,50,5,2,"robo", true); // finder robo
				for(int j = 0; j<RoboCoor.size();j=j+3){


					txtArea1  = new JTextArea ("Forholdet mellem pixel og cm er = " + ppcm, 1,1);
					String text1 = txtArea1.getText();
					lbltxt.setText(text1);
//					System.out.println("Bold nr " + j +" ligger på "+Math.round(RoboCoor[j]) + ","+Math.round(RoboCoor[j+1]) +" Med radius = " + Math.round(RoboCoor[j+2]));

				}

				Mat frame = Highgui.imread("AfterColorConvert.png"); // henter det konverterede billlede

				double[] front = frame.get(Math.round(RoboCoor.get(1)), Math.round(RoboCoor.get(0))); ///Y OG X ER BYTTET OM GConnectDET get-metoden
				//double red = front[2]; //henter en rød farver fra den ene cirkel
				double green = front[1];
				double red = front[2];

				double[] back = frame.get(Math.round(RoboCoor.get(4)), Math.round(RoboCoor.get(3))); ///
				double green2 = back[1];
				double red2 = back[2]; // henter en rød farve ([2]) fra den anden cirkel

				Punkt roboFrontPunkt = new Punkt(10,10);
				Punkt roboBagPunkt = new Punkt(20,20);
				// heConnectder sættes robotpunket, alt efter hvilken cirkel der er rød.
				if(red > 245){
					roboFrontPunkt.setX(Math.round(RoboCoor.get(0)));
					roboFrontPunkt.setY(Math.round(RoboCoor.get(1)));
					roboBagPunkt.setX(Math.round(RoboCoor.get(3)));
					roboBagPunkt.setY(Math.round(RoboCoor.get(4)));
				} else if (red2 > 245){
					roboFrontPunkt.setX(Math.round(RoboCoor.get(3)));
					roboFrontPunkt.setY(Math.round(RoboCoor.get(4)));
					roboBagPunkt.setX(Math.round(RoboCoor.get(0)));
					roboBagPunkt.setY(Math.round(RoboCoor.get(1)));
				} else if(green > 245){
					roboFrontPunkt.setX(Math.round(RoboCoor.get(3)));
					roboFrontPunkt.setY(Math.round(RoboCoor.get(4)));
					roboBagPunkt.setX(Math.round(RoboCoor.get(0)));
					roboBagPunkt.setY(Math.round(RoboCoor.get(1)));
				} else if (green2 > 245){
					roboFrontPunkt.setX(Math.round(RoboCoor.get(0)));
					roboFrontPunkt.setY(Math.round(RoboCoor.get(1)));
					roboBagPunkt.setX(Math.round(RoboCoor.get(3)));
					roboBagPunkt.setY(Math.round(RoboCoor.get(4)));
				}
				
				ArrayList<Float> ballCoor = balls.findCircle(Integer.parseInt(jl5.getText()),Integer.parseInt(jl6.getText()),Integer.parseInt(jl1.getText()),Integer.parseInt(jl2.getText()),Integer.parseInt(jl3.getText()),Integer.parseInt(jl4.getText()),"balls",false);//minradius, maxrdius, antalbolde

				
				minPunkt = RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet
				
				
				lbltxt2.setText("koordinaterne til Bagpunkt er (" + roboBagPunkt.getX() +","+roboBagPunkt.getY()+")");	
				lbltxt3.setText("koordinaterne til Frontpunkt er (" + roboFrontPunkt.getX() +","+roboFrontPunkt.getY()+")");
				lbltxt4.setText ("koordinaterne til MinPunkt er (" + minPunkt.getX() +","+minPunkt.getY()+")");
						

				Punkt nyRoboFront = new Punkt(roboFrontPunkt.getX()-roboBagPunkt.getX(),roboFrontPunkt.getY()-roboBagPunkt.getY());
				Punkt nyRoboBag = new Punkt(0,0);
				Punkt nyMinPunkt = new Punkt(minPunkt.getX()-roboBagPunkt.getX(),minPunkt.getY()-roboBagPunkt.getY());
				System.out.println("koordinaterne til nyBagpunkt er (" + nyRoboBag.getX() +","+nyRoboBag.getY()+")");
				System.out.println("koordinaterne til nyFrontpunkt er (" + nyRoboFront.getX() +","+nyRoboFront.getY()+")");
				System.out.println("koordinaterne til nyMinpunkt er (" + nyMinPunkt.getX() +","+nyMinPunkt.getY()+")");

				CalcAngle Angle = new CalcAngle();
				int BallAngle = Angle.Calcangle(nyRoboBag, nyMinPunkt);
				System.out.println("BallAngle = " + BallAngle);
				int RoboAngle = Angle.Calcangle(nyRoboBag, nyRoboFront);
				System.out.println("RoboAngle = " + RoboAngle);
				TurnAngle = RoboAngle-BallAngle;

				CalcDist dist = new CalcDist();
				minLength = Math.abs(dist.Calcdist(roboFrontPunkt, minPunkt));

				ImageIcon afterc = new ImageIcon("billed0.png");
				Image image1 = afterc.getImage(); // transform it
				Image afimage = image1.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				afterc = new ImageIcon(afimage);  // transform it back
				//lblafterc = new JLabel (afterc, JLabel.CENTER);

				ImageIcon img = new ImageIcon("RouteTest3.png");
				Image image2 = img.getImage(); // transform it
				Image dimage = image2.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				img = new ImageIcon(dimage);  // transform it back
				//lblimg = new JLabel (img, JLabel.CENTER);


				ImageIcon findb = new ImageIcon("robo.png");
				Image image3 = findb.getImage(); // transform it
				Image abimage = image3.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				findb = new ImageIcon(abimage);  // transform it back
				//lblfindb = new JLabel (findb, JLabel.CENTER);


				ImageIcon bh = new ImageIcon("balls.png"); //readyForBallMethodGrey
				Image image4 = bh.getImage(); // transform it
				Image acimage = image4.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				bh = new ImageIcon(acimage);  // transform it back
				//	lblbh = new JLabel (bh, JLabel.CENTER);
				
				ImageIcon edge = new ImageIcon("edge.png");
				Image image5 = edge.getImage(); // transform it
				Image edimage = image5.getScaledInstance(300, 169,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				edge = new ImageIcon(edimage);  // transform it back
				
				lblimg.setIcon(img);
				lblafterc.setIcon(afterc);
				lblfindb.setIcon(findb);
				lblbh.setIcon(bh);
				lbledge.setIcon(edge);

				lblimg.setBounds (200, insets.top + 6, lblimg.getPreferredSize().width, lblimg.getPreferredSize().height);
				lblafterc.setBounds(605, insets.top + 6, lblafterc.getPreferredSize().width, lblafterc.getPreferredSize().height);
				lblfindb.setBounds(200, insets.top + 236, lblfindb.getPreferredSize().width, lblfindb.getPreferredSize().height);
				lblbh.setBounds(605, insets.top + 236, lblbh.getPreferredSize().width, lblbh.getPreferredSize().height);
				lbledge.setBounds(1010, insets.top + 6, lbledge.getPreferredSize().width, lbledge.getPreferredSize().height);
				
				lbltxt.setBounds(200, insets.top + 500, lbltxt.getPreferredSize().width, 10);
				lbltxt2.setBounds(200, insets.top + 515, lbltxt2.getPreferredSize().width, 10);
				lbltxt3.setBounds(200, insets.top + 530, lbltxt3.getPreferredSize().width, 10);
				lbltxt4.setBounds (200, insets.top + 545, lbltxt4.getPreferredSize().width, 10);
				
			}
		});

		frame1.add(jl1);frame1.add(jl2);frame1.add(jl4);frame1.add(jl8);frame1.add(jl9);frame1.add(jl5);frame1.add(jl6);frame1.add(jl3);frame1.add(lblimg);frame1.add(jl7);frame1.add(jl10);frame1.add(jl11);frame1.add(jl12);frame1.add(lblafterc);frame1.add(lblbh);frame1.add(txtArea1);frame1.add(lbltxt); frame1.add(lbltxt2);frame1.add(lbltxt3);frame1.add(lbltxt4);

		btnConnect.setBounds (btnConnect.getX() + btnConnect.getWidth() + 5, insets.top + 590, btnConnect.getPreferredSize().width, btnConnect.getPreferredSize().height);

		btnConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				
				System.out.println("In CONNECT");
				
						System.out.println("Waiting for your go!");	
			//			int input = scan.nextInt();

						int Case;
						int i;
						System.out.println("TurnAngle = " + TurnAngle);
						int angle = (int) (TurnAngle*2.129);	//vinkel konvertering
						System.out.println("angle " + angle);
						if(Math.abs(angle) < 250){
							if(angle > 0) 				//vælger retning der skal drejes
								Case = 11;				
							else Case = 22;
						}
						else{
							angle = angle/10;
							if(angle > 0) 				//vælger retning der skal drejes
								Case = 31;				
							else Case = 42;
						}
						angle = Math.abs(angle);
						try {
							dos.write(Case);			//sender case
							dos.flush();
							dos.write(angle);			//sender vinkel
							dos.flush();

							Thread.sleep(1500);
							dos.write(61);			//sender case
							dos.flush();
							dos.write(61);			//sender vinkel
							dos.flush();
							Thread.sleep(500);
							
							//kører robot frem
							System.out.println("minlength " + minLength);
							int distance = (int)((minLength*2.3)/ppcm);	//længde konvertering
							System.out.println("dist = " + distance);
							dos.write(81);
							dos.flush();
							i = distance;
							dos.write(i);
							dos.flush();

							Thread.sleep((18*distance));

							//samler bold op
							dos.write(71);				
							dos.flush();
							dos.write(71);
							dos.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
					
					//}
					
			}
		});
		
		btnSend.setBounds (btnSend.getX() + btnSend.getWidth() + 80, insets.top + 550, btnSend.getPreferredSize().width, btnSend.getPreferredSize().height);

		btnSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

				System.out.println("In SEND");

				
			}
		});

		//Gør rammen synlig
		frame1.setVisible (true);


		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}	
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}		
	}

}}
