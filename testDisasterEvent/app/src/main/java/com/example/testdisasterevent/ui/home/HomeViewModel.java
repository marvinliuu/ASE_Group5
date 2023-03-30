package com.example.testdisasterevent.ui.home;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdisasterevent.data.ReportDataSource;
import com.example.testdisasterevent.data.ReportDataSource_Police;
import com.example.testdisasterevent.data.RoadsInfoDatasource;
import com.example.testdisasterevent.data.model.ReportInfo;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private RoadsInfoDatasource roadsInfoDatasource;
    private ReportDataSource_Police reportDataSource_police;
    public int indexOfReportInfo;

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
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }


    private static LatLng calculateDestinationLocation(double lat, double lng, double distance, double bearing) {
        double radius = 6371.01; // 地球平均半径，单位为千米

        double lat1 = Math.toRadians(lat);
        double lon1 = Math.toRadians(lng);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(distance / radius)
                + Math.cos(lat1) * Math.sin(distance / radius) * Math.cos(Math.toRadians(bearing)));

        double lon2 = lon1 + Math.atan2(Math.sin(Math.toRadians(bearing))
                        * Math.sin(distance / radius) * Math.cos(lat1),
                Math.cos(distance / radius) - Math.sin(lat1) * Math.sin(lat2));

        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);

        return new LatLng(lat2, lon2);
    }

    public LatLng[] calculateDestinationLocations(LatLng centrePoint, double distance) {
        double lat = centrePoint.latitude;
        double lng = centrePoint.longitude;
        LatLng[] locations = new LatLng[2];

        LatLng neLocation = calculateDestinationLocation(lat, lng, distance, 45);
        locations[0] = neLocation;

        LatLng swLocation = calculateDestinationLocation(lat, lng, distance, 225);
        locations[1] = swLocation;

        return locations;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public List<LatLng> findMaxAreaPoints(List<LatLng> points, int k) {
        if (points == null || points.size() < k) {
            return new ArrayList<>();
        }

        points.sort(Comparator.comparingDouble(p -> p.latitude));
        double maxArea = 0;
        List<LatLng> result = new ArrayList<>();

        for (int i = 0; i <= points.size() - k; i++) {
            List<LatLng> currentPoints = points.subList(i, i + k);
            double area = calculateArea(currentPoints);
            if (area > maxArea) {
                maxArea = area;
                result = currentPoints;
            }
        }
        return result;
    }

    private static double calculateArea(List<LatLng> points) {
        double area = 0;
        int j = points.size() - 1;
        for (int i = 0; i < points.size(); i++) {
            area += (points.get(j).longitude + points.get(i).longitude) * (points.get(j).latitude - points.get(i).latitude);
            j = i;
        }
        return Math.abs(area / 2);
    }
}