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
package edu.example.ssf.mma.hardwareAdapter.GPS;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.data.MathCalculations;
import edu.example.ssf.mma.hardwareAdapter.IGPS;


// TODO: Auto-generated Javadoc

/**
 * Initialising the GPS-Sensor of the Smartphone and get the data output form
 * the sensor.
 * @author D. Lagamtzis
 * @version 2.0
 */

public class gps extends AppCompatActivity implements LocationListener, IGPS {

	private Location location;
	private LocationManager locationManager;
	private static final String TAG = "BOOMBOOMTESTGPS";
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	public gps(){}

	public gps(Context context ) {

		initGPS(context );

	}


	@Override
	public void initGPS(Context context) {

		getLocation(context);
	}

	@Override
	public void start() {
		//Not in use

	}

	@Override
	public void stop() {
		// Not in use
	}
	public void getLocation(Context context){
		if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
			Log.e("fist","error");
		}
		try {
			LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
			boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (isGPSEnabled){
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
				location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}else{
				Log.e("sec","error");
			}
			// Initialize the location fields
			if (location != null) {
				System.out.println("Provider " + LocationManager.GPS_PROVIDER + " has been selected.");
				onLocationChanged(location);
			} else {
				getGPS(location);
				long actualTime = System.currentTimeMillis();
				CurrentTickData.curTimestamp = MathCalculations.convertDate(actualTime,"hh:mm:ss:SSS");
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	/* Request updates at startup */
	@Override
	protected void onResume() {
		super.onResume();
		/*if (ContextCompat.checkSelfPermission( c, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
			Log.e("fist","error");
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000,10,this);*/
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		getGPS(location);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}


	@Override
	public void gpsUI(double v1, double v2, double v3, float v4, float v5,TextView... tvs){
		tvs[0].setText("GPS Alt: " + String.format("%.2f", v1));
		tvs[1].setText("GPS Lat: " + String.format("%.6f", v2));
		tvs[2].setText("GPS Lon: "  + String.format("%.6f", v3));
		tvs[3].setText("GPS Bear: " + String.format("%.2f", v4));
		tvs[4].setText("GPS Speed: " + String.format("%.2f", v5));

	}

	@Override
	public Double getAltitude() {
		return null;
	}

	@Override
	public Double getLatitude() {
		return null;
	}

	@Override
	public Double getLongitude() {
		return null;
	}

	@Override
	public Float getBearing() {
		return null;
	}

	@Override
	public Float getSpeed() {
		return null;
	}

	private void getGPS(Location location){

		location.set(location);
		CurrentTickData.GPSalt = location.getAltitude();
		CurrentTickData.GPSbearing = location.getBearing();
		CurrentTickData.GPSlat = location.getLatitude();
		CurrentTickData.GPSlon = location.getLongitude();
		CurrentTickData.GPSspeed = location.getSpeed();
		CurrentTickData.curTimestamp = location.getTime() +"";
	}

}