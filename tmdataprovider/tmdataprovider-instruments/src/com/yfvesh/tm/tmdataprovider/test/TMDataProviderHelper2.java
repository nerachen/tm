package com.yfvesh.tm.tmdataprovider.test;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public final class TMDataProviderHelper2 {

	private static final String TAG = "TMDataProviderHelperTag";
	private static final String SQL_CONNECTOR = "= ";
	private static final String SQL_CONNECTOR_COMMA = "= '";
	private static final String SQL_COMMA = "'";

	private TMDataProviderHelper2() {
	}

	private static final String[] TMDATA_COLUMNS = new String[] { TMDatas._ID,
			TMDatas.TM_KEY, TMDatas.TM_TEXT, TMDatas.TM_NUMBER };

	public static Uri insertTMData(String key, String txt,
			ContentProvider ctxprovider) {
		if(!isProviderExist(ctxprovider)) {
			return null;
		}
		
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(TMDatas.TM_KEY, key);
		contentvalues.put(TMDatas.TM_TEXT, txt);
		
		Uri uri = ctxprovider.insert(TMDatas.CONTENT_URI, contentvalues);
		Log.d(TAG, "insertTMData return uri:" + uri);
		return uri;
	}

	public static void updateTMDataByKey(String key, String txt,
			ContentProvider ctxprovider) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(TMDatas.TM_KEY, key);
		contentvalues.put(TMDatas.TM_TEXT, txt);
		int count = updateValue(contentvalues, null, null, ctxprovider);
		if (count == 0) {
			Log.d(TAG, "updateTMData not exit,insert it instead");
			insertTMData(key, txt, ctxprovider);
		}
	}

	public static void updateTMDataById(int id, String key, String txt,
			ContentProvider ctxprovider) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(TMDatas.TM_KEY, key);
		contentvalues.put(TMDatas.TM_TEXT, txt);
		String selection = TMDatas._ID + SQL_CONNECTOR + id;
		int count = updateValue(contentvalues, selection, null, ctxprovider);
		if (count == 0) {
			Log.d(TAG, "failed to find the id, and abondad to create new item ");
		}
	}

	public static List<TMDatas> getTMDataByKey(String key,
			ContentProvider ctxprovider) {
		String selection = TMDatas.TM_KEY + SQL_CONNECTOR_COMMA + key
				+ SQL_COMMA;
		return getValue(selection, null, ctxprovider);
	}

	public static List<TMDatas> getTMDataByValue(String text,
			ContentProvider ctxprovider) {
		String selection = TMDatas.TM_TEXT + SQL_CONNECTOR_COMMA + text
				+ SQL_COMMA;
		return getValue(selection, null, ctxprovider);
	}

	public static TMDatas getTMDataByID(int id, ContentProvider ctxprovider) {
		String selection = TMDatas._ID + SQL_CONNECTOR + id;
		List<TMDatas> tmdatalist = getValue(selection, null, ctxprovider);
		if (tmdatalist.size() == 1) {
			return tmdatalist.get(0);
		}
		return null;
	}

	public static List<TMDatas> getAllTMDatas(ContentProvider ctxprovider) {
		return getValue(null, null, ctxprovider);
	}

	public static void deleteByText(String text, ContentProvider ctxprovider) {
		String selection = TMDatas.TM_TEXT + SQL_CONNECTOR_COMMA + text
				+ SQL_COMMA;
		deleteValue(selection, null, ctxprovider);
	}

	public static void deleteByKey(String key, ContentProvider ctxprovider) {
		String selection = TMDatas.TM_KEY + SQL_CONNECTOR_COMMA + key
				+ SQL_COMMA;
		deleteValue(selection, null, ctxprovider);
	}

	public static void deleteByID(int id, ContentProvider ctxprovider) {
		String selection = TMDatas._ID + SQL_CONNECTOR + id;
		deleteValue(selection, null, ctxprovider);
	}

	public static void deleteAll(ContentProvider ctxprovider) {
		String selection = TMDatas._ID + " > -1";
		deleteValue(selection, null, ctxprovider);
	}

	private static int updateValue(ContentValues contentvalues,
			String selection, String[] selectionArgs,
			ContentProvider ctxprovider) {
		if(!isProviderExist(ctxprovider)) {
			return 0;
		}
		return ctxprovider.update(TMDatas.CONTENT_URI, contentvalues,
				selection, selectionArgs);
	}

	private static void deleteValue(String selection, String[] selectionArgs,
			ContentProvider ctxprovider) {
		if(!isProviderExist(ctxprovider)) {
			return;
		}
		ctxprovider.delete(TMDatas.CONTENT_URI, selection, selectionArgs);
	}

	private static List<TMDatas> getValue(String selection,
			String[] selectionArgs, ContentProvider ctxprovider) {
		List<TMDatas> tmdatalist = new ArrayList<TMDatas>();
		if(!isProviderExist(ctxprovider)) {
			return tmdatalist;
		}
		Cursor cur = ctxprovider.query(TMDatas.CONTENT_URI, TMDATA_COLUMNS,
				selection, selectionArgs, null);
		if (cur != null && cur.moveToFirst()) {
			do {
				TMDatas tmdata = new TMDatas(cur.getInt(cur
						.getColumnIndex(TMDatas._ID)), cur.getString(cur
						.getColumnIndex(TMDatas.TM_KEY)), cur.getString(cur
						.getColumnIndex(TMDatas.TM_TEXT)), cur.getInt(cur
						.getColumnIndex(TMDatas.TM_NUMBER)));
				tmdatalist.add(tmdata);
				Log.d("tag", "TMDatas._ID=" + tmdata.getId()
						+ " TMDatas.TM_KEY=" + tmdata.getKey()
						+ " TMDatas.TM_TEXT" + tmdata.getText());
			} while (cur.moveToNext());
		}
		return tmdatalist;
	}
	
	/* check if the provider/table exist */
	public static boolean isProviderExist(ContentProvider ctxprovider) {
		Cursor cur = ctxprovider.query(TMDatas.CONTENT_URI, TMDATA_COLUMNS, null,
				null, null);
		if (cur == null) {
			return false;
		} else {
			cur.close();
			return true;
		}
	}
}
