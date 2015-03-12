package com.android.factorytest;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GPS extends BaseActivity {

	private TextView mGPSInfo;
	private TextView mSatellite;
	private TextView time;
	private LocationManager mLocationMgr;
	private StringBuffer mStringBuffer;
	private static final int OPENING = 1;
	private boolean mGpsFlag;
	private boolean mOKFlag = true;
	private List<GpsSatellite> mGpsSatelliteList;
	private String mSatelliteInfo;
	private boolean isOK = false;
	private static final int EVENT_TICK = 1;
	private int wasteTime=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		mSuccess.setEnabled(false);
		
		time = (TextView) findViewById(R.id.gps_test_time);
		mGPSInfo = (TextView) findViewById(R.id.gpsInfo);
		mSatellite = (TextView) findViewById(R.id.statellite);


		mLocationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationMgr.addGpsStatusListener(statusListener);
		handler.sendEmptyMessage(1);
	}

		
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			
			time.setText(getString(R.string.time)+wasteTime);
			wasteTime++;
			if(1 == msg.what){
					handler.sendEmptyMessageDelayed(1, 1000);
			}
		};
	};


	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if ((msg.what == EVENT_TICK) && isOK) {
				isOK();
			}
			mHandler.sendEmptyMessageDelayed(EVENT_TICK, 0);
		}
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mGpsFlag = true;
		gpsSearch();
		//mHandler.sendEmptyMessageDelayed(EVENT_TICK, 0);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mGpsFlag = false;
		mOKFlag = false;
	}



	private void gpsSearch() {

		if (!mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			mGPSInfo.setText(R.string.gps_closed);
			openGPSHandler.sendEmptyMessageDelayed(OPENING, 5000);

		} else {
			mGPSInfo.setText(R.string.gps_opened);

			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(true);
			criteria.setBearingRequired(true);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);

			String provider = mLocationMgr.getBestProvider(criteria, true);
			Location location = mLocationMgr.getLastKnownLocation(provider);
			showLocalGpsInfo(location);
			openGPSHandler.sendEmptyMessageDelayed(OPENING, 1000);
			mLocationMgr.requestLocationUpdates(provider, 1000, 0,
					new LocationListener() {

				@Override
				public void onLocationChanged(Location location) {
					// TODO Auto-generated method stub
					showLocalGpsInfo(location);
				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					showLocalGpsInfo(null);
				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStatusChanged(String provider,
						int status, Bundle extras) {
					// TODO Auto-generated method stub
				}
			});
		}
	}

	private GpsStatus.Listener statusListener = new GpsStatus.Listener() {

		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub
			mGpsSatelliteList = new ArrayList<GpsSatellite>();
			GpsStatus status = mLocationMgr.getGpsStatus(null);
			mSatelliteInfo = updateGpsStatus(event, status);
			mSatellite.setText(mSatelliteInfo);
		}
	};

	private String updateGpsStatus(int event, GpsStatus status) {
		StringBuilder gpsInfoBuffer = new StringBuilder("");
		String gpsContent = "";
		if (status == null) {
			gpsInfoBuffer.append("Satellite Count = " + 0);
		} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = status.getMaxSatellites();
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
			mGpsSatelliteList.clear();
			int count = 0;
			int satelliteCount = 0;
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite s = it.next();
				mGpsSatelliteList.add(s);
				count++;
				gpsContent += "\n" + count + "." + s.getSnr() + "dB";

				if (s.getSnr() >= 40.0) {
					satelliteCount++;
				}

				if (satelliteCount >= 3) {
					mSuccess.setEnabled(true);
			//		isOK = true;
				}
			}
			gpsInfoBuffer.append("Satellite Count = "
					+ mGpsSatelliteList.size() + gpsContent);
		}

		return gpsInfoBuffer.toString();
	}

	private void isOK() {
		SharedPreferences sp;
		SharedPreferences.Editor editor;
		String TEST = "facotytest";

		sp = getSharedPreferences(TEST, 1);
		editor = getSharedPreferences(TEST, 2).edit();
		isOK = false;
		editor.putInt("gps", 1);
		editor.commit();
		if (sp.getBoolean("all", false) && mOKFlag) {
			mOKFlag = false;
			Intent intent = new Intent();
			intent.setClass(GPS.this, LAndPSensor.class);
			startActivity(intent);
		}
		finish();
	}
	
	private Handler openGPSHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if ((msg.what == OPENING) && mGpsFlag) {
				gpsSearch();
				mSatellite.invalidate();
				mGPSInfo.invalidate();
			}
		}
	};

	private void showLocalGpsInfo(Location location) {
		mStringBuffer = new StringBuffer();
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			double speed = location.getSpeed();
			double altitude = location.getAltitude();
			double bearing = location.getBearing();

			mStringBuffer
			.append(getString(R.string.gps_opened) + "\n")
			.append(getString(R.string.gps_location) + "\n")
			.append(getString(R.string.gps_latitude) + latitude
					+ getString(R.string.gps_longitude) + longitude
					+ getString(R.string.gps_speed) + speed
					+ getString(R.string.gps_altitude) + altitude
					+ getString(R.string.gps_bearing) + bearing);
			mGPSInfo.setText(mStringBuffer);
		} else {
			mStringBuffer.append(getString(R.string.gps_opened) + "\n")
			.append(getString(R.string.gps_location) + "\n")
			.append(getString(R.string.gps_unknown));
			mGPSInfo.setText(mStringBuffer);
		}

		if (mSatelliteInfo == null) {
			mSatelliteInfo = "Don't find out Satellite";
		}
		mSatellite.setText(mSatelliteInfo);
	}
	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_gps);
	}
	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		mSuccess.setEnabled(false);
		mSatellite.setText("");
		mGPSInfo.setText("");
		wasteTime = 0;
		gpsSearch();
	}
}
