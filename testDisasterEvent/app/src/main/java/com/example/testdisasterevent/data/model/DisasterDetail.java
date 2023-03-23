package com.example.testdisasterevent.data.model;

public class DisasterDetail {
    private int radius;
    private String location;
    private String happenTime;
    private float latitude;
    private float longitude;
    private String  disasterTitle;


    public DisasterDetail(int radius, String location, String happenTime, float latitude, float longitude, String disasterTitle) {
        this.radius = radius;
        this.location = location;
        this.happenTime = happenTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.disasterTitle = disasterTitle;
    }

    public int getRadius() { return this.radius; }
    public String getLocation() {return this.location;}
    public String getHappenTime() {return this.happenTime;}
    public String getDisasterTitle() {return this.disasterTitle;}
    public float getLatitude() {return this.latitude;}
    public float getLongitude() {return this.longitude;}
}
