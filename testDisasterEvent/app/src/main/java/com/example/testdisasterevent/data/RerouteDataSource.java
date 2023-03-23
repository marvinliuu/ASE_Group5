package com.example.testdisasterevent.data;

import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testdisasterevent.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Route;

public class RerouteDataSource {
    public interface RouteCallback {
        void onRouteReady(DirectionsResult result);
        void onError(String errorMessage);
    }

    private GeoApiContext context;

    public RerouteDataSource(GeoApiContext context) {
        this.context = context;
    }

    public DirectionsRoute findMinTimeRoute(List<LatLng> exits, LatLng currentLocation) throws InterruptedException, ApiException, IOException {
        // 初始化最短路线时间为一个很大的数
        Duration shortestDuration = new Duration();
        shortestDuration.inSeconds = Integer.MAX_VALUE;
        DirectionsRoute optimalResult = new DirectionsRoute();
        String org = currentLocation.toString();
        // 遍历每个出口
        for (LatLng exitLocation : exits) {
            String des = exitLocation.toString();
            // 创建 Directions API 请求
            DirectionsApiRequest directionsRequest = new DirectionsApiRequest(context);

            // 设置请求参数
            directionsRequest.origin(org.substring(10, org.length() - 1));
            directionsRequest.destination(des.substring(10, org.length() - 1));
            directionsRequest.mode(TravelMode.WALKING);

            // 发送请求并获取返回结果
            DirectionsResult directionsResult = directionsRequest.await();

            // 获取路线时间
            if (directionsResult.routes.length > 0) {
                DirectionsRoute route = directionsResult.routes[0];
                DirectionsLeg leg = route.legs[0];
                Duration duration = leg.duration;

                // 如果当前路线时间比之前的最短路线时间更短，就更新最短路线时间
                if (duration.inSeconds < shortestDuration.inSeconds) {
                    shortestDuration = duration;
                    optimalResult = directionsResult.routes[0];
                }
            }
        }

        // 最短路线时间即为从当前位置到耗时最短的出口的路线时间
        String time = shortestDuration.humanReadable;
        return optimalResult;
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
                errorMessage = "The destination is too close, there is no road found";
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
