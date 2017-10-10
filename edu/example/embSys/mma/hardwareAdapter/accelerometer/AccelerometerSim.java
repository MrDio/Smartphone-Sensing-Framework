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
package edu.example.embSys.mma.hardwareAdapter.accelerometer;

import edu.example.embSys.mma.data.AccBuffer;
import edu.example.embSys.mma.data.AccXYZ;
import edu.example.embSys.mma.data.CsvFileReader;
import edu.example.embSys.mma.hardwareAdapter.IAccelerometer;


// TODO: Auto-generated Javadoc
/**
 * Simulated accelerometer with data input from a saved CSV file .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class AccelerometerSim implements IAccelerometer{

	/** Declaration of the acc buffer. */
	//TODO buffer is not used until now. To use the buffer, you should simulate the 
	// onSensorChanged function e.g. by writing read-flags to synchronise getAccX, getAccY, getAccZ was called 
	// accessing one row of the csv-file.
	private AccBuffer accBuffer;
	
	/**
	 * the AccelerometerSim method initialise a new buffer.
	 */
	public AccelerometerSim() {
		this.accBuffer=new AccBuffer();
	}

	/**
	 * gets the x-axis value in Float format from the CSV file .
	 *
	 * @return getAccX returns the values of the x-axis from the CSV file
	 */
	@Override
	public Float getAccX() {
		return CsvFileReader.getAccX();
	}
	
	/**
	 * gets the y-axis value in Float format from the CSV file .
	 *
	 * @return getAccY returns the values of the y-axis from the CSV file
	 */
	@Override	
	public Float getAccY() {
		return CsvFileReader.getAccY();
	}
	
	/**
	 * gets the z-axis value in Float format from the CSV file .
	 *
	 * @return getAccZ returns the values of the z-axis from the CSV file
	 */
	@Override
	public Float getAccZ() {
		return CsvFileReader.getAccZ();
	}
	
	/**
	 * writeValues() method (no executable source code).
	 */
	@Override
	public void writeValues(){
	
	}
	
	/**
	 * initAccelerometer() method (no executable source code).
	 */
	@Override
	public void initAccelerometer() {
	
		
	/**
	 *  gets the data from the buffer
	 *  
	 *  @return returns null, because it gets the data from the CSV file	
	 */
	}
	
	/**
	 *  gets the data from the buffer
	 *  
	 *  @return returns the data from the buffer	
	 */
	@Override
	public AccXYZ[] getBuffer() {
		return this.accBuffer.getBuffer();
	}
	
	/**
	 * gets the vector a value in Float format from the CSV file .
	 *
	 * @return acca returns the values of the z-axis from the CSV file
	 */
	@Override
	public Double getAccA() {
		return  CsvFileReader.getVectorA();
	}
	
}
