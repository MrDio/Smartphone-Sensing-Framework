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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.example.ssf.mma.config.ConfigApp;


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
    public static void readFile() {
    	File inputFile;
    	String tmpLine;
    	try {
    		inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ ConfigApp.targetStorageDir, ConfigApp.csvReaderStimuli);
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
			Log.d("CSV-File-Reader_crtFile", actLine[0].toString());
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
		if (readFlagAccx && readFlagAccy && readFlagAccz && readFlagGPSAlt && readFlagGPSLon && readFlagGPSLat && readFlagMicMaxAmpl && readFlagAccVectorA && readFlagRotationX && readFlagRotationY && readFlagRotationZ && readFlagMagneticX && readFlagMagneticY && readFlagMagneticZ && readFlagProxy) {
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
			readFlagMagneticX = false;
			readFlagMagneticY = false;
			readFlagMagneticZ = false;
			readFlagProxy = false;

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
		Log.d("getAccX", "called");
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
	public static Double getMaxAmplitude() {
		if (actLine == null) {
			return 0.0d;
		}
		readFlagMicMaxAmpl=true;
		Double tmp = Double.valueOf(actLine[indexOfMicMaxAmpl]);
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
		Log.d("CSVReader_getRotationX", tmpRotationX.toString());
		return tmpRotationX;
	}

	public static Float getRotationY() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagRotationY=true;
		Float tmpRotationY=Float.valueOf(actLine[indexOfRotationY]);
		checkReadLine();
		Log.d("CSVReader_getRotationY", tmpRotationY.toString());
		return tmpRotationY;
	}

	public static Float getRotationZ() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagRotationZ=true;
		Float tmpRotationZ=Float.valueOf(actLine[indexOfRotationZ]);
		checkReadLine();
		Log.d("CSVReader_getRotationZ", tmpRotationZ.toString());
		return tmpRotationZ;
	}

	/** Magnetic field
	 *
	 * @return
	 */
	public static Float getMagneticX() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagMagneticX=true;
		Float tmpMagnX=Float.valueOf(actLine[indexOfMagnetoX]);
		checkReadLine();
		Log.d("CSVReader_getMagneticX", tmpMagnX.toString());
		return tmpMagnX;
	}

	public static Float getMagneticY() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagMagneticY=true;
		Float tmpMagnY=Float.valueOf(actLine[indexOfMagnetoY]);
		checkReadLine();
		Log.d("CSVReader_getMagneticY", tmpMagnY.toString());
		return tmpMagnY;
	}

	public static Float getMagneticZ() {
		if (actLine == null) {
			return 0.0f;
		}
		readFlagMagneticZ=true;
		Float tmpMagnZ=Float.valueOf(actLine[indexOfMagnetoZ]);
		Log.d("CSVReader_getMagneticZ", tmpMagnZ.toString());
		checkReadLine();

		return tmpMagnZ;
	}
	public static String getProximity() {
		if (actLine == null) {
			return "";
		}
		readFlagProxy=true;
		String tmpProx =actLine[indexOfProximity];
		checkReadLine();
		return tmpProx;
	}

	// Stand Alone only in UI --- no sense in simulation
	public static String getEvent() {
		if (actLine == null) {
			return " ";
		}
		//readFlagRotationZ=true;
		String tmpEvent =actLine[indexOfEvent];
		checkReadLine();
		return tmpEvent;
	}
}
