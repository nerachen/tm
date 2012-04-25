package com.yfvesh.tm.tmnetservice.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TMLocation implements Parcelable {

	private long mTime;
	private double mLongitude;
	private double mLatitude;
	private float mSpeed;
	private float mDirection;
	private float mAltitude;
	private int mSatNum;
	private int mAlarmStatus;

	public TMLocation() {
	}

	public TMLocation(long time, double longt, double lat, float speed,
			float direction, float alt, int satnum, int alarmstatus) {
		mTime = time;
		mLongitude = longt;
		mLatitude = lat;
		mSpeed = speed;
		mDirection = direction;
		mAltitude = alt;
		mSatNum = satnum;
		mAlarmStatus = alarmstatus;
	}

	public TMLocation(Parcel in) {
		readFromParcel(in);
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		mTime = time;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longtitude) {
		mLongitude = longtitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	public float getSpeed() {
		return mSpeed;
	}

	public void setSpeed(float speed) {
		mSpeed = speed;
	}

	public float getDirection() {
		return mDirection;
	}

	public void setDirection(float direction) {
		mDirection = direction;
	}

	public float getAltitude() {
		return mAltitude;
	}

	public void setAltitude(float altitude) {
		mAltitude = altitude;
	}

	public int getSatNum() {
		return mSatNum;
	}

	public void setSatNum(int satnum) {
		mSatNum = satnum;
	}

	public int getAlarmStatus() {
		return mAlarmStatus;
	}

	public void setAlarmStatus(int alarmstatus) {
		mAlarmStatus = alarmstatus;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mTime);
		dest.writeDouble(mLongitude);
		dest.writeDouble(mLatitude);
		dest.writeFloat(mSpeed);
		dest.writeFloat(mDirection);
		dest.writeFloat(mAltitude);
		dest.writeInt(mSatNum);
		dest.writeInt(mAlarmStatus);
	}

	public void readFromParcel(Parcel in) {
		mTime = in.readLong();
		mLongitude = in.readDouble();
		mLatitude = in.readDouble();
		mSpeed = in.readFloat();
		mDirection = in.readFloat();
		mAltitude = in.readFloat();
		mSatNum = in.readInt();
		mAlarmStatus = in.readInt();
	}

	public static final Parcelable.Creator<TMLocation> CREATOR = new Parcelable.Creator<TMLocation>() {
		public TMLocation createFromParcel(Parcel in) {
			return new TMLocation(in);
		}

		public TMLocation[] newArray(int size) {
			return new TMLocation[size];
		}
	};

	@Override
	public String toString() {
		return "Longitude:" + mLongitude + " Latitude:" + mLatitude;
	}
}
