package com.yfvesh.tm.vehicle;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.yfvesh.tm.tmnetservice.ITMSrvStatusLstnr;
import com.yfvesh.tm.tmnetservice.TMNetServiceHelper;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;
import com.yfvesh.tm.utility.TMDataProviderHelper;
import com.yfvesh.tm.utility.TMDatas;
import com.yfvesh.tm.utility.TMDevices;

public class VehiclestatusuploadActivity extends Activity implements
		OnClickListener, OnFocusChangeListener, ITMSrvStatusLstnr {
	/** Called when the activity is first created. */

	private static final int MSG_ON_TMNET_RESULTS = 101;
	private Button mselectedBtn = null;
	private Button mworkingBtn = null;
	private Button mspareBtn = null;
	private Button mmaintainBtn = null;
	private int mvehicleStatus = VEHICLE_UNKOWN;
	private NetworkStatusReceiver mNetworkStateReceiver;

	public static final int VEHICLE_UNKOWN = 0;
	public static final int VEHICLE_WORKING = 1;
	public static final int VEHICLE_SPARE = 2;
	public static final int VEHICLE_MAINTAIN = 3;

	public static final String VEHSTATUS_STATUS = "vhestatus_status";

	public static TMNetServiceHelper TMNetSrvHelper;
	private static int mTransId = TMNetDef.TM_REQ_ID_VEHICLE_STATUS_BASE;
	private MsgHandler mMsgHandler = new MsgHandler();
	private TMVehicleStatusResultReceiver mTMStatusRcvr = new TMVehicleStatusResultReceiver();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initTMNetServiceHelper();
		TMNetSrvHelper.bindTMNetService();
		
		Button btnback = (Button)this.findViewById(R.id.btnback);
		btnback.setOnClickListener(this);
		
		mworkingBtn = (Button) this.findViewById(R.id.btnworking);
		mworkingBtn.setOnClickListener(this);

		mspareBtn = (Button) this.findViewById(R.id.btnspare);
		mspareBtn.setOnClickListener(this);

		mmaintainBtn = (Button) this.findViewById(R.id.btnmaintain);
		mmaintainBtn.setOnClickListener(this);

		initData();
		highlightBtnStatus();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// String action= intent.getAction();
		// if(NetworkStatusReceiver.ACTION_URL == action)
		// {
		// if(true == mhasMission){
		// sendVehicleStatus();
		// mhasMission = false;
		// }
		// }
	}

	@Override
	public void onResume() {
		super.onResume();
		registerTMStatusRcvr();
		mNetworkStateReceiver = new NetworkStatusReceiver();
		// register filter
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetworkStateReceiver, filter);
	}

	@Override
	public void onPause() {
		unRegisterTMStatusRcvr();
		// unregister filter
		unregisterReceiver(mNetworkStateReceiver);
		super.onPause();
		
		
	}

	@Override
	protected void onDestroy() {
		if (TMNetSrvHelper != null) {
			TMNetSrvHelper.unbindTMNetService();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnback: {
			this.finish();
			break;
		}
		case R.id.btnworking:
			onWorkingBtnClicked();
			sendVehicleStatus();
			break;
		case R.id.btnspare:
			onSpareBtnClicked();
			sendVehicleStatus();
			break;
		case R.id.btnmaintain:
			onMaintainBtnClicked();
			sendVehicleStatus();
			break;
		default:
			return;
		}
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub

	}

	private void resetBtnStatus() {
		if (null == mselectedBtn) {
			Log.d("vehiclestatusupload", "m_selectedBtn is null!");
			return;
		}
		if (mworkingBtn == mselectedBtn) {
			mselectedBtn
					.setBackgroundResource(R.drawable.working_button_status);
		} else if (mspareBtn == mselectedBtn) {
			mselectedBtn.setBackgroundResource(R.drawable.spare_button_status);
		} else if (mmaintainBtn == mselectedBtn) {
			mselectedBtn
					.setBackgroundResource(R.drawable.maintain_button_status);
		} else {
			Log.d("vehiclestatusupload", "invalid m_selectedBtn status!!");
		}
	}

	private void highlightBtnStatus() {
		if (null == mselectedBtn) {
			Log.d("vehiclestatusupload", "m_selectedBtn is null!");
			return;
		}
		if (mworkingBtn == mselectedBtn) {
			mselectedBtn.setBackgroundResource(R.drawable.working_2);
		} else if (mspareBtn == mselectedBtn) {
			mselectedBtn.setBackgroundResource(R.drawable.spare_2);
		} else if (mmaintainBtn == mselectedBtn) {
			mselectedBtn.setBackgroundResource(R.drawable.maintain_2);
		} else {
			Log.d("vehiclestatusupload", "invalid m_selectedBtn status!!");
		}
	}

	private void onSpareBtnClicked() {
		mvehicleStatus = VEHICLE_SPARE;
		resetBtnStatus();
		mselectedBtn = mspareBtn;
		highlightBtnStatus();
		saveVehicleStatusData();
	}

	private void onMaintainBtnClicked() {
		mvehicleStatus = VEHICLE_MAINTAIN;
		resetBtnStatus();
		mselectedBtn = mmaintainBtn;
		highlightBtnStatus();
		saveVehicleStatusData();
	}

	private void onWorkingBtnClicked() {
		mvehicleStatus = VEHICLE_WORKING;
		resetBtnStatus();
		mselectedBtn = mworkingBtn;
		highlightBtnStatus();
		saveVehicleStatusData();
	}

	private void initData() {
		getVehicleStatusData();
		if (VEHICLE_WORKING == mvehicleStatus) {
			mselectedBtn = mworkingBtn;
		} else if (VEHICLE_SPARE == mvehicleStatus) {
			mselectedBtn = mspareBtn;
		} else if (VEHICLE_MAINTAIN == mvehicleStatus) {
			mselectedBtn = mmaintainBtn;
		} else {
			Log.d("vehiclestatusupload", "invalid m_vehicleStatus !!");
		}
	}

	private void saveVehicleStatusData() {
		// save latitude
		int value = mvehicleStatus;
		String text = String.valueOf(value);
		saveDataToProvider(VEHSTATUS_STATUS, text);
	}

	private void saveDataToProvider(String key, String text) {
		if (key != null && text != null) {
			TMDataProviderHelper.updateTMDataByKey(key, text,
					this.getContentResolver());
		}
	}

	private void getVehicleStatusData() {
		// save latitude
		String text = null;
		TMDatas data = readDataFromProvider(VEHSTATUS_STATUS);

		if (null != data) {
			text = data.getText();
			mvehicleStatus = Integer.parseInt(text);
		}
	}

	private TMDatas readDataFromProvider(String key) {
		if (key != null) {
			List<TMDatas> tmDataList = TMDataProviderHelper.getTMDataByKey(key,
					this.getContentResolver());
			if (tmDataList.size() > 0) {
				return tmDataList.get(0);
			}
		}
		return null;
	}

	private boolean checkNetworkService() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {

			return false;
		}
		return true;
	}

	private boolean checkLogin() {
		checkTMNetSrvHelperInited();
		if(TMNetSrvHelper.checkTMNetSrvWorkable()) {
			return TMNetSrvHelper.tmCheckUserLoginStatus();
		}
		return false;
	}

	private boolean sendVehicleStatus() {
		// check whether network is available,if not,pop dialog
		if (false == checkNetworkService()) {
			popAlertDlg(PopupDialogAcitivity.WARNING_NETWORK);
			return false;
		}
		// check whether user is login,if not,call login activity
		else if (false == checkLogin()) {
			popAlertDlg(PopupDialogAcitivity.WARNING_LOGIN);
			return false;
		} else {
			// send status data to server
			checkTMNetSrvHelperInited();
			if (TMNetSrvHelper.checkTMNetSrvWorkable()) {
				// does not check the status here, no handling for sending fail
				TMNetSrvHelper
						.tmSendVehicleStatus(getTransId(), mvehicleStatus);
			}
		}
		return true;
	}

	private void popAlertDlg(int warningType) {
		Intent i = new Intent(this,
				com.yfvesh.tm.vehicle.PopupDialogAcitivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		i.putExtra("com.yfvesh.tm.vehicle", warningType);
		startActivity(i);
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
				TMNetDef.TM_REQ_RESULT_ACTION_OTHERS);
		registerReceiver(mTMStatusRcvr, intentFilter);
	}

	private void unRegisterTMStatusRcvr() {
		unregisterReceiver(mTMStatusRcvr);
	}
	
	private class TMVehicleStatusResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String actionstr = intent.getAction();
			Bundle bundle = intent.getExtras();
			if (TMNetDef.TM_REQ_RESULT_ACTION_OTHERS.equals(actionstr)) {
				if (bundle != null) {
					if (bundle.containsKey(TMNetDef.TAG_TM_REQ_ID)) {
						int id = bundle.getInt(TMNetDef.TAG_TM_REQ_ID);
						if (mTransId == id) {
							if (bundle.containsKey(TMNetDef.TAG_TM_REQ_STATUS)
									&& bundle
											.containsKey(TMNetDef.TAG_TM_REQ_DETAIL_TYPE)) {
								int status = bundle
										.getInt(TMNetDef.TAG_TM_REQ_STATUS);
								int detailtype = bundle
										.getInt(TMNetDef.TAG_TM_REQ_DETAIL_TYPE);
								if (TMNetDef.TM_REQ_TYPE_VEHICLE_STATUS == detailtype) {
									Message msg = new Message();
									msg.what = MSG_ON_TMNET_RESULTS;
									msg.arg1 = status;
									mMsgHandler.sendMessage(msg);
								}
							}
						}
					}
				}
			}
		}
	}

	private class MsgHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (MSG_ON_TMNET_RESULTS == msg.what) {
				Log.d(this.getClass().getSimpleName(),
						"MsgHandler MSG_ON_TMNET_RESULTS");
				int status = msg.arg1;
				if (TMNetDef.TM_SUCCESS == status) {
					Toast.makeText(VehiclestatusuploadActivity.this,
							R.string.text_uploadsuccess, Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				super.handleMessage(msg);
			}
		}
	}
}