package com.android.factorytest;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class Test_speaker extends BaseActivity {

	private MediaPlayer mp;
	private Ringtone rt;
	private int LOOPING = 1;
	private boolean isPlay;
	private int volume;
	private AudioManager manager;
	private TextView notify;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		notify = (TextView) findViewById(R.id.title_text);
		

		String uri = "android.resource://" + getPackageName() + "/"
				+ R.raw.recevicer_test;
		manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		rt = RingtoneManager.getRingtone(this, Uri.parse(uri));
		
		// Uri defaultUri = null;
		// defaultUri = Settings.System.DEFAULT_RINGTONE_URI;
		// rt = RingtoneManager.getRingtone(this, defaultUri);
		rt.setStreamType(AudioManager.STREAM_SYSTEM);
		volume = manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
		notify.setText(getString(R.string.speaker_notify)+"\n"+getString(R.string.volume,volume));
		
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isPlay = true;
		playHandler.sendEmptyMessageDelayed(LOOPING, 1000);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isPlay = false;
		if (rt.isPlaying()) {
			rt.stop();
		}
		super.onPause();
	}



	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_speaker);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		if (rt.isPlaying()) {
			rt.stop();
		}
		rt.play();
	}
}
