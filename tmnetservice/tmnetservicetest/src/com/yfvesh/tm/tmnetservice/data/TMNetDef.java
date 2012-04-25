package com.yfvesh.tm.tmnetservice.data;

public final class TMNetDef {
	/* error defines for TM net result */
	public static final int TM_SUCCESS = 0;
	public static final int TM_ERR_REQ_NOT_DONE = 1;
	public static final int TM_ERR_SERVICE_NOT_BIND = 2;
	public static final int TM_ERR_REMOTE_EXCEPTION = 3;
	public static final int TM_ERR_NET_OFF = -1;
	public static final int TM_ERR_USER_NOT_LOGIN = 1001;
	public static final int TM_ERR_USER_LOGIN_FAILED = 1002;
	public static final int TM_ERR_USER_LOGOFF_FAILED = 1003;
	public static final int TM_ERR_USER_LOGIN_DUPLICATE = 1006;
	public static final int TM_ERR_USER_LOGIN_EXIST = 1008;
	public static final int TM_ERR_UPLOAD_FAILED = 1004;
	public static final int TM_ERR_UPLOAD_RESERVED = 1005;
	public static final int TM_ERR_UPLOAD_INVALID = 1007;
	public static final int TM_ERR_DEVICE_UNREG = 1009;
	
	/* type for request different kind of remote request */
	public static final int TM_REQ_TYPE_MIN = 100;
	
	public static final int TM_REQ_TYPE_USERLOGIN = 101;
	public static final int TM_REQ_TYPE_GPS = 102;
	public static final int TM_REQ_TYPE_TRAFFIC = 103;
	public static final int TM_REQ_TYPE_OTHERS = 110;
	
	public static final int TM_PUSH_TYPE_START = 200;
	public static final int TM_PUSH_TYPE_FILE = 201;
	public static final int TM_PUSH_TYPE_MSG = 202;
	public static final int TM_PUSH_TYPE_DEST = 203;
	public static final int TM_PUSH_TYPE_WEATHER = 204;
	public static final int TM_REQ_TYPE_MAX = 211;

	/* message detail type for remote user login/logout */
	public static final int TM_REQ_TYPE_USERLOGIN_IN = 1001;
	public static final int TM_REQ_TYPE_USERLOGIN_OFF = 1002;
	public static final int TM_REQ_TYPE_GPS_UPLOAD = 1011;
	public static final int TM_REQ_TYPE_VEHICLE_STATUS = 1021;
	public static final int TM_REQ_TYPE_VEHICLE_DATA = 1031;
	public static final int TM_REQ_TYPE_TMC_STATUS = 1041;
	public static final int TM_REQ_TYPE_WEATHER = 1050;
	public static final int TM_REQ_TYPE_WEATHER_LOC = 1051;
	public static final int TM_REQ_TYPE_WEATHER_CITY = 1052;
	
	/* the transition id base for each module */
	public static final int TM_REQ_ID_GPS_BASE = 0x1FFFFFF;
	public static final int TM_REQ_ID_TMC_BASE = 0x2FFFFFF;
	public static final int TM_REQ_ID_USRLOGIN_BASE = 0x3FFFFFF;
	public static final int TM_REQ_ID_VEHICLE_DATA_BASE = 0x4FFFFFF;
	public static final int TM_REQ_ID_VEHICLE_STATUS_BASE = 0x5FFFFFF;
	public static final int TM_REQ_ID_WEATHER_BASE = 0x6FFFFFF;
	

	/* action string for broad cast results,register filter for broadcast results */
	public static final String TM_REQ_RESULT_ACTION_USERLOGIN = "com.yfvesh.tm.hminetservice.req.userlogin";
	public static final String TM_REQ_RESULT_ACTION_GPS = "com.yfvesh.tm.hminetservice.req.gps";
	public static final String TM_REQ_RESULT_ACTION_TRAFFIC = "com.yfvesh.tm.hminetservice.req.traffic";
	public static final String TM_REQ_RESULT_ACTION_OTHERS = "com.yfvesh.tm.hminetservice.req.others";
	
	public static final String TM_PUSH_RESULT_ACTION_FILE = "com.yfvesh.tm.hminetservice.push.file";
	public static final String TM_PUSH_RESULT_ACTION_MSG = "com.yfvesh.tm.hminetservice.push.msg";
	public static final String TM_PUSH_RESULT_ACTION_DEST = "com.yfvesh.tm.hminetservice.push.dest";
	public static final String TM_PUSH_RESULT_ACTION_WEATHER = "com.yfvesh.tm.hminetservice.push.weather";
	/*
	 * the key for passing net request parameters, client shall get interest
	 * data from bundle by the keys
	 */
	/* key for the req id */
	public static final String TAG_TM_REQ_ID = "req_id";
	/* status of this request, exist if this is a intuitive request */
	public static final String TAG_TM_REQ_STATUS = "req_status";
	/* the detail type, e.g login or logout, exist if this is a intuitive request */
	public static final String TAG_TM_REQ_DETAIL_TYPE = "req_detail_type";
	/* the user named used to login or log off */
	public static final String TAG_TM_REQ_USER_NAME = "user_name";
	/* the password used to login */
	public static final String TAG_TM_REQ_USER_PW = "user_pw";
	/* the SIM card number used for net request */
	public static final String TAG_TM_REQ_SIMCARD_NUM = "simcard_num";
	/* the location of GPS data for upload */
	public static final String TAG_TM_REQ_GPS_TMLOCATION = "gps_tmlocation";
	/* the vehicle working status for upload */
	public static final String TAG_TM_REQ_VEHICLE_STATUS = "vehicle_status";
	/* the vehicle data from can for upload */
	public static final String TAG_TM_REQ_VEHICLE_DATA= "vehicle_data";
	/* the city code for weather request */
	public static final String TAG_TM_REQ_CITY_CODE= "city_code";
	/* the longitude for weather request */
	public static final String TAG_TM_REQ_LOC_LONGT= "loc_longt";
	/* the latitude for weather request */
	public static final String TAG_TM_REQ_LOC_LAT= "loc_lat";

	
	/* the result type of pushed result */
	public static final String TAG_TM_PUSH_RESULT = "push_result";
	
	/* the file type of pushed files */
	public static final String TAG_TM_PUSH_FILE_TYPE = "push_file_type";
	public static final int TM_PUSH_FILE_TYPE_SONG = 0;
	public static final int TM_PUSH_FILE_TYPE_VIDEO = 1;
	public static final int TM_PUSH_FILE_TYPE_FLASH = 2;
	/* the file path of pushed files */
	public static final String TAG_TM_PUSH_FILE_PATH = "push_file_path";
	
	/* the remote push message */
	public static final String TAG_TM_PUSH_MSG_CONTENT = "push_msg_content";
	
	/* the remote push weather */
	public static final String TAG_TM_PUSH_WEATHER_CONTENT = "push_weather_content";
	/* the local get weather */
	public static final String TAG_TM_REQ_WEATHER_CONTENT = "req_weather_content";
	
	/* the remote push POI list */
	public static final String TAG_TM_PUSH_POI_LIST = "push_poi_list";
	public static final String TAG_TM_PUSH_POI_NUMS = "push_poi_nums";
	
	public static String mappingTypeToActionStr(int type) {
		String actionstr ="";
		switch(type) {
		case TM_REQ_TYPE_USERLOGIN:
			actionstr = TM_REQ_RESULT_ACTION_USERLOGIN;
			break;
		case TM_REQ_TYPE_GPS:
			actionstr = TM_REQ_RESULT_ACTION_GPS;
			break;
		case TM_REQ_TYPE_TRAFFIC:
			actionstr = TM_REQ_RESULT_ACTION_TRAFFIC;
			break;
		case TM_REQ_TYPE_OTHERS:
			actionstr = TM_REQ_RESULT_ACTION_OTHERS;
			break;
		case TM_PUSH_TYPE_FILE:
			actionstr = TM_PUSH_RESULT_ACTION_FILE;
			break;
		case TM_PUSH_TYPE_MSG:
			actionstr = TM_PUSH_RESULT_ACTION_MSG;
			break;
		case TM_PUSH_TYPE_DEST:
			actionstr = TM_PUSH_RESULT_ACTION_DEST;
			break;
		case TM_PUSH_TYPE_WEATHER:
			actionstr = TM_PUSH_RESULT_ACTION_WEATHER;
			break;
		default:
			break;
		}
		return actionstr;
	}
}
