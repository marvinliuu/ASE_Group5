package com.example.testdisasterevent.data.model;

public class ReportInfo {
    private int injured;
    private String location;
    private String happenTime;
    private String reportTime;
    private float latitude;
    private float longitude;
    private String reportTitle;
    private String description;
    private int reportState;
    private String reportNumber;


    public ReportInfo( int injured, String location, String reportTime, float latitude,
                       float longitude, String disasterTitle,  int reportState,
                       String reportNumber, String description) {

        this.injured = injured;
        this.location = location;
        this.happenTime = happenTime;
        this.reportTime = reportTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reportTitle = disasterTitle;
        this.description = description;
        this.reportState = reportState;
        this.reportNumber = reportNumber;

    }

    public int getInjured() {return this.injured;}
    public String getLocation() {return this.location;}
    public String getHappenTime() {return this.happenTime;}
    public String getReportTime() {return this.reportTime;}
    public String getReportTitle() {return this.reportTitle;}
    public float getLatitude() {return this.latitude;}
    public float getLongitude() {return this.longitude;}
    public String getDescription() {return this.description;}
    public int getReportState() {return this.reportState;}
    public String getReportNumber(){return this.reportNumber;}
}
