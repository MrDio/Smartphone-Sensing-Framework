package com.example.ldg8fe.rapiddisruption.hardwareAdapter.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.example.ldg8fe.rapiddisruption.data.CsvFileWriter;
import com.example.ldg8fe.rapiddisruption.data.CurrentTickData;
import com.example.ldg8fe.rapiddisruption.data.MathCalculations;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.HardwareFactory;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.IAccelerometer;

import java.text.SimpleDateFormat;
import java.util.Date;


public class accelerometer implements SensorEventListener, IAccelerometer {

    private SensorManager sensorManager;
    private Sensor acc;

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
        float x = values[0];
        float y = values[1];
        float z = values[2];
        double accelationSquareRoot = MathCalculations.calculatePythagoras(x,y,z);
        //long actualTime = event.timestamp;


        CurrentTickData.accX = x;
        CurrentTickData.accY = y;
        CurrentTickData.accZ = z;
        CurrentTickData.accVecA = accelationSquareRoot;

        long actualTime = System.currentTimeMillis();
        CurrentTickData.curTimestamp = MathCalculations.convertDate(actualTime,"hh:mm:ss:SSS");
        Log.d("AccValues",CurrentTickData.accVecA +"");


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
    public void accUI(double v1, double v2, double v3, double v4,TextView... tvs){
        tvs[0].setText("X: " + String.format("%.2f", v1));
        tvs[1].setText("Y: " + String.format("%.2f", v2));
        tvs[2].setText("Z: " + String.format("%.2f", v3));
        tvs[3].setText("AccV: " + String.format("%.2f", v4));
    }

    @Override
    public Float getAccX() {
        return null;
    }

    @Override
    public Float getAccY() {
        return null;
    }

    @Override
    public Float getAccZ() {
        return null;
    }

    @Override
    public Double getAccA() {
        return null;
    }
}
