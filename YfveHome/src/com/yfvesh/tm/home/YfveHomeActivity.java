package com.yfvesh.tm.home;

import com.android.internal.telephony.ITelephony;
import com.yfvesh.tm.home.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.lang.reflect.Method;

public class YfveHomeActivity extends Activity {

	public static final int INDEX_USRLOGIN = 0;
	public static final int INDEX_ENTP_MSG = 1;
	public static final int INDEX_ECALL = 2;
	public static final int INDEX_ORDER_MSG = 3;
	public static final int INDEX_ONLINE_POI = 4;
	public static final int INDEX_TEAM_COMMU = 5;
	public static final int INDEX_VEHICLE_STATUS = 6;
	public static final int INDEX_WEATHER = 7;
	public static final int INDEX_VEHICLE_CONDITION = 8;
	public static final int INDEX_NAVI = 9;
	public static final int INDEX_APPS_COUNT = 10;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		startService(this, "com.yfvesh.tm.tmnetservice.TMNET_SERVICE");
		startService(this, "com.yfvesh.tm.action.gpsupload");
		startService(this, "com.yfvesh.tm.ttsservice.TTS_SERVICE");
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setBackgroundResource(R.drawable.home_bg);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				String strFunction = YfveHomeActivity.this.getResources()
						.getString(ImageAdapter.getIndexedString(position));

				if (position == INDEX_USRLOGIN) {
					// log in
					Intent intent = new Intent();
					intent.setAction("com.yfvesh.tm.action.usrlogin");
					sendOrderedBroadcast(intent, null);
				} else if (position == INDEX_ENTP_MSG) {
					// log in
					Intent intent = new Intent();
					intent.setAction("com.yfvesh.tm.action.enterprisemsg");
					sendOrderedBroadcast(intent, null);
				} else if (position == INDEX_ECALL) {
					Method method;
					try {
						method = Class.forName("android.os.ServiceManager")
								.getMethod("getService", String.class);
						IBinder binder = (IBinder) method.invoke(null,
								new Object[] { TELEPHONY_SERVICE });
						ITelephony telephony = ITelephony.Stub
								.asInterface(binder);
						if (!telephony.isIdle()) {
							telephony.endCall();
							Thread.sleep(500);
						}

					} catch (Exception e) {
					}
					Intent intent = new Intent();
					// intent.setAction(Intent.ACTION_DIAL);
					intent.setAction(Intent.ACTION_CALL);
					// String data = "tel:10086";
					String data = parent.getContext().getString(
							R.string.ecall_number);
					Uri uri = Uri.parse(data);
					intent.setData(uri);
					startActivity(intent);
				} else if (position == INDEX_ORDER_MSG) {
					// order message
					Intent intent = new Intent();
					intent.setAction("com.yfvesh.tm.action.infoondemand");
					sendOrderedBroadcast(intent, null);
				} else if (position == INDEX_ONLINE_POI) {

				} else if (position == INDEX_TEAM_COMMU) {

				} else if (position == INDEX_VEHICLE_STATUS) {
					// vehicle status
					Intent intent = new Intent();
					intent.setAction("com.yfvesh.tm.action.vehicle");
					sendOrderedBroadcast(intent, null);
				} else if (position == INDEX_WEATHER) {
					// vehicle status
					Intent intent = new Intent();
					intent.setAction("com.yfvesh.tm.action.weatherrep");
					sendOrderedBroadcast(intent, null);
				} else if (position == INDEX_VEHICLE_CONDITION) {
					Intent intent = new Intent();
					intent.setAction("com.yfvesh.tm.action.vclcondition");
					sendOrderedBroadcast(intent, null);
				} else if (position == INDEX_NAVI) {

				} else {
					Toast.makeText(
							YfveHomeActivity.this,
							strFunction
									+ " is not integrated, please wait for new relases",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String strFunction = getResources().getString(
				R.string.mainmenu_choice0 + requestCode);

		if (resultCode == 0) {
		}
	}

	public static void startService(Context context, String actionstr) {
		Intent intent = new Intent();
		intent.setAction(actionstr);
		context.startService(intent);
	}

}