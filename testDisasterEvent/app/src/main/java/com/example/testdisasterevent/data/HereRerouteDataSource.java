package com.example.testdisasterevent.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.data.model.DisasterDetail;
import com.google.android.gms.maps.model.LatLng;
import com.here.sdk.core.Color;
import com.here.sdk.core.GeoBox;
import com.here.sdk.core.GeoCircle;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoPolygon;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.Point2D;
import com.here.sdk.core.engine.SDKOptions;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.core.threading.TaskHandle;
import com.here.sdk.mapview.MapCamera;
import com.here.sdk.mapview.MapImage;
import com.here.sdk.mapview.MapImageFactory;
import com.here.sdk.mapview.MapMarker;
import com.here.sdk.mapview.MapMeasure;
import com.here.sdk.mapview.MapPolyline;
import com.here.sdk.mapview.MapView;
import com.here.sdk.routing.AvoidanceOptions;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.CarOptions;
import com.here.sdk.routing.Maneuver;
import com.here.sdk.routing.ManeuverAction;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Section;
import com.here.sdk.routing.SectionNotice;
import com.here.sdk.routing.Span;
import com.here.sdk.routing.TrafficSpeed;
import com.here.sdk.routing.Waypoint;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HereRerouteDataSource {
    private final RoutingEngine routingEngine;
    private List<LatLng> points = new ArrayList<>();
    public MutableLiveData<List<LatLng>> livePointData = new MutableLiveData<>();

    public HereRerouteDataSource() {
        try {
            routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            throw new RuntimeException("Initialization of RoutingEngine failed: " + e.error.name());
        }
    }


    public void addRoute(LatLng startPoint, LatLng desPoint, List<GeoBox> geoBoxes) throws InstantiationErrorException {

        Waypoint startWaypoint = new Waypoint(new GeoCoordinates(startPoint.latitude, startPoint.longitude));
        Waypoint destinationWaypoint = new Waypoint(new GeoCoordinates(desPoint.latitude, desPoint.longitude));

        List<Waypoint> waypoints =
                new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));

        CarOptions carOptions = new CarOptions();
        AvoidanceOptions avoidanceOptions = new AvoidanceOptions();
//        geoBoxes.add(new GeoBox(new GeoCoordinates(53.34516591066565,-6.2531348024413616), new GeoCoordinates(53.34389407985007,-6.255265165785585)));

        avoidanceOptions.avoidAreas = geoBoxes;
        carOptions.avoidanceOptions = avoidanceOptions;

        routingEngine.calculateRoute(
                waypoints,
                carOptions,
                new CalculateRouteCallback() {
                    @Override
                    public void onRouteCalculated(RoutingError routingError, List<Route> routes) {
                        if (routingError == null) {
                            Route route = routes.get(0);
                            showRouteOnMap(route);
                        } else {
                            System.out.println("Error while calculating a route:" + routingError.toString());
                        }
                    }
                });
    }

    private void showRouteOnMap(Route route) {
        // Show route as polyline.
        GeoPolyline routeGeoPolyline = route.getGeometry();

        float widthInPixels = 20;
        MapPolyline routeMapPolyline = new MapPolyline(routeGeoPolyline,
                widthInPixels,
                Color.valueOf(0, 0.56f, 0.54f, 0.63f)); // RGBA

        for (int i = 0; i < routeMapPolyline.getGeometry().vertices.size(); i++) {
            GeoCoordinates temp = routeMapPolyline.getGeometry().vertices.get(i);
            points.add(new LatLng(temp.latitude, temp.longitude));
        }
        livePointData.setValue(points);

    }

}
