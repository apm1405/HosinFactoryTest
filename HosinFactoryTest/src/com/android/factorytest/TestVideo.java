package com.android.factorytest;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class TestVideo extends Activity {

	private VideoView mVideoView;
	private Button mPlayPauseButton;
	private Button mOKButton;
	private Button mFalseButton;
	private int LOOPING = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.test_video);

		mVideoView = (VideoView) findViewById(R.id.videoview);
		mPlayPauseButton = (Button) findViewById(R.id.play_pause_button);
		mOKButton = (Button) findViewById(R.id.video_ok);
		mFalseButton = (Button) findViewById(R.id.vidio_false);

		String uri = "android.resource://" + getPackageName() + "/"
				+ R.raw.video_test;
		mVideoView.setVideoURI(Uri.parse(uri));

		mVideoView.setMediaController(new MediaController(TestVideo.this));
		mVideoView.requestFocus();

		mOKButton.setOnClickListener(mListener);
		mFalseButton.setOnClickListener(mListener);
		mPlayPauseButton.setOnClickListener(mPlayPauseListener);
	}

	private Handler mPlayHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if ((msg.what == LOOPING) && mVideoView.isPlaying()) {
				mPlayPauseButton.setText(R.string.text_pause);
				mPlayHandler.sendEmptyMessageDelayed(LOOPING, 1000);
			} else {
				mPlayPauseButton.setText(R.string.text_play);
				mPlayHandler.sendEmptyMessageDelayed(LOOPING, 1000);
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mPlayHandler.sendEmptyMessageDelayed(LOOPING, 1000);

	}

	@Override
	protected void onPause() {
		super.onPause();
		// mIsPlay = false;
		if (mVideoView != null && mVideoView.isPlaying()) {
			mVideoView.pause();
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
			finish();
			Intent intent = new Intent();
			intent.setClass(TestVideo.this, TestRadioAndEarphone.class);
			startActivity(intent);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private OnClickListener mPlayPauseListener = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			// add by suxy disable button-tackpicture first
			if (mVideoView != null && mVideoView.isPlaying()) {
				mVideoView.pause();
				mPlayPauseButton.setText(R.string.text_play);
			} else {
				mVideoView.start();
				mPlayPauseButton.setText(R.string.text_pause);
			}

		}
	};

	private OnClickListener mListener = new OnClickListener() {

		private SharedPreferences sp;
		private SharedPreferences.Editor editor;
		private static final String TEST = "facotytest";

		public void onClick(View v) {
			// TODO Auto-generated method stub
			// add by suxy disable button-tackpicture first

			sp = getSharedPreferences(TEST, MODE_WORLD_READABLE);
			editor = getSharedPreferences(TEST, MODE_WORLD_WRITEABLE).edit();
			if (v == mOKButton) {
				editor.putInt("video", 1);
			} else {
				editor.putInt("video", 0);
			}

			editor.commit();
			if (sp.getBoolean("all", false)) {
				Intent intent = new Intent();
				intent.setClass(TestVideo.this, Tp_test.class);
				startActivity(intent);
			}
			finish();
		}

	};

}
