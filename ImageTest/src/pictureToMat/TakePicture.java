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
    	VideoCapture camera = new VideoCapture(0);
    	for (int i = 0; i<1;i++){//flere billeder
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
    	    			Highgui.imwrite("billed0.png", frame);

    	    		}
    	    		System.out.println("Picture saved");
    	    		
    	    		
    	    		System.out.println();
    	    		
    	    		break;
    	    	}
    	    }	
    	}
    	camera.release();
	}
}   

