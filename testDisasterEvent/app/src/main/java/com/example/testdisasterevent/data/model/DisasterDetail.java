package com.example.testdisasterevent.data.model;

public class DisasterDetail {
    private int radius;
    private String location;
    private String happenTime;
    private float latitude;
    private float longitude;
    private String  disasterTitle;
    private String reporterUID;
    private int isUpdate;


    public DisasterDetail(int radius, String location, String happenTime, float latitude, float longitude, String disasterTitle, String reporterUID, int isUpdate) {
        this.radius = radius;
        this.location = location;
        this.happenTime = happenTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.disasterTitle = disasterTitle;
        this.reporterUID = reporterUID;
        this.isUpdate = isUpdate;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
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

    public String getDisasterTitle() {
        return disasterTitle;
    }

    public void setDisasterTitle(String disasterTitle) {
        this.disasterTitle = disasterTitle;
    }

    public String getReporterUID() {
        return reporterUID;
    }

    public void setReporterUID(String reporterUID) {
        this.reporterUID = reporterUID;
    }

    public int getUpdate() {
        return isUpdate;
    }

    public void setUpdate(int update) {
        isUpdate = update;
    }
}
