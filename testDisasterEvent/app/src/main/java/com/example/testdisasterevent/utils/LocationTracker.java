package com.example.testdisasterevent.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.*;

public class LocationTracker {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public interface LocationUpdateListener {
        void onLocationUpdated(Location location);
    }

    private LocationUpdateListener locationUpdateListener;

    public LocationTracker(Context context, LocationUpdateListener locationUpdateListener) {
        this.context = context;
        this.locationUpdateListener = locationUpdateListener;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        createLocationRequest();
        createLocationCallback();
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null && locationUpdateListener != null) {
                        locationUpdateListener.onLocationUpdated(location);
                    }
                }
            }
        };
    }

    public void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 在这里请求权限，或者在调用此类的 Activity/Fragment 中请求
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
