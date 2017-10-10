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
package edu.example.embSys.mma.userInterface;


// TODO: Auto-generated Javadoc
/**
 * IUserInterface interface that declares all Strings for the TextViews in the Activity and allows updating the changed sensor-data in runtime .
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.1
 */
public interface IUserInterface {

	/**
	 * Sets the act state.
	 *
	 * @param actState the new act state
	 */
	public void setActState(String actState);

	/**
	 * Sets the accelerometer x-value.
	 *
	 * @param accx the new accelerometer x-value
	 */
	public void setAccX(String accx);
	
	/**
	 * Sets the accelerometer y-value.
	 *
	 * @param accy the new accelerometer y-value
	 */
	public void setAccY(String accy);
	
	/**
	 * Sets the accelerometer z-value.
	 *
	 * @param accz the new accelerometer z-value
	 */
	public void setAccZ(String accz);
	
	/**
	 * Sets the GPS altitude.
	 *
	 * @param gpsAlt the new GPS altitutde
	 */
	public void setGPSAlt(String gpsAlt);
	
	/**
	 * Sets the GPS longitude.
	 *
	 * @param gpsLon the new GPS longitude
	 */
	public void setGPSLon(String gpsLon);
	
	/**
	 * Sets the GPS latitude.
	 *
	 * @param gpsLat the new GPS latitude
	 */
	public void setGPSLat(String gpsLat);
	
	/**
	 * Sets the microphone max amplitude.
	 *
	 * @param micMaxAmpl the new microphone max amplitude
	 */
	public void setMicMaxAmpl(String micMaxAmpl);
	
	/**
	 * Sets the rotation around the X-Axis
	 *
	 * @param rotX the rotation fetched from the gyro
	 */
	public void setRotationX(String rotX);
	
	/**
	 * Sets the rotation around the Y-Axis
	 *
	 * @param rotY the rotation fetched from the gyro
	 */
	public void setRotationY(String rotY);
	
	/**
	 * Sets the rotation around the Z-Axis
	 *
	 * @param rotZ the rotation fetched from the gyro
	 */
	public void setRotationZ(String rotZ);
	
	
	/**
	 * Callback from the statemachine to update the chart data.
	 */
	public void updateCharts();
	
	/**
	 * Sets the Heartbeat rate
	 *
	 * @param rate from the Heartbeat sensor
	 */
	public void setHeartrate(String heartrate);
	
	
}
