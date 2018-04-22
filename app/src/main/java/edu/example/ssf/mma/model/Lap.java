package edu.example.ssf.mma.model;

import java.util.ArrayList;

public class Lap {


    private boolean isValid = true;
    private int number;
    private double roundTime;
    private double performanceIndicator;
    private ArrayList<Section> sections;
    private ArrayList<TickData> rawData;

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
        return "Lap: "+number;
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
        return minutes + ":" + seconds + ":" + (int)roundTime;
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
}
