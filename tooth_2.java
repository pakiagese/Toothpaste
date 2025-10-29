import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_imgcodecs.*;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;

public class ToothpasteDetector {
    public static void main(String[] args) {
        String imagePath = "path/to/your/image.jpg"; // Replace with path
        Mat src = imread(imagePath);
        if (src.empty()) {
            System.err.println("Could not load image!");
            return;
        }
        
        // Convert to HSV for color detection (toothpaste often blue/white)
        Mat hsv = new Mat();
        cvtColor(src, hsv, COLOR_BGR2HSV);
        
        // Define range for blue toothpaste (adjust lower/upper)
        Scalar lowerBlue = new Scalar(100, 50, 50);
        Scalar upperBlue = new Scalar(130, 255, 255);
        Mat mask = new Mat();
        inRange(hsv, lowerBlue, upperBlue, mask);
        
        // Find contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(mask, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        
        // Filter for tube-like rectangles (aspect ratio ~1:3)
        int detections = 0;
        for (MatOfPoint contour : contours) {
            Rect rect = boundingRect(contour);
            if (rect.width > 20 && rect.height > 50 && rect.width * 3 > rect.height * 2) {
                rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
                detections++;
            }
        }
        
        imshow("Toothpaste Detections", src);
        waitKey(0);
        System.out.println("Detected " + detections + " toothpaste tubes.");
    }
}