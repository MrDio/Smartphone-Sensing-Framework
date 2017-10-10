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
package edu.example.embSys.mma.hardwareAdapter;

import android.content.Context;
/**
 * Interface class to instantiate the the Bluetooth and Zephyr-Heartrate-Sensor and get the heartrate in bpm
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */
public interface IHeartrate {
		
	/**
	 	Implementiert Hardware Sensor
	 */
	
	public void initHeartBeatSensor(Context context);
	
	/**
	 * 
	 * @return the Heartrate value
	 * 
	 */
	public int getValue();
	
	
	/**
	 * 
	 * @return Returns a State of the connection between application and the heartrate sensor
	 * 
	 * */
	public boolean getConnectedState();
	
	/**
	 * 
	 * Can be called to close all open listeners
	 * 
	 * */
	public void finish();
	
	/**
	 * 
	 * @return Can checks if finish completed called
	 * 
	 * */
	public boolean getFinished();

}
