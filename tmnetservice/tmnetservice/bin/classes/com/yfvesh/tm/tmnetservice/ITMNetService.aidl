package com.yfvesh.tm.tmnetservice;

import com.yfvesh.tm.tmnetservice.data.TMLocation;
import com.yfvesh.tm.tmnetservice.data.TMVehicleData;

interface ITMNetService{

	/* set the simcard num needs for all net request */
	boolean setSimNum(String simnum);
	
	/* get current user login status */
	boolean isUserLogged();
	
	/* login to remote server */
	int userLogin(int id,String username, String password);
	
	/* cancel login request */
	int cancelUserLogActionReq(int id);
	
	/* logout from remote server */
	int userLogout(int id,String username);
	
	/* gps data upload to remote */
	int gpsUpload(int id,in TMLocation tmloc);
	
	/* upload vehicle working status */
	int sendVehicleStatus(int id,int status);
	
	/* get TMC info */
	int getTMC(int id);
	
	/* get weather by citycode */
	int getWeatherByCity(int id,String citycode);
	
	/* get weather report by lat/longt */
	int getWeatherByLoc(int id,double lat, double longt);
	
	/* send */
	int sendVehicleData(int id,in TMVehicleData vehicledata);
	
	/* init the transition for remote */
}