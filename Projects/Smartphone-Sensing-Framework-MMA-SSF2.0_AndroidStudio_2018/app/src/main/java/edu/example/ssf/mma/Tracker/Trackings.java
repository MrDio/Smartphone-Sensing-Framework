package edu.example.ssf.mma.Tracker;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;

/**
 * Created by dennismuller on 23.04.18.
 */

public class Trackings {

    private static Date beginnOfTrackingTime = null;

    public static void startNewTracking() {
        beginnOfTrackingTime = new Date();
        onStartNewTracking.run();
    }

    public static Consumer<Long> onNewDelta = null;
    public static Runnable onStartNewTracking = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void endTracking(){
        Date now = new Date();
        Timestamp timestampEnd = new Timestamp(now.getTime());
        long diff = timestampEnd.getTime() - beginnOfTrackingTime.getTime();
        onNewDelta.accept(diff);
    }


}
