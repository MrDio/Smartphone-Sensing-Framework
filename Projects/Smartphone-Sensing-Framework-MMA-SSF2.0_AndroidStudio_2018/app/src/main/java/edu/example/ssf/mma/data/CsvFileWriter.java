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

import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import be.tarsos.dsp.util.fft.FFT;
import edu.example.ssf.mma.config.ConfigApp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class is responsible for creating a new csv File, writing the data into the file and to close it,
 * if the recording has finished.
 *
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @author D. Lagamtzis
 * @version 2.0
 */

public class CsvFileWriter {

    /**
     * Declaration of the attribute for the FileWriter that is used to write into the csv file
     */
    private static FileWriter fileWriter;

    /**
     * Declaration of the String for the separator that is used to separate the incoming sensor data
     */
    private final static String separator = ";";


    public static String lastFilePath;
    private static boolean headerWasWritten;

    /**
     * Constructor of the CSVFileWriter class.
     */
    private CsvFileWriter() {
    }

    /**
     * If there is not already the path to store the files into created, this path will be created.
     * A new file will be created here also. The name of the new file will be the current Timestamp.
     * If the new file is created, the first line in the file will be: Count; Timestamp; AccX; AccY;
     * AccZ; GPS-Alt; GPS-Lon; GPS-Lat; MicMaxAmpl; Acc Vector a
     */
    public static void crtFile() {
        File outputFile;
        String fileName;

        fileName = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss'.csv'").format(new Date());
        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + ConfigApp.targetStorageDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            outputFile = new File(dir, fileName);
            lastFilePath = outputFile.getPath();
            headerWasWritten = false;

            Log.d("creating file", "File Created");
            fileWriter = new FileWriter(outputFile, true);

        } catch (Exception e) {
            Log.w("creating file error", e.toString());
        }
    }


    /**
     * Writes the captured data in the csv file.
     */
    public static void writeLine(FFT fft, float[] amplitudes, int sample_rate) {
        if (fileWriter == null) {
            return;
        }
        try {
            int maxHz = 5000;
            // write header if not for current file
            if (!headerWasWritten) {
                StringBuilder lineHeader = new StringBuilder();
                for (int i = 0; i < amplitudes.length; i++) {
                    if(i > maxHz) break;
                    lineHeader.append(String.format("%3d Hz", (int) fft.binToHz(i, sample_rate))).append(separator);
                }
                fileWriter.write(lineHeader.toString() + "\r\n");
                headerWasWritten = true;
            }

            // Write line
            StringBuilder lineData = new StringBuilder();
            for (int i = 0; i < amplitudes.length; i++) {
                if(i > maxHz) break;
                lineData.append(String.format("%8.3f", amplitudes[i])).append(separator);
            }
            fileWriter.write(lineData.toString() + "\r\n");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

    /**
     * closes the file after recording has finished.
     */
    public static void closeFile() {
        if (fileWriter == null) {
            return;
        }
        try {

            fileWriter.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
