package opencv;

import java.awt.image.BufferedImage;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

public class VideoCap
{

    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    VideoCap()
    {
        cap = new VideoCapture();
        cap.open(1);
        cap.set(3, 1920.00);
        cap.set(4, 1080.00);
        
    }

    BufferedImage getOneFrame()
    {
        cap.read(mat2Img.mat);
        return mat2Img.getImage(mat2Img.mat);
    }
}
