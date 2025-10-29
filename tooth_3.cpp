#include <opencv2/opencv.hpp>
#include <iostream>

using namespace cv;
using namespace std;

int main(int argc, char** argv) {
    if (argc < 3) {
        cout << "Usage: " << argv[0] << " <image> <template>" << endl;
        return -1;
    }
    
    Mat img = imread(argv[1], IMREAD_COLOR);
    Mat templ = imread(argv[2], IMREAD_COLOR); // Template of toothpaste tube
    
    if (img.empty() || templ.empty()) {
        cout << "Could not load images!" << endl;
        return -1;
    }
    
    Mat result;
    matchTemplate(img, templ, result, TM_CCOEFF_NORMED);
    
    double minVal, maxVal;
    Point minLoc, maxLoc;
    minMaxLoc(result, &minVal, &maxVal, &minLoc, &maxLoc);
    
    // Threshold for detection
    if (maxVal > 0.8) { // Adjust threshold
        rectangle(img, maxLoc, Point(maxLoc.x + templ.cols, maxLoc.y + templ.rows), Scalar(0, 255, 0), 2);
        cout << "Toothpaste detected at (" << maxLoc.x << ", " << maxLoc.y << ")" << endl;
    } else {
        cout << "No toothpaste detected." << endl;
    }
    
    imshow("Detection", img);
    waitKey(0);
    return 0;
}