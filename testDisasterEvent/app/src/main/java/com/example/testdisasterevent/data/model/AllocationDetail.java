package com.example.testdisasterevent.data.model;

public class AllocationDetail {
    private int ambulance;
    private int bus;
    private int police;
    private int fireTruck;
    public AllocationDetail() {

    }
    public AllocationDetail(int ambulance, int bus, int police, int fireTruck) {
        this.ambulance = ambulance;
        this.bus = bus;
        this.police = police;
        this.fireTruck = fireTruck;
    }

    public int getAmbulance() {
        return ambulance;
    }

    public void setAmbulance(int ambulance) {
        this.ambulance = ambulance;
    }

    public int getBus() {
        return bus;
    }

    public void setBus(int bus) {
        this.bus = bus;
    }

    public int getPolice() {
        return police;
    }

    public void setPolice(int police) {
        this.police = police;
    }

    public int getFireTruck() {
        return fireTruck;
    }

    public void setFireTruck(int fireTruck) {
        this.fireTruck = fireTruck;
    }
}
