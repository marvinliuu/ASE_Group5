package com.example.testdisasterevent.data.model;

public class ReportFromCitizen {
    private float longitude;
    private int disasterType;
    private float latitude;
    private int injuredNum;
    private int radius;
    private String otherInfo;

    public ReportFromCitizen(){};

    public ReportFromCitizen(int disasterType, int injuredNum, float latitude, float longtitude, String otherInfo, int radius) {
        this.disasterType = disasterType;
        this.injuredNum = injuredNum;
        this.latitude = latitude;
        this.longitude = longitude;
        this.otherInfo = otherInfo;
        this.radius = radius;
    }



    public void setDisasterType(int disasterType) {
        this.disasterType = disasterType;
    }

    public void setInjuredNum(int injuredNum) {
        this.injuredNum = injuredNum;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getDisasterType() {
        return disasterType;
    }

    public int getInjuredNum() {
        return injuredNum;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public int getRadius() {
        return radius;
    }
}
