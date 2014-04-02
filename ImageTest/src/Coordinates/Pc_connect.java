package Coordinates;

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
			scan = new Scanner(System.in);
			while(true){
			int input = scan.nextInt();
			System.out.println("input = " + input);
			int i = input;
			dos.write(i);
			dos.flush();
			System.out.println("send " + i);
			}
		}
		catch(Exception ex){System.out.println(ex);}
	}
}
