
package com.android.factorytest;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TestBackLightValue extends BaseActivity{
	
	int brightness;
	int brightness_saved;
	RelativeLayout lyout;
	private	LinearLayout buttonLayout;
	private LinearLayout notifyLayout;
	TextView tv;
	int count =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//…Ë÷√»´∆¡  
		
		
		tv = (TextView)findViewById(R.id.title_text);
		notifyLayout = (LinearLayout) findViewById(R.id.test_bg_notify);
		notifyLayout.setVisibility(View.VISIBLE);
		
		buttonLayout= (LinearLayout)findViewById(R.id.test_bg_buttons);
		buttonLayout.setVisibility(View.VISIBLE);
		
	}
	

	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_backgroundlight);
	}


	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(TestBackLightValue.this, TestBackgroundlight.class);
		intent.putExtra("position",getIntent().getIntExtra("position", 0));
		finish();
		startActivity(intent);
		return;
	}

}
