package com.yfvesh.tm.weatherrep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yfvesh.tm.weatherrep.ScrollLayout.OnScrollToScreenListener;

public class WeatherreportActivity extends Activity implements OnClickListener,
		OnScrollToScreenListener {
	/** Called when the activity is first created. */
	/*
	 * private WeatherCtrlM mSecDayWeatherCtrl = null; private WeatherCtrlM
	 * mthrdDayWeatherCtrl = null;
	 * 
	 * private WeatherCtrlL mFirstDayWeatherCtrl = null;
	 * 
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * super.onCreate(savedInstanceState); setContentView(R.layout.weather);
	 * 
	 * mSecDayWeatherCtrl = (WeatherCtrlM)
	 * this.findViewById(R.id.weatherCtrlMLayout1); LinearLayout
	 * layoutWeatherCtrlMSec =
	 * (LinearLayout)mSecDayWeatherCtrl.findViewById(R.id.layoutWeatherCtrlM);
	 * layoutWeatherCtrlMSec.setOnClickListener(this);
	 * 
	 * mthrdDayWeatherCtrl = (WeatherCtrlM)
	 * this.findViewById(R.id.weatherCtrlMLayout2); LinearLayout
	 * layoutWeatherCtrlMThrd =
	 * (LinearLayout)mthrdDayWeatherCtrl.findViewById(R.id.layoutWeatherCtrlM);
	 * layoutWeatherCtrlMThrd.setOnClickListener(this);
	 * 
	 * mFirstDayWeatherCtrl = (WeatherCtrlL)
	 * this.findViewById(R.id.weatherCtrlLLayout1); Button butRefresh =
	 * (Button)mFirstDayWeatherCtrl.findViewById(R.id.btnRefresh);
	 * butRefresh.setOnClickListener(this); }
	 * 
	 * @Override public void onClick(View arg0) { // TODO Auto-generated method
	 * stub switch(arg0.getId()) { case R.id.layoutWeatherCtrlM:
	 * onLayoutMClick(arg0); break; case R.id.btnRefresh:
	 * onButtonRefreshClick(arg0); break; default: break;
	 * 
	 * } }
	 * 
	 * public void onLayoutMClick(View arg0) { switch(((View)
	 * arg0.getParent()).getId()) { case R.id.weatherCtrlMLayout1: break; case
	 * R.id.weatherCtrlMLayout2: break; default: break;
	 * 
	 * } }
	 * 
	 * public void onButtonRefreshClick(View arg0){ switch(((View)
	 * arg0.getParent()).getId()) { case R.id.weatherCtrlLLayout1: break;
	 * default: break;
	 * 
	 * }
	 * 
	 * }
	 */
	public static final int MODE_DAY = 0;
	public static final int MODE_NGT = 1;

	private LinearLayout mLayoutTitle = null;
	private LinearLayout mLayoutBg = null;
	
	private WeatherCtrl mWeatherCtrl1 = null;
	private WeatherCtrl mWeatherCtrl2 = null;
	private ScrollLayout mScrollLayout = null;
	
	private Button mBtnCity = null;
	private Button mBtnBack = null;

	CityDatabaseHelper mDBHelper = null;
	
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mBtnCity = (Button)this.findViewById(R.id.btn_city_manage);
		mBtnCity.setOnClickListener(this);
		
		mBtnBack = (Button)this.findViewById(R.id.btnfinish);
		mBtnBack.setOnClickListener(this);
		
		mLayoutTitle = (LinearLayout)this.findViewById(R.id.layout_title);
		
		mScrollLayout = (ScrollLayout)this.findViewById(R.id.ScrollLayout);
		
		mLayoutBg = (LinearLayout)this.findViewById(R.id.layout_main);
		
		setMode(MODE_DAY);
		
		// make Layout
		mWeatherCtrl1 = new WeatherCtrl(this);
		mWeatherCtrl1.setMode(MODE_NGT);
		mScrollLayout.addView(mWeatherCtrl1);
		
		// make Layout
		mWeatherCtrl2 = new WeatherCtrl(this);
		mWeatherCtrl2.setMode(MODE_DAY);
		mScrollLayout.addView(mWeatherCtrl2);

		mScrollLayout.setOnScrollToScreen(this);// 设置滑动时的监听

		mScrollLayout.setDefaultScreen(0);// 设置ScrollLayout的默认显示第几个屏幕的内容

		//create a thread to init city database
		//create a db
		try {
			initCityDatabase();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_city_manage:
				switchToCityManage();
				break;
			case R.id.btnfinish:
				finish();
				break;
			default:
				
				break;
				
		}
	}

	private void switchToCityManage(){

		Intent i = new Intent(this,
				com.yfvesh.tm.weatherrep.CityManagerActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(i);
	}
	
	public void setMode(int mode) {
		// TODO Auto-generated method stub
		if (mode == MODE_DAY) {
			mLayoutBg.setBackgroundResource(R.drawable.bg_weather_blue_day);
		} else {
			mLayoutBg.setBackgroundResource(R.drawable.bg_weather_dark_day);
		}
	}

	@Override
	public void doAction(int whichScreen) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "滑动到了第" + whichScreen + "个屏幕", 300).show();
	}
	
	public void initCityDatabase() throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		CityDatabaseHelper mDBHelper = new CityDatabaseHelper(this);
		/*make sure if table exist*/	
		List<TMCityDatas> dataLst = new ArrayList<TMCityDatas>();
		String  projection[] = new String[]{TMCityDatas.TM_CITY,
											TMCityDatas.TM_CODE,
											TMCityDatas.TM_COUNTRY,
											TMCityDatas.TM_PROVINCE,
											TMCityDatas._ID};
		String selection = TMCityDatas._ID+"=1";
		dataLst = mDBHelper.query(projection, selection, null, null);
		/*table is not exist,init table*/	
		if(0 == dataLst.size())
		{
			try {
				mDBHelper.initDB();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}