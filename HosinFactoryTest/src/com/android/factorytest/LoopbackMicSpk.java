package com.android.factorytest;

import java.util.LinkedList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class LoopbackMicSpk extends BaseActivity {

	
	private AudioRecord mAudioRecod;
	private AudioTrack mAudioTrack;
	private Thread mRecordThread;
	private Thread mPlayThread;
	private boolean mFlag = true;
	private byte[] mInBytes;
	private byte[] mOutBytes;
	private int mBuffer;
	private LinkedList<byte[]> mLinkedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetestBtnVisible(false);
		
		init();

		mRecordThread = new Thread(new RecordSound());
		mPlayThread = new Thread(new PlayRecord());
		mRecordThread.start();
		mPlayThread.start();
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
				/*
				 * try { mRecordThread.sleep(1000); } catch(InterruptedException
				 * e){ }
				 */
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

				/*
				 * try { mPlayThread.sleep(1000); } catch(InterruptedException
				 * e){ }
				 */

			}
		}
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



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mFlag = false;
		mAudioRecod.stop();
		mAudioRecod.release();
		mAudioRecod = null;
		mAudioTrack.stop();
		mAudioTrack.release();
		mAudioTrack = null;
	}

	
	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_loopback_micspk);
	}

	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		
	}
}
