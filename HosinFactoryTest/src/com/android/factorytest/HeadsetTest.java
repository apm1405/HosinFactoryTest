package com.android.factorytest;

import java.util.LinkedList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class HeadsetTest extends BaseActivity  {
	private TextView notify;
	private TextView headset_name;
	private ImageView headset_img;
	private AudioRecord mAudioRecod;
	private AudioTrack mAudioTrack;
	private Thread mRecordThread;
	private Thread mPlayThread;
	private boolean mFlag = false;
	private byte[] mInBytes;
	private byte[] mOutBytes;
	private int mBuffer;
	private LinkedList<byte[]> mLinkedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		init();
		mRecordThread = new Thread(new RecordSound());
		mPlayThread = new Thread(new PlayRecord());
		mRecordThread.start();
		mPlayThread.start();
		IntentFilter filter = new IntentFilter(
				"android.intent.action.HEADSET_PLUG");
		registerReceiver(mReceiver, filter);
	}

	private void init() {
		mBuffer = AudioRecord
				.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_DEFAULT,
						AudioFormat.ENCODING_PCM_16BIT);

		mAudioRecod = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
				AudioFormat.CHANNEL_OUT_DEFAULT,
				AudioFormat.ENCODING_PCM_16BIT, mBuffer);

		mAudioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 44100,
				AudioFormat.CHANNEL_OUT_DEFAULT,
				AudioFormat.ENCODING_PCM_16BIT, mBuffer, AudioTrack.MODE_STREAM);

		mInBytes = new byte[mBuffer];
		mOutBytes = new byte[mBuffer];
		mLinkedList = new LinkedList<byte[]>();
	}

	private void initView() {
		notify = (TextView) findViewById(R.id.title_text);
		notify.setText(getString(R.string.headset_test_notify));

		headset_name = (TextView) findViewById(R.id.headset_name);
		headset_img = (ImageView) findViewById(R.id.headset_img);


		mSuccess.setEnabled(false);
	}

	private class RecordSound implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			byte[] tmp;
			mAudioRecod.startRecording();

			while (mFlag) {
				mAudioRecod.read(mInBytes, 0, mBuffer);
				tmp = mInBytes.clone();

				if (mLinkedList.size() >= 2) {
					mLinkedList.removeFirst();
				}
				mLinkedList.add(tmp);
			}
		}
	}

	private class PlayRecord implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			byte[] tmp;
			mAudioTrack.play();

			while (mFlag) {
				try {
					mOutBytes = mLinkedList.getFirst();
					tmp = mOutBytes.clone();
					mAudioTrack.write(tmp, 0, tmp.length);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 5:
			notify.setText(getString(R.string.headset_keycode)
					+ "KEYCODE_CALL");
			mSuccess.setEnabled(true);
			return true;
		case 6: 
			notify.setText(getString(R.string.headset_keycode)
					+ "KEYCODE_ENDCALL");
			mSuccess.setEnabled(true);
			return true;
		case 85: 
			notify.setText(getString(R.string.headset_keycode)
					+ "KEYCODE_MEDIA_PLAY_PAUSE");
			mSuccess.setEnabled(true);
			if (mFlag) {
				mFlag = false;
			} else {
				mFlag = true;

				mRecordThread = new Thread(new RecordSound());
				mPlayThread = new Thread(new PlayRecord());
				mRecordThread.start();
				mPlayThread.start();
			}
			return true;
		
		case 79: 
			notify.setText(getString(R.string.headset_keycode)
					+ "KEYCODE_HEADSETHOOK");
			mSuccess.setEnabled(true);
			if (mFlag) {
				mFlag = false;
			} else {
				mFlag = true;

				mRecordThread = new Thread(new RecordSound());
				mPlayThread = new Thread(new PlayRecord());
				mRecordThread.start();
				mPlayThread.start();
				
			}
			return true;
		
		case KeyEvent.KEYCODE_VOLUME_UP:
			if(position>0){
				finish();
				Intent intent = new Intent();
				intent.setClass(this,MMITestData.testItems[position-1]);
				startActivity(intent);
			}
			return true;
		default:
			notify.setText("You have pressed key: "
					+ keyCode);
			return true;
		}
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
				headset_name.setText("headset name = "
						+ intent.getStringExtra("name"));
				if (intent.hasExtra("state")) {
					if (0 == intent.getIntExtra("state", 2)) {
						// 耳机拔出
						headset_name.setText("");
						headset_img.setImageResource(R.drawable.phoneflat);
					}
					if (1 == intent.getIntExtra("state", 2)) {
						// 耳机插入
						if (intent.hasExtra("microphone")) {
							if (0 == intent.getIntExtra("microphone", 2)) {
								// 耳机没有麦克风
								headset_img
										.setImageResource(R.drawable.headset_without_mic);
							}
							if (1 == intent.getIntExtra("microphone", 2)) {
								// 耳机有麦克风
								headset_img
										.setImageResource(R.drawable.headset_with_mic);
							}
						}
					}
				}
			}
		}
	};

	protected void onDestroy() {
		mFlag = false;
		mAudioRecod.stop();
		mAudioRecod.release();
		mAudioRecod = null;
		mAudioTrack.stop();
		mAudioTrack.release();
		mAudioTrack = null;
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}


	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.headset);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		notify.setText(getString(R.string.headset_test_notify));
		mSuccess.setEnabled(false);
		mFlag = false;
	}
}
