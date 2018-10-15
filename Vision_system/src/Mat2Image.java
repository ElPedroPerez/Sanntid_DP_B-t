package opencv;

import org.opencv.core.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import java.util.Random;
import javax.swing.JLabel;


public class Mat2Image {

    private Random rng = new Random(12345);
    private JLabel imgContoursLabel;
    private static final double FOCAL_LENGTH = 349.4784;//focal length in pixels obtained from Calibration class
    private static final double RADIUS_OF_MARKER = 4.0;
    double distance = 0;
    List myDistanceList = new ArrayList();
    List list = new ArrayList();
    Mat src = new Mat();
    Mat mat = new Mat();
    Mat smooth = new Mat();
    Mat onlyColorRange = new Mat();
    Mat greyscale = new Mat();
    Mat circles = new Mat();
    Mat hsvPicture = new Mat();
    List<MatOfPoint> contoursList = new ArrayList<MatOfPoint>();
    BufferedImage img;
    byte[] dat;
    double x = 0.0;
    double y = 0.0;
    int r = 0;

    public Mat2Image() {
    }

    public Mat2Image(Mat mat) {
        getSpace(mat);
    }

    public void getSpace(Mat mat) {

        this.mat = mat;
        this.src = mat;
        this.hsvPicture = mat;
        this.onlyColorRange = mat;
        this.greyscale = mat;
        this.circles = mat;

        Scalar lower_color_bounds = new Scalar(5, 100, 100);
        Scalar upper_color_bounds = new Scalar(30, 255, 255);

        //mat.convertTo(mat, -1, 0.7, 0);
        Imgproc.cvtColor(mat, hsvPicture, Imgproc.COLOR_BGR2HSV);
        Imgproc.cvtColor(hsvPicture, greyscale, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.GaussianBlur(hsvPicture, smooth, new Size(11, 11), 4, 4);
        
        //Find countours
        

        //Core.inRange(smooth, lower_color_bounds,
          //      upper_color_bounds, onlyColorRange);
          
          
          
          
          

        Mat cannyOutput = new Mat();
        Imgproc.Canny(greyscale, cannyOutput, 100, 100 * 2);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        Point[] centers = new Point[contours.size()];
        float[][] radius = new float[contours.size()][1];
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
            centers[i] = new Point();
            Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
        }

        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);

        List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
        for (MatOfPoint2f poly : contoursPoly) {
            contoursPolyList.add(new MatOfPoint(poly.toArray()));
        }
        for (int i = 0; i < contours.size(); i++) {
            try {
                Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
                Imgproc.drawContours(onlyColorRange, contoursPolyList, i, color);
                //Imgproc.rectangle(onlyColorRange, boundRect[i].tl(), boundRect[i].br(), color, 2);
                Imgproc.circle(onlyColorRange, centers[i], (int) radius[i][0], color, 2);
                distance = (FOCAL_LENGTH * RADIUS_OF_MARKER) / radius[1][0];
                if (distance > 0 && distance <= 100)
                {
                    myDistanceList.add(distance);
                }
                
                if (myDistanceList.size() >= 15) {
                    for (int number = 0; number < myDistanceList.size(); number++) {
                        distance = distance + (double) myDistanceList.get(number);
                        
                    }
                    distance = distance / myDistanceList.size();
                    System.out.println("Distance in cm " + distance);
                    distance = 0;
                    myDistanceList.clear();
                }

            } catch (Exception e) {
                //System.out.println("Something went wrong...");
            }

        }

        //Finding distance
        int w = mat.cols(), h = mat.rows();

        if (dat == null || dat.length != w * h * 3) {
            dat = new byte[w * h * 3];
        }
        if (img == null || img.getWidth() != w || img.getHeight() != h
                || img.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        }

    }

    BufferedImage getImage(Mat mat) {
        getSpace(mat);
        mat.get(0, 0, dat);
        img.getRaster().setDataElements(0, 0,
                mat.cols(), mat.rows(), dat);
        return img;
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    
}
