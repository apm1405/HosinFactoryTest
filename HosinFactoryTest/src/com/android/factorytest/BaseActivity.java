package com.android.factorytest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public  abstract class BaseActivity extends Activity implements OnClickListener    {
	public static boolean isAuto = false;
	public int position;
	public Button mSuccess;
	public Button mFailed;
	public Button mRetest;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setLayout();
		initButtons();
		position = getIntent().getIntExtra("position", 0);
		Log.i("messi","position: "+position);
	}

	public void initButtons() {
		mSuccess = (Button) findViewById(R.id.success);
		mFailed = (Button) findViewById(R.id.failed);
		mRetest = (Button) findViewById(R.id.retest);
		mSuccess.setOnClickListener(this);
		mFailed.setOnClickListener(this);
		mRetest.setOnClickListener(this);
	}
	protected  void setRetestBtnVisible(boolean visible){
		if(visible)
			mRetest.setVisibility(View.VISIBLE);
		else 
			mRetest.setVisibility(View.INVISIBLE);
	}
	abstract void setLayout();
	abstract void retestOnClick();	
		
	
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
			if(position>0){
				finish();
				Intent intent = new Intent();
				intent.putExtra("position", position-1);
				intent.setClass(this,MMITestData.testItems[position-1]);
				startActivity(intent);
			}
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		SharedPreferences sp = getSharedPreferences("facotytest", 1);
		Editor editor = getSharedPreferences("facotytest", 2).edit();
		if (v == mSuccess) {
			editor.putInt(MMITestData.keys[position], 1);
			editor.commit();
			if (sp.getBoolean("all", false) && position< MMITestData.testItems.length) {
					Intent intent = new Intent();
					intent.setClass(this, MMITestData.testItems[position+1]);
					intent.putExtra("position", position+1);
					startActivity(intent);
				
			}
			finish();
		} 
		
		else if(v == mFailed){
			editor.putInt(MMITestData.keys[position], 0);
			editor.commit();
			if (sp.getBoolean("all", false) && position< MMITestData.testItems.length) {
				Intent intent = new Intent();
				intent.setClass(this, MMITestData.testItems[position+1]);
				intent.putExtra("position", position+1);
				startActivity(intent);
			}
			finish();
		}
		if(v == mRetest){
			retestOnClick();
		}
	}
}
