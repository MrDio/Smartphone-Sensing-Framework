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
package edu.example.embSys.mma.hardwareAdapter.Heartrate;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import edu.example.embSys.mma.hardwareAdapter.IHeartrate;

import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ConnectListenerImpl;
import zephyr.android.HxMBT.ConnectedEvent;
import zephyr.android.HxMBT.ZephyrPacketArgs;
import zephyr.android.HxMBT.ZephyrPacketEvent;
import zephyr.android.HxMBT.ZephyrPacketListener;
import zephyr.android.HxMBT.ZephyrProtocol;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
/**
 * Class which instantiate the Bluetooth and Zephyr-Heartrate-Sensor and returns the heartrate in bpm
 * @author Dionysios Satikidis (dionysios.satikidis@gmail.com)
 * @version 1.0
 */

public class HeartrateHardware implements IHeartrate {

	final static String TAG = "HBS";
	final static String STATUS_NO_BEAT = "Can't get a HeartBeat\nIs the sensore removed?";
	final static String STATUS_GET_BEAT = "Get a HeartBeat";
	final static String BOND_BONDED = "Connected"; // 12
	final static String BOND_BONDING = "Connecting"; // 10 
	final static String BOND_NONE = "-1"; // 11
	final static String DEVICE_TYPE_CLASSIC = "Bluetooth device type, Classic - BR/EDR devices"; // 1
	final static String DEVICE_TYPE_DUAL = "Dual Mode - BR/EDR/LE "; // Value 3
	final static String DEVICE_TYPE_LE = "Bluetooth Low Energy only"; // 2
	final static String DEVICE_TYPE_UNKNOWN   = "UNKNOWN"; // 1
	final static String ERROR = "ERROR"; // -2147483648
	final static String UNKNOWN = "UNKNOWN"; // -2147483648
	
	
	static String BhMacID = "00:07:80:9D:8A:E8";

	private static int heartBeat = -1;
	private static boolean connected;
	private static String status;
	private Handler aNewHandler;
	private BTBondReceiver btBondR = new BTBondReceiver();
	private BTBroadcastReceiver btBroadR = new BTBroadcastReceiver();
	
	boolean active = false;
	
	final Handler Newhandler = new Handler();

	Context context;
	BluetoothAdapter adapter = null;
	NewConnectedListener newConListener;
	BTClient btClient = null;

	/**
	 * 
	 * HeartBeatSensorHardware get the context from the Activity
	 * 
	 * */
	public HeartrateHardware(Context context) {
		this.context = context;
		Log.d(TAG,"HRH context");
		//this.initHeartBeatSensor(context);
	}

	
	
	/**
	 * 
	 * Initialize the Intent
	 * @param context
	 * 
	 * */
	public void initHeartBeatSensor(Context context) {
		Log.d(TAG,"initHeartBeatSensor: start");

		this.active = true;

		IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
		context.registerReceiver(btBroadR, filter);
		IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
		context.registerReceiver(btBondR, filter2);
		Log.d(TAG,"initHeartBeatSensor: intents erstellt");


		pairHxM();
	}
	
	/**
	 * 
	 * get the Value from the hardware sensor
	 * 
	 * @return getValue returns the HeartBeat value
	 * 
	 */
	@Override
	public int getValue() {
		//Log.d(TAG,"getValue");
		return heartBeat;
	}
	
	/**
	 * 
	 * returns if the device connected to the hardware or not 
	 * 
	 * @return getConnectedState returns the connection status as boolean value
	 *
	 **/
	@Override
	public boolean getConnectedState() {
		return connected;
	}

	/**
	 * 
	 * the finish method
	 * 
	 * unregister the receivers btBroadR and btBondR
	 * reset the adapter to null
	 * remove the messages from aNewHandler and NewHandler
	 * and close the btClient if the "Thread.State.NEW"  not set yet
	 * 
	 **/
	@Override
	public void finish() {
		this.active = false;
		Log.d(TAG, "finished: schliese alles");
		context.unregisterReceiver(btBroadR);
		context.unregisterReceiver(btBondR);
		adapter = null;
		Newhandler.removeMessages(0);
		aNewHandler.removeMessages(0);

		if (btClient.getState() != Thread.State.NEW) {
			btClient.Close();
		}
		Log.d(TAG,"Thread.btClient.getState =" + (btClient.getState().toString()));

	}

	/**
	 * 
	 * BTBondReceiver receive every intents from the BTBroadcastReceive
	 * the intents contains the bond statues from the bluetooth device
	 * 
	 * it set the status value to
	 * 
	 * DEVICE_TYPE_UNKNOWN
	 * DEVICE_TYPE_CLASSIC
	 * DEVICE_TYPE_LE
	 * BOND_NONE
	 * BOND_BONDING
	 * BOND_BONDED
	 * ERROR
	 * UNKNOWN
	 * 
	 */
	private class BTBondReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle b = intent.getExtras();
			BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
			Log.d(TAG, "BOND_STATED = " + device.getBondState());

						
			switch (device.getBondState()) {
			case 0:
				status = DEVICE_TYPE_UNKNOWN;
				break;
			case 1:
				status = DEVICE_TYPE_CLASSIC;
				break;
			case 2:
				status = DEVICE_TYPE_LE;
				heartBeat = -2;
				break;
			case 3:
				status = DEVICE_TYPE_DUAL;
				break;
			case 10: // BOND_NONE
				connected = false;
				status = BOND_NONE;
				heartBeat = -10;
				break;
			case 11: // BOND_BONDING
				status = BOND_BONDING;
				heartBeat = -11;
				break;
			case 12: // BOND_BONDED
				connected = true;
				heartBeat = -12;
				status = BOND_BONDED;
				break;
			case -2147483648:
				status = ERROR;
				heartBeat = Integer.MIN_VALUE;
				break;
			default:
				connected = false;
				status = UNKNOWN;
			}
		}
	}// END BTBondReceiver

	/**
	 * 
	 * pairHxM establish the bluetooth pairing between the device and the smartphone
	 * 
	 */
	public void pairHxM() {
		adapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
		Log.d(TAG,"pairHxM: start");

		if (pairedDevices.size() > 0) { // if devices near
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().startsWith("HXM")) { // watch for a device with the name HxM
					BluetoothDevice btDevice = device; // if the device found, creat a portable device
					BhMacID = btDevice.getAddress(); // write Mac in the portable Device

					connected = true;
					status = "Paired";
					Log.d(TAG,"pairHxM: " + status);

					break;
				}
			}
		}

		@SuppressWarnings("unused")
		BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
		btClient = new BTClient(adapter, BhMacID);
		newConListener = new NewConnectedListener(Newhandler, Newhandler);
		btClient.addConnectedEventListener(newConListener);
		if (btClient.IsConnected()) {
			Log.d(TAG,"BluetoothDevice: isConntected");

			btClient.start();
		}

	}// END pairring

	/**
	 * 
	 * BTBroadcastReceiver class handle the pin/password question for the pairing with the device
	 * 
	 * */
	private class BTBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"BTBroadcastReceiver -> onReceive: start Pairing Intent");

			Bundle b = intent.getExtras(); // get device Address
			Log.d(TAG, "BTIntent " + intent.getAction());
			Log.d(TAG, "BTIntent " + b.get("android.bluetooth.device.extra.DEVICE").toString());
			Log.d(TAG, "BTIntent " + b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
			try {
				BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
				Method m = BluetoothDevice.class.getMethod("convertPinToBytes",new Class[] { String.class }); 
				byte[] pin = (byte[]) m.invoke(device, "1234"); 
				m = device.getClass().getMethod("setPin",new Class[] { pin.getClass() });
				Object result = m.invoke(device, pin); 
				
				Log.d(TAG, "result t " + result.toString());

			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 	
	 * 
	 * start a handlern
	 * listen to the bluetooth connection for the heratrate values from the device
	 * 
	 **/
	public class NewConnectedListener extends ConnectListenerImpl {
		private int HR_SPD_DIST_PACKET = 0x26;
		private HRSpeedDistPacketInfo HRSpeedDistPacket = new HRSpeedDistPacketInfo();

		public NewConnectedListener(Handler handler, Handler oldHandler) {
			super(handler, null);
			Log.d(TAG, "NewConnectedListener: erstellt");
			aNewHandler = oldHandler;
		}

		public void Connected(ConnectedEvent<BTClient> eventArgs) {
			System.out.println(String.format("Connected to BioHarness %s.",eventArgs.getSource().getDevice().getName()));
			ZephyrProtocol zephyrProtocol = new ZephyrProtocol(eventArgs.getSource().getComms());
			Log.d(TAG, "Connected - to: " + eventArgs.getSource().getDevice().getName());
			zephyrProtocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
				public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
					ZephyrPacketArgs msg = eventArgs.getPacket();
					Log.d(TAG, "Connected --> ReceivedPacket");

					if (HR_SPD_DIST_PACKET == msg.getMsgID()) {
						byte[] DataArray = msg.getBytes();// getting BT Datas
						int HRate = HRSpeedDistPacket.GetHeartRate(DataArray); // getting only datafield

						if (HRate != 0){ // Checking if Sensor is on Body
							HRate = HRate & 0xff;
							Log.d(TAG,"Connected --> ReceivedPacket --> Hrate: " + HRate);
							status = STATUS_GET_BEAT;
						} else {
							status = STATUS_NO_BEAT;
						}
						heartBeat = HRate;
					}
				}
			});
		}
	}

	/**
	 * getFinished get a status if the device is initialized
	 * 
	 **/
	@Override
	public boolean getFinished() {
		return !active;
	}

	/**
	 * 
	 *  getStatus deliver the status from the BTBondReceiver 
	 * 
	 **/
	public String getStatus() {
		return status;
	}

}
