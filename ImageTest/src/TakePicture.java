import org.opencv.core.*;
import org.opencv.highgui.Highgui;        
import org.opencv.highgui.VideoCapture;        
        
public class TakePicture {
    public static void main (String args[]){
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	VideoCapture camera = new VideoCapture(0);
    	for (int i = 0; i<10;i++){
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
    	    		
    	    		
    	    		
    	    			Highgui.imwrite("C:/Users/Emil/Desktop/Pictures/camera"+i+".bmp", frame);
    	    		}
    	    		System.out.println("OK");
    	    		
    	    		
    	    		System.out.println();
    	    		
    	    		break;
    	    	}
    	    }	
    	}
    	camera.release();
    }
}   
/*

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class Hello
{
   public static void main( String[] args )
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
     
      Mat mat1 = Mat.zeros( 10, 10, CvType.CV_8UC1 );
      System.out.println();
      System.out.println();
      System.out.println( "mat1 := ");
      System.out.println( " " + mat1.dump() );
      
      
      System.out.println();
      System.out.println();
      System.out.println( "mat1 := ");
      System.out.println( " " + mat1.dump() );
      
   }
}
*/