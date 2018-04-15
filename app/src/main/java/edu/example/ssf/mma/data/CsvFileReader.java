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

import android.os.Environment;
import android.util.Log;

import edu.example.ssf.mma.config.ConfigApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * The Class CsvFileReader reads the saved csv-file and gets the data from the file for the simulated sensors..
 *
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 *
 */

public class CsvFileReader{


    /** Declaration of the FileInputStream fis*/
    private static FileInputStream fis;

    /** Declaration of the FileReader fr*/
    private static FileReader fr;

    /** Declaration of the BufferedReader br*/
    private static BufferedReader br;

    /** Declaration of the String for the separator*/
    private final static String separator = ";";

    /** Declaration of the Stringarray actline*/
    private static String[] actLine;

    /** Declaration of the Index for the AccX-Value*/
    private final static Integer indexOfAccx = 2;

    /** Declaration of the Index for the AccY-Value*/
    private final static Integer indexOfAccy = 3;

    /** Declaration of the Index for the AccZ-Value*/
    private final static Integer indexOfAccz = 4;

    /** Declaration of the Index for the Microphone Amplitude*/
    private final static Integer indexOfAccVectorA = 5;

    /** Declaration of the Index for the Altitude*/
    private final static Integer indexOfGPSAlt = 6;

    /** Declaration of the Index for the Longitude*/
    private final static Integer indexOfGPSLon = 7;

    /** Declaration of the Index for the Latitude*/
    private final static Integer indexOfGPSLat = 8;

    /** Declaration of the Index for the Microphone Amplitude*/
    private final static Integer indexOfMicMaxAmpl = 9;

    /** Declaration of the Index for the RotationX-Value*/
    private final static Integer indexOfRotationX = 10;

    /** Declaration of the Index for the RotationY-Value*/
    private final static Integer indexOfRotationY = 11;

    /** Declaration of the Index for the RotationZ-Value*/
    private final static Integer indexOfRotationZ = 12;

    /** Declaration of the Index for the MagneticfieldX-Value*/
    private final static Integer indexOfMagnetoX = 13;

    /** Declaration of the Index for the MagneticfieldY-Value*/
    private final static Integer indexOfMagnetoY = 14;

    /** Declaration of the Index for the MagneticfieldZ-Value*/
    private final static Integer indexOfMagnetoZ = 15;

    /** Declaration of the Index for the Proximity-Value*/
    private final static Integer indexOfProximity = 16;

    /** Declaration of the Index for the Event-Value*/
    private final static Integer indexOfEvent = 17;

    /**Setting the flag of the read AccX to false*/
    private static Boolean readFlagAccx = false;

    /**Setting the flag of the read AccY to false*/
    private static Boolean readFlagAccy = false;

    /**Setting the flag of the read AccZ to false*/
    private static Boolean readFlagAccz = false;

    /**Setting the flag of the read Altitude to false*/
    private static Boolean readFlagGPSAlt = false;

    /**Setting the flag of the read Longitude to false*/
    private static Boolean readFlagGPSLon = false;

    /**Setting the flag of the read Latitude to false*/
    private static Boolean readFlagGPSLat = false;

    /**Setting the flag of the read Microphone Amplitude to false*/
    private static Boolean readFlagMicMaxAmpl = false;

    /**Setting the flag of the read RotationX to false*/
    private static Boolean readFlagRotationX = false;

    /**Setting the flag of the read RotationY to false*/
    private static Boolean readFlagRotationY = false;

    /**Setting the flag of the read RotationZ to false*/
    private static Boolean readFlagRotationZ = false;

    /**Setting the flag of the read RotationX to false*/
    private static Boolean readFlagMagneticX = false;

    /**Setting the flag of the read RotationY to false*/
    private static Boolean readFlagMagneticY = false;

    /**Setting the flag of the read RotationZ to false*/
    private static Boolean readFlagMagneticZ = false;

    /**Setting the flag of the read RotationZ to false*/
    private static Boolean readFlagProxy = false;

    /**Setting the flag of the read vector a to false*/
    private static  Boolean readFlagAccVectorA = false;

    /**
     * The Enum CSV_Value including all parameter saved in the csv-file .
     */
    public static enum CSV_Value{  // missing RotX, RotY, RotZ, keep in mind !
        ACCX, ACCY, ACCZ, GPSAlt, GPSLon, GPSLat, MicMaxAmpl
    }


    /**
     * reading the data from the csv-files.
     */
    public static ArrayList<Lap> readFile() {
        File inputFile;
        String tmpLine;
        Lap currentLap = new Lap();
        ArrayList<Lap> laps = new ArrayList<>();
        try {
            inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ ConfigApp.targetStorageDir, "2018-04-11_06_57_30.csv");
            fr = new FileReader(inputFile);
            fis = new FileInputStream(inputFile);
            br = new BufferedReader(fr);
            br.readLine(); // Throw first row away

            int round;
            long timestamp = 0;
            long lastTimestamp = 0;
            long lastLapTimestamp = 0;
            long firstTimestamp = 0;
            Float accX;
            Float accY;
            currentLap.setNumber(0);
            while((tmpLine = br.readLine()) != null){
                actLine = tmpLine.split(separator);
                round = Integer.parseInt(actLine[0]);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss:SSS", Locale.GERMAN);
                try {
                    Date mDate = sdf.parse(actLine[1]);
                    timestamp = mDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                accX = Float.parseFloat(actLine[2]);
                accY = Float.parseFloat(actLine[3]);
                if(firstTimestamp == 0){
                    firstTimestamp = timestamp;
                }

                TickData data = new TickData();
                if(round != currentLap.getNumber()){
                    lastLapTimestamp += lastTimestamp;
                    laps.add(currentLap);
                    currentLap = new Lap();
                    currentLap.setNumber(round);
                }
                data.setTimeStamp(timestamp-firstTimestamp-lastLapTimestamp);
                data.setAccX(accX);
                data.setAccY(accY);
                currentLap.setRoundTime(data.getTimeStamp());
                currentLap.setDataPoint(data);
                lastTimestamp = timestamp-firstTimestamp-lastLapTimestamp;
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return laps;
    }

    /**
     * Closes the file after reading is done.
     */
    public static void closeFile() {
        try {
            fis.close();
            br.close();
            fr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}