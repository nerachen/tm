package com.yfvesh.tm.gpsupload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class GpsuploadServiceBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent inte) {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(); 
		myIntent.setAction("com.yfvesh.tm.action.gpsupload");
		context.startService(myIntent);  
	}
}

