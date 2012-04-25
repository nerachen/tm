package com.yfvesh.tm.tmnetservice.data;

import android.os.Parcel;
import android.os.Parcelable;

public final class TMPoi implements Parcelable {

	private double mLongitude;
	private double mLatitude;
	private String mPoiName;

	public TMPoi() {
	}

	public TMPoi(double longt, double lat, String poiname) {
		mLongitude = longt;
		mLatitude = lat;
		mPoiName = poiname;
	}

	public TMPoi(Parcel in) {
		readFromParcel(in);
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longt) {
		mLongitude = longt;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double lat) {
		mLatitude = lat;
	}

	public String getPoiName() {
		return mPoiName;
	}

	public void setPoiName(String poiname) {
		mPoiName = poiname;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		mLongitude = in.readDouble();
		mLatitude = in.readDouble();
		mPoiName = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(mLongitude);
		dest.writeDouble(mLatitude);
		dest.writeString(mPoiName);
	}

	public static final Parcelable.Creator<TMPoi> CREATOR = new Parcelable.Creator<TMPoi>() {
		public TMPoi createFromParcel(Parcel in) {
			return new TMPoi(in);
		}

		public TMPoi[] newArray(int size) {
			return new TMPoi[size];
		}
	};

	@Override
	public String toString() {
		return " Poiname:" + mPoiName + " Longitude:" + mLongitude
				+ " Latitude:" + mLatitude;
	}
}
