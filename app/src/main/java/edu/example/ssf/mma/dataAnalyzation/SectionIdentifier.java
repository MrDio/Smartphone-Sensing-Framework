package edu.example.ssf.mma.dataAnalyzation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.example.ssf.mma.model.Lap;
import edu.example.ssf.mma.model.Section;
import edu.example.ssf.mma.model.SectionType;
import edu.example.ssf.mma.model.TickData;

public class SectionIdentifier {
    private static HashMap<Integer, TickData> pointsUnderThreshold = new HashMap<>();
    private static HashMap<Integer, TickData> pointsOverThreshold = new HashMap<>();
    private static TreeMap<Integer, Section> SectionList = new TreeMap<>();
    private static final float CURVETHRESHOLD = 3.5f;

    private static ArrayList<Section> identifySections(ArrayList<TickData> data) {
        pointsOverThreshold = new HashMap<>();
        pointsUnderThreshold = new HashMap<>();
        SectionList = new TreeMap<>();

        ArrayList<Section> sections = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (Math.abs(data.get(i).getAccX()) > CURVETHRESHOLD)
                pointsOverThreshold.put(i, data.get(i));
            else {
                pointsUnderThreshold.put(i, data.get(i));
            }

        }
        putLonelyPointsFromUpperBorderIntoLower();
        SectionList = createSectionsInLowerDataset(SectionList, pointsUnderThreshold);
        SectionList = createSectionsInUpperDataset(SectionList, pointsOverThreshold);

        for (Map.Entry<Integer, Section> entry : SectionList.entrySet()) {
            sections.add(entry.getValue());
        }

        return sections;
    }

    private static TreeMap<Integer, Section> createSectionsInLowerDataset(TreeMap<Integer, Section> sectionMap, HashMap<Integer, TickData> lowerBoundDataset) {
        int i = Collections.min(lowerBoundDataset.keySet());
        int max = Collections.max(lowerBoundDataset.keySet());

        while (i < max) {
            Section section = new Section();
            ArrayList<TickData> temporaryList = new ArrayList<>();
            temporaryList.add(lowerBoundDataset.get(i));
            int key = i;
            int counter = 0;
            while (lowerBoundDataset.containsKey(i) && counter < 10) {
                temporaryList.add(lowerBoundDataset.get(i));
                i++;
                counter++;
            }

            section.setStart(temporaryList.get(0));
            section.setEnd(temporaryList.get(temporaryList.size() - 1));
            section.setMedian(calculateMedian(temporaryList));
            sectionMap.put(key, section);
            while (!lowerBoundDataset.containsKey(i) && i < max) {
                i++;
            }
        }
        return sectionMap;
    }

    private static TreeMap<Integer, Section> createSectionsInUpperDataset(TreeMap<Integer, Section> sectionMap, HashMap<Integer, TickData> upperBoundDataset) {
        if (upperBoundDataset.size() != 0) {
            int i = Collections.min(upperBoundDataset.keySet());
            int max = Collections.max(upperBoundDataset.keySet());
            while (i < max) {
                Section section = new Section();
                ArrayList<TickData> temporaryList = new ArrayList<>();
                if (pointsUnderThreshold.containsKey(i - 1)) {
                    temporaryList.add(pointsUnderThreshold.get(i - 1));
                }
                int key = i - 1;
                while (upperBoundDataset.containsKey(i)) {
                    temporaryList.add(upperBoundDataset.get(i));
                    i++;
                }

                section.setStart(temporaryList.get(0));
                if (pointsUnderThreshold.containsKey(i)) {
                    section.setEnd(pointsUnderThreshold.get(i));
                } else {
                    section.setEnd(temporaryList.get(temporaryList.size() - 1));
                }
                section.setMedian(calculateMedian(temporaryList));
                sectionMap.put(key, section);
                while (!upperBoundDataset.containsKey(i) && i < max) {
                    i++;
                }
            }
            return sectionMap;
        }
        return sectionMap;
    }

    private static void putLonelyPointsFromUpperBorderIntoLower() {
        List<Integer> toRemove = new ArrayList<>();
        for (Map.Entry<Integer, TickData> entry : pointsOverThreshold.entrySet()) {
            if (CheckLonelinessOfPoint(entry)) {
                pointsUnderThreshold.put(entry.getKey(), entry.getValue());
                toRemove.add(entry.getKey());
            }
        }
        for (Integer key : toRemove) {
            pointsOverThreshold.remove(key);
        }
    }

    private static boolean CheckLonelinessOfPoint(Map.Entry<Integer, TickData> entry) {
        if (entry.getKey() > 0 && entry.getKey() < pointsOverThreshold.size() - 1) {
            if (!pointsOverThreshold.containsKey(entry.getKey() - 1) && !pointsOverThreshold.containsKey(entry.getKey() + 1))
                return true;
        } else if (entry.getKey() == 0 && !pointsOverThreshold.containsKey(entry.getKey() + 1)) {
            return true;
        } else if (entry.getKey() == pointsOverThreshold.size() - 1 && !pointsOverThreshold.containsKey(entry.getKey() - 1)) {
            return true;
        }
        return false;
    }

    private static TickData calculateMedian(ArrayList<TickData> curveData) {
        TickData result = new TickData();
        float addedX = 0.0f;
        float addedY = 0.0f;
        int counter = 2;
        for (TickData data : curveData) {
            if (Math.abs(data.getAccX()) > CURVETHRESHOLD) {

                addedX += data.getAccX();
                counter++;
            }

            addedY += data.getAccY();
        }

        result.setTimeStamp((curveData.get(0).getTimeStamp() + curveData.get(curveData.size() - 1).getTimeStamp()) / 2);
        if (counter < 2) {
            result.setAccX(0.0f);
        } else {
            result.setAccX(addedX / counter);
        }

        result.setAccY(addedY / counter);

        return result;
    }

    public static ArrayList<Lap> createSections(ArrayList<Lap> mLaps) {
        System.out.println("<<<<AFTER IDENTIFYING>>>>>");
        for (Lap lap : mLaps) {

            ArrayList<Section> sections = SectionIdentifier.identifySections(lap.getRawData());
            System.out.println("Lap " + lap.getNumber() + " Sections: " + sections.size());
            for (Section section : sections) {
                System.out.println("Section: Start: " + section.getStart().getTimeStamp() + " | End: " + section.getEnd().getTimeStamp());
                double start = section.getStart().getTimeStamp();
                double end = section.getEnd().getTimeStamp();
                double vertex = start + ((end - start) / 2);
                double median = section.getMedian().getAccX();
                double area = 0.0;
                for (double i = start; i <= end; i++) {
                    if (start == end) {
                        break;
                    }
                    double b = -1 * (Math.pow(((start + end) / 2.0), 2.0)) + (start * end);
                    double a = median / b;
                    area += a * (Math.pow(i - vertex, 2)) + median;
                }
                section.setForceToVehicle(area);
            }

            lap.setSections(sections);

        }
        return mLaps;
    }

    public static ArrayList<Lap> classifySections(ArrayList<Lap> mLaps) {
        System.out.println("<<<<SECTIONS WITH AREA ATTACHED>>>>>");
        for (Lap lap : mLaps) {
            System.out.println("Lap " + lap.getNumber() + " Sections: " + lap.getSections().size());
            ArrayList<Section> sections = lap.getSections();
            ArrayList<Section> newSections = new ArrayList<>();
            boolean possibleMerge = false;
            Section sectionToMerge = null;

            for (int i = 0; i < sections.size(); i++) {
                System.out.println("Section: Start: " + sections.get(i).getStart().getTimeStamp() + " | End: " + sections.get(i).getEnd().getTimeStamp() + " | Area: " + sections.get(i).getForceToVehicle());
                if (Math.abs(sections.get(i).getForceToVehicle()) < 900.0) {
                    if (possibleMerge == true) {
                        sectionToMerge = mergeSection(sectionToMerge, sections.get(i));
                    } else {
                        possibleMerge = true;
                        sectionToMerge = sections.get(i);
                    }
                } else {
                    if (possibleMerge == true) {
                        sectionToMerge.setType(SectionType.STRAIGHT);
                        newSections.add(sectionToMerge);
                        sectionToMerge = null;
                    }
                    possibleMerge = false;
                    if (sections.get(i).getForceToVehicle() < 0) {
                        sections.get(i).setType(SectionType.LEFTCURVE);
                    } else {
                        sections.get(i).setType(SectionType.RIGHTCURVE);
                    }
                    newSections.add(sections.get(i));
                }
            }
            if (sectionToMerge != null) {
                newSections.add(sectionToMerge);
            }
            lap.setSections(newSections);


        }
        return mLaps;

    }

    public static ArrayList<Lap> invalidateLaps(ArrayList<Lap> mLaps) {
        HashMap<Integer, Integer> amountOfSectionsCount = new HashMap<>();

        for (Lap lap : mLaps) {
            if (amountOfSectionsCount.containsKey(lap.getSections().size())) {
                int count = amountOfSectionsCount.get(lap.getSections().size());
                amountOfSectionsCount.put(lap.getSections().size(), count + 1);
            } else {
                amountOfSectionsCount.put(lap.getSections().size(), 1);
            }
        }

        Map.Entry<Integer, Integer> maxEntry = null;

        for (Map.Entry<Integer, Integer> entry : amountOfSectionsCount.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        for (Lap lap : mLaps) {
            if (lap.getSections().size() != maxEntry.getKey()) {
                lap.setValid(false);
            }
        }

        return mLaps;

    }

    private static Section mergeSection(Section start, Section end) {
        Section mergedSection = new Section();
        mergedSection.setType(SectionType.STRAIGHT);
        mergedSection.setStart(start.getStart());
        mergedSection.setEnd(end.getEnd());
        mergedSection.setMedian(new TickData());
        return mergedSection;
    }
}
