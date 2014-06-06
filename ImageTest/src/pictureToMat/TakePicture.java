package pictureToMat;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;        
import org.opencv.highgui.VideoCapture;        
        
public class TakePicture {
    public  void main (String args[]){
    	
    }
//XXX
	public  void takePicture() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
    	VideoCapture camera = new VideoCapture(0);// us 1 if two cameras a connected (including integrated webcam)
    /*	boolean wset =*/camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1600);
    /*	boolean hset = */camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 900);
	
    	if(!camera.isOpened()){
    		System.out.println("Error");
    	}
    	else {
    		
    		Mat frame = new Mat();
    	    while(true){
    	    	
    	    	if (camera.read(frame)){
    	    		System.out.println("Frame Obtained");
    	    		System.out.println("Captured Frame Width " + 
    	    		frame.width() + " Height " + frame.height());
    	    		
    	    		//	Highgui.imwrite("C:/Users/Emil/Desktop/Pictures/Billed"+i+".jpg", frame);
    	    			Highgui.imwrite("billed0.jpg", frame);
    	  



    	    		}
    	    		System.out.println("Picture saved");
    	    		
    	    		
    	    		System.out.println();
    	    		
    	    		break;
    	    	}
    	    }	
    	
    	camera.release();
	}
}   

