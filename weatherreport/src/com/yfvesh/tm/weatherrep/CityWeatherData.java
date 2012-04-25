package com.yfvesh.tm.weatherrep;


public class CityWeatherData{
	
	private String mCityName;//"current" for current position name
	private String mIndexInDataBase; // Index in provider e.g"WEATHER1"
	
	public CityWeatherData(String cityName,int Index)
	{
		setCityName(cityName);
		setIndexInDataBase(Index);
	}
	
	public void setCityName(String cityName)
	{
		mCityName = cityName;
	}
	
	public void setIndexInDataBase(int Index)
	{
		String IndexString;
		IndexString = "WEATHER"+String.valueOf(Index);
		mIndexInDataBase = IndexString;
	}
	
	public String getCityName()
	{
		return mCityName;
	}
	
	public String getIndexInDataBase()
	{
		return mIndexInDataBase;
	}
}