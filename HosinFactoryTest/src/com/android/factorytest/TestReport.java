package com.android.factorytest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TestReport extends Activity implements OnClickListener{
	private TextView fmTestResult;
	private TextView btTestResult;
	private TextView wifiTestResult;
	private TextView gpsTestResult;
	private TextView testFailed;
	private TextView testSucceed;
	private TextView untest;
	private Button exit;
	private StringBuilder untestDevice = null;
	private StringBuilder failedDevice = null;
	private StringBuilder succeedDevice = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.test_report);
		initView();
		setTestResult();
	}
	private void initView(){
		fmTestResult = (TextView) findViewById(R.id.fm_test_result);
		btTestResult = (TextView) findViewById(R.id.bt_test_result);
		wifiTestResult = (TextView) findViewById(R.id.wifi_test_result);
		gpsTestResult = (TextView) findViewById(R.id.gps_test_result);
		testFailed = (TextView) findViewById(R.id.test_result_failed);
		testSucceed = (TextView) findViewById(R.id.test_result_success);
		untest = (TextView) findViewById(R.id.test_result_untest);
		exit = (Button) findViewById(R.id.test_result_button);
		exit.setOnClickListener(this);
	}
	private void setTestResult(){
		SharedPreferences	sp = getSharedPreferences("facotytest", 1);
		String[] keys =  MMITestData.keys;
		int[] ids = MMITestData.itemNameIds;
		int id=0;
		int result = -1;
		untestDevice = new StringBuilder();
		failedDevice = new StringBuilder();
		succeedDevice = new StringBuilder();
		for(int i=0;i<keys.length;i++){
			result = sp.getInt(keys[i], -1);
			id = ids[i];
			setTextResultByKey(keys[i],result,id);
		}
		testSucceed.setText(succeedDevice.toString());
		testFailed.setText(failedDevice.toString());
		untest.setText(untestDevice.toString());
	}
	
	
	private void setTextResultByKey(String key,int result ,int id){
		
		if(key.equals("radio")){
			if(-1 == result){
				fmTestResult.setTextColor(Color.WHITE);
				fmTestResult.setText("NotTested");
			}
			else if(1 == result){
				fmTestResult.setTextColor(Color.BLUE);
				fmTestResult.setText("Pass");
			}
			else{
				fmTestResult.setTextColor(Color.RED);
				fmTestResult.setText("Fail");
			}
		}
		else if(key.equalsIgnoreCase("bluetooth")){
			if(-1 == result){
				btTestResult.setTextColor(Color.WHITE);
				btTestResult.setText("NotTested");
			}
			else if(1 == result){
				btTestResult.setTextColor(Color.BLUE);
				btTestResult.setText("Pass");
			}
			else{
				btTestResult.setTextColor(Color.RED);
				btTestResult.setText("Fail");
			}
			
		}
		else if(key.equals("wifi")){
			if(-1 == result){
				wifiTestResult.setTextColor(Color.WHITE);
				wifiTestResult.setText("NotTested");
			}
			else if(1 == result){
				wifiTestResult.setTextColor(Color.BLUE);
				wifiTestResult.setText("Pass");
			}
			else{
				wifiTestResult.setTextColor(Color.RED);
				wifiTestResult.setText("Fail");
			}
		}
		else if(key.equals("gps")){
			if(-1 == result){
				gpsTestResult.setTextColor(Color.WHITE);
				gpsTestResult.setText("NotTested");
			}
			else if(1 == result){
				gpsTestResult.setTextColor(Color.BLUE);
				gpsTestResult.setText("Pass");
			}
			else{
				gpsTestResult.setTextColor(Color.RED);
				gpsTestResult.setText("Fail");
			}
		}
		if(-1 == result && id!=R.string.report_title){
			untestDevice.append(getString(id)+"\n");
		}
		else if(1 == result && id!=R.string.report_title){
			succeedDevice.append(getString(id)+"\n");
		}
		else{
			if(id!=R.string.report_title)
			failedDevice.append(getString(id)+"\n");
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
}
