package com.android.factorytest;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageParser.NewPermissionInfo;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mediatek.FMRadio.MyAIDL;

public class TestRadioAndEarphone extends BaseActivity  {

	private Intent intent;
	private ComponentName component;
	private ServiceConnection conn;
	private MyAIDL binder;
	private ImageButton previous;
	private ImageButton next;
	private Button reset;
	private SeekBar seekBar;
	private TextView frequencyTextView;
	private TextView notify;
	private float maxFrequency = 108.0f;
	private float minFrequency = 87.0f;
	private float defaultFrequency = 107.5f;
	private float currentFrequency  ;
	private int maxVolume;
	private int currentVolume;
	private AudioManager mAudioManager;
	private boolean isBind =false;
	private boolean isFirstBind = true;
	private boolean isHeadsetOn = false;
	private class MyTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			return null;
			// TODO Auto-generated method stub\
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DisplayMetrics m;
		registerReceiver(mReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
		 mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		 isHeadsetOn =  mAudioManager.isWiredHeadsetOn();
		 Log.i("messi","isHeadsetOn: "+isHeadsetOn);
		 maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_FM);
		 currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_FM  );
		 mAudioManager.setStreamVolume( AudioManager.STREAM_FM   , maxVolume, 2);//set volume to max
		
		previous = (ImageButton) findViewById(R.id.fmPreButton);
		next = (ImageButton) findViewById(R.id.fmNextButton);
		reset = (Button) findViewById(R.id.fmReset);
		seekBar = (SeekBar) findViewById(R.id.fmProgressBar);
		frequencyTextView = (TextView) findViewById(R.id.fmFrequence);
		notify = (TextView) findViewById(R.id.fmrdsinfo);
		
		currentFrequency = defaultFrequency;
		frequencyTextView.setText("FM"+currentFrequency+"MHz");
		seekBar.setProgress((int)(maxFrequency -minFrequency)*10);
		previous.setOnClickListener(this);
		next.setOnClickListener(this);
		reset.setOnClickListener(this);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currentFrequency = (float)progress/10 +minFrequency;
				if(minFrequency == currentFrequency){
					previous.setClickable(false);
				}
				else if(maxFrequency == currentFrequency){
					next.setClickable(false);
				}
				else{
					previous.setClickable(true);
					next.setClickable(true);
				}
				frequencyTextView.setText("FM"+currentFrequency+"MHz");
				try {
					if(binder!=null){
						binder.setFrequency(currentFrequency);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		List<ResolveInfo> list = null;
		String[] array = new String[10];	
		 list.toArray(array);
		for(int i=0;i<list.size();i++){
			
		}
		 intent = new Intent();
		 component = new ComponentName("com.mediatek.FMRadio",
		 "com.mediatek.FMRadio.MyFMRadioService");
		intent.setComponent(component);
		conn = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				Log.i("messi","onServiceDisconnected");
				isBind = false;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				
				binder  = MyAIDL.Stub.asInterface(service);
				Log.i("messi","onServiceDisconnectedxxxx"+binder+" service: ");
				isBind = true;
			}
		};
	
		
		
	} 
	private BroadcastReceiver mReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals("android.intent.action.HEADSET_PLUG")){
				if (intent.hasExtra("state")) {
					if (0 == intent.getIntExtra("state", 2)) {
						// 耳机拔出
						notify.setText(TestRadioAndEarphone.this.getString(R.string.fm_test_notify));
					}
					if (1 == intent.getIntExtra("state", 2)) {
						//耳机插入
						notify.setText("");
						if(!isHeadsetOn){
								handler.sendEmptyMessage(1);
								isHeadsetOn  = true;
						}
					}
				}
			}
		}
	};
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			
			if(1 == msg.what){
				if(isBind)
					unbindService(conn);
					isBind = true;
					bindService(intent, conn, BIND_AUTO_CREATE);
			}
			
		}
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(isHeadsetOn){
		bindService(intent, conn, BIND_AUTO_CREATE);
		isFirstBind = false;
		isBind =true;
		}
		Intent intent;
		super.onResume();
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(isBind){
			unbindService(conn);
		}
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mReceiver);
		mAudioManager.setStreamVolume(AudioManager.STREAM_FM  , currentVolume, 2);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		int id = v.getId();
		 if(R.id.fmPreButton == id){
			currentFrequency -= 0.1;
			seekBar.setProgress((int)((currentFrequency - minFrequency)*10));
		 }
		 else if(R.id.fmNextButton == id){
			currentFrequency +=0.1;
			seekBar.setProgress((int)((currentFrequency - minFrequency)*10));
		 }
		 else if(R.id.fmReset == id){
			 currentFrequency = defaultFrequency;
			 seekBar.setProgress((int)((currentFrequency - minFrequency)*10));
		 }
	}
	@Override
	void setLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.test_radio_earphone);
	}
	@Override
	void retestOnClick() {
		// TODO Auto-generated method stub
		if(isBind){
			unbindService(conn);
			isBind = false;
		}
		bindService(intent, conn, BIND_AUTO_CREATE);
		isBind = true;
		 currentFrequency = defaultFrequency;
		 seekBar.setProgress((int)((currentFrequency - minFrequency)*10));
	}
}

