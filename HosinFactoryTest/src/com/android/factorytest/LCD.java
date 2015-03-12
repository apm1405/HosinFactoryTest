package com.android.factorytest;


import android.app.ActionBar;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LCD extends BaseActivity {

	private static final int CHANGETIME = 1000;
	private RelativeLayout mLayout;
	private int mNum = 0;
	private final static int CHANGING = 1;
	private boolean isOver = false;
	private LinearLayout layout ;
	private int[] colors = {Color.RED,Color.GREEN,Color.BLUE,Color.BLACK,Color.WHITE};
	private int current = 0;
	private View buttons;
	private View notify;
	private ActionBar bar;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}



	private void initView() {
		bar = getActionBar();
		bar.hide();
		mLayout = (RelativeLayout) findViewById(R.id.layout);

		layout = (LinearLayout) findViewById(R.id.lcd_test_layout);
		layout.setBackgroundColor(colors[current]);
		buttons = findViewById(R.id.lcd_test_buttons);
		notify = findViewById(R.id.lcd_test_nofity);
		 
		layout.setOnClickListener(mListener);

		//mHandler.sendEmptyMessageDelayed(CHANGING, CHANGETIME);
	}
	
/*	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == CHANGING) {
				mNum++;
				changeColor(mNum);
			}
		}
	};*/

	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*int id = v.getId();
			sp = getSharedPreferences(TEST, 1);
			editor = getSharedPreferences(TEST, 2).edit();
			switch (id) {
			case R.id.success:
				editor.putInt("lcd", 1);
				editor.commit();
				if (sp.getBoolean("all", false)) {
					Intent intent = new Intent();
					intent.setClass(LCD.this, TestKey.class);
					startActivity(intent);
				}
				finish();
				break;	
			case R.id.failed:
				editor.putInt("lcd", 0);
				editor.commit();
				if (sp.getBoolean("all", false)) {
					Intent intent = new Intent();
					intent.setClass(LCD.this, TestKey.class);
					startActivity(intent);
				}
				finish();
				break;
			case R.id.retest:
				notify.setVisibility(View.GONE);
				buttons.setVisibility(View.GONE);
				bar.hide();
				layout.setVisibility(View.VISIBLE);
				
				break;
			case R.id.lcd_test_layout:*/
				current++;
				if(current>colors.length-1){
					current = 0;
					bar.show();
					layout.setVisibility(View.GONE);
					notify.setVisibility(View.VISIBLE);
					buttons.setVisibility(View.VISIBLE);
					
					
				}
				layout.setBackgroundColor(colors[current]);
			
			}
		
			

	};
	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.lcd);
	}
	/*private void changeColor(int num) {
		switch (num % 6) {
		case 0:
			mLayout.setBackgroundColor(Color.WHITE);
			mHandler.sendEmptyMessageDelayed(CHANGING, CHANGETIME);
			break;
		case 1:
			mLayout.setBackgroundColor(Color.RED);
			mHandler.sendEmptyMessageDelayed(CHANGING, CHANGETIME);
			break;
		case 2:
			mLayout.setBackgroundColor(Color.GREEN);
			mHandler.sendEmptyMessageDelayed(CHANGING, CHANGETIME);
			break;
		case 3:
			mLayout.setBackgroundColor(Color.BLUE);
			mHandler.sendEmptyMessageDelayed(CHANGING, CHANGETIME);
			break;
		case 4:
			mLayout.setBackgroundColor(Color.BLACK);
			mHandler.sendEmptyMessageDelayed(CHANGING, CHANGETIME);
			break;
		}
	}*/



	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		notify.setVisibility(View.GONE);
		buttons.setVisibility(View.GONE);
		bar.hide();
		layout.setVisibility(View.VISIBLE);
	}
}
