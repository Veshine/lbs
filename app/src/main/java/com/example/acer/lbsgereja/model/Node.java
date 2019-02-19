package com.example.acer.lbsgereja.model;

/**
 * Created on   : 11/12/2017
 * Developed by : Hendrawan Adi Wijaya
 * Github       : https://github.com/andevindo
 * Website      : http://www.andevindo.com
 */

public class Node {

    private int mId;
    private int mIndexSimpul;
    private double mLatitude;
    private double mLongitude;

    public int getIndexSimpul() {
        return mIndexSimpul;
    }

    public void setIndexSimpul(int indexSimpul) {
        mIndexSimpul = indexSimpul;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
