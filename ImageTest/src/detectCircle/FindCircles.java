package detectCircle;


import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import java.nio.ByteBuffer;

import javax.swing.WindowConstants;

import com.googlecode.javacpp.*;
import com.googlecode.javacv.*;

/**
 * This is a test class that detects circles in an image.
 * 
 * Note: See 'Learning OpenCV' from Oreilly (Google it) for more information on
 * the used commands.
 * 
 * Note: Turn automatic import organizing off and use asterisks because most of
 * the methods are statically imported (and cannot be found with organize
 * imports).
 * 
 * @author Nico Vervliet
 */
public class FindCircles {
	public static void main(String[] args) {
		// Load the image (located in the project folder (just outside the src
		// folder))
		IplImage img = cvLoadImage("baneJpg.jpg", CV_LOAD_IMAGE_COLOR);

		// Construct two canvas frames to show the images
		CanvasFrame originalCanvas = new CanvasFrame("Out");
		CanvasFrame editedCanvas = new CanvasFrame("Bewerkt");

		// Reserve memory space for a gray and an edged version of the image
		IplImage gray = cvCreateImage(cvGetSize(img), 8, 1);
		IplImage edge = cvCreateImage(cvGetSize(img), 8, 1);

		// Convert the image to gray
		cvCvtColor(img, gray, CV_BGR2GRAY);

		// Set the converted image's origin (not needed?)
		gray.origin(1);

		// Throw unwanted colors away??
		cvThreshold(gray, gray, 150, 255, CV_THRESH_BINARY);

		// Smooth the image to reduce unneccesary results
		cvSmooth(gray, gray, CV_GAUSSIAN, 11);

		// Detect the edges of the circles
		int edge_thresh = 1;
		cvCanny(gray, edge, edge_thresh, (float) edge_thresh * 3, 5);

		// Create memory space to store the circles
		CvMemStorage cstorage = CvMemStorage.create();

		// Find the circles
		CvSeq circles = cvHoughCircles(gray, cstorage, CV_HOUGH_GRADIENT, 2,
				(double) gray.height() / 2, 50, 300, 40, 150);
		// CvSeq circles = cvHoughCircles(gray, cstorage, CV_HOUGH_GRADIENT, 2,
		// (double) gray.height() / 2, 5, 9, 40, 110);
		// CvSeq circles = cvHoughCircles(gray, cstorage, CV_HOUGH_GRADIENT, 2,
		// (double) gray.height() / 50, edge_thresh * 3, edge_thresh, 10,
		// 35);

		// Print the number of circles found
		System.out.println(circles.total());

		for (int i = 0; i < circles.total(); i++) {
			// Get the pointer to the current circle
			Pointer p = cvGetSeqElem(circles, i);
			// Define a buffer for the data (3 floats of 4 bytes are needed, so
			// 12 bytes needed in buffer)s
			ByteBuffer floatBuffer = p.asByteBuffer();

			// Get the x, y and r from memory. Floats are stored in 4 bytes
			float x = floatBuffer.getFloat();
			float y = floatBuffer.getFloat(4);
			float r = floatBuffer.getFloat(8);
			System.out.println(x + ", " + y + ", " + r);

			// Define the center point and the radius
			CvPoint center = new CvPoint(Math.round(x), Math.round(y));
			int radius = Math.round(r);

			// Draw the center of the circle
			cvCircle(img, center, 3, CvScalar.GREEN, -1, 8, 0);
			// Draw the contour of the circle
			cvCircle(img, center, radius, CvScalar.BLUE, 3, 8, 0);

			// Note: for some reason the circles aren't located on the exact
			// center of the circle.
		}

		// Show the two resulting images
		originalCanvas.showImage(img);
		editedCanvas.showImage(gray);

		// Make sure the application stops after closing the original image
		// window.
		originalCanvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}