package com.android.factorytest;


import java.util.List;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Gravity extends BaseActivity implements SensorEventListener {

	private Sensor sensor;
	private SensorManager sm;
	private ImageView gravity_image;
	private TextView xyz;
	private boolean mBottomFlag = false;
	private boolean mTopFlag = false;
	private boolean mLeftFlag = false;
	private boolean mRightFlag = false;
	private  final String TAG = "Gravity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
		sensor = sensors.get(0);
		
		xyz = (TextView)findViewById(R.id.xyz);
		gravity_image = (ImageView) findViewById(R.id.gravity_image);

		mSuccess.setEnabled(false);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		sm.unregisterListener(this);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			Log.d(TAG, "X:" + x);
			Log.d(TAG, "Y:" + y);
			Log.d(TAG, "Z:" + z);
			xyz.setText("X:" + x  + "\nY:" + y + "\nZ:" + z);
			// int accuracy = event.accuracy;
			if(Math.abs(z)>7){
				gravity_image.setImageResource(R.drawable.grao);
			}
			if(Math.abs(x)>7){
				if(x>7){
					gravity_image.setImageResource(R.drawable.grar);
					mRightFlag = true;	
				}else{
					gravity_image.setImageResource(R.drawable.gral);
					mLeftFlag = true;	
				}
			}
			
			if(Math.abs(y)>7){
				if(y>7){
					gravity_image.setImageResource(R.drawable.gra);
					mTopFlag = true;	
				}else{
					gravity_image.setImageResource(R.drawable.grad);
					mBottomFlag = true;	
				}
				
			}
		}

		if (mBottomFlag == true && mTopFlag == true && mLeftFlag == true
				&& mRightFlag == true) {
			mSuccess.setEnabled(true);
		}
	}


	


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.gravity);
	}


	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		mSuccess.setEnabled(false);
		mBottomFlag = false;
		mTopFlag = false;
		mLeftFlag = false;
		mRightFlag = false;
	}
}
