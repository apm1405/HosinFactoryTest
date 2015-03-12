package com.android.factorytest;


import java.util.List;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class TestDistance extends BaseActivity implements SensorEventListener {

	private Sensor sensor;
	private SensorManager sm;
	private TextView distance;
	private TextView notify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_PROXIMITY);
		sensor = sensors.get(0);
		
		notify = (TextView) findViewById(R.id.title_text);
		distance = (TextView) findViewById(R.id.distance);
		
		notify.setText(R.string.distance_text);
		mSuccess.setEnabled(false);
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
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			if (event.values[0] == 0) {
				
				distance.setBackgroundColor(Color.GREEN);
				distance.setText(R.string.distance_state_close_to);
				mSuccess.setEnabled(true);
			} else {
				
				distance.setBackgroundColor(Color.BLACK);
				distance.setText(R.string.distance_state_far_away);
			}
		}
	}




	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.distance);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		mSuccess.setEnabled(false);
	}

}
