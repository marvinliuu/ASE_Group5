package com.example.testdisasterevent.ui.disaster;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdisasterevent.algorithms.FindMaxAreaAlgorithm;
import com.example.testdisasterevent.data.DisasterDataSource;
import com.example.testdisasterevent.data.HosAllocationDataSource;
import com.example.testdisasterevent.data.LoginRepository;
import com.example.testdisasterevent.data.RerouteDataSource;
import com.example.testdisasterevent.data.RoadsInfoDatasource;
import com.example.testdisasterevent.data.TaskDataSource;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.data.model.HospitalDetails;
import com.example.testdisasterevent.data.model.TaskDetail;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

public class DisasterViewModel extends ViewModel {
    private DisasterDataSource disasterDataSource;  // Type: DisasterDataSource, used for accessing disaster-related data
    private TaskDataSource taskDataSource;  // Type: TaskDataSource, used for accessing task-related data
    private RoadsInfoDatasource roadsInfoDatasource;  // Type: RoadsInfoDatasource, used for accessing road-related data
    private RerouteDataSource rerouteDataSource;  // Type: RerouteDataSource, used for accessing reroute-related data
    private FindMaxAreaAlgorithm findMaxAreaAlgorithm;  // Type: FindMaxAreaAlgorithm, used for finding the maximum area of a disaster circle


    public LiveData<DisasterDetail[]> getDisasterDetails() {
        return disasterDataSource.getDisasterDetails();
    }

    public LiveData<TaskDetail[]> getTaskDetails() {
        return taskDataSource.getTaskDetails();
    }

    public LiveData<List<LatLng>> getNearbyRoads(double latitude, double longitude, double radius) {
        return roadsInfoDatasource.getNearbyRoads(latitude, longitude, radius);
    }

    public LiveData<List<LatLng>> getUserNearbyRoads(double latitude, double longitude) {
        return roadsInfoDatasource.getUserNearbyRoads(latitude, longitude);
    }

    public List<LatLng> getSelectedPoints(double latitude, double longitude, double radius) {
        return roadsInfoDatasource.selectNearByLocation(latitude, longitude, radius);
    }

    public DirectionsRoute findOptimalRoute(List<LatLng> exits, LatLng currentLocation) throws InterruptedException, ApiException, IOException {
        return rerouteDataSource.findMinTimeRoute(exits, currentLocation);
    }

    public DisasterViewModel() {
        disasterDataSource = new DisasterDataSource();
        taskDataSource = new TaskDataSource();
        //hosAllocationDataSource = new HosAllocationDataSource();
        roadsInfoDatasource = new RoadsInfoDatasource();
        // Set up the GeoApiContext with your API key
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDrYjvowVSGRHTyi5vO7CZx2Py32G1BoaY")
                .build();
        rerouteDataSource = new RerouteDataSource(context);
        findMaxAreaAlgorithm = new FindMaxAreaAlgorithm();
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


    public List<LatLng> selectEntries(Set<LatLng> candidate, double radius) {
        int pointsNum = (int)(radius / 20);
        pointsNum = pointsNum < 2 ? 2 : pointsNum;
        pointsNum = pointsNum > candidate.size() ? candidate.size() : pointsNum;
        List<LatLng> candidateList = new ArrayList<>(candidate);
        if (pointsNum == 2) return selectTwoEntries(candidate, radius);
        else {
            List<LatLng> res = findMaxAreaAlgorithm.findMaxAreaPoints(candidateList, pointsNum);
            return res;
        }
    }

    public List<LatLng> selectTwoEntries(Set<LatLng> candidate, double radius) {
        List<LatLng> candidateList = new ArrayList<>(candidate);
        LatLng[] candidatePoint = new LatLng[candidate.size()];
        for (int i = 0; i < candidatePoint.length; i ++) candidatePoint[i] = candidateList.get(i);
        LatLng start = new LatLng(0, 0), end = new LatLng(0, 0);
        double max_distance = 0;
        double current_distance;
        for (int i = 0; i < candidatePoint.length; i ++) {
            for (int j = 0; j < candidatePoint.length; j ++) {
                current_distance = distance(candidatePoint[i], candidatePoint[j]);
                if (current_distance > max_distance) {
                    start = candidatePoint[i];
                    end = candidatePoint[j];
                    max_distance = current_distance;
                }
            }
        }
        List<LatLng> res = new ArrayList<>();
        res.add(start);
        res.add(end);
        return res;
    }

}