package com.example.testdisasterevent.ui.home;

import static java.lang.Math.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdisasterevent.data.ReportDataSource_Police;
import com.example.testdisasterevent.data.RoadsInfoDatasource;
import com.example.testdisasterevent.data.model.ReportInfo;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class HomeViewModel extends ViewModel {

    private RoadsInfoDatasource roadsInfoDatasource;
    private ReportDataSource_Police reportDataSource_police;

    public LiveData<ReportInfo[]> getReportInfo() {
        return reportDataSource_police.getReportInfo();
    }

    public HomeViewModel() {
        roadsInfoDatasource = new RoadsInfoDatasource();
        reportDataSource_police = new ReportDataSource_Police();
    }


    public double distance(LatLng point1, LatLng point2) {
        double lat1 = point1.latitude;
        double lon1 = point1.longitude;
        double lat2 = point2.latitude;
        double lon2 = point2.longitude;
        double R = 6371;
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                cos(toRadians(lat1)) * cos(toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }
}