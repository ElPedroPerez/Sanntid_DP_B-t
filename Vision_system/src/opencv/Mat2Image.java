package opencv;

import org.opencv.core.*;

import java.awt.image.BufferedImage;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import java.util.Random;
import javax.swing.JLabel;

public class Mat2Image
{

    private UDPSender udpSender;
    private Random rng = new Random(12345);
    private JLabel imgContoursLabel;
    private static final double FOCAL_LENGTH = 293.23;//focal length in pixels obtained from Calibration class
    private static final double RADIUS_OF_MARKER = 12;
    double distance = 0;
    List myDistanceList = new ArrayList();
    List list = new ArrayList();
    List hsvList = new ArrayList();
    Mat src = new Mat();
    Mat mat = new Mat();
    Mat smooth = new Mat();
    Mat onlyColorRange = new Mat();
    Mat greyscale = new Mat();
    Mat circles = new Mat();
    Mat hsvPicture = new Mat();
    Mat mask = new Mat();
    Mat hue = new Mat();
    Mat sat = new Mat();
    Mat val = new Mat();
    Mat hVal = new Mat();
    Mat sVal = new Mat();
    Mat vVal = new Mat();
    Mat bitWise = new Mat();
    // Mat kernel = new Mat();
    List<MatOfPoint> contoursList = new ArrayList<MatOfPoint>();
    BufferedImage img;
    byte[] dat;
    double x = 0.0;
    double y = 0.0;
    int r = 0;
    double minVariance = 200;
    double maxVariance = 0;
    double totVariance = 0;
    DistanceCalculator distanceCalculator = new DistanceCalculator();

    public Mat2Image()
    {
    }

    public Mat2Image(Mat mat)
    {
        getSpace(mat);
    }

    public void getSpace(Mat mat)
    {

        this.mat = mat;
        this.src = mat;
        this.hsvPicture = mat;
        this.onlyColorRange = mat;
        this.greyscale = mat;
        this.circles = mat;
        this.mask = mat;
        this.hue = mat;
        this.sat = mat;
        this.val = mat;
        this.hVal = mat;
        this.sVal = mat;
        this.vVal = mat;
        this.bitWise = mat;

        //InternalWebCam
//        Scalar lower_color_bounds = new Scalar(145 / 2, (70 * 255) / 100, (40 * 255) / 100);
//        Scalar upper_color_bounds = new Scalar(183 / 2, (100 * 255) / 100, (100 * 255) / 100);
        //ExtrenalWebCam
        Scalar lower_color_bounds = new Scalar(68 / 2, (80 * 255) / 100, (50 * 255) / 100);
        Scalar upper_color_bounds = new Scalar(120 / 2, (100 * 255) / 100, (100 * 255) / 100);

        // Use this site https://alloyui.com/examples/color-picker/hsv
        // for picking colors. Convertion is done in this software
        Scalar hmn = new Scalar(0);
        Scalar hmx = new Scalar(180);

        Scalar smn = new Scalar((30 * 255) / 100);
        Scalar smx = new Scalar((40 * 255) / 100);

        Scalar vmn = new Scalar((50 * 255) / 100);
        Scalar vmx = new Scalar((65 * 255) / 100);

        //mat.convertTo(mat, -1, 0.7, 0);
        Imgproc.cvtColor(mat, hsvPicture, Imgproc.COLOR_BGR2HSV);
        //Imgproc.cvtColor(hsvPicture, greyscale, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(hsvPicture, smooth, new Size(11, 11), 4, 4);

//        Find countours
        Core.inRange(smooth, lower_color_bounds,
                upper_color_bounds, onlyColorRange);

// Splitting HSV picture into seperate H, S and V channel and adds them to an
// arraylist. Then masking each and everyone of them to a selected HSV range
// making it a binary image
//        Core.split(smooth, hsvList);
//        Core.inRange((Mat) hsvList.get(0), hmn, hmx, hVal);
//        Core.inRange((Mat) hsvList.get(1), smn, smx, sVal);
//        Core.inRange((Mat) hsvList.get(2), vmn, vmx, vVal);
        // Takes the binary 3 images and laying them ontop of each other with a
// bitwise AND operation.    
//        Core.bitwise_and(hVal, sVal, bitWise);
//        Core.bitwise_and(bitWise, vVal, bitWise);
//sVal = bitWise ;
// HoughCircles not in use
        // Imgproc.HoughCircles(bitWise, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 1000, 200, 5000, 0, 1000);
        Mat cannyOutput = new Mat();
        Imgproc.Canny(bitWise, cannyOutput, 100, 100 * 2);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        Point[] centers = new Point[contours.size()];
        float[][] radius = new float[contours.size()][1];
        for (int i = 0; i < contours.size(); i++)
        {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
            centers[i] = new Point();
            Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
        }

        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);

        List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
        for (MatOfPoint2f poly : contoursPoly)
        {
            contoursPolyList.add(new MatOfPoint(poly.toArray()));
        }

        float myRadius = 0;
        int myRadiusIndex = 0;
        //Finding biggest circle in array 
        for (int i = 0; i < contours.size(); i++)
        {
            if (radius[i][0] > myRadius)
            {
                myRadius = radius[i][0];
                myRadiusIndex = i;
            }
        }

//        for (int i = 0; i < contours.size(); i++)
//        {
        try
        {
            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            Imgproc.drawContours(onlyColorRange, contoursPolyList, myRadiusIndex, color);
            //Imgproc.rectangle(onlyColorRange, boundRect[i].tl(), boundRect[i].br(), color, 2);
            Imgproc.circle(onlyColorRange, centers[myRadiusIndex], (int) radius[myRadiusIndex][0], color, 2);
//                System.out.println("Radius is: " + radius[i][0]);
            distance = ((FOCAL_LENGTH * RADIUS_OF_MARKER) / radius[myRadiusIndex][0]);
            if (distance > 0 && distance <= 1000)
            {
                myDistanceList.add(distance);
            }

            if (myDistanceList.size() >= 50)
            {
                for (int number = 0; number < myDistanceList.size(); number++)
                {
                    distance = distance + (double) myDistanceList.get(number);
                }
                distance = (distance / myDistanceList.size());

                System.out.println("Distance is " + distance + " cm");

                if (distance > maxVariance)
                {
                    maxVariance = distance;
                }

                if (distance < minVariance)
                {
                    minVariance = distance;
                }

                System.out.println("Variance is: " + (maxVariance - minVariance) + " cm");

                myDistanceList.clear();
                double averageCenter = 0;

                for (int i = 0; i < centers.length; i++)
                {
                    averageCenter = averageCenter + centers[i].x;
                }
                averageCenter = averageCenter / centers.length;
                averageCenter = averageCenter - 325;
                if (averageCenter < 0)
                {
                    averageCenter = averageCenter * -1;
                }
                System.out.println("Center is at X: " + averageCenter);
                double Y = sqrt(averageCenter * averageCenter - distance * distance);
                System.out.println("Y: " + Y);
//                this.udpSender = new UDPSender();
//                udpSender.send("<Distance:" + (Math.round(distance * 100.0) / 100.0) + ":AverageCenter:" + (Math.round(averageCenter * 100.0) / 100.0) + ":Y:" +  (Math.round(Y * 100.0) / 100.0) + ">" );
                //distanceCalculator.distanceToShip(averageCenter, distance);
            }

            distance = 0;
//                Thread.sleep(100);

        } catch (Exception e)
        {
            //System.out.println("Something went wrong..." + e);
        }

//        }
        int w = mat.cols(), h = mat.rows();

        if (dat == null || dat.length != w * h * 3)
        {
            dat = new byte[w * h * 3];
        }
        if (img == null || img.getWidth() != w || img.getHeight() != h
                || img.getType() != BufferedImage.TYPE_BYTE_GRAY)
        {
            img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        }

    }

    BufferedImage getImage(Mat mat)
    {
        getSpace(mat);
        mat.get(0, 0, dat);
        img.getRaster().setDataElements(0, 0,
                mat.cols(), mat.rows(), dat);
        return img;
    }

    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

}
