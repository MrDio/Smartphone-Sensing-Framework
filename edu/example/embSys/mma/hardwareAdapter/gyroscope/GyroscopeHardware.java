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

package edu.example.embSys.mma.hardwareAdapter.gyroscope;

import edu.example.embSys.mma.data.AccBuffer;
import edu.example.embSys.mma.data.AccXYZ;
import edu.example.embSys.mma.hardwareAdapter.IGyroscope;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

// TODO: Auto-generated Javadoc
/**
 * Class which instantiate the Gyroscope, gets the x-/y-/z-axis rotation in rad/s
 * @author Kavivarman Sivarasah and Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class GyroscopeHardware implements IGyroscope, SensorEventListener{

	/**The rotation value in X-direction. The default value is 1*/
	private Float rotationX=1.0f;
	
	/**The rotation value in Y-direction. The default value is 1*/	
	private Float rotationY=1.0f;
	
	/**The rotation value in Z-direction. The default value is 1*/
	private Float rotationZ=1.0f;
	
	/**Shows if the sensors are initialised */
	private boolean sensorInitialised;
	
	/**Declaration of the rotationBuffer used to store data temporarily in*/
	private AccBuffer rotationBuffer;
	
	/**The sensorManager is used to access to the gyroscope hardware*/
	private SensorManager sensorManager;

	/**The reference to the gyroscope handle*/
	private Sensor gyroscope;
	
	
	/**
	 * Instantiates the Sensor of the Smartphone
	 *
	 * @param sensorManager needed to access the devices accelerometer sensor
	 */
	public GyroscopeHardware(SensorManager sensorManager) {
		sensorInitialised = false;
		this.sensorManager = sensorManager;
		this.rotationBuffer = new AccBuffer();
		this.initGyroscope();
	}

	/**
	 *  auto-generated method not used in this source code
	 */
	@Override
	public void writeValues() {
		// TODO Auto-generated method stub
	}

	/**
	 * Normally called when the accuracy has changed
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * gets the Buffer
	 * 
	 * @return the buffer in which the values are stored 
	 */
	@Override
	public AccXYZ[] getBuffer() {
		return this.rotationBuffer.getBuffer();
	}

	/**
	 * Initialise the gyroscope.
	 */
	@Override
	public void initGyroscope() {
		this.gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		this.sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		this.sensorInitialised=true;		
	}
	
	/**
	 * is called when the Sensor event changes. It gets the values for x/y/z and add them to the buffer.
	 * 
	 * @param event the SensorEvent
	 */
	@SuppressLint("UseValueOf")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if(!sensorInitialised){
			return;
		}
		this.rotationX=event.values[0];
		this.rotationY=event.values[1];
		this.rotationZ=event.values[2];
		this.rotationBuffer.addValue(new AccXYZ(new Float(this.rotationX), new Float(this.rotationY), new Float(this.rotationZ)));
	}

	@Override
	public Float getRotationX() {
		return this.rotationX;
	}

	@Override
	public Float getRotationY() {
		return this.rotationY;
	}

	@Override
	public Float getRotationZ() {
		return this.rotationZ;
	}

}

