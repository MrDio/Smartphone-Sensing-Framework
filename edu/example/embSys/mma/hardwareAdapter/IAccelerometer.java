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
package edu.example.embSys.mma.hardwareAdapter;

import edu.example.embSys.mma.data.AccXYZ;


// TODO: Auto-generated Javadoc
/**
 * Interface calss to Instantiate the Accelerometer , get the x-/y-/z-axis value and the buffer.
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public interface IAccelerometer {
		
		
	/**
	 * Initialise the accelerometer.
	 */
	public void initAccelerometer();
	
	/**
	 * gets the x-axis value of the accelerometer. 
	 * 
	 * @return accX returns Float value of the accelerometers x-axis 
	 */
	public Float getAccX();
	
	/**
	 * gets the y-axis value of the accelerometer. 
	 * 
	 * @return accY returns Float value of the accelerometers y-axis 
	 */

	public Float getAccY();
	
	/**
	 * gets the z-axis value of the accelerometer. 
	 * 
	 * @return accZ returns Float value of the accelerometers z-axis 
	 */
	public Float getAccZ();
	
	/**
	 * gets the vector a 
	 * 
	 * @return acca returns Float value of the accelerometers 
	 */
	public Double getAccA();

	/**
	 *  auto-generated method not used in this source code.
	 */
	public void writeValues();
	
	/**
	 * gets the Buffer.
	 *
	 * @return the buffer in which the values are stored
	 */
	public AccXYZ[] getBuffer();
}