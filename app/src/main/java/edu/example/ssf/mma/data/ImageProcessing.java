package edu.example.ssf.mma.data;

/**
 * Image Processing Class for opencv functions
 *
 * @author D. Lagamtzis
 * @version 1.0
 */

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public abstract class ImageProcessing {

    public static Mat ProcessImage(Mat rgbaImage) {
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(rgbaImage, hsvImage, Imgproc.COLOR_RGB2HSV);

        Mat maskedImage = new Mat();

        int sen = 25;
        Core.inRange(hsvImage, new Scalar(0, 0, 255 - sen), new Scalar(255, sen, 255), maskedImage);

        Mat tmp = new Mat();
        Core.bitwise_and(rgbaImage, rgbaImage, tmp, maskedImage);
        Mat grayImageMat = MatToGrayscale(tmp);

        //Mat tmp2 = grayImageMat.clone();
        //Imgproc.erode(grayImageMat, tmp2, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(25, 25)));


        // Hough Line Transformation
        Mat edges = MatToGrayscaleHoughlines(grayImageMat);
        Mat lines = new Mat();
        int threshold = 50;
        int minLineSize = 20;
        int lineGap = 10;
        Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, threshold, minLineSize, lineGap);

        return lines;
    }


    private static Mat MatToGrayscale(Mat image) {
        Mat grayImage = new Mat(image.cols(), image.rows(), CvType.CV_8UC1);
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(grayImage, grayImage, Imgproc.COLOR_GRAY2RGBA, 4);
        return grayImage;
    }

    private static Mat MatToGrayscaleHoughlines(Mat image) {
        Mat grayImage = new Mat(image.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(grayImage, grayImage, 50, 150);
        return grayImage;
    }

    public static Bitmap MatToBitmap(Mat imageMat) {
        Bitmap imageBitmap = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, imageBitmap);
        return imageBitmap;
    }

}
