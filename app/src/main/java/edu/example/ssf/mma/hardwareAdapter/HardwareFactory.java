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

import edu.example.ssf.mma.config.ConfigApp;
import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.hardwareAdapter.GPS.gps;
import edu.example.ssf.mma.hardwareAdapter.GPS.gpsSim;
import edu.example.ssf.mma.hardwareAdapter.accelerometer.accelerometer;
import edu.example.ssf.mma.hardwareAdapter.accelerometer.accelerometerSim;
import edu.example.ssf.mma.hardwareAdapter.gyroscope.gyro;
import edu.example.ssf.mma.hardwareAdapter.gyroscope.gyroSim;
import edu.example.ssf.mma.hardwareAdapter.magnetometer.magneto;
import edu.example.ssf.mma.hardwareAdapter.magnetometer.magnetoSim;
import edu.example.ssf.mma.hardwareAdapter.microphone.microSim;
import edu.example.ssf.mma.hardwareAdapter.microphone.microphone;
import edu.example.ssf.mma.hardwareAdapter.proximity.proxiSim;
import edu.example.ssf.mma.hardwareAdapter.proximity.proximity;


// TODO: Auto-generated Javadoc

/**
 * * Initialises the hardware components and checks if either a simulated or the sensor of the device should be used.
 * A factory for creating Hardware objects.
 *
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @author D. Lagamtzis
 * @version 2.0
 */

public class HardwareFactory {

    /**
     * referencing the isSimulation variable to the isSimulation variable from the ConfigApp class.
     */
    private static boolean isSimulation = ConfigApp.isSimulation;

    /**
     * setting the simulated accelerometer to null.
     */
    public static IAccelerometer hwAcc = null;

//    public static IGPS hwGPS = null;
//
//    public static IGyroscope hwGyro = null;
//
//    public static IMicrophone hwMic = null;
//
//    public static IMagneto hwMagn = null;
//
//    public static IProximity hwProxi = null;


    /**
     * decides if in case of simulated sensors get the data from the CSV file or if using the sensors of the device from the seonsrs itself.
     */

    public HardwareFactory(Context context) {
        if (isSimulation) {
            Log.d("hwSim", "simulation fail");
            CsvFileReader.readFile();
            Log.d("hwSim", "simulation readfile");
        } else {
            getAccelerometer(context);
            Log.d("init?", "acc initialized");
//		getGPS(context);
//		Log.d("init?","gps initialized");
//		getGyroscope(context);
//		Log.d("init?","gyro initialized");
//		getMicrophone(context);
//		Log.d("init?","mic initialized");
//		getMagnetometer(context);
//		Log.d("init?","magnetomerer initialized");
//		getProximity(context);
//		Log.d("init?","proxi initialized");
        }
    }


    /**
     * determines if to uses the devices accelerometer or simulate a accelerometer
     *
     * @return either the simulated or the devices accelerometer
     */
    public static IAccelerometer getAccelerometer(Context context) {
        if (isSimulation) {
            Log.d("getAccX", "HardwareFactory.getAccelerometer");
            hwAcc = new accelerometerSim();
        } else {
            hwAcc = new accelerometer(context);
        }
        return hwAcc;
    }
//
//    /**
//     * determines if to uses the devices GPS-sensor or simulate a GPS-sensor
//     *
//     * @return either the simulated or the devices GPS-sensor
//     */
//    public static IGPS getGPS(Context context) {
//        if (isSimulation) {
//            hwGPS = new gpsSim();
//        } else {
//            hwGPS = new gps(context);
//        }
//        return hwGPS;
//    }
//
//    /**
//     * determines if to uses the devices Gyro-sensor or simulate a Gyro-sensor
//     *
//     * @return either the simulated or the devices Gyro-sensor
//     */
//    public static IGyroscope getGyroscope(Context context) {
//        if (isSimulation) {
//            hwGyro = new gyroSim();
//        } else {
//            hwGyro = new gyro(context);
//        }
//        return hwGyro;
//    }
//
//
//    /**
//     * determines if to uses the devices microphone or simulate a microphone
//     *
//     * @return either the simulated or the devices microphone
//     */
//    public static IMicrophone getMicrophone(Context context) {
//        if (isSimulation) {
//            hwMic = new microSim();
//        } else {
//            hwMic = new microphone(context);
//        }
//        return hwMic;
//    }
//
//    /**
//     * determines if to uses the devices magnetometer or simulate a magnetometer
//     *
//     * @return either the simulated or the devices magnetometer
//     */
//    public static IMagneto getMagnetometer(Context context) {
//        if (isSimulation) {
//            hwMagn = new magnetoSim();
//        } else {
//            hwMagn = new magneto(context);
//        }
//        return hwMagn;
//    }
//
//    /**
//     * determines if to uses the devices proximity sensor or simulate a proximity sensor
//     *
//     * @return either the simulated or the devices proximity sensor
//     */
//    public static IProximity getProximity(Context context) {
//        if (isSimulation) {
//            hwProxi = new proxiSim();
//        } else {
//            hwProxi = new proximity(context);
//        }
//        return hwProxi;
//    }


}