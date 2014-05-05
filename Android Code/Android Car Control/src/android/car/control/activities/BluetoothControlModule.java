package android.car.control.activities;

import java.util.ArrayList;
import java.util.HashMap;

import data.CodesEnum;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.car.control.R;
import android.car.control.R.layout;
import android.car.control.communication.BluetoothManager;
import android.car.control.ui_components.ListDialogFragment;
import android.car.control.ui_components.ListDialogFragment.ListDialogListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BluetoothControlModule extends FragmentActivity implements
		ListDialogListener {

	private final static int REQUEST_ENABLE_BT = 1;

	private BluetoothManager manager;

	private BluetoothAdapter mBluetoothAdapter;

	private BluetoothDevice connectedDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_control_module);
		manager = new BluetoothManager();
		try {
			CodesEnum code = manager.intialize();
			if (code == CodesEnum.BLUETOOTH_NOT_ENABLED) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			} else {
				manager.loadPairedDevices();
				showDeviceSelection();
			}
		} catch (Exception e) {
			Log.d("Bluetooth debug", e.getMessage());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Log.d("Bluetooth debug", "User enabled bluetooth");
			manager.loadPairedDevices();
			showDeviceSelection();

		} else {
			Log.d("Bluetooth debug", "User did not enable bluetooth");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			manager.disconnect();
		} catch (Exception e) {
			Log.d("Bluetooth debug", e.getMessage());
		}
	}

	private void showDeviceSelection() {
		if (!manager.getPairedDevices().isEmpty()) {
			ListDialogFragment showDevices = new ListDialogFragment(
					manager.getPairedDevicesNames());
			showDevices.show(getSupportFragmentManager(), "DevicesNamelist");
		}
	}

	@Override
	public void onDialogSelect(DialogFragment dialog, int which) {
		this.connectedDevice = this.manager.getPairedDevices().get(which);
		doSomething();
	}

	public void doSomething() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					manager.connect(connectedDevice);
					Log.d("Bluetooth debug", "***********************Sleeping 5****************");
					Thread.sleep(5000);
					Log.d("Bluetooth debug", "Connected");
					char[] command = { 'H' };
					byte[] b = new byte[command.length];
					for (int i = 0; i < b.length; i++) {
						b[i] = (byte) command[i];
						Log.d("Bluetooth debug", String.valueOf(b[i]));
					}
					manager.write(b);
					Log.d("Bluetooth debug", "Sending data");
				} catch (Exception e) {
					Log.d("Bluetooth debug", e.getMessage());
				}

			}
		}).start();

	}

}
