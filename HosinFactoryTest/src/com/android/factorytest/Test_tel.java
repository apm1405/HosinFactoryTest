package com.android.factorytest;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Test_tel extends Activity {

	private Button tel_ok;
	private Button tel_false;
	private Button tel_call;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_tel);

		tel_ok = (Button) findViewById(R.id.tel_ok);
		tel_false = (Button) findViewById(R.id.tel_false);
		tel_call = (Button) findViewById(R.id.tel_call);

		tel_ok.setOnClickListener(mListener);
		tel_false.setOnClickListener(mListener);
		tel_call.setOnClickListener(callOnClickListener);
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
//				WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
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
			if (v == tel_ok) {
				editor.putInt("tel", 1);
			} else {
				editor.putInt("tel", 0);
			}
			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(Test_tel.this, TestSubCamera.class);
				startActivity(intent);
			}

			finish();
		}

	};

	private OnClickListener callOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// Intent callIntent = new
			// Intent("android.intent.action.CALL_BUTTON");
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			startActivity(callIntent);
		}
	};

}
