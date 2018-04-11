/*
 *  This file is part of the Multimodal Mobility Analyser(MMA), based
 *  on the Smartphone Sensing Framework (SSF)

    MMA (also SSF) is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MMA (also SSF) is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.example.ssf.mma.userInterface;


/**
 * This class provides the Main Activity of the application
 * Its only activ responsibility is the management of the UI.
 * Everything else is a passiv responsibility and is management by typical OOP or callbacks
 * @author D. Lagamtzis
 * @version 2.0
 */
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import edu.example.ssf.mma.R;
import edu.example.ssf.mma.data.CsvFileWriter;
import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.HardwareFactory;
import edu.example.ssf.mma.timer.StateMachineHandler;

public class MainActivity extends AppCompatActivity {

    public static boolean isMeasuring = false;
    public static boolean carOnStart = false;
    public static boolean isRacing = false;
    // Init HW-Factory
    HardwareFactory hw;

    //Permissions Android
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    // UI
    private TextView instructionText;
    private ImageView light1;
    private ImageView light2;
    private ImageView light3;
    private ImageView light4;
    private ImageView light5;
    private Button button;


    /** Declaration of the state machine. */
    private StateMachineHandler stateMachineHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new  String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);

        //UI -- Textviews init
        instructionText = findViewById(R.id.instructionText);
        light1 = findViewById(R.id.light1);
        light2 = findViewById(R.id.light2);
        light3 = findViewById(R.id.light3);
        light4 = findViewById(R.id.light4);
        light5 = findViewById(R.id.light5);
        button = findViewById(R.id.doneButton);
        light1.setImageResource(R.mipmap.lightred);
        setInitialState();
    }

    public void setInitialState(){
        light1.setImageResource(R.mipmap.lightred);
        light2.setImageResource(R.mipmap.lightoff);
        light3.setImageResource(R.mipmap.lightoff);
        light4.setImageResource(R.mipmap.lightoff);
        light5.setImageResource(R.mipmap.lightoff);
        instructionText.setText("Mount device on car");
        button.setText("DONE");
        button.setActivated(true);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickMount(view);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Hardware
                    hw = new HardwareFactory(this);
                    stateMachineHandler = new StateMachineHandler(this);
                }
        }
    }

    public void onClickMount(View v){

        light1.setImageResource(R.mipmap.lightred);
        light2.setImageResource(R.mipmap.lightred);
        light3.setImageResource(R.mipmap.lightoff);
        light4.setImageResource(R.mipmap.lightoff);
        light5.setImageResource(R.mipmap.lightoff);
        instructionText.setText("Calibrate sensors");
        button.setText("CALIBRATE");
        button.setActivated(true);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCalibrate(view);
            }
        });
    }

    public void onClickCalibrate(View v){

        light1.setImageResource(R.mipmap.lightred);
        light2.setImageResource(R.mipmap.lightred);
        light3.setImageResource(R.mipmap.lightyellow);
        light4.setImageResource(R.mipmap.lightoff);
        light5.setImageResource(R.mipmap.lightoff);
        instructionText.setText("Calibrating...");
        button.setText("");
        button.setActivated(false);
        button.setVisibility(View.INVISIBLE);

        HardwareFactory.hwAcc.start();
//        HardwareFactory.hwAcc.enableCalibration();
        HardwareFactory.hwProxi.start();

        light1.setImageResource(R.mipmap.lightred);
        light2.setImageResource(R.mipmap.lightred);
        light3.setImageResource(R.mipmap.lightred);
        light4.setImageResource(R.mipmap.lightoff);
        light5.setImageResource(R.mipmap.lightoff);
        instructionText.setText("Calibration successful");
        button.setText("GET IN POSITION");
        button.setActivated(true);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waitingForStartPosition();
            }
        });
    }

    public void waitingForStartPosition(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                light1.setImageResource(R.mipmap.lightred);
                light2.setImageResource(R.mipmap.lightred);
                light3.setImageResource(R.mipmap.lightred);
                light4.setImageResource(R.mipmap.lightyellow);
                light5.setImageResource(R.mipmap.lightoff);
                instructionText.setText("Place car on start");
                button.setText("");
                button.setActivated(false);
                button.setVisibility(View.INVISIBLE);
            }
        });
        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                while (!carOnStart && !isRacing) {
                    if(CurrentTickData.proxState){
                        carOnStart = true;
                        onReadyForStart();
                    }
                }
            }
        });
        t.start();
    }

    public void onReadyForStart(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                light1.setImageResource(R.mipmap.lightred);
                light2.setImageResource(R.mipmap.lightred);
                light3.setImageResource(R.mipmap.lightred);
                light4.setImageResource(R.mipmap.lightred);
                light5.setImageResource(R.mipmap.lightoff);
                instructionText.setText("You're ready to start");
                button.setText("GO");
                button.setActivated(true);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        button.setActivated(false);
                        button.setVisibility(View.INVISIBLE);
                        onClickStartMeasurements(view);
                    }
                });
            }
        });
        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                while (carOnStart) {
                    if(!CurrentTickData.proxState && !isRacing){
                        carOnStart = false;
                        waitingForStartPosition();
                    }
                }
            }
        });
        t.start();

    }

    public void onClickStartMeasurements(View v){
        carOnStart = false;
        CsvFileWriter.crtFile();
        light1.setImageResource(R.mipmap.lightred);
        light2.setImageResource(R.mipmap.lightred);
        light3.setImageResource(R.mipmap.lightred);
        light4.setImageResource(R.mipmap.lightred);
        light5.setImageResource(R.mipmap.lightred);
        instructionText.setText("");
        button.setText("");
        button.setActivated(false);
        button.setVisibility(View.INVISIBLE);


        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                while (!isMeasuring) {
                    if(!CurrentTickData.proxState){
                        isMeasuring = true;
                        stateMachineHandler.startStateMachine();
                        onRacing();
                    }
                }
            }
        });
        t.start();

    }

    public void onRacing(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isRacing = true;


                light1.setImageResource(R.mipmap.lightoff);
                light2.setImageResource(R.mipmap.lightoff);
                light3.setImageResource(R.mipmap.lightoff);
                light4.setImageResource(R.mipmap.lightoff);
                light5.setImageResource(R.mipmap.lightoff);
                instructionText.setText("");
                button.setText("FINISH");
                button.setActivated(true);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickFinish(view);
                    }
                });
            }
        });

    }

    public void onClickFinish(View view){
        CsvFileWriter.closeFile();
        CurrentTickData.resetValues();
        stateMachineHandler.stopStateMachine();

        isMeasuring = false;
        carOnStart = false;
        isRacing = false;

        setInitialState();
    }



}
