package com.example.testdisasterevent.data.model;

public class DisasterDetail {
    private int radius;
    private String location;
    private String happenTime;
    private double latitude;
    private double longitude;
    private String disasterType;
    private String GardaUID;
    private int isUpdate;


    public DisasterDetail(int radius, String location, String happenTime, double latitude, double longitude, String disasterType, String GardaUID, int isUpdate) {
        this.radius = radius;
        this.location = location;
        this.happenTime = happenTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.disasterType = disasterType;
        this.GardaUID = GardaUID;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getGardaUID() {
        return GardaUID;
    }

    public void setGardaUID(String gardaUID) {
        this.GardaUID = gardaUID;
    }

    public int getUpdate() {
        return isUpdate;
    }

    public void setUpdate(int update) {
        isUpdate = update;
    }
}
