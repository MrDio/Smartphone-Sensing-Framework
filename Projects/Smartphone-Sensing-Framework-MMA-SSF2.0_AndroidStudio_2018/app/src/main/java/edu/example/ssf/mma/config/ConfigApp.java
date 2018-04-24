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

package edu.example.ssf.mma.config;


/**
 * This class contains the application configuration like the sensor-sampling-time, 
 * the output-folder for the captured data (same folder at the host-PC and at target-Smartphone), 
 * the Simulation-Mode-flag etc..
 *
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 */
public class ConfigApp {

	/**
	 * This is the global sample time of the applications' state-machine in ms.
	 */
	public static Integer globalSampleTimeMs = 1;

	/** The delay of state machine timer task time in ms. */
	public static Integer delayStateMachineTimerTaskTimeMs = 10;

	/** Switch to simulation- or to real-mode. */
	/** real-mode == false */
	public static boolean isSimulation = false;

	/** The string of the target storage direction. */
	public static String targetStorageDir = "/RapidDisruption/Data";
	
	/** The string for the csv reader stimuli. */
	public static String csvReaderStimuli = "S01.csv";

}
