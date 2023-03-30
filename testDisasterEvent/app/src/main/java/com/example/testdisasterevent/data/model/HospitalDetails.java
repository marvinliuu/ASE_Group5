package com.example.testdisasterevent.data.model;

public class HospitalDetails {
    private int hid;
    private int n_ambulance;
    private int n_ava_ambulance;
    private int n_doctor;
    private int n_ava_doctor;
    private float latitude;
    private float longitude;
    private String  hospitalName;


    public HospitalDetails(int hid, String hospitalName, int n_ambulance, int n_ava_amublance, int n_doctor, int n_ava_doctor, float latitude, float longtitude) {
        this.hid = hid;
        this.hospitalName = hospitalName;
        this.n_ambulance = n_ambulance;
        this.latitude = latitude;
        this.longitude = longtitude;
        this.n_ava_ambulance = n_ava_amublance;
        this.n_doctor = n_doctor;
        this.n_ava_doctor = n_ava_doctor;
    }

    public int getHid() { return this.hid; }
    public String getHospitalName() {return this.hospitalName;}
    public int getTotalAmbulance() {return this.n_ambulance;}
    public int getAvaAmbulance() {return this.n_ava_ambulance;}
    public int getTotalDoctor() {return this.n_doctor;}
    public int getAvaDoctor() {return this.n_ava_doctor;}
    public float getLatitude() {return this.latitude;}
    public float getLongitude() {return this.longitude;}
}
