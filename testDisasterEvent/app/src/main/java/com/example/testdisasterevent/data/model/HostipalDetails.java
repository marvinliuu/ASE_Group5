package com.example.testdisasterevent.data.model;

public class HostipalDetails {
    private int hid;
    private int n_ambulance;
    private int n_ava_amublance;
    private int n_doctor;
    private int n_ava_doctor;
    private float latitude;
    private float longtitude;
    private String  hostipalName;


    public HostipalDetails(int hid, String hostipalName, int n_ambulance, int n_ava_amublance, int n_doctor, int n_ava_doctor, float latitude, float longtitude) {
        this.hid = hid;
        this.hostipalName = hostipalName;
        this.n_ambulance = n_ambulance;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.n_ava_amublance = n_ava_amublance;
        this.n_doctor = n_doctor;
        this.n_ava_doctor = n_ava_doctor;
    }

    public int getHid() { return this.hid; }
    public String getHostipalName() {return this.hostipalName;}
    public int getTotalAmublance() {return this.n_ambulance;}
    public int getAvaAmbulance() {return this.n_ava_amublance;}
    public int getTotalDoctor() {return this.n_doctor;}
    public int getAvaDoctor() {return this.n_ava_doctor;}
    public float getLatitude() {return this.latitude;}
    public float getLongitude() {return this.longtitude;}
}
