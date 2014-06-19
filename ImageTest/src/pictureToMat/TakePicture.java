package pictureToMat;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;        
import org.opencv.highgui.VideoCapture;        
        
public class TakePicture {
	
	public void takePicture()
	{		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
    	VideoCapture camera = new VideoCapture(1);// us 1 if two cameras a connected (including integrated webcam)
    	camera.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1600); /* width of camera image */
    	camera.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 900); /* height of camera image */
	
    	if(!camera.isOpened()){
    		System.out.println("Error");
    	}
    	else {
    		
    		while(true)
    		{

        		Mat frame = new Mat();	
    	    	camera.read(frame);
        		//System.out.println("Frame Obtained");
        		System.out.println("Captured Frame Width " + 
        		frame.width() + " Height " + frame.height());
        		
        		if(frame.empty())
        			System.out.println("NULL BILLEDE");
        		else
        		{

            		Highgui.imwrite("billed0.png", frame);

        	    	//System.out.println("Picture saved");
        	    	//System.out.println();
        	    	
        	    	break;
        		}
    		}
    	}	
    	
    	camera.release();
	}
}
