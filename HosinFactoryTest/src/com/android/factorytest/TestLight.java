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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestLight extends Activity implements SensorEventListener {

	private Sensor sensor;
	private SensorManager sm;
	private TextView light;
	private Button light_ok;
	private Button light_false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_LIGHT);
		sensor = sensors.get(0);

		light = (TextView) findViewById(R.id.light);
		light_ok = (Button) findViewById(R.id.success);
		light_false = (Button) findViewById(R.id.failed);

		light_ok.setOnClickListener(mListener);
		light_false.setOnClickListener(mListener);

		light.setText(R.string.quantitative_value);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		sm.unregisterListener(this);
		super.onPause();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

			Log.i("SENSOR_LIGHT", event.values[0] + "");
			light.setText(getString(R.string.quantitative_value)
					+ event.values[0]);
		}
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

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
	//	this.getWindow().addFlags(
		//		WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
		super.onAttachedToWindow();
	}

	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		public void onClick(View v) {
			// TODO Auto-generated method stub
			sp = getSharedPreferences(TEST, MODE_WORLD_READABLE);
			editor = getSharedPreferences(TEST, MODE_WORLD_WRITEABLE).edit();
			if (v == light_ok) {
				editor.putInt("light", 1);
			} else {
				editor.putInt("light", 0);
			}
			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(TestLight.this, TestDistance.class);
				startActivity(intent);
			}

			finish();
		}

	};

}
