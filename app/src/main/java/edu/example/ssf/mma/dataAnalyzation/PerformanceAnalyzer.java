package edu.example.ssf.mma.dataAnalyzation;



import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.example.ssf.mma.config.ConfigApp;
import edu.example.ssf.mma.model.Lap;
import edu.example.ssf.mma.model.Section;
import edu.example.ssf.mma.model.SectionData;
import edu.example.ssf.mma.model.SectionPerformance;
import edu.example.ssf.mma.model.SectionSpeed;
import edu.example.ssf.mma.model.SectionType;

public class PerformanceAnalyzer {

    private final static int MAXFORCETHRESHOLD = ConfigApp.forceThresholdUpperBound;
    private final static int MINFORCETHRESHOLD = ConfigApp.forceThresholdLowerBound;
    private final static float MINCURVETHRESHOLD = ConfigApp.curveThresholdLowerBound;
    private final static float MAXCURVETHRESHOLD = ConfigApp.curveThresholdUpperBound;

    public static void calculatePerformanceIndicator(ArrayList<Lap> mLaps){
        Lap fastestLap = null;
        TreeMap<Integer, Section> fastestlapSections = new TreeMap<>();
        for (Lap lap: mLaps) {
            if(lap.isValid()){
                if(fastestLap == null || fastestLap.getRoundTime() > lap.getRoundTime()){
                    fastestLap = lap;
                }
            }
        }
        fastestLap.setFastestLap(true);
        for (int i = 0; i <fastestLap.getSections().size() ; i++) {
            fastestlapSections.put(i,fastestLap.getSections().get(i));
        }


        for (Map.Entry<Integer, Section> sectionEntry: fastestlapSections.entrySet()){
            Double fastestNextSectionTime;
            Float fastestNextSectionForces;
            if(sectionEntry.getKey()<fastestlapSections.size()-1){
                fastestNextSectionTime = fastestlapSections.get(sectionEntry.getKey()+1).getTimeTaken();
                fastestNextSectionForces = fastestlapSections.get(sectionEntry.getKey()+1).getMedian().getAccX();
            } else{
                fastestNextSectionTime = null;
                fastestNextSectionForces = null;
            }
            SectionData fastestSectionData = new SectionData(sectionEntry.getValue().getTimeTaken(),fastestNextSectionTime,sectionEntry.getValue().getMedian().getAccX(),fastestNextSectionForces);
            for (Lap lap: mLaps){
                if(!lap.equals(fastestLap) && lap.isValid()){
                    ArrayList<Section> sectionListForCurrentLap = lap.getSections();
                    Section currentSection = sectionListForCurrentLap.get(sectionEntry.getKey());
                    Double nextSectionTime;
                    Float nextSectionForces;
                    if(sectionEntry.getKey()<fastestlapSections.size()-1){
                        nextSectionTime = sectionListForCurrentLap.get(sectionEntry.getKey()+1).getTimeTaken();
                        nextSectionForces = sectionListForCurrentLap.get(sectionEntry.getKey()+1).getMedian().getAccX();
                    } else{
                        nextSectionTime = null;
                        nextSectionForces = null;
                    }
                    SectionData currentSectionData = new SectionData(sectionListForCurrentLap.get(sectionEntry.getKey()).getTimeTaken(),nextSectionTime,sectionListForCurrentLap.get(sectionEntry.getKey()).getMedian().getAccX(),nextSectionForces);
                    if(sectionEntry.getValue().getType() == SectionType.STRAIGHT){
                        currentSection.setSectionSpeed(calculateSpeedStraight(fastestSectionData,currentSectionData));
                    } else{
                        currentSection.setSectionSpeed(calculateSpeedCurve(fastestSectionData,currentSectionData));
                    }
                    currentSection.setSectionPerformance(determineSectionPerformance(currentSection.getSectionSpeed()));
                }
            }
        }
    }

    private static SectionSpeed calculateSpeedStraight(SectionData fastestSection, SectionData currentSection){
        if(fastestSection.getNextSectionTime()!= null && currentSection.getNextSectionTime() != null){
            if(currentSection.getSectionTime()<fastestSection.getSectionTime() && currentSection.getNextSectionTime() > fastestSection.getNextSectionTime()){
                return SectionSpeed.TOOFAST;
            }
            if(currentSection.getSectionTime()<fastestSection.getSectionTime() && currentSection.getNextSectionTime() < fastestSection.getNextSectionTime()){
                return SectionSpeed.FAST;
            }
            if(currentSection.getSectionTime()>fastestSection.getSectionTime() && currentSection.getNextSectionTime() > fastestSection.getNextSectionTime()){
                return SectionSpeed.SLOW;
            }
            if(currentSection.getSectionTime() > fastestSection.getSectionTime() && currentSection.getNextSectionTime() < fastestSection.getNextSectionTime()){
                return SectionSpeed.GOOD;
            }
        } else{
            if(currentSection.getSectionTime()<fastestSection.getSectionTime()){
                return SectionSpeed.FAST;
            } else if(currentSection.getSectionTime()>fastestSection.getSectionTime()){
                return SectionSpeed.SLOW;
            }
        }
        return SectionSpeed.NOTAVAILABLE;
    }

    private static SectionSpeed calculateSpeedCurve(SectionData fastestSection, SectionData currentSection){
        if(fastestSection.getNextSectionTime()!= null && currentSection.getNextSectionTime() != null){
            if(currentSection.getSectionTime() < fastestSection.getSectionTime() && currentSection.getNextSectionTime()>fastestSection.getNextSectionTime()){
                return SectionSpeed.TOOFAST;
            }
            if(currentSection.getSectionTime() < fastestSection.getSectionTime() && currentSection.getNextSectionTime() < fastestSection.getNextSectionTime()){
                return SectionSpeed.FAST;
            }
            if(currentSection.getSectionTime() > fastestSection.getSectionTime() && currentSection.getNextSectionTime() > fastestSection.getNextSectionTime()){
                return SectionSpeed.SLOW;
            }
            if(currentSection.getSectionTime() > fastestSection.getSectionTime() && currentSection.getNextSectionTime() < fastestSection.getNextSectionTime()){
                return SectionSpeed.GOOD;
            }
        } else{
            if(currentSection.getSectionTime() > fastestSection.getSectionTime()){
                return SectionSpeed.FAST;
            } else if(currentSection.getSectionTime() < fastestSection.getSectionTime()){
                return SectionSpeed.SLOW;
            }
        }
        return SectionSpeed.NOTAVAILABLE;
    }

    private static SectionPerformance determineSectionPerformance(SectionSpeed speed){
        switch (speed){
            case GOOD:
                return SectionPerformance.NEUTRAL;
            case FAST:
                return SectionPerformance.GOOD;
            case TOOFAST:
                return SectionPerformance.BAD;
            case SLOW:
                return SectionPerformance.BAD;
            default:
                return SectionPerformance.UNDEFINED;
        }
    }

    public static void fitThreshold(ArrayList<Lap> laps){
        Map<Integer,Pair<Float,Float>> bestThreshold = new HashMap<>();
        Map<Float,ArrayList<Float>> thresholdsCasesToTest = new HashMap<>();

        for (float i = MINCURVETHRESHOLD; i <= MAXCURVETHRESHOLD ; i+=0.5) {
            ArrayList<Float> forceThresholds = new ArrayList<>();
            for (int f = MINFORCETHRESHOLD; f <= MAXFORCETHRESHOLD; f+=100) {
                forceThresholds.add((float)f);
            }
            thresholdsCasesToTest.put(i,forceThresholds);
        }
        for (Map.Entry<Float,ArrayList<Float>> entry : thresholdsCasesToTest.entrySet())
        {
            for (float threshold: entry.getValue()){
                sectionIdentification(laps, threshold, entry.getKey());

                int validCount = 0;
                for (Lap lap : laps){
                    if(lap.isValid()){
                        validCount++;
                    }
                }
                if(!bestThreshold.containsKey(validCount)){

                    bestThreshold.put(validCount, new Pair<>(entry.getKey(),threshold));
                }

            }
        }


        int maxKey = Collections.max(bestThreshold.keySet());
        sectionIdentification(laps,bestThreshold.get(maxKey).second,bestThreshold.get(maxKey).first);
    }

    private static ArrayList<Lap> sectionIdentification(ArrayList<Lap> laps, float forceThreshold, float curveThreshold) {

        SectionIdentifier.setFORCETHRESHOLD(forceThreshold);
        SectionIdentifier.setCURVETHRESHOLD(curveThreshold);

        laps = SectionIdentifier.createSections(laps);
        laps = SectionIdentifier.classifySections(laps);
        laps = SectionIdentifier.invalidateLaps(laps);

        return laps;
    }
}
