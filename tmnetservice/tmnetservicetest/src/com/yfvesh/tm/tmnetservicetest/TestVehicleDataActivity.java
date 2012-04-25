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
import com.yfvesh.tm.tmnetservice.data.TMVehicleData;

public class TestVehicleDataActivity extends Activity implements OnClickListener {
	private TMMotorDataResultReceiver mTMMotorDataResultRcvr = new TMMotorDataResultReceiver();
	private TMNetServiceHelper mTMNetSrvHelper;
	private TextView mTxtLogView;
	private String mLogStr;
	private int mTransId = TMNetDef.TM_REQ_ID_VEHICLE_DATA_BASE;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.testvehichledata);
	    
	    Button btn1 = (Button) this.findViewById(R.id.button1);
		Button btn2 = (Button) this.findViewById(R.id.button2);
		Button btn5 = (Button) this.findViewById(R.id.button5);

		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn5.setOnClickListener(this);

		mTxtLogView = (TextView) this.findViewById(R.id.textlog);
		mTMNetSrvHelper = new TMNetServiceHelper(this.getParent());
		mTMNetSrvHelper.setSimNumToTMNetSrv("simnum_vehicle_data");
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			doStartBind();
		} else if (v.getId() == R.id.button2) {
			uploadVehicleData();
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
	
	private void uploadVehicleData() {
		int id = getTransId();
		TMVehicleData tmvdata = getTMVehicleData();
		updateLogView("call tmSendVehicleData with id=" + id + "TMVehicleData:"
				+ tmvdata.toString());
		int result = mTMNetSrvHelper.tmSendVehicleData(id, tmvdata);
		updateLogView("tmSendVehicleData results with:"+result);
	}
	
	private TMVehicleData getTMVehicleData() {
		TMVehicleData tmvdata = new TMVehicleData();
		tmvdata.setSpeed(100);
		return tmvdata;
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
		registerReceiver(mTMMotorDataResultRcvr, intentFilter);
	}

	private void unRegisterTMRcvr() {
		unregisterReceiver(mTMMotorDataResultRcvr);
	}
	
	private class TMMotorDataResultReceiver extends BroadcastReceiver {
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
								if (TMNetDef.TM_REQ_TYPE_VEHICLE_DATA == detailtype) {
									updateLogView("recv TM_REQ_TYPE_VEHICLE_DATA result, status="
											+ status);
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
