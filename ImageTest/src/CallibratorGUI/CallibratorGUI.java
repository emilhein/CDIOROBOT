package CallibratorGUI;

import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import dist.Punkt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import pictureToMat.*;

public class CallibratorGUI  {


	static JFrame frame1;
	static Container pane;
	static JButton btnApply, btnRun;
	static JLabel lblDP, lblCirkleDIst, lblParameter1,lblBallCount, lblParameter2, lblMinradius, lblMaxradius, jl1, jl2, jl3, jl4, jl5, jl6,jl7, lblimg, lblafterc, lblfindb, lblbh, lbledge, lbltxt;
	static JTextField txtDP, txtBallCount,txtCirkleDIst, txtParameter1, txtParameter2, txtMinradius, txtMaxradius;
	static ImageIcon img, afterc, findb, bh, edge;
	static Insets insets;
	static JTextArea txtArea1;

//
//	
//	private void updateTxtArea1(final String text) {
//	    SwingUtilities.invokeLater(new Runnable() {
//	      public void run() {
//	        txtArea1.append(text);
//	      }
//	    });
//	  }
//
//	private void redirectSystemStreams() {
//	    OutputStream out = new OutputStream() {
//	      @Override
//	      public void write(int b) throws IOException {
//	        updateTxtArea1(String.valueOf((char) b));
//	      }
//
//	      @Override
//	      public void write(byte[] b, int off, int len) throws IOException {
//	        updateTxtArea1(new String(b, off, len));
//	      }
//
//	      @Override
//	      public void write(byte[] b) throws IOException {
//	        write(b, 0, b.length);
//	      }
//	    };
//	
//	    System.setOut(new PrintStream(out, true));
//	    System.setErr(new PrintStream(out, true));
//	  }
//
//	
	
	
	public static void main (String args[]){

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



		ImageIcon afterc = new ImageIcon("Billed0.png");
		lblafterc = new JLabel (afterc, JLabel.CENTER);

		ImageIcon img = new ImageIcon("RouteTest3.png");
		lblimg = new JLabel (img, JLabel.CENTER);


		ImageIcon findb = new ImageIcon("Robo.png");
		lblfindb = new JLabel (findb, JLabel.CENTER);

		ImageIcon bh = new ImageIcon("balls.png");
		lblbh = new JLabel (bh, JLabel.CENTER);
		
		ImageIcon edge = new ImageIcon("balls.png");
		lbledge = new JLabel (edge, JLabel.CENTER);



		btnRun = new JButton ("Run Program");
		btnApply = new JButton ("Apply");
		lblDP = new JLabel ("DP:");
		lblCirkleDIst = new JLabel ("Cirkle Dist:");
		lblParameter1 = new JLabel ("Parameter 1:");
		lblParameter2 = new JLabel ("Parameter 2:");
		lblMinradius = new JLabel ("Min radius:");
		lblMaxradius = new JLabel ("Max radius:");
		lblBallCount = new JLabel ("BallCount:");

		txtArea1 = new JTextArea (1, 1);
		lbltxt = new JLabel ();
		jl1 = new JLabel ();
		jl2 = new JLabel ();
		jl3 = new JLabel ();
		jl4 = new JLabel ();
		jl5 = new JLabel ();
		jl6 = new JLabel ();
		jl7 = new JLabel();
		txtDP = new JTextField (10);
		txtCirkleDIst = new JTextField  (10);
		txtParameter1 = new JTextField  (10);
		txtParameter2 = new JTextField  (5);
		txtMinradius = new JTextField  (10);
		txtMaxradius = new JTextField  (10);
		txtBallCount = new JTextField (10);



		txtDP.setText("1");
		txtCirkleDIst.setText("1");
		txtParameter1.setText("50");
		txtParameter2.setText("5");
		txtMinradius.setText("2");
		txtMaxradius.setText("8");
		txtBallCount.setText("13");
		
		
		//Tilføjer alle komponenter
		pane.add (jl1);
		pane.add (jl2);
		pane.add (jl3);
		pane.add (jl4);
		pane.add (jl5);
		pane.add (jl6);
		pane.add (jl7);
		pane.add (lblDP);
		pane.add (lblCirkleDIst);
		pane.add (lblParameter1);
		pane.add (lblParameter2);
		pane.add (lblMinradius);
		pane.add (lblMaxradius);
		pane.add(lblBallCount);
		pane.add (txtDP);
		pane.add (txtCirkleDIst);
		pane.add (txtParameter1);
		pane.add (txtParameter2);
		pane.add (txtMinradius);
		pane.add (txtMaxradius);
		pane.add(txtBallCount);
		pane.add (btnApply);
		pane.add (btnRun);
		pane.add (lblimg);
		pane.add (lblafterc);
		pane.add (lblfindb);
		pane.add (lblbh);
		pane.add (lbltxt);
		pane.add (txtArea1);
		pane.add (lbledge);

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


		lblBallCount.setBounds (lblBallCount.getX() + lblBallCount.getWidth() + 5, insets.top + 275, lblBallCount.getPreferredSize().width, lblBallCount.getPreferredSize().height);
		txtBallCount.setBounds (txtBallCount.getX() + txtBallCount.getWidth() + 5, insets.top + 290, txtBallCount.getPreferredSize().width, txtBallCount.getPreferredSize().height);


		txtDP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtBallCount.getText();
				jl7.setText(input);
				jl7.setBounds(150, insets.top + 20, jl7.getPreferredSize().width, jl7.getPreferredSize().height);	
			}		
		});
		frame1.add(jl1);




		btnApply.setBounds (btnApply.getX() + btnApply.getWidth() + 5, insets.top + 320, btnApply.getPreferredSize().width, btnApply.getPreferredSize().height);

		btnApply.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

				
				
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

				String input7 = txtBallCount.getText();
				jl7.setText(input7);
				jl7.setBounds(150, insets.top + 280, jl7.getPreferredSize().width, jl7.getPreferredSize().height);

				TakePicture takepic = new TakePicture();
				takepic.takePicture();

				ballMethod balls = new ballMethod();

				float[] RoboCoor = balls.findCircle(8, 12, 2,1,50,5,2,"robo"); // finder robo
				for(int j = 0; j<RoboCoor.length;j=j+3){


					txtArea1  = new JTextArea ("Bold nr " + j +" ligger på "+Math.round(RoboCoor[j]) + ","+Math.round(RoboCoor[j+1]) +" Med radius = " + Math.round(RoboCoor[j+2]), 1,1);
					String text1 = txtArea1.getText();
					lbltxt.setText(text1);

//					System.out.println("Bold nr " + j +" ligger på "+Math.round(RoboCoor[j]) + ","+Math.round(RoboCoor[j+1]) +" Med radius = " + Math.round(RoboCoor[j+2]));

				}

				Mat frame = Highgui.imread("AfterColorConvert.png"); // henter det konverterede billlede

				double[] front = frame.get(Math.round(RoboCoor[1]), Math.round(RoboCoor[0])); ///X OG Y ER FUCKED
				//double red = front[2]; //henter en rød farver fra den ene cirkel
				double red = front[2];

				double[] back = frame.get(Math.round(RoboCoor[4]), Math.round(RoboCoor[3])); /// X OG Y ER FUCKED
				double red2 = back[2]; // henter en rød farve ([2]) fra den anden cirkel

				Punkt roboFrontPunkt = new Punkt(0,0);
				Punkt roboBagPunkt = new Punkt(0,0);
				// herunder sættes robotpunket, alt efter hvilken cirkel der er rød.
				if(red > 245){
					roboFrontPunkt.setX(Math.round(RoboCoor[0]));
					roboFrontPunkt.setY(Math.round(RoboCoor[1]));
					roboBagPunkt.setX(Math.round(RoboCoor[3]));
					roboBagPunkt.setY(Math.round(RoboCoor[4]));
				} else if (red2 > 245){
					roboFrontPunkt.setX(Math.round(RoboCoor[3]));
					roboFrontPunkt.setY(Math.round(RoboCoor[4]));
					roboBagPunkt.setX(Math.round(RoboCoor[0]));
					roboBagPunkt.setY(Math.round(RoboCoor[1]));
				}
				float[] ballCoor = balls.findCircle(Integer.parseInt(jl5.getText()),Integer.parseInt(jl6.getText()),Integer.parseInt(jl1.getText()),Integer.parseInt(jl2.getText()),Integer.parseInt(jl3.getText()),Integer.parseInt(jl4.getText()),Integer.parseInt(jl6.getText()),"balls");//minradius, maxrdius, antalbolde

				RouteTest.drawBallMap(ballCoor, roboBagPunkt, roboFrontPunkt); // tegner dem i testprogrammet



				ImageIcon afterc = new ImageIcon("Billed0.png");
				Image image1 = afterc.getImage(); // transform it
				Image afimage = image1.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				afterc = new ImageIcon(afimage);  // transform it back
				//lblafterc = new JLabel (afterc, JLabel.CENTER);



				ImageIcon img = new ImageIcon("RouteTest3.png");
				Image image2 = img.getImage(); // transform it
				Image dimage = image2.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				img = new ImageIcon(dimage);  // transform it back
				//lblimg = new JLabel (img, JLabel.CENTER);



				ImageIcon findb = new ImageIcon("Robo.png");
				Image image3 = findb.getImage(); // transform it
				Image abimage = image3.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				findb = new ImageIcon(abimage);  // transform it back
				//lblfindb = new JLabel (findb, JLabel.CENTER);



				ImageIcon bh = new ImageIcon("balls.png");
				Image image4 = bh.getImage(); // transform it
				Image acimage = image4.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
				bh = new ImageIcon(acimage);  // transform it back
				//	lblbh = new JLabel (bh, JLabel.CENTER);
				
				ImageIcon edge = new ImageIcon("balls.png");
				Image image5 = edge.getImage(); // transform it
				Image edimage = image5.getScaledInstance(400, 225,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
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
				
				//lbltxt.setBounds(200, insets.top + 510, lbltxt.getPreferredSize().width, 10);

//				txtArea1.setText(String);
//				txtArea1.append(String);
				
			}

		});

		frame1.add(jl1);frame1.add(jl2);frame1.add(jl4);frame1.add(jl5);frame1.add(jl6);frame1.add(jl3);frame1.add(lblimg);frame1.add(jl7);frame1.add(lblafterc);frame1.add(lblbh);frame1.add(txtArea1);frame1.add(lbltxt);

		btnRun.setBounds (btnRun.getX() + btnRun.getWidth() + 75, insets.top + 320, btnRun.getPreferredSize().width, btnRun.getPreferredSize().height);

		btnRun.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{


				//Indsæt her noget, der kalder main kladsen.

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



}
