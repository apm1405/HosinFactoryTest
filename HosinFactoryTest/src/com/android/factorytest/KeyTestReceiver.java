package com.android.factorytest;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

public class KeyTestReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String TAG = "KeyTestReceiver";
    
    public void onReceive(Context context, Intent intent) {
        try {
            if(intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
                Log.v(TAG, "catch onReceive ACTION_BOOT_COMPLETED");
                SystemProperties.set("persist.sys.powerkey", "0");
                SystemProperties.set("persist.sys.enable_lid_switch", "1");
                return;
            }
        } catch(Exception e) {
            Log.v(TAG, "Exception");
            e.printStackTrace();
        }
    }
}
