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
package edu.example.ssf.mma.data;


/**
 * The Class CurrentTickData saves all values from the current tick, in order to write and display the same values from the the same tick.
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 */

public class CurrentTickData {
	//State machine data
	/**declaration of the current tick. */
	public static Integer curTick = 0;
	
	/** setting the current timestamp at the beginning to "N.N." */
	public static String curTimestamp = "N.N.";
	
	/** setting the accelerometer vector A to 0.0d in the beginning */
	public static Double accVecA = 0.0d;
	
	//Acceleration Sensor Data
	/** setting the x-value of the accelerometer to 1 in the beginning */
	public static Float accX=1.0f;
	
	/** setting the y-value of the accelerometer to 1 in the beginning */
	public static Float accY=1.0f;
	
	/** setting the z-value of the accelerometer to 1 in the beginning */
	public static Float accZ=1.0f;
	//Proximity Sensor State
	/** setting the boolean-value of the proximity Sensor to false in the beginning */
	public static boolean proxState= false;

	/** setting the current round to 0*/
	public static int round = 0;


	public static void resetValues(){
		curTick = 0;
		curTimestamp = "N.N.";
		accVecA = 0.0d;
		accX=1.0f;
		accY=1.0f;
		accZ=1.0f;
		proxState=false;
		round = 0;
	}
}
