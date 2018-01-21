package edu.example.ssf.mma.hardwareAdapter.proximity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.data.MathCalculations;
import edu.example.ssf.mma.hardwareAdapter.IProximity;
/**
 * Initialising the Proximity-Sensor of the Smartphone and get the data output form
 * the sensor.
 * @author D. Lagamtzis
 * @version 2.0
 */

public class proximity implements SensorEventListener, IProximity {

    private SensorManager sensorManager;
    private Sensor proxi;
    private static final int SENSOR_SENSITIVITY = 4;

    public proximity(){}

    public proximity(Context context) {
        initProximity(context);
    }

    @Override
    public void start() {
        sensorManager.registerListener(this, proxi, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            //getProximity(event);
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                CurrentTickData.proxState = "near";
                //CurrentTickData.proximity = event.values[0];
            } else {
                //far
                CurrentTickData.proxState = "far";
                //CurrentTickData.proximity = event.values[0];
            }
            long actualTime = System.currentTimeMillis();
            CurrentTickData.curTimestamp = MathCalculations.convertDate(actualTime,"hh:mm:ss:SSS");
        }

    }

    private void getProximity(SensorEvent event) {


        float x = event.values[0];

        long actualTime = event.timestamp;


        CurrentTickData.proximity = x;
        CurrentTickData.curTimestamp = actualTime + "";
        Log.d("ProximitxVal",CurrentTickData.proximity +"");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void initProximity(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        proxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    public void proxiUI(String s1, TextView...tvs){
        Log.d("proxi","proxi");
        tvs[0].setText("Proximity: " + s1);
    }

    @Override
    public String getProximity() {
        return null;
    }


}
