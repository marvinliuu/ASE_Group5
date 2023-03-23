package com.example.testdisasterevent.data.model;

public class ReportFromCitizen {
    public int disasterType;
    public String injuredNum;
    public float latitude;
    public float longitude;
    public String  otherInfo;


    public ReportFromCitizen(int disasterType, String injuredNum, float latitude, float longitude, String  otherInfo) {
        this.disasterType = disasterType;
        this.injuredNum = injuredNum;
        this.latitude = latitude;
        this.longitude = longitude;
        this.otherInfo = otherInfo;
    }

    public int getDisasterType() {return this.disasterType;}
    public String getInjuredNum() {return this.injuredNum;}
    public String getOtherInfo() {return this.otherInfo;}
    public float getLatitude() {return this.latitude;}
    public float getLongitude() {return this.longitude;}

}
