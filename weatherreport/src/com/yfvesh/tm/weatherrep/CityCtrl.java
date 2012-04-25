package com.yfvesh.tm.weatherrep;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CityCtrl extends LinearLayout {
	
	private TextView mTxtViewCityName;
	private int 	 mStatus = STATUS_INVALID;
	public static final int STATUS_INVALID = -1;
	public static final int STATUS_LOCATION = 0;
	public static final int STATUS_CITY = 1;
	public static final int STATUS_ADDCITY = 2;
	
    public CityCtrl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    public CityCtrl(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.city_manager_ctrl, this,true);
		mTxtViewCityName = (TextView) findViewById(R.id.txtviewCityName);
	}

    
    public void setCityNameText(String cityName)
    {
    	mTxtViewCityName.setText(cityName);
    }
    
    public int getStauts()
    {
    	return mStatus;
    }
    
    public void setStatus(int status)
    {
    	mStatus = status;
    }
   
}