package com.yfvesh.tm.ttsservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class TtsLaunchReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent inte) {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent();
		myIntent.setAction("com.yfvesh.tm.ttsservice.TTS_SERVICE");
		context.startService(myIntent);
	}
}

