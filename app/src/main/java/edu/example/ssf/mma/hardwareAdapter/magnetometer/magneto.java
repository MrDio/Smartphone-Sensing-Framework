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
package edu.example.ssf.mma.hardwareAdapter.magnetometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.IMagneto;

/**
 * Initialising the MagneticField-Sensor of the Smartphone and get the data output form
 * the sensor.
 * @author D. Lagamtzis
 * @version 2.0
 */

public class magneto implements SensorEventListener, IMagneto {

    private SensorManager sensorManager;
    private Sensor magn;

    private Float x = 0.0f;
    private Float y = 0.0f;
    private Float z = 0.0f;

    public magneto(){}

    public magneto(Context context) {
        initMagneto(context);
    }

    @Override
    public void start() {
        sensorManager.registerListener(this, magn, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            getMagneto(event);
        }

    }

    private void getMagneto(SensorEvent event) {


        float[] values = event.values;
        // Movement
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];

        CurrentTickData.magneticX = this.x;
        CurrentTickData.magneticY = this.y;
        CurrentTickData.magneticZ = this.z;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void initMagneto(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        magn = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }


    @Override
    public Float getMagnetoX() {
        return null;
    }

    @Override
    public Float getMagnetoY() {
        return null;
    }

    @Override
    public Float getMagnetoZ() {
        return null;
    }

}
