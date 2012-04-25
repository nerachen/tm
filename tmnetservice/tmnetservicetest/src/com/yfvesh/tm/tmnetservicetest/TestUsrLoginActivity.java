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

public class TestUsrLoginActivity extends Activity implements OnClickListener {

	private TMNetServiceHelper mTMNetSrvHelper;
	private TextView mTxtLogView;
	private String mLogStr;
	private TMUsrLoginResultReceiver mTMUsrLoginResultRcvr = new TMUsrLoginResultReceiver();

	private int mTransId = TMNetDef.TM_REQ_ID_USRLOGIN_BASE;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testusrlogin);
		Button btn1 = (Button) this.findViewById(R.id.button1);
		Button btn2 = (Button) this.findViewById(R.id.button2);
		Button btn3 = (Button) this.findViewById(R.id.button3);
		Button btn4 = (Button) this.findViewById(R.id.button4);
		Button btn5 = (Button) this.findViewById(R.id.button5);
		Button btncancel = (Button) this.findViewById(R.id.buttoncancel);
		
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btncancel.setOnClickListener(this);
		
		mTxtLogView = (TextView) this.findViewById(R.id.textlog);
		mTMNetSrvHelper = new TMNetServiceHelper(this.getParent());
		mTMNetSrvHelper.setSimNumToTMNetSrv("simnum_userlog");
	}

	protected void onResume() {
		super.onResume();
		registerTMUsrLoginRcvr();
	}

	protected void onPause() {
		if (mTMNetSrvHelper != null) {
			mTMNetSrvHelper.unbindTMNetService();
		}
		unRegisterTMUsrLoginRcvr();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.button1) {
			doStartBind();
		} else if (v.getId() == R.id.button2) {
			doLogin();
		} else if (v.getId() == R.id.button3) {
			doLogOff();
		} else if (v.getId() == R.id.button4) {
			doCheckStatus();
		} else if (v.getId() == R.id.button5) {
			releaseUnbindService();
		} else if(v.getId() == R.id.buttoncancel) {
			cancelUserLogActionReq();
		}
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

	private void doStartBind() {
		updateLogView("call mTMNetSrvHelper.bindTMNetService()");
		mTMNetSrvHelper.bindTMNetService();
	}

	private void releaseUnbindService() {
		updateLogView("call mTMNetSrvHelper.unbindTMNetService()");
		mTMNetSrvHelper.unbindTMNetService();
	}

	private void doLogin() {
		String username = "username";
		String password = "password";
		int id = getTransId();
		updateLogView("call tmUserLogin with id=" + id + " username="
				+ username + " password=" + password);
		int result = mTMNetSrvHelper.tmUserLogin(id, username, password);
		updateLogView("tmUserLogin returns :" + result);

	}
	
	private void cancelUserLogActionReq() {
		int result = mTMNetSrvHelper.tmCancelUserLogActionReq(mTransId);
		updateLogView("try to cancel request id=" + mTransId +" result with:"+result);
	}
	private void doLogOff() {
		String username = "username";
		int id = getTransId();
		updateLogView("call tmUserLogout with id=" + id + " username="
				+ username);
		int result = mTMNetSrvHelper.tmUserLogout(id, "username");
		updateLogView("tmUserLogout returns :" + result);

	}

	private void doCheckStatus() {
		boolean blogged = false;
		if (mTMNetSrvHelper.checkTMNetSrvWorkable()) {
			blogged = mTMNetSrvHelper.tmCheckUserLoginStatus();
			updateLogView("tmCheckUserLoginStatus returns :" + blogged);
		} else {
			updateLogView("TMNetSrv not workable, give up tmCheckUserLoginStatus ");
		}
	}

	private void registerTMUsrLoginRcvr() {
		IntentFilter intentFilter = new IntentFilter(
				TMNetDef.TM_REQ_RESULT_ACTION_USERLOGIN);
		registerReceiver(mTMUsrLoginResultRcvr, intentFilter);
	}

	private void unRegisterTMUsrLoginRcvr() {
		unregisterReceiver(mTMUsrLoginResultRcvr);
	}

	private int getTransId() {
		return ++mTransId;
	}

	private class TMUsrLoginResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String actionstr = intent.getAction();
			if (TMNetDef.TM_REQ_RESULT_ACTION_USERLOGIN.equals(actionstr)) {
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
								if (TMNetDef.TM_REQ_TYPE_USERLOGIN_IN == detailtype) {
									updateLogView("recv TM_REQ_TYPE_USERLOGIN_IN result, status="
											+ status);
								} else if (TMNetDef.TM_REQ_TYPE_USERLOGIN_OFF == detailtype) {
									updateLogView("recv TM_REQ_TYPE_USERLOGIN_OFF result, status="
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