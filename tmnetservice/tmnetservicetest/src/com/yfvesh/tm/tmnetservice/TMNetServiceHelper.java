package com.yfvesh.tm.tmnetservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.yfvesh.tm.tmnetservice.data.TMLocation;
import com.yfvesh.tm.tmnetservice.data.TMNetDef;
import com.yfvesh.tm.tmnetservice.data.TMVehicleData;

public final class TMNetServiceHelper {

	private RemoteServiceConnection mRemoteTMConn = null;
	private ITMNetService mRemoteTMSercice = null;
	private Context mCtx;

	public TMNetServiceHelper(Context ctx) {
		mCtx = ctx;
	}

	public void bindTMNetService() {
		if (mCtx != null) {
			if (mRemoteTMConn == null) {
				mRemoteTMConn = new RemoteServiceConnection();
			}
			if (mRemoteTMSercice == null) {
				Intent i = new Intent(
						"com.yfvesh.tm.tmnetservice.TMNET_SERVICE");
				mCtx.bindService(i, mRemoteTMConn, Context.BIND_AUTO_CREATE);
				Log.d(getClass().getSimpleName(), "bindTMNetService Service()");
			}
		}
	}

	public void unbindTMNetService() {
		if (mRemoteTMConn != null && mCtx != null) {
			mCtx.unbindService(mRemoteTMConn);
			mRemoteTMConn = null;
			mRemoteTMSercice = null;
			Log.d(getClass().getSimpleName(), "unbindService Service()");
		}
	}

	public boolean checkTMNetSrvWorkable() {
		if (mRemoteTMConn == null || mCtx == null || mRemoteTMSercice == null) {
			return false;
		}
		return true;
	}

	public boolean setSimNumToTMNetSrv(String simnum) {
		if (mRemoteTMConn == null && mRemoteTMSercice == null) {
			return false;
		} else {
			try {
				return mRemoteTMSercice.setSimNum(simnum);
			} catch (RemoteException e) {
			}
		}
		return false;
	}

	public int tmUserLogin(int id, String username, String password) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.userLogin(id, username, password);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public int tmUserLogout(int id, String username) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.userLogout(id, username);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public int tmCancelUserLogActionReq(int id) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.cancelUserLogActionReq(id);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public boolean tmCheckUserLoginStatus() {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return false;
		} else {
			try {
				return mRemoteTMSercice.isUserLogged();
			} catch (RemoteException e) {
			}
		}
		return false;
	}

	public int tmGpsUpload(int id, TMLocation tmloc) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.gpsUpload(id, tmloc);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public int tmSendVehicleStatus(int id, int status) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.sendVehicleStatus(id, status);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public int tmGetTMC(int id) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.getTMC(id);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public int tmGetWeatherByCity(int id, String citycode) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.getWeatherByCity(id, citycode);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public int tmGetWeatherByLoc(int id, double lat, double longt) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.getWeatherByLoc(id, lat, longt);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	public int tmSendVehicleData(int id, TMVehicleData vehicledata) {
		if (mRemoteTMConn == null || mRemoteTMSercice == null) {
			return TMNetDef.TM_ERR_SERVICE_NOT_BIND;
		} else {
			try {
				return mRemoteTMSercice.sendVehicleData(id, vehicledata);
			} catch (RemoteException e) {
				return TMNetDef.TM_ERR_REMOTE_EXCEPTION;
			}
		}
	}

	private class RemoteServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className,
				IBinder boundService) {
			mRemoteTMSercice = ITMNetService.Stub
					.asInterface((IBinder) boundService);
			Log.d(getClass().getSimpleName(), "onServiceConnected()");
		}

		public void onServiceDisconnected(ComponentName className) {
			mRemoteTMSercice = null;
			mRemoteTMConn = null;
			Log.d(getClass().getSimpleName(), "onServiceDisconnected");
		}
	};
}
