package com.yfvesh.tm.tmnetservice.test;

import android.content.Intent;
import android.os.RemoteException;
import android.test.ServiceTestCase;

import com.yfvesh.tm.tmnetservice.ITMNetService;
import com.yfvesh.tm.tmnetservice.TMNetService;

public class TMNetServiceTest extends ServiceTestCase<com.yfvesh.tm.tmnetservice.TMNetService> {

	private ITMNetService mRemoteTMSercice = null;

	public TMNetServiceTest() {
		super(com.yfvesh.tm.tmnetservice.TMNetService.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.setAction("com.yfvesh.tm.tmnetservice.TMNET_SERVICE");
		mRemoteTMSercice = ITMNetService.Stub.asInterface(bindService(intent));
	}

	@Override
	protected void tearDown() throws Exception {
		
		super.tearDown();
	}

	public void testPreConditon() {
		assertNotNull( mRemoteTMSercice );
		// assertNotNull( mUserpwEdit );
		// assertNotNull( mBtnLogin );
		// assertEquals(false,mBtnLogin.isEnabled());
	}
	
	public void testUserLogin() {
		assertNotNull( mRemoteTMSercice );
		try {
			int result = mRemoteTMSercice.userLogin(10001, "username", "password");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assertEquals(true,mRemoteTMSercice.isUserLogged());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testUserLogOut() {
		assertNotNull( mRemoteTMSercice );
		try {
			mRemoteTMSercice.userLogin(10002, "username", "password");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assertEquals(true,mRemoteTMSercice.isUserLogged());
			mRemoteTMSercice.userLogout(10003, "username");
			assertEquals(false,mRemoteTMSercice.isUserLogged());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
