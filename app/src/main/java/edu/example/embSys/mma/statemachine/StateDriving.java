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
package edu.example.embSys.mma.statemachine;

import edu.example.embSys.mma.statemachine.AbstractState;
import edu.example.embSys.mma.statemachine.IParentStateMachine;


// TODO: Auto-generated Javadoc
/**
 * This class extends the AbstractState Class and sets the stateLabel to "DRIVING".
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class StateDriving extends AbstractState{
	
	/**
	 * Instantiates a new state driving.
	 *
	  * @param stateMachine Callback-reference to the IParentStateMachine
	 */
	public StateDriving(IParentStateMachine stateMachine) {
		super("DRIVING", stateMachine);
	}
	
	/**
	 * Override-method.
	 */
	@Override
	public void doit() {
		getParentStateMachine().setStateLabel(this.getStateName());
	}

	/**
	 *  auto-generated method.
	 */
	@Override
	public void entry() {
		// TODO Auto-generated method stub
		
	}

	/**
	 *  auto-generated method.
	 */
	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}

}