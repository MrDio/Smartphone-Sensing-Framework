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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.example.embSys.mma.config.ConfigApp;



import android.util.Log;

/**
 * The Class CsvFileReader reads the saved csv-file and gets the data from the file for the simulated sensors..
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 * @image jpg data_details.jpg
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
	
	/** Declaration of the Index for the Altitude*/
	private final static Integer indexOfGPSAlt = 5;
	
	/** Declaration of the Index for the Longitude*/
	private final static Integer indexOfGPSLon = 6;
	
	/** Declaration of the Index for the Latitude*/
	private final static Integer indexOfGPSLat = 7;
	
	/** Declaration of the Index for the Microphone Amplitude*/
	private final static Integer indexOfMicMaxAmpl = 8;
	
	/** Declaration of the Index for the Microphone Amplitude*/
	private final static Integer indexOfAccVectorA = 9;
	
	/** Declaration of the Index for the RotationX-Value*/
	private final static Integer indexOfRotationX = 10;
	
	/** Declaration of the Index for the RotationY-Value*/
	private final static Integer indexOfRotationY = 11;
	
	/** Declaration of the Index for the RotationZ-Value*/
	private final static Integer indexOfRotationZ = 12;
	
	/** Declaration of the Index for the Heardrate-Value*/
	private final static Integer indexOfHeartrate = 13;	
	
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
	
	/**Setting the flag of the read vector a to false*/
	private static  Boolean readFlagAccVectorA = false;
	
	/**Setting the flag of the read RotationX to false*/
	private static Boolean readFlagRotationX = false;
	
	/**Setting the flag of the read RotationY to false*/
	private static Boolean readFlagRotationY = false;
	
	/**Setting the flag of the read RotationZ to false*/
	private static Boolean readFlagRotationZ = false;
	
	/**Setting the flag of the read RotationZ to false*/
	private static Boolean readFlagHeartrate = false;
	

	/**
	 * The Enum CSV_Value including all parameter saved in the csv-file .
	 */
	public static enum CSV_Value{
		ACCX, ACCY, ACCZ, GPSAlt, GPSLon, GPSLat, MicMaxAmpl, AccA, RotX, RotY, RotZ, Heartrate
	}
	
	
    /**
     * reading the data from the csv-files.
     */
    public static void readFile() {
    	File inputFile;
    	String tmpLine;
    	try {
    		inputFile = new File(CrtTargetDir.targetDir.getAbsolutePath(), ConfigApp.csvReaderStimuli);
	    		if(inputFile.exists()){
	    		fr = new FileReader(inputFile);
				fis = new FileInputStream(inputFile);
				br = new BufferedReader(fr);
				br.readLine(); // Throw first row away
				tmpLine=br.readLine();	//read first line
				if (tmpLine != null) {
					actLine=tmpLine.split(separator);
				}else {
					actLine=null;
				}
				Log.d("CSV-File-Reader_crtFile", actLine.toString());
    		}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     * Checking  the read line in the csv-file.
     */
    private static void checkReadLine(){
    	String tmpLine = null;
    	if (readFlagAccx && readFlagAccy && readFlagAccz && readFlagGPSAlt && readFlagGPSLon && readFlagGPSLat && readFlagMicMaxAmpl && readFlagAccVectorA && readFlagRotationX && readFlagRotationY && readFlagRotationZ && readFlagHeartrate) {
    		try {
    			tmpLine=br.readLine();
    			if (tmpLine != null) {
    				actLine=tmpLine.split(separator);
				}else {
					actLine=null;
				}
    			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		readFlagAccx=false;
    		readFlagAccy=false;
    		readFlagAccz=false;
    		readFlagGPSAlt=false;
    		readFlagGPSLon=false;
    		readFlagGPSLat=false;
    		readFlagMicMaxAmpl=false;
    		readFlagAccVectorA=false;
    		readFlagRotationX = false;
    		readFlagRotationY = false;
    		readFlagRotationZ = false;
		    readFlagHeartrate = false;

		}
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

	
	/**
	 * gets the x-axis value of the accelerometer for simulation. 
	 * 
	 * @return accX returns float value of the accelerometers x-axis 
	 */
	public static Float getAccX() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagAccx=true;
		Float tmpAccx=Float.valueOf(actLine[indexOfAccx]);
		checkReadLine();
		Log.d("CSV-File-Reader_getAccX", tmpAccx.toString());
		return tmpAccx;
	}

	
	/**
	 * gets the y-axis value of the accelerometer for simulation. 
	 * 
	 * @return accY returns float value of the accelerometers y-axis 
	 */
	public static Float getAccY() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagAccy=true;
		Float tmp = Float.valueOf(actLine[indexOfAccy]);
		checkReadLine();
		return tmp;
	}

	
	/**
	 * gets the z-axis value of the accelerometer for simulation. 
	 * 
	 * @return accZ returns float value of the accelerometers z-axis 
	 */
	public static Float getAccZ() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagAccz=true;
		Float tmp = Float.valueOf(actLine[indexOfAccz]);
		checkReadLine();
		return tmp;
	
	}


	/**
	 * Gets the altitude-value for the simulation.
	 *
	 * @return the altitude as type of double
	 */
	public static Double getAltitude() {
		if (actLine == null) {
			return 0.0d;
		}
		readFlagGPSAlt=true;
		Double tmp = Double.valueOf(actLine[indexOfGPSAlt]); 
		checkReadLine();
		return tmp;
	}


	/**
	 * Gets the latitude-value for the simulation.
	 *
	 * @return the latitude as type of double
	 */
	public static Double getLatitude() {
		if (actLine == null) {
			return 0.0d;
		}
		readFlagGPSLat=true;
		Double tmp = Double.valueOf(actLine[indexOfGPSLat]); 
		checkReadLine();
		return tmp;
	}


	/**
	 * Gets the longitude-value for the simulation.
	 *
	 * @return the longitude as type of double
	 */
	public static Double getLongitude() {
		if (actLine == null) {
			return 0.0d;
		}
		readFlagGPSLon=true;
		Double tmp = Double.valueOf(actLine[indexOfGPSLon]);
		checkReadLine();		
		return tmp;
	}


	/**
	 * Gets the bearing-value for the simulation.
	 *
	 * @return the bearing as type of float
	 */
	public static Float getBearing() {
		// TODO Auto-generated method stub
		return 0.0f;
	}


	/**
	 * Gets the speed for the simulation.
	 *
	 * @return the speed as type of float
	 */
	public static Float getSpeed() {
		// TODO Auto-generated method stub
		return 0.0f;
	}


	/**
	 * Gets the max amplitude of the microphone for the simulation.
	 *
	 * @return the max amplitude of the microphone
	 */
	public static Integer getMaxAmplitude() {
		if (actLine == null) {
			return 0;
		}
		readFlagMicMaxAmpl=true;
		Integer tmp = Integer.valueOf(actLine[indexOfMicMaxAmpl]); 
		checkReadLine();
		return tmp;
	}
	
	/**
	 * Gets the value of the Value A for the simulation
	 *
	 * @return the max amplitude of the microphone
	 */
	public static Double getVectorA() {
		if (actLine == null) {
			return 0.0d;
		}
		readFlagAccVectorA=true;
		Double tmp = Double.valueOf(actLine[indexOfAccVectorA]); 
		checkReadLine();
		return tmp;
	}
	
	public static Float getRotationX() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagRotationX=true;
		Float tmpRotationX=Float.valueOf(actLine[indexOfRotationX]);
		checkReadLine();
		Log.d("CSV-File-Reader_getRotationX", tmpRotationX.toString());
		return tmpRotationX;
	}

	public static Float getRotationY() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagRotationY=true;
		Float tmpRotationY=Float.valueOf(actLine[indexOfRotationY]);
		checkReadLine();
		Log.d("CSV-File-Reader_getRotationY", tmpRotationY.toString());
		return tmpRotationY;
	}

	public static Float getRotationZ() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagRotationZ=true;
		Float tmpRotationZ=Float.valueOf(actLine[indexOfRotationZ]);
		checkReadLine();
		Log.d("CSV-File-Reader_getRotationZ", tmpRotationZ.toString());
		return tmpRotationZ;
	}

	/**
	 * gets the heartrate value of the heatrate sensor for simulation. 
	 * 
	 * @return heartrate returns Integer value of the heartrate
	 */	
	public static Integer getHeartrate() {
		if (actLine == null) {
			return -1; // default parameter
		}
		readFlagHeartrate=true;
		Integer tmpHeartrate=Integer.valueOf(actLine[indexOfHeartrate]);
		checkReadLine();
		Log.d("CSV-File-Reader_getHeartrate", tmpHeartrate.toString());
		return tmpHeartrate;
	}
}
