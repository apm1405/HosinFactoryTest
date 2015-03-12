package com.android.factorytest;



import android.os.Bundle;
import android.os.Vibrator;

public class VibrationTest extends BaseActivity {

	private Vibrator mVibrator;
	private long[] vibratorPatter = { 1000, 2000, 1000, 2000 } ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		mVibrator.vibrate(vibratorPatter, 0);
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if (mVibrator != null) {
			mVibrator.cancel();
		}
		super.onPause();
	}



	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.vibration);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		mVibrator.cancel();
		mVibrator.vibrate(vibratorPatter, 0);
		return ;
	}
}
