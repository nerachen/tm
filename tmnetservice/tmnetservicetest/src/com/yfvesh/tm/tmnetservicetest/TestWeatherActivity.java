package com.yfvesh.tm.tmnetservicetest;

import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yfvesh.tm.tmnetservice.TMNetServiceHelper;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;
import com.yfvesh.tm.tmnetservice.data.TMWeather;

public class TestWeatherActivity extends Activity implements OnClickListener {

	private TMWeatherResultReceiver mTMWeatherResultRcvr = new TMWeatherResultReceiver();
	private TMNetServiceHelper mTMNetSrvHelper;
	private TextView mTxtLogView;
	private String mLogStr;
	private int mTransId = TMNetDef.TM_REQ_ID_WEATHER_BASE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testweather);

		Button btn1 = (Button) this.findViewById(R.id.button1);
		Button btn2 = (Button) this.findViewById(R.id.button2);
		Button btn3 = (Button) this.findViewById(R.id.button3);
		Button btn5 = (Button) this.findViewById(R.id.button5);

		btn1.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn5.setOnClickListener(this);

		mTxtLogView = (TextView) this.findViewById(R.id.textlog);
		mTMNetSrvHelper = new TMNetServiceHelper(this.getParent());
		mTMNetSrvHelper.setSimNumToTMNetSrv("simnum_weather");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			doStartBind();
		} else if (v.getId() == R.id.button2) {
			getWeatherByCityCode();
		} else if (v.getId() == R.id.button3) {
			getWeatherByLoc();
		} else if (v.getId() == R.id.button5) {
			releaseUnbindService();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerTMRcvr();
	}

	@Override
	protected void onPause() {
		if (mTMNetSrvHelper != null) {
			mTMNetSrvHelper.unbindTMNetService();
		}
		unRegisterTMRcvr();
		super.onPause();
	}

	private void getWeatherByCityCode() {
		int id = getTransId();
		TMWeather tmweather = getTMWeatherWithCityCode();
		updateLogView("call tmGetWeatherByCity with id=" + id + "TMWeather:"
				+ tmweather.toString());
		int result = mTMNetSrvHelper.tmGetWeatherByCity(id,
				tmweather.getCityCode());
		updateLogView("tmGetWeatherByCity results with:" + result);

	}

	private void getWeatherByLoc() {
		int id = getTransId();
		TMWeather tmweather = getTMWeatherWithLoc();
		updateLogView("call tmGetWeatherByLoc with id=" + id + "TMWeather:"
				+ tmweather.toString());
		int result = mTMNetSrvHelper.tmGetWeatherByLoc(id,
				tmweather.getLatitude(), tmweather.getLongitude());
		updateLogView("tmGetWeatherByLoc results with:" + result);
	}

	private TMWeather getTMWeatherWithCityCode() {
		TMWeather tmweather = new TMWeather();
		tmweather.setCityCode("shanghai");
		return tmweather;
	}

	private TMWeather getTMWeatherWithLoc() {
		TMWeather tmweather = new TMWeather();
		tmweather.setLatitude(121.12312321);
		tmweather.setLongitude(38.12312312);
		return tmweather;
	}

	private void doStartBind() {
		updateLogView("call mTMNetSrvHelper.bindTMNetService()");
		mTMNetSrvHelper.bindTMNetService();
	}

	private void releaseUnbindService() {
		updateLogView("call mTMNetSrvHelper.unbindTMNetService()");
		mTMNetSrvHelper.unbindTMNetService();
	}

	private void updateLogView(String logstr) {
		if (mTxtLogView != null) {
			String curstr = mTxtLogView.getText().toString();
			mLogStr = Calendar.getInstance().getTime().toGMTString() + ": "
					+ logstr + "\n" + curstr;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mTxtLogView.setText(mLogStr);
			}
		});
	}

	private int getTransId() {
		return ++mTransId;
	}

	private void registerTMRcvr() {
		IntentFilter intentFilter = new IntentFilter(
				TMNetDef.TM_REQ_RESULT_ACTION_OTHERS);
		registerReceiver(mTMWeatherResultRcvr, intentFilter);
	}

	private void unRegisterTMRcvr() {
		unregisterReceiver(mTMWeatherResultRcvr);
	}

	private class TMWeatherResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String actionstr = intent.getAction();
			if (TMNetDef.TM_REQ_RESULT_ACTION_OTHERS.equals(actionstr)) {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					if (bundle.containsKey(TMNetDef.TAG_TM_REQ_ID)) {
						int id = bundle.getInt(TMNetDef.TAG_TM_REQ_ID);
						updateLogView("recv tm result id =" + id
								+ " while current id =" + mTransId);
						if (mTransId == id) {
							if (bundle.containsKey(TMNetDef.TAG_TM_REQ_STATUS)
									&& bundle
											.containsKey(TMNetDef.TAG_TM_REQ_DETAIL_TYPE)) {
								int status = bundle
										.getInt(TMNetDef.TAG_TM_REQ_STATUS);
								int detailtype = bundle
										.getInt(TMNetDef.TAG_TM_REQ_DETAIL_TYPE);
								if (TMNetDef.TM_REQ_TYPE_WEATHER_LOC == detailtype || 
										TMNetDef.TM_REQ_TYPE_WEATHER_CITY == detailtype ||
										TMNetDef.TM_REQ_TYPE_WEATHER == detailtype) {
									updateLogView("recv TM_REQ_TYPE_WEATHER result, status="
											+ status);
									if(bundle.containsKey(TMNetDef.TAG_TM_REQ_WEATHER_CONTENT)) {
										TMWeather tmweather  = bundle.getParcelable(TMNetDef.TAG_TM_REQ_WEATHER_CONTENT);
										updateLogView("recv TAG_TM_REQ_WEATHER_CONTENT, weather details:"
												+ tmweather);
									}
								} else {
									updateLogView("recv unknow tm result");
								}
							}
						}
					}
				}
			}
		}
	}
}
