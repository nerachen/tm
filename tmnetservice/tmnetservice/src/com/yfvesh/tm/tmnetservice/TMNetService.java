package com.yfvesh.tm.tmnetservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.supcon.vehiclenetwork.YZ_VehicleTransitImpl;
import com.supcon.vehiclenetwork.listen.VehicleTransitListen;
import com.supcon.vehiclenetwork.sturt.POIStruct;
import com.supcon.vehiclenetwork.sturt.WeatherSturt;
import com.yfvesh.tm.tmnetservice.data.TMLocation;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;
import com.yfvesh.tm.tmnetservice.data.TMPoi;
import com.yfvesh.tm.tmnetservice.data.TMVehicleData;
import com.yfvesh.tm.tmnetservice.data.TMWeather;

public class TMNetService extends Service implements INetResponse {
	private boolean mbUserLogged = false;

	private YZ_VehicleTransitImpl mYZTransitImpl = new YZ_VehicleTransitImpl();
	private YZTransitLstnr mYZTransitLstnr = new YZTransitLstnr();

	/* the SIM number used in each net request */
	private String mSimNum = "unknown";

	private Map<Integer, TMRequestThread> mThreadMap = new HashMap<Integer, TMRequestThread>();

	/* the actually implementation of AIDL interface */
	private ITMNetService.Stub mTMNetSrvStub = new ITMNetService.Stub() {

		@Override
		public boolean setSimNum(String simnum) throws RemoteException {
			mSimNum = simnum;
			return true;
		}

		@Override
		public boolean isUserLogged() throws RemoteException {
			return mbUserLogged;
		}

		@Override
		public int userLogin(int id, String username, String password)
				throws RemoteException {
			return addUsrLoginRequest(id, username, password);
		}

		@Override
		public int cancelUserLogActionReq(int id) throws RemoteException {
			return cancelUsrLoginRequest(id);
		}

		@Override
		public int userLogout(int id, String username) throws RemoteException {
			return addUsrLogoffRequest(id, username);
		}

		@Override
		public int gpsUpload(int id, TMLocation tmloc) throws RemoteException {
			return addGpsUploadRequest(id, tmloc);
		}

		@Override
		public int sendVehicleStatus(int id, int status) throws RemoteException {
			return addVehicleStatusUploadRequest(id, status);
		}

		@Override
		public int getTMC(int id) throws RemoteException {
			// TODO Auto-generated method stub
			// TMC not decide yet,only a stub here
			return addTMCRequest(id);
		}

		@Override
		public int getWeatherByCity(int id, String citycode)
				throws RemoteException {
			return addWeatherByCityRequest(id, citycode);
		}

		@Override
		public int getWeatherByLoc(int id, double lat, double longt)
				throws RemoteException {
			return addWeatherByLocRequest(id, lat, longt);
		}

		@Override
		public int sendVehicleData(int id, TMVehicleData vehicledata)
				throws RemoteException {
			return addVehicleDataUploadRequest(id, vehicledata);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(getClass().getSimpleName(), "TMNetService ** onCreate()!");
		initYZTransitImpl();
		createReqThreads();
		startReqThreads();
	}

	@Override
	public void onDestroy() {
		mTMNetSrvStub = null;
		Log.d(getClass().getSimpleName(), "onDestroy ** called!");
		stopReqThreads();
		deInitYZTransitImpl();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mTMNetSrvStub;
	}

	private void initYZTransitImpl() {
		mYZTransitImpl.yz_2_init("10.10.76.32", 9001, "127.0.0.1", 9010,
				"\\SDCARD\\YZ", mYZTransitLstnr);
	}

	private void deInitYZTransitImpl() {
		mYZTransitImpl.yz_2_destory();
	}

	private int addUsrLoginRequest(Integer id, String username, String userpw) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_USERLOGIN)) {
			TMRequestThread thread = mThreadMap
					.get(TMNetDef.TM_REQ_TYPE_USERLOGIN);
			if (thread.addLoginRequest(TMNetDef.TM_REQ_TYPE_USERLOGIN, id,
					simnum, username, userpw)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private int cancelUsrLoginRequest(Integer id) {
		return cancelRequest(TMNetDef.TM_REQ_TYPE_USERLOGIN, id);
	}

	private int addUsrLogoffRequest(Integer id, String username) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_USERLOGIN)) {
			TMRequestThread thread = mThreadMap
					.get(TMNetDef.TM_REQ_TYPE_USERLOGIN);
			if (thread.addLogoffRequest(TMNetDef.TM_REQ_TYPE_USERLOGIN, id,
					simnum, username)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private int addGpsUploadRequest(Integer id, TMLocation tmloc) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_GPS)) {
			TMRequestThread thread = mThreadMap.get(TMNetDef.TM_REQ_TYPE_GPS);
			if (thread.addGpsUploadRequest(TMNetDef.TM_REQ_TYPE_GPS, id,
					simnum, tmloc)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private int addVehicleStatusUploadRequest(Integer id, int status) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_OTHERS)) {
			TMRequestThread thread = mThreadMap
					.get(TMNetDef.TM_REQ_TYPE_OTHERS);
			if (thread.addVehicleStatusUploadRequest(
					TMNetDef.TM_REQ_TYPE_OTHERS, id, simnum, status)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private int addTMCRequest(Integer id) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_TRAFFIC)) {
			TMRequestThread thread = mThreadMap
					.get(TMNetDef.TM_REQ_TYPE_TRAFFIC);
			if (thread.addTMCRequest(TMNetDef.TM_REQ_TYPE_TRAFFIC, id, simnum)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private int addVehicleDataUploadRequest(Integer id, TMVehicleData vdata) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_OTHERS)) {
			TMRequestThread thread = mThreadMap
					.get(TMNetDef.TM_REQ_TYPE_OTHERS);
			if (thread.addVehicleDataUploadRequest(TMNetDef.TM_REQ_TYPE_OTHERS,
					id, simnum, vdata)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private int addWeatherByCityRequest(Integer id, String citycode) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_OTHERS)) {
			TMRequestThread thread = mThreadMap
					.get(TMNetDef.TM_REQ_TYPE_OTHERS);
			if (thread.addWeatherByCityRequest(TMNetDef.TM_REQ_TYPE_OTHERS, id,
					simnum, citycode)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private int addWeatherByLocRequest(Integer id, double lat, double longt) {
		String simnum = getSimCardNum();
		if (checkReqThread(TMNetDef.TM_REQ_TYPE_OTHERS)) {
			TMRequestThread thread = mThreadMap
					.get(TMNetDef.TM_REQ_TYPE_OTHERS);
			if (thread.addWeatherByLocRequest(TMNetDef.TM_REQ_TYPE_OTHERS, id,
					simnum, lat, longt)) {
				return TMNetDef.TM_SUCCESS;
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private void createReqThreads() {
		createReqThread(TMNetDef.TM_REQ_TYPE_USERLOGIN);
		createReqThread(TMNetDef.TM_REQ_TYPE_GPS);
		createReqThread(TMNetDef.TM_REQ_TYPE_TRAFFIC);
		createReqThread(TMNetDef.TM_REQ_TYPE_OTHERS);
	}

	private synchronized void createReqThread(int type) {
		if (!mThreadMap.containsKey(type)) {
			TMRequestThread thread = TMRequestThread.createTMReqThread(type);
			if (thread != null) {
				thread.setTMTrasitImpl(mYZTransitImpl);
				thread.setNetRespListner(this);
				mThreadMap.put(type, thread);
			}
		}
	}

	private void startReqThreads() {
		startReqThread(TMNetDef.TM_REQ_TYPE_USERLOGIN);
		startReqThread(TMNetDef.TM_REQ_TYPE_GPS);
		startReqThread(TMNetDef.TM_REQ_TYPE_TRAFFIC);
		startReqThread(TMNetDef.TM_REQ_TYPE_OTHERS);
	}

	private synchronized void startReqThread(int type) {
		if (mThreadMap.containsKey(type)) {
			TMRequestThread thread = mThreadMap.get(type);
			if (!thread.isAlive()) {
				thread.start();
			}
		}
	}

	private synchronized void stopReqThreads() {
		for (Map.Entry<Integer, TMRequestThread> entry : mThreadMap.entrySet()) {
			TMRequestThread thread = entry.getValue();
			if (thread != null && thread != null) {
				thread.stopThread();
			}
		}
		mThreadMap.clear();
	}

	private synchronized void stopReqThread(int type) {
		if (!mThreadMap.containsKey(type)) {
			TMRequestThread thread = mThreadMap.remove(type);
			if (thread != null && thread.isAlive()) {
				thread.stopThread();
			}
		}
	}

	private synchronized int cancelRequest(int type, int id) {
		if (mThreadMap.containsKey(type)) {
			TMRequestThread thread = mThreadMap.get(type);
			if (thread.isAlive()) {
				return thread.removRequest(type, id);
			}
		}
		return TMNetDef.TM_ERR_REQ_NOT_DONE;
	}

	private synchronized boolean checkReqThread(int type) {
		boolean result = false;
		/* if not yet created, create it */
		if (!mThreadMap.containsKey(type)) {
			createReqThread(type);
		}

		if (mThreadMap.containsKey(type)) {
			TMRequestThread thread = mThreadMap.get(type);
			if (!thread.isAlive()) {
				/* remove the non alive one, create start new one */
				mThreadMap.remove(type);
				createReqThread(type);
				thread = mThreadMap.get(type);
				if (thread != null) {
					thread.start();
					result = true;
				}
			} else {
				result = true;
			}
		}
		return result;
	}

	private String getSimCardNum() {
		return mSimNum;
	}

	private class YZTransitLstnr extends VehicleTransitListen {
		@Override
		public void yz_3_infoordercallback(int filetype, String filepath,
				int result) {
			/* this is used to push files */
			Bundle data = new Bundle();
			data.putInt(TMNetDef.TAG_TM_PUSH_FILE_TYPE, filetype);
			data.putString(TMNetDef.TAG_TM_PUSH_FILE_PATH, filepath);
			data.putInt(TMNetDef.TAG_TM_PUSH_RESULT, result);
			String actionstr = TMNetDef
					.mappingTypeToActionStr(TMNetDef.TM_PUSH_TYPE_FILE);
			braodcastResult(actionstr, data);
		}

		@Override
		public void yz_3_messagecallback(String msg) {
			Bundle data = new Bundle();
			data.putString(TMNetDef.TAG_TM_PUSH_MSG_CONTENT, msg);
			String actionstr = TMNetDef
					.mappingTypeToActionStr(TMNetDef.TM_PUSH_TYPE_MSG);
			braodcastResult(actionstr, data);
		}

		@Override
		public void yz_3_remotedescallback(int poinum, List<POIStruct> poilist,
				int result) {
			Bundle data = new Bundle();
			data.putInt(TMNetDef.TAG_TM_PUSH_POI_NUMS, poinum);

			/* set the data is any POI record */
			if (poilist != null && poilist.size() > 0) {
				ArrayList<TMPoi> tmpoilist = new ArrayList<TMPoi>();
				int count = poilist.size();
				for (int i = 0; i < count; ++i) {
					tmpoilist.add(TMDataConverter.mapSupConPoiToTMPoi(poilist
							.get(i)));
				}
				data.putParcelableArrayList(TMNetDef.TAG_TM_PUSH_POI_LIST,
						tmpoilist);
			}

			data.putInt(TMNetDef.TAG_TM_PUSH_RESULT, result);
			String actionstr = TMNetDef
					.mappingTypeToActionStr(TMNetDef.TM_PUSH_TYPE_DEST);
			braodcastResult(actionstr, data);
		}

		@Override
		public void yz_3_weathercallback(WeatherSturt weather) {
			Bundle data = new Bundle();
			TMWeather tmweather = TMDataConverter
					.mapSupConWeatherToTMWeather(weather);
			data.putParcelable(TMNetDef.TAG_TM_PUSH_WEATHER_CONTENT, tmweather);
			String actionstr = TMNetDef
					.mappingTypeToActionStr(TMNetDef.TM_PUSH_TYPE_WEATHER);
			braodcastResult(actionstr, data);
		}
	}

	private void braodcastResult(String actionstr, Bundle bundle) {
		if (actionstr != null && actionstr.length() > 0) {
			Intent intent = new Intent();
			intent.setAction(actionstr);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			sendBroadcast(intent);
		}
	}

	@Override
	public void onNetResponse(int id, int orgreqtype, int status,
			int detailtype, Bundle bundle) {
		Log.d("tag", "onNetResponse id=" + id + " status=" + status
				+ " detailtype=" + detailtype);
		String actionstr = TMNetDef.mappingTypeToActionStr(orgreqtype);
		Bundle data;
		if (bundle == null) {
			data = new Bundle();
		} else {
			data = bundle;
		}
		data.putInt(TMNetDef.TAG_TM_REQ_ID, id);
		data.putInt(TMNetDef.TAG_TM_REQ_STATUS, status);
		data.putInt(TMNetDef.TAG_TM_REQ_DETAIL_TYPE, detailtype);
		/* the login status in handled in service */
		if (TMNetDef.TM_REQ_TYPE_USERLOGIN == orgreqtype) {
			updateLoginResults(detailtype, status);
		}
		braodcastResult(actionstr, data);
	}

	private void updateLoginResults(int detailtype, int status) {
		if (TMNetDef.TM_REQ_TYPE_USERLOGIN_IN == detailtype) {
			if (TMNetDef.TM_SUCCESS == status
					|| TMNetDef.TM_ERR_USER_LOGIN_DUPLICATE == status) {
				mbUserLogged = true;
			} else {
				mbUserLogged = false;
			}
		} else if (TMNetDef.TM_REQ_TYPE_USERLOGIN_OFF == detailtype) {
			if (TMNetDef.TM_SUCCESS == status) {
				mbUserLogged = false;
			}
		}
	}
}
