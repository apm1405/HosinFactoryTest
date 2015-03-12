package com.android.factorytest;



import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class TestWiFi extends BaseActivity {

	private WifiManager wm;
	private TextView titleText;
	private TextView wifiInfo;
	private static final int EVENT_TICK = 1;
	private int wifi_state;
	private boolean isSend = true;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		wm = (WifiManager) getSystemService(WIFI_SERVICE);
		wifi_state = wm.getWifiState();
		
		titleText = (TextView) findViewById(R.id.title_text);
		wifiInfo = (TextView) findViewById(R.id.wifiInfo);
		mSuccess.setEnabled(false);
		titleText.setText(getString(R.string.wifi_search_result));
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if ((msg.what == EVENT_TICK) && isSend) {
				SetwifiStateEnabled();
			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isSend = false;
		if (wifi_state == WifiManager.WIFI_STATE_DISABLED) {
			wm.setWifiEnabled(false);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isSend = true;
		SetwifiStateEnabled();
		super.onResume();
	}



	private void SetwifiStateEnabled() {
		wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (wm.isWifiEnabled()) {
			if (wm.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
				try {
					wifiInfo.setText(wifiinfo());
				} catch (Exception e) {

				}
			} else {
				wifiInfo.setText(R.string.wifi_opening);
			}
		} else {
			wifiInfo.setText(R.string.wifi_open_wifi);
			wm.setWifiEnabled(true);
		}
		mHandler.sendEmptyMessageDelayed(EVENT_TICK, 1000);
	}

	private StringBuffer wifiinfo() {
		StringBuffer info = null;
		
		info = new StringBuffer();
		List<ScanResult> sr = null;
		sr = wm.getScanResults();
		if (sr.size() != 0) {

			for (int i = 0; i < sr.size(); i++) {
				ScanResult s = sr.get(i);
				info.append("\n").append(i + 1).append("  ").append(s.SSID).append("\n").append(s.BSSID).append("\n")
				.append(s.capabilities).append("\n").append(s.frequency).append("  ").append(s.level).append("\n");
				if(s.SSID.equals("88888888888888888888888888888888")){
					if(s.level>=-80){
						mSuccess.setEnabled(true);
					}
				}
			}
		} else {
			info.append(getString(R.string.wifi_searching) + "\n");
		}
		return info;

	}



	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_wifi);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		wifiInfo.setText("");
		mSuccess.setEnabled(false);
		mHandler.sendEmptyMessageDelayed(EVENT_TICK, 1000);
		return ;
	}

}
