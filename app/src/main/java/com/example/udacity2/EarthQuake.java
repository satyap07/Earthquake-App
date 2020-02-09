package com.example.udacity2;

public class EarthQuake {
    private double mMagnitude;
    private String mPlace;
    private long mDate;
    private String mURL;

    public EarthQuake(double mMagnitude, String mPlace, long mDate, String mURL) {
        this.mMagnitude = mMagnitude;
        this.mPlace = mPlace;
        this.mDate = mDate;
        this.mURL=mURL;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public long getmDate() {
        return mDate;
    }

    public String getmURL() {
        return mURL;
    }
}
