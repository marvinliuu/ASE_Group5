package com.example.testdisasterevent.data.model;

public class TaskDetail {
    private float latitude;
    private float longtitude;
    private int injury;
    private String  disasterTitle;
    private String happenTime;
    private String location;

    public TaskDetail(String location, float latitude, float longtitude, int injury, String disasterTitle, String happenTime) {
        this.location = location;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.injury = injury;
        this.disasterTitle = disasterTitle;
        this.happenTime = happenTime;
    }

    public String getLocation() {return this.location;}
    public String getHappenTime() {return this.happenTime;}
    public String getDisasterTitle() {return this.disasterTitle;}
    public float getLatitude() {return this.latitude;}
    public float getLongitude() {return this.longtitude;}
    public String getInjury() {return String.valueOf(this.injury);}

}
