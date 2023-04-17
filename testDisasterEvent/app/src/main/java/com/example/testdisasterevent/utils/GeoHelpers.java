package com.example.testdisasterevent.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.example.testdisasterevent.data.HereRerouteDataSource;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.here.sdk.core.GeoBox;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.errors.InstantiationErrorException;

import java.util.ArrayList;
import java.util.List;

import io.opencensus.resource.Resource;

public class GeoHelpers {
    /**
     * Date: 23.04.12
     * Function: init here api sdk
     * Author: Siyu Liao
     * Version: Week 12
     */
    public void getRouteLatLngInfo (String start, String end, HereRerouteDataSource hereRerouteDataSource, DisasterDetail[] details) throws InstantiationErrorException {
        LatLng startPoint = getLocLatLng(start);
        LatLng endPoint = getLocLatLng(end);
        List<GeoBox> geoBoxes = calDisAreaGeoInfo(details);
        hereRerouteDataSource.addRoute(startPoint, endPoint, geoBoxes);
    }

    /**
     * Date: 23.04.17
     * Function: get location latitude & longitude info
     * Author: Siyu Liao
     * Version: Week 13
     */
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


    /**
     * Date: 23.04.17
     * Function: get location GeoBox Info
     * Author: Siyu Liao
     * Version: Week 13
     */
    List<GeoBox> calDisAreaGeoInfo(DisasterDetail[] details) {
        List<GeoBox> geoBoxes = new ArrayList<GeoBox>();
        if (details == null)
            return geoBoxes;
        for (DisasterDetail detail : details) {
            LatLng[] latLng = calculateDestinationLocations(new LatLng(detail.getLatitude(), detail.getLongitude()), detail.getRadius());
            geoBoxes.add(new GeoBox(new GeoCoordinates(latLng[0].latitude, latLng[0].longitude), new GeoCoordinates(latLng[1].latitude, latLng[1].longitude)));
        }
        return geoBoxes;
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


    /**
     * Date: 23.04.17
     * Function: add ori / des marker on the map
     * Author: Siyu Liao
     * Version: Week 13
     */
    public void addOnePointMarker(boolean ori, LatLng point, GoogleMap map, Resources resources) {
        IconSettingUtils iconSettingUtils = new IconSettingUtils();
        Bitmap bitmap = iconSettingUtils.setOriDesIcon(ori,resources);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaledWidth = width * 0.4f;
        float scaledHeight = height * 0.4f;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(point)
                .icon(markerIcon)
                .anchor(0.5f, 0.5f);;
        map.addMarker(markerOptions);
    }

}
