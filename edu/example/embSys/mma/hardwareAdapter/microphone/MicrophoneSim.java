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
package edu.example.embSys.mma.hardwareAdapter.microphone;

import edu.example.embSys.mma.data.CsvFileReader;
import edu.example.embSys.mma.hardwareAdapter.IMicrophone;

// TODO: Auto-generated Javadoc
/**
 * Simulated microphone with data input from a saved CSV file .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class MicrophoneSim implements IMicrophone{

	/**
	 * auto generated method.
	 */
	public MicrophoneSim() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * auto generated method.
	 */
	@Override
	public void initMicrophone() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * gets the max amplitude of the microphone as integer type from the CSV file .
	 *
	 * @return gets the amplitude values from the CSV file
	 */
	@Override
	public Integer getMaxAmplitude() {
		return CsvFileReader.getMaxAmplitude();
	}


}
