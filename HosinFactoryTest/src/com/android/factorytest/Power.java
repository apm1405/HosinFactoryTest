package com.android.factorytest;

import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class Power extends BaseActivity {

	private IntentFilter mFilter;
	private StringBuffer powerInfo;
	private TextView power_info;
	// private TextView current_electric;
	private TextView uptime;
	private static final int EVENT_TICK = 1;
	private boolean mRun = false;

	private TextView mInfo = null;
	private String mCmdString = null;
	private static final int EVENT_UPDATE = 1;
	private static final float FORMART_TEN = 10.0f;
	private static final int UPDATE_INTERVAL = 1500;
	private TextView batteryStatus;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == EVENT_TICK) {
				uptime.setText(getString(R.string.power_up_time) + mUptime());
				sendEmptyMessageDelayed(EVENT_TICK, 1000);
				
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		batteryStatus = (TextView) findViewById(R.id.title_text);
		batteryStatus.setText(getString(R.string.power_charge_state));
		power_info = (TextView) findViewById(R.id.power_info);
		// current_electric = (TextView) findViewById(R.id.current_electric);
		uptime = (TextView) findViewById(R.id.uptime);
		
		setRetestBtnVisible(false);
		
		mFilter = new IntentFilter();
		mFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		/*
		 * if (ChipSupport.getChip() == ChipSupport.MTK_6573_SUPPORT) {
		 * mCmdString = "cat /sys/devices/platform/mt6573-battery/"; } else if
		 * (ChipSupport.getChip() == ChipSupport.MTK_6575_SUPPORT) { //
		 * mCmdString = "cat /sys/devices/platform/mt6575-battery/"; // Jelly
		 * Bean (James Lo) mCmdString =
		 * "cat /sys/devices/platform/mt6329-battery/"; } else if
		 * (ChipSupport.getChip() == ChipSupport.MTK_6577_SUPPORT) { // 6577
		 * branch // Jelly Bean (James Lo) // mCmdString =
		 * "cat /sys/devices/platform/mt6577-battery/"; mCmdString =
		 * "cat /sys/devices/platform/mt6329-battery/"; } else if
		 * (ChipSupport.getChip() == ChipSupport.MTK_6589_SUPPORT) { mCmdString
		 * = "cat /sys/devices/platform/mt6320-battery/"; } else if
		 * (ChipSupport.getChip() > ChipSupport.MTK_6589_SUPPORT) { mCmdString =
		 * "cat /sys/devices/platform/battery/"; } else { mCmdString = ""; }
		 */

		mCmdString = "cat /sys/devices/platform/battery/";

	}

	private String getInfo(String cmd) {
		String result = null;
		try {
			String[] cmdx = { "/system/bin/sh", "-c", cmd }; // file must
			// exist// or
			// wait()
			// return2
			int ret = ShellExe.execCommand(cmdx);
			if (0 == ret) {
				result = ShellExe.getOutput();
			} else {
				// result = "ERROR";
				result = ShellExe.getOutput();
			}
		} catch (IOException e) {
			result = "ERR.JE";
		}
		return result;
	}

	public Handler mUpdateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_UPDATE:
				Bundle b = msg.getData();
				// current_electric.setText(getString(R.string.power_battery_current_electric)
				// + b.getString("INFO") + "mA");
				break;
			default:
				break;
			}
		}
	};

	class FunctionThread extends Thread {

		@Override
		public void run() {
			while (mRun) {
				StringBuilder text = new StringBuilder("");
				String cmd = "";
				cmd = mCmdString + "G_BatteryAverageCurrent";
				text.append(getInfo(cmd));
				Bundle b = new Bundle();
				b.putString("INFO", text.toString());

				Message msg = new Message();
				msg.what = EVENT_UPDATE;
				msg.setData(b);

				mUpdateHandler.sendMessage(msg);
				try {
					sleep(UPDATE_INTERVAL);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mRun = false;
		unregisterReceiver(mr);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessageDelayed(EVENT_TICK, 1000);
		startRun();
		super.onResume();
	}


	void startRun() {
		Log.i("run", "run");
		mRun = true;
		new FunctionThread().start();
		registerReceiver(mr, mFilter);
	}

	BroadcastReceiver mr = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			powerInfo = new StringBuffer();
			batteryStatus.setText(getString(R.string.power_charge_state));
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				Getstatus(intent);
				if (Getplugged(intent) != null) {
					batteryStatus.append(Getplugged(intent));
				}
				powerInfo.append(
					 getString(R.string.power_current_electric))
						.append(intent.getIntExtra("level", 0));
				powerInfo.append(
						"\n" + getString(R.string.power_total_electric))
						.append(intent.getIntExtra("scale", 0));
				powerInfo.append("\n" + getString(R.string.power_state))
						.append(Gethealth(intent));
				powerInfo
						.append("\n"
								+ getString(R.string.power_current_voltage))
						.append(intent.getIntExtra("voltage", 0)).append("mV");
				powerInfo
				.append("\n"
						+ getString(R.string.power_current_temp))
				.append((intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0))*0.1).append("¡æ");
			//	powerInfo
			//	.append("\n"
			//			+ getInfo("cat /sys/class/power_supply/battery/uevent"));
				power_info.setText(powerInfo);
				
				uptime.setText(getString(R.string.power_up_time) + mUptime());
			}
			Log.i("x", powerInfo.toString());

		}

		// Getstatus
		void Getstatus(Intent intent) {
			String status = null;
			mSuccess.setEnabled(true);
			switch (intent.getIntExtra("status",
					BatteryManager.BATTERY_STATUS_UNKNOWN)) {
			case BatteryManager.BATTERY_STATUS_CHARGING:
				status = getString(R.string.power_charging);
				batteryStatus.append(status);
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				status = getString(R.string.power_discharging);
				batteryStatus.append(status);
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				status = getString(R.string.power_full);
				batteryStatus.append(status);
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				status = getString(R.string.power_not_charging);
				batteryStatus.setText(getString(R.string.please_charge));
				mSuccess.setEnabled(false);
			//	power_ok.setBackground(new ColorDrawable(getResources().getColor(R.color.gray)));
			//	power_ok.setTextColor(getResources().getColor(R.color.gray));
				break;
			}
			return ;

		}

		// Getplugged
		String Getplugged(Intent intent) {
			String plugged = null;
			switch (intent.getIntExtra("plugged",
					BatteryManager.BATTERY_PLUGGED_AC)) {
			case BatteryManager.BATTERY_PLUGGED_AC:
				plugged = getString(R.string.power_ac);
				break;
			case BatteryManager.BATTERY_PLUGGED_USB:
				plugged = getString(R.string.power_usb);
				break;
			}
			return plugged;

		}

		// Gethealth
		String Gethealth(Intent intent) {
			String health = null;
			switch (intent.getIntExtra("health",
					BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
			case BatteryManager.BATTERY_HEALTH_DEAD:
				health = getString(R.string.battery_heath_dead);
				break;
			case BatteryManager.BATTERY_HEALTH_GOOD:
				health = getString(R.string.battery_heath_good);
				break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				health = getString(R.string.battery_heath_over_voltage);
				break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				health = getString(R.string.battery_heath_over_heat);
				break;
			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
				health = getString(R.string.battery_heath_unknown);
				break;
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				health = getString(R.string.battery_heath_failure);
				break;
			
			}
			return health;

		}

	};

	// mUptime
	String mUptime() {
		long uptime = SystemClock.elapsedRealtime();

		return (DateUtils.formatElapsedTime(uptime / 1000));

	}


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.power);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}
}
