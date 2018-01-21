package edu.example.ssf.mma.hardwareAdapter.gyroscope;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.data.MathCalculations;
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
        float x = values[0];
        float y = values[1];
        float z = values[2];

        CurrentTickData.rotationX = x;
        CurrentTickData.rotationY = y;
        CurrentTickData.rotationZ = z;
        long actualTime = System.currentTimeMillis();
        CurrentTickData.curTimestamp = MathCalculations.convertDate(actualTime,"hh:mm:ss:SSS");
        Log.d("GyroVal",CurrentTickData.rotationX +"");
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
    public void gyroUI(float v1, float v2, float v3, TextView... tvs){
        Log.d("gyro","gyro");
        tvs[0].setText("Rot. X: " + String.format("%.2f", v1));
        tvs[1].setText("Rot. Y: " + String.format("%.2f", v2));
        tvs[2].setText("Rot. Z: " + String.format("%.2f", v3));
    }

    @Override
    public Float getRotX() {
        return null;
    }

    @Override
    public Float getRotY() {
        return null;
    }

    @Override
    public Float getRotZ() {
        return null;
    }

}
