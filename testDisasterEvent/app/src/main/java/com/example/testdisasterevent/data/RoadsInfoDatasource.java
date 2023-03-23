package com.example.testdisasterevent.data;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.data.model.HostipalDetails;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RoadsInfoDatasource {
    private static final String ROADS_API_BASE = "https://roads.googleapis.com/v1/nearestRoads";
    private static final String KEY = "AIzaSyDrYjvowVSGRHTyi5vO7CZx2Py32G1BoaY"; // replace with your own API key

    public List<LatLng> selectNearByLocation (double latitude, double longitude, double radius) {
        // Define the center point of the circle as a LatLng object
        LatLng center = new LatLng(latitude, longitude);

        // Define the number of points you want to generate on the circle
        int numPoints = 8;

        // Calculate the positions on the circle
        List<LatLng> circlePoints = new ArrayList<LatLng>();
        for (int i = 0; i < numPoints; i++) {
            double angle = i * 360.0 / numPoints;
            LatLng pointOnCircle = SphericalUtil.computeOffset(center, radius, angle);
            circlePoints.add(pointOnCircle);
        }
        return circlePoints;
    }

    public LiveData<List<LatLng>> getNearbyRoads(double latitude, double longitude, double radius) {
        List<LatLng> pathPoints = selectNearByLocation(latitude, longitude, radius);
        MutableLiveData<List<LatLng>> roadInfos = new MutableLiveData<>();
        List<LatLng> pointSet = new ArrayList<>();
        new AsyncTask<Void, Void, List<LatLng>>() {
            @Override
            protected List<LatLng> doInBackground(Void... params) {

                // Build the API request URL
                StringBuilder urlBuilder = new StringBuilder(ROADS_API_BASE);
                urlBuilder.append("?points=");
                for (LatLng point : pathPoints) {
                    urlBuilder.append(point.latitude).append(",").append(point.longitude).append("|");
                }
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
                urlBuilder.append("&key=").append(KEY);

                try {
                    // Send the API request
                    URL url = new URL(urlBuilder.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    // Parse the API response
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder sb = new StringBuilder();
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }

                    JSONObject response = new JSONObject(sb.toString());
                    JSONArray snappedPoints = response.getJSONArray("snappedPoints");

                    for (int i = 0; i < snappedPoints.length(); i++) {
                        JSONObject snappedPoint = snappedPoints.getJSONObject(i);
                        JSONObject latlongJson = snappedPoint.getJSONObject("location");
                        Double latitude = latlongJson.getDouble("latitude");
                        Double longitude = latlongJson.getDouble("longitude");
                        pointSet.add(new LatLng(latitude, longitude));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return pointSet;
            }

            @Override
            protected void onPostExecute(List<LatLng> pointSet) {
                // Use the road names in your app as needed
            }
        }.execute();
        roadInfos.setValue(pointSet);
    return roadInfos;
    }

    public LiveData<List<LatLng>> getUserNearbyRoads(double latitude, double longitude) {
        MutableLiveData<List<LatLng>> userRoadInfos = new MutableLiveData<>();
        List<LatLng> pointSet = new ArrayList<>();
        new AsyncTask<Void, Void, List<LatLng>>() {
            @Override
            protected List<LatLng> doInBackground(Void... params) {

                // Build the API request URL
                StringBuilder urlBuilder = new StringBuilder(ROADS_API_BASE);
                urlBuilder.append("?points=");
                urlBuilder.append(latitude).append(",").append(longitude);
                urlBuilder.append("&key=").append(KEY);

                try {
                    // Send the API request
                    URL url = new URL(urlBuilder.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    // Parse the API response
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder sb = new StringBuilder();
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }

                    JSONObject response = new JSONObject(sb.toString());
                    JSONArray snappedPoints = response.getJSONArray("snappedPoints");

                    for (int i = 0; i < snappedPoints.length(); i++) {
                        JSONObject snappedPoint = snappedPoints.getJSONObject(i);
                        JSONObject latlongJson = snappedPoint.getJSONObject("location");
                        Double latitude = latlongJson.getDouble("latitude");
                        Double longitude = latlongJson.getDouble("longitude");
                        pointSet.add(new LatLng(latitude, longitude));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return pointSet;
            }

            @Override
            protected void onPostExecute(List<LatLng> pointSet) {
                // Use the road names in your app as needed
            }
        }.execute();
        userRoadInfos.setValue(pointSet);
        return userRoadInfos;
    }
}
