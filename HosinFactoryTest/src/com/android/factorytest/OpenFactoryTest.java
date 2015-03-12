package com.android.factorytest;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

public class OpenFactoryTest extends Activity {
	private Intent foreignIntent;
	private String mLanguageToLoad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		foreignIntent = getIntent();
		mLanguageToLoad = foreignIntent.getStringExtra("language");
		Configuration config = getResources().getConfiguration();
		DisplayMetrics metrics = getResources().getDisplayMetrics();

		if (mLanguageToLoad.equals("en")) {
			Log.i("Factory", "ZJT English");
			config.locale = Locale.ENGLISH;
		} else {
			config.locale = Locale.SIMPLIFIED_CHINESE;
		}
		getResources().updateConfiguration(config, metrics);

		openForeignFactoryTest();
	}

	private void openForeignFactoryTest() {
		finish();
		foreignIntent.setClass(OpenFactoryTest.this, FactorytestActivity.class);
		startActivity(foreignIntent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
}
