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
import android.support.annotation.IntegerRes;
import android.util.Log;

import edu.example.ssf.mma.config.ConfigApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.transform.dom.DOMLocator;


/**
 * This class is responsible for creating a new csv File, writing the data into the file and to close it,
 * if the recording has finished.
 *
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @author D. Lagamtzis
 * @version 2.0
 */

public class DataList {

    private static List<Double> dataList;

    /**
     * Constructor of the CSVFileWriter class.
     */
    private DataList() {
    }

    /**
     * If there is not already the path to store the files into created, this path will be created.
     * A new file will be created here also. The name of the new file will be the current Timestamp.
     * If the new file is created, the first line in the file will be: Count; Timestamp; AccX; AccY;
     * AccZ; GPS-Alt; GPS-Lon; GPS-Lat; MicMaxAmpl; Acc Vector a
     */
    public static void createDatalis() {
        dataList = new ArrayList<>();
    }


    /**
     * Writes the captured data in the csv file.
     *
     * @param timestamp current timestamp
     * @param accx      current Accelerometer X-Value
     * @param accy      current Accelerometer Y-Value
     * @param accz      current Accelerometer Z-Value
     * @param accVecA   = sqrt(accx+accy+accz)
     */
    public static void addData(int cnt,
                               String timestamp,
                               double accx,
                               double accy,
                               double accz,
                               double accVecA) {
        if (dataList == null) {
            return;
        }
        dataList.add(accVecA);
    }

    /**
     * closes the file after recording has finished.
     */
    public static List<Double> getDatalist() {
        return dataList;
    }
}
