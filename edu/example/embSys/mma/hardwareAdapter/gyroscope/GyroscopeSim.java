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
package edu.example.embSys.mma.hardwareAdapter.gyroscope;

import edu.example.embSys.mma.data.AccBuffer;
import edu.example.embSys.mma.data.AccXYZ;
import edu.example.embSys.mma.data.CsvFileReader;
import edu.example.embSys.mma.hardwareAdapter.IGyroscope;


/**
 * Simulated gyroscope with data input from a saved CSV file 
 * @author Kavivarman Sivarasah and Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class GyroscopeSim implements IGyroscope{

	/** Declaration of the acc buffer. */
	//TODO buffer is not used until now. To use the buffer, you should simulate the 
	// onSensorChanged function e.g. by writing read-flags to synchronise getAccX, getAccY, getAccZ was called 
	// accessing one row of the csv-file.
	private AccBuffer accBuffer;
	
	/**
	 * the AccelerometerSim method initialise a new buffer 
	 */
	public GyroscopeSim() {
		this.accBuffer=new AccBuffer();
	}

	@Override
	public void writeValues(){
	
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

	@Override
	public void initGyroscope() {
		
	}

	@Override
	public Float getRotationX() {
		return CsvFileReader.getRotationX();
	}

	@Override
	public Float getRotationY() {
		return CsvFileReader.getRotationY();
	}

	@Override
	public Float getRotationZ() {
		return CsvFileReader.getRotationZ();
	}
	
}
