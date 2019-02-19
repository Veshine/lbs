package com.example.acer.lbsgereja.model;

/**
 * Created on   : 11/12/2017
 * Developed by : Hendrawan Adi Wijaya
 * Github       : https://github.com/andevindo
 * Website      : http://www.andevindo.com
 */

public class Graph {

    private int mId;
    private Node mSimpulAwal;
    private Node mSimpulAkhir;
    private String mJalur;
    private double mJarak;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Node getSimpulAwal() {
        return mSimpulAwal;
    }

    public void setSimpulAwal(Node simpulAwal) {
        mSimpulAwal = simpulAwal;
    }

    public Node getSimpulAkhir() {
        return mSimpulAkhir;
    }

    public void setSimpulAkhir(Node simpulAkhir) {
        mSimpulAkhir = simpulAkhir;
    }

    public String getJalur() {
        return mJalur;
    }

    public void setJalur(String jalur) {
        mJalur = jalur;
    }

    public double getJarak() {
        return mJarak;
    }

    public void setJarak(double jarak) {
        mJarak = jarak;
    }
}
