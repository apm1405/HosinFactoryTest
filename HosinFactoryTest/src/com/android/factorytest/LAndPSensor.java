package com.android.factorytest;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LAndPSensor extends Activity implements SensorEventListener {

	private Sensor mLSensor;
	private Sensor mPSensor;
	private SensorManager mSensorMgr;
	private TextView mLsIntro;
	private TextView mLsContent;
	private ImageView mLsImg;
	private TextView mPsIntro;
	private TextView mPsContent;
	private ImageView mPsImg;
	private Button mSuccess;
	private Button mFailure;
	private int[] mFlag = { 0, 0, 0 };

	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			sp = getSharedPreferences(TEST, 1);
			editor = getSharedPreferences(TEST, 2).edit();

			if (v == mSuccess) {
				editor.putInt("lpsensor", 1);
			} else {
				editor.putInt("lpsensor", 0);
			}
			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(LAndPSensor.this, KeyVibrationTest.class);
				startActivity(intent);
			}
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_lsensor_and_psensor);

		List<Sensor> sensor;

		mLsIntro = (TextView) findViewById(R.id.ls_intro);
		mLsContent = (TextView) findViewById(R.id.ls_content);
		mLsImg = (ImageView) findViewById(R.id.ls_img);

		mPsIntro = (TextView) findViewById(R.id.ps_intro);
		mPsContent = (TextView) findViewById(R.id.ps_content);
		mPsImg = (ImageView) findViewById(R.id.ps_img);

		mSuccess = (Button) findViewById(R.id.lp_sensor_success);
		mFailure = (Button) findViewById(R.id.lp_sensor_failure);

		mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensor = mSensorMgr.getSensorList(Sensor.TYPE_LIGHT);
		mLSensor = sensor.get(0);
		sensor = mSensorMgr.getSensorList(Sensor.TYPE_PROXIMITY);
		mPSensor = sensor.get(0);

		mSuccess.setOnClickListener(mListener);
		mSuccess.setVisibility(View.INVISIBLE);
		mFailure.setOnClickListener(mListener);

		mLsIntro.setText(R.string.ls_intro);
		mPsIntro.setText(R.string.ps_intro);
		mLsContent.setText(R.string.ls_text);
		mPsContent.setText(R.string.ps_text);
		mLsImg.setImageResource(R.drawable.lpsensor_state_blue_bar);
		mPsImg.setImageResource(R.drawable.lpsensor_state_blue_bar);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mSensorMgr.registerListener(this, mLSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorMgr.registerListener(this, mPSensor,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorMgr.unregisterListener(this, mLSensor);
		mSensorMgr.unregisterListener(this, mPSensor);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			mLsContent.setText(getString(R.string.ls_text) + event.values[0]);
			if (event.values[0] <= 25) {
				mLsImg.setImageResource(R.drawable.lpsensor_state_white_bar);
				mFlag[0] = 1;
			} else {
				mLsImg.setImageResource(R.drawable.lpsensor_state_blue_bar);
			}
		} else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			if (event.values[0] == 0) {
				mPsContent.setText(getString(R.string.ps_text)
						+ event.values[0]);
				mPsImg.setImageResource(R.drawable.lpsensor_state_white_bar);
				mFlag[1] = 1;
			} else {
				mPsContent.setText(getString(R.string.ps_text)
						+ event.values[0]);
				mPsImg.setImageResource(R.drawable.lpsensor_state_blue_bar);
			}
		}

		if (mFlag[0] == 1 && mFlag[1] == 1 && mFlag[2] == 0) {
			mSuccess.setVisibility(View.VISIBLE);
			mFlag[2] = 1;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

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
			finish();
			Intent intent = new Intent();
			intent.setClass(LAndPSensor.this, GPS.class);
			startActivity(intent);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
	    this.getWindow().addFlags(
		 		WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
		super.onAttachedToWindow();
	}

}
