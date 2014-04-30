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
			NXTInfo nxtInfo = new NXTInfo(2,"G9 awesome!","0016530918D4");
			NXTConnector connt = new NXTConnector();
			System.out.println("trying to connect");
			connt.connectTo(nxtInfo, NXTComm.LCP);
			System.out.println("connected");

			OutputStream dos = connt.getOutputStream();
			InputStream dis = connt.getInputStream();
			scan = new Scanner(System.in);
			while(true){
			int input = scan.nextInt();
			System.out.println("input = " + input);
			int i = input;
			dos.write(i);
			dos.flush();
			Thread.sleep(500);
			int u = dis.read();
			System.out.println("send " + i);
			System.out.println("recived " + u);
			System.out.println("done ");
//		public static void runRobot(int minLength, int TurnAngle){
//			int i;
//			int Case;
//			int angle = TurnAngle/5;
//			if(angle < 0) 
//				Case = 11;
//			else Case = 22;
//			
//			i = Case;
//			dos.write(i);
//			dos.flush();
//			i = angle;
//			dos.write(i);
//			dos.flush();
			
//			int u = dis.read();
//			while(u=1){
//			u = dis.read();
//			}
//			
//			int dist = minLength * 3;
//			dos.write(51);
//			dos.flush();
//			i = dist;
//			dos.write(i);
//			dos.flush();
			}
			
//			}
		}
		catch(Exception ex){System.out.println(ex);}
	}
}
