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
package edu.example.ssf.mma.hardwareAdapter.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;


import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.data.MathCalculations;
import edu.example.ssf.mma.hardwareAdapter.IAccelerometer;


/**
 * Initialising the ACC-Sensor of the Smartphone and get the data output form
 * the sensor.
 *
 * @author D. Lagamtzis
 * @version 2.0
 */

public class Accelerometer implements SensorEventListener, IAccelerometer {

    private SensorManager sensorManager;
    private Sensor acc;

    private Float x = 0.0f;
    private Float y = 0.0f;
    private Float z = 0.0f;
    private Double accelerationSquareRoot = 0.0d;
    private float[] gravity = {0, 0, 9.81f};

    public Accelerometer() {
    }

    public Accelerometer(Context context) {
        initAccelerometer(context);
    }

    @Override
    public void start() {
        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_FASTEST);
        //CsvFileWriter.crtFile();
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
        // CsvFileWriter.closeFile();
    }

//    @Override
//    public void enableCalibration() {
//    }
//
//    @Override
//    public void disableCalibration() {
//    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        System.out.println("Accelerometer Changed");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate

/*            final float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            float[] linear_acceleration = new float[3];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];*/


            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {


        float[] values = event.values;
        // Movement
        x = values[0];
        y = values[1];
        z = values[2];
        double accelationSquareRoot = MathCalculations.calculatePythagoras(x, y, z);

        CurrentTickData.accX = x;
        CurrentTickData.accY = y;
        CurrentTickData.accZ = z;
        CurrentTickData.accVecA = accelationSquareRoot;

        //Log.d("AccValues",CurrentTickData.accVecA +"");

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void initAccelerometer(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    @Override
    public Float getAccX() {
        return this.x;
    }

    @Override
    public Float getAccY() {
        return this.y;
    }

    @Override
    public Float getAccZ() {
        return this.z;
    }

    @Override
    public Double getAccA() {
        return this.accelerationSquareRoot;
    }
}
