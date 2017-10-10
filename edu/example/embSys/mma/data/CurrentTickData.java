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
package edu.example.embSys.mma.data;

// TODO: Auto-generated Javadoc
/**
 * The Class CurrentTickData saves all values from the current tick, in order to write and display the same values from the the same tick.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class CurrentTickData {
	
	//State machine data
	/**declaration of the current tick. */
	public static Integer curTick = 0;
	
	/** setting the current timestamp at the beginning to "N.N." */
	public static String curTimestamp = "N.N.";
	
	/**setting the actState in the beginning to "N.N.". */
	public static String actState = "N.N.";
	
	/** setting the accelerometer vector A to 0.0d in the beginning */
	public static Double accVecA = 0.0d;
	
	//Acceleration Sensor Data
	/**  setting the x-value of the accelerometer to 1 in the beginning. */
	public static Float accX=1.0f;
	
	/**  setting the y-value of the accelerometer to 1 in the beginning. */
	public static Float accY=1.0f;
	
	/**  setting the z-value of the accelerometer to 1 in the beginning. */
	public static Float accZ=1.0f;
	
	//GPS Sensor Data
	/**  setting the microphone altitude to 0 in the beginning. */
	public static Double GPSalt = 0.0d;
	
	/**  setting the latitude to 0 in the beginning. */
	public static Double GPSlat = 0.0d;
	
	/**  setting the longitude to 0 in the beginning. */
	public static Double GPSlon = 0.0d;
	
	/**  setting the bearing to 0 in the beginning. */
	public static Float GPSbearing = 0.0f;
	
	/**  setting the speed to 0 in the beginning. */
	public static Float GPSspeed = 0.0f;

	//Microphone Sensor Data
	/**  setting the microphone max amplitude to 0 in the beginning. */
	public static Integer micMaxAmpl = 0;
	
	//Acceleration Sensor Data
	/** setting the x-value of the gyroscope to 1 in the beginning */
	public static Float rotationX=1.0f;
	
	/** setting the y-value of the gyroscope to 1 in the beginning */
	public static Float rotationY=1.0f;
	
	/** setting the z-value of the gyroscope to 1 in the beginning */
	public static Float rotationZ=1.0f;

	/** setting the heartrate value to zero in the beginning*/
	public static Integer heartrate=0;

}
