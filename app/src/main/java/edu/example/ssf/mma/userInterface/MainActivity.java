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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.example.ssf.mma.R;
import edu.example.ssf.mma.charts.AccChart;
import edu.example.ssf.mma.config.ConfigApp;
import edu.example.ssf.mma.data.CsvFileWriter;
import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.HardwareFactory;
import edu.example.ssf.mma.machinevision.MachineVision;
import edu.example.ssf.mma.timer.StateMachineHandler;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static boolean mmaCallBackBool = false;
    public static boolean navigationBool = false;
    private int idOfNavObj;

    // Init HW-Factory
    HardwareFactory hw;

    //Permissions Android
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    // UI
    private TextView headerTextView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private static TextView textViewActState;
    private EditText eventEditText;
    private ToggleButton recButton, mmaButton, eventButton;
    private Button fileBrowserButton, showChartButton;

    //Text View Result
    private String defaultMessage = "Please Choose your Sensor to Display!";


    /** Declaration of the state machine. */
    private StateMachineHandler stateMachineHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new  String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_MULTIPLE_REQUEST);

        //UI -- Textviews init
        eventEditText = findViewById(R.id.editText);
        headerTextView = findViewById(R.id.headerTextView);
        headerTextView.setText(defaultMessage);
        textView1 = findViewById(R.id.TextOne);
        textView2 = findViewById(R.id.TextTwo);
        textView3 = findViewById(R.id.TextThree);
        textView4 = findViewById(R.id.TextFour);
        textView5 =  findViewById(R.id.TextFive);
        textViewActState=findViewById(R.id.textViewActState);
        textViewActState.setText("");

        // NavigationView init
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Buttons & Toggle Buttons
        fileBrowserButton = findViewById(R.id.fileexplorer);
        fileBrowserButton.setOnClickListener(new View.OnClickListener() {
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
                        mmaCallBackBool = true;
                    Log.d("CHECKED", "called");
                }
                else {
                    if (recButton.isChecked()) {
                        onClickMicREC();
                        CsvFileWriter.crtFile();
                        mmaCallBackBool = true;
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
                if((mmaButton.isChecked()) || (recButton.isChecked())){

                    if (eventButton.isChecked()) {
                        if(eventEditText.getText().toString().trim().length() == 0){
                            CurrentTickData.event = "Event Occured";
                        }
                        else{
                            CurrentTickData.event = eventEditText.getText().toString();
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
                if(eventButton.isChecked()){
                    eventEditText.setText(null);
                }
            }
        });
        mmaButton = findViewById(R.id.mmaButton);
        mmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t.isAlive()){
                    // Do Nothing
                }
                else{
                    t.start();}
                if (mmaButton.isChecked()) {

                    HardwareFactory.hwAcc.start();
                    HardwareFactory.hwGPS.initGPS(MainActivity.this);
                    HardwareFactory.hwGyro.start();
                    HardwareFactory.hwMagn.start();
                    HardwareFactory.hwProxi.start();
                    stateMachineHandler.startStateMachine();
                    showChartButton.setVisibility(View.VISIBLE);
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
        showChartButton = findViewById(R.id.showChart);
        showChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t.isAlive()){
                    // Do Nothing
                }
                else{
                    t.start();}
                if(showChartButton.isPressed()){
                    Intent intent = new Intent(MainActivity.this, AccChart.class);
                    startActivity(intent);
                }
            }
        });
        //Buttons & Toggle Buttons

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Hardware
                    hw = new HardwareFactory(this);
                    stateMachineHandler=new StateMachineHandler(this);
                } else {
                    // Do Nothing
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
        idOfNavObj = item.getItemId();
        if (navigationBool) {
            navigationBool = false;
            onClickUI();
        }
            if (idOfNavObj == R.id.nav_acc) {
                headerTextView.setText(R.string.acce);
                navigationBool = true;
                onClickUI();
            } else if (idOfNavObj == R.id.nav_gyro) {
                headerTextView.setText(R.string.gyro);
                navigationBool = true;
                onClickUI();
            } else if (idOfNavObj == R.id.nav_magn) {
                headerTextView.setText(R.string.magn);
                navigationBool = true;
                onClickUI();
            } else if (idOfNavObj == R.id.nav_mic) {
                headerTextView.setText(R.string.mic);
                navigationBool = true;
                onClickUI();
            } else if (idOfNavObj == R.id.nav_gps) {
                headerTextView.setText(R.string.gps);
                navigationBool = true;
                onClickUI();
            } else if (idOfNavObj == R.id.nav_proximity) {
                headerTextView.setText(R.string.proximity);
                navigationBool = true;
                onClickUI();
            }else if (idOfNavObj == R.id.machinevision) {
                headerTextView.setText(R.string.machinevision);
                navigationBool = true;
                onClickMachineVision();
            }

            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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
                        if (idOfNavObj == R.id.nav_acc) {
                            textView5.setVisibility(View.INVISIBLE);
                            textView1.setText("X: " + String.format("%.2f", CurrentTickData.accX));
                            textView2.setText("Y: " + String.format("%.2f", CurrentTickData.accY));
                            textView3.setText("Z: " + String.format("%.2f", CurrentTickData.accZ));
                            textView4.setText("AccV: " + String.format("%.2f", CurrentTickData.accVecA));
                        } else if (idOfNavObj == R.id.nav_gps) {
                            textView1.setText("GPS Alt: " + String.format("%.2f", CurrentTickData.GPSalt));
                            textView2.setText("GPS Lat: " + String.format("%.6f", CurrentTickData.GPSlat));
                            textView3.setText("GPS Lon: "  + String.format("%.6f", CurrentTickData.GPSlon));
                            textView4.setText("GPS Bear: " + String.format("%.2f", CurrentTickData.GPSbearing));
                            textView5.setText("GPS Speed: " + String.format("%.2f", CurrentTickData.GPSspeed));
                            Log.d("GPS", "lat : " + CurrentTickData.GPSlat + "---" + "lon : " + CurrentTickData.GPSlon );
                        } else if (idOfNavObj == R.id.nav_gyro){
                            textView4.setVisibility(View.INVISIBLE);
                            textView5.setVisibility(View.INVISIBLE);
                            textView1.setText("Rot. X: " + String.format("%.2f", CurrentTickData.rotationX));
                            textView2.setText("Rot. Y: " + String.format("%.2f", CurrentTickData.rotationY));
                            textView3.setText("Rot. Z: " + String.format("%.2f", CurrentTickData.rotationZ));
                            // Log.d("GPS", "lat : " + CurrentTickData. + "---" + "lon : " + CurrentTickData. );
                        } else if (idOfNavObj == R.id.nav_mic) {
                            textView2.setVisibility(View.INVISIBLE);
                            textView3.setVisibility(View.INVISIBLE);
                            textView4.setVisibility(View.INVISIBLE);
                            textView5.setVisibility(View.INVISIBLE);
                            textView1.setText("Max Aplitude : " + CurrentTickData.micMaxAmpl);

                        } else if (idOfNavObj == R.id.nav_magn){
                            textView4.setVisibility(View.INVISIBLE);
                            textView5.setVisibility(View.INVISIBLE);
                            textView1.setText("Magn. X: " + String.format("%.2f", CurrentTickData.magneticX));
                            textView2.setText("Magn. Y: " + String.format("%.2f", CurrentTickData.magneticY));
                            textView3.setText("Magn. Z: " + String.format("%.2f", CurrentTickData.magneticZ));
                            // Log.d("GPS", "lat : " + CurrentTickData. + "---" + "lon : " + CurrentTickData. );
                        }else if (idOfNavObj == R.id.nav_proximity) {
                            textView2.setVisibility(View.INVISIBLE);
                            textView3.setVisibility(View.INVISIBLE);
                            textView4.setVisibility(View.INVISIBLE);
                            textView5.setVisibility(View.INVISIBLE);
                            textView1.setText("Proximity: " + CurrentTickData.proxState);
                        }


                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // Do Nothing
                }
            }
        }
    });

    /** TODO SEPARATE INTO OWN INTERFACE SoC
     *  onClick Handlers for different Sensors + UI (later non textual but graphical )
     */
    public void onClickUI(){
        if(navigationBool) {
            //Thread
            if (t.isAlive()){
                //Do Nothing
            }
            else{
                t.start();}
            // UI
            show_hideUI();

        } else {
            //UI
            headerTextView.setText(defaultMessage);
            show_hideUI();

        }
    }
    public void onClickMachineVision(){
        if(navigationBool) {
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            textView3.setVisibility(View.INVISIBLE);
            textView4.setVisibility(View.INVISIBLE);
            textView5.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, MachineVision.class);
            startActivity(intent);
            // UI

        } else {
            //UI
            headerTextView.setText(defaultMessage);


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
        if(textView1.getVisibility() == View.VISIBLE){
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            textView3.setVisibility(View.INVISIBLE);
            textView4.setVisibility(View.INVISIBLE);
            textView5.setVisibility(View.INVISIBLE);
        }else if(textView1.getVisibility() == View.INVISIBLE){
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.VISIBLE);
            textView4.setVisibility(View.VISIBLE);
            textView5.setVisibility(View.VISIBLE);

        }
    }

}
