/**
 * Tensorflow Object Detection Prototype Class that works with opencv and tensorflow
 *
 * @author D. Lagamtzis, B. Grau
 * @version 1.0
 */

package edu.example.ssf.mma.imagedetection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import edu.example.ssf.mma.R;

import static edu.example.ssf.mma.userInterface.MainActivity.navigationBool;

public class ImageDetection extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 117;
    private static final float IMAGE_STD = 1;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "output";
    private static final String MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings.txt";

    private Classifier classifier;

    private boolean isRunning = false;

    private TextView textView;

    //java camera view
    private JavaCameraView javaCameraView;
    private Mat mRgba, mRgbaF, mRgbaT;

    //callback loader
    BaseLoaderCallback mCallBackLoader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedetection);
        textView = findViewById(R.id.textView);
        //connect the camera
        javaCameraView = findViewById(R.id.jvc);
        //set visibility
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        //set callback function
        javaCameraView.setCvCameraViewListener(this);

        if (classifier == null) {
            classifier = TensorFlowImageClassifier.create(ImageDetection.this.getAssets(),
                    MODEL_FILE, LABEL_FILE, INPUT_SIZE, IMAGE_MEAN, IMAGE_STD, INPUT_NAME, OUTPUT_NAME);
        }
    }

    @Override
    public void onBackPressed() {
        navigationBool = false;
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
        //release
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

        if(!isRunning)
            new AsyncClassifier().execute(matForNetwork);
        return mRgba;
    }

    class AsyncClassifier extends AsyncTask<Mat, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isRunning = true;
        }

        @Override
        protected String doInBackground(Mat... images) {
            Mat image = images[0];

            Bitmap bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(image, bitmap);
            Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);

            final List<Classifier.Recognition> results = classifier.recognizeImage(newBitmap);

            return results.toString();
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            textView.setText(string);
            isRunning = false;
        }
    }

}