package com.android.factorytest;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TestBackgroundlight extends BaseActivity {


	int brightness;
	int brightness_saved;
	RelativeLayout lyout;
	TextView tv;
	private int count =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
		super.onCreate(savedInstanceState);
        
		lyout = (RelativeLayout)findViewById(R.id.backlight_layout);
		lyout.setOnClickListener(new LymListener());
		setBrightness(255);
		lyout.setBackgroundColor(Color.WHITE);
		brightness_saved=150;
	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		setBrightness(brightness_saved);
		super.onPause();
	}

	@Override
	protected void onResume() {
		setBrightness(brightness_saved);
		super.onResume();
	}

	

	@SuppressLint("NewApi")
	private void setBrightness(int brightness) {
		Window localWindow = getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow
				.getAttributes();
		float f = brightness / 255.0F;
		localLayoutParams.screenBrightness = f;
		localWindow.setAttributes(localLayoutParams);
	}


	
	class LymListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			count ++;
			if(count == 1){
				setBrightness(100);
				lyout.setBackgroundColor(Color.WHITE);
				//mResolver = getContentResolver();
				//Settings.System.putInt(mResolver, SCREEN_BRIGHTNESS, 150);
				return ;
			}
			if(count ==2){
				setBrightness(1);
				lyout.setBackgroundColor(Color.WHITE);
				//mResolver = getContentResolver();
				//Settings.System.putInt(mResolver, SCREEN_BRIGHTNESS, 1);
				return ;
			}
			if(count ==3){
				count =0;
				Intent intent = new Intent();
				intent.setClass(TestBackgroundlight.this, TestBackLightValue.class);
				intent.putExtra("position",getIntent().getIntExtra("position", 0));
				startActivity(intent);
				finish();
				return ;
			}
		}
	}

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_backgroundlight);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}
}
