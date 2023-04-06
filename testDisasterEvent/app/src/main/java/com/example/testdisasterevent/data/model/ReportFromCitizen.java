package com.example.testdisasterevent.data.model;

public class ReportFromCitizen {
    private double longitude;
    private String disasterType;
    private double latitude;
    private int injuredNum;
    private int radius;
    private String otherInfo;
    private String accountUID;
    private int reportState;
    private String location;
    private String imageURL;
    private String timestamp;

    public ReportFromCitizen(){};

    public ReportFromCitizen(double longitude, String disasterType, double latitude, int injuredNum, int radius, String otherInfo, String accountUID, int reportState, String location, String imageURL, String timestamp) {
        this.longitude = longitude;
        this.disasterType = disasterType;
        this.latitude = latitude;
        this.injuredNum = injuredNum;
        this.radius = radius;
        this.otherInfo = otherInfo;
        this.accountUID = accountUID;
        this.reportState = reportState;
        this.location = location;
        this.imageURL = imageURL;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getReportState() {
        return reportState;
    }

    public void setReportState(int reportState) {
        this.reportState = reportState;
    }

    public String getAccountUID() {
        return accountUID;
    }

    public void setAccountUID(String accountUID) {
        this.accountUID = accountUID;
    }

    public void setDisasterType(String disasterType) {
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

    public String getDisasterType() {
        return disasterType;
    }

    public int getInjuredNum() {
        return injuredNum;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public int getRadius() {
        return radius;
    }
}
