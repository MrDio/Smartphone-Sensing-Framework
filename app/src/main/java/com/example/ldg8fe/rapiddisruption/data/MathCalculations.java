package com.example.ldg8fe.rapiddisruption.data;

import java.text.SimpleDateFormat;

/**
 * Created by LDG8FE on 05.12.2017.
 */

public class MathCalculations {

    public static double calculatePythagoras(float x, float y, float z){
        return Math.sqrt(x*x+y*y+z*z);
    }
    public static double pow(double x, int exp){
        return Math.pow(x,exp);
    }
    public static String convertDate(long dateInMilliseconds, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(dateInMilliseconds);
    }

    public static double getDB(Integer ampli){

        int x = ampli;
        double x2 = x;
        double db = (20 * Math.log10(x2 / 0.1));
        if(db>0)
        {
            return db;
        }
        else
        {
            return 0;
        }
    }
}
