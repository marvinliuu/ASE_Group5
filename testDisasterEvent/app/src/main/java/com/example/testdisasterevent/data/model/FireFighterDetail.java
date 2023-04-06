package com.example.testdisasterevent.data.model;

public class FireFighterDetail {
    private int fid;
    private int n_truck;
    private int n_ava_truck;
    private int n_firefighter;
    private int n_ava_firefighter;
    private float latitude;
    private float longitude;
    private String  fireBrigadeName;

    public FireFighterDetail(int fid, int n_truck, int n_ava_truck, int n_firefighter, int n_ava_firefighter, float latitude, float longitude, String fireBrigadeName) {
        this.fid = fid;
        this.n_truck = n_truck;
        this.n_ava_truck = n_ava_truck;
        this.n_firefighter = n_firefighter;
        this.n_ava_firefighter = n_ava_firefighter;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fireBrigadeName = fireBrigadeName;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getN_truck() {
        return n_truck;
    }

    public void setN_truck(int n_truck) {
        this.n_truck = n_truck;
    }

    public int getN_ava_truck() {
        return n_ava_truck;
    }

    public void setN_ava_truck(int n_ava_truck) {
        this.n_ava_truck = n_ava_truck;
    }

    public int getN_firefighter() {
        return n_firefighter;
    }

    public void setN_firefighter(int n_firefighter) {
        this.n_firefighter = n_firefighter;
    }

    public int getN_ava_firefighter() {
        return n_ava_firefighter;
    }

    public void setN_ava_firefighter(int n_ava_firefighter) {
        this.n_ava_firefighter = n_ava_firefighter;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getFireBrigadeName() {
        return fireBrigadeName;
    }

    public void setFireBrigadeName(String fireBrigadeName) {
        this.fireBrigadeName = fireBrigadeName;
    }
}
