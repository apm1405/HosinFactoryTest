package com.android.factorytest;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class FactorytestActivity extends Activity  {
	//private Button mButtonAllTest = null;
	private Button mButtonResetPhone = null;
	
	private Button mButtonExit = null;

	private PowerManager.WakeLock wakeLock = null;
	private PowerManager myPowerManager = null;
	
	private GridView gridView;//messi change all Button to a GridView 
	/* @Haoran.Xu 2013.06.05 Optimize the factory test,open WIFI and BT first */
	private WifiManager mWifiMgr;
	private BluetoothAdapter mAdapter;		
	public List<BluetoothDevice> mBTDevList;
	private MyAdapter adapter;
	private Intent intent;
	private SharedPreferences sp;
	public static final String TAG = "position";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		gridView = (GridView) findViewById(R.id.factory_test_gridview);
		adapter = new MyAdapter(this);
		gridView.setAdapter(adapter);
		 intent = new Intent();
		Intent i = new Intent();
		i.setComponent(new ComponentName("com.mediatek.FMRadio",  "com.mediatek.FMRadio.FMRadioService"));
		stopService(i);
		
	//	mButtonAllTest = (Button) findViewById(R.id.all_test);
	//	mButtonAllTest.setOnClickListener(new allListener());

		String version = SystemProperties.get("ro.custom.build.version", null);
	//	if (version != null) {
	//		mButtonAllTest.setText(getString(R.string.but_all_test) + "("
	//				+ android.os.Build.DISPLAY + ")");
	//	}

		
		mButtonResetPhone = (Button) findViewById(R.id.reset_button);
		mButtonResetPhone.setOnClickListener(new resetPhoneListener());

		mButtonExit = (Button) findViewById(R.id.exit_button);
		mButtonExit.setOnClickListener(new exitListener());

		Settings.Secure.setLocationProviderEnabled(getContentResolver(),
			LocationManager.GPS_PROVIDER, true);

		/*
		 * @Haoran.Xu 2013.06.05 Optimize the factory test,open WIFI and BT
		 * first
		 */
		openWIFIAndBTWhenEntryFT();

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "PowerManager");
		wakeLock.acquire();
	}

	// @Haoran.Xu 2013.06.05 Optimize the factory test,open WIFI and BT first
	private void openWIFIAndBTWhenEntryFT() {
		mWifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
		mWifiMgr.setWifiEnabled(true);
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		mAdapter.enable();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		final String TEST = "facotytest";
		SharedPreferences.Editor editor = getSharedPreferences(TEST, 2).edit();
		editor.putBoolean("all", false);
		editor.commit();
		sp = getSharedPreferences(TEST, 1);
		adapter.notifyDataSetChanged();//刷新button的字体颜色以判断时候测试通过
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			return true;
		case KeyEvent.KEYCODE_MENU:
			return true;
		case KeyEvent.KEYCODE_BACK:
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		default:
			return false;
		}
	}
	class MyAdapter extends BaseAdapter{
		private Context mContext;
		public MyAdapter(Context context){
			mContext = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MMITestData.itemNameIds.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return MMITestData.itemNameIds[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
				convertView = LayoutInflater.from(mContext).inflate(R.layout.factory_test_item, null);
				Button button = (Button) convertView.findViewById(R.id.test_grid_item);
				button.setTextSize(15);
				button.setText(mContext.getString(MMITestData.itemNameIds[position]));
				int testflag = sp.getInt(MMITestData.keys[position], -1);
				switch (testflag) {
				case -1:
					break;
				case 0:
					button.setTextColor(Color.RED);
					break;
				case 1:
					button.setTextColor(Color.BLUE);
					break;
				}
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(FactorytestActivity.this,MMITestData.testItems[position]);
						intent.putExtra(TAG, position);
						FactorytestActivity.this.startActivity(intent);
					}
				});
				
			return convertView;
		}
		
	}
	// Factory Test all test
	class allListener implements OnClickListener {

		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			editor = getSharedPreferences(TEST, MODE_WORLD_WRITEABLE).edit();
			editor.clear();
			editor.putBoolean("all", true);
			editor.commit();

			intent.setClass(FactorytestActivity.this, VersionInfo.class);
			FactorytestActivity.this.startActivity(intent);
		}
	}
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (wakeLock != null) {
			wakeLock.release();
		}
		Settings.Secure.setLocationProviderEnabled(getContentResolver(),
				LocationManager.GPS_PROVIDER, false);

		mWifiMgr.setWifiEnabled(false);
		if (mAdapter.isEnabled()) {
			mAdapter.disable();
		}

	}


	// Factory Reset Phone
	class resetPhoneListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setAction("android.settings.PRIVACY_SETTINGS");
			FactorytestActivity.this.startActivity(intent);
		}
	}

	// Factory Test exit
	class exitListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();

		}
	}

}
