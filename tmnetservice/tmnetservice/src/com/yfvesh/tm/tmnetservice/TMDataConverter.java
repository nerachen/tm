package com.yfvesh.tm.tmnetservice;

import com.supcon.vehiclenetwork.sturt.LocationSturt;
import com.supcon.vehiclenetwork.sturt.POIStruct;
import com.supcon.vehiclenetwork.sturt.VehicleDataStruct;
import com.supcon.vehiclenetwork.sturt.WeatherSturt;
import com.yfvesh.tm.tmnetservice.data.TMLocation;
import com.yfvesh.tm.tmnetservice.data.TMPoi;
import com.yfvesh.tm.tmnetservice.data.TMVehicleData;
import com.yfvesh.tm.tmnetservice.data.TMWeather;

public final class TMDataConverter {
	public static LocationSturt mapTMLocationToSupConLoaction(TMLocation tmloc) {
		LocationSturt loc = new LocationSturt();

		loc.setTime(tmloc.getTime());
		loc.setLongitude(tmloc.getLongitude());
		loc.setLatitude(tmloc.getLatitude());
		loc.setSpeed(tmloc.getSpeed());
		loc.setDirection(tmloc.getDirection());
		loc.setAltitude(tmloc.getAltitude());
		loc.setAlarmType(tmloc.getAlarmStatus());

		return loc;
	}

	public static VehicleDataStruct mapTMVehicleDataTOSupConVehicleData(
			TMVehicleData tmvdata) {
		VehicleDataStruct vdata = new VehicleDataStruct();

		vdata.setRotationalSpeed(tmvdata.getRotationSpeed());
		vdata.setSpeed(tmvdata.getSpeed());
		vdata.setOilLevel(tmvdata.getOilLevel());
		vdata.setCoolantTemperature(tmvdata.getCoolantTemperature());
		vdata.setOilPressure(tmvdata.getOilPressure());
		vdata.setAbsState(tmvdata.getAbsState());
		vdata.setTractionControl(tmvdata.getTractionControl());
		vdata.setNonSlip(tmvdata.getNonSlip());
		vdata.setGear(tmvdata.getGear());
		vdata.setCruise(tmvdata.getCruise());
		vdata.setAntiTheft(tmvdata.getAntiThief());
		vdata.setTirePressure(tmvdata.getTirePressure());
		vdata.setDrivingMode(tmvdata.getDrivingMode());

		return vdata;
	}

	public static TMPoi mapSupConPoiToTMPoi(POIStruct poi) {
		return new TMPoi(poi.getLongtitude(), poi.getLatitude(),
				poi.getPoiname());
	}
	
	public static TMWeather mapSupConWeatherToTMWeather(WeatherSturt weather) {
		TMWeather tmweather = new TMWeather();
		
		tmweather.setLongitude(weather.getLongitude());
		tmweather.setLatitude(weather.getLatitude());
		tmweather.setCityCode(weather.getCityCode());
		tmweather.setCurrCondition(weather.getCurrCondition());
		tmweather.setCurrTempC(weather.getCurrTemp_c());
		tmweather.setCurrHumidity(weather.getCurrHumidity());
		tmweather.setCurrWindDirection(weather.getCurrWindDirection());
		tmweather.setCurrWindPower(weather.getCurrWindPower());
		tmweather.setCarWashIndex(weather.getCarWashIndex());
		tmweather.setTravelIndex(weather.getTravelIndex());
		tmweather.setDressingIndex(weather.getDressingIndex());
		tmweather.setComfortIndex(weather.getComfortIndex());
		tmweather.setRetain1(weather.getRetain1());
		tmweather.setRetain2(weather.getRetain2());
		tmweather.setRetain3(weather.getRetain3());
		tmweather.setRetain4(weather.getRetain4());
		tmweather.setRetain5(weather.getRetain5());
		tmweather.setCondition(weather.getCondition());
		tmweather.setMaxTempC(weather.getMaxTemp_c());
		tmweather.setMinTempC(weather.getMinTemp_c());
		
		return tmweather;
	}
}
