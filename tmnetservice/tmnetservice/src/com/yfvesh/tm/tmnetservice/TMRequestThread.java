package com.yfvesh.tm.tmnetservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.supcon.vehiclenetwork.YZ_VehicleTransitImpl;
import com.supcon.vehiclenetwork.sturt.LocationSturt;
import com.supcon.vehiclenetwork.sturt.VehicleDataStruct;
import com.supcon.vehiclenetwork.sturt.WeatherSturt;
import com.yfvesh.tm.tmnetservice.data.TMLocation;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;
import com.yfvesh.tm.tmnetservice.data.TMVehicleData;
import com.yfvesh.tm.tmnetservice.data.TMWeather;

public class TMRequestThread extends Thread {

	private static final String TAG = TMRequestThread.class.getSimpleName();

	/* the supcon network request handle */
	private YZ_VehicleTransitImpl mTransitImpl;
	/* call back for supcon results */
	private INetResponse mNetRespLstnr;

	private MsgHandler mHandler;

	/*
	 * mReqType defined in TMNetDef.java, to split the request into different
	 * looper
	 */
	private int mReqType = -1;

	public TMRequestThread(int type) {
		mReqType = type;
	}

	public static TMRequestThread createTMReqThread(int type) {
		if ((type > TMNetDef.TM_REQ_TYPE_MIN)
				&& (type < TMNetDef.TM_REQ_TYPE_MAX)) {
			return new TMRequestThread(type);
		} else {
			return null;
		}
	}

	public synchronized void setTMTrasitImpl(YZ_VehicleTransitImpl trasitimpl) {
		mTransitImpl = trasitimpl;
	}

	public synchronized void setNetRespListner(INetResponse lstnr) {
		mNetRespLstnr = lstnr;
	}

	@Override
	public void run() {
		try {
			Looper.prepare();
			Log.d(TAG, "TMRequestThread start loop,mReqType = " + mReqType);
			mHandler = new MsgHandler();
			Looper.loop();
			Log.d(TAG, "TMRequestThread end loop,mReqType = " + mReqType);
		} catch (Throwable ex) {
			Log.e(TAG, "TMRequestThread halted due to an error", ex);
		}
	}

	public synchronized void stopThread() {
		if(mHandler != null) {
			Log.d(TAG, "TMRequestThread stopThread is called,mReqType = " + mReqType);
			mHandler.postAtFrontOfQueue(new Runnable() {
				@Override
				public void run() {
					Looper.myLooper().quit();
				}
			});
		}
		
	}

	public synchronized void addRequest(int type, Integer id) {
		/* only for test */
		if (TMNetDef.TM_REQ_TYPE_USERLOGIN == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_USERLOGIN_IN;
			msg.obj = id;
			mHandler.sendMessage(msg);
		}
	}

	public synchronized boolean addLoginRequest(int type, Integer id,
			String simnum, String username, String userpw) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_USERLOGIN == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_USERLOGIN_IN;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			bundle.putString(TMNetDef.TAG_TM_REQ_USER_NAME, username);
			bundle.putString(TMNetDef.TAG_TM_REQ_USER_PW, userpw);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized boolean addLogoffRequest(int type, Integer id,
			String simnum, String username) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_USERLOGIN == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_USERLOGIN_OFF;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			bundle.putString(TMNetDef.TAG_TM_REQ_USER_NAME, username);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized boolean addGpsUploadRequest(int type, Integer id,
			String simnum, TMLocation tmlocation) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_GPS == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_GPS_UPLOAD;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			bundle.putParcelable(TMNetDef.TAG_TM_REQ_GPS_TMLOCATION, tmlocation);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized boolean addVehicleStatusUploadRequest(int type,
			Integer id, String simnum, int status) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_OTHERS == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_VEHICLE_STATUS;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			bundle.putInt(TMNetDef.TAG_TM_REQ_VEHICLE_STATUS, status);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized boolean addTMCRequest(int type, Integer id,
			String simnum) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_TRAFFIC == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_TMC_STATUS;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized boolean addVehicleDataUploadRequest(int type,
			Integer id, String simnum, TMVehicleData vdata) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_OTHERS == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_VEHICLE_DATA;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			bundle.putParcelable(TMNetDef.TAG_TM_REQ_VEHICLE_DATA, vdata);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized boolean addWeatherByCityRequest(int type, Integer id,
			String simnum, String citycode) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_OTHERS == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_WEATHER_CITY;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			bundle.putString(TMNetDef.TAG_TM_REQ_CITY_CODE, citycode);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized boolean addWeatherByLocRequest(int type, Integer id,
			String simnum, double lat, double longt) {
		boolean result = false;
		if (TMNetDef.TM_REQ_TYPE_OTHERS == type && mHandler != null) {
			Message msg = new Message();
			msg.what = TMNetDef.TM_REQ_TYPE_WEATHER_LOC;
			msg.obj = id;
			Bundle bundle = new Bundle();
			bundle.putString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM, simnum);
			bundle.putDouble(TMNetDef.TAG_TM_REQ_LOC_LONGT, longt);
			bundle.putDouble(TMNetDef.TAG_TM_REQ_LOC_LAT, lat);
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			result = true;
		}
		return result;
	}

	public synchronized int removRequest(int type, Integer id) {
		int result = TMNetDef.TM_ERR_REQ_NOT_DONE;
		if (mHandler != null) {
			if (TMNetDef.TM_REQ_TYPE_USERLOGIN == type) {

				if (mHandler.hasMessages(TMNetDef.TM_REQ_TYPE_USERLOGIN_IN, id)) {
					Log.d(TAG, "remove msg TM_REQ_TYPE_USERLOGIN_IN,id=" + id);
					mHandler.removeMessages(TMNetDef.TM_REQ_TYPE_USERLOGIN_IN,
							id);
					result = TMNetDef.TM_SUCCESS;
				}
			}
		}
		return result;
	}

	public void setReqType(int type) {
		mReqType = type;
	}

	public int getReqType() {
		return mReqType;
	}

	private class MsgHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (mTransitImpl == null) {
				Log.d(TAG, "handleMessage abort for mTransitImpl is null");
				return;
			}

			switch (msg.what) {
			case TMNetDef.TM_REQ_TYPE_USERLOGIN_IN: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_USERLOGIN_IN, and id =" + id);
				if (bund != null) {
					if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_USER_NAME)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_USER_PW)) {
						String simnum = bund
								.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
						String username = bund
								.getString(TMNetDef.TAG_TM_REQ_USER_NAME);
						String userpw = bund
								.getString(TMNetDef.TAG_TM_REQ_USER_PW);
						Log.d(TAG, "call yz_2_userlogin with simnum:" + simnum
								+ " username: " + username + " userpw: "
								+ userpw);
						int yzresult = mTransitImpl.yz_2_userlogin(simnum,
								username, userpw);
						// send the request result;
						Log.d(TAG, "yz_2_userlogin returns :" + yzresult);
						if (mNetRespLstnr != null) {
							Bundle resbundle = new Bundle();
							resbundle.putString(TMNetDef.TAG_TM_REQ_USER_NAME,
									username);
							resbundle.putString(TMNetDef.TAG_TM_REQ_USER_PW,
									userpw);
							mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
									msg.what, resbundle);
						}
					}
				}
				break;
			}
			case TMNetDef.TM_REQ_TYPE_USERLOGIN_OFF: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_USERLOGIN_OFF, and id =" + id);
				if (bund != null) {
					if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_USER_NAME)) {
						String simnum = bund
								.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
						String username = bund
								.getString(TMNetDef.TAG_TM_REQ_USER_NAME);
						Log.d(TAG, "call yz_2_userlogout with simnum:" + simnum
								+ " username: " + username);
						int yzresult = mTransitImpl.yz_2_userlogout(simnum,
								username);
						Log.d(TAG, "yz_2_userlogout returns :" + yzresult);
						if (mNetRespLstnr != null) {
							Bundle resbundle = new Bundle();
							resbundle.putString(TMNetDef.TAG_TM_REQ_USER_NAME,
									username);
							mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
									msg.what, resbundle);
						}
					}
				}
				break;
			}
			case TMNetDef.TM_REQ_TYPE_GPS_UPLOAD: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_GPS_UPLOAD, and id =" + id);
				if (bund != null) {
					if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_GPS_TMLOCATION)) {
						String simnum = bund
								.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
						TMLocation tmlocation = bund
								.getParcelable(TMNetDef.TAG_TM_REQ_GPS_TMLOCATION);
						LocationSturt loc = TMDataConverter
								.mapTMLocationToSupConLoaction(tmlocation);
						int yzresult = mTransitImpl.yz_3_gpsupload(simnum, loc);
						Log.d(TAG, "yz_3_gpsupload returns :" + yzresult);
						if (mNetRespLstnr != null) {
							mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
									msg.what, null);
						}
					}
				}
				break;
			}
			case TMNetDef.TM_REQ_TYPE_VEHICLE_STATUS: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_VEHICLE_STATUS, and id =" + id);
				if (bund != null) {
					if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_VEHICLE_STATUS)) {
						String simnum = bund
								.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
						int status = bund
								.getInt(TMNetDef.TAG_TM_REQ_VEHICLE_STATUS);
						int yzresult = mTransitImpl.yz_2_sendvehiclestatus(
								simnum, status);
						Log.d(TAG, "yz_2_sendvehiclestatus returns :"
								+ yzresult);
						if (mNetRespLstnr != null) {
							mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
									msg.what, null);
						}
					}
				}
				break;
			}
			case TMNetDef.TM_REQ_TYPE_TMC_STATUS: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_TMC_STATUS, and id =" + id);
				if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)) {
					// String simnum =
					// bund.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
					int yzresult = mTransitImpl.yz_3_gettmc();
					Log.d(TAG, "yz_2_sendvehiclestatus returns :" + yzresult);
					if (mNetRespLstnr != null) {
						mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
								msg.what, null);
					}
				}
				break;
			}
			case TMNetDef.TM_REQ_TYPE_VEHICLE_DATA: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_VEHICLE_DATA, and id =" + id);
				if (bund != null) {
					if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_VEHICLE_DATA)) {
						String simnum = bund
								.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
						TMVehicleData tmvdata = bund
								.getParcelable(TMNetDef.TAG_TM_REQ_VEHICLE_DATA);
						VehicleDataStruct vdata = TMDataConverter
								.mapTMVehicleDataTOSupConVehicleData(tmvdata);
						int yzresult = mTransitImpl.yz_3_sendvehicledata(
								simnum, vdata);
						Log.d(TAG, "yz_3_sendvehicledata returns :" + yzresult);
						if (mNetRespLstnr != null) {
							mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
									msg.what, null);
						}
					}
				}
				break;
			}
			case TMNetDef.TM_REQ_TYPE_WEATHER_CITY: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_WEATHER_CITY, and id =" + id);
				if (bund != null) {
					if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_CITY_CODE)) {
						String simnum = bund
								.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
						String citycode = bund
								.getString(TMNetDef.TAG_TM_REQ_CITY_CODE);
						WeatherSturt weather = new WeatherSturt();
						int yzresult = mTransitImpl.yz_3_getweather(simnum,
								citycode, weather);
						Log.d(TAG, "yz_3_getweather returns :" + yzresult);
						if (mNetRespLstnr != null) {
							Bundle resbundle = new Bundle();
							TMWeather tmweather = TMDataConverter
									.mapSupConWeatherToTMWeather(weather);
							resbundle.putParcelable(
									TMNetDef.TAG_TM_REQ_WEATHER_CONTENT,
									tmweather);
							mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
									msg.what, resbundle);
						}
					}
				}
				break;
			}
			case TMNetDef.TM_REQ_TYPE_WEATHER_LOC: {
				Integer id = (Integer) msg.obj;
				Bundle bund = msg.getData();
				Log.d(TAG, "case TM_REQ_TYPE_WEATHER_LOC, and id =" + id);
				if (bund != null) {
					if (bund.containsKey(TMNetDef.TAG_TM_REQ_SIMCARD_NUM)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_LOC_LONGT)
							&& bund.containsKey(TMNetDef.TAG_TM_REQ_LOC_LAT)) {
						String simnum = bund
								.getString(TMNetDef.TAG_TM_REQ_SIMCARD_NUM);
						double longt = bund
								.getDouble(TMNetDef.TAG_TM_REQ_LOC_LONGT);
						double lat = bund
								.getDouble(TMNetDef.TAG_TM_REQ_LOC_LAT);
						WeatherSturt weather = new WeatherSturt();
						int yzresult = mTransitImpl.yz_3_getweather(simnum,
								lat, longt, weather);
						Log.d(TAG, "yz_3_getweather returns :" + yzresult);
						if (mNetRespLstnr != null) {
							Bundle resbundle = new Bundle();
							TMWeather tmweather = TMDataConverter
									.mapSupConWeatherToTMWeather(weather);
							resbundle.putParcelable(
									TMNetDef.TAG_TM_REQ_WEATHER_CONTENT,
									tmweather);
							mNetRespLstnr.onNetResponse(id, mReqType, yzresult,
									msg.what, resbundle);
						}
					}
				}
				break;
			}
			default:
				break;
			}
		}
	}
}
