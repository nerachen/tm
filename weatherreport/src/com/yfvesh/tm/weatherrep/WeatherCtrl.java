package com.yfvesh.tm.weatherrep;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherCtrl extends LinearLayout {
	
	private WeatherTipsCtrl mTipsCarwashing;
	private WeatherTipsCtrl mTipsTravaling;
	private WeatherTipsCtrl mTipsDressing;
	private WeatherTipsCtrl mTipsFeeling;
	private WeatherCtrlM mSecDayWeather;
	private WeatherCtrlM mTrdDayWeather;
	private WeatherCtrlL  mFirstDayWeather;
	
	public static final int MODE_DAY = 0;
	public static final int MODE_NGT = 1;
	
    public WeatherCtrl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.weather, this,true);
		mTipsCarwashing = (WeatherTipsCtrl) findViewById(R.id.weatherTipsCtrlLayout1);
		mTipsTravaling = (WeatherTipsCtrl) findViewById(R.id.weatherTipsCtrlLayout2);
		mTipsDressing = (WeatherTipsCtrl) findViewById(R.id.weatherTipsCtrlLayout3);
		mTipsFeeling = (WeatherTipsCtrl) findViewById(R.id.weatherTipsCtrlLayout4);
		
		mSecDayWeather = (WeatherCtrlM) findViewById(R.id.weatherCtrlMLayout1);
		mTrdDayWeather = (WeatherCtrlM) findViewById(R.id.weatherCtrlMLayout2);
		
		mFirstDayWeather = (WeatherCtrlL) findViewById(R.id.weatherCtrlLLayout1);
		
		//set tips style
		mTipsCarwashing.setTipsStyle(WeatherTipsCtrl.STYLE_CARWASHING);
		mTipsTravaling.setTipsStyle(WeatherTipsCtrl.STYLE_TRAVALING);
		mTipsDressing.setTipsStyle(WeatherTipsCtrl.STYLE_DRESSING);
		mTipsFeeling.setTipsStyle(WeatherTipsCtrl.STYLE_FEELING);
	}
    
    public WeatherCtrl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		
	}

    
    public void setMode(int mode)
    {
    	
    	mTipsCarwashing.setMode(mode);
    	mTipsTravaling.setMode(mode);
    	mTipsDressing.setMode(mode);
    	mTipsFeeling.setMode(mode);
    	
    	mSecDayWeather.setMode(mode);
    	mTrdDayWeather.setMode(mode);
    	
    	mFirstDayWeather.setMode(mode);
    }
}