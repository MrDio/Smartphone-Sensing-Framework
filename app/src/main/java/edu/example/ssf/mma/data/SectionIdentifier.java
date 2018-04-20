package edu.example.ssf.mma.data;

import java.util.ArrayList;

public class SectionIdentifier {
    private static Section currentSection;
    private static ArrayList<TickData> temporarySectionPoints = new ArrayList<>();
    private static boolean curveStarted = false;
    private static final float CURVETHRESHOLD = 1f;
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
        currentSection.setType(Section.SectionType.UNDEFINED);
        for (TickData tickData : data) {
            if(Math.abs(tickData.getAccX()) >= CURVETHRESHOLD && !curveStarted && temporarySectionPoints.size() >= 3){
                curveStarted = true;
                currentSection.setEnd(tickData);
                currentSection.setTimeTaken();
                TickData median = calculateMedian(temporarySectionPoints);
                currentSection.setMedian(median);
                sections.add(currentSection);
                currentSection = new Section();
                currentSection.setStart(tickData);
            } else if(curveStarted && Math.abs(tickData.getAccX()) <= CURVETHRESHOLD && temporarySectionPoints.size() >= 3){
                curveStarted = false;
                currentSection.setEnd(tickData);
                currentSection.setTimeTaken();
                TickData median = calculateMedian(temporarySectionPoints);
                currentSection.setMedian(median);
                sections.add(currentSection);
                currentSection = new Section();
                currentSection.setStart(tickData);
                currentSection.setType(Section.SectionType.STRAIGHT);
                temporarySectionPoints = new ArrayList<>();
            }
            temporarySectionPoints.add(tickData);
        }
        currentSection.setEnd(data.get(data.size()-1));
        currentSection.setTimeTaken();
        TickData median = calculateMedian(temporarySectionPoints);
        currentSection.setMedian(median);
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
