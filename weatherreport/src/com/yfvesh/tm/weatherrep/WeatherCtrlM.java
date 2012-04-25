package com.yfvesh.tm.weatherrep;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherCtrlM extends LinearLayout {
	
	private TextView mTxtViewWeather;
	private TextView mTxtViewDate1;
	private TextView mTxtViewDate2;
	private TextView mTxtViewLowTemp;
	private TextView mTxtViewHighTemp;
	private TextView  mTxtViewSeparateLine;
	
	private ImageView  mImageWeatherIcon;
	
	private LinearLayout  mLayout;
	private List<TextView> mTxtviewLst = new LinkedList<TextView>();
	
	public static final int MODE_DAY = 0;
	public static final int MODE_NGT = 1;
	
    public WeatherCtrlM(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    public WeatherCtrlM(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.weather_ctrl_m, this,true);
		mTxtViewWeather = (TextView) findViewById(R.id.txtviewWeatherText);
		mTxtViewDate1 = (TextView) findViewById(R.id.txtviewDate1);
		mTxtViewDate2 = (TextView) findViewById(R.id.txtviewDate2);
		mTxtViewLowTemp = (TextView) findViewById(R.id.txtviewLowestTemp);
		mTxtViewHighTemp = (TextView) findViewById(R.id.txtviewHighestTemp);
		mTxtViewSeparateLine = (TextView) findViewById(R.id.txtviewSepaLine);
		
		mImageWeatherIcon = (ImageView) findViewById(R.id.imgviewWeatherIcon);
		
		mLayout = (LinearLayout) findViewById(R.id.layoutWeatherCtrlM);
		//setLayoutBackground(R.drawable.weather_frame_right_dark);
		//Add txt View to lst
		addtxtviewToLst();
	}
    private void addtxtviewToLst()
    {
    	mTxtviewLst.add(mTxtViewWeather);
    	mTxtviewLst.add(mTxtViewDate1);
    	mTxtviewLst.add(mTxtViewDate2);
    	mTxtviewLst.add(mTxtViewLowTemp);
    	mTxtviewLst.add(mTxtViewHighTemp);
    	mTxtviewLst.add(mTxtViewSeparateLine);
    }

    public void setWeatherText(String weatherContent)
    {
    	mTxtViewWeather.setText(weatherContent);
    }
    
    public void setHighTemp(String Temperature)
    {
    	mTxtViewHighTemp.setText(Temperature);
    }
    
    public void setLowTemp(String Temperature)
    {
    	mTxtViewLowTemp.setText(Temperature);
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
    
    public void setMode(int mode)
    {
    	setModeTextColor(mode);
    	setModeBg(mode);
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
    
    public void setModeBg(int mode)
    {
    	
    	if(MODE_DAY == mode)
    	{
    		setLayoutBackground(R.drawable.weather_ctrl_m_status_day);
    	}
    	else if(MODE_NGT == mode)
    	{
    		setLayoutBackground(R.drawable.weather_ctrl_m_status_ngt);
    	}
    	else
    	{
    		
    	}
    }
}