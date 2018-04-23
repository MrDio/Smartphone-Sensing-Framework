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
package edu.example.ssf.mma.hardwareAdapter;


import android.content.Context;
import android.util.Log;

import edu.example.ssf.mma.hardwareAdapter.accelerometer.accelerometer;
import edu.example.ssf.mma.hardwareAdapter.lightsensor.LightSensor;
import edu.example.ssf.mma.hardwareAdapter.proximity.proximity;


// TODO: Auto-generated Javadoc

/**
 * * Initialises the hardware components and checks if either a simulated or the sensor of the device should be used.
 * A factory for creating Hardware objects.
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 */

public class HardwareFactory {


	/** setting the simulated accelerometer to null. */
	public static IAccelerometer hwAcc = null;
	public static ILightSensor hwLight = null;
	public static IProximity hwProxi = null;

	
	/**
	 * decides if in case of simulated sensors get the data from the CSV file or if using the sensors of the device from the seonsrs itself. 
	 */

	public HardwareFactory(Context context){

		getAccelerometer(context);
		Log.d("init?","acc initialized");

		getLight(context);
		Log.d("init?","light initialized");

		getProximity(context);
		Log.d("init?","proxi initialized");

	}


	/**
	 * determines if to uses the devices accelerometer or simulate a accelerometer
	 * 
	 * @return either the simulated or the devices accelerometer 
	 */
	public static IAccelerometer getAccelerometer(Context context) {

		hwAcc = new accelerometer(context);

		return hwAcc;
	}


	/**
	 * determines if to uses the devices proximity sensor or simulate a proximity sensor
	 *
	 * @return either the simulated or the devices proximity sensor
	 */
	public static IProximity getProximity(Context context) {

		hwProxi = new proximity(context);

		return hwProxi;
	}


	/**
	 * determines if to use the devices light sensor or simulate a light sensor
	 *
	 * @return either the simulated or the devices light sensor
	 */
	public static ILightSensor getLight(Context context) {

		hwLight = new LightSensor(context);

		return hwLight;
	}


}