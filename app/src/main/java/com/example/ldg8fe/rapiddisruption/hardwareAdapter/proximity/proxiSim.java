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
package com.example.ldg8fe.rapiddisruption.hardwareAdapter.proximity;



// TODO: Auto-generated Javadoc

import android.content.Context;
import android.widget.TextView;

import com.example.ldg8fe.rapiddisruption.data.CsvFileReader;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.IGyroscope;
import com.example.ldg8fe.rapiddisruption.hardwareAdapter.IProximity;

/**
 * Simulated GPS sensor with data input from a saved CSV file 
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
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

	@Override
	public void proxiUI(String s1, TextView... tvs) {
		tvs[0].setText("Proximity: " + s1);
	}

}
