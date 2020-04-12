/**
 * Tensorflow Object Detection Prototype Class that works with opencv and tensorflow
 *
 * @author D. Lagamtzis, B. Grau
 * @version 1.0
 */

package edu.croptomato.imagedetection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import edu.croptomato.R;
import edu.croptomato.classifier.Classifier;
import edu.croptomato.classifier.TensorFlowImageClassifier;

public class ImageDetection extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private TextView percentageUnripe;
    private TextView percentageRipe;
    private ProgressBar ripenessProgress;

    private Classifier classifier;
    private boolean isRunning = false;

    //java camera view
    private JavaCameraView javaCameraView;
    private Mat mRgba, mRgbaF, mRgbaT;

    //callback loader
    private BaseLoaderCallback mCallBackLoader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == BaseLoaderCallback.SUCCESS) {
                javaCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.percentageRipe = findViewById(R.id.percentageRipe);
        this.percentageUnripe = findViewById(R.id.percentageUnripe);
        this.ripenessProgress = findViewById(R.id.progressBar);

        //connect the camera
        javaCameraView = findViewById(R.id.jvc);
        //set visibility
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        //set callback function
        javaCameraView.setCvCameraViewListener(this);

        if (classifier == null) {
            classifier = TensorFlowImageClassifier.create(ImageDetection.this.getAssets(),
                    Constants.MODEL_FILE, Constants.LABEL_FILE, Constants.INPUT_SIZE, Constants.IMAGE_MEAN,
                    Constants.IMAGE_STD, Constants.INPUT_NAME, Constants.OUTPUT_NAME);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()) {
            Log.d("openCV", "Connected");
            //display when the activity resumed,, callback loader
            mCallBackLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.d("openCV", "Not connected");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mCallBackLoader);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        //4 channel
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //get each frame from camera
        mRgba = inputFrame.rgba();
        Mat matForNetwork = mRgba.clone();
        // Rotate mRgba 90 degrees
        Core.transpose(matForNetwork, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0, 0, 1);
        Core.flip(mRgbaF, matForNetwork, 1);

        if (!isRunning)
            new AsyncClassifier().execute(matForNetwork);
        return mRgba;
    }

    public void updatePercentages(float percentageUnripe, float percentageRipe) {
        this.percentageUnripe.setText(String.format("%.2f%%", percentageUnripe * 100.0f));
        this.percentageRipe.setText(String.format("%.2f%%", percentageRipe * 100.0f));
        this.ripenessProgress.setProgress((int)(percentageRipe * 100.0f));
        this.ripenessProgress.setMax(100);
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    class AsyncClassifier extends AsyncTask<Mat, Void, List<Classifier.Recognition>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setRunning(true);
        }

        @Override
        protected List<Classifier.Recognition> doInBackground(Mat... images) {
            Mat image = images[0];

            Bitmap bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(image, bitmap);
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, Constants.INPUT_SIZE, Constants.INPUT_SIZE, true);

            return classifier.recognizeImage(newBitmap);
        }

        @Override
        protected void onPostExecute(List<Classifier.Recognition> results) {
            super.onPostExecute(results);

            float percentageUnripe = 0.0f;
            float percentageRipe = 0.0f;
            for (Classifier.Recognition result : results) {
                switch (result.getTitle()) {
                    case "unripe":
                        percentageUnripe = result.getConfidence();
                        break;
                    case "ripe":
                        percentageRipe = result.getConfidence();
                        break;
                }
            }

            updatePercentages(percentageUnripe, percentageRipe);
            setRunning(false);
        }
    }

}