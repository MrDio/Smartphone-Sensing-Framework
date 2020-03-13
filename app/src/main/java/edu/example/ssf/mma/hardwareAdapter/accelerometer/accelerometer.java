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

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.data.MathCalculations;
import edu.example.ssf.mma.hardwareAdapter.IAccelerometer;


/**
 * Initialising the ACC-Sensor of the Smartphone and get the data output form
 * the sensor.
 * @author D. Lagamtzis
 * @version 2.0
 */

public class accelerometer implements SensorEventListener, IAccelerometer {

    private SensorManager sensorManager;
    private Sensor acc;

    private Float x = 0.0f;
    private Float y = 0.0f;
    private Float z = 0.0f;
    private Double accelationSquareRoot = 0.0d;

    public accelerometer(){}

    public accelerometer(Context context) {
        initAccelerometer(context);
    }

    @Override
    public void start() {
        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        //CsvFileWriter.crtFile();
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
       // CsvFileWriter.closeFile();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {


        float[] values = event.values;
        // Movement
        x = values[0];
        y = values[1];
        z = values[2];
        double accelationSquareRoot = MathCalculations.calculatePythagoras(x,y,z);

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
        return this.accelationSquareRoot;
    }
}
