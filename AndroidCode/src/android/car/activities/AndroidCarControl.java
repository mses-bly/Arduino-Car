package android.car.activities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.car.R;
import android.car.communication.BluetoothManager;
import android.car.data.CodesEnum;
import android.car.ui_components.ListDialogFragment;
import android.car.ui_components.ListDialogFragment.ListDialogListener;
import android.car.ui_components.VerticalSeekBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AndroidCarControl extends FragmentActivity implements
		ListDialogListener, OnSeekBarChangeListener {

	private final static int REQUEST_ENABLE_BT = 1;

	private BluetoothManager manager;

	private BluetoothDevice connectedDevice;

	private VerticalSeekBar seekBarSpeed;

	private TextView speedText;

	private CheckBox reverseCheckbox;

	private Context context;

	private ProgressDialog myProgressDialog;

	private ToggleButton switchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_control_module);
		manager = new BluetoothManager();
		seekBarSpeed = (VerticalSeekBar) findViewById(R.id.seek_bar_car_speed);
		seekBarSpeed.setOnSeekBarChangeListener(this);
		speedText = (TextView) findViewById(R.id.speed_indicator_text);
		reverseCheckbox = (CheckBox) findViewById(R.id.reverse_checkbox);
		reverseCheckbox.setOnCheckedChangeListener(compoundButtonListener);
		switchButton = (ToggleButton) findViewById(R.id.on_off_switch);
		switchButton.setOnCheckedChangeListener(compoundButtonListener);
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
			Log.d("ERROR", e.getMessage());
		}
		context = this;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Log.d("INFO", "User enabled bluetooth");
			manager.loadPairedDevices();
			showDeviceSelection();

		} else {
			Log.d("INFO", "User did not enable bluetooth");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (manager != null) {
			manager.disconnect();
			Log.d("INFO", "Connection destroyed");
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
	public void onDialogSelect(final DialogFragment dialog, final int which) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
						myProgressDialog = ProgressDialog.show(
								context,
								"",
								"Connecting to device "
										+ connectedDevice.getName());
						myProgressDialog.setCancelable(true);
					}
				});
				connectedDevice = manager.getPairedDevices().get(which);
				try {
					manager.connect(connectedDevice);
					Log.d("INFO", "Device connected");
					result = "Connected to device" + connectedDevice.getName();
				} catch (Exception e) {
					Log.d("ERROR", e.getLocalizedMessage());
					result = e.getLocalizedMessage();
				}
				final String res = result;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						myProgressDialog.dismiss();
						Toast.makeText(context, res, Toast.LENGTH_LONG).show();
					}
				});
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
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
		if(switchButton.isChecked()){
			accelerate();
		}
	}

	CompoundButton.OnCheckedChangeListener compoundButtonListener = new CompoundButton.OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(buttonView.getId() == switchButton.getId()){
				if (isChecked) {
					accelerate();
				} else {
					stop();
				}
			}
			else{
				if(buttonView.getId() == reverseCheckbox.getId()){
					new Thread(new Runnable() {
						@Override
						public void run() {
							stop();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								Log.d("ERROR",e.getLocalizedMessage());
							}
							accelerate();
						}
					}).start();
				}
			}
			
			
		}
	};
	
	private void accelerate() {
		if (manager.isConnected()) {
			int reverse;
			if (reverseCheckbox.isChecked()) {
				reverse = 1;
			} else {
				reverse = 0;
			}
			int speed = seekBarSpeed.getProgress();
			Log.d("INFO", "Acelerating "+speed);
			byte[] command = { (2 & 0xFF), (byte) (reverse & 0xFF),
					(byte) (speed & 0xFF) };
			try {
				manager.write(command);
			} catch (Exception e) {
				Log.d("ERROR", e.getLocalizedMessage());
			}
		}
	}

	private void stop() {
		if (manager.isConnected()) {
			int reverse;
			if (reverseCheckbox.isChecked()) {
				reverse = 1;
			} else {
				reverse = 0;
			}
			byte[] command = { (2 & 0xFF), (byte) (reverse & 0xFF), 0x00 };
			try {
				manager.write(command);
				Log.d("INFO", "Stopping... ");
			} catch (Exception e) {
				Log.d("ERROR", e.getLocalizedMessage());
			}
		}
	}
}
