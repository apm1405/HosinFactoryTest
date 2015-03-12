package com.android.factorytest;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class MMITestList extends ListActivity implements OnClickListener, OnItemClickListener{
	public static  String mLanguageToLoad = "en";
	private Button exitButton;
	private List<String> ids = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		Configuration config = getResources().getConfiguration();
		DisplayMetrics metrics = getResources().getDisplayMetrics();

		if (mLanguageToLoad.equals("en")) {
			Log.i("Factory", "ZJT English");
			config.locale = Locale.ENGLISH;
		} else {
			config.locale = Locale.SIMPLIFIED_CHINESE;
		}
		getResources().updateConfiguration(config, metrics);
		initData();
		setContentView(R.layout.mmi_test_main);
		mAdapter  = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ids);
		setListAdapter(mAdapter);
		mList.setOnItemClickListener(this);
		exitButton = (Button) findViewById(R.id.mmi_list_button);
		exitButton.setOnClickListener(this);
	}
	private void initData(){
		ids.add(getString(R.string.mmi_auto_test));
		ids.add(getString(R.string.mmi_hardware_test));
		ids.add(getString(R.string.report_title));
		ids.add(getString(R.string.mmi_restore));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent() ;
		switch (position) {
		case 0:
			 SharedPreferences.Editor editor;
			editor = getSharedPreferences("facotytest", MODE_WORLD_WRITEABLE).edit();
			editor.clear();
			editor.putBoolean("all", true);
			editor.commit();
			intent.setClass(MMITestList.this, VersionInfo.class);
			intent.putExtra("position",0);
			MMITestList.this.startActivity(intent);
			break;
		case 1:
			intent.putExtra("language", getIntent().getStringExtra("language"));
			intent.setClass(MMITestList.this, OpenFactoryTest.class);
			MMITestList.this.startActivity(intent);
			break;
		case 2:
			intent.putExtra("language", getIntent().getStringExtra("language"));
			intent.setClass(MMITestList.this, TestReport.class);
			MMITestList.this.startActivity(intent);
			break;
		case 3:
			 intent = new Intent();
			intent.setAction("android.settings.PRIVACY_SETTINGS");
			MMITestList.this.startActivity(intent);
			break;
		default:
			break;
		}
	}
	
}
