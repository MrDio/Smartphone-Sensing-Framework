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
package edu.example.embSys.mma.hardwareAdapter.accelerometer;

import edu.example.embSys.mma.data.AccBuffer;
import edu.example.embSys.mma.data.AccXYZ;
import edu.example.embSys.mma.hardwareAdapter.IAccelerometer;

import android.annotation.SuppressLint;
//import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


// TODO: Auto-generated Javadoc
/**
 * Initialising the accelerometer-Sensor of the Smartphone and get the data from the buffer.
 *
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class AccelerometerHardware implements IAccelerometer, SensorEventListener{

	/**The acceleration value in X-direction. The default value is 1*/
	private Float accX=1.0f;
	
	/**The acceleration value in Y-direction. The default value is 1*/	
	private Float accY=1.0f;
	
	/**The acceleration value in Z-direction. The default value is 1*/
	private Float accZ=1.0f;
	
	/**The acceleration value of a. The default value is 1*/
	private Double accA=1.0d;
	
	/** Shows if the sensors are initialised. */
	private boolean sensorInitialised;
	
	/** Declaration of the accBuffer used to store data temporarily in. */
	private AccBuffer accBuffer;
	
	/** The sensorManager is used to access to the accelerometer hardware. */
	private SensorManager sensorManager;

	/** The reference to the accelerometer handle. */
	private Sensor accelerometer;
	
	
	/**
	 * Instantiates the Sensor of the Smartphone.
	 *
	 * @param sensorManager needed to access the devices accelerometer sensor
	 */
	public AccelerometerHardware(SensorManager sensorManager) {
		sensorInitialised = false;
		this.sensorManager = sensorManager;
		this.accBuffer=new AccBuffer();
		this.initAccelerometer();
	}
	
	/**
	 * gets the x-axis value of the accelerometer. 
	 * 
	 * @return accX returns Float value of the accelerometers x-axis 
	 */
	@Override
	public Float getAccX() {
		return this.accX;
	}

	/**
	 * gets the y-axis value of the accelerometer. 
	 * 
	 * @return accY returns Float value of the accelerometers y-axis 
	 */	@Override
	public Float getAccY() {
		return this.accY;
	}

	 /**
		 * gets the z-axis value of the accelerometer. 
		 * 
		 * @return accZ returns Float value of the accelerometers x-axis 
		 */
	@Override
	public Float getAccZ() {
		return this.accZ;
	}

	/**
	 *  auto-generated method not used in this source code.
	 */
	@Override
	public void writeValues() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Instantiates the Sensor of the Smartphone.
	 */
	@Override
	public void initAccelerometer() {
		this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		this.sensorInitialised=true;
	}

	/**
	 * Normally called when the accuracy has changed.
	 *
	 * @param sensor the sensor
	 * @param accuracy the accuracy
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
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
		this.accX=event.values[0];
		this.accY=event.values[1];
		this.accZ=event.values[2];
		this.accA=Math.sqrt((this.accX*this.accX)+(this.accY*this.accY)+(this.accZ*this.accZ));
		this.accBuffer.addValue(new AccXYZ(new Float(this.accX), new Float(this.accY), new Float(this.accZ)));
	}

	/**
	 * gets the Buffer.
	 *
	 * @return the buffer in which the values are stored
	 */
	@Override
	public AccXYZ[] getBuffer() {
		return this.accBuffer.getBuffer();
	}

	@Override
	public Double getAccA() {
		// TODO Auto-generated method stub
		return this.accA;
	}

}

