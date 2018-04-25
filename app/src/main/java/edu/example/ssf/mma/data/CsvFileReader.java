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

import edu.example.ssf.mma.config.ConfigApp;
import edu.example.ssf.mma.model.Lap;
import edu.example.ssf.mma.model.TickData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
            inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ ConfigApp.targetStorageDir, ConfigApp.currentLapFile);
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