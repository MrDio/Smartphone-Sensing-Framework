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

// TODO: Auto-generated Javadoc
/**
 * This class get the X,Y,Z Values of the Accelerometer.
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class AccXYZ {

	/**  Declaration of the X-Value form the accelerometer as a FLoat number. */
	private Float accX;

	/**  Declaration of the Y-Value form the accelerometer as a FLoat number. */
	private Float accY;
	
	/**  Declaration of the Z-Value form the accelerometer as a FLoat number. */
	private Float accZ;
	
	
	/**
	 * Instantiates a new Acc XYZ with all three values from the accelerometer.
	 *
	 * @param accX The Float Value of the accelerometers x-axis
	 * @param accY The Float Value of the accelerometers y-axis
	 * @param accZ the Float Value of the accelerometers z-axis
	 */
	public AccXYZ(Float accX, Float accY, Float accZ) {
		this.accX=accX;
		this.accY=accY;
		this.accZ=accZ;
	}
	
	/**
	 * Gets the Float X-value accelerometer.
	 *
	 * @return Float value of the accelerometer x-axis
	 */
	public Float getAccX() {
		return accX;
	}
	
	/**
	 * Gets the Float Y-value accelerometer.
	 *
	 * @return Float value of the accelerometer y-axis
	 */
	public Float getAccY() {
		return accY;
	}
	
	/**
	 * Gets the Float Z-value accelerometer.
	 *
	 * @return Float value of the accelerometer z-axis
	 */
	public Float getAccZ() {
		return accZ;
	}
	
}