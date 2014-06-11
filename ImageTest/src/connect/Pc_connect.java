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
	//		prøver at forbinde til vores robot
			NXTInfo nxtInfo2 = new NXTInfo(2,"G9 awesome!","0016530918D4");
			NXTInfo nxtInfo = new NXTInfo(2, "G9 NXT", "00165312B12E");//robot nr 2
			NXTConnector connt = new NXTConnector();
			System.out.println("trying to connect");
			connt.connectTo(nxtInfo, NXTComm.LCP);
			System.out.println("connected");		//forbundet
	//		åbner streams
			OutputStream dos = connt.getOutputStream();
			InputStream dis = connt.getInputStream();
			scan = new Scanner(System.in);			//scanner til manuel input
	//		test
			while(true)
			{							
				int input = scan.nextInt();
				System.out.println("input = " + input);
				int i = input;
				dos.write(i);
				dos.flush();
				System.out.println("send " + i);
				input = scan.nextInt();
				System.out.println("input = " + input);
				i = input;
				dos.write(i);
				dos.flush();
				System.out.println("send " + i);
				int u = dis.read();
				System.out.println("recived " + u);			
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
	}
}
