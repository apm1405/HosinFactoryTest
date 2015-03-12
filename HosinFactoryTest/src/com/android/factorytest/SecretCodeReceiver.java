package com.android.factorytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//import android.os.SystemProperties;

public class SecretCodeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent i) {
		// TODO Auto-generated method stub
		// if(SystemProperties.getInt("ro.opengles.version",0)==131072){
		MMITestList.mLanguageToLoad = "zh";
		Intent intent = new Intent(context, Power.class);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		// }

	}

}
