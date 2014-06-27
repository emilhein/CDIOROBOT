package connect;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

import java.io.*;
import java.util.*;

public class Pc_connect_times10 {
	private static Scanner scan;

	public static void main(String [] args)
	{ 
		try
		{ 
	//		prøver at forbinde til vores robot
			NXTInfo nxtInfo = new NXTInfo(2,"G9 awesome!","0016530918D4");
			NXTInfo nxtInfo2 = new NXTInfo(2, "G9 NXT", "00165312B12E");//robot nr 2
			NXTConnector connt = new NXTConnector();
			System.out.println("trying to connect");
			connt.connectTo(nxtInfo, NXTComm.LCP);
			System.out.println("connected");		//forbundet
	//		åbner streams
			OutputStream dos = connt.getOutputStream();
//			InputStream dis = connt.getInputStream(); bruges ikke længere
			scan = new Scanner(System.in);			//scanner til manuel input
	//		test
			while(true)
			{							
				System.out.println("Hvilken kommando (11,21 etc..)");				
				int input1 = scan.nextInt(); 				//input fra keyboard
				System.out.println("Hvor mange grader?");				

				int input2 = scan.nextInt();				//input fra keyboard

				for(int a = 0; a<10;a++){
				dos.write(input1);							//sender til robot
				dos.flush();								//flusher
				Thread.sleep(500);
				dos.write(input2);							//sender til robot
				dos.flush();								//flusher
				Thread.sleep(500);
				}

			}
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
	}
}
