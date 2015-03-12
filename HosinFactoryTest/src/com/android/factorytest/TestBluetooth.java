package com.android.factorytest;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TestBluetooth extends BaseActivity {

	private BluetoothAdapter mAdapter;
	private boolean isEnabled;
	private static final int OPENING = 1;
	private TextView blueInfo;
	private TextView blueState;
	private IntentFilter finished;
	private IntentFilter found;
	private StringBuffer blueBuffer;
	private List<BluetoothDevice> adds;
	private String notify;
	private int deviceMount = 0;//搜索到的蓝牙设备数量
	private String status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		blueInfo = (TextView) findViewById(R.id.blueInfo);
		blueState = (TextView) findViewById(R.id.title_text);
		mSuccess.setEnabled(false);
		 finished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		 found =  new IntentFilter(BluetoothDevice.ACTION_FOUND);
		 mAdapter = BluetoothAdapter.getDefaultAdapter();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		setBluetoothEnable();
		registerReceiver(bluetoothReceiver, finished);
		registerReceiver(bluetoothReceiver, found);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
		unregisterReceiver(bluetoothReceiver);
	}

	private void setBluetoothEnable() {
		
		isEnabled = mAdapter.isEnabled();
		if (mAdapter.isEnabled()) {
			notify = getString(R.string.bluetooth_opened);
			
			blueState.setText(notify);
			blueBuffer = new StringBuffer();
			if(adds == null)
			adds = new ArrayList<BluetoothDevice>();
			mAdapter.startDiscovery();
		} else {
			blueState.setText(R.string.bluetooth_opening);
			try {
				mAdapter.enable();
				openBlueHandler.sendEmptyMessageDelayed(OPENING, 1000);
			} catch (Exception e) {
				blueState.setText(R.string.bluetooth_openfail);
			}
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (isEnabled == true) {
			mAdapter.disable();
		}
		super.onDestroy();
		
	}


	private Handler openBlueHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == OPENING) {
				setBluetoothEnable();
			}
		}
	};


	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
		// StringBuffer blueBuffer = new StringBuffer();
		// List<BluetoothDevice> adds = new ArrayList<BluetoothDevice>();
		// int num = 1;
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			Log.i("messi", "onReceive");
			String action = intent.getAction();
			notify  = getString(R.string.device_mount);
			 if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				 Log.i("messi", "ACTION_FOUND");
				 mSuccess.setEnabled(true);
				status = getString(R.string.bluetooth_searching);
				BluetoothDevice bd = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if ((!adds.contains(bd))) {
					adds.add(bd);
					deviceMount++;
					// blueState.setText(getString(R.string.bluetooth_opened)+"\n"
					// +getString(R.string.bluetooth_search_result));
						blueBuffer.append(getString(R.string.bluetooth_name))
								.append(bd.getName()).append("\n");
						;
					blueBuffer.append(getString(R.string.bluetooth_mac))
							.append(bd.getAddress()).append("\n");
					blueInfo.setText(blueBuffer);
				}
			}
			else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				 Log.i("messi", "ACTION_DISCOVERY_FINISHED");
				mSuccess.setEnabled(true);
					status = getString(R.string.search_finish);
			}
			blueState.setText( notify+deviceMount+ "\n"
					+ status);
		}
	};

	public void onClick(View v) {
		if(mAdapter.isDiscovering() == true) {
			mAdapter.cancelDiscovery();
	    }
		super.onClick(v);
	};
	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_bluetooth);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		mSuccess.setEnabled(false);
		deviceMount =0;
		if(adds!=null)
			adds.clear();
		status = getString(R.string.bluetooth_searching);
		blueState.setText( notify+deviceMount+ "\n"
				+ status);
		blueBuffer.delete(0,blueBuffer.length());
		blueInfo.setText("");
		mAdapter.startDiscovery();
	}
}
