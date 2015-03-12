package com.android.factorytest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
public class Earphone extends Activity {

	private AudioManager audiomanager;
	IntentFilter headfilter;
	private TextView ep_text;
	private Button ep_ok;
	private Button ep_false;
	private Ringtone rt;
	private int LOOPING = 1;
	private boolean isPlay;
	private HeadsetPlugUnplugBroadcastReceiver mHeadsetPlugUnplugBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_earphone);
		ep_text = (TextView) findViewById(R.id.ep_text);
		ep_ok = (Button) findViewById(R.id.ep_ok);
		ep_false = (Button) findViewById(R.id.ep_false);

		ep_ok.setOnClickListener(mListener);
		ep_false.setOnClickListener(mListener);

		audiomanager = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);

		Uri defaultUri = null;
		defaultUri = Settings.System.DEFAULT_RINGTONE_URI;
		rt = RingtoneManager.getRingtone(this, defaultUri);
		rt.setStreamType(AudioManager.STREAM_DTMF);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (mHeadsetPlugUnplugBroadcastReceiver == null) {
			mHeadsetPlugUnplugBroadcastReceiver = new HeadsetPlugUnplugBroadcastReceiver();
		}
		Intent i = registerReceiver(mHeadsetPlugUnplugBroadcastReceiver,
				new IntentFilter(Intent.ACTION_HEADSET_PLUG));
		if (i != null)
			mHeadsetPlugUnplugBroadcastReceiver.onReceive(this, i);
		else
			wiredHeadsetIsOn(false);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (rt.isPlaying()) {
			rt.stop();
		}
		isPlay = false;
		this.unregisterReceiver(mHeadsetPlugUnplugBroadcastReceiver);
		super.onPause();
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
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			if (isPlay) {
				ep_text.setText(R.string.wiredheadsetonstartmusic);
				isPlay = false;
				rt.stop();
			} else {
				ep_text.setText(R.string.wiredheadsetonstopmusic);
				isPlay = true;
				rt.play();
			}
			return true;
		case KeyEvent.KEYCODE_HEADSETHOOK:
			if (isPlay) {
				ep_text.setText(R.string.wiredheadsetonstartmusic);
				isPlay = false;
				rt.stop();
			} else {
				ep_text.setText(R.string.wiredheadsetonstopmusic);
				isPlay = true;
				rt.play();
			}
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

	void testplay() {
		playHandler.sendEmptyMessageDelayed(LOOPING, 1000);
	}

	private Handler playHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if ((msg.what == LOOPING) && isPlay) {
				if (!rt.isPlaying()) {
					rt.play();
					Log.i("loop", "loop");
				}
				playHandler.sendEmptyMessageDelayed(LOOPING, 1000);
			}
		}
	};

	public class HeadsetPlugUnplugBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)) {
				int state = intent.getIntExtra("state", 0);
				// System.out.println("_______ HeadsetPlugUnplugBroadcastReceiver.onReceive() state="+state);
				Earphone.this.wiredHeadsetIsOn(state != 0);
			}
		}
	}

	private void wiredHeadsetIsOn(boolean isOn) {
		// TODO: fancy warning dialog
		ep_text.setText(isOn ? R.string.wiredheadsetonstopmusic
				: R.string.insert_wiredhead);
		if (isOn) {
			isPlay = true;
			testplay();
		} else {
			if (rt.isPlaying()) {
				isPlay = false;
				rt.stop();
			}
		}
	}

	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		public void onClick(View v) {
			// TODO Auto-generated method stub
			sp = getSharedPreferences(TEST, MODE_WORLD_READABLE);
			editor = getSharedPreferences(TEST, MODE_WORLD_WRITEABLE).edit();
			if (v == ep_ok) {
				editor.putInt("ep", 1);
			} else {
				editor.putInt("ep", 0);
			}
			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(Earphone.this, Gravity.class);
				startActivity(intent);
			}

			finish();
		}

	};

}
