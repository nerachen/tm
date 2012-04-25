package com.yfvesh.tm.tmdataprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class TMDatas implements BaseColumns {
	
	public static final Uri CONTENT_URI = 
		Uri.parse("content://"+TMDataProvider.AUTHORITY+"/tmdatas");
	public static final String CONTENT_TYPE_ITEMS = 
		"vnd.android.cursor.dir/vnd.yfveshtm.tmdatas";
	public static final String CONTENT_TYPE_ITEM = 
		"vnd.android.cursor.item/vnd.yfveshtm.tmdata";
	
	public static final String TM_KEY = "tmkey";
	public static final String TM_TEXT = "tmtext";
	public static final String TM_NUMBER = "tmnumber";
	
	private int id;
	private String key;
	private String text;
	private int	num;//actually this is not used currently
	
	public TMDatas(int id, String key, String text, int num) {
		this.id = id;
		this.key = key;
		this.text = text;
		this.num = num;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getNumber() {
		return num;
	}
	
	public void setNumber(int num) {
		this.num = num;
	}
}
