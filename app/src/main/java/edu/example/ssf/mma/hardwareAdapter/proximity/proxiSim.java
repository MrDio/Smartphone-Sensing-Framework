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
package edu.example.ssf.mma.hardwareAdapter.proximity;



// TODO: Auto-generated Javadoc

import android.content.Context;

import edu.example.ssf.mma.data.CsvFileReader;
import edu.example.ssf.mma.hardwareAdapter.IProximity;

/**
 * Simulated Proximity sensor with data input from a saved CSV file
 * @author D. Lagamtzis
 * @version 1.0
 */

public class proxiSim implements IProximity {


	/**
	 * gets the longitude as type double from the CSV file 
	 * 
	 * @return gets the longitude values from the CSV file 
	 */
	@Override
	public String getProximity() {
		// TODO Auto-generated method stub
		return CsvFileReader.getProximity();
	}


	@Override
	public void initProximity(Context context) {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}


}
