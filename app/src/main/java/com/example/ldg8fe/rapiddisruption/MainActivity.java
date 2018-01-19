package com.example.ldg8fe.rapiddisruption;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ldg8fe.rapiddisruption.charts.AccChart;
import com.example.ldg8fe.rapiddisruption.config.ConfigApp;
import com.example.ldg8fe.rapiddisruption.data.CsvFileWriter;
import com.example.ldg8fe.rapiddisruption.data.CurrentTickData;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.HardwareFactory;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.microphone.microphone;
import com.example.ldg8fe.rapiddisruption.timer.StateMachineHandler;
import com.example.ldg8fe.rapiddisruption.userInterface.ListFileActivity;


import android.Manifest;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    /** Declaration of the attribute for the String that is used as the TAG, when working with LOGs. String is "ActiveStateUIActivity" */


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static boolean checked = false;
    private boolean navigationBool = false;
    private int id;

    // Init HW-Factory
    HardwareFactory hw;

    //Permissions Android
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    // UI
    private TextView tv;
    private TextView val1;
    private TextView val2;
    private TextView val3;
    private TextView val4;
    private TextView val5;
    private static TextView textViewActState;
    private EditText et;
    private ToggleButton recButton, mmaButton, eventButton;
    private Button file,showChart;

    //Text View Result
    private String deafault = "Please Choose your Sensor to Display!";


    /** Declaration of the state machine. */
    private StateMachineHandler stateMachineHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new  String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);

        //UI -- Textviews
        et   = findViewById(R.id.editText);
        tv = findViewById(R.id.test);tv.setText(deafault);
        val1 = findViewById(R.id.TextOne);
        val2 = findViewById(R.id.TextTwo);
        val3 = findViewById(R.id.TextThree);
        val4 = findViewById(R.id.TextFour);
        val5 =  findViewById(R.id.TextFive);
        textViewActState=findViewById(R.id.textViewActState);
        textViewActState.setText("");

        // NavigationView
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Buttons & Toggle Buttons
        file = findViewById(R.id.fileexplorer);
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, ListFileActivity.class);
                startActivity(intent);
            }
        });

        recButton = findViewById(R.id.recMic);
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ConfigApp.isSimulation){
                        checked = true;
                    Log.d("CHECKED", "called");
                }
                else {
                    if (recButton.isChecked() == true) {
                        onClickMicREC();
                        CsvFileWriter.crtFile();
                        checked = true;
                    } else {
                        onClickMicREC();
                        CurrentTickData.resetValues();
                        CsvFileWriter.closeFile();
                        stateMachineHandler.stopStateMachine();
                    }
                }
            }
        });

        eventButton = findViewById(R.id.eventHandler);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((mmaButton.isChecked() == true) || (recButton.isChecked() == true )){

                    if (eventButton.isChecked() == true) {
                        if(et.getText().toString().trim().length() == 0){
                            CurrentTickData.event = "Event Occured";
                        }
                        else{
                            CurrentTickData.event = et.getText().toString();
                        }

                    } else {
                        CurrentTickData.event = "";

                    }
                    Log.d("event", "" + CurrentTickData.event);
                }
                else {
                    Toast.makeText(MainActivity.this,"Please first start the MMA", Toast.LENGTH_LONG).show();
                    eventButton.setChecked(false);
                }
                if(eventButton.isChecked() == false){
                    et.setText(null);
                }
            }
        });
        mmaButton = findViewById(R.id.mmaButton);
        mmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t.isAlive()){}
                else{
                    t.start();}
                if (mmaButton.isChecked()) {

                    HardwareFactory.hwAcc.start();
                    HardwareFactory.hwGPS.initGPS(MainActivity.this);
                    HardwareFactory.hwGyro.start();
                    HardwareFactory.hwMagn.start();
                    HardwareFactory.hwProxi.start();
                    stateMachineHandler.startStateMachine();
                    showChart.setVisibility(View.VISIBLE);
                }else{
                    HardwareFactory.hwAcc.stop();
                    HardwareFactory.hwGPS.stop();
                    HardwareFactory.hwGyro.stop();
                    HardwareFactory.hwMagn.stop();
                    HardwareFactory.hwProxi.stop();
                    CurrentTickData.resetValues();
                }

            }
        });
        showChart = findViewById(R.id.showChart);
        showChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t.isAlive()){}
                else{
                    t.start();}
                if(showChart.isPressed()){
                    Intent intent = new Intent(MainActivity.this, AccChart.class);
                    startActivity(intent);
                }
            }
        });
        //Buttons & Toggle Buttons


        stateMachineHandler=new StateMachineHandler(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Hardware
                    hw = new HardwareFactory(this);
                } else {
                }
        }
    }
    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();
        if (navigationBool == true) {
            navigationBool = false;
            onClickUI();
        }
            if (id == R.id.nav_acc) {
                tv.setText("Accelerometer");
                navigationBool = true;
                onClickUI();
            } else if (id == R.id.nav_gyro) {
                tv.setText("Gyroscope");
                navigationBool = true;
                onClickUI();
            } else if (id == R.id.nav_magn) {
                tv.setText("Magnetic Field Sensor");
                navigationBool = true;
                onClickUI();
            } else if (id == R.id.nav_mic) {
                tv.setText("Microphone");
                navigationBool = true;
                onClickUI();
            } else if (id == R.id.nav_gps) {
                tv.setText("GPS");
                navigationBool = true;
                onClickUI();
            } else if (id == R.id.nav_proximity) {
                tv.setText("Proximity");
                navigationBool = true;
                onClickUI();
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actState(String state){
        textViewActState.setText(state);

    }

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (id == R.id.nav_acc) {
                            val5.setVisibility(View.INVISIBLE);
                            HardwareFactory.hwAcc.accUI(CurrentTickData.accX, CurrentTickData.accY, CurrentTickData.accZ, CurrentTickData.accVecA,val1,val2,val3,val4);
                        } else if (id == R.id.nav_gps) {
                            HardwareFactory.hwGPS.gpsUI(CurrentTickData.GPSalt, CurrentTickData.GPSlat, CurrentTickData.GPSlon,  CurrentTickData.GPSbearing, CurrentTickData.GPSspeed, val1, val2, val3, val4, val5);
                            Log.d("GPS", "lat : " + CurrentTickData.GPSlat + "---" + "lon : " + CurrentTickData.GPSlon );
                        } else if (id == R.id.nav_gyro){
                            val4.setVisibility(View.INVISIBLE);
                            val5.setVisibility(View.INVISIBLE);
                            HardwareFactory.hwGyro.gyroUI(CurrentTickData.rotationX, CurrentTickData.rotationY, CurrentTickData.rotationZ, val1, val2, val3);
                            // Log.d("GPS", "lat : " + CurrentTickData. + "---" + "lon : " + CurrentTickData. );
                        } else if (id == R.id.nav_mic) {
                            val2.setVisibility(View.INVISIBLE);val3.setVisibility(View.INVISIBLE);val4.setVisibility(View.INVISIBLE);val5.setVisibility(View.INVISIBLE);
                            val1.setText("Max Aplitude : " + CurrentTickData.micMaxAmpl);

                        } else if (id == R.id.nav_magn){
                            val4.setVisibility(View.INVISIBLE);
                            val5.setVisibility(View.INVISIBLE);
                            HardwareFactory.hwMagn.magnUI(CurrentTickData.magneticX, CurrentTickData.magneticY, CurrentTickData.magneticZ, val1, val2, val3);
                            // Log.d("GPS", "lat : " + CurrentTickData. + "---" + "lon : " + CurrentTickData. );
                        }else if (id == R.id.nav_proximity) {
                            val2.setVisibility(View.INVISIBLE);val3.setVisibility(View.INVISIBLE);val4.setVisibility(View.INVISIBLE);val5.setVisibility(View.INVISIBLE);
                            HardwareFactory.hwProxi.proxiUI(CurrentTickData.proxState, val1);
                        }


                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }
        }
    });

    /** TODO SEPARATE INTO OWN INTERFACE SoC
     *  onClick Handlers for different Sensors + UI (later non textual but graphical )
     */
    public void onClickUI(){
        if(navigationBool == true) {
            //Thread
            if (t.isAlive()){}
            else{
                t.start();}
            // UI
            show_hideUI();

        } else {
            //UI
            tv.setText(deafault);
            show_hideUI();

        }
    }
    public void onClickMicREC(){
        if(recButton.isChecked()) {
            //Thread
            if (t.isAlive()){}
            else{
                t.start();}
            //Sensor
            HardwareFactory.hwMic.start();
            Toast.makeText(this,"Aufnahme beginnt", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"Datei geöffnet", Toast.LENGTH_LONG).show();
        } else {
            //Sensor
            HardwareFactory.hwMic.stop();
            Toast.makeText(this,"Aufnahme beendet", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"Datei geschlossen", Toast.LENGTH_LONG).show();
        }
    }
    public void show_hideUI(){
        if(val1.getVisibility() == View.VISIBLE){
            val1.setVisibility(View.INVISIBLE);
            val2.setVisibility(View.INVISIBLE);
            val3.setVisibility(View.INVISIBLE);
            val4.setVisibility(View.INVISIBLE);
            val5.setVisibility(View.INVISIBLE);
        }else if(val1.getVisibility() == View.INVISIBLE){
            val1.setVisibility(View.VISIBLE);
            val2.setVisibility(View.VISIBLE);
            val3.setVisibility(View.VISIBLE);
            val4.setVisibility(View.VISIBLE);
            val5.setVisibility(View.VISIBLE);

        }
    }
}
