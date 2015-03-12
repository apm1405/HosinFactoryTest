package com.android.factorytest;


import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TestLightSensor extends BaseActivity implements SensorEventListener {

	private Sensor sensor;
	private SensorManager sm;
	private TextView notify;
	private TextView light;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_LIGHT);
		sensor = sensors.get(0);
		
		notify = (TextView) findViewById(R.id.title_text);
		notify.setText(R.string.light_text);
		light = (TextView) findViewById(R.id.light);

		mSuccess.setEnabled(false);

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
			if(event.values[0]<=10){
				light.setBackgroundColor(Color.GREEN);
				mSuccess.setEnabled(true);
			}else{
				light.setBackgroundColor(Color.BLACK);
			}
		}
	}


	

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.light);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		mSuccess.setEnabled(false);
	}

}
