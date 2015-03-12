package com.android.factorytest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//import android.os.SystemProperties;

public class SecretForeignCodeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent i) {
		// TODO Auto-generated method stub
		// if(SystemProperties.getInt("ro.opengles.version",0)==131072){

		Intent intent = new Intent(context, MMITestList.class);
		intent.putExtra("language", "en");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		// }

	}

}
