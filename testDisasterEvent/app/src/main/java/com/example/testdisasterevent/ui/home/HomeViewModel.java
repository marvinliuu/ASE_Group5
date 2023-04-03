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
    public int indexOfReportInfo;
    private JsonObject defaultJson;

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

    public LatLng getLocLatLng(String locName) {
        LatLng res = null;
        try {
            // Set up the GeoApiContext with your API key
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyDrYjvowVSGRHTyi5vO7CZx2Py32G1BoaY")
                    .build();


            // Initiating geocoding requests
            GeocodingResult[] results = GeocodingApi.geocode(context, locName).await();

            // Get the first result of the geocoded result (usually the most relevant result)
            GeocodingResult result = results[0];

            // Obtain location information (longitude and latitude) for geocoding results
            com.google.maps.model.LatLng location = result.geometry.location;
            double latitude = location.lat;
            double longitude = location.lng;
            res = new LatLng(latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private static LatLng calculateDestinationLocation(double lat, double lng, double distance, double bearing) {
        double radius = 6371.01; // earth radius

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

    public LatLng[] calculateDestinationLocations(LatLng centrePoint, double radius) {
        double lat = centrePoint.latitude;
        double lng = centrePoint.longitude;
        double distance = radius * Math.sqrt(2) / 1000;
        LatLng[] locations = new LatLng[2];

        LatLng neLocation = calculateDestinationLocation(lat, lng, distance, 45);
        locations[0] = neLocation;

        LatLng swLocation = calculateDestinationLocation(lat, lng, distance, 225);
        locations[1] = swLocation;

        return locations;
    }

    public LatLng[] findTwoNearestPoint (LatLng startPoint, LatLng endPoint, LatLng centerPoint, double distanceX) {
        LatLng[] starts = calculateTangentPoints(centerPoint, startPoint, distanceX);
        LatLng[] ends = calculateTangentPoints(centerPoint, endPoint, distanceX);
        double minDis = Integer.MAX_VALUE;
        LatLng finalStart = null;
        LatLng finalEnd = null;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double tempDis = distance(starts[i], ends[j]);
                if (tempDis < minDis) {
                    minDis = tempDis;
                    finalStart = starts[i];
                    finalEnd = ends[j];
                }
            }
        }
        LatLng[] res = new LatLng[2];
        res[0] = finalStart;
        res[1] = finalEnd;
        return res;
    }

    public LatLng[] calculateTangentPoints(LatLng centerPoint, LatLng edgePoint, double distanceX) {
        double latA = edgePoint.latitude;
        double lonA = edgePoint.longitude;
        double latC = centerPoint.latitude;
        double lonC = centerPoint.longitude;
        double earthRadius = 6371; // Earth's radius in kilometers
        double radianLatA = toRadians(latA);
        double radianLonA = toRadians(lonA);
        double radianLatC = toRadians(latC);
        double radianLonC = toRadians(lonC);
        double deltaLat = radianLatA - radianLatC;
        double deltaLon = radianLonA - radianLonC;

        // Calculate the distance between A and C (the circle radius) using haversine formula
        double a = pow(sin(deltaLat / 2), 2) + cos(radianLatA) * cos(radianLatC) * pow(sin(deltaLon / 2), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        double radius = earthRadius * c;

        // Calculate the initial bearing from point A to point C
        double initialBearing = atan2(sin(deltaLon) * cos(radianLatC),
                cos(radianLatA) * sin(radianLatC) - sin(radianLatA) * cos(radianLatC) * cos(deltaLon));
        initialBearing = (toDegrees(initialBearing) + 360) % 360;

        // Calculate the tangent bearings from point A
        double tangentBearing1 = (initialBearing + 90) % 360;
        double tangentBearing2 = (initialBearing + 270) % 360;

        // Calculate the coordinates of points B1 and B2 using the tangent bearings and distance x
        double[][] result = new double[2][2];
        result[0] = calculateDestinationPoint(latA, lonA, tangentBearing1, distanceX);
        result[1] = calculateDestinationPoint(latA, lonA, tangentBearing2, distanceX);
        LatLng[] res = new LatLng[2];
        res[0] = new LatLng(result[0][0], result[0][1]);
        res[1] = new LatLng(result[1][0], result[1][1]);
        return res;
    }

    public static double[] calculateDestinationPoint(double lat, double lon, double bearing, double distance) {
        double earthRadius = 6371; // Earth's radius in kilometers
        double radianLat = toRadians(lat);
        double radianLon = toRadians(lon);
        double radianBearing = toRadians(bearing);
        double angularDistance = distance / earthRadius;
        double latResult = asin(sin(radianLat) * cos(angularDistance) +
                cos(radianLat) * sin(angularDistance) * cos(radianBearing));
        double lonResult = radianLon + atan2(sin(radianBearing) * sin(angularDistance) * cos(radianLat),
                cos(angularDistance) - sin(radianLat) * sin(latResult));
        return new double[]{toDegrees(latResult), toDegrees(lonResult)};
    }

}