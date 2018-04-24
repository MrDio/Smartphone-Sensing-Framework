package edu.example.ssf.mma.dataAnalyzation;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nullable;

import edu.example.ssf.mma.data.DataModification;
import edu.example.ssf.mma.model.CurveGrade;
import edu.example.ssf.mma.model.Lap;
import edu.example.ssf.mma.model.Section;
import edu.example.ssf.mma.model.SectionData;
import edu.example.ssf.mma.model.SectionPerformance;
import edu.example.ssf.mma.model.SectionSpeed;
import edu.example.ssf.mma.model.SectionType;

public class PerformanceAnalyzer {

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
                        currentSection.setCurveGrade(calculateCurveGrade(fastestSectionData,currentSectionData));
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

    private static CurveGrade calculateCurveGrade(SectionData fastestSection, SectionData currentSection){
        if(fastestSection.getNextSectionTime()!= null && currentSection.getNextSectionTime() != null){
            if(currentSection.getSectionTime() > fastestSection.getSectionTime() && currentSection.getSectionForces() > fastestSection.getSectionForces() && currentSection.getNextSectionTime()>fastestSection.getNextSectionTime()){
                return CurveGrade.BAD;
            }
            if(currentSection.getSectionTime() < fastestSection.getSectionTime() && currentSection.getNextSectionTime() < fastestSection.getNextSectionTime() && currentSection.getSectionForces() > fastestSection.getSectionForces()){
                return CurveGrade.PERFECT;
            }
            if(currentSection.getSectionTime() > fastestSection.getSectionTime() && currentSection.getNextSectionTime() > fastestSection.getNextSectionTime() && currentSection.getSectionForces() < fastestSection.getSectionForces()){
                return CurveGrade.BAD;
            }
            if(currentSection.getSectionTime() > fastestSection.getSectionTime() && currentSection.getNextSectionTime() < fastestSection.getNextSectionTime()){
                return CurveGrade.GOOD;
            }
        } else{
            if(currentSection.getSectionForces() > fastestSection.getSectionForces()){
                return CurveGrade.GOOD;
            } else if(currentSection.getSectionForces() < fastestSection.getSectionForces()){
                return CurveGrade.BAD;
            }
        }
        return CurveGrade.NEUTRAL;
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
        Map<Integer,Float> BestThreshold = new HashMap<>();
        ArrayList<Float> thresholdCasesToTest = new ArrayList<>();

        int maxThreshold = 2500;
        int initThreshold = 900;

        for (int i = initThreshold; i <= maxThreshold; i+=100) {
            thresholdCasesToTest.add((float)i);
        }
        for (float threshold: thresholdCasesToTest){
            sectionIdentification(laps, threshold);

            int validCount = 0;
            for (Lap lap : laps){
                if(lap.isValid()){
                    validCount++;
                }
            }

            if(!BestThreshold.containsKey(validCount)){
                BestThreshold.put(validCount, threshold);
            }

        }
        int maxKey = Collections.max(BestThreshold.keySet());
        sectionIdentification(laps,BestThreshold.get(maxKey));
    }

    private static ArrayList<Lap> sectionIdentification(ArrayList<Lap> laps, float threshold) {

        SectionIdentifier.setFORCETHRESHOLD(threshold);

        laps = SectionIdentifier.createSections(laps);
        laps = SectionIdentifier.classifySections(laps);
        laps = SectionIdentifier.invalidateLaps(laps);

        return laps;
    }
}
