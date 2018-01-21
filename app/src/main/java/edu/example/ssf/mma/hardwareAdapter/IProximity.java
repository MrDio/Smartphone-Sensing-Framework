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
package edu.example.ssf.mma.hardwareAdapter;


import android.content.Context;
import android.widget.TextView;

/**
 * Interface calss to Instantiate the Accelerometer , get the x-/y-/z-axis value and the buffer
 * @author Dionysios Satikidis (dionysios.satikidis@yahoo.de)
 * @version 1.0
 */

public interface IProximity {


	/**
	 * Initialise the accelerometer.
	 */
	public void initProximity(Context context);

	public void start();
	public void stop();

	public void proxiUI(String s1, TextView... tvs);
	public String getProximity();
}