package com.android.factorytest;


import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("NewApi")
public class Flashlight extends BaseActivity {

	private Button flashlight_switch;
	private Camera camera = null;
	private Camera.Parameters parameters = null;
	private boolean isOn = false;
	private Vibrator mVibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		flashlight_switch = (Button) findViewById(R.id.flashlight_switch);
		setRetestBtnVisible(false);
		mSuccess.setEnabled(false);
		
		flashlight_switch.setOnClickListener(switchListener);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// setFlashlightOn(true);
		//flashlight_switch.setText(isOn ? R.string.turn_off : R.string.turn_on);
		super.onResume();
	}

	private boolean setFlashlightOn(boolean on) {
		if (on) {
			if (camera == null) {
				camera = Camera.open();
				camera.startPreview();
				parameters = camera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameters);
				flashlight_switch.setBackgroundResource(R.drawable.fl);
				mSuccess.setEnabled(true);
				
			}
		} else {
			if (camera != null) {
				parameters = camera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameters);
				camera.release();
				camera = null;
				flashlight_switch.setBackgroundResource(R.drawable.floff);
			}
		}
		return on;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isOn = setFlashlightOn(false);
		super.onPause();
	}



	private OnClickListener switchListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			mVibrator.vibrate(100);
			isOn = setFlashlightOn(!isOn);
			
			//flashlight_switch.setText(isOn ? R.string.turn_off: R.string.turn_on);
					
		}
	};


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.flashlight);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}

}
