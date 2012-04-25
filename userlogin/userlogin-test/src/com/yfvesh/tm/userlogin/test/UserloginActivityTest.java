package com.yfvesh.tm.userlogin.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.yfvesh.tm.userlogin.R;
import com.yfvesh.tm.userlogin.UserloginActivity;

public class UserloginActivityTest extends
		ActivityInstrumentationTestCase2<UserloginActivity> {

	private UserloginActivity mActivity;
	/* the input for user name */
	private AutoCompleteTextView mUsernameEdit;
	/* the input for user password */
	private EditText mUserpwEdit;

	private Button mBtnLogin;

	public UserloginActivityTest() {
		super("com.yfvesh.tm.userlogin", UserloginActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		setActivityInitialTouchMode(false);
		mUsernameEdit = (AutoCompleteTextView) mActivity
				.findViewById(R.id.editTextUserName);
		mUserpwEdit = (EditText) mActivity.findViewById(R.id.textuserpassword);
		mBtnLogin = (Button) mActivity.findViewById(R.id.btnlogin);
	}

	@Override
	protected void tearDown() throws Exception {
		mActivity = null;
		super.tearDown();
	}

	public void testPreConditon() {
		assertNotNull( mUsernameEdit );
		assertNotNull( mUserpwEdit );
		assertNotNull( mBtnLogin );
		assertEquals(false,mBtnLogin.isEnabled());
	}
	
	public void testInputUserNameCase1() {
		mActivity.runOnUiThread(new Runnable() {
	          public void run() {
	        	  mUsernameEdit.requestFocus();
	          }
	      });
		this.sendKeys(KeyEvent.KEYCODE_A);
		this.sendKeys(KeyEvent.KEYCODE_N);
		this.sendKeys(KeyEvent.KEYCODE_D);
		this.sendKeys(KeyEvent.KEYCODE_R);
		this.sendKeys(KeyEvent.KEYCODE_O);
		this.sendKeys(KeyEvent.KEYCODE_I);
		this.sendKeys(KeyEvent.KEYCODE_D);
		getInstrumentation().waitForIdleSync();
		String text = mUsernameEdit.getText().toString();
		assertEquals("android",text);
	}
	
	public void testInputUserPWCase1() {
		mActivity.runOnUiThread(new Runnable() {
	          public void run() {
	        	  mUserpwEdit.requestFocus();
	          }
	      });
		this.sendKeys(KeyEvent.KEYCODE_1);
		this.sendKeys(KeyEvent.KEYCODE_2);
		this.sendKeys(KeyEvent.KEYCODE_3);
		this.sendKeys(KeyEvent.KEYCODE_4);
		this.sendKeys(KeyEvent.KEYCODE_5);
		this.sendKeys(KeyEvent.KEYCODE_6);
		this.sendKeys(KeyEvent.KEYCODE_7);
		getInstrumentation().waitForIdleSync();
		String text = mUserpwEdit.getText().toString();
		assertEquals("1234567",text);
		
	}
}
