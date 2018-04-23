package edu.example.ssf.mma.hardwareAdapter.lightsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.ILightSensor;
import edu.example.ssf.mma.userInterface.MainActivity;

public class LightSensor implements SensorEventListener, ILightSensor {

    private SensorManager sensorManager;
    private Sensor light;
    private static float SENSOR_DARK_CAP = 0;
    private Context context;

    private float calibratedMax;
    private float calibratedMin;
    private float intensity;
    private boolean isNewRound = false;

    public LightSensor() {
    }

    public LightSensor(Context context) {
        initLightSensor(context);
    }

    @Override
    public void initLightSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        this.context = context;
    }

    @Override
    public void start() {
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public float getLightIntesitiy() {
        return this.calibratedMax;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            //System.out.println(new Date().getTime()%10000);
            if (event.values[0] <= SENSOR_DARK_CAP) {
                //dark
                intensity = event.values[0];
                isNewRound = true;
            } else {
                //bright
                if (MainActivity.isRacing && isNewRound) {
                    Toast.makeText(context, "Round: " + CurrentTickData.round, Toast.LENGTH_LONG).show();
                    CurrentTickData.round++;
                    isNewRound = false;
                }
                intensity = event.values[0];
            }

        }
        //System.out.println("LightIntensity: " + this.intensity);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void calibrateMax() {
        this.calibratedMax = this.intensity;
        System.out.println("MAX: " + this.calibratedMax);
    }

    @Override
    public void calibrateMin() {
        this.calibratedMin = this.intensity;
        System.out.println("MIN: " + this.calibratedMin);
        if (calibratedMax - calibratedMin > 50) {
            SENSOR_DARK_CAP = calibratedMin + 25;
        } else if(calibratedMax - calibratedMin > 10){
            SENSOR_DARK_CAP = calibratedMin;
        } else {
            //TODO: eigentlich müsste hier dann feedback kommen, dass es zu dunkel ist...
            SENSOR_DARK_CAP = calibratedMin;
        }
    }
}
