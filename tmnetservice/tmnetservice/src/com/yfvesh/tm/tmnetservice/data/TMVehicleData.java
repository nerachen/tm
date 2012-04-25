package com.yfvesh.tm.tmnetservice.data;

import android.os.Parcel;
import android.os.Parcelable;

public final class TMVehicleData implements Parcelable {

	private int mRotationalSpeed;
	private int mSpeed;
	private int mOilLevel;
	private int mCoolantTemperature;
	private int mOilPressure;
	private int mAbsState;
	private int mTractionControl;
	private int mNonSlip;
	private int mGear;
	private int mCruise;
	private int mAntiThief;
	private int mTirePressure;
	private int mDrivingMode;

	public TMVehicleData() {
	}
	
	public TMVehicleData(int rotationspeed, int speed, int oillevel,
			int coolanttemp, int oilpressure, int absstate, int transctl,
			int nonslip, int gear, int cruise, int antthief, int tirepressure,
			int drivemode) {
		mRotationalSpeed = rotationspeed;
		mSpeed = speed;
		mOilLevel = oillevel;
		mCoolantTemperature = coolanttemp;
		mOilPressure = oilpressure;
		mAbsState = absstate;
		mTractionControl = transctl;
		mNonSlip = nonslip;
		mGear = gear;
		mCruise = cruise;
		mAntiThief = antthief;
		mTirePressure = tirepressure;
		mDrivingMode = drivemode;
	}

	public TMVehicleData(Parcel in) {
		readFromParcel(in);
	}
	
	public int getRotationSpeed() {
		return mRotationalSpeed;
	}

	public void setRotationSpeed(int iparam) {
		mRotationalSpeed = iparam;
	}

	public int getSpeed() {
		return mSpeed;
	}

	public void setSpeed(int iparam) {
		mSpeed = iparam;
	}

	public int getOilLevel() {
		return mOilLevel;
	}

	public void setOilLevel(int iparam) {
		mOilLevel = iparam;
	}

	public int getCoolantTemperature() {
		return mCoolantTemperature;
	}

	public void setCoolantTemperature(int iparam) {
		mCoolantTemperature = iparam;
	}

	public int getOilPressure() {
		return mOilPressure;
	}

	public void setOilPressure(int iparam) {
		mOilPressure = iparam;
	}

	public int getAbsState() {
		return mAbsState;
	}

	public void setAbsState(int iparam) {
		mAbsState = iparam;
	}

	public int getTractionControl() {
		return mTractionControl;
	}

	public void setTractionControl(int iparam) {
		mTractionControl = iparam;
	}

	public int getNonSlip() {
		return mNonSlip;
	}

	public void setNonSlip(int iparam) {
		mNonSlip = iparam;
	}

	public int getGear() {
		return mGear;
	}

	public void setGear(int iparam) {
		mGear = iparam;
	}

	public int getCruise() {
		return mCruise;
	}

	public void setCruise(int iparam) {
		mCruise = iparam;
	}

	public int getAntiThief() {
		return mAntiThief;
	}

	public void setAntiThief(int iparam) {
		mAntiThief = iparam;
	}

	public int getTirePressure() {
		return mTirePressure;
	}

	public void setTirePressure(int iparam) {
		mTirePressure = iparam;
	}

	public int getDrivingMode() {
		return mDrivingMode;
	}

	public void setDrivingMode(int iparam) {
		mDrivingMode = iparam;
	}

	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mRotationalSpeed);
		dest.writeInt(mSpeed);
		dest.writeInt(mOilLevel);
		dest.writeInt(mCoolantTemperature);
		dest.writeInt(mOilPressure);
		dest.writeInt(mAbsState);
		dest.writeInt(mTractionControl);
		dest.writeInt(mNonSlip);
		dest.writeInt(mGear);
		dest.writeInt(mCruise);
		dest.writeInt(mAntiThief);
		dest.writeInt(mTirePressure);
		dest.writeInt(mDrivingMode);
	}

	public void readFromParcel(Parcel in) {
		mRotationalSpeed = in.readInt();
		mSpeed = in.readInt();
		mOilLevel = in.readInt();
		mCoolantTemperature = in.readInt();
		mOilPressure = in.readInt();
		mAbsState = in.readInt();
		mTractionControl = in.readInt();
		mNonSlip = in.readInt();
		mGear = in.readInt();
		mCruise = in.readInt();
		mAntiThief = in.readInt();
		mTirePressure = in.readInt();
		mDrivingMode = in.readInt();
	}

	public static final Parcelable.Creator<TMVehicleData> CREATOR = new Parcelable.Creator<TMVehicleData>() {
		public TMVehicleData createFromParcel(Parcel in) {
			return new TMVehicleData(in);
		}

		public TMVehicleData[] newArray(int size) {
			return new TMVehicleData[size];
		}
	};
	
	@Override
	public String toString() {
		return " speed:" + mSpeed ;
	}
}
