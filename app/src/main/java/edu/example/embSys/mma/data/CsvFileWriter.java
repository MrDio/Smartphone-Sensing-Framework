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
    GNU v3 General Public License for more details.

    Released under GNU v3
    
    You should have received a copy of the GNU General Public License
    along with MMA.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.example.embSys.mma.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class is responsible for creating a new csv File, writing the data into the file and to close it,
 * if the recording has finished.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class CsvFileWriter {
	
	/** Declaration of the attribute for the FileWriter that is used to write into the csv file */
	private static FileWriter fileWriter;
	
	/** Declaration of the String for the separator that is used to separate the incoming sensor data*/
	private final static String separator = ";";
	
	private static boolean fileOpen = false;
	
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
    	
    	//Check if file is already open
    	if (fileOpen) {
			return;
		}
    	
		fileName = new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss'.csv'").format(new Date());
		outputFile = new File(CrtTargetDir.targetDir, fileName);
		
		try {
			fileWriter=new FileWriter(outputFile, true);
			fileWriter.write("Count"+separator+
					"Timestamp"+separator+
					"AccX"+separator+
					"AccY"+separator+
					"AccZ"+separator+
					"GPS-Alt"+separator+
					"GPS-Lon"+separator+
					"GPS-Lat"+separator+
					"MicMaxAmpl"+separator+
					"Acc Vector a"+separator+
					"Rotation X" + separator +
					"Rotation Y" + separator +
					"Rotation Z" + separator +
					"Heartrate" + 
					"\r\n");
			fileOpen=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    
    /**
     * Writes the captured data in the csv file.
     *
     * @param cnt current count (tick)
     * @param timestamp current timestamp
     * @param accx current Accelerometer X-Value
     * @param accy current Accelerometer Y-Value
     * @param accz current Accelerometer Z-Value
     * @param GPSAlt current GPS Altitude
     * @param GPSLon current GPS Longitude
     * @param GPSLat current GPS Latitude
     * @param micMaxAmpl current maximum Microphone Amplitude
     * @param accVecA = sqrt(accx+accy+accz)
     * @param rotationX rotation around the X-Axis
     * @param rotationX rotation around the Y-Axis
     * @param rotationX rotation around the Z-Axis
     * @param heartrate give the current heartrate
     */
	public static void writeLine(String cnt,
			String timestamp,
			String accx,
			String accy,
			String accz,
			String GPSAlt,
			String GPSLon,
			String GPSLat,
			String micMaxAmpl,
			String accVecA,
			String rotationX,
			String rotationY,
			String rotationZ,
			String Heartrate) {
		if (fileWriter==null) {
			return;
		}
		try {
			fileWriter.write(cnt+separator+
					timestamp+separator+
					accx+separator+
					accy+separator+
					accz+separator+
					GPSAlt+separator+
					GPSLon+separator+
					GPSLat+separator+
					micMaxAmpl+separator+
					accVecA+ separator +
					rotationX + separator +
					rotationY + separator +
					rotationZ + separator + 
					Heartrate + 
					"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			fileOpen=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
