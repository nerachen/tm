package com.yfvesh.tm.weatherrep;

import android.provider.BaseColumns;

public class TMCityDatas implements BaseColumns {
	
	public static final String TM_CITY = "tmcity";
	public static final String TM_CODE = "tmcode";
	public static final String TM_PROVINCE = "tmprovince";
	public static final String TM_COUNTRY = "tmcountry";
	
	private int id;
	private String city;
	private String province;
	private String country;
	private String code;
	
	
	public TMCityDatas() 
	{

	}
	
	public TMCityDatas(int id, 
			 		   String code, 
					   String county, 
					   String city, 
					   String province,
					   String country
					   ) 
	{
		this.id = id;
		this.code = code;
		this.city = city;
		this.province = province;
		this.country = country;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
}
