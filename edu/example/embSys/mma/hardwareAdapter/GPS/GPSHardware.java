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
package edu.example.embSys.mma.hardwareAdapter.GPS;

import edu.example.embSys.mma.hardwareAdapter.IGPS;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

// TODO: Auto-generated Javadoc
/**
 * Initialising the GPS-Sensor of the Smartphone and get the data output form
 * the sensor.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class GPSHardware implements IGPS, LocationListener {

	
	/** The location manager. */
	private LocationManager locationManager;

	/** setting the altitude in the beginning to 0. */
	private Double altitude = 0.0d;

	/** setting the latitude in the beginning to 0. */
	private Double latitude = 0.0d;

	/** setting the longitude in the beginning to 0. */
	private Double longitude = 0.0d;

	/** setting the bearing in the beginning to 0. */
	private Float bearing = 0.0f;

	/** setting the speed in the beginning to 0. */
	private Float speed = 0.0f;

	/**
	 * Instantiates the GPS sensor of the Smartphone.
	 * 
	 * @param locationManager
	 *            provides access to the system location services
	 */
	public GPSHardware(LocationManager locationManager) {
		this.locationManager = locationManager;
		this.initGPS();
	}

	/**
	 * is called when the location has changed.
	 *
	 * @param arg0 the new location.
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		this.altitude = arg0.getAltitude();
		this.latitude = arg0.getLatitude();
		this.longitude = arg0.getLongitude();
		this.bearing = arg0.getBearing();
		this.speed = arg0.getSpeed();
	}

	/**
	 * auto generated method.
	 *
	 * @param arg0 the arg0
	 */
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * auto generated method.
	 *
	 * @param arg0 the arg0
	 */
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * auto generated method.
	 *
	 * @param provider the provider
	 * @param status the status
	 * @param extras the extras
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	
	/**
	 * updates the location status with a minimum time interval of 5000 milliseconds between an update 
	 * and a minimum distance of 10 meters between an update.
	 */
	@Override
	public void initGPS() {
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 5000, 10, this);
	}

	
	/**
	 * gets the altitude as a double type from the GPS sensor.
	 *
	 * @return returned altitude value is of the type double
	 */
	@Override
	public Double getAltitude() {
		return this.altitude;
	}

	
	/**
	 * gets the latitude as a double type from the GPS sensor.
	 *
	 * @return returned latitude value is of the type double
	 */
	@Override
	public Double getLatitude() {
		return this.latitude;
	}

	/**
	 * gets the longitude as a double type from the GPS sensor.
	 *
	 * @return returned longitude value is of the type double
	 */
	@Override
	public Double getLongitude() {
		return this.longitude;
	}

	/**
	 * gets the bearing as a float type from the GPS sensor.
	 *
	 * @return returned bearing value is of the type float
	 */
	@Override
	public Float getBearing() {
		return this.bearing;
	}

	/**
	 * gets the speed as a float type from the GPS sensor.
	 *
	 * @return returned speed value is of the type float
	 */
	@Override
	public Float getSpeed() {
		return this.speed;
	}
}
