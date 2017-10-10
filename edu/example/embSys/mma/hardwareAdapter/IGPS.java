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
package edu.example.embSys.mma.hardwareAdapter;

// TODO: Auto-generated Javadoc
/**
 * Interface class to initialise the GPS-sensor , get the altitude, longitude, latitude, bearing and speed.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public interface IGPS {
	
	/**
	 * Initialise the GPS-Sensor.
	 */
	public void initGPS();

	/**
	 * Gets the altitude-value.
	 *
	 * @return the altitude as type of Double
	 */
	public Double getAltitude();

	/**
	 * Gets the latitude-value.
	 *
	 * @return the latitude as type of double
	 */
	public Double getLatitude();
	
	/**
	 * Gets the longitude-value.
	 *
	 * @return the longitude as type of double
	 */
	public Double getLongitude();
	
	/**
	 * Gets the bearing-value.
	 *
	 * @return the bearing as type of float
	 */
	public Float getBearing();
	
	/**
	 * Gets the speed-value.
	 *
	 * @return the speed as type of float
	 */
	public Float getSpeed();
}
