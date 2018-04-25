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
package edu.example.ssf.mma.hardwareAdapter.proximity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.IProximity;
import edu.example.ssf.mma.userInterface.MainActivity;

/**
 * Initialising the Proximity-Sensor of the Smartphone and get the data output form
 * the sensor.
 * @author D. Lagamtzis
 * @version 2.0
 */

public class ProximitySensor implements SensorEventListener, IProximity {

    private SensorManager sensorManager;
    private Sensor proxi;
    private static final int SENSOR_SENSITIVITY = 4;
    private Context context;
    private boolean isNewRound = false;
    private Float x = 0.0f;
    private boolean proxState = false;

    public ProximitySensor(){}

    public ProximitySensor(Context context) {
        initProximity(context);
    }

    @Override
    public void start() {
        sensorManager.registerListener(this, proxi, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            System.out.println(event.values[0]);
            if (event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                this.proxState = true;
                CurrentTickData.proxState = this.proxState;
                if(MainActivity.isRacing) isNewRound = true;

            } else {
                //far
                this.proxState = false;
                if(MainActivity.isRacing && isNewRound){
                    isNewRound = false;
                    CurrentTickData.round++;
                    Toast.makeText(context, "Round: "+CurrentTickData.round, Toast.LENGTH_LONG).show();
                }

                CurrentTickData.proxState = this.proxState;
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void initProximity(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        proxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        this.context = context;
    }

    @Override
    public boolean getProximity() {
        return this.proxState;
    }


}
