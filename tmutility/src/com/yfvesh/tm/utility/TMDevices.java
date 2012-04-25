package com.yfvesh.tm.utility;

import android.content.Context;
import android.telephony.TelephonyManager;

public class TMDevices {
	private static String MSISDN_NUM = "";
	public static String getSimNum(Context ctx) {
		if(MSISDN_NUM != null || MSISDN_NUM.length()>0) {
			return MSISDN_NUM;
		}
		if(ctx != null) {
			TelephonyManager mTelMgr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
			String msisdn = mTelMgr.getLine1Number(); 
			if( msisdn != null && msisdn.length()>0) {
				MSISDN_NUM = msisdn;
			} else {
				MSISDN_NUM ="";
			}
		}
		return MSISDN_NUM;
	}
}
