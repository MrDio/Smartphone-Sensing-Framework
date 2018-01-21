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

// TODO: Auto-generated Javadoc

import android.content.Context;
import android.widget.TextView;

/**
 * Interface class to initialise the GPS-sensor , get the altitude, longitude, latitude, bearing and speed.
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 */

public interface IGPS {

	/**
	 * Initialise the accelerometer.
	 */
	public void initGPS(Context context);

	public void start();
	public void stop();

	public void gpsUI(double v1,double v2,double v3,float v4, float v5,TextView... tvs);

	public Double getAltitude();
	public Double getLatitude();

	public Double getLongitude();

	public Float getBearing();


	public Float getSpeed();
}
