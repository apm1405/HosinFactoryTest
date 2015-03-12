package com.android.factorytest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Memory extends Activity {

	private TextView memorytext;
	private Button memory_ok;
	private Button memory_false;
	private final String MTD_PATH = "/proc/mtd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_memory);
		memorytext = (TextView) findViewById(R.id.memorytext);
		memory_ok = (Button) findViewById(R.id.memory_ok);
		memory_false = (Button) findViewById(R.id.memory_false);

		memory_ok.setOnClickListener(mListener);
		memory_false.setOnClickListener(mListener);
		/*
		 * StringBuffer memoryInfo = new StringBuffer();
		 * memoryInfo.append(getString
		 * (R.string.total_memory)).append(getTotalMemory
		 * ()).append(getString(R.string
		 * .avail_memory)).append(getAvailMemory());
		 * memorytext.setText(memoryInfo);
		 * 
		 * TelephonyManager tm = (TelephonyManager)
		 * getSystemService(Context.TELEPHONY_SERVICE); tm.getDeviceId();
		 * 
		 * Log.i("DeviceId",tm.getDeviceId());
		 */
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		File file = new File(MTD_PATH);
		BufferedReader reader = null;
		StringBuffer memoryInfo = new StringBuffer();
		memoryInfo.append(getString(R.string.total_memory))
				.append(getTotalMemory() + "	")
				.append(getString(R.string.avail_memory))
				.append(getAvailMemory() + "\n");
		try {
			reader = new BufferedReader(new FileReader(file));
			String tmpString = null;
			String tmpRead = null;
			int line = 1;
			while ((tmpRead = reader.readLine()) != null) {
				if (line == 2) {
					tmpString += "\n" + tmpRead;
					break;
				} else {
					tmpString = tmpRead;
				}
				tmpRead = null;
				line++;
			}
			reader.close();
			reader = null;
			memoryInfo.append(tmpString);
			memorytext.setText(memoryInfo);
		} catch (IOException e) {
			Log.i("/proc/mtd", "read file failed");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					Log.i("/proc/mtd", "read file failed");
				}
			}
		}
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
		//		WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);
		super.onAttachedToWindow();
	}

	String getTotalMemory() {
		String minfo = "/proc/meminfo";
		String strs;
		String[] arrayOfString;
		long totalMemory = 0;
		try {
			FileReader mFileReader = new FileReader(minfo);
			BufferedReader mBufferedReader = new BufferedReader(mFileReader,
					8129);
			strs = mBufferedReader.readLine();
			arrayOfString = strs.split("\\s+");
			Log.i("arrayOfString", "\\s+");
			totalMemory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			Log.i("totalMemory", arrayOfString[1]);
			mBufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Formatter.formatFileSize(getBaseContext(), totalMemory);
	}

	String getAvailMemory() {
		long am = 0;
		ActivityManager mam = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		mam.getMemoryInfo(info);
		am = info.availMem;
		return Formatter.formatFileSize(Memory.this, am);

	}

	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		public void onClick(View v) {
			// TODO Auto-generated method stub
			sp = getSharedPreferences(TEST, MODE_WORLD_READABLE);
			editor = getSharedPreferences(TEST, MODE_WORLD_WRITEABLE).edit();
			if (v == memory_ok) {
				editor.putInt("memory", 1);
			} else {
				editor.putInt("memory", 0);
			}
			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(Memory.this, TestSd.class);
				startActivity(intent);
			}

			finish();
		}

	};
}
