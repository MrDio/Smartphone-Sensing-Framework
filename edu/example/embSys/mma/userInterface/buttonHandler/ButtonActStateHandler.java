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
package edu.example.embSys.mma.userInterface.buttonHandler;

import android.util.Log;
import android.widget.Button;


// TODO: Auto-generated Javadoc
/**
 * handles the text that is displayed in the button and changes it either to start or to stop.
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class ButtonActStateHandler {

	
	/**  Declaration of the String that dislays stopped State of the Sensors. */
	private final String startStatemachine="Stop";			
	
	/**  Declaration of the String that is dislays the started state of the Sensors. */
	private final String stopStatemachine="Start";
	
	/** Declaration of the String  that is dislayed in the button when the app is first started. actState can be start or stop depending on the 
	 * current state*/
	private String actState = stopStatemachine;
	
	/**  A Reference to the handled button. */
	private Button button = null;
	
	
	/**
	 * Instantiates a new button act state handler.
	 *
	 * @param button button who handles the starting stoping of the statemachine
	 */
	public ButtonActStateHandler(Button button) {
		this.button=button;
	}
	
	/**
	 * Sets the button.
	 *
	 * @param button button who handles the starting stoping of the statemachine
	 */
	public void setButton(Button button) {
		this.button = button;
	}

	
	/**
	 * Button act state click method, that is called when the button is clicked and changes the text in the button itself.
	 * 
	*/
	public void buttonActStateClick(){
		if (this.actState.equals(startStatemachine)) {
			this.actState=stopStatemachine;
			Log.d("ButtonActStateHandler", "actState==STOP_SM");
		}
		else if (this.actState.equals(stopStatemachine)) {
			this.actState=startStatemachine;
			
			Log.d("ButtonActStateHandler", "actState==START_SM");
		}else {
			Log.d("ERROR", "Undefined State reached!");
		}
		this.button.setText(this.actState);
	}

	
	/**
	 * Checks if is start.
	 *
	 * @return true, if is start
	 */
	public boolean isStart() {
		return this.actState.equals(this.startStatemachine);
	}
	
	
	/**
	 * Checks if is stop.
	 *
	 * @return true, if is stop
	 */
	public boolean isStop() {
		return this.actState.equals(this.stopStatemachine);
	}
}