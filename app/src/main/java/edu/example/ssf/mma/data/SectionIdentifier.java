package edu.example.ssf.mma.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SectionIdentifier {
    private static Section currentSection;
    private static ArrayList<TickData> curveTicks;
    private static boolean curveStarted = false;
    private static final float CURVETHRESHOLD = 2f;
    private static final int MINCURVEPOINTS = 3;
    private static final int NUMBEROFPOINTSCONSIDERED = 4;
    private static boolean firstSection= false;


    public static ArrayList<Section> identifySections(ArrayList<TickData> data){
        curveStarted = false;
        firstSection = true;
        ArrayList<Section> sections = new ArrayList<>();
        currentSection = new Section();
        currentSection.setStart(data.get(0));
        currentSection.setMedian(new TickData());
        currentSection.setType(Section.SectionType.STRAIGHT);
        for (TickData tickData : data) {
            if(CheckNextPointsGREATERThanThreshold(data,tickData,CURVETHRESHOLD,NUMBEROFPOINTSCONSIDERED) && !curveStarted && currentSection.getType() == Section.SectionType.STRAIGHT){
                curveStarted = true;
                curveTicks = new ArrayList<>();
                currentSection.setEnd(tickData);
                currentSection.setTimeTaken();
                sections.add(currentSection);
                currentSection = new Section();
                currentSection.setStart(tickData);
            } else if(CheckNextPointsSMALLERThanThreshold(data,tickData,CURVETHRESHOLD,NUMBEROFPOINTSCONSIDERED) && curveStarted && currentSection.getType() == Section.SectionType.UNDEFINED){
                curveStarted = false;
                currentSection.setEnd(tickData);
                currentSection.setTimeTaken();
                TickData median = calculateMedian(curveTicks);
                currentSection.setMedian(median);
                if(curveTicks.size()<MINCURVEPOINTS){
                    currentSection.setType(Section.SectionType.INVALID);
                } else{
                    currentSection.calculateCurveType();
                }
                sections.add(currentSection);
                currentSection = new Section();
                currentSection.setStart(tickData);
                currentSection.setType(Section.SectionType.STRAIGHT);
            }
            if(curveStarted && Math.abs(tickData.getAccX()) >= CURVETHRESHOLD){
                curveTicks.add(tickData);
            }
        }
        currentSection.setEnd(data.get(data.size()-1));
        sections.add(currentSection);
        return sections;
    }

    private static TickData calculateMedian(ArrayList<TickData> curveData){
        TickData result = new TickData();
        float addedX = 0.0f;
        float addedY = 0.0f;
        for (TickData data : curveData) {
            addedX+=data.getAccX();
            addedY+=data.getAccY();
        }

        result.setTimeStamp((curveData.get(0).getTimeStamp()+curveData.get(curveData.size()-1).getTimeStamp())/2);
        result.setAccX(addedX/curveData.size());
        result.setAccY(addedY/curveData.size());

        return result;
    }

    private static boolean CheckNextPointsGREATERThanThreshold(ArrayList<TickData> dataSet, TickData origin, float threshold, int checkRange){
        int originIndex = dataSet.indexOf(origin);
        if(originIndex+checkRange >= dataSet.size())
            return false;
        for (int i = 0; i < checkRange; i++) {
            if(Math.abs(dataSet.get(originIndex+i).getAccX())<threshold)
             return false;
        }
        return true;
    }
    private static boolean CheckNextPointsSMALLERThanThreshold(ArrayList<TickData> dataSet, TickData origin, float threshold, int checkRange){
        int originIndex = dataSet.indexOf(origin);
        if(originIndex+checkRange >= dataSet.size())
            return false;
        for (int i = 0; i < checkRange; i++) {
            if(Math.abs(dataSet.get(originIndex+i).getAccX())>threshold)
             return false;
        }
        return true;
    }
}
