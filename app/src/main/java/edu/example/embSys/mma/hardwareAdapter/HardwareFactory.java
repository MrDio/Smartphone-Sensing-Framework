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
import android.hardware.SensorManager;
import android.location.LocationManager;
import edu.example.embSys.mma.config.ConfigApp;
import edu.example.embSys.mma.data.CrtTargetDir;
import edu.example.embSys.mma.data.CsvFileReader;
import edu.example.embSys.mma.hardwareAdapter.GPS.GPSHardware;
import edu.example.embSys.mma.hardwareAdapter.GPS.GPSSim;
import edu.example.embSys.mma.hardwareAdapter.Heartrate.HeartrateHardware;
import edu.example.embSys.mma.hardwareAdapter.Heartrate.HeartrateSim;
import edu.example.embSys.mma.hardwareAdapter.accelerometer.AccelerometerHardware;
import edu.example.embSys.mma.hardwareAdapter.accelerometer.AccelerometerSim;
import edu.example.embSys.mma.hardwareAdapter.gyroscope.GyroscopeHardware;
import edu.example.embSys.mma.hardwareAdapter.gyroscope.GyroscopeSim;
import edu.example.embSys.mma.hardwareAdapter.microphone.MicrophoneHardware;
import edu.example.embSys.mma.hardwareAdapter.microphone.MicrophoneSim;

// TODO: Auto-generated Javadoc
/**
 * * Initialises the hardware components and checks if either a simulated or the sensor of the device should be used.
 * A factory for creating Hardware objects.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class HardwareFactory {

	/** referencing the isSimulation variable to the isSimualtion variable from the ConfigApp class. */
	private static boolean isSimulation = ConfigApp.isSimulation;
	
	/** setting the simulated acclereometer to null. */
	private static IAccelerometer accelerometer = null;
	
	/** setting the simulated GPS sensor to null. */
	private static IGPS gps = null;
	
	/** setting the simulated gyroscope to null */
	private static IGyroscope gyroscope = null;
	
	/** setting the simulated microphone to null. */
	private static IMicrophone microphone = null;
	
	/** setting the Sensormanager to null. */
	private static SensorManager sensorManager = null;
	
	/** setting the Location manager to null. */
	private static LocationManager locationManager = null;
	
	/** setting the simulated Heartrate sensor to null. */
	private static IHeartrate heartrate = null;
	
	/** setting the context for the Heatrate to null. */
	private static Context hrateContext = null;
	
	
	/**
	 * decides if in case of simulated sensors get the data from the CSV file or if using the sensors of the device from the sensors itself. 
	 */
	public static void initFactory() {
		CrtTargetDir.crtTargetDir();
		if (isSimulation) {
			CsvFileReader.readFile();
		}else {
			//do nothing, CsvFileWriter.crtFile(); will called by pressing the REC button
		}
	}
	
	
	/**
	 * Sets the location manager.
	 *
	 * @param locationManager provides access to the system location services
	 */
	public static void setLocationManager(LocationManager locationManager) {
		HardwareFactory.locationManager = locationManager;
	}

	/**
	 * Sets the sensor manager.
	 *
	 * @param sensorManager SensorManager lets you access the device's sensors
	 */
	public static void setSensorManager(SensorManager sensorManager) {
		HardwareFactory.sensorManager = sensorManager;
	}
	
	/**
	 * Sets the context for Heartrate.
	 *
	 * @param setHeartrateContext context lets you access the context from the MainActivity
	 */
	public static void setHeartrateContext(Context hrateContext) {
		HardwareFactory.hrateContext = hrateContext;
	}
	
	
	/**
	 * determines if to uses the devices accelerometer or simulate a accelerometer.
	 *
	 * @return either the simulated or the devices accelerometer
	 */
	public static IAccelerometer getAccelerometer() {
		if (isSimulation) {
			accelerometer = new AccelerometerSim();
		}else {
			if (accelerometer == null) {
				accelerometer = new AccelerometerHardware(sensorManager);
			}
		}	
		return accelerometer;
	}
	
	/**
	 * determines if to uses the devices GPS-sensor or simulate a GPS-sensor.
	 *
	 * @return either the simulated or the devices GPS-sensor
	 */
	public static IGPS getGPS() {
		if (isSimulation) {
			gps = new GPSSim();
		}else {
			if (gps == null) {
				gps = new GPSHardware(locationManager);
			}
		}
		return gps;
	}
	
	/**
	 * determines if to uses the devices gyroscope or simulate a gyroscope
	 * 
	 * @return either the simulated or the devices accelerometer 
	 */
	public static IGyroscope getGyroscope() {
		if (isSimulation) {
			gyroscope = new GyroscopeSim();
		}else {
			if (gyroscope == null) {
				gyroscope = new GyroscopeHardware(sensorManager);
			}
		}	
		return gyroscope;
	}
	
	/**
	 * determines if to uses the devices microphone or simulate a microphone.
	 *
	 * @return either the simulated or the devices microphone
	 */
	public static IMicrophone getMicrophone() {
		if (isSimulation) {
			microphone = new MicrophoneSim();
		}else {
			if (microphone == null) {
				microphone = new MicrophoneHardware();
			}
		}
		return microphone;
	}
	
	/**
	 * determines if to uses the devices heartbeat or simulate a heartbeat.
	 *
	 * @return either the simulated or the devices heartbeat
	 */
	public static IHeartrate getHeartbeat() {
		if (isSimulation) {
			heartrate = new HeartrateSim();
		}else {
			if (heartrate == null) {
				heartrate = new HeartrateHardware(hrateContext);
				heartrate.initHeartBeatSensor(hrateContext);
			}
		}
		return heartrate;
	}

	
	
	/**
	 * Checks if is simulation.
	 *
	 * @return true, if is simulation
	 */
	public static boolean isSimulation() {
		return isSimulation;
	}

	/**
	 * Sets the simulation.
	 *
	 * @param sim the new simulation
	 */
	public static void setSimulation(boolean sim) {
		isSimulation = sim;
	}
	
	
	
	
}