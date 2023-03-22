package com.example.testdisasterevent.data;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;

public class RerouteDataSource {
    public interface RouteCallback {
        void onRouteReady(DirectionsResult result);
        void onError(String errorMessage);
    }

    private GeoApiContext context;

    public RerouteDataSource(GeoApiContext context) {
        this.context = context;
    }

    public void getRoute(LatLng origin, LatLng destination, TravelMode mode, RouteRestriction[] restrictions, RouteCallback callback) {
        new DirectionsTask(context, origin, destination, mode, restrictions, callback).execute();
    }

    private static class DirectionsTask extends AsyncTask<Void, Void, DirectionsResult> {

        private GeoApiContext context;
        private LatLng origin;
        private LatLng destination;
        private TravelMode mode;
        private RouteRestriction[] restrictions;
        private RouteCallback callback;
        private String errorMessage = null;

        public DirectionsTask(GeoApiContext context, LatLng origin, LatLng destination, TravelMode mode, RouteRestriction[] restrictions, RouteCallback callback) {
            this.context = context;
            this.origin = origin;
            this.destination = destination;
            this.mode = mode;
            this.restrictions = restrictions;
            this.callback = callback;
        }

        @Override
        protected DirectionsResult doInBackground(Void... params) {
            try {
                String org = origin.toString();
                String des = destination.toString();
                DirectionsApiRequest request = DirectionsApi.newRequest(context)
                        .origin(org.substring(10, org.length() - 1))
                        .destination(des.substring(10, org.length() - 1))
                        .mode(mode)
                        .avoid(restrictions);
                return request.await();
            } catch (InterruptedException | IOException | com.google.maps.errors.ApiException e) {
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onPostExecute(DirectionsResult result) {
            if (errorMessage != null) {
                callback.onError(errorMessage);
            } else {
                callback.onRouteReady(result);
            }
        }
    }
}
