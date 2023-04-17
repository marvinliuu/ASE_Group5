package com.example.testdisasterevent.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.*;

// Class to track user's location using the Fused Location Provider
public class LocationTracker {
    // Request code for location permission
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    // Context of the calling component
    private Context context;
    // Fused Location Provider Client for location updates
    private FusedLocationProviderClient fusedLocationClient;
    // Location request object to store request configurations
    private LocationRequest locationRequest;
    // Location callback to handle location updates
    private LocationCallback locationCallback;

    // Interface to listen for location updates
    public interface LocationUpdateListener {
        // Method to handle location updates
        void onLocationUpdated(Location location);
    }

    // Location update listener to be implemented by the calling component
    private LocationUpdateListener locationUpdateListener;

    // Constructor of the LocationTracker class
    public LocationTracker(Context context, LocationUpdateListener locationUpdateListener) {
        this.context = context;
        this.locationUpdateListener = locationUpdateListener;
        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // Create the location request and location callback
        createLocationRequest();
        createLocationCallback();
    }

    // Method to create and configure the location request
    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        // Set the update interval for location updates
        locationRequest.setInterval(5000);
        // Set the fastest update interval that the app can handle
        locationRequest.setFastestInterval(30000);
        // Set the priority for high accuracy location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Method to create the location callback
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            // Method called when a location update is received
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // If there are no locations, return
                if (locationResult == null) {
                    return;
                }
                // Iterate through received locations and pass them to the listener
                for (Location location : locationResult.getLocations()) {
                    if (location != null && locationUpdateListener != null) {
                        locationUpdateListener.onLocationUpdated(location);
                    }
                }
            }
        };
    }

    // Method to start requesting location updates
    public void startLocationUpdates() {
        // Check if the app has location permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Request location updates with the configured request and callback
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    // Method to stop requesting location updates
    public void stopLocationUpdates() {
        // Remove location updates using the configured callback
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
