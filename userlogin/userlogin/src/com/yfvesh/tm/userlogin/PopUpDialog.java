package com.yfvesh.tm.userlogin;

import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yfvesh.tm.tmnetservice.TMNetServiceHelper;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;

public class PopUpDialog extends Activity implements OnClickListener {

	/* flags for login status */
	private static final int STATUS_REQ_INIT = -1;
	private static final int STATUS_REQ_INPROGRESS = 0;
	private static final int STATUS_REQ_DONE = 1;
	private static final int STATUS_REQ_ERR_NETWORK = 2;
	private static final int STATUS_REQ_ERR_USRINFO = 3;
	private static final int STATUS_REQ_CANCELED = 4;

	/* the time gap between satus changes ,2s */
	private static final int TIME_2_SECONDS = 2000;
	/* button to cancel the login progress */
	private Button mBtnCancel;

	/* the anim progress indicator image view */
	private ImageView mImgView;
	/* the text info for status */
	private TextView mTxtView;
	/* the cancel button area */
	private View mLinerLayoutCancel;

	/* flag to indicate whether login in progress */
	private int mReqStatus = STATUS_REQ_INIT;

	/* user name and password */
	private String mUsrName;
	private String mUsrPW;

	private int mDlgReason = -1;
	/* handler to help handle status */
	private MsgHandler mMsgHandler;

	/* message indicate the network result */
	public static final int MSG_POPUP_REQ_RESULT = 101;
	public static final int MSG_FINSIH_CANCELED = 102;
	public static final int MSG_FINSIH_ERROR = 103;
	public static final int MSG_FINSIH_DONE = 104;

	/* thread for test */
	private Thread mThread;

	private static int mTransId = TMNetDef.TM_REQ_ID_USRLOGIN_BASE;
	public static TMNetServiceHelper mTMNetSrvHelper;
	private TMUsrLoginResultReceiver mTMUsrLoginResultRcvr = new TMUsrLoginResultReceiver();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);

		mMsgHandler = new MsgHandler();

		mTMNetSrvHelper = UserloginActivity.TMNetSrvHelper;

		mBtnCancel = (Button) findViewById(R.id.btncancel);
		mBtnCancel.setOnClickListener(this);
		mImgView = (ImageView) findViewById(R.id.textloginimg);
		mTxtView = (TextView) findViewById(R.id.textlogininfo);
		mLinerLayoutCancel = findViewById(R.id.linerlayoutcancel);
		getBundleInfo();
		initUiBaseOnReq();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btncancel: {
			cancelLoginRequest();
			updateUI();
			handleReqResult();
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerTMUsrLoginRcvr();
		if (mDlgReason == UserloginActivity.REQUEST_CODE_POPUP_LOGIN) {
			if (mReqStatus == STATUS_REQ_INIT) {
				// firstly created,initial the request
				sendLoginReq();
			}
		} else if (mDlgReason == UserloginActivity.REQUEST_CODE_POPUP_LOGOFF) {
			sendLogOffReq();
		}
		updateUI();
	}

	@Override
	protected void onPause() {
		unRegisterTMUsrLoginRcvr();
		stopProgressAnim();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void sendLoginReq() {
		mReqStatus = STATUS_REQ_INPROGRESS;
		int result = -1;
		if (mTMNetSrvHelper != null) {
			result = mTMNetSrvHelper
					.tmUserLogin(getTransId(), mUsrName, mUsrPW);
			Log.d(this.getClass().getSimpleName(), "remote result = " + result);
		}

		if (result != TMNetDef.TM_SUCCESS) {
			Message msg = new Message();
			msg.what = MSG_POPUP_REQ_RESULT;
			msg.arg1 = STATUS_REQ_ERR_NETWORK;
			mMsgHandler.sendMessage(msg);
		}
		// mThread = new Thread(new TestReqRunner());
		// mThread.start();
	}

	private void sendLogOffReq() {
		mReqStatus = STATUS_REQ_INPROGRESS;
		int result = -1;
		if(mTMNetSrvHelper!=null) {
			result = mTMNetSrvHelper.tmUserLogout(getTransId(), mUsrName);
		}
		if (result != TMNetDef.TM_SUCCESS) {
			Message msg = new Message();
			msg.what = MSG_POPUP_REQ_RESULT;
			msg.arg1 = STATUS_REQ_ERR_NETWORK;
			mMsgHandler.sendMessage(msg);
		}
		// mThread = new Thread(new TestReqRunner());
		// mThread.start();
	}

	private void cancelLoginRequest() {
		mReqStatus = STATUS_REQ_CANCELED;
		if(mTMNetSrvHelper!=null) {
			mTMNetSrvHelper.tmCancelUserLogActionReq(mTransId);
		}
	}

	private void startProgressAnim() {
		if (mReqStatus != STATUS_REQ_INPROGRESS) {
			return;
		}
		if (mImgView != null) {
			mImgView.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.rotate));
		}
	}

	private void stopProgressAnim() {
		if (mImgView != null) {
			mImgView.clearAnimation();
		}
	}

	private void getBundleInfo() {
		final Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey(UserloginActivity.USER_NAME)) {
			mUsrName = bundle.getString(UserloginActivity.USER_NAME);
		}
		if (bundle.containsKey(UserloginActivity.USER_PW)) {
			mUsrPW = bundle.getString(UserloginActivity.USER_PW);
		}

		if (bundle.containsKey(UserloginActivity.DIALOG_POPUP_REASON)) {
			mDlgReason = bundle.getInt(UserloginActivity.DIALOG_POPUP_REASON);
		}
		Log.d("PopUpDialog", "mUsrName=" + mUsrName);
		Log.d("PopUpDialog", "mUsrPW=" + mUsrPW);
	}

	private void initUiBaseOnReq() {
		if (mDlgReason == UserloginActivity.REQUEST_CODE_POPUP_LOGIN) {
			mBtnCancel.setVisibility(View.VISIBLE);
			mImgView.setVisibility(View.VISIBLE);
		} else if (mDlgReason == UserloginActivity.REQUEST_CODE_POPUP_LOGOFF) {
			mBtnCancel.setVisibility(View.GONE);
			mImgView.setVisibility(View.GONE);
		}
	}

	private void updateUI() {
		if (mDlgReason == UserloginActivity.REQUEST_CODE_POPUP_LOGIN) {
			if (mReqStatus == STATUS_REQ_INPROGRESS) {
				mLinerLayoutCancel.setVisibility(View.VISIBLE);
				mImgView.setImageResource(R.drawable.icon_waiting);
				mTxtView.setText(R.string.txt_login_wait);
				startProgressAnim();
			} else if (mReqStatus == STATUS_REQ_DONE) {
				stopProgressAnim();
				mLinerLayoutCancel.setVisibility(View.GONE);
				mImgView.setImageResource(R.drawable.icon_waiting);
				mTxtView.setText(R.string.txt_login_done);
			} else if (mReqStatus == STATUS_REQ_ERR_NETWORK) {
				stopProgressAnim();
				mLinerLayoutCancel.setVisibility(View.GONE);
				mImgView.setImageResource(R.drawable.icon_remind);
				mTxtView.setText(R.string.txt_login_err_network);
			} else if (mReqStatus == STATUS_REQ_ERR_USRINFO) {
				stopProgressAnim();
				mLinerLayoutCancel.setVisibility(View.GONE);
				mImgView.setImageResource(R.drawable.icon_remind);
				mTxtView.setText(R.string.txt_login_err_usrinfo);
			} else if (mReqStatus == STATUS_REQ_CANCELED) {
				stopProgressAnim();
			}
		} else if (mDlgReason == UserloginActivity.REQUEST_CODE_POPUP_LOGOFF) {
			mTxtView.setText(R.string.txt_logoff_wait);
		}

	}

	private void handleReqResult() {
		if (mReqStatus == STATUS_REQ_DONE) {
			mMsgHandler
					.sendEmptyMessageDelayed(MSG_FINSIH_DONE, TIME_2_SECONDS);
		} else if (mReqStatus == STATUS_REQ_ERR_NETWORK
				|| mReqStatus == STATUS_REQ_ERR_USRINFO) {
			mMsgHandler.sendEmptyMessageDelayed(MSG_FINSIH_ERROR,
					TIME_2_SECONDS);
		} else if (mReqStatus == STATUS_REQ_CANCELED) {
			mMsgHandler.sendEmptyMessage(MSG_FINSIH_CANCELED);
		}
	}

	private class MsgHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_POPUP_REQ_RESULT: {
				mReqStatus = msg.arg1;
				updateUI();
				handleReqResult();
				break;
			}
			case MSG_FINSIH_CANCELED: {
				setResult(RESULT_CANCELED);
				finish();
				break;
			}
			case MSG_FINSIH_ERROR: {
				setResult(RESULT_CANCELED);
				finish();
				break;
			}
			case MSG_FINSIH_DONE: {
				setResult(RESULT_OK);
				finish();
				break;
			}
			default:
				break;
			}
		}
	}

	private int getTransId() {
		return ++mTransId;
	}

	private void registerTMUsrLoginRcvr() {
		IntentFilter intentFilter = new IntentFilter(
				TMNetDef.TM_REQ_RESULT_ACTION_USERLOGIN);
		registerReceiver(mTMUsrLoginResultRcvr, intentFilter);
	}

	private void unRegisterTMUsrLoginRcvr() {
		unregisterReceiver(mTMUsrLoginResultRcvr);
	}

	private class TMUsrLoginResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String actionstr = intent.getAction();
			Bundle bundle = intent.getExtras();
			if (TMNetDef.TM_REQ_RESULT_ACTION_USERLOGIN.equals(actionstr)) {
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
								if (TMNetDef.TM_REQ_TYPE_USERLOGIN_IN == detailtype ||
										TMNetDef.TM_REQ_TYPE_USERLOGIN_OFF == detailtype) {
									Message msg = new Message();
									msg.what = MSG_POPUP_REQ_RESULT;
									msg.arg1 = mapTMResults2LocalStatus(status);
									mMsgHandler.sendMessage(msg);
								} 
							}
						}
					}
				}
			}
		}
	}

	private static int mapTMResults2LocalStatus(int tmresult) {
		int status = STATUS_REQ_ERR_NETWORK;
		if (TMNetDef.TM_SUCCESS == tmresult
				|| TMNetDef.TM_ERR_USER_LOGIN_DUPLICATE == tmresult) {
			status = STATUS_REQ_DONE;
		}
		return status;
	}

	private class TestReqRunner implements Runnable {
		@Override
		public void run() {
			final int maxstatus = 4;
			try {
				Thread.sleep(TIME_2_SECONDS);
			} catch (InterruptedException e) {
			}
			Random rd = new Random();
			Message msg = new Message();
			msg.what = MSG_POPUP_REQ_RESULT;
			// the test only random send a value indicate the status
			msg.arg1 = rd.nextInt(maxstatus) + 1; // 1,2,3,4
			mMsgHandler.sendMessage(msg);
		}
	}
}
