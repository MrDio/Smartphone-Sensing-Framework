/*
 *  This file is part of the Multimodal Mobility Analyser(MMA), based
 *  on the Smartphone Sensing Framework (SSF)

    MMA (also SSF) is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MMA (also SSF) is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.example.ssf.mma.data;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * This class contains several mathematical calculations --> class methods
 *
 * @author D. Lagamtzis
 * @version 1.0
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
        double db = (20 * Math.log10((double) x / 0.1));
        return db > 0 ? db : 0;
    }
}
