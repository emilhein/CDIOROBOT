package CallibratorGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.Main;

public class CallibratorGUI  {


	static JFrame frame1;
	static Container pane;
	static JButton btnApply;
	static JLabel lblDP, lblCirkleDIst, lblParameter1, lblParameter2, lblMinradius, lblMaxradius, jl1, jl2, jl3, jl4, jl5, jl6;
	static JTextField txtDP, txtCirkleDIst, txtParameter1, txtParameter2, txtMinradius, txtMaxradius;
	static Insets insets;

	
	public static void main (String args[]){

		//Opretter rammen

		frame1 = new JFrame ("CallibratorGUI");

		//S�tter st�rrelsen af rammen i pixelx 
		frame1.setSize (1200,500);

		//Prepare panel
		pane = frame1.getContentPane();

		insets = pane.getInsets();

		//tilf�j layout for null
		pane.setLayout (null);



		btnApply = new JButton ("Apply");
		lblDP = new JLabel ("DP:");
		lblCirkleDIst = new JLabel ("Cirkle Dist:");
		lblParameter1 = new JLabel ("Parameter 1:");
		lblParameter2 = new JLabel ("Parameter 2:");
		lblMinradius = new JLabel ("Min radius:");
		lblMaxradius = new JLabel ("Max radius:");
		jl1 = new JLabel ();
		jl2 = new JLabel ();
		jl3 = new JLabel ();
		jl4 = new JLabel ();
		jl5 = new JLabel ();
		jl6 = new JLabel ();
		txtDP = new JTextField (10);
		txtCirkleDIst = new JTextField  (10);
		txtParameter1 = new JTextField  (10);
		txtParameter2 = new JTextField  (5);
		txtMinradius = new JTextField  (10);
		txtMaxradius = new JTextField  (10);

		pane.add (lblDP); //tilf�jer lblDP
		lblDP.setBounds (insets.left + 5, insets.top + 5, lblDP.getPreferredSize().width, lblDP.getPreferredSize().height);


		//Tilf�jer alle komponenter
		pane.add (jl1);
		pane.add (jl2);
		pane.add (jl3);
		pane.add (jl4);
		pane.add (jl5);
		pane.add (jl6);
		pane.add (lblDP);
		pane.add (lblCirkleDIst);
		pane.add (lblParameter1);
		pane.add (lblParameter2);
		pane.add (lblMinradius);
		pane.add (lblMaxradius);
		pane.add (txtDP);
		pane.add (txtCirkleDIst);
		pane.add (txtParameter1);
		pane.add (txtParameter2);
		pane.add (txtMinradius);
		pane.add (txtMaxradius);
		pane.add (btnApply);
	

		//		//Placerer alle kompoenter
		lblDP.setBounds (lblDP.getX() + lblDP.getWidth() + 5, insets.top + 5, lblDP.getPreferredSize().width, lblDP.getPreferredSize().height);
		txtDP.setBounds (txtDP.getX() + txtDP.getWidth() + 5, insets.top + 20, txtDP.getPreferredSize().width, txtDP.getPreferredSize().height);
		
		txtDP.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input = txtDP.getText();
				jl1.setText(input);
				jl1.setBounds(jl1.getX() + jl1.getWidth() + 120, insets.top + 20, jl1.getPreferredSize().width, jl1.getPreferredSize().height);	
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
				jl2.setBounds(jl2.getX() + jl2.getWidth() + 120, insets.top + 65, jl2.getPreferredSize().width, jl2.getPreferredSize().height);	
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
				jl3.setBounds(jl3.getX() + jl3.getWidth() + 120, insets.top + 110, jl3.getPreferredSize().width, jl3.getPreferredSize().height);	
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
				jl4.setBounds(jl4.getX() + jl4.getWidth() + 120, insets.top + 155, jl4.getPreferredSize().width, jl4.getPreferredSize().height);	
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
				jl5.setBounds(jl5.getX() + jl5.getWidth() + 120, insets.top + 200, jl5.getPreferredSize().width, jl5.getPreferredSize().height);	
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
				jl6.setBounds(jl6.getX() + jl6.getWidth() + 120, insets.top + 240, jl6.getPreferredSize().width, jl6.getPreferredSize().height);	
			}		
		});
		frame1.add(jl6);

		btnApply.setBounds (btnApply.getX() + btnApply.getWidth() + 5, insets.top + 275, btnApply.getPreferredSize().width, btnApply.getPreferredSize().height);

		btnApply.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String input1 = txtDP.getText();
				jl1.setText(input1);
				jl1.setBounds(jl1.getX() + jl1.getWidth() + 120, insets.top + 20, jl1.getPreferredSize().width, jl1.getPreferredSize().height);
				
				String input2 = txtCirkleDIst.getText();
				jl2.setText(input2);
				jl2.setBounds(jl2.getX() + jl2.getWidth() + 120, insets.top + 65, jl2.getPreferredSize().width, jl2.getPreferredSize().height);	
				
				String input3 = txtParameter1.getText();
				jl3.setText(input3);
				jl3.setBounds(jl3.getX() + jl3.getWidth() + 120, insets.top + 110, jl3.getPreferredSize().width, jl3.getPreferredSize().height);	
				
				String input4 = txtParameter2.getText();
				jl4.setText(input4);
				jl4.setBounds(jl4.getX() + jl4.getWidth() + 120, insets.top + 155, jl4.getPreferredSize().width, jl4.getPreferredSize().height);	
				
				String input5 = txtMinradius.getText();
				jl5.setText(input5);
				jl5.setBounds(jl5.getX() + jl5.getWidth() + 120, insets.top + 200, jl5.getPreferredSize().width, jl5.getPreferredSize().height);	
				
				String input6 = txtMaxradius.getText();
				jl6.setText(input6);
				jl6.setBounds(jl6.getX() + jl6.getWidth() + 120, insets.top + 240, jl6.getPreferredSize().width, jl6.getPreferredSize().height);	
			}
		
		});
		
frame1.add(jl1);frame1.add(jl2);frame1.add(jl3);frame1.add(jl4);frame1.add(jl5);frame1.add(jl6);
	
//G�r rammen synlig
		frame1.setVisible (true);
		

		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}	
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}		

	}


}