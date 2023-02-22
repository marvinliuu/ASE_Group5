package com.example.testdisasterevent.data.model;

public class DisasterDetail {
    private String location;
    private String happenTime;
    private float latitude;
    private float longtitude;
    private String  disasterTitle;


    public DisasterDetail(String location, String happenTime, float latitude, float longtitude, String disasterTitle) {
        this.location = location;
        this.happenTime = happenTime;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.disasterTitle = disasterTitle;
    }

    public String getLocation() {return this.location;}
    public String getHappenTime() {return this.happenTime;}
    public String getDisasterTitle() {return this.disasterTitle;}
    public float getLatitude() {return this.latitude;}
    public float getLongtitude() {return this.longtitude;}
}
