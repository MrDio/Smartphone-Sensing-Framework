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

package edu.example.ssf.mma.timer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;

import edu.example.ssf.mma.config.ConfigApp;
import edu.example.ssf.mma.data.CsvFileWriter;
import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.HardwareFactory;
import edu.example.ssf.mma.hardwareAdapter.IAccelerometer;
import edu.example.ssf.mma.hardwareAdapter.IGPS;
import edu.example.ssf.mma.hardwareAdapter.IGyroscope;
import edu.example.ssf.mma.hardwareAdapter.IMagneto;
import edu.example.ssf.mma.hardwareAdapter.IMicrophone;
import edu.example.ssf.mma.hardwareAdapter.IProximity;
import edu.example.ssf.mma.userInterface.MainActivity;


/**
 * 
 * This class provides functions to control the state machine of the application, like
 * startStateMachine and stopStateMachine. Furthermore it extends the android.os.Handler class,
 * and implements the message queue endpoint to the statemachine-timer-task. 
 *
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 */

public class StateMachineHandler extends Handler{

	
	/**
     * References to the timer-task of the state machine, and further flags and configuration
     */
	private StateMachineTimerTask stateMachineTimerTask = null;
	
	/**Refernece to the timer*/
	private Timer timer = null;
	
	/**referencing the cycle time to the cicle time in the ConficApp class*/
	private Integer cycleTimeMs = ConfigApp.globalSampleTimeMs;
	
	/**referencing the delay time to the delay time in the ConficApp class*/
	private Integer delayTimeMs = ConfigApp.delayStateMachineTimerTaskTimeMs;
    
    /**setting the variable for initialising the task to true*/
    private boolean initTask = true;
	/** Interface to the hardware abstraction layer. */
	private IAccelerometer accelerometer = null;

	/** Interface to the hardware abstraction layer. */
	private IGPS gps = null;

	/** Interface to the hardware abstraction layer. */
	private IMicrophone microphone = null;

	/** Interface to the hardware abstraction layer. */
	private IProximity proximity = null;
	/** Interface to the hardware abstraction layer. */
	private IMagneto magneto = null;
	/** Interface to the hardware abstraction layer. */
	private IGyroscope gyroscope = null;


	public StateMachineHandler(Context context) {
		
		/**
		 * Create state-machine task and timer
		 */
		this.stateMachineTimerTask= new StateMachineTimerTask(this);
        this.timer=new Timer();
		/**
		 * Interface to the hardware abstraction layer
		 */
		this.accelerometer = HardwareFactory.getAccelerometer(context);
		this.gps = HardwareFactory.getGPS(context);
		this.microphone = HardwareFactory.getMicrophone(context);
		this.gyroscope = HardwareFactory.getGyroscope(context);
		this.magneto = HardwareFactory.getMagnetometer(context);
		this.proximity = HardwareFactory.getProximity(context);
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
	 * Stops the state machine
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

