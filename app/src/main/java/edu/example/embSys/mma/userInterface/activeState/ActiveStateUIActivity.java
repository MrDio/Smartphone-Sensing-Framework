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
    GNU v3 General Public License for more details.

    Released under GNU v3
    
    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.example.embSys.mma.userInterface.activeState;



import android.content.Context;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.example.embSys.mma.R;
import edu.example.embSys.mma.charts.ChartWriter;
import edu.example.embSys.mma.data.CsvFileWriter;
import edu.example.embSys.mma.hardwareAdapter.HardwareFactory;
import edu.example.embSys.mma.timer.StateMachineHandler;
import edu.example.embSys.mma.userInterface.IUserInterface;
import edu.example.embSys.mma.userInterface.buttonHandler.ButtonActStateHandler;
import edu.example.embSys.mma.userInterface.capturedState.CapturedStateUIActivity;
import edu.example.embSys.mma.userInterface.drawState.DrawStateUIListFragmentActivitiy;




// TODO: Auto-generated Javadoc
/**
 * The MainActivity class displays the current values of the Accelerometer, GPS and the Microphone Amplitude.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */


public class ActiveStateUIActivity extends FragmentActivity implements IUserInterface{
	
	/** Declaration of the attribute for the String that is used as the TAG, when working with LOGs. String is "ActiveStateUIActivity" */
	private final String TAG = "ActiveStateUIActivity";

	/**  Declaration of the attribute for the Fragment that is for the capture activity. */
	private Fragment capture;
	
	/**  Declaration of the attribute for the Fragment that is used for the draw activity. */
	private ListFragment draw;

	/**  Declaration of the attribute for the TextView, used to show the ActualState. */
	private TextView textViewActState;
	
	/**  Declaration of the attribute for the TextView, used to show the X-Value of the Accelerometer. */
	private TextView textViewAccX;
	
	/**  Declaration of the attribute for the TextView, used to show the y-Value of the Accelerometer. */
	private TextView textViewAccY;
	
	/**  Declaration of the attribute for the TextView, used to show the Z-Value of the Accelerometer. */
	private TextView textViewAccZ;
	
	/**  Declaration of the attribute for the TextView, used to show the current Altitude. */
	private TextView textViewGPSAlt;
	
	/**  Declaration of the attribute for the TextView, used to show the current Longitude. */
	private TextView textViewGPSLon;
	
	/**  Declaration of the attribute for the TextView, used to show the current Latitude. */
	private TextView textViewGPSLat;
	
	/**  Declaration of the attribute for the TextView, used to show the current maximal Microphone Amplitude. */
	private TextView textViewMicMaxAmpl;
	
	/**  Declaration of the attribute for the TextView, used to show gyro rotation around the X-axis. */
	private TextView textViewRotX;

	/**  Declaration of the attribute for the TextView, used to show gyro rotation around the Y-axis. */
	private TextView textViewRotY;
	
	/**  Declaration of the attribute for the TextView, used to show gyro rotation around the Z-axis. */
	private TextView textViewRotZ;

	/**  Declaration of the attribute for the TextView, used to show heartrate */
	private TextView textViewHeartRate;
	
	/**  Declaration of the Button used for changing between the activity's layout and the fragment's layout. */
	private Button changeButton; //TODO ButtonChangeHandler
	
	/**  Declaration of the Button used for starting and stopping the recording. */
	private Button recButton; //TODO ButtonRecHandler
	
	/** Declaration of the ButtonActStateHanlder used to handle the state of start or stop of the application. */
	private ButtonActStateHandler buttonActStateHandler;
	
	/** Declaration of the state machine. */
	private StateMachineHandler stateMachineHandler;
	
	/** Declaration of the chart writer. */
	private ChartWriter chartWriter;
	
	
	/**
	 * This method determines in what state the app is, when pressing the start button and changes the state to the opposite one.
	 *
	 * @param view this parameter is the view that was clicked	 */
	public void buttonActStateStartStopClicked(View view) {
		// Delegation Pattern
		this.buttonActStateHandler.buttonActStateClick();
		if (this.buttonActStateHandler.isStart()) {
			this.stateMachineHandler.startStateMachine();
			Log.d("buttonActStateStartStopClicked", "Start State Machine");
		} else if (this.buttonActStateHandler.isStop()) {
			this.stateMachineHandler.stopStateMachine();
			Log.d("buttonActStateStartStopClicked", "Stop State Machine");
		}
	}

	/**
	 * Is first called when the application starts and creates the Layout of the MainActivity. It also initzalize the ButtonActStateHandler,
	 * the HardwareFactory and the StateMachineHandler
	 *
	 * @param savedInstanceState If the activity is being re-initialized then this Bundle contains
	 *  the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
	 */
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        
	        Log.i(TAG,"activity_main.xml created");	        
	        
	        this.chartWriter = new ChartWriter();
	        
	        this.capture = new CapturedStateUIActivity(); 
	        this.draw = new DrawStateUIListFragmentActivitiy(this.chartWriter.getAllCharts());
	        
	        //attach start stop handler of the statemachines' button buttonActStateStartStop	
	        this.buttonActStateHandler=new ButtonActStateHandler((Button)findViewById(R.id.buttonActStateStartStop));
	        //TODO add button Handlers here, and give them the reference
	        
	        this.textViewActState=(TextView)findViewById(R.id.textViewActState);
	        this.textViewAccX=(TextView)findViewById(R.id.textViewAccX);
	        this.textViewAccY=(TextView)findViewById(R.id.textViewAccY);
	        this.textViewAccZ=(TextView)findViewById(R.id.textViewAccZ);
			
	        this.textViewGPSAlt=(TextView)findViewById(R.id.textViewGPSAlt);
			this.textViewGPSLat=(TextView)findViewById(R.id.textViewGPSLat);
			this.textViewGPSLon=(TextView)findViewById(R.id.textViewGPSLon);
	        
			this.textViewMicMaxAmpl=(TextView)findViewById(R.id.textViewMicMaxAmpl);
			
			this.textViewRotX=(TextView)findViewById(R.id.textViewRotX);
			this.textViewRotY=(TextView)findViewById(R.id.textViewRotY);
			this.textViewRotZ=(TextView)findViewById(R.id.textViewRotZ);
			
			this.textViewHeartRate=(TextView)findViewById(R.id.textViewHeartRate);

			
	        /**
	         * Factory Pattern
	         */
			HardwareFactory.initFactory();
	        HardwareFactory.setSensorManager((SensorManager) getSystemService(Context.SENSOR_SERVICE));
	        HardwareFactory.setLocationManager((LocationManager) getSystemService(Context.LOCATION_SERVICE));
	        HardwareFactory.setHeartrateContext(this);
	        
	        this.stateMachineHandler=new StateMachineHandler(this);
	    }	
	
	
	/**
	 * determines if the capture class will show up or not, when pressing the ChangeMode Button.
	 *
	 * @param v the view that has been clicked
	 */
	public void changeMode(View v) {
		
		 this.changeButton = (Button)findViewById(R.id.change);
         Log.i(TAG, "changeButton clicked");
		 
         FragmentManager fm = getSupportFragmentManager();
		 FragmentTransaction ft = fm.beginTransaction();

		if(capture.isHidden())
		{		
			Log.i(TAG, "show Capture_Fragment");	
			ft.show(capture);
			ft.replace(R.id.frame, capture);
			changeButton.setText("Draw States ->");
		}else if(draw.isHidden()){
			Log.i(TAG, "show Draw_Fragment");	
			ft.show(draw);
			ft.replace(R.id.frame, draw);
			changeButton.setText("<- Actual State");
		}
		else{
			Log.i(TAG, "hide Capture_Fragment");
			ft.hide(capture);
			ft.hide(draw);
			changeButton.setText("Captured States ->");
		}
	
		ft.addToBackStack(null);
		ft.commit();
	}
	
	/**
	 * determines if the recording of the data will start or stop when pressing the REC Button.
	 *
	 * @param view the view that has been clicked
	 */

	public void recording (View view) {
		
		this.recButton = (Button)findViewById(R.id.REC);
		Log.i(TAG,"Recording Button clicked");
		
		String record="REC";			
		String recording="REC...";
		//String recState = record;
		String recState = recButton.getText().toString();
		
		//listener = new MySensorListener();
		
		if(recState.equals(record))
		{
			CsvFileWriter.crtFile();
			Toast.makeText(this, "Record start", Toast.LENGTH_SHORT).show();
			recState = recording;
		}
		else if(recState.equals(recording)){
			CsvFileWriter.closeFile();
			Log.i(TAG, "File closed");
			recState = record;
			Toast.makeText(this, "Record stop", Toast.LENGTH_SHORT).show();
		}
		else {
		Log.d("ERROR", "Undefined State reached!");
		}
		recButton.setText(recState);	
	}

	
	/**
	 * Setter for the Actual State.
	 *
	 * @param actState String of the Actual State
	 */
	@Override
	public void setActState(String actState) {
		this.textViewActState.setText(actState);
	}

	/**
	 * Setter for the Actual X-Value of the Accelerometer.
	 *
	 * @param accx String of the current Accelerometer X-Value
	 */
	@Override
	public void setAccX(String accx) {
		this.textViewAccX.setText(accx);
	}

	
	/**
	 * Setter for the Actual Y-Value of the Accelerometer.
	 *
	 * @param accy String of the current Accelerometer Y-Value
	 */
	@Override
	public void setAccY(String accy) {
		this.textViewAccY.setText(accy);
	}

	
	/**
	 * Setter for the Actual Z-Value of the Accelerometer.
	 *
	 * @param accz String of the current Accelerometer Z-Value
	 */
	@Override
	public void setAccZ(String accz) {
		this.textViewAccZ.setText(accz);
	}

	/**
	 * Setter for the Actual Altitude Value of the GPS.
	 *
	 * @param gpsAlt String of the current Altitude GPS Position
	 */
	@Override
	public void setGPSAlt(String gpsAlt) {
		this.textViewGPSAlt.setText(gpsAlt);
	}

	/**
	 * Setter for the Actual Longitude Value of the GPS.
	 *
	 * @param gpsLon String of the current Longitude GPS Position
	 */
	@Override
	public void setGPSLon(String gpsLon) {
		this.textViewGPSLon.setText(gpsLon);
	}

	/**
	 * Setter for the Actual Latitude Value of the GPS.
	 *
	 * @param gpsLat String of the current Latitude GPS Position
	 */
	@Override
	public void setGPSLat(String gpsLat) {
		this.textViewGPSLat.setText(gpsLat);
	}

	/**
	 * Setter for the Actual max Amplitude of the Microphone.
	 *
	 * @param micMaxAmpl String of the current max Microphone Amplitude
	 */
	@Override
	public void setMicMaxAmpl(String micMaxAmpl) {
		this.textViewMicMaxAmpl.setText(micMaxAmpl);
	}

	/**
	 * Callback from the statemachine to update the chart data.
	 */
	@Override
	public void updateCharts() {
		this.chartWriter.updateAllCharts();
	}

	/**
	 * Sets the rotation around the X-Axis
	 *
	 * @param rotX the rotation fetched from the gyro
	 */
	@Override
	public void setRotationX(String rotX) {
		this.textViewRotX.setText(rotX);
		
	}

	/**
	 * Sets the rotation around the Y-Axis
	 *
	 * @param rotY the rotation fetched from the gyro
	 */
	@Override
	public void setRotationY(String rotY) {
		this.textViewRotY.setText(rotY);
	}

	/**
	 * Sets the rotation around the Z-Axis
	 *
	 * @param rotZ the rotation fetched from the gyro
	 */
	@Override
	public void setRotationZ(String rotZ) {
		this.textViewRotZ.setText(rotZ);
	}
	
	/**
	 * Sets the Heartbeat rate
	 *
	 * @param rate from the Heartbeat sensor
	 */
	public void setHeartrate(String heartrate){
		this.textViewHeartRate.setText(heartrate);
		
	}
}
