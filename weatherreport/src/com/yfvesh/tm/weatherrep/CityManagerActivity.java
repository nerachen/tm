package com.yfvesh.tm.weatherrep;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class CityManagerActivity extends Activity implements OnClickListener{
	
	
	private List<CityCtrl> mLstCityCtrl= new ArrayList<CityCtrl>();
	public static List<CityWeatherData> mLstWeatherData = new ArrayList<CityWeatherData>();
	Button mReturn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citymanager);
        initCityCtrlList();
     
        mReturn = (Button)this.findViewById(R.id.btnback);
        mReturn.setOnClickListener(this);
        
       
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
			case R.id.layoutCity:
				onCityLayoutClick(arg0);
				break;
			case R.id.btnback:
				switchToWeatherRep();
				break;
			default:
				
				break;
				
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//android.os.Process.killProcess(android.os.Process.myPid());
		CityManagerActivity.mLstWeatherData.clear();
	}
	
	public void onCityLayoutClick(View arg0)
	{
		CityCtrl cityCtrl =  null;
		cityCtrl= (CityCtrl)this.findViewById(((View) arg0.getParent()).getId());
		if(null != cityCtrl)
		{
			int status = cityCtrl.getStauts();
			switch(status)
			{
				case CityCtrl.STATUS_LOCATION:
					switchToWeatherRepWithExtra();
					break;
				case CityCtrl.STATUS_CITY:
					switchToWeatherRepWithExtra();
					break;
				case CityCtrl.STATUS_ADDCITY:
					addCity();
					break;
				default:
					break;
					
			}
		}

	}
	private void addCity(){

	}
	
	private void switchToWeatherRepWithExtra(){

		Intent i = new Intent(this,
				com.yfvesh.tm.weatherrep.WeatherreportActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(i);
//		this.finish();
	}
	
	private void switchToWeatherRep(){

		Intent i = new Intent(this,
				com.yfvesh.tm.weatherrep.WeatherreportActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startActivity(i);
		//this.finish();
	}
	
	// add to list
	private void initCityCtrlList(){
		CityCtrl tmpCtrl =  null;
		LinearLayout layoutCity = null;
		
		tmpCtrl= (CityCtrl)this.findViewById(R.id.CityCtrl1);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);
		
		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl2);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);
		
		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl3);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);

		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl4);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);

		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl5);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);

		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl6);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);

		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl7);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);

		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl8);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);

		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl9);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);

		tmpCtrl =  (CityCtrl)this.findViewById(R.id.CityCtrl10);
		mLstCityCtrl.add(tmpCtrl);
		//set Click Listener
		layoutCity = (LinearLayout)tmpCtrl.findViewById(R.id.layoutCity);
		layoutCity.setOnClickListener(this);
		
		setWeatherDataList();
		setCityCtrlStatus();

	}
	
	private void setWeatherDataList(){
		CityWeatherData tmpData = new CityWeatherData("当前位置",1);
		CityManagerActivity.mLstWeatherData.add(tmpData);
		
		tmpData = new CityWeatherData("上海",2);
		CityManagerActivity.mLstWeatherData.add(tmpData);
		
		tmpData = new CityWeatherData("北京",3);
		CityManagerActivity.mLstWeatherData.add(tmpData);
		
		tmpData = new CityWeatherData("广州",4);
		CityManagerActivity.mLstWeatherData.add(tmpData);
		
		tmpData = new CityWeatherData("宜昌",5);
		CityManagerActivity.mLstWeatherData.add(tmpData);
		
		tmpData = new CityWeatherData("武汉",6);
		CityManagerActivity.mLstWeatherData.add(tmpData);
		
		tmpData = new CityWeatherData("长沙",7);
		CityManagerActivity.mLstWeatherData.add(tmpData);
	}
	
	private void setCityCtrlStatus(){
		
		int index = 0;
		int weatherDataCount = CityManagerActivity.mLstWeatherData.size();
		int citycCtrlCount = mLstCityCtrl.size();
		CityCtrl ctrl = null;
		
		ctrl = mLstCityCtrl.get(index);
		String cityName = CityManagerActivity.mLstWeatherData.get(index).getCityName();
		ctrl.setCityNameText(cityName);
		ctrl.setStatus(CityCtrl.STATUS_LOCATION);
		ctrl.setVisibility(View.VISIBLE);
		index++;
		
		
		for(index = index;index<weatherDataCount;index++)
		{
			ctrl= mLstCityCtrl.get(index);
			cityName = CityManagerActivity.mLstWeatherData.get(index).getCityName();
			ctrl.setCityNameText(cityName);
			ctrl.setStatus(CityCtrl.STATUS_CITY);
			ctrl.setVisibility(View.VISIBLE);
		}
		ctrl = mLstCityCtrl.get(index);
		ctrl.setCityNameText("+");
		ctrl.setVisibility(View.VISIBLE);
		index++;
		
		for(index = index ;index<citycCtrlCount;index++)
		{
			ctrl= mLstCityCtrl.get(index);
			ctrl.setVisibility(View.INVISIBLE);
		}
		
		
	}
}