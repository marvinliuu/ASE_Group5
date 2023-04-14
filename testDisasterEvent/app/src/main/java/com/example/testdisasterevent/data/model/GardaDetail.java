package com.example.testdisasterevent.data.model;

public class GardaDetail {
    private int gid;
    private int n_car;
    private int n_ava_car;
    private int n_garda;
    private int n_ava_garda;
    private float latitude;
    private float longitude;
    private String  gardaName;

    public GardaDetail(int gid, int n_car, int n_ava_car, int n_garda, int n_ava_garda, float latitude, float longitude, String gardaName) {
        this.gid = gid;
        this.n_car = n_car;
        this.n_ava_car = n_ava_car;
        this.n_garda = n_garda;
        this.n_ava_garda = n_ava_garda;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gardaName = gardaName;
    }

    public int getGid() {
        return gid;
    }
    public void setGid(int gid) {
        this.gid = gid;
    }
    public int getN_car() {
        return n_car;
    }
    public void setN_car(int n_car) {
        this.n_car = n_car;
    }
    public int getN_ava_car() {
        return n_ava_car;
    }
    public void setN_ava_car(int n_ava_car) {
        this.n_ava_car = n_ava_car;
    }
    public int getN_garda() {
        return n_garda;
    }
    public void setN_garda(int n_garda) {
        this.n_garda = n_garda;
    }
    public int getN_ava_garda() {
        return n_ava_garda;
    }
    public void setN_ava_garda(int n_ava_garda) {
        this.n_ava_garda = n_ava_garda;
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
    public String getGardaName() {
        return gardaName;
    }
    public void setGardaName(String gardaName) {
        this.gardaName = gardaName;
    }
}
