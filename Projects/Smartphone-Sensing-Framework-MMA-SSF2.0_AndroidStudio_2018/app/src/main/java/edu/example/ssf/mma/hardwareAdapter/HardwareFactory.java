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
import edu.example.ssf.mma.hardwareAdapter.microphone.microSim;
import edu.example.ssf.mma.hardwareAdapter.microphone.microphone;


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

    public static IMicrophone hwMic = null;

    /**
     * decides if in case of simulated sensors get the data from the CSV file or if using the sensors of the device from the seonsrs itself.
     */

    public HardwareFactory(Context context) {
        if (isSimulation) {
            Log.d("hwSim", "simulation fail");
            CsvFileReader.readFile();
            Log.d("hwSim", "simulation readfile");
        } else {

            getMicrophone(context);
            Log.d("init?", "mic initialized");

        }
    }


    /**
     * determines if to uses the devices microphone or simulate a microphone
     *
     * @return either the simulated or the devices microphone
     */
    public static IMicrophone getMicrophone(Context context) {
        if (isSimulation) {
            hwMic = new microSim();
        } else {
            hwMic = new microphone(context);
        }
        return hwMic;
    }
}