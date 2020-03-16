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

import android.os.Bundle;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

import edu.example.ssf.mma.statemachine.IStateMachine;
import edu.example.ssf.mma.statemachine.StateMachine;


// TODO: Auto-generated Javadoc

/**
 * This class is checks every tick if there was a transition or state change and also increases the count variable by one. 
 * It also sets the runStateMachine Variable either to true or to false.
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 */

public class StateMachineTimerTask extends TimerTask{


    //Flag for state-machine start-stop-control
	/** Flag for the state machine start-stop-control */
    private Boolean runStateMachine = Boolean.TRUE;
    
    //Callback-reference to the handler
    /** Callback-reference to the handler */
    private StateMachineHandler stateMachineHandler = null;
    
    //The format of the timestamp
    /** The format of the timestamp */
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("hh:mm:ss:SSS", Locale.GERMAN);
    
    /** Message queue tags for the communication with the handler. */
    public static final String actStateMsgTag = "ACT_STATE";
    
    /** The Constant curTickMsgTag. */
    public static final String curTickMsgTag = "CUR_TICK";
    
    /** The Constant curTimestampMsgTag. */
    public static final String curTimestampMsgTag = "CUR_TIMESTAMP";

	//Interface to the state machine
	/**  Interface to the state machine. */
	private IStateMachine stateMachine = null;


	/** Current tick. */
    private Integer cnt=0;
    
    /**
     * This is the constructor of the state-machines timer-task.
     *
     * @param stateMachineHandler Callback-reference to the handler
     */
    public StateMachineTimerTask(StateMachineHandler stateMachineHandler) {
    	this.stateMachineHandler=stateMachineHandler;
		this.stateMachine=new StateMachine();
	}
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		if (!this.runStateMachine) {
			this.stateMachine.initStateMachine();
			cnt=0;
			return;
		}

		this.stateMachine.transisionCheck();
		this.stateMachine.stateCheck();

		cnt++;

		Message msg = new Message();
		Bundle bundle = new Bundle();

		bundle.putString(actStateMsgTag, this.stateMachine.getStateLabel());
		bundle.putInt(curTickMsgTag, this.cnt);
		bundle.putString(curTimestampMsgTag, this.timestampFormat.format(new Date()));

		msg.setData(bundle);
		
		this.stateMachineHandler.sendMessage(msg); // (2)

		//Log.d("Timestamp", bundle.getString(curTimestampMsgTag));
		//Log.d("RUN", cnt.toString());
	}

	/**
	 * sets the runStateMachine variable to true, when the StateMachine starts.
	 */
	public void startStateMachine() {
		this.runStateMachine=Boolean.TRUE;
	}

	/**
	 * sets the runStateMachine variable to false, when the StateMachine stops.
	 */
	public void stopStateMachine() {
		this.runStateMachine=Boolean.FALSE;
	}


}