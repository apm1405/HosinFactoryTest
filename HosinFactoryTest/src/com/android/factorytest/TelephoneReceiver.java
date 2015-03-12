package com.android.factorytest;


import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class TelephoneReceiver extends 	BaseActivity {
	private TextView notify;
	private Ringtone mRingtone;
	private int LOOPING = 1;
	private boolean mIsPlay;
	private AudioManager audioManager;
	private int volume;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		notify = (TextView) findViewById(R.id.title_text);
		
		notify.setText(getString(R.string.receiver_notify));
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		audioManager.setMode(AudioManager.MODE_IN_CALL);
		volume =	audioManager.getStreamVolume( AudioManager.STREAM_VOICE_CALL );
		
		notify.setText(getString(R.string.receiver_notify)+"\n"+getString(R.string.volume,volume));
		setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
		String uri = "android.resource://" + getPackageName() + "/"
				+ R.raw.recevicer_test;
		mRingtone = RingtoneManager.getRingtone(this, Uri.parse(uri));
		mRingtone.setStreamType(AudioManager.STREAM_VOICE_CALL);
		mIsPlay = true;
	}

	private Handler mPlayHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.i("messi","handleMessage");
			if ((msg.what == LOOPING) && mIsPlay&&audioManager!=null) {
				if (!mRingtone.isPlaying()) {
					Log.i("messi","audioManager isPlaying");
					mRingtone.play();
				}
				mPlayHandler.sendEmptyMessageDelayed(LOOPING, 1000);
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mIsPlay = true;
		mPlayHandler.sendEmptyMessageDelayed(LOOPING, 1000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsPlay = false;
		if (mRingtone.isPlaying()) {
			mRingtone.stop();
		}
		audioManager.setMode(AudioManager.MODE_NORMAL);
	}



	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_receiver);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		if (mRingtone.isPlaying()) {
			mRingtone.stop();
			mRingtone.play();
		}
	}
}
