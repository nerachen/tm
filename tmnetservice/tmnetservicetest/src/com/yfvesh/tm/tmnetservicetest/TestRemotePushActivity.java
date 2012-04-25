package com.yfvesh.tm.tmnetservicetest;

import java.util.Calendar;
import java.util.List;

import com.yfvesh.tm.tmnetservicetest.R;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;
import com.yfvesh.tm.tmnetservice.data.TMPoi;
import com.yfvesh.tm.tmnetservice.data.TMWeather;
import com.yfvesh.tm.tmnetservice.TMNetServiceHelper;

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

public class TestRemotePushActivity extends Activity implements OnClickListener {

	private TMRemotePushReceiver mTMRemotePushRcvr = new TMRemotePushReceiver();
	private TMNetServiceHelper mTMNetSrvHelper;
	private TextView mTxtLogView;
	private String mLogStr;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testremotepush);

		Button btn1 = (Button) this.findViewById(R.id.button1);
		Button btn5 = (Button) this.findViewById(R.id.button5);

		btn1.setOnClickListener(this);
		btn5.setOnClickListener(this);

		mTxtLogView = (TextView) this.findViewById(R.id.textlog);
		mTMNetSrvHelper = new TMNetServiceHelper(this.getParent());
		mTMNetSrvHelper.setSimNumToTMNetSrv("simnum_remotepush");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			doStartBind();
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

	private void registerTMRcvr() {
		IntentFilter intentFilter = new IntentFilter(
				TMNetDef.TM_PUSH_RESULT_ACTION_FILE);
		intentFilter.addAction(TMNetDef.TM_PUSH_RESULT_ACTION_MSG);
		intentFilter.addAction(TMNetDef.TM_PUSH_RESULT_ACTION_DEST);
		intentFilter.addAction(TMNetDef.TM_PUSH_RESULT_ACTION_WEATHER);
		registerReceiver(mTMRemotePushRcvr, intentFilter);
	}

	private void unRegisterTMRcvr() {
		unregisterReceiver(mTMRemotePushRcvr);
	}

	private class TMRemotePushReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String actionstr = intent.getAction();
			Bundle bundle = intent.getExtras();
			if (TMNetDef.TM_PUSH_RESULT_ACTION_FILE.equals(actionstr)) {
				if (bundle != null) {
					if (bundle.containsKey(TMNetDef.TAG_TM_PUSH_FILE_TYPE)
							&& bundle
									.containsKey(TMNetDef.TAG_TM_PUSH_FILE_PATH)
							&& bundle.containsKey(TMNetDef.TAG_TM_PUSH_RESULT)) {
						int filetype = bundle
								.getInt(TMNetDef.TAG_TM_PUSH_FILE_TYPE);
						String filepath = bundle
								.getString(TMNetDef.TAG_TM_PUSH_FILE_PATH);
						int result = bundle.getInt(TMNetDef.TAG_TM_PUSH_RESULT);
						updateLogView("Recv remote push: TM_PUSH_RESULT_ACTION_FILE filetype="
								+ filetype
								+ " filepath="
								+ filepath
								+ " result=" + result);
					}
				}
			} else if (TMNetDef.TM_PUSH_RESULT_ACTION_MSG.equals(actionstr)) {
				if (bundle != null) {
					if (bundle.containsKey(TMNetDef.TAG_TM_PUSH_MSG_CONTENT)) {
						String msg = bundle
								.getString(TMNetDef.TAG_TM_PUSH_MSG_CONTENT);
						updateLogView("Recv remote push: TM_PUSH_RESULT_ACTION_MSG msg="
								+ msg);
					}
				}
			} else if (TMNetDef.TM_PUSH_RESULT_ACTION_DEST.equals(actionstr)) {
				if (bundle != null) {
					if (bundle.containsKey(TMNetDef.TAG_TM_PUSH_POI_NUMS)
							&& bundle.containsKey(TMNetDef.TAG_TM_PUSH_RESULT)) {
						int poinum = bundle
								.getInt(TMNetDef.TAG_TM_PUSH_POI_NUMS);
						int result = bundle.getInt(TMNetDef.TAG_TM_PUSH_RESULT);
						updateLogView("Recv remote push: TM_PUSH_RESULT_ACTION_DEST poinum="
								+ poinum + " result=" + result);
						if (bundle.containsKey(TMNetDef.TAG_TM_PUSH_POI_LIST)) {
							List<TMPoi> poilist = bundle
									.getParcelableArrayList(TMNetDef.TAG_TM_PUSH_POI_LIST);
							if (poilist != null) {
								updateLogView("Recv remote push: poilist size="
										+ poilist.size());
							}
						}
					}
				}
			} else if (TMNetDef.TM_PUSH_RESULT_ACTION_WEATHER.equals(actionstr)) {
				if (bundle != null) {
					if (bundle
							.containsKey(TMNetDef.TAG_TM_PUSH_WEATHER_CONTENT)) {
						TMWeather tmweather = bundle
								.getParcelable(TMNetDef.TAG_TM_PUSH_WEATHER_CONTENT);
						if (tmweather != null) {
							updateLogView("Recv remote push: TM_PUSH_RESULT_ACTION_WEATHER TMWeather:"
									+ tmweather.toString());
						}
					}
				}
			}
		}
	}
}
