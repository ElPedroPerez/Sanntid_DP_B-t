/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opencv;


import org.opencv.core.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

/**
 *
 * @author Robin
 */
public class OpenCV
{ 
    private Mat frame;
    private Mat hsvImage;
    private Mat mask;
     private VideoCapture cam;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
        	VideoCapture camera = new VideoCapture(0);
    	
    	if(!camera.isOpened())
        {
    		System.out.println("Error");
    	}
    	else 
        {
    		Mat frame = new Mat();
    	    while(true){
    	    	if (camera.read(frame)){
    	    		System.out.println("Frame Obtained");
    	    		System.out.println("Captured Frame Width " + 
    	    		frame.width() + " Height " + frame.height());
    	    		Imgcodecs.imwrite("camera.jpg", frame);
    	    		System.out.println("OK");
    	    		break;
    	    	}
    	    }	
    	}
    	camera.release();
    }
    
    
     public void run() {
        
           cam = new VideoCapture(0);
        frame = new Mat();
        cam.set(CAP_PROP_FRAME_WIDTH, 320);
        cam.set(CAP_PROP_FRAME_HEIGHT, 240);
       
                 ProcessPicture();
        }
     public  void ProcessPicture(){
     
     //Convert the frame to HSV
        Imgproc.cvtColor(this.frame, this.hsvImage, Imgproc.COLOR_BGR2HSV);

        //Create threshold values, Hue, saturation and colorvalue
        double hueStart = 170;
        double hueStop = 180;
        double saturationStart = 150;
        double saturationStop = 255;
        double valueStart = 150;
        double valueStop = 255;

        //Makes two scalar values, one for starting values and one for stopping 
        //values of the double values above.
        Scalar minValues = new Scalar(hueStart, saturationStart, valueStart);
        Scalar maxValues = new Scalar(hueStop, saturationStop, valueStop);

        //Every color that is within the max and min values will be white..
        //All the other values will be black
        Core.inRange(this.hsvImage, minValues, maxValues, this.mask);
        
        
          } 
    
    }



