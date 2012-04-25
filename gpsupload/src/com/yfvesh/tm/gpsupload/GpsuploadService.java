package com.yfvesh.tm.gpsupload;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.yfvesh.tm.tmnetservice.ITMSrvStatusLstnr;
import com.yfvesh.tm.tmnetservice.TMNetServiceHelper;
import com.yfvesh.tm.tmnetservice.data.TMLocation;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;
import com.yfvesh.tm.utility.*;

public class GpsuploadService extends Service implements ITMSrvStatusLstnr {

	private LocationManager m_locationManager = null;
	private Timer m_timer = null;
	private TimerTask m_timerTask = null;

	private final int m_uploadTimeInterval = 1000 * 10;
	private final int m_uploadDistance = 5;
	private final int m_checkTimeInterval = 1000 * 30 * 1;
	private final String m_GPSprovider = LocationManager.GPS_PROVIDER;

	public static final String GPS_LATITUDE = "gps_latitude";
	public static final String GPS_LONGTITUDE = "gps_longtitude";
	public static final String GPS_TIME = "gps_time";
	public static final String GPS_SPEED = "gps_speed";
	public static final String GPS_DIRECTION = "gps_direction";
	public static final String GPS_ALTITUDE = "gps_altitude";

	// Message define
	private final int MSG_POPDIALOG = 0x00000001;
	private final int MSG_GPS_UPLOAD_RESULT = 0x00000002;

	public static TMNetServiceHelper TMNetSrvHelper;
	private static int mTransId = TMNetDef.TM_REQ_ID_VEHICLE_STATUS_BASE;
	private TMGPSUploadResultReceiver mTMStatusRcvr = new TMGPSUploadResultReceiver();

	@Override
	public void onCreate() {
		super.onCreate();
		initTMNetServiceHelper();
		TMNetSrvHelper.bindTMNetService();
		registerTMStatusRcvr();
		// create localmanager
		String serviceName = Context.LOCATION_SERVICE;
		m_locationManager = (LocationManager) this
				.getSystemService(serviceName);
		// set listener，interval = 10s，distance change >= 5m
		m_locationManager.requestLocationUpdates(m_GPSprovider,
				m_uploadTimeInterval, m_uploadDistance, m_locationListener);
	}

	private final Handler m_handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_POPDIALOG:
				if (false == checkGPSStatus()) {
					Log.d("gpsupload", "Gps is still Disable!!");
					popAlertDlg(PopupDialogAcitivity.WARNING_GPS);
				}
				break;
			case MSG_GPS_UPLOAD_RESULT: {
				int status = msg.arg1;
				int transid = msg.arg2;
				if(status == TMNetDef.TM_SUCCESS) {
					Toast.makeText(getApplicationContext(),
							R.string.text_uploadsuccess, Toast.LENGTH_SHORT).show();
				}
				break;
			}
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private LocationListener m_locationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.d("gpsupload", "location change coming!!");
			Notify(location);
			if (false == sendGPSData(location)) {
				// save gps data to buffer,waiting for the next time
			}
			// saveGPSData(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			popAlertDlg(PopupDialogAcitivity.WARNING_GPS);
			// set timer，check GPS status every 2 minute.remind user opening
			// gps service
			setTimer();
			Log.d("gpsupload", "Gps Disable!!");
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			killTimer();
			Log.d("gpsupload", "Gps Enable!!");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	};

	private void killTimer() {
		Log.d("gpsupload", "kill timer!!");
		m_timer.cancel();
	}

	private void setTimer() {
		Log.d("gpsupload", "set timer!!");

		m_timerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// GPS is disable,pop up dlg
				Log.d("gpsupload", "time up!!");
				Message message = new Message();
				message.what = MSG_POPDIALOG;
				m_handler.sendMessage(message);
			}
		};

		m_timer = new Timer();
		m_timer.schedule(m_timerTask, m_checkTimeInterval, m_checkTimeInterval);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		TMNetSrvHelper.unbindTMNetService();
		unRegisterTMStatusRcvr();
		super.onDestroy();
	}

	private void Notify(Location location) {
		String Content;
		if (location != null) {
			Log.d("gpsupload", "Get location success!!");
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			Content = "GPS 经度：" + lon + "纬度：" + lat;
		} else {
			Content = "无GPS信息";
		}
		Toast.makeText(getApplicationContext(), Content, Toast.LENGTH_SHORT)
				.show();
	}

	private boolean checkGPSStatus() {
		boolean bGpsEnabled = Settings.Secure.isLocationProviderEnabled(
				this.getContentResolver(), m_GPSprovider);
		if (false == bGpsEnabled) {
			Log.d("gpsupload", "gps is disabled");
			// Settings.Secure.setLocationProviderEnabled
			// (this.getContentResolver(), m_GPSprovider,true);
		} else {
			Log.d("gpsupload", "gps is enabled");
		}
		return bGpsEnabled;
	}

	private void saveDataToProvider(String key, String text) {
		if (key != null && text != null) {
			TMDataProviderHelper.updateTMDataByKey(key, text,
					this.getContentResolver());
		}
	}

	private void saveGPSData(Location location) {
		float fvalue = 0.0f;
		long lvalue = 0;
		String text;

		// save latitude
		fvalue = (float) (location.getLatitude());
		text = String.valueOf(fvalue);
		saveDataToProvider(GPS_LATITUDE, text);

		// save longtitude
		fvalue = (float) (location.getLongitude());
		text = String.valueOf(fvalue);
		saveDataToProvider(GPS_LONGTITUDE, text);

		// save altitude
		fvalue = (float) (location.getAltitude());
		text = String.valueOf(fvalue);
		saveDataToProvider(GPS_ALTITUDE, text);

		// save speed
		fvalue = location.getSpeed();
		text = String.valueOf(fvalue);
		saveDataToProvider(GPS_SPEED, text);

		// save direction
		fvalue = location.getBearing();
		text = String.valueOf(fvalue);
		saveDataToProvider(GPS_DIRECTION, text);

		// save time
		lvalue = location.getTime();
		text = String.valueOf(lvalue);
		saveDataToProvider(GPS_TIME, text);
	}

	private boolean sendGPSData(Location location) {
		// check whether network is available,if not,pop dialog
		if (false == checkNetworkService()) {
			popAlertDlg(PopupDialogAcitivity.WARNING_NETWORK);
			return false;
		}
		// check whether user is login,if not,call login activity
		else {
			// send status data to server
			uploadGPSData(location);
		}
		return true;
	}

	private void popAlertDlg(int warningType) {
		Intent i = new Intent(this,
				com.yfvesh.tm.gpsupload.PopupDialogAcitivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		i.putExtra("com.yfvesh.tm.gpsupload", warningType);
		startActivity(i);
	}

	private boolean checkNetworkService() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {

			return false;
		}
		return true;
	}

	private void uploadGPSData(Location location) {
		checkTMNetSrvHelperInited();
		if (TMNetSrvHelper.checkTMNetSrvWorkable()) {
			TMLocation tmloc = new TMLocation();

			tmloc.setTime(location.getTime());
			tmloc.setLongitude(location.getLongitude());
			tmloc.setLatitude(location.getLatitude());
			tmloc.setSpeed(location.getSpeed());
			tmloc.setDirection(location.getBearing());
			tmloc.setAltitude((float) location.getAltitude());

			// hard code ones
			tmloc.setSatNum(3);
			tmloc.setAlarmStatus(0);

			TMNetSrvHelper.tmGpsUpload(getTransId(), tmloc);
		}
	}

	private int getTransId() {
		return ++mTransId;
	}

	private void initTMNetServiceHelper() {
		TMNetSrvHelper = new TMNetServiceHelper(this, this);
		TMNetSrvHelper.setSimNumToTMNetSrv(TMDevices.getSimNum(this));
	}

	private void checkTMNetSrvHelperInited() {
		if (TMNetSrvHelper == null) {
			initTMNetServiceHelper();
			TMNetSrvHelper.bindTMNetService();
		} else if (!TMNetSrvHelper.checkTMNetSrvWorkable()) {
			TMNetSrvHelper.bindTMNetService();
		}
	}

	@Override
	public void onTMSrvConnected() {
		Log.d(getClass().getSimpleName(), "onTMSrvConnected");
		// if(mMsgHandler!=null) {
		// mMsgHandler.sendEmptyMessage(MSG_TMNET_SRV_CONNECTED);
		// }
	}

	@Override
	public void onTMSrvDisconnected() {
		// if(mMsgHandler!=null) {
		// mMsgHandler.sendEmptyMessage(MSG_TMNET_SRV_DISCONNECTED);
		// }
	}

	private void registerTMStatusRcvr() {
		IntentFilter intentFilter = new IntentFilter(
				TMNetDef.TM_REQ_RESULT_ACTION_GPS);
		registerReceiver(mTMStatusRcvr, intentFilter);
	}

	private void unRegisterTMStatusRcvr() {
		unregisterReceiver(mTMStatusRcvr);
	}

	private class TMGPSUploadResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String actionstr = intent.getAction();
			Bundle bundle = intent.getExtras();
			if (TMNetDef.TM_REQ_RESULT_ACTION_GPS.equals(actionstr)) {
				if (bundle != null) {
					if (bundle.containsKey(TMNetDef.TAG_TM_REQ_ID)) {
						int id = bundle.getInt(TMNetDef.TAG_TM_REQ_ID);
						if (id >= TMNetDef.TM_REQ_ID_VEHICLE_STATUS_BASE) {
							if (bundle.containsKey(TMNetDef.TAG_TM_REQ_STATUS)
									&& bundle
											.containsKey(TMNetDef.TAG_TM_REQ_DETAIL_TYPE)) {
								int status = bundle
										.getInt(TMNetDef.TAG_TM_REQ_STATUS);
								int detailtype = bundle
										.getInt(TMNetDef.TAG_TM_REQ_DETAIL_TYPE);
								if (TMNetDef.TM_REQ_TYPE_GPS_UPLOAD == detailtype) {
									Message msg = new Message();
									msg.what = MSG_GPS_UPLOAD_RESULT;
									msg.arg1 = status;
									msg.arg2 = id;
									m_handler.sendMessage(msg);
								}
							}
						}
					}
				}
			}
		}
	}
}