package connect;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

import java.io.*;
import java.util.*;

public class Pc_connect {
	private static Scanner scan;

	public static void main(String [] args)
	{ 
		try
		{ 
	//		pr�ver at forbinde til vores robot
			NXTInfo nxtInfo = new NXTInfo(2,"G9 awesome!","0016530918D4");
			NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");//robot nr 2
			NXTConnector connt = new NXTConnector();
			System.out.println("trying to connect");
			connt.connectTo(nxtInfo, NXTComm.LCP);	//venter p� forbindelse
			System.out.println("connected");		//forbundet
	//		�bner streams
			OutputStream dos = connt.getOutputStream();
			//InputStream dis = connt.getInputStream(); ikke l�ngere brugt
			scan = new Scanner(System.in);			//scanner til manuel input
	//		test
			while(true)
			{							
				int input = scan.nextInt();			//input der skal sendes til robot
				System.out.println("input = " + input);
				int i = input;
				dos.write(i);						//sender
				dos.flush();						//flusher
				System.out.println("send " + i);
				input = scan.nextInt();				//input der skal sendes til robot
				System.out.println("input = " + input);
				i = input;
				dos.write(i);						//sender
				dos.flush();						//flusher
				System.out.println("send " + i);
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
	}
}
