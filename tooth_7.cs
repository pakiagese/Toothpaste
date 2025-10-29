using System;
using Emgu.CV;
using Emgu.CV.CvEnum;
using Emgu.CV.Structure;
using System.Drawing;

class ToothpasteDetector {
    static void Main(string[] args) {
        if (args.Length < 1) {
            Console.WriteLine("Usage: program <image_path>");
            return;
        }
        
        using (Mat img = CvInvoke.Imread(args[0], ImreadModes.Color))
        using (Mat gray = new Mat())
        using (Mat edges = new Mat()) {
            if (img.IsEmpty) {
                Console.WriteLine("Could not load image!");
                return;
            }
            
            CvInvoke.CvtColor(img, gray, ColorConversion.Bgr2Gray);
            CvInvoke.Canny(gray, edges, 50, 150);
            
            // Hough lines for straight tube edges
            LineSegment2D[] lines = CvInvoke.HoughLinesP(edges, 1, Math.PI / 180, 100, 30, 10);
            
            int detections = 0;
            foreach (LineSegment2D line in lines) {
                if (line.Length > 50) { // Long lines for tube body
                    CvInvoke.Line(img, line.P1, line.P2, new MCvScalar(0, 255, 0), 2);
                    detections++;
                }
            }
            
            CvInvoke.Imshow("Detections", img);
            CvInvoke.WaitKey(0);
            Console.WriteLine($"Detected {detections / 4} potential toothpaste tubes (approx. 4 lines per tube).");
        }
    }
}