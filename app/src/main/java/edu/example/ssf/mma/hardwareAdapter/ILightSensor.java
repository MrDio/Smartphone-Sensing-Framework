package edu.example.ssf.mma.hardwareAdapter;

import android.content.Context;

public interface ILightSensor {

    void initLightSensor (Context context);

    void start();
    void stop();
    void calibrateMax();
    void calibrateMin();

    float getLightIntesitiy();
}
