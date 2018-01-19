package com.example.ldg8fe.rapiddisruption.hardwareAdapter.magnetometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import com.example.ldg8fe.rapiddisruption.data.CurrentTickData;
import com.example.ldg8fe.rapiddisruption.data.MathCalculations;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.IMagneto;


public class magneto implements SensorEventListener, IMagneto {

    private SensorManager sensorManager;
    private Sensor magn;

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
        float x = values[0];
        float y = values[1];
        float z = values[2];


        CurrentTickData.magneticX = x;
        CurrentTickData.magneticY = y;
        CurrentTickData.magneticZ = z;
        long actualTime = System.currentTimeMillis();
        CurrentTickData.curTimestamp = MathCalculations.convertDate(actualTime,"hh:mm:ss:SSS");
        Log.d("MagnetoVal",CurrentTickData.magneticX +"");
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
    public void magnUI(float v1, float v2, float v3, TextView... tvs){
        Log.d("magneto","magneto");
        tvs[0].setText("Magn. X: " + String.format("%.2f", v1));
        tvs[1].setText("Magn. Y: " + String.format("%.2f", v2));
        tvs[2].setText("Magn. Z: " + String.format("%.2f", v3));
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
