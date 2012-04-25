package com.yfvesh.tm.utility;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public final class TMDataProviderHelper {

	private static final String TAG = "TMDataProviderHelperTag";
	private static final String SQL_CONNECTOR = "= ";
	private static final String SQL_CONNECTOR_COMMA = "= '";
	private static final String SQL_COMMA = "'";

	private TMDataProviderHelper() {
	}

	private static final String[] TMDATA_COLUMNS = new String[] { TMDatas._ID,
			TMDatas.TM_KEY, TMDatas.TM_TEXT, TMDatas.TM_NUMBER };

	/* returns null when failed, otherwise return the url of the insert item */
	public static Uri insertTMData(String key, String txt,
			ContentResolver ctxprslvr) {
		if (!isProviderExist(ctxprslvr)) {
			return null;
		}
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(TMDatas.TM_KEY, key);
		contentvalues.put(TMDatas.TM_TEXT, txt);
		Uri uri = ctxprslvr.insert(TMDatas.CONTENT_URI, contentvalues);
		Log.d(TAG, "insertTMData return uri:" + uri);
		return uri;
	}

	public static void updateTMDataByKey(String key, String txt,
			ContentResolver ctxprslvr) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(TMDatas.TM_KEY, key);
		contentvalues.put(TMDatas.TM_TEXT, txt);
		if (isKeyExist(key, ctxprslvr)) {
			updateValue(contentvalues, null, null, ctxprslvr);
		} else {
			Log.d(TAG, "updateTMData not exit,insert it instead");
			insertTMData(key, txt, ctxprslvr);
		}
	}

	public static void updateTMDataById(int id, String key, String txt,
			ContentResolver ctxprslvr) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(TMDatas.TM_KEY, key);
		contentvalues.put(TMDatas.TM_TEXT, txt);
		String selection = TMDatas._ID + SQL_CONNECTOR + id;
		int count = updateValue(contentvalues, selection, null, ctxprslvr);
		if (count == 0) {
			Log.d(TAG, "failed to find the id, and abondad to create new item ");
		}
	}

	public static List<TMDatas> getTMDataByKey(String key,
			ContentResolver ctxprslvr) {
		String selection = TMDatas.TM_KEY + SQL_CONNECTOR_COMMA + key
				+ SQL_COMMA;
		return getValue(selection, null, ctxprslvr);
	}

	public static List<TMDatas> getTMDataByValue(String text,
			ContentResolver ctxprslvr) {
		String selection = TMDatas.TM_TEXT + SQL_CONNECTOR_COMMA + text
				+ SQL_COMMA;
		return getValue(selection, null, ctxprslvr);
	}

	public static TMDatas getTMDataByID(int id, ContentResolver ctxprslvr) {
		String selection = TMDatas._ID + SQL_CONNECTOR + id;
		List<TMDatas> tmdatalist = getValue(selection, null, ctxprslvr);
		if (tmdatalist.size() == 1) {
			return tmdatalist.get(0);
		}
		return null;
	}

	public static List<TMDatas> getAllTMDatas(ContentResolver ctxprslvr) {
		return getValue(null, null, ctxprslvr);
	}

	public static void deleteByText(String text, ContentResolver ctxprslvr) {
		String selection = TMDatas.TM_TEXT + SQL_CONNECTOR_COMMA + text
				+ SQL_COMMA;
		deleteValue(selection, null, ctxprslvr);
	}

	public static void deleteByKey(String key, ContentResolver ctxprslvr) {
		String selection = TMDatas.TM_KEY + SQL_CONNECTOR_COMMA + key
				+ SQL_COMMA;
		deleteValue(selection, null, ctxprslvr);
	}

	public static void deleteByID(int id, ContentResolver ctxprslvr) {
		String selection = TMDatas._ID + SQL_CONNECTOR + id;
		deleteValue(selection, null, ctxprslvr);
	}

	public static void deleteAll(ContentResolver ctxprslvr) {
		String selection = TMDatas._ID + " > -1";
		deleteValue(selection, null, ctxprslvr);
	}

	private static int updateValue(ContentValues contentvalues,
			String selection, String[] selectionArgs, ContentResolver ctxprslvr) {
		if (!isProviderExist(ctxprslvr)) {
			return 0;
		}
		return ctxprslvr.update(TMDatas.CONTENT_URI, contentvalues, selection,
				selectionArgs);
	}

	private static void deleteValue(String selection, String[] selectionArgs,
			ContentResolver ctxprslvr) {
		if (!isProviderExist(ctxprslvr)) {
			return;
		}
		ctxprslvr.delete(TMDatas.CONTENT_URI, selection, selectionArgs);
	}

	private static boolean isKeyExist(String key, ContentResolver ctxprslvr) {
		boolean bresult = false;
		String selection = TMDatas.TM_KEY + SQL_CONNECTOR_COMMA + key
				+ SQL_COMMA;
		List<TMDatas> tmdatalist = getValue(selection, null, ctxprslvr);
		bresult = (tmdatalist.size() > 0) ? true : false;
		return bresult;
	}

	private static List<TMDatas> getValue(String selection,
			String[] selectionArgs, ContentResolver ctxprslvr) {
		List<TMDatas> tmdatalist = new ArrayList<TMDatas>();

		if (!isProviderExist(ctxprslvr)) {
			return tmdatalist;
		}

		Cursor cur = ctxprslvr.query(TMDatas.CONTENT_URI, TMDATA_COLUMNS,
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
		if(cur!=null) {
			cur.close();
		}
		return tmdatalist;
	}

	/* check if the provider/table exist */
	public static boolean isProviderExist(ContentResolver ctxprslvr) {
		Cursor cur = ctxprslvr.query(TMDatas.CONTENT_URI, TMDATA_COLUMNS, null,
				null, null);
		if (cur == null) {
			return false;
		} else {
			cur.close();
			return true;
		}
	}
}
