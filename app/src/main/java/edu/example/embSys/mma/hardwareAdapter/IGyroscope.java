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
package edu.example.embSys.mma.hardwareAdapter;

import edu.example.embSys.mma.data.AccXYZ;


/**
 * Interface for classes which instantiate the Gyroscope, gets the x-/y-/z-axis rotation in rad/s
 * @author
 * @version 1.0
 */

public interface IGyroscope {
		
		
	/**
	 * Initialise the gyroscope.
	 */
	public void initGyroscope();
	
	/**
	 * gets the x-axis rotation value of the gyroscope. 
	 * 
	 * @return accX returns Float value of the gyroscopes x-axis rotation
	 */
	public Float getRotationX();
	
	/**
	 * gets the y-axis rotation value of the gyroscope. 
	 * 
	 * @return accY returns Float value of the gyroscope y-axis rotation
	 */

	public Float getRotationY();
	
	/**
	 * gets the z-axis rotation value of the gyroscope. 
	 * 
	 * @return accZ returns Float value of the gyroscopes z-axis rotation
	 */
	public Float getRotationZ();

	/**
	 *  auto-generated method not used in this source code
	 */
	public void writeValues();
	
	/**
	 * gets the Buffer
	 * 
	 * @return the buffer in which the values are stored 
	 */
	public AccXYZ[] getBuffer();
}