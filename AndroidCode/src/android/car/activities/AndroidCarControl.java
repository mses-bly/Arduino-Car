package android.car.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.car.R;
import android.car.communication.BluetoothManager;
import android.car.data.CodesEnum;
import android.car.ui_components.ListDialogFragment;
import android.car.ui_components.ListDialogFragment.ListDialogListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BluetoothControlModule extends FragmentActivity implements
		ListDialogListener, OnSeekBarChangeListener {

	private final static int REQUEST_ENABLE_BT = 1;

	private BluetoothManager manager;

	private BluetoothDevice connectedDevice;

	private SeekBar seekBarSpeed;

	private TextView speedText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_control_module);
		manager = new BluetoothManager();
		seekBarSpeed = (SeekBar) findViewById(R.id.seek_bar_car_speed);
		seekBarSpeed.setOnSeekBarChangeListener(this);
		speedText = (TextView) findViewById(R.id.speed_indicator_text);
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
		if (manager != null) {
			manager.disconnect();
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
		try {
			manager.connect(connectedDevice);
			Log.d("Bluetooth debug", "Device connected");
		} catch (Exception e) {
			Log.d("Bluetooth debug", e.getMessage());
		}
	}
	public void changeSpeed(final int speed){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					manager.write(speed);
					int response = manager.read();
				} catch (Exception e) {
					Log.d("Bluetooth debug", e.getMessage());
				}

			}
		}).start();

	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		speedText.setText(String.valueOf(progress));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
		changeSpeed(seekBar.getProgress());
	}

}
