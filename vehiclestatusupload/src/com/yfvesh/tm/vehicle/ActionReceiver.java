package com.yfvesh.tm.vehicle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActionReceiver extends BroadcastReceiver {

	public static final String ACTION_URL = "com.yfvesh.tm.action.vehicle";

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		String actionstr = arg1.getAction();
		if (actionstr != null && actionstr.equals(ACTION_URL)) {
			Intent intent = new Intent(arg0, VehiclestatusuploadActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			arg0.startActivity(intent);
		}

	}

}


