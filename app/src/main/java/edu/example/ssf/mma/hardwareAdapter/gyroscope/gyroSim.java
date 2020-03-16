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
package edu.example.ssf.mma.hardwareAdapter.gyroscope;



// TODO: Auto-generated Javadoc

import android.content.Context;

import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.hardwareAdapter.IGyroscope;

/**
 * Simulated Gyro sensor with data input from a saved CSV file
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 */

public class gyroSim implements IGyroscope {

	/**
	 * gets the altitude as type double from the CSV file 
	 * 
	 * @return gets the altitude values from the CSV file 
	 */
	@Override
	public Float getRotX() {
		// TODO Auto-generated method stub
		return CsvFileReader.getRotationX();
	}

	/**
	 * gets the Latitude as type double from the CSV file 
	 * 
	 * @return gets the latitude values from the CSV file 
	 */
	@Override
	public Float getRotY() {
		// TODO Auto-generated method stub
		return CsvFileReader.getRotationY();
	}

	/**
	 * gets the longitude as type double from the CSV file 
	 * 
	 * @return gets the longitude values from the CSV file 
	 */
	@Override
	public Float getRotZ() {
		// TODO Auto-generated method stub
		return CsvFileReader.getRotationZ();
	}


	@Override
	public void initGyro(Context context) {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}


}
