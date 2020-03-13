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
package edu.example.ssf.mma.hardwareAdapter.magnetometer;



// TODO: Auto-generated Javadoc

import android.content.Context;

import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.hardwareAdapter.IMagneto;

/**
 * Simulated Magnetic Field sensor with data input from a saved CSV file
 * @author D. Lagamtzis
 * @version 1.0
 */

public class magnetoSim implements IMagneto {

	/**
	 * gets the altitude as type double from the CSV file 
	 * 
	 * @return gets the altitude values from the CSV file 
	 */
	@Override
	public Float getMagnetoX() {
		// TODO Auto-generated method stub
		return CsvFileReader.getMagneticX();
	}

	/**
	 * gets the Latitude as type double from the CSV file 
	 * 
	 * @return gets the latitude values from the CSV file 
	 */
	@Override
	public Float getMagnetoY() {
		// TODO Auto-generated method stub
		return CsvFileReader.getMagneticY();
	}

	/**
	 * gets the longitude as type double from the CSV file 
	 * 
	 * @return gets the longitude values from the CSV file 
	 */
	@Override
	public Float getMagnetoZ() {
		// TODO Auto-generated method stub
		return CsvFileReader.getMagneticZ();
	}



	@Override
	public void initMagneto(Context context) {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}



}
