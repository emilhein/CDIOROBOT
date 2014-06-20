package pictureToMat;

import com.googlecode.javacv.CanvasFrame;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.*;
import com.googlecode.javacv.OpenCVFrameGrabber;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

public class NewTakepicture {
	private FrameGrabber grabber;
	
	
	public NewTakepicture() {
		grabber = new OpenCVFrameGrabber("");
		grabber.setImageWidth(1600);
		grabber.setImageHeight(900);
		try {
			grabber.start();
		} catch (com.googlecode.javacv.FrameGrabber.Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void takePicture() {
		// Create canvas frame for displaying webcam.
		//CanvasFrame canvas = new CanvasFrame("Webcam");

		// Set Canvas frame to close on exit
		//canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

		// Declare FrameGrabber to import output from webcam
		
		try {
			// Declare img as IplImage
			IplImage img;

			while (true) {

				// inser grabed video fram to IplImage img
				img = grabber.grab();
				img = grabber.grab();
				
				
				// Set canvas size as per dimentions of video frame.
				//canvas.setCanvasSize(640, 480);

				if (img != null) {
					// Flip image horizontally
					// cvFlip(img, img, 1);
					// Show video frame in canvas
					//canvas.showImage(img);
					cvSaveImage("billed0.png", img);
					break;
				}
				grabber.flush();
			}
		} catch (Exception e) {
		}
	}
}