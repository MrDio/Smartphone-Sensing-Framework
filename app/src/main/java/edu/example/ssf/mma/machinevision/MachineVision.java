package edu.example.ssf.mma.machinevision;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.example.ssf.mma.R;
import edu.example.ssf.mma.data.ImageProcessing;
import edu.example.ssf.mma.userInterface.ListColorActivity;
import edu.example.ssf.mma.userInterface.ListFileActivity;


import static org.opencv.core.Core.FILLED;
import static org.opencv.core.CvType.CV_8UC4;

/**
 * Machine Vision Prototype Class that works with opencv
 *
 * @author D. Lagamtzis
 * @version 1.0
 */

public class MachineVision extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    //Permissions Android

    //java camera view
    JavaCameraView javaCameraView;
    private Button saveColorButton;
    private Button colorListButton;
    private EditText editText2;


    private boolean saveColorFlag;


    boolean colorSelected = false;
    double rS = 0;
    double gS = 0;
    double bS = 0;

    Boolean sameColorRadioButtonFlag = true;
    Boolean complementaryColorRadioButtonFlag = false;

    int LAUNCH_COLOR_LIST_ACTIVITY = 1;

    private String path;


    private SeekBar seekBar;
    Mat mRgba, mRgbaF, mRgbaT;
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
        setContentView(R.layout.activity_machine_vision);

        //connect the camera
        javaCameraView = findViewById(R.id.jvc);
        //set visibility
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        //set callback function
        javaCameraView.setCvCameraViewListener(this);


        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.complementaryColorRadioButton:
                        complementaryColorRadioButtonFlag = true;
                        sameColorRadioButtonFlag = false;
                        break;
                    case R.id.sameColorRadioButton:
                        sameColorRadioButtonFlag = true;
                        complementaryColorRadioButtonFlag = false;
                        break;
                }
            }
        });

        editText2 = findViewById(R.id.editText2);

        //Buttons & Toggle Buttons
        saveColorButton = findViewById(R.id.saveColorButton);
        saveColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveColorFlag)
                    saveColorFlag = false;
                else {
                    saveColorFlag = true;
                }
            }
        });

        //Buttons & Toggle Buttons
        colorListButton = findViewById(R.id.colorListButton);
        colorListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activity2Intent = new Intent(MachineVision.this, ListColorActivity.class);
                activity2Intent.putExtra("key1", "var1");
                startActivityForResult(activity2Intent, LAUNCH_COLOR_LIST_ACTIVITY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_COLOR_LIST_ACTIVITY) {
            if (resultCode == ListColorActivity.RESULT_OK) {
                String[] result = data.getStringArrayExtra("result");
                rS = Double.valueOf(result[2]);
                gS = Double.valueOf(result[3]);
                bS = Double.valueOf(result[4]);
                colorSelected = true;
                Toast.makeText(this, result[0], Toast.LENGTH_SHORT).show();
            }
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
        mRgba = new Mat(height, width, CV_8UC4);
        mRgbaF = new Mat(height, width, CV_8UC4);
        mRgbaT = new Mat(width, width, CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        //release
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        String path = "config.txt";


        //get each frame from camera
        mRgba = inputFrame.rgba();

        // Rotate mRgba 90 degrees
        Core.transpose(mRgba, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0, 0, 0);
        Core.flip(mRgbaF, mRgba, 1);


        int height_all = mRgba.height() / 2 - 75;
        int width_all = mRgba.width() / 2 - 75;


        int height = mRgba.height() / 2 - 25;
        int width = mRgba.width() / 2 - 25;

        Rect rectCrop = new Rect(height, width, 50, 50);
        Mat croppedImage = new Mat(mRgba, rectCrop);

        Scalar sum = Core.sumElems(croppedImage);

        double r = sum.val[0] / 2500;
        double g = sum.val[1] / 2500;
        double b = sum.val[2] / 2500;

        double rT = 0.0;
        double gT = 0.0;
        double bT = 0.0;


        if (sameColorRadioButtonFlag && !complementaryColorRadioButtonFlag) {

            rT = rS;
            gT = gS;
            bT = bS;
        } else if (complementaryColorRadioButtonFlag && !sameColorRadioButtonFlag) {

            rT = 255 - rS;
            gT = 255 - gS;
            bT = 255 - bS;
        }


        double rP = 100 - (Math.abs(rT - r) * 0.39);
        double gP = 100 - (Math.abs(gT - g) * 0.39);
        double bP = 100 - (Math.abs(bT - b) * 0.39);

        String rgb = "";

        rgb = Integer.toString((int) Math.round(0.333 * rP + 0.333 * gP + 0.333 * bP));

        String all = rgb + "%";

        if (!colorSelected) {
            Imgproc.rectangle(
                    mRgba,                    //Matrix obj of the image
                    new Point(height_all, width_all),        //p1
                    new Point(height_all + 150, width_all + 150),       //p2
                    new Scalar(0, 0, 255),     //Scalar object for color
                    5                          //Thickness of the line
            );
            Imgproc.rectangle(
                    mRgba,                    //Matrix obj of the image
                    new Point(height, width),        //p1
                    new Point(height + 50, width + 50),       //p2
                    new Scalar(0, 0, 255),     //Scalar object for color
                    5                          //Thickness of the line
            );
        } else if (colorSelected) {
            if(complementaryColorRadioButtonFlag){
                Imgproc.rectangle(
                        mRgba,                    //Matrix obj of the image
                        new Point(height_all, width_all),        //p1
                        new Point(height_all + 150, width_all + 150),       //p2
                        new Scalar(rT, gT, bT),     //Scalar object for color
                        FILLED                          //Thickness of the line
                );
            }else {
                Imgproc.rectangle(
                        mRgba,                    //Matrix obj of the image
                        new Point(height_all, width_all),        //p1
                        new Point(height_all + 150, width_all + 150),       //p2
                        new Scalar(rS, gS, bS),     //Scalar object for color
                        FILLED                          //Thickness of the line
                );
            }

            Imgproc.rectangle(
                    mRgba,                    //Matrix obj of the image
                    new Point(height, width),        //p1
                    new Point(height + 50, width + 50),       //p2
                    new Scalar(r, g, b),     //Scalar object for color
                    FILLED                          //Thickness of the line
            );
        }

        if (saveColorFlag) {
            // save current color
            LocalDate localDate = LocalDate.now();

            String strValue = editText2.getText().toString();
            if (strValue.indexOf(',') != -1) {
                strValue = strValue.replace(',', ' ');
            }
            if (strValue.equals("")) {
                strValue = localDate.toString();
            }

            File f = new File(path);

            File root = new File(Environment.getExternalStorageDirectory(), "ColorData");
            if (!root.exists()) {
                root.mkdirs();
            }
            File savedFile = new File(root, path);
            if (!(savedFile.exists())) {
                FileWriter writer;
                try {
                    writer = new FileWriter(savedFile);
                    String myString = strValue + "," + localDate + "," + Double.toString(r) + "," + Double.toString(g) + "," + Double.toString(b) + "\n";
                    writer.write(myString);
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                FileWriter writer;
                try {
                    writer = new FileWriter(savedFile, true);
                    String myString = strValue + "," + localDate + "," + Double.toString(r) + "," + Double.toString(g) + "," + Double.toString(b) + "\n";
                    writer.write(myString);
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            saveColorFlag = false;
        }
        if (sameColorRadioButtonFlag || complementaryColorRadioButtonFlag) {
            Imgproc.putText(mRgba, all,
                    new Point(50, mRgba.height() - 50),
                    Core.FONT_HERSHEY_SIMPLEX,
                    1,
                    new Scalar(0, 0, 255),
                    5);
        }
        return mRgba;
    }
}