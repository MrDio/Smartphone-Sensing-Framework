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


    public static void smoothCurves(){
        for (Lap lap : mLaps) {
            List<List<TickData>> paritionedTicks = Lists.partition(lap.getRawData(),SMOOTHINGPOINTAMOUNT);
            ArrayList<TickData> newTickDataList = new ArrayList();
            CsvFileWriter.crtFile();
            newTickDataList.add(lap.getRawData().get(0));
            for (List<TickData> data : paritionedTicks) {
                TickData smoothedData = combineTickDatas(data);
                newTickDataList.add(smoothedData);
            }
            newTickDataList.add(lap.getRawData().get(lap.getRawData().size()-1));
            for (TickData x : newTickDataList) {
                CsvFileWriter.writeLine("0",""+x.getTimeStamp(),""+x.getAccX(),""+x.getAccY());
            }
            lap.setRawData(newTickDataList);
        }

    }
    private static TickData combineTickDatas(List<TickData> dataToCombine){
        TickData result = new TickData();
        for (TickData data : dataToCombine) {

            result.setAccX(result.getAccX()+data.getAccX());
            result.setAccY(result.getAccY()+data.getAccY());
            result.setTimeStamp(result.getTimeStamp()+data.getTimeStamp());
        }
        result.setTimeStamp(result.getTimeStamp()/dataToCombine.size());
        result.setAccX(result.getAccX()/dataToCombine.size());
        result.setAccY(result.getAccY()/dataToCombine.size());
        return result;
    }

    public static void calculatePerformanceIndicator(){

    }
}
