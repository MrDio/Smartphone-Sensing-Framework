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
package edu.example.ssf.mma.hardwareAdapter.gyroscope;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.IGyroscope;

/**
 * Initialising the Gyro-Sensor of the Smartphone and get the data output form
 * the sensor.
 * @author D. Lagamtzis
 * @version 2.0
 */
public class gyro implements SensorEventListener, IGyroscope {

    private SensorManager sensorManager;
    private Sensor gyr;

    private Float x = 0.0f;
    private Float y = 0.0f;
    private Float z = 0.0f;

    public gyro(){}

    public gyro(Context context) {
        initGyro(context);
    }

    @Override
    public void start() {
        sensorManager.registerListener(this, gyr, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            getGyroscope(event);
        }

    }

    private void getGyroscope(SensorEvent event) {


        float[] values = event.values;
        // Movement
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];

        CurrentTickData.rotationX = this.x;
        CurrentTickData.rotationY = this.y;
        CurrentTickData.rotationZ = this.z;

       // Log.d("GyroVal",CurrentTickData.rotationX +"");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void initGyro(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyr = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }


    @Override
    public Float getRotX() {
        return this.x;
    }

    @Override
    public Float getRotY() {
        return this.y;
    }

    @Override
    public Float getRotZ() {
        return this.z;
    }

}
