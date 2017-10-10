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

package edu.example.embSys.mma.data;

import java.util.LinkedList;
import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * Buffer to save the Values of the x/y/z-axis of the accelerometer temporary .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class AccBuffer {

	/** Declaration of the buffer. */
	private List<AccXYZ> buffer;
	
	/** Declaration of the max size for the array. */
	private static Integer MAX_SIZE = 10;
	
	/**
	 * Instantiates a new acc buffer.
	 */
	public AccBuffer() {
		this.buffer=new LinkedList<AccXYZ>();
	}
	
	/**
	 * Gets the buffer.
	 * 
	 * @return the buffer in which the values are stored
	 */
	public AccXYZ[] getBuffer(){
		return (AccXYZ[])this.buffer.toArray();
	}
	
	
	/**
	 * Adds the value to the Buffer.
	 *
	 * @param accXYZ the x/y/z values of the accelerometer
	 */
	public void addValue(AccXYZ accXYZ) {
		if (this.buffer.size() == MAX_SIZE) {
			this.buffer.remove(0);
		}
		this.buffer.add(accXYZ);
	}
}
