package edu.example.ssf.mma.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SectionIdentifier {
    private static Section currentSection;
    private static ArrayList<TickData> curveTicks;
    private static boolean curveStarted = false;
    private static final float CURVETHRESHOLD = 1.5f;
    private static final int MINCURVEPOINTS = 8;


    public static ArrayList<Section> identifySections(List<TickData> data){
        ArrayList<Section> sections = new ArrayList<>();
        currentSection = new Section();
        currentSection.setStart(data.get(0));
        currentSection.setMedian(new TickData());
        currentSection.setType(Section.SectionType.STRAIGHT);
        for (TickData TickData : data) {
            if(Math.abs(TickData.getAccX()) >= CURVETHRESHOLD && !curveStarted && currentSection.getType() == Section.SectionType.STRAIGHT){
                curveStarted = true;
                curveTicks = new ArrayList<>();
                currentSection.setEnd(data.get(data.indexOf(data)-1));
                currentSection.setTimeTaken();
                sections.add(currentSection);
                currentSection = new Section();
                currentSection.setStart(data.get(data.indexOf(data)-1));
            } else if(Math.abs(TickData.getAccX()) <= CURVETHRESHOLD && curveStarted && currentSection.getType() == Section.SectionType.UNDEFINED){
                curveStarted = false;
                currentSection.setEnd(data.get(data.indexOf(data)+1));
                curveTicks.add(data.get(data.indexOf(data)+1));
                currentSection.setTimeTaken();
                TickData median = calculateMedian(curveTicks);
                currentSection.setMedian(median);
                if(curveTicks.size()<MINCURVEPOINTS && median.getAccX() < CURVETHRESHOLD){
                    currentSection.setType(Section.SectionType.INVALID);
                } else{
                    currentSection.calculateCurveType();
                }
                sections.add(currentSection);
                currentSection = new Section();
                currentSection.setStart(data.get(data.indexOf(data)+1));
                currentSection.setType(Section.SectionType.STRAIGHT);
            }
            if(curveStarted && TickData.getAccX() >= CURVETHRESHOLD){
                curveTicks.add(TickData);
            }
        }
        return new ArrayList<Section>();
    }

    private static TickData calculateMedian(ArrayList<TickData> curveData){
        TickData accumulatedTickData = new TickData();
        TickData result = new TickData();

        for (TickData data : curveData) {
            accumulatedTickData.setAccX(accumulatedTickData.getAccX()+data.getAccX());
            accumulatedTickData.setAccY(accumulatedTickData.getAccY()+data.getAccY());
        }

        result.setTimeStamp((curveData.get(0).getTimeStamp()+curveData.get(curveData.size()-1).getTimeStamp())/2);
        result.setAccX(accumulatedTickData.getAccX()/curveData.size());
        result.setAccY(accumulatedTickData.getAccY()/curveData.size());

        return result;
    }
}
