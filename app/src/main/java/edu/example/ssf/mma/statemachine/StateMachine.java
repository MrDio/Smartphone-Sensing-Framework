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
package edu.example.ssf.mma.statemachine;

import android.util.Log;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.data.MathCalculations;


// TODO: Auto-generated Javadoc
/**
 * Interprets the accelerometer-data and determines in which state the user is at the moment.
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class StateMachine implements IStateMachine, IParentStateMachine{

	/**
	 * state of the state machine when not in the state "WALKING" or "DRIVING". Also the state the state machine is in at the start.
	 */
	private AbstractState unknown = null;
	
	/**state of the state machine when not in the state "UNKNOWN" or "DRIVING".*/
	private AbstractState walking = null;
	
	/**state of the state machine when not in the state "UNKNOWN" or "WALKING".*/
	private AbstractState driving = null;
	
	/** setting the abstract state in the beginning to null. */
	private AbstractState actState = null;
	
	/** setting the next state in the beginning to null. */
	private AbstractState nextState = null;
	
	/**setting the label of the state in the beginning to "N.N."*/
	private String stateLabel = "N.N.";
	
	/**
	 * Instantiates a new state machine.
	 */
	public StateMachine() {
		this.unknown=new StateUnknown(this);
		this.walking=new StateWalking(this);
		this.driving=new StateDriving(this);
		
		this.actState=this.unknown;
		this.nextState=this.unknown;
	}
	
	
	/**
	 * This method checks if a transition change has occurred and if the state label has to be changed.
	 */
	
	@Override
	public void transisionCheck() {
		/**
		 * Calculate absolute value of the acceleration vector a
		 */
		CurrentTickData.accVecA= MathCalculations.calculatePythagoras(CurrentTickData.accX,
				CurrentTickData.accY,CurrentTickData.accZ);
		
//		if (this.actState instanceof StateUnknown) {
//			Log.d("STATE_MACHINE", "UNKNOWN");
//			if (CurrentTickData.accVecA >= 9.81-1.91 && CurrentTickData.accVecA <= 9.81+0.69) {
//				this.nextState=this.driving;
//			}else if (CurrentTickData.accVecA > 9.81-4.31 && CurrentTickData.accVecA <= 9.81+5.19 ) {
//				this.nextState=this.walking;
//			}else {
//				this.nextState=this.unknown;
//			}
//		}else if (this.actState instanceof StateDriving) {
//			Log.d("STATE_MACHINE", "DRIVING");
//			/*PushToLosant ptl = new PushToLosant();
//			ptl.pushtoLosant();*/
//			if (CurrentTickData.accVecA >= 9.81-1.91 && CurrentTickData.accVecA <= 9.81+0.69) {
//				this.nextState=this.driving;
//			}else if (CurrentTickData.accVecA > 9.81-4.31 && CurrentTickData.accVecA <= 9.81+5.19 ) {
//				this.nextState=this.walking;
//			}else {
//				this.nextState=this.unknown;
//			}
//		}else if (this.actState instanceof StateWalking) {
//			Log.d("STATE_MACHINE", "WALKING");
//			if (CurrentTickData.accVecA >= 9.81-1.91 && CurrentTickData.accVecA <= 9.81+0.69) {
//				this.nextState=this.driving;
//			}else if (CurrentTickData.accVecA > 9.81-4.31 && CurrentTickData.accVecA <= 9.81+5.19 ) {
//				this.nextState=this.walking;
//			}else {
//				this.nextState=this.unknown;
//			}
//		}
	}

	/**
	 * changes the label text to another state.
	 */
	@Override
	public void stateCheck() {
		this.actState.doit();
		if (this.actState != this.nextState) {
			this.actState.exit();
			this.nextState.entry();
		}
		this.actState=this.nextState;
		this.stateLabel=this.nextState.getStateName();
	}

	
	/**
	 *  gets the state label
	 *  
	 *  @return returns the statelabel
	 */
	@Override
	public String getStateLabel() {
		return this.stateLabel;
	}

	/**
	 * sets the state label.
	 *
	 * @param label uesd String to set StateLabel to "TEST"
	 */
	@Override
	public void setStateLabel(String label) {
		this.stateLabel=label;
	}

	/**
	 * Instantiates the actual state, the next state and the state label.
	 */
	@Override
	public void initStateMachine() {
		this.actState=this.unknown;
		this.nextState=this.unknown;
		this.stateLabel=this.unknown.getStateName();
	}
}

