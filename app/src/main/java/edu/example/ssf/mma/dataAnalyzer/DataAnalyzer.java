package edu.example.ssf.mma.dataAnalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import edu.example.ssf.mma.characters.Characters;

/**
 * Created by MatzeG on 19.04.2018.
 */

public class DataAnalyzer {
    Map<String, Double> charLimits;

    {
        charLimits = new TreeMap<String, Double>() {{
            put("A", 10.0);
            put("B", 8.0);
            put("C", 3.0);
            put("O", 4.0);
        }};
    }

    public String compareData(List<Double> dataList, String learnValue) {
        String ret = "";
        Map<String, Double> res = calculateAll(dataList);
        if (learnValue != null) {
            double resVal = res.get(learnValue);
            if (resVal < charLimits.get(learnValue)) {
                Map.Entry<String, Double> smallestEntry = getSmallest(res);
                if (smallestEntry.getKey().equals(learnValue)) {
                    ret = "\""+learnValue + "\" was correct";
                } else {
                    double tempVal = smallestEntry.getValue();
                    if (tempVal <= charLimits.get(smallestEntry.getKey())) {
                        ret = "\""+learnValue + "\" could not be detected";
                    } else {
                        ret = "\""+learnValue + "\" was correct";
                    }
                }
            } else {
                ret = "\""+learnValue + "\" could not be detected";
            }
        } else {
            List<Map.Entry<String, Double>> list = new ArrayList<>(res.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
                public int compare(Map.Entry<String, Double> o1,
                                   Map.Entry<String, Double> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
            ret+="Probability:\r\n";
            for (Iterator<Map.Entry<String,Double>> iterator =list.iterator(); iterator.hasNext();) {
                Map.Entry<String ,Double> entry=iterator.next();
                ret+=entry.getKey();
                if (iterator.hasNext()){
                    ret+=" > ";
                }
            }
        }
        return ret;
    }

    private double calculateSmallestMSE(List<Double> dataList, String learnValue) {
        double ret = Double.MAX_VALUE;
        List<List<Double>> preparedData = Characters.chars.get(learnValue);
        for (List<Double> onePreparedList : preparedData) {
            int counter = 0;
            double temp = 0;
            for (Double onePreparedValue : onePreparedList) {
                if (dataList.size() == counter) {
                    break;
                }
                temp += Math.pow(dataList.get(counter) - onePreparedValue, 2);
                counter++;
            }
            double mSE = (1.0 / onePreparedList.size()) * temp;
            if (mSE < ret) {
                ret = mSE;
            }
        }
        return ret;
    }

    public Map<String, Double> calculateAll(List<Double> dataList) {
        Map<String, Double> returnMap = new TreeMap<>();
        for (Iterator<String> iterator = Characters.chars.keySet().iterator(); iterator.hasNext(); ) {
            String temp = iterator.next();
            double mSE = calculateSmallestMSE(dataList, temp);
            returnMap.put(temp, mSE);
        }
        return returnMap;
    }


    public Map.Entry<String, Double> getSmallest(Map<String, Double> result) {
        double temp = Double.MAX_VALUE;
        Map.Entry<String, Double> smallest = null;
        for (Map.Entry<String, Double> entry : result.entrySet()) {
            if (entry.getValue() < temp) {
                smallest = entry;
                temp = entry.getValue();
            }
        }
        return smallest;
    }

}
