package com.yfvesh.tm.weatherrep;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherCtrlL extends LinearLayout {
	
	private TextView mTxtViewWeather;
	private TextView mTxtViewWind;
	private TextView mTxtViewDate1;
	private TextView mTxtViewDate2;
	private TextView mTxtViewTemp;
	private TextView mTxtViewCurrTemp;
	private TextView  mTxtViewReleaseTime;
	
	private Button  mBtnRefresh;
	
	private List<TextView> mTxtviewLst = new LinkedList<TextView>();
	
	private ImageView  mImageWeatherIcon;
	
	private LinearLayout  mLayout;

	public static final int MODE_DAY = 0;
	public static final int MODE_NGT = 1;
	
    public WeatherCtrlL(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    public WeatherCtrlL(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.weather_ctrl_l, this,true);
		mTxtViewWeather = (TextView) findViewById(R.id.txtviewWeatherTextL);
		mTxtViewDate1 = (TextView) findViewById(R.id.txtviewDate1L);
		mTxtViewDate2 = (TextView) findViewById(R.id.txtviewDate2L);
		mTxtViewTemp = (TextView) findViewById(R.id.txtviewTemp);
		mTxtViewCurrTemp = (TextView) findViewById(R.id.txtviewCurrTemp);
		mTxtViewReleaseTime = (TextView) findViewById(R.id.txtviewReleaseTime);
		mTxtViewWind = (TextView) findViewById(R.id.txtviewWind);
		
		mImageWeatherIcon = (ImageView) findViewById(R.id.imgviewWeatherIconL);
		mLayout = (LinearLayout) findViewById(R.id.layoutWeatherCtrlL);
		
		mBtnRefresh = (Button) findViewById(R.id.btnRefresh);
		//setLayoutBackground(R.drawable.weather_frame_right_dark);
		
		//Add txt View to lst
		addtxtviewToLst();
	}

    private void addtxtviewToLst()
    {
    	mTxtviewLst.add(mTxtViewWeather);
    	mTxtviewLst.add(mTxtViewDate1);
    	mTxtviewLst.add(mTxtViewDate2);
    	mTxtviewLst.add(mTxtViewTemp);
    	mTxtviewLst.add(mTxtViewCurrTemp);
    	mTxtviewLst.add(mTxtViewReleaseTime);
    	mTxtviewLst.add(mTxtViewWind);
    }
    
    public void setWeatherText(String weatherContent)
    {
    	mTxtViewWeather.setText(weatherContent);
    }
    
    public void setTemp(String Temperature)
    {
    	mTxtViewTemp.setText(Temperature);
    }
    
    public void setCurrTemp(String Temperature)
    {
    	mTxtViewCurrTemp.setText(Temperature);
    }
    
    public void setReleaseTime(String Time)
    {
    	mTxtViewReleaseTime.setText(Time);
    }
    
    public void setWind(String Wind)
    {
    	mTxtViewWind.setText(Wind);
    }
    
    public void setDate1(String Date)
    {
    	mTxtViewDate1.setText(Date);
    }
    
    public void setDate2(String Date)
    {
    	mTxtViewDate2.setText(Date);
    }
    
    public void setImageWeatherIcon(int resId)
    {
    	mImageWeatherIcon.setImageResource(resId);
    }
    
    public void setLayoutBackground(int resId)
    {
    	mLayout.setBackgroundResource(resId);
    }
    
    public void setModeTextColor(int mode)
    {
    	int ViewNum  = mTxtviewLst.size();
    	int dayColor = 0xff0f4687;
    	int ngtColor = 0xffffffff;
    	if(MODE_DAY == mode)
    	{
    		for(int i = 0;i<ViewNum;i++)
    		{
    			mTxtviewLst.get(i).setTextColor(dayColor);
    		}
    	}
    	else if(MODE_NGT == mode)
    	{
    		for(int i = 0;i<ViewNum;i++)
    		{
    			mTxtviewLst.get(i).setTextColor(ngtColor);
    		}
    	}
    	else
    	{
    		
    	}
    }
    
    public void setModeButton(int mode)
    {
    	if(MODE_DAY == mode)
    	{
    		mBtnRefresh.setBackgroundResource(R.drawable.refresh_button_status_day);
    	}
    	else if(MODE_NGT == mode)
    	{
    		mBtnRefresh.setBackgroundResource(R.drawable.refresh_button_status_ngt);
    	}
    	else
    	{
    		
    	}
    }
    
    public void setMode(int mode)
    {
    	setModeTextColor(mode);
    	setModeButton(mode);
    }
    
}