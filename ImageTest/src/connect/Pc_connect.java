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
		try{ 
//			prøver at forbinde til vores robot
			NXTInfo nxtInfo = new NXTInfo(2,"G9 awesome!","0016530918D4");
			NXTConnector connt = new NXTConnector();
			System.out.println("trying to connect");
			connt.connectTo(nxtInfo, NXTComm.LCP);
			System.out.println("connected");		//forbundet
//			åbner streams
			OutputStream dos = connt.getOutputStream();
			InputStream dis = connt.getInputStream();
			scan = new Scanner(System.in);			//scanner til manuel input
//			test
			while(true){							
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
//		public static void runRobot(int minLength, int TurnAngle){
//			int Case;					
//			int angle = TurnAngle/5;	//vinkel konvertering
//			if(angle < 0) 				//vælger retning der skal drejes
//				Case = 11;				
//			else Case = 22;
//			
//			dos.write(Case);			//sender case
//			dos.flush();
//			dos.write(angle);			//sender vinkel
//			dos.flush();
			
//			venter på at motorerne ikke kører længere
//			int u = dis.read();			
//			while(u=1){
//			u = dis.read();
//			}
			
//			kører robot frem
//			int dist = (minLength * 3) - 150;	//længde konvertering
//			dos.write(51);
//			dos.flush();
//			i = dist;
//			dos.write(i);
//			dos.flush();
			
//			samler bold op
//			dos.write(31);				
//			dos.flush();
//			dos.write(31);
//			dos.flush();			
			}
//			}
		}
		catch(Exception ex){System.out.println(ex);}
	}
}
