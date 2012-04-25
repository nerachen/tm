package com.yfvesh.tm.weatherrep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


class CityDatabaseHelper extends SQLiteOpenHelper {

		private static final String TAG = "TMCityDataDBTag";
		private static final int DATABASE_VERSION = 1;
		public static final String DATABASE_NAME = "yftmcitydata.db";
		private static final String DATA_TABLE_NAME = "tmcitydatas";
		
		private static final String XML_COUNTRY = "country";
		private static final String XML_PROVINCE = "province";
		private static final String XML_CITY = "city";
		private static final String XML_NAME = "name";
		private static final String XML_CODE = "code";
		
		private Context mContext = null;
		
		public static final String DELETE_ALL = "1";
		
		
		public CityDatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		public CityDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE_NAME);
			db.execSQL("CREATE TABLE " + DATA_TABLE_NAME + " (" 
						+ TMCityDatas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
						+ TMCityDatas.TM_CODE + " TEXT," 
						+ TMCityDatas.TM_CITY + " TEXT," 
						+ TMCityDatas.TM_PROVINCE + " TEXT,"
						+ TMCityDatas.TM_COUNTRY + " TEXT" 
						+ ");");
			

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "onUpgrade from oldVersion:" + oldVersion
					+ " to newVersion:" + newVersion);
			onCreate(db);
		}
		
		public void insert(TMCityDatas data)
		{
			//获取数据库对象
			SQLiteDatabase db = this.getWritableDatabase();
			
			//使用insert方法向表中插入数据
			ContentValues values = new ContentValues();
			values.put(TMCityDatas.TM_CODE, data.getCode());
			values.put(TMCityDatas.TM_CITY, data.getCity());
			values.put(TMCityDatas.TM_PROVINCE, data.getProvince());
			values.put(TMCityDatas.TM_COUNTRY, data.getCountry());
	
			//调用方法插入数据
			long id = db.insert(DATA_TABLE_NAME, null, values);
			//关闭SQLiteDatabase对象
			db.close();	
		}
		public List<TMCityDatas> query(String[] projection,String selection,
				String[] selectionArgs, String sortOrder)
		{
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.query(DATA_TABLE_NAME, projection, selection, selectionArgs, null,
					null, sortOrder);
			List<TMCityDatas> cityList = new ArrayList<TMCityDatas>();
			TMCityDatas data  = new TMCityDatas();
			
			int indexOfId = c.getColumnIndex(TMCityDatas._ID);
			int indexOfCity = c.getColumnIndex(TMCityDatas.TM_CITY);
			int indexOfCode = c.getColumnIndex(TMCityDatas.TM_CODE);
			int indexOfProvince = c.getColumnIndex(TMCityDatas.TM_PROVINCE);
			int indexOfCountry = c.getColumnIndex(TMCityDatas.TM_COUNTRY);
			
			
			for (c.moveToFirst();!(c.isAfterLast());c.moveToNext()) 
			{
				data.setCity(c.getString(indexOfCity));
				data.setCode(c.getString(indexOfCode));
				data.setCountry(c.getString(indexOfCountry));
				data.setProvince(c.getString(indexOfProvince));
				data.setId(c.getInt(indexOfId));
				cityList.add(data);
			}
			return cityList;
		}

		public void delete(String selection,String[] selectionArgs)
		{
			
			SQLiteDatabase db = this.getWritableDatabase();
			
			//delete
			db.delete(DATA_TABLE_NAME, selection, selectionArgs);
		}
		
		public void update(TMCityDatas data,String selection,String[] selectionArgs)
		{
			
			//获取数据库对象
			SQLiteDatabase db = this.getWritableDatabase();
			//使用update方法更新表中的数据
			ContentValues values = new ContentValues();
			values.put(TMCityDatas.TM_CODE, data.getCode());
			values.put(TMCityDatas.TM_CITY, data.getCity());
			values.put(TMCityDatas.TM_PROVINCE, data.getProvince());
			values.put(TMCityDatas.TM_COUNTRY, data.getCountry());
			
			//更新
			db.update(DATABASE_NAME, values, selection, selectionArgs);
		}
		
		public void initDB() throws XmlPullParserException, IOException
		{
			//get Resources   
            Resources res = mContext.getResources();   
            //get XmlResourceParser instantce   
            XmlResourceParser xrp = res.getXml(R.xml.city);  
            TMCityDatas data = new TMCityDatas();
            String country = null;
            String province = null;
            String city = null;
            String code = null;
            
            while(xrp.getEventType()!=XmlResourceParser.END_DOCUMENT){
            	if(xrp.getEventType() == XmlResourceParser.START_TAG
            			&& xrp.getName().equals(XML_COUNTRY))
            	{
            		//country
            		country = xrp.getAttributeValue(null, XML_NAME);
            	}
            	
            	else if(xrp.getEventType() == XmlResourceParser.START_TAG
    					&& xrp.getName().equals(XML_PROVINCE))
            	{
            		//province
            		province = xrp.getAttributeValue(null, XML_NAME);	
            	}
            	
            	else if(xrp.getEventType() == XmlResourceParser.START_TAG
    					&& xrp.getName().equals(XML_CITY))
            	{
            		//city
            		city = xrp.getAttributeValue(null, XML_NAME);
    				code = xrp.getAttributeValue(null, XML_CODE);
    				data.setCity(city);
    				data.setProvince(province);
    				data.setCountry(country);
    				data.setCode(code);
    				insert(data);
            	}
            	else
            	{
            		
            	}
            	xrp.next();
            }
		}
	}
