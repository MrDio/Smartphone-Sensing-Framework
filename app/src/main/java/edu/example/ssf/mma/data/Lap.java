package edu.example.ssf.mma.data;

import java.util.ArrayList;

public class Lap {



    private int number;
    private double roundTime;
    private double performanceIndicator;
    private ArrayList<Section> sections;

    public Lap(int number, double roundTime, double performanceIndicator){
        this.number = number;
        this.roundTime = roundTime;
        this.performanceIndicator = performanceIndicator;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public String getNumber() {
        return "Lap: "+number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getRoundTime() {


        int minutes = (int)roundTime/60;
        int seconds = (int)roundTime;
        int milli = (int)((roundTime - seconds) * 1000);
        seconds -= 60*minutes;
        return minutes + ":" + seconds + ":" + milli;
    }

    public void setRoundTime(double roundTime) {
        this.roundTime = roundTime;
    }

    public int getPerformanceIndicator() {
        if(performanceIndicator > 0.75){
            return 2;
        }
        if(performanceIndicator > 0.25){
            return 1;
        }
        else{
            return 0;
        }
    }

    public void setPerformanceIndicator(double performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }

}
