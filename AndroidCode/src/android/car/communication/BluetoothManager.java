package android.car.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.car.data.CodesEnum;
import android.util.Log;

public class BluetoothManager {
	private static final String UUID = "cbec9a30-d3e1-11e3-9c1a-0800200c9a66";

	private BluetoothAdapter adapter;
	private ArrayList<BluetoothDevice> pairedDevices;
	private BluetoothSocket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private boolean isConnected;
	
	public CodesEnum intialize() throws Exception {
		isConnected = false;
		adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter == null) {
			throw new Exception("Bluetooth is not supported by this device");
		} else {
			if (!adapter.isEnabled()) {
				return CodesEnum.BLUETOOTH_NOT_ENABLED;
			} else {
				return CodesEnum.OK;
			}
		}
	}

	public void loadPairedDevices() {
		pairedDevices = new ArrayList<BluetoothDevice>();
		if (adapter != null) {
			Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
			this.pairedDevices.addAll(pairedDevices);
		}
	}

	public ArrayList<BluetoothDevice> getPairedDevices() {
		return pairedDevices;
	}

	public ArrayList<String> getPairedDevicesNames() {
		ArrayList<String> names = new ArrayList<String>();
		for (BluetoothDevice device : this.pairedDevices) {
			names.add(device.getName());
		}
		return names;
	}

	@SuppressLint("NewApi")
	public void connect(BluetoothDevice device) throws Exception {
		try {
			BluetoothSocket auxSocket = device
					.createInsecureRfcommSocketToServiceRecord(java.util.UUID
							.fromString(UUID));
			Method m = device.getClass().getMethod(
					"createInsecureRfcommSocket", new Class[] { int.class });
			auxSocket = (BluetoothSocket) m.invoke(device, 1);
			adapter.cancelDiscovery();
			auxSocket.connect();
			this.socket = auxSocket;
			this.inputStream = socket.getInputStream();
			this.outputStream = socket.getOutputStream();
			isConnected = true;
			Log.d("INFO","Device connected");
			
		} catch (IOException e) {
			throw new Exception("Problem with the creation of the socket");
		}

	}

	public void disconnect() {
		try {
			if (socket != null) {
				socket.close();
				isConnected = false;
			}
		} catch (IOException e) {
		}
	}

	public void write(int data) throws Exception {
		byte b3 = (byte) (data & 0xFF);
		this.outputStream.write(b3);
		Log.d("INFO", "Sending data: "+Integer.toBinaryString(data));
	}
	
	public void write(byte[] data) throws Exception {
		this.outputStream.write(data);
		Log.d("INFO", "Sending data array...)");
	}
	
	
	public int read() throws Exception {
		try {
			int response = inputStream.read();
			Log.d("INFO", "Receiving data: "+response);
			return response;
		} catch (IOException e) {
			throw new Exception("Problem reading from the socket");
		}
	}

	public boolean isConnected() {
		return isConnected;
	}
	
	

}
