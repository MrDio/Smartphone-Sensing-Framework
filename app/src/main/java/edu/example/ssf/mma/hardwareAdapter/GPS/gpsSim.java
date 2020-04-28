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
package edu.example.ssf.mma.hardwareAdapter.GPS;



// TODO: Auto-generated Javadoc

import android.content.Context;

import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.hardwareAdapter.IGPS;

/**
 * Simulated GPS sensor with data input from a saved CSV file 
 * @version 2.0
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 * @author D. Lagamtzis
 * @version 2.0
 */

public class gpsSim implements IGPS {

	/**
	 * gets the altitude as type double from the CSV file 
	 * 
	 * @return gets the altitude values from the CSV file 
	 */
	@Override
	public Double getAltitude() {
		// TODO Auto-generated method stub
		return CsvFileReader.getAltitude();
	}

	/**
	 * gets the Latitude as type double from the CSV file 
	 * 
	 * @return gets the latitude values from the CSV file 
	 */
	@Override
	public Double getLatitude() {
		// TODO Auto-generated method stub
		return CsvFileReader.getLatitude();
	}

	/**
	 * gets the longitude as type double from the CSV file 
	 * 
	 * @return gets the longitude values from the CSV file 
	 */
	@Override
	public Double getLongitude() {
		// TODO Auto-generated method stub
		return CsvFileReader.getLongitude();
	}

	/**
	 * gets the bearing as type float  
	 * 
	 * @return 444f 
	 */
	@Override
	public Float getBearing() {
		// TODO Auto-generated method stub
		return 444f;
	}

	/**
	 * gets the speed as type float 
	 * 
	 * @return 34f
	 */
	@Override
	public Float getSpeed() {
		// TODO Auto-generated method stub
		return 34f;
	}

	@Override
	public void initGPS(Context context) {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

}
