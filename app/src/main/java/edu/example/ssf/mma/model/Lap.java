package edu.example.ssf.mma.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Lap implements Serializable {


    private boolean isValid = true;
    private int number;
    private double roundTime;
    private double performanceIndicator;
    private ArrayList<Section> sections;
    private ArrayList<TickData> rawData;
    private boolean isFastestLap = false;


    public Lap(){
        sections = new ArrayList<>();
        rawData = new ArrayList<>();
        performanceIndicator = 0.5;
    }

    public Lap(int number, double roundTime, double performanceIndicator){
        this.number = number;
        this.roundTime = roundTime;
        this.performanceIndicator = performanceIndicator;
        sections = new ArrayList<>();
        rawData = new ArrayList<>();
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public String getNumberAsString() {
        return "Lap: "+(number+1);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getRoundTimeAsString() {


        int seconds = (int)roundTime/1000;
        roundTime -= seconds*1000;
        int minutes = seconds/60;
        seconds -= minutes*60;

        String secondString = Integer.toString(seconds);
        int milliseconds = (int)roundTime;
        String millisecondString = Integer.toString(milliseconds);

        if(seconds < 10){
            secondString = "0"+seconds;
        }
        if(milliseconds < 100){
            millisecondString = "0"+milliseconds;
        }
        if(milliseconds < 10){
            millisecondString = "00"+milliseconds;
        }
        return minutes + ":" + secondString + ":" + millisecondString;
    }

    public double getRoundTime() {
        return roundTime;
    }

    public void setRoundTime(double roundTime) {
        this.roundTime = roundTime;
    }

    public int getPerformanceIndicator() {
        if(performanceIndicator > 0.75){
            return 3;
        }
        if(performanceIndicator > 0.25){
            return 2;
        }
        if(performanceIndicator > 0){
            return 1;
        }
        else{
            return 0;
        }
    }

    public void setPerformanceIndicator(double performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }

    public ArrayList<TickData> getRawData() {
        return rawData;
    }

    public void setRawData(ArrayList<TickData> rawData) {
        this.rawData = rawData;
    }

    public void setDataPoint(TickData data){
        rawData.add(data);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isFastestLap() {
        return isFastestLap;
    }

    public void setFastestLap(boolean fastestLap) {
        isFastestLap = fastestLap;
    }
}
