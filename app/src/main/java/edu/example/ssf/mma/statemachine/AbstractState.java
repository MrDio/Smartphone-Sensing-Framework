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


// TODO: Auto-generated Javadoc
/**
 * This class is the parent class for all other State classes and defines the methods and attributes of a generic state. 
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public abstract class AbstractState {

	/** Declaration of the string used as a statename. */
	private String stateName;
	
	/** DEclaration of the parent state machine. */
	private IParentStateMachine parentStateMachine;
	
	
	/**
	 * Gets the parent state machine.
	 *
	 * @return the parentStateMachine
	 */
	public IParentStateMachine getParentStateMachine() {
		return parentStateMachine;
	}

	/**
	 * Sets the parent state machine.
	 *
	 * @param parentStateMachine callback reference to the IParentStateMachine
	 */
	public void setParentStateMachine(IParentStateMachine parentStateMachine) {
		this.parentStateMachine = parentStateMachine;
	}

	/**
	 * Instantiates a new abstract state.
	 *
	 * @param name the state name
	 * @param parentStateMachine callback-reference to the IParentStateMachine
	 */
	public AbstractState(String name, IParentStateMachine parentStateMachine) {
		this.stateName=name;
		this.parentStateMachine=parentStateMachine;
	}
	
	/**
	 * Gets the state name.
	 *
	 * @return the state name
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * Sets the state name.
	 *
	 * @param stateName the new state name
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	/**
	 * abstract method doit()
	 * is executed while being in a concrete state.
	 */
	public abstract void doit();
	
	/**
	 * abstract method entry()
	 * is executed while entering a concrete state.
	 */
	public abstract void entry();
	
	/**
	 * abstract method exit()
	 * is executed when leaving a concrete state.
	 */
	public abstract void exit();
}