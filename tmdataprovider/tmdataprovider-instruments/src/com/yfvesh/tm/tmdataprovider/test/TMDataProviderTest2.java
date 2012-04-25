package com.yfvesh.tm.tmdataprovider.test;

import java.util.List;

import com.yfvesh.tm.tmdataprovider.TMDataProvider;

import android.content.ContentProvider;
import android.net.Uri;
import android.test.ProviderTestCase2;

public class TMDataProviderTest2 extends ProviderTestCase2<TMDataProvider> {

	private ContentProvider mProvider;

	public TMDataProviderTest2() {
		super(TMDataProvider.class, TMDataProvider.AUTHORITY);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		super.setUp();
		mProvider = getProvider();
	}

	public void testAddValue1() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.insertTMData(key, txt, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() > 0);
		assertEquals(txt, tmdatalist.get(0).getText());
	}

	/* when insert two item with the same key, shall generate two records */
	public void testAddValue2() {
		String key = "key_1";
		String txt1 = "txt_1";
		String txt2 = "txt_2";
		TMDataProviderHelper2.insertTMData(key, txt1, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() > 0);
		assertEquals(txt1, tmdatalist.get(0).getText());

		TMDataProviderHelper2.insertTMData(key, txt2, mProvider);
		tmdatalist = TMDataProviderHelper2.getTMDataByKey(key, mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() == 2);
		assertEquals(txt2, tmdatalist.get(1).getText());
	}

	public void testGetValue11() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.insertTMData(key, txt, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() > 0);
		assertEquals(txt, tmdatalist.get(0).getText());
	}

	public void testGetValue12() {
		String key = "key_1";
		String txt = "txt_1";
		Uri uri = TMDataProviderHelper2.insertTMData(key, txt, mProvider);

		String itemid = uri.getPathSegments().get(1);
		int id = Integer.parseInt(itemid);

		TMDatas tmdatas = TMDataProviderHelper2.getTMDataByID(id, mProvider);
		assertEquals(key, tmdatas.getKey());
		assertEquals(txt, tmdatas.getText());
	}

	public void testGetValue31() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.insertTMData(key, txt, mProvider);

		List<TMDatas> tmdatalist = TMDataProviderHelper2
				.getAllTMDatas(mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() == 1);
		assertEquals(txt, tmdatalist.get(0).getText());
	}

	public void testGetValue32() {
		String key1 = "key_1";
		String txt1 = "txt_1";
		TMDataProviderHelper2.insertTMData(key1, txt1, mProvider);

		String key2 = "key_2";
		String txt2 = "txt_2";
		TMDataProviderHelper2.insertTMData(key2, txt2, mProvider);

		List<TMDatas> tmdatalist = TMDataProviderHelper2
				.getAllTMDatas(mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() == 2);
		assertEquals(key1, tmdatalist.get(0).getKey());
		assertEquals(txt1, tmdatalist.get(0).getText());
		assertEquals(key2, tmdatalist.get(1).getKey());
		assertEquals(txt2, tmdatalist.get(1).getText());
	}

	/* update key/value, if no exist shall generate a new record */
	public void testUpdateValue11() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.updateTMDataByKey(key, txt, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() > 0);
		assertEquals(txt, tmdatalist.get(0).getText());
	}

	public void testUpdateValue12() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.insertTMData(key, txt, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() == 1);
		assertEquals(txt, tmdatalist.get(0).getText());

		txt = "txt_2";
		TMDataProviderHelper2.updateTMDataByKey(key, txt, mProvider);
		tmdatalist = TMDataProviderHelper2.getTMDataByKey(key, mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() == 1);
		assertEquals(txt, tmdatalist.get(0).getText());
	}

	public void testUpdateValue13() {
		String key = "key_1";
		String txt1 = "txt_1";
		String txt2 = "txt_2";
		TMDataProviderHelper2.insertTMData(key, txt1, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() > 0);
		assertEquals(txt1, tmdatalist.get(0).getText());

		TMDataProviderHelper2.insertTMData(key, txt2, mProvider);
		tmdatalist = TMDataProviderHelper2.getTMDataByKey(key, mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() == 2);
		assertEquals(txt2, tmdatalist.get(1).getText());

		String txt = "txt_3";
		TMDataProviderHelper2.updateTMDataByKey(key, txt, mProvider);
		tmdatalist = TMDataProviderHelper2.getTMDataByKey(key, mProvider);
		assertNotNull(tmdatalist);
		assertTrue(tmdatalist.size() == 2);
		assertEquals(txt, tmdatalist.get(0).getText());
		assertEquals(txt, tmdatalist.get(1).getText());
	}

	/* update by an non exist id shall not create new record */
	public void testUpdateValue21() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.updateTMDataById(1, key, txt, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertEquals(0, tmdatalist.size());
	}

	public void testUpdateValue22() {
		String key = "key_1";
		String txt = "txt_1";
		Uri uri = TMDataProviderHelper2.insertTMData(key, txt, mProvider);

		String itemid = uri.getPathSegments().get(1);
		int id = Integer.parseInt(itemid);
		TMDatas tmdatas = TMDataProviderHelper2.getTMDataByID(id, mProvider);
		assertEquals(key, tmdatas.getKey());
		assertEquals(txt, tmdatas.getText());

		key = "key_22";
		txt = "txt_22";
		TMDataProviderHelper2.updateTMDataById(id, key, txt, mProvider);

		tmdatas = TMDataProviderHelper2.getTMDataByID(id, mProvider);
		assertEquals(key, tmdatas.getKey());
		assertEquals(txt, tmdatas.getText());
	}

	public void testUpdateValue23() {
		String key = "key_1";
		String txt1 = "txt_23";
		Uri uri = TMDataProviderHelper2.insertTMData(key, txt1, mProvider);

		String itemid1 = uri.getPathSegments().get(1);
		int id1 = Integer.parseInt(itemid1);
		TMDatas tmdatas1 = TMDataProviderHelper2.getTMDataByID(id1, mProvider);
		assertEquals(key, tmdatas1.getKey());
		assertEquals(txt1, tmdatas1.getText());

		String txt2 = "txt_23";
		uri = TMDataProviderHelper2.insertTMData(key, txt2, mProvider);
		String itemid2 = uri.getPathSegments().get(1);
		int id2 = Integer.parseInt(itemid2);
		TMDatas tmdatas2 = TMDataProviderHelper2.getTMDataByID(id2, mProvider);
		assertEquals(key, tmdatas2.getKey());
		assertEquals(txt2, tmdatas2.getText());
		assertEquals(tmdatas1.getKey(), tmdatas2.getKey());

		txt1 = "txt_23_1";
		txt2 = "txt_23_2";
		TMDataProviderHelper2.updateTMDataById(id1, key, txt1, mProvider);
		TMDataProviderHelper2.updateTMDataById(id2, key, txt2, mProvider);

		tmdatas1 = TMDataProviderHelper2.getTMDataByID(id1, mProvider);
		tmdatas2 = TMDataProviderHelper2.getTMDataByID(id2, mProvider);
		assertEquals(key, tmdatas1.getKey());
		assertEquals(key, tmdatas2.getKey());
		assertEquals(tmdatas1.getKey(), tmdatas2.getKey());

		assertEquals(txt1, tmdatas1.getText());
		assertEquals(txt2, tmdatas2.getText());
		assertFalse(tmdatas1.getText().equals(tmdatas2.getText()));
	}

	public void testdeleteValue10() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.insertTMData(key, txt, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertEquals(1, tmdatalist.size());
		assertEquals(key, tmdatalist.get(0).getKey());
		assertEquals(txt, tmdatalist.get(0).getText());

		TMDataProviderHelper2.deleteByKey(key, mProvider);
		tmdatalist = TMDataProviderHelper2.getTMDataByKey(key, mProvider);
		assertEquals(0, tmdatalist.size());
	}

	public void testdeleteValue11() {
		String key1 = "key_1";
		String txt1 = "txt_1";
		TMDataProviderHelper2.insertTMData(key1, txt1, mProvider);
		List<TMDatas> tmdatalist1 = TMDataProviderHelper2.getTMDataByKey(key1,
				mProvider);
		assertEquals(1, tmdatalist1.size());
		assertEquals(key1, tmdatalist1.get(0).getKey());
		assertEquals(txt1, tmdatalist1.get(0).getText());

		String key2 = key1;
		String txt2 = "txt_2";
		TMDataProviderHelper2.insertTMData(key2, txt2, mProvider);
		List<TMDatas> tmdatalist2 = TMDataProviderHelper2.getTMDataByKey(key2,
				mProvider);
		assertEquals(2, tmdatalist2.size());
		assertEquals(key2, tmdatalist2.get(1).getKey());
		assertEquals(txt2, tmdatalist2.get(1).getText());

		TMDataProviderHelper2.deleteByKey(key1, mProvider);
		tmdatalist2 = TMDataProviderHelper2.getTMDataByKey(key1, mProvider);
		assertEquals(0, tmdatalist2.size());
	}

	public void testdeleteValue20() {
		String key = "key_1";
		String txt = "txt_1";
		TMDataProviderHelper2.insertTMData(key, txt, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2.getTMDataByKey(key,
				mProvider);
		assertEquals(1, tmdatalist.size());
		assertEquals(key, tmdatalist.get(0).getKey());
		assertEquals(txt, tmdatalist.get(0).getText());

		TMDataProviderHelper2.deleteByText(txt, mProvider);
		tmdatalist = TMDataProviderHelper2.getTMDataByKey(key, mProvider);
		assertEquals(0, tmdatalist.size());
	}

	public void testdeleteValue21() {
		String key1 = "key_1";
		String txt1 = "txt_1";
		TMDataProviderHelper2.insertTMData(key1, txt1, mProvider);
		List<TMDatas> tmdatalist1 = TMDataProviderHelper2.getTMDataByKey(key1,
				mProvider);
		assertEquals(1, tmdatalist1.size());
		assertEquals(key1, tmdatalist1.get(0).getKey());
		assertEquals(txt1, tmdatalist1.get(0).getText());

		String key2 = "key_2";
		String txt2 = txt1;
		TMDataProviderHelper2.insertTMData(key2, txt2, mProvider);
		tmdatalist1 = TMDataProviderHelper2.getTMDataByValue(txt2, mProvider);
		assertEquals(2, tmdatalist1.size());
		assertEquals(key2, tmdatalist1.get(1).getKey());
		assertEquals(txt2, tmdatalist1.get(1).getText());

		TMDataProviderHelper2.deleteByText(txt1, mProvider);
		tmdatalist1 = TMDataProviderHelper2.getAllTMDatas(mProvider);
		assertEquals(0, tmdatalist1.size());
	}

	public void testdeleteValue30() {
		String key = "key_1";
		String txt = "txt_1";

		Uri uri = TMDataProviderHelper2.insertTMData(key, txt, mProvider);
		String itemid1 = uri.getPathSegments().get(1);
		int id1 = Integer.parseInt(itemid1);

		TMDatas tmdatas1 = TMDataProviderHelper2.getTMDataByID(id1, mProvider);
		assertEquals(key, tmdatas1.getKey());
		assertEquals(txt, tmdatas1.getText());

		TMDataProviderHelper2.deleteByID(id1, mProvider);
		tmdatas1 = TMDataProviderHelper2.getTMDataByID(id1, mProvider);
		assertNull(tmdatas1);
	}

	public void testdeleteValue31() {
		String key1 = "key_1";
		String txt1 = "txt_1";

		Uri uri1 = TMDataProviderHelper2.insertTMData(key1, txt1, mProvider);
		String itemid1 = uri1.getPathSegments().get(1);
		int id1 = Integer.parseInt(itemid1);

		String key2 = "key_2";
		String txt2 = "txt_2";

		Uri uri2 = TMDataProviderHelper2.insertTMData(key2, txt2, mProvider);
		String itemid2 = uri2.getPathSegments().get(1);
		int id2 = Integer.parseInt(itemid2);

		TMDatas tmdatas1 = TMDataProviderHelper2.getTMDataByID(id1, mProvider);
		assertEquals(key1, tmdatas1.getKey());
		assertEquals(txt1, tmdatas1.getText());
		TMDatas tmdatas2 = TMDataProviderHelper2.getTMDataByID(id2, mProvider);
		assertEquals(key2, tmdatas2.getKey());
		assertEquals(txt2, tmdatas2.getText());

		TMDataProviderHelper2.deleteByID(id1, mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2
				.getAllTMDatas(mProvider);
		assertEquals(1, tmdatalist.size());
		assertEquals(key2, tmdatalist.get(0).getKey());
		assertEquals(txt2, tmdatalist.get(0).getText());
	}

	public void testdeleteValue40() {
		String key1 = "key_1";
		String txt1 = "txt_1";

		Uri uri1 = TMDataProviderHelper2.insertTMData(key1, txt1, mProvider);
		String itemid1 = uri1.getPathSegments().get(1);
		int id1 = Integer.parseInt(itemid1);

		String key2 = "key_2";
		String txt2 = "txt_2";

		Uri uri2 = TMDataProviderHelper2.insertTMData(key2, txt2, mProvider);
		String itemid2 = uri2.getPathSegments().get(1);
		int id2 = Integer.parseInt(itemid2);

		TMDatas tmdatas1 = TMDataProviderHelper2.getTMDataByID(id1, mProvider);
		assertEquals(key1, tmdatas1.getKey());
		assertEquals(txt1, tmdatas1.getText());
		TMDatas tmdatas2 = TMDataProviderHelper2.getTMDataByID(id2, mProvider);
		assertEquals(key2, tmdatas2.getKey());
		assertEquals(txt2, tmdatas2.getText());

		TMDataProviderHelper2.deleteAll(mProvider);
		List<TMDatas> tmdatalist = TMDataProviderHelper2
				.getAllTMDatas(mProvider);
		assertEquals(0, tmdatalist.size());
	}
}
