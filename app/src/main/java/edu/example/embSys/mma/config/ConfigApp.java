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

package edu.example.embSys.mma.config;


// TODO: Auto-generated Javadoc
/**
 * This class contains the application configuration like the sensor-sampling-time, 
 * the output-folder for the captured data (same folder at the host-PC and at target-Smartphone), 
 * the Simulation-Mode-flag etc..
 * 
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */ 
public class ConfigApp {
	
	/**
	 * This is the global sample time of the applications' state-machine in ms.
	 */
	public static Integer globalSampleTimeMs = 100;
	
	/** The email default email address to send the captured csv-data. */
	public static String emailAddress = "no@replay.de";

	/** Switch to simulation- or to real-mode. */
	public static boolean isSimulation = false;
	
	/** The delay of state machine timer task time in ms. */
	public static Integer delayStateMachineTimerTaskTimeMs = 10;
	
	/** The string of the target storage direction. */
	public static String targetStorageDir = "StorageDir/";
	
	/** The string for the csv reader stimuli. */
	public static String csvReaderStimuli = "Sample-Data.csv"; //"Sample-Data.csv";
}
