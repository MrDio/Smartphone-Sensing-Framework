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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class is responsible for creating a new csv File, writing the data into the file and to close it,
 * if the recording has finished.
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 */

public class CsvFileWriter {
	
	/** Declaration of the attribute for the FileWriter that is used to write into the csv file */
	private static FileWriter fileWriter;
	
	/** Declaration of the String for the separator that is used to separate the incoming sensor data*/
	private final static String separator = ";";



	
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
			File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ ConfigApp.targetStorageDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			outputFile = new File( dir,fileName);
			Log.d("creating file", "File Created");
			fileWriter=new FileWriter(outputFile, true);
			fileWriter.write("Round" +separator+
					"Timestamp"+separator+
					"AccX"+separator+
					"AccY"+separator+
					"\r\n");
		}
		catch(Exception e){
			Log.w("creating file error", e.toString());
		}
	}
	
    
    /**
     * Writes the captured data in the csv file.
     * @param round current round
     * @param timestamp current timestamp
     * @param accx current Accelerometer X-Value
     * @param accy current Accelerometer Y-Value
     */
	public static void writeLine(String round,
			String timestamp,
			String accx,
			String accy
			) {
		if (fileWriter==null) {
			return;
		}
		try {
			fileWriter.write(round + separator +
					timestamp+separator+
					accx+separator+
					accy+separator+
					"\r\n");



		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	/**
	 * closes the file after recording has finished.
	 */
	public static void closeFile() {
		if (fileWriter==null) {
			return;
		}
		try {
			fileWriter.close();
			Log.d("closing file", "File closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
