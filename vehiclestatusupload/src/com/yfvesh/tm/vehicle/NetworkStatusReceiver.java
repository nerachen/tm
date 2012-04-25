package com.yfvesh.tm.vehicle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;

public class NetworkStatusReceiver extends BroadcastReceiver {
	
	public final static String ACTION_URL = "com.yfvesh.tm.vehicle.VehiclestatusuploadActivity.uploadvehiclestatus";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean success = false;   
		//get network service   
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);	
		State state = (connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
		// get network status
		if (android.net.NetworkInfo.State.CONNECTED == state) { 
		// whether wifi is in use?
		success = true;   
		}   
		  
		state = connManager.getNetworkInfo(   
		// get network status
		ConnectivityManager.TYPE_MOBILE).getState();  
		// whether 3g is in use?
		if (State.CONNECTED == state) {    
		success = true;   
		} 
		if (!success) {   
			Toast.makeText(context, R.string.text_networkisunavailable, Toast.LENGTH_LONG).show();   
		} 
		else
		{
//			Intent intentForActivity = new Intent(context,VehiclestatusuploadActivity.class);
//			intentForActivity.setAction(ACTION_URL);
//			context.startActivity(intentForActivity);
			Toast.makeText(context, R.string.text_networkisavailable, Toast.LENGTH_LONG).show();   
		}
	}  
}