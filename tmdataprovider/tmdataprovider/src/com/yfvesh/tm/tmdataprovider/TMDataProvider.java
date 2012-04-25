package com.yfvesh.tm.tmdataprovider;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class TMDataProvider extends ContentProvider {

	/* log tag */
	private static final String TAG = "TMDataProviderTag";

	private static final String DATABASE_NAME = "yftmdata.db";

	private static final int DATABASE_VERSION = 1;

	private static final String DATA_TABLE_NAME = "tmdatas";

	public static final String AUTHORITY = "com.yfvesh.tm.tmdataprovider";

	private static final int TM_DATA_ITEMS = 1;
	private static final int TM_DATA_ITEM = 2;

	private static Map<String, String> dataProjectionMap = new HashMap<String, String>();
	private static final UriMatcher URIMATCHER = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		URIMATCHER.addURI(AUTHORITY, "tmdatas", TM_DATA_ITEMS);
		URIMATCHER.addURI(AUTHORITY, "tmdatas/#", TM_DATA_ITEM);

		dataProjectionMap.put(TMDatas._ID, TMDatas._ID);
		dataProjectionMap.put(TMDatas.TM_KEY, TMDatas.TM_KEY);
		dataProjectionMap.put(TMDatas.TM_TEXT, TMDatas.TM_TEXT);
		dataProjectionMap.put(TMDatas.TM_NUMBER, TMDatas.TM_NUMBER);
	}

	private DatabaseHelper mDBHelper;

	@Override
	public boolean onCreate() {
		mDBHelper = new DatabaseHelper(this.getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		String str = null;
		switch (URIMATCHER.match(uri)) {
		case TM_DATA_ITEMS: {
			str = TMDatas.CONTENT_TYPE_ITEMS;
			break;
		}
		case TM_DATA_ITEM: {
			str = TMDatas.CONTENT_TYPE_ITEM;
			break;
		}
		default:
			break;
		}
		return str;
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int count = 0;
		switch (URIMATCHER.match(uri)) {
		case TM_DATA_ITEMS: {
			count = db.delete(DATA_TABLE_NAME, where, whereArgs);
			break;
		}
		case TM_DATA_ITEM: {
			String itemid = uri.getPathSegments().get(1);
			count = db.delete(DATA_TABLE_NAME, TMDatas._ID
					+ "="
					+ itemid
					+ (!TextUtils.isEmpty(where) ? (" AND (" + where + ")")
							: ""), whereArgs);
			break;
		}
		default: {
			throw new IllegalArgumentException("Unknown Uri +" + uri);
		}
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (URIMATCHER.match(uri) != TM_DATA_ITEMS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		ContentValues values = (initialValues != null ? new ContentValues(
				initialValues) : new ContentValues());
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		long rowId = db.insert(DATA_TABLE_NAME, TMDatas.TM_KEY, values);
		if (rowId > 0) {
			Uri datauri = ContentUris
					.withAppendedId(TMDatas.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(datauri, null);
			return datauri;
		}
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URIMATCHER.match(uri)) {
		case TM_DATA_ITEMS: {
			qb.setTables(DATA_TABLE_NAME);
			qb.setProjectionMap(dataProjectionMap);
			break;
		}
		case TM_DATA_ITEM: {
			qb.setTables(DATA_TABLE_NAME);
			qb.setProjectionMap(dataProjectionMap);
			qb.appendWhere(TMDatas._ID + "=" + uri.getPathSegments().get(1));
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		int count;
		switch (URIMATCHER.match(uri)) {
		case TM_DATA_ITEMS: {
			count = db
					.update(DATA_TABLE_NAME, values, selection, selectionArgs);
			break;
		}
		case TM_DATA_ITEM: {
			String itemid = uri.getPathSegments().get(1);
			count = db.update(DATA_TABLE_NAME, values,
					TMDatas._ID
							+ "="
							+ itemid
							+ (!TextUtils.isEmpty(selection) ? (" AND ("
									+ selection + ")") : ""), selectionArgs);
			break;
		}
		default: {
			throw new IllegalArgumentException("Unknown Uri +" + uri);
		}
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE_NAME);
			db.execSQL("CREATE TABLE " + DATA_TABLE_NAME + " (" + TMDatas._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TMDatas.TM_KEY
					+ " TEXT," + TMDatas.TM_TEXT + " TEXT," + TMDatas.TM_NUMBER
					+ " INTEGER" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, "onUpgrade from oldVersion:" + oldVersion
					+ " to newVersion:" + newVersion);
			onCreate(db);
		}
	}

}