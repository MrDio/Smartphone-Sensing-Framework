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

    public static void applySavitzkyGolay(){
        for (Lap lap : mLaps) {
            ArrayList<TickData> newTickDataList = new ArrayList();
            CsvFileWriter.crtFile();
            newTickDataList.add(lap.getRawData().get(0));
            newTickDataList.add(lap.getRawData().get(1));
            newTickDataList.add(lap.getRawData().get(2));
            newTickDataList.add(lap.getRawData().get(3));
            List<TickData> tempLap = lap.getRawData();
            for (int i = 4; i < tempLap.size()-4; i++) {
                TickData tempData = new TickData();
                float newAccX = getNewAccX(tempLap, i);
                float newAccY = getNewAccY(tempLap, i);
                tempData.setAccX(newAccX);
                tempData.setAccY(newAccY);
                tempData.setTimeStamp(tempLap.get(i).getTimeStamp());
                newTickDataList.add(tempData);
            }
            newTickDataList.add(lap.getRawData().get(lap.getRawData().size()-4));
            newTickDataList.add(lap.getRawData().get(lap.getRawData().size()-3));
            newTickDataList.add(lap.getRawData().get(lap.getRawData().size()-2));
            newTickDataList.add(lap.getRawData().get(lap.getRawData().size()-1));
            for (TickData x : newTickDataList) {
                CsvFileWriter.writeLine("0",""+x.getTimeStamp(),""+x.getAccX(),""+x.getAccY());
            }
            lap.setRawData(newTickDataList);
        }
    }

    private static float getNewAccX(List<TickData> tempLap, int i) {
        float c0 = tempLap.get(i-4).getAccX() * (-21);
        float c1 = tempLap.get(i-3).getAccX() * 14 + c0;
        float c2 = tempLap.get(i-2).getAccX() * 39 + c1;
        float c3 = tempLap.get(i-1).getAccX() * 54 + c2;
        float c4 = tempLap.get(i).getAccX() * 59 + c3;
        float c5 = tempLap.get(i+1).getAccX() * 54 + c4;
        float c6 = tempLap.get(i+2).getAccX() * 39 + c5;
        float c7 = tempLap.get(i+3).getAccX() * 14 + c6;
        float c8 = tempLap.get(i+4).getAccX() * (-21) + c7;

        return c8/231;
    }

    private static float getNewAccY(List<TickData> tempLap, int i) {
        float c0 = tempLap.get(i-4).getAccY() * (-21);
        float c1 = tempLap.get(i-3).getAccY() * 14 + c0;
        float c2 = tempLap.get(i-2).getAccY() * 39 + c1;
        float c3 = tempLap.get(i-1).getAccY() * 54 + c2;
        float c4 = tempLap.get(i).getAccY() * 59 + c3;
        float c5 = tempLap.get(i+1).getAccY() * 54 + c4;
        float c6 = tempLap.get(i+2).getAccY() * 39 + c5;
        float c7 = tempLap.get(i+3).getAccY() * 14 + c6;
        float c8 = tempLap.get(i+4).getAccY() * (-21) + c7;

        return c8/231;
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
