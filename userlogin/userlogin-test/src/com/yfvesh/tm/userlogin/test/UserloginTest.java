package com.yfvesh.tm.userlogin.test;

import com.yfvesh.tm.userlogin.UserloginActivity;

import junit.framework.TestCase;

public class UserloginTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testTrimEndChar1() {
		String str = "abc";
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="ab";
		assertEquals(expstr,result);
	}
	
	public void testTrimEndChar2() {
		String str = "123";
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="12";
		assertEquals(expstr,result);
	}
	
	public void testTrimEndChar3() {
		String str = "#$%";
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="#$";
		assertEquals(expstr,result);
	}
	
	public void testTrimEndChar11() {
		String str = "a";
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimEndChar21() {
		String str = "1";
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimEndChar31() {
		String str = "#%";
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="#";
		assertEquals(expstr,result);
	}
	
	public void testTrimEndChar4() {
		String str = "";
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimEndChar5() {
		String str = null;
		String result = UserloginActivity.trimEndChar(str);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex1() {
		String str = "abc";
		String result = UserloginActivity.trimCharBeforeIndex(str,0);
		String expstr ="abc";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex11() {
		String str = "abc";
		String result = UserloginActivity.trimCharBeforeIndex(str,1);
		String expstr ="bc";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex12() {
		String str = "abc";
		String result = UserloginActivity.trimCharBeforeIndex(str,2);
		String expstr ="ac";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex13() {
		String str = "abc";
		String result = UserloginActivity.trimCharBeforeIndex(str,3);
		String expstr ="ab";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex14() {
		String str = "abc";
		String result = UserloginActivity.trimCharBeforeIndex(str,4);
		String expstr ="abc";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex15() {
		String str = "abc";
		String result = UserloginActivity.trimCharBeforeIndex(str,-1);
		String expstr ="abc";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex5() {
		String str = "";
		String result = UserloginActivity.trimCharBeforeIndex(str,0);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex51() {
		String str = "";
		String result = UserloginActivity.trimCharBeforeIndex(str,-1);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex52() {
		String str = "";
		String result = UserloginActivity.trimCharBeforeIndex(str,1);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex53() {
		String str = "";
		String result = UserloginActivity.trimCharBeforeIndex(str,2);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex6() {
		String str = null;
		String result = UserloginActivity.trimCharBeforeIndex(str,0);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex61() {
		String str = null;
		String result = UserloginActivity.trimCharBeforeIndex(str,-1);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex62() {
		String str = null;
		String result = UserloginActivity.trimCharBeforeIndex(str,1);
		String expstr ="";
		assertEquals(expstr,result);
	}
	
	public void testTrimCharBeforeIndex63() {
		String str = null;
		String result = UserloginActivity.trimCharBeforeIndex(str,2);
		String expstr ="";
		assertEquals(expstr,result);
	}
}
