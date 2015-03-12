package com.android.factorytest;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class KeyVibrationTest extends Activity {
	private Button mHome;
	private Button mMenu;
	private Button mBack;
	private Button mVolumeUp;
	private Button mVolumeDown;
	private Vibrator mVibrator;
	private Button mSuccess;
	private Button mFailure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.key_vibration);
		mHome = (Button) findViewById(R.id.home_button);
		mMenu = (Button) findViewById(R.id.menu_button);
		mBack = (Button) findViewById(R.id.back_button);
		mVolumeUp = (Button) findViewById(R.id.volume_up_button);
		mVolumeDown = (Button) findViewById(R.id.volume_down_button);

		mSuccess = (Button) findViewById(R.id.success);
		mFailure = (Button) findViewById(R.id.failure);

		mSuccess.setOnClickListener(mListener);
		mFailure.setOnClickListener(mListener);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		long[] vibratorPatter = { 1000, 1000, 1000, 1000 };
		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		mVibrator.vibrate(vibratorPatter, 0);
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			mHome.setTextColor(Color.BLUE);
			return true;
		case KeyEvent.KEYCODE_MENU:
			mMenu.setTextColor(Color.BLUE);
			return true;
		case KeyEvent.KEYCODE_BACK:
			mBack.setTextColor(Color.BLUE);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			mVolumeUp.setTextColor(Color.BLUE);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			mVolumeDown.setTextColor(Color.BLUE);
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
	//	this.getWindow().addFlags(
	//			WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
		super.onAttachedToWindow();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (mVibrator != null) {
			mVibrator.cancel();
		}

		super.onPause();
	}

	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		public void onClick(View v) {
			// TODO Auto-generated method stub
			sp = getSharedPreferences(TEST, 1);
			editor = getSharedPreferences(TEST, 2).edit();
			if (v == mSuccess) {
				editor.putInt("keyandvibrationtest", 1);
			} else {
				editor.putInt("keyandvibrationtest", 0);
			}
			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(KeyVibrationTest.this,
						TestRadioAndEarphone.class);
				startActivity(intent);
			}
			finish();
		}

	};
}
