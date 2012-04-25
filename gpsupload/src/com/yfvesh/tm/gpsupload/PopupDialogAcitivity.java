package com.yfvesh.tm.gpsupload;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PopupDialogAcitivity extends Activity implements OnClickListener{

	private Button m_BtnConfirm; 
	private TextView mTxtView;
	private int mWarningType = -1;
	
	public static final int  WARNING_NETWORK =  0;
	public static final int  WARNING_GPS = 1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popupdialog);
		
		mTxtView = (TextView)findViewById(R.id.showString); 
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		mWarningType = bundle.getInt("com.yfvesh.tm.gpsupload");
		switch(mWarningType)
		{
		case WARNING_NETWORK:
			mTxtView.setText(R.string.text_3gnotopen);
			break;
		case WARNING_GPS:
			mTxtView.setText(R.string.text_gpsnotopen);
			break;
		default:
			Toast.makeText(this, "无效的字符串ID", Toast.LENGTH_LONG).show();
			break;
		}
		
		m_BtnConfirm = (Button)findViewById(R.id.confirmbutton);  
		m_BtnConfirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.confirmbutton:
			switchToActivity();
			this.finish();
			break;
		default:
			break;
		}

		
	}
	
	public void switchToActivity(){
		
		Intent i = new Intent();
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
				| Intent.FLAG_ACTIVITY_CLEAR_TOP 
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		
		switch(mWarningType)
		{
		case WARNING_NETWORK:
			i.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
			ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
			i.setComponent(cName);
			break;
		case WARNING_GPS:
			i.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			break;
		default:
			Toast.makeText(this, "无效的警告", Toast.LENGTH_LONG).show();
			return;
		}
		startActivity(i);
	}
}