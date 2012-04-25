package com.yfvesh.tm.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class YfveLaunchReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent inte) {
		// TODO Auto-generated method stub
		startService(context,"com.yfvesh.tm.tmnetservice.TMNET_SERVICE");
		startService(context,"com.yfvesh.tm.action.gpsupload");
		startService(context,"com.yfvesh.tm.ttsservice.TTS_SERVICE");
	}
	
	public static void startService(Context context,String actionstr) {
		Intent intent = new Intent();
		intent.setAction(actionstr);
		context.startService(intent);
	}
}

