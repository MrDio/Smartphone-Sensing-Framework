package edu.example.ssf.mma.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SectionIdentifier {
    private static HashMap<Integer,TickData> pointsUnderThreshold = new HashMap<>();
    private static HashMap<Integer,TickData> pointsOverThreshold = new HashMap<>();
    private static TreeMap<Integer,Section> SectionList = new TreeMap<>();

    private static final float CURVETHRESHOLD = 1f;

    public static ArrayList<Section> identifySections(ArrayList<TickData> data){
        ArrayList<Section> sections = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if(Math.abs(data.get(i).getAccX())>CURVETHRESHOLD)
                pointsOverThreshold.put(i,data.get(i));
            else{
                pointsUnderThreshold.put(i,data.get(i));
            }

        }
        putLonelyPointsFromUpperBorderIntoLower();
        SectionList = createSectionsInLowerDataset(SectionList,pointsUnderThreshold);
        SectionList = createSectionsInUpperDataset(SectionList,pointsOverThreshold);

        for (Map.Entry<Integer,Section> entry: SectionList.entrySet()) {
            sections.add(entry.getValue());
        }

        return sections;
    }
    private static TreeMap<Integer,Section> createSectionsInLowerDataset(TreeMap<Integer,Section> sectionMap, HashMap<Integer,TickData> lowerBoundDataset){
        int i = Collections.min(lowerBoundDataset.keySet());
        int max = Collections.max(lowerBoundDataset.keySet());

        while (i < max) {
            Section section = new Section();
            ArrayList<TickData> temporaryList = new ArrayList<>();
            temporaryList.add(lowerBoundDataset.get(i));
            int key = i;
            while(lowerBoundDataset.containsKey(i)){
                temporaryList.add(lowerBoundDataset.get(i));
                i++;
            }

            section.setStart(temporaryList.get(0));
            section.setEnd(temporaryList.get(temporaryList.size()-1));
            section.setMedian(calculateMedian(temporaryList));
            sectionMap.put(key,section);
            while(!lowerBoundDataset.containsKey(i) && i<max){
                i++;
            }
        }
        return sectionMap;
    }

    private static TreeMap<Integer,Section> createSectionsInUpperDataset(TreeMap<Integer,Section> sectionMap, HashMap<Integer,TickData> upperBoundDataset){
        int i = Collections.min(upperBoundDataset.keySet());
        int max = Collections.max(upperBoundDataset.keySet());
        while (i < max) {
            Section section = new Section();
            ArrayList<TickData> temporaryList = new ArrayList<>();
            temporaryList.add(pointsUnderThreshold.get(i-1));
            int key = i-1;
            while(upperBoundDataset.containsKey(i)){
                temporaryList.add(upperBoundDataset.get(i));
                i++;
            }

            section.setStart(temporaryList.get(0));
            if(pointsOverThreshold.get(i)!= null){
                section.setEnd(pointsUnderThreshold.get(i));
            } else{
                section.setEnd(temporaryList.get(temporaryList.size()-1));
            }
            section.setMedian(calculateMedian(temporaryList));
            sectionMap.put(key,section);
            while(!upperBoundDataset.containsKey(i) && i<max){
                i++;
            }
        }
        return sectionMap;
    }

    private static void putLonelyPointsFromUpperBorderIntoLower(){
        List<Integer> toRemove = new ArrayList<>();
        for (Map.Entry<Integer,TickData> entry: pointsOverThreshold.entrySet()) {
            if(CheckLonelinessOfPoint(entry)){
                pointsUnderThreshold.put(entry.getKey(),entry.getValue());
                toRemove.add(entry.getKey());
            }
        }
        for (Integer key: toRemove) {
            pointsOverThreshold.remove(key);
        }
    }

    private static boolean CheckLonelinessOfPoint(Map.Entry<Integer,TickData> entry){
        if(entry.getKey() > 0 && entry.getKey()<pointsOverThreshold.size()-1){
            if(!pointsOverThreshold.containsKey(entry.getKey()-1) && !pointsOverThreshold.containsKey(entry.getKey()+1))
                return true;
        } else if(entry.getKey()==0 && !pointsOverThreshold.containsKey(entry.getKey()+1))
        {
            return true;
        }else if(entry.getKey()==pointsOverThreshold.size()-1 && !pointsOverThreshold.containsKey(entry.getKey()-1))
        {
            return true;
        }
        return false;
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
}
