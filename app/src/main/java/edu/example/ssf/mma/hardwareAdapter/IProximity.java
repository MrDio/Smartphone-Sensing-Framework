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

/**
 * Interface class to Instantiate the Proximity Sensor , get the state of the Sensor
 * @author D. Lagamtzis
 * @version 1.0
 */

public interface IProximity {


	/**
	 * Initialise the accelerometer.
	 */
	 void initProximity(Context context);

	 void start();
	 void stop();

	 String getProximity();
}