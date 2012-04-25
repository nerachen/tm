package com.yfvesh.tm.tmnetservice.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public final class TMWeather implements Parcelable {

	private double mLongitude;
	private double mLatitude;
	private String mCityCode;
	private String mCurrCondition;
	private int mCurrTempC;
	private float mCurrHumidity;
	private String mCurrWindDirection;
	private int mCurrWindPower;
	private String mCarWashIndex;
	private String mTravelIndex;
	private String mDressingIndex;
	private String mComfortIndex;
	private String mRetain1;
	private String mRetain2;
	private String mRetain3;
	private String mRetain4;
	private String mRetain5;
	private ArrayList<String> mCondition = new ArrayList<String>();
	private int[] mMaxTempC = new int[0];
	private int[] mMinTempC = new int[0];

	public TMWeather() {
	}

	public TMWeather(Parcel in) {
		readFromParcel(in);
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	public String getCityCode() {
		return mCityCode;
	}

	public void setCityCode(String citycode) {
		mCityCode = citycode;
	}

	public String getCurrCondition() {
		return mCurrCondition;
	}

	public void setCurrCondition(String currcondition) {
		mCurrCondition = currcondition;
	}

	public int getCurrTempC() {
		return mCurrTempC;
	}

	public void setCurrTempC(int currtempc) {
		mCurrTempC = currtempc;
	}

	public float getCurrHumidity() {
		return mCurrHumidity;
	}

	public void setCurrHumidity(float currhumidity) {
		mCurrHumidity = currhumidity;
	}

	public String getCurrWindDirection() {
		return mCurrWindDirection;
	}

	public void setCurrWindDirection(String currwinddirection) {
		mCurrWindDirection = currwinddirection;
	}

	public int getCurrWindPower() {
		return mCurrWindPower;
	}

	public void setCurrWindPower(int currwindpower) {
		mCurrWindPower = currwindpower;
	}

	public String getCarWashIndex() {
		return mCarWashIndex;
	}

	public void setCarWashIndex(String carwashindex) {
		mCarWashIndex = carwashindex;
	}

	public String getTravelIndex() {
		return mTravelIndex;
	}

	public void setTravelIndex(String travelindex) {
		mTravelIndex = travelindex;
	}

	public String getDressingIndex() {
		return mDressingIndex;
	}

	public void setDressingIndex(String dressingindex) {
		mDressingIndex = dressingindex;
	}

	public String getComfortIndex() {
		return mComfortIndex;
	}

	public void setComfortIndex(String comfortindex) {
		mComfortIndex = comfortindex;
	}

	public String getRetain1() {
		return mRetain1;
	}

	public void setRetain1(String retain1) {
		mRetain1 = retain1;
	}

	public String getRetain2() {
		return mRetain2;
	}

	public void setRetain2(String retain2) {
		mRetain2 = retain2;
	}

	public String getRetain3() {
		return mRetain3;
	}

	public void setRetain3(String retain3) {
		mRetain3 = retain3;
	}

	public String getRetain4() {
		return mRetain4;
	}

	public void setRetain4(String retain4) {
		mRetain4 = retain4;
	}

	public String getRetain5() {
		return mRetain5;
	}

	public void setRetain5(String retain5) {
		mRetain5 = retain5;
	}

	public ArrayList<String> getCondition() {
		return mCondition;
	}

	public void setCondition(ArrayList<String> conditon) {
		mCondition = conditon;
	}

	public void setCondition(String[] condition) {
		if (condition != null) {
			int len = condition.length;
			mCondition.clear();
			for (int i = 0; i < len; ++i) {
				mCondition.add(condition[i]);
			}
		}
	}

	public int[] getMaxTempC() {
		return mMaxTempC;
	}

	public void setMaxTempC(int[] maxtempc) {
		if (maxtempc == null) {
			mMaxTempC = new int[0];
		} else {
			mMaxTempC = maxtempc;
		}
	}

	public int[] getMinTempC() {
		return mMinTempC;
	}

	public void setMinTempC(int[] mintempc) {
		if (mintempc == null) {
			mMinTempC = new int[0];
		} else {
			mMinTempC = mintempc;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		mLongitude = in.readDouble();
		mLatitude = in.readDouble();
		mCityCode = in.readString();
		mCurrCondition = in.readString();
		mCurrTempC = in.readInt();
		mCurrHumidity = in.readFloat();
		mCurrWindDirection = in.readString();
		mCurrWindPower = in.readInt();
		mCarWashIndex = in.readString();
		mTravelIndex = in.readString();
		mDressingIndex = in.readString();
		mComfortIndex = in.readString();
		mRetain1 = in.readString();
		mRetain2 = in.readString();
		mRetain3 = in.readString();
		mRetain4 = in.readString();
		mRetain5 = in.readString();
		in.readStringList(mCondition);

		in.readIntArray(mMaxTempC);
		in.readIntArray(mMinTempC);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(mLongitude);
		dest.writeDouble(mLatitude);
		dest.writeString(mCityCode);
		dest.writeString(mCurrCondition);
		dest.writeInt(mCurrTempC);
		dest.writeFloat(mCurrHumidity);
		dest.writeString(mCurrWindDirection);
		dest.writeInt(mCurrWindPower);
		dest.writeString(mCarWashIndex);
		dest.writeString(mTravelIndex);
		dest.writeString(mDressingIndex);
		dest.writeString(mComfortIndex);
		dest.writeString(mRetain1);
		dest.writeString(mRetain2);
		dest.writeString(mRetain3);
		dest.writeString(mRetain4);
		dest.writeString(mRetain5);
		dest.writeStringList(mCondition);

		dest.writeIntArray(mMaxTempC);
		dest.writeIntArray(mMinTempC);
	}

	public static final Parcelable.Creator<TMWeather> CREATOR = new Parcelable.Creator<TMWeather>() {
		public TMWeather createFromParcel(Parcel in) {
			return new TMWeather(in);
		}

		public TMWeather[] newArray(int size) {
			return new TMWeather[size];
		}
	};

	@Override
	public String toString() {
		return " CityCode:" + mCityCode + " Longitude:" + mLongitude
				+ " Latitude:" + mLatitude;
	}
}
