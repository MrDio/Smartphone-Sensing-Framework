package edu.example.ssf.mma.data;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class PerformanceAnalyzer {

    private static final int SMOOTHINGPOINTAMOUNT = 4;

    private static ArrayList<Lap> mLaps;

    public static void initialize(ArrayList<Lap> laps){
        mLaps = laps;
    }

    public static void clearNoise(){
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void smoothCurves(){
        mLaps.forEach(lap -> {
            List<List<TickData>> paritionedTicks = Lists.partition(lap.getRawData(),SMOOTHINGPOINTAMOUNT);
            ArrayList<TickData> newTickDataList = new ArrayList();
            newTickDataList.add(lap.getRawData().get(0));
            paritionedTicks.forEach(data->{
                TickData smoothedData = combineTickDatas(data);
                newTickDataList.add(smoothedData);
            });
            newTickDataList.add(lap.getRawData().get(lap.getRawData().size()-1));
            lap.setRawData(newTickDataList);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static TickData combineTickDatas(List<TickData> dataToCombine){
        TickData result = new TickData();
        dataToCombine.forEach(data -> {
            result.setAccX(result.getAccX()+data.getAccX());
            result.setAccY(result.getAccY()+data.getAccY());
            result.setTimeStamp(result.getTimeStamp()+data.getTimeStamp());
        });
        result.setTimeStamp(result.getTimeStamp()/dataToCombine.size());
        result.setAccX(result.getAccX()/dataToCombine.size());
        result.setAccY(result.getAccY()/dataToCombine.size());
        return result;
    }

    public static void calculatePerformanceIndicator(){

    }
}
