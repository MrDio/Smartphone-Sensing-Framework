package edu.example.ssf.mma.data;

import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SectionIdentifier {
    private static HashMap<Integer,TickData> pointsUnderThreshold = new HashMap<>();
    private static HashMap<Integer,TickData> pointsOverThreshold = new HashMap<>();
    private static TreeMap<Integer,Section> SectionList = new TreeMap<>();
    private static ArrayList<Lap> mLaps = new ArrayList<>();
    private static final int SMOOTHINGPOINTAMOUNT = 5;
    private static final float CURVETHRESHOLD = 3.5f;

    public static void initialize(ArrayList<Lap> laps){
        mLaps = laps;
    }

    public static ArrayList<Section> identifySections(ArrayList<TickData> data){
        pointsOverThreshold = new HashMap<>();
        pointsUnderThreshold = new HashMap<>();
        SectionList = new TreeMap<>();
        
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
            int counter = 0;
            while(lowerBoundDataset.containsKey(i) && counter < 10){
                temporaryList.add(lowerBoundDataset.get(i));
                i++;
                counter++;
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
            if(pointsUnderThreshold.containsKey(i-1)){
                temporaryList.add(pointsUnderThreshold.get(i-1));
            }
            int key = i-1;
            while(upperBoundDataset.containsKey(i)){
                temporaryList.add(upperBoundDataset.get(i));
                i++;
            }

            section.setStart(temporaryList.get(0));
            if(pointsUnderThreshold.containsKey(i)){
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
        int counter = 2;
        for (TickData data : curveData) {
            if(Math.abs(data.getAccX()) > CURVETHRESHOLD){

                addedX+=data.getAccX();
                counter++;
            }

            addedY+=data.getAccY();
        }

        result.setTimeStamp((curveData.get(0).getTimeStamp()+curveData.get(curveData.size()-1).getTimeStamp())/2);
        if(counter < 2){
            result.setAccX(0.0f);
        }
        else{
            result.setAccX(addedX/counter);
        }

        result.setAccY(addedY/counter);

        return result;
    }

    public static void applySavitzkyGolay(){
        for (Lap lap : mLaps) {
            ArrayList<TickData> newTickDataList = new ArrayList();
            CsvFileWriter.crtFile("SG_" + lap.getNumber() + "_" + new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss'.csv'").format(new Date()));
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
                CsvFileWriter.writeLine(""+lap.getNumber(),""+x.getTimeStamp(),""+x.getAccX(),""+x.getAccY());
            }
            lap.setRawData(newTickDataList);
            CsvFileWriter.closeFile();
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
            CsvFileWriter.crtFile("S_" + lap.getNumber() + "_" + new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss'.csv'").format(new Date()));
            newTickDataList.add(lap.getRawData().get(0));
            for (List<TickData> data : paritionedTicks) {
                TickData smoothedData = combineTickDatas(data);
                newTickDataList.add(smoothedData);
            }
            newTickDataList.add(lap.getRawData().get(lap.getRawData().size()-1));
            for (TickData x : newTickDataList) {
                CsvFileWriter.writeLine(""+lap.getNumber(),""+x.getTimeStamp(),""+x.getAccX(),""+x.getAccY());
            }
            lap.setRawData(newTickDataList);
            CsvFileWriter.closeFile();
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

    public static ArrayList<Lap> createSections(){
        System.out.println("<<<<AFTER IDENTIFYING>>>>>");
        for (Lap lap : mLaps){

            ArrayList<Section> sections = SectionIdentifier.identifySections(lap.getRawData());
            System.out.println("Lap "+lap.getNumber()+" Sections: "+sections.size());
            for (Section section : sections){
                System.out.println("Section: Start: "+section.getStart().getTimeStamp()+" | End: "+section.getEnd().getTimeStamp());
                double start = section.getStart().getTimeStamp();
                double end = section.getEnd().getTimeStamp();
                double vertex = start + ((end - start) / 2);
                double median = section.getMedian().getAccX();
                double area = 0.0;
                for(double i = start; i <= end; i++){
                    if(start == end){
                        break;
                    }
                    double b = -1 * (Math.pow(((start + end) / 2.0), 2.0)) + (start * end);
                    double a = median/b;
                    area += a * (Math.pow(i-vertex, 2)) + median;
                }
                section.setForceToVehicle(area);
            }

            lap.setSections(sections);

        }
        classifySections();
        return mLaps;
    }

    private static void classifySections(){
        System.out.println("<<<<SECTIONS WITH AREA ATTACHED>>>>>");
        for (Lap lap : mLaps){
            System.out.println("Lap "+lap.getNumber()+" Sections: "+lap.getSections().size());
            ArrayList<Section> sections = lap.getSections();
            ArrayList<Section> newSections = new ArrayList<>();
            boolean possibleMerge = false;
            Section sectionToMerge = null;

            for(int i = 0; i < sections.size(); i++){
                System.out.println("Section: Start: "+sections.get(i).getStart().getTimeStamp()+" | End: "+sections.get(i).getEnd().getTimeStamp()+" | Area: "+sections.get(i).getForceToVehicle());
                if(Math.abs(sections.get(i).getForceToVehicle()) < 900.0 ){
                    if(possibleMerge == true){
                        sectionToMerge = mergeSection(sectionToMerge, sections.get(i));
                    }
                    else{
                        possibleMerge = true;
                        sectionToMerge = sections.get(i);
                    }
                }
                else{
                    if(possibleMerge == true){
                        sectionToMerge.setType(Section.SectionType.STRAIGHT);
                        newSections.add(sectionToMerge);
                        sectionToMerge = null;
                    }
                    possibleMerge = false;
                    if(sections.get(i).getForceToVehicle() < 0){
                        sections.get(i).setType(Section.SectionType.LEFTCURVE);
                    }
                    else{
                        sections.get(i).setType(Section.SectionType.RIGHTCURVE);
                    }
                    newSections.add(sections.get(i));
                }
            }
            if(sectionToMerge != null){
                newSections.add(sectionToMerge);
            }


            lap.setSections(newSections);


        }

        invalidateLaps();
        System.out.println("<<<<AFTER MERGING>>>>>");
        for(Lap lap : mLaps){
            System.out.println("Lap "+lap.getNumber()+" Sections: "+lap.getSections().size());
            for(int i = 0; i < lap.getSections().size(); i++){
                System.out.println("Section: Start: "+lap.getSections().get(i).getStart().getTimeStamp()+" | End: "+lap.getSections().get(i).getEnd().getTimeStamp()+" | Type: "+lap.getSections().get(i).getType());
            }
        }
    }

    private static void invalidateLaps(){
        HashMap<Integer, Integer> amountOfSectionsCount = new HashMap<>();

        for(Lap lap : mLaps){
            if(amountOfSectionsCount.containsKey(lap.getSections().size())){
                int count = amountOfSectionsCount.get(lap.getSections().size());
                amountOfSectionsCount.put(lap.getSections().size(), count+1);
            }
            else{
                amountOfSectionsCount.put(lap.getSections().size(), 1);
            }
        }

        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : amountOfSectionsCount.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        for(Lap lap : mLaps){
            if(lap.getSections().size() != maxEntry.getKey()) {
                lap.setPerformanceIndicator(-1);
            }
        }



    }

    private static Section mergeSection(Section start, Section end){
        Section mergedSection = new Section();
        mergedSection.setStart(start.getStart());
        mergedSection.setEnd(end.getEnd());
        mergedSection.setStart(start.getStart());
        mergedSection.setEnd(end.getEnd());
        mergedSection.setTimeTaken();
        return mergedSection;
    }
}
