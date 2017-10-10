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

package edu.example.embSys.mma.timer;

import java.util.Timer;

import edu.example.embSys.mma.config.ConfigApp;
import edu.example.embSys.mma.data.CsvFileWriter;
import edu.example.embSys.mma.data.CurrentTickData;
import edu.example.embSys.mma.hardwareAdapter.HardwareFactory;
import edu.example.embSys.mma.hardwareAdapter.IAccelerometer;
import edu.example.embSys.mma.hardwareAdapter.IGPS;
import edu.example.embSys.mma.hardwareAdapter.IGyroscope;
import edu.example.embSys.mma.hardwareAdapter.IHeartrate;
import edu.example.embSys.mma.hardwareAdapter.IMicrophone;
import edu.example.embSys.mma.userInterface.IUserInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

// TODO: Auto-generated Javadoc
/**
 * 
 * This class provides functions to control the state machine of the application, like
 * startStateMachine and stopStateMachine. Furthermore it extends the android.os.Handler class,
 * and implements the message queue endpoint to the statemachine-timer-task. 
 * 
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class StateMachineHandler extends Handler{
	
	/** Interface to the user-interface layer. */
	private IUserInterface userInterface = null;
	
	/** References to the timer-task of the state machine, and further flags and configuration. */
	private StateMachineTimerTask stateMachineTimerTask = null;
	
	/** Refernece to the timer. */
	private Timer timer = null;
	
	/** referencing the cycle time to the cicle time in the ConficApp class. */
	private Integer cycleTimeMs = ConfigApp.globalSampleTimeMs;
	
	/** referencing the delay time to the delay time in the ConficApp class. */
	private Integer delayTimeMs = ConfigApp.delayStateMachineTimerTaskTimeMs;
    
    /** setting the variable for initialising the task to true. */
    private boolean initTask = true;
	
    /** Interface to the hardware abstraction layer. */
    private IAccelerometer accelerometer = null;
    
    /** Interface to the hardware abstraction layer. */
    private IGPS gps = null;
    
    /** Interface to the hardware abstraction layer. */
    private IMicrophone microphone = null;
    
    /** Interface to the hardware abstraction layer. */
	private IGyroscope gyroscope = null;
	
	/** Interface to the hardware abstraction layer. */
	private IHeartrate heartrate = null;	
	
	/**
	 * This is the constructor of the state-machines handler. Please use this 
	 * handler to control the state-machine.
	 * @param userInterface Interface to the user-interface layer
	 */
	public StateMachineHandler(IUserInterface userInterface) {
		/**
		 * Interface to the user-interface layer
		 */
		this.userInterface=userInterface;
		
		/**
		 * Create state-machine task and timer
		 */
		this.stateMachineTimerTask= new StateMachineTimerTask(this);
        this.timer=new Timer();
        
        /**
         * Interface to the hardware abstraction layer
         */
        this.accelerometer = HardwareFactory.getAccelerometer();
        this.gps = HardwareFactory.getGPS();
        this.microphone = HardwareFactory.getMicrophone();
        this.gyroscope = HardwareFactory.getGyroscope();
        this.heartrate = HardwareFactory.getHeartbeat();

	}
	

	/* (non-Javadoc)
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
    	/**
    	 * Fetch data from the state-machine
    	 */
		//Fresh data from the state-machine has arrived. Get data from bundle
		Bundle bundle = msg.getData();
		
		//Unpack data from bundle by using message tags defined in StateMachineTimerTask
    	CurrentTickData.actState = bundle.getString(StateMachineTimerTask.actStateMsgTag);
    	CurrentTickData.curTimestamp = bundle.getString(StateMachineTimerTask.curTimestampMsgTag);
    	CurrentTickData.curTick = bundle.getInt(StateMachineTimerTask.curTickMsgTag);
    	
    	/**
    	 * Fetch data from the sensors
    	 * Attention! Some sensor getter-function triggers 
    	 * underlying drivers to update the sensor value!
    	 * Fetch the sensor data just one time per tick! 
    	 * It's better to store data temporary at first.  
    	 */
    	//Fetch data from acceleration sensor
    	CurrentTickData.accX=this.accelerometer.getAccX();
    	CurrentTickData.accY=this.accelerometer.getAccY();
    	CurrentTickData.accZ=this.accelerometer.getAccZ();
    	CurrentTickData.accVecA=this.accelerometer.getAccA();
    	
    	//Fetch data from GPS-Sensor
    	CurrentTickData.GPSalt=this.gps.getAltitude();
    	CurrentTickData.GPSlon=this.gps.getLongitude();
    	CurrentTickData.GPSlat=this.gps.getLatitude();
    	CurrentTickData.GPSbearing=this.gps.getBearing();
    	CurrentTickData.GPSspeed=this.gps.getSpeed();
    	
    	//Fetch data from microphone
    	CurrentTickData.micMaxAmpl=this.microphone.getMaxAmplitude();
    	
    	//Fetch data from gyros
    	CurrentTickData.rotationX=this.gyroscope.getRotationX();
    	CurrentTickData.rotationY=this.gyroscope.getRotationY();
    	CurrentTickData.rotationZ=this.gyroscope.getRotationZ();
    	
        //Fetch data from Heartrat-Sensor
        CurrentTickData.heartrate=this.heartrate.getValue();
        
    	/**
    	 * Update user-interface (the State, accelerometer-sensor, GPS-sensor, microphone)
    	 */
    	//Update user-interface State
    	this.userInterface.setActState(CurrentTickData.actState);
    	
    	//Update user-interface acceleration-sensor
    	this.userInterface.setAccX(CurrentTickData.accX.toString());
    	this.userInterface.setAccY(CurrentTickData.accY.toString());
    	this.userInterface.setAccZ(CurrentTickData.accZ.toString());
    	
    	//Update user-interface GPS-sensor
    	this.userInterface.setGPSAlt(CurrentTickData.GPSalt.toString());
    	this.userInterface.setGPSLon(CurrentTickData.GPSlon.toString());
    	this.userInterface.setGPSLat(CurrentTickData.GPSlat.toString());
    	
    	//Update user-interface microphone
    	this.userInterface.setMicMaxAmpl(CurrentTickData.micMaxAmpl.toString());
    	
    	//Update user-interface GYRO
    	this.userInterface.setRotationX(CurrentTickData.rotationX.toString());
    	this.userInterface.setRotationY(CurrentTickData.rotationY.toString());
    	this.userInterface.setRotationZ(CurrentTickData.rotationZ.toString());
    	
        //Update user-interface HeartRate
        this.userInterface.setHeartrate(CurrentTickData.heartrate.toString());
        
    	/**
    	 * Update Chart data
    	 */
    	this.userInterface.updateCharts();
    	
    	/**
    	 * Write line to csv-file for current tick
    	 */
    	CsvFileWriter.writeLine(
    			CurrentTickData.curTick.toString(),
    			CurrentTickData.curTimestamp.toString(),
    			CurrentTickData.accX.toString(),
    			CurrentTickData.accY.toString(),
    			CurrentTickData.accZ.toString(),
    			CurrentTickData.GPSalt.toString(),
    			CurrentTickData.GPSlon.toString(),
    			CurrentTickData.GPSlat.toString(),
    			CurrentTickData.micMaxAmpl.toString(),
    			CurrentTickData.accVecA.toString(),
    			CurrentTickData.rotationX.toString(),
    			CurrentTickData.rotationY.toString(),
    			CurrentTickData.rotationZ.toString(),
    		    CurrentTickData.heartrate.toString());
    	
    	//Call daddy and say everything is ok, by forwarding received message
    	super.handleMessage(msg);
    }
	
	/**
	 * Initialises (just once) and starts the state-machine.
	 */
	public void startStateMachine(){
		this.stateMachineTimerTask.startStateMachine();
		if (initTask) {
        	this.timer.schedule(this.stateMachineTimerTask, this.delayTimeMs, this.cycleTimeMs);
        	this.initTask=false;
		}
		this.stateMachineTimerTask.startStateMachine();
   	}
	
	/**
	 * Stops the state machine.
	 */
	public void stopStateMachine(){
		this.stateMachineTimerTask.stopStateMachine();
	}
	
	/**
	 * Under Construction: This method allows to change the cycle-time of the state-machine in run-time.
	 * @param cycleTimeMs The cycle time to change 
	 */
	void changeCycleTime(Integer cycleTimeMs){
		this.cycleTimeMs=cycleTimeMs;
		this.stopStateMachine();
		this.startStateMachine();
	}
}

