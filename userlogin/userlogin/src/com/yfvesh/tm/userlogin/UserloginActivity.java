package com.yfvesh.tm.userlogin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.yfvesh.tm.tmnetservice.ITMSrvStatusLstnr;
import com.yfvesh.tm.tmnetservice.TMNetServiceHelper;
import com.yfvesh.tm.utility.TMDevices;

public class UserloginActivity extends Activity implements OnClickListener, ITMSrvStatusLstnr {

	/* the number for max text length for user name and password */
	public static final int MAX_INPUT_LENGTH = 15;

	/* the message to indicate TMNetSrv is connected */
	private static final int MSG_TMNET_SRV_CONNECTED = 301;
	private static final int MSG_TMNET_SRV_DISCONNECTED = 302;
	
	/* the tag for bundle data keys */
	public static final String USER_NAME = "user_name";
	public static final String USER_PW = "user_password";
	public static final String DIALOG_POPUP_REASON = "dlgreason";

	/* save the result for saved logged info */
	private String mUserName;
	private String mUserPW;

	/* save the result send for log request */
	private String mTmpUserName;
	private String mTmpUserPW;

	/* request code for login pop-up */
	public static final int REQUEST_CODE_POPUP_LOGIN = 1;
	public static final int REQUEST_CODE_POPUP_LOGOFF = 2;

	/* the input for user name */
	private AutoCompleteTextView mUsernameEdit;
	/* the input for user password */
	private EditText mUserpwEdit;

	private Button mBtnLogin;
	private Button mBtnSkip;
	private Button mBtnExit;
	private Button mBtnLogOff;

	private Button mBtnDelUsrName;
	private Button mBtnDelUsrPW;

	private SharedPreferences mPreferences;

	public static TMNetServiceHelper TMNetSrvHelper;
	private MsgHandler mMsgHandler = new MsgHandler();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		initTMNetServiceHelper();
		TMNetSrvHelper.bindTMNetService();
		
		getUserInfo();

		mBtnDelUsrName = (Button) this.findViewById(R.id.btndelusername);
		mBtnDelUsrName.setOnClickListener(this);
		mBtnDelUsrPW = (Button) this.findViewById(R.id.btndeluserpw);
		mBtnDelUsrPW.setOnClickListener(this);

		mBtnLogin = (Button) this.findViewById(R.id.btnlogin);
		mBtnLogin.setOnClickListener(this);
		mBtnSkip = (Button) this.findViewById(R.id.btnskip);
		mBtnSkip.setOnClickListener(this);
		mBtnExit = (Button) this.findViewById(R.id.btnexit);
		mBtnExit.setOnClickListener(this);
		mBtnLogOff = (Button) this.findViewById(R.id.btnlogoff);
		mBtnLogOff.setOnClickListener(this);

		mUsernameEdit = (AutoCompleteTextView) this
				.findViewById(R.id.editTextUserName);
		mUserpwEdit = (EditText) this.findViewById(R.id.textuserpassword);

		setInputLimit();
		initWidgetStates();

		mUsernameEdit.addTextChangedListener(new android.text.TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				initLoginButtonState();
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});

		mUserpwEdit.addTextChangedListener(new android.text.TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				initLoginButtonState();
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btndelusername: {
			onBtnDelUsrnameClick();
			break;
		}
		case R.id.btndeluserpw: {
			onBtnDelUsrpwClick();
			break;
		}
		case R.id.btnlogin: {
			startLogin();
			break;
		}
		case R.id.btnexit: {
			this.finish();
			break;
		}
		case R.id.btnlogoff: {
			startLogoff();
			break;
		}
		case R.id.btnskip: {
			// may do something
			// e.g, save status user not logged in then
			this.finish();
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		if(TMNetSrvHelper!= null) {
			TMNetSrvHelper.unbindTMNetService();
		}
		super.onDestroy();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (REQUEST_CODE_POPUP_LOGIN == requestCode) {
			if (RESULT_OK == resultCode) {
				// login DONE, finish this
				saveUserInfo();
				this.finish();
			}
		} else if (REQUEST_CODE_POPUP_LOGOFF == requestCode) {
			// assume this always success
			logoffUserInfo();
			initWidgetStates();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void onBtnDelUsrnameClick() {
		if (mUsernameEdit != null && (!isUserLogged())) {
			if (!mUsernameEdit.isFocused()) {
				mUsernameEdit.requestFocus();
				return;
			}
			int cursor = mUsernameEdit.getSelectionStart();
			mUsernameEdit.setText(trimCharBeforeIndex(mUsernameEdit.getText()
					.toString(), cursor));
			cursor = (cursor > 1) ? (cursor - 1) : 0;
			mUsernameEdit.setSelection(cursor);
		}
	}

	private void onBtnDelUsrpwClick() {
		if (mUserpwEdit != null && (!isUserLogged())) {
			if (!mUserpwEdit.isFocused()) {
				mUserpwEdit.requestFocus();
				return;
			}
			int cursor = mUserpwEdit.getSelectionStart();
			mUserpwEdit.setText(trimCharBeforeIndex(mUserpwEdit.getText()
					.toString(), cursor));
			cursor = (cursor > 1) ? (cursor - 1) : 0;
			mUserpwEdit.setSelection(cursor);
		}
	}

	private void saveUserInfo() {
		mUserName = mTmpUserName;
		mUserPW = mTmpUserPW;

		/* save user info to preference */
		saveUserInfoToPreference(USER_NAME, mUserName);
		saveUserInfoToPreference(USER_PW, mUserPW);
	}

	private void saveUserInfoToPreference(String key, String text) {
		if (key != null && text != null) {
			Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
					.edit();
			editor.putString(key, text);
			editor.commit();
		}
	}

	private void delUserInfoFromPreferenceByKey(String key) {
		if (key != null) {
			Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
					.edit();
			editor.remove(key);
			editor.commit();
		}
	}

	private void logoffUserInfo() {
		mUserName = "";
		mUserPW = "";
	}

	private void getUserInfo() {
		/* get the user info from local preference */
		if (mPreferences.contains(USER_NAME) && mPreferences.contains(USER_PW)) {
			mUserName = mPreferences.getString(USER_NAME, "");
			mUserPW = mPreferences.getString(USER_PW, "");
		}
	}

	private boolean isUserLogged() {
		boolean result = false;
		checkTMNetSrvHelperInited();
		if(TMNetSrvHelper.checkTMNetSrvWorkable()) {
			Log.d(this.getClass().getSimpleName(), "**checkTMNetSrvWorkable return true;");
			//if net service not workable, treated as user not logged
			if (TMNetSrvHelper.tmCheckUserLoginStatus()) {
				Log.d(this.getClass().getSimpleName(), "**checkTMNetSrvWorkable tmCheckUserLoginStatus return true;");
				result = true;
				if (mUserName == null || mUserPW == null || mUserName.length() == 0
						|| mUserPW.length() == 0) {
					getUserInfo();
				}
			} else {
				Log.d(this.getClass().getSimpleName(), "**checkTMNetSrvWorkable tmCheckUserLoginStatus return false;");
			}
		} else {
			Log.d(this.getClass().getSimpleName(), "**checkTMNetSrvWorkable return false;");
		}
		return result;
	}

	private void startLogin() {
		mTmpUserName = mUsernameEdit.getText().toString();
		mTmpUserPW = mUserpwEdit.getText().toString();
		if (mTmpUserName.length() > 0 && mTmpUserPW.length() > 0) {
			Intent intent = new Intent();
			intent.setClass(this, PopUpDialog.class);
			intent.putExtra(UserloginActivity.USER_NAME, mTmpUserName);
			intent.putExtra(UserloginActivity.USER_PW, mTmpUserPW);
			intent.putExtra(DIALOG_POPUP_REASON, REQUEST_CODE_POPUP_LOGIN);
			startActivityForResult(intent, REQUEST_CODE_POPUP_LOGIN);
		}
	}

	private void startLogoff() {
		if (mUserName.length() > 0 && mUserPW.length() > 0) {
			Intent intent = new Intent();
			intent.setClass(this, PopUpDialog.class);
			intent.putExtra(UserloginActivity.USER_NAME, mUserName);
			intent.putExtra(UserloginActivity.USER_PW, mUserPW);
			intent.putExtra(DIALOG_POPUP_REASON, REQUEST_CODE_POPUP_LOGOFF);
			startActivityForResult(intent, REQUEST_CODE_POPUP_LOGOFF);
		}
	}

	private void setInputLimit() {
		InputFilter[] filterarray = new InputFilter[1];
		filterarray[0] = new InputFilter.LengthFilter(MAX_INPUT_LENGTH);
		if (mUsernameEdit != null) {
			mUsernameEdit.setFilters(filterarray);
		}
		if (mUserpwEdit != null) {
			mUserpwEdit.setFilters(filterarray);
		}
	}

	private void initWidgetStates() {
		if (isUserLogged()) {
			Log.d(this.getClass().getSimpleName(), "initWidgetStates initUserLoggedUi");
			initUserLoggedUi();
		} else {
			Log.d(this.getClass().getSimpleName(), "initWidgetStates initUserUnloggedUi");
			initUserUnloggedUi();
			initLoginButtonState();
		}
	}

	private void initUserLoggedUi() {
		mBtnLogin.setVisibility(View.GONE);
		mBtnSkip.setVisibility(View.GONE);
		mBtnExit.setVisibility(View.VISIBLE);
		mBtnLogOff.setVisibility(View.VISIBLE);

		mUsernameEdit.setText(mUserName);
		mUserpwEdit.setText(mUserPW);

		// mBtnDelUsrName.setEnabled(false);
		// mBtnDelUsrPW.setEnabled(false);

		mUsernameEdit.setEnabled(false);
		mUserpwEdit.setEnabled(false);
	}

	private void initUserUnloggedUi() {
		mBtnLogin.setVisibility(View.VISIBLE);
		mBtnSkip.setVisibility(View.VISIBLE);
		mBtnExit.setVisibility(View.GONE);
		mBtnLogOff.setVisibility(View.GONE);

		mUsernameEdit.setText("");
		mUserpwEdit.setText("");

		mBtnDelUsrName.setEnabled(true);
		mBtnDelUsrPW.setEnabled(true);
		mUsernameEdit.setEnabled(true);
		mUserpwEdit.setEnabled(true);
	}

	/* help to set the login button states based on user input */
	private void initLoginButtonState() {
		if (validateUsername() && validateUserpw()) {
			if (mBtnLogin != null) {
				mBtnLogin.setEnabled(true);
			}
		} else {
			if (mBtnLogin != null) {
				mBtnLogin.setEnabled(false);
			}
		}
	}

	private boolean validateUsername() {
		boolean bvalid = false;
		if (mUsernameEdit != null
				&& mUsernameEdit.getText().toString().length() > 0) {
			bvalid = true;
		}
		return bvalid;
	}

	private boolean validateUserpw() {
		boolean bvalid = false;
		if (mUserpwEdit != null
				&& mUserpwEdit.getText().toString().length() > 0) {
			bvalid = true;
		}
		return bvalid;
	}

	public static String trimCharBeforeIndex(String str, int index) {
		if (str != null && str.length() > 0) {
			if (index < 0 || index > str.length()) {
				return str;
			}
			String sufixstr = str.substring(0, index);
			String posixstr = str.substring(index, str.length());
			return trimEndChar(sufixstr) + posixstr;
		} else {
			return "";
		}
	}

	public static String trimEndChar(String str) {
		if (str != null && str.length() > 1) {
			return str.substring(0, str.length() - 1);
		} else {
			return "";
		}
	}

	@Override
	public void onTMSrvConnected() {
		Log.d(getClass().getSimpleName(), "UserloginActivity onTMSrvConnected");
		if(mMsgHandler!=null) {
			mMsgHandler.sendEmptyMessage(MSG_TMNET_SRV_CONNECTED);
		}
	}

	@Override
	public void onTMSrvDisconnected() {
		if(mMsgHandler!=null) {
			mMsgHandler.sendEmptyMessage(MSG_TMNET_SRV_DISCONNECTED);
		}
	}
	
	private void initTMNetServiceHelper() {
		TMNetSrvHelper = new TMNetServiceHelper(this,this);
		TMNetSrvHelper.setSimNumToTMNetSrv(TMDevices.getSimNum(this));
	}
	
	private void checkTMNetSrvHelperInited() {
		if(TMNetSrvHelper == null) {
			initTMNetServiceHelper();
			TMNetSrvHelper.bindTMNetService();
		} else if (!TMNetSrvHelper.checkTMNetSrvWorkable()) {
			TMNetSrvHelper.bindTMNetService();
		}
	}
	
	private class MsgHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(MSG_TMNET_SRV_CONNECTED == msg.what) {
				Log.d(this.getClass().getSimpleName(), "MsgHandler MSG_TMNET_SRV_CONNECTED");
				initWidgetStates();
			}else {
				super.handleMessage(msg);
			}
		}
	}
}