package com.example.testdisasterevent.data.model;

public class TaskDetail {
    private float latitude;
    private float longitude;
    private int injury;
    private String  disasterTitle;
    private String happenTime;
    private String location;
    private String state;
    private String taskKey;

    public TaskDetail(float latitude, float longitude, int injury, String disasterTitle, String happenTime, String location, String state, String taskKey) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.injury = injury;
        this.disasterTitle = disasterTitle;
        this.happenTime = happenTime;
        this.location = location;
        this.state = state;
        this.taskKey = taskKey;
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

    public int getInjury() {
        return injury;
    }

    public void setInjury(int injury) {
        this.injury = injury;
    }

    public String getDisasterTitle() {
        return disasterTitle;
    }

    public void setDisasterTitle(String disasterTitle) {
        this.disasterTitle = disasterTitle;
    }

    public String getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(String happenTime) {
        this.happenTime = happenTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaskKey(){ return  taskKey; }

    public void setTaskKey(String taskKey) { this.taskKey = taskKey;}
}
