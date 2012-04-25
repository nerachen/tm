package com.yfvesh.tm.tmnetservicetest;

import com.yfvesh.tm.tmnetservicetest.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TestMainActivity extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testmain);
		addTabs();
	}

	private void addTabs() {
		addTab("TestGpsUploadActivity", "GpsUpload",
				TestGpsUploadActivity.class);
		addTab("TestTMCActivity", "TMC",
				TestTMCActivity.class);
		addTab("TestUsrLoginActivity", "UsrLogin",
				TestUsrLoginActivity.class);
		addTab("TestVehicleDataActivity", "VehicleData",
				TestVehicleDataActivity.class);
		addTab("TestVehicleStatusActivity", "VehicleStatus",
				TestVehicleStatusActivity.class);
		addTab("TestWeatherActivity", "Weather",
				TestWeatherActivity.class);
		addTab("TestRemotePushActivity", "RemotePush",
				TestRemotePushActivity.class);
		
	}

	private void addTab(String name, String indicator, Class<?> cls) {
		Intent intent = new Intent()
				.setClass(this, cls);
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec = tabHost.newTabSpec(name)
				.setIndicator(indicator, null).setContent(intent);
		tabHost.addTab(spec);
	}
}
