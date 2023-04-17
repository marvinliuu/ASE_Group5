package com.example.testdisasterevent.data;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.data.model.FireFighterDetail;
import com.example.testdisasterevent.data.model.GardaDetail;
import com.example.testdisasterevent.data.model.HospitalDetails;
import com.example.testdisasterevent.data.model.ReportFromCitizen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class HosAllocationDataSource {
    public HospitalDetails[] hosdetails;
    public GardaDetail[] gardaDetails;
    public FireFighterDetail[] fireBrigadeDetails;
    private static double EARTH_RADIUS = 6378.137;	//earth radius
    private DisasterDetail reportData;

    public HosAllocationDataSource() {

        getHospitalData();
        getGardaData();
        getFireBrigadeData();
    }

    public void AllocationSubmit(DisasterDetail data){
        reportData = data;
    }

    /**
     * Date: 23.04.14
     * Function: get hospital resource infos from database
     * Author: Siyu Liao
     * Version: Week 12
     */
    public LiveData<HospitalDetails[]> getHospitalData() {

        final MutableLiveData<HospitalDetails[]> hospitalLiveData = new MutableLiveData<>();
        if (hosdetails != null) {
            hospitalLiveData.setValue(hosdetails);
            return hospitalLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Hospital");

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                hosdetails = new HospitalDetails[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    int hid = postSnapshot.child("hid").getValue(int.class);
                    int n_ambulance = postSnapshot.child("n_ambulance").getValue(int.class);
                    int n_ava_ambulance = postSnapshot.child("n_ava_ambulance").getValue(int.class);
                    int n_doctor = postSnapshot.child("n_doctor").getValue(int.class);
                    int n_ava_doctor = postSnapshot.child("n_ava_doctor").getValue(int.class);
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String name = postSnapshot.child("name").getValue(String.class);
                    hosdetails[count++] = new HospitalDetails(hid, name, n_ambulance, n_ava_ambulance, n_doctor, n_ava_doctor, latitude, longitude);
                }
                hospitalLiveData.setValue(hosdetails);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
        Log.d("allocation", "get hospital data");
        return hospitalLiveData;
    }

    /**
     * Date: 23.04.14
     * Function: get garda resource infos from database
     * Author: Siyu Liao
     * Version: Week 12
     */
    public LiveData<GardaDetail[]> getGardaData() {

        final MutableLiveData<GardaDetail[]> gardaLiveData = new MutableLiveData<>();
        if (gardaDetails != null) {
            gardaLiveData.setValue(gardaDetails);
            return gardaLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Garda");

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                gardaDetails = new GardaDetail[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    int hid = postSnapshot.child("gid").getValue(int.class);
                    int n_car = postSnapshot.child("n_car").getValue(int.class);
                    int n_ava_car = postSnapshot.child("n_ava_car").getValue(int.class);
                    int n_police = postSnapshot.child("n_police").getValue(int.class);
                    int n_ava_police = postSnapshot.child("n_ava_police").getValue(int.class);
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String name = postSnapshot.child("name").getValue(String.class);
                    gardaDetails[count++] = new GardaDetail(hid, n_car, n_ava_car, n_police, n_ava_police,  latitude, longitude,name);
                }
                gardaLiveData.setValue(gardaDetails);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
        Log.d("allocation", "get garda data");
        return gardaLiveData;
    }

    /**
     * Date: 23.04.14
     * Function: get fire brigade resource infos from database
     * Author: Siyu Liao
     * Version: Week 12
     */
    public LiveData<FireFighterDetail[]> getFireBrigadeData() {

        final MutableLiveData<FireFighterDetail[]> firefighterLiveData = new MutableLiveData<>();
        if (fireBrigadeDetails != null) {
            firefighterLiveData.setValue(fireBrigadeDetails);
            return firefighterLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("FireBrigade");

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                fireBrigadeDetails = new FireFighterDetail[len];
                int count = 0;
                // Process the retrieved data here
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    int fid = postSnapshot.child("fid").getValue(int.class);
                    int n_truck = postSnapshot.child("n_truck").getValue(int.class);
                    int n_ava_truck = postSnapshot.child("n_ava_truck").getValue(int.class);
                    int n_firefighter = postSnapshot.child("n_firefighter").getValue(int.class);
                    int n_ava_firefighter = postSnapshot.child("n_ava_firefighter").getValue(int.class);
                    float latitude = postSnapshot.child("latitude").getValue(float.class);
                    float longitude = postSnapshot.child("longitude").getValue(float.class);
                    String name = postSnapshot.child("name").getValue(String.class);
                    fireBrigadeDetails[count++] = new FireFighterDetail(fid, n_truck, n_ava_truck, n_firefighter, n_ava_firefighter,  latitude, longitude,name);
                }
                firefighterLiveData.setValue(fireBrigadeDetails);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
        Log.d("allocation", "get fire data");
        return firefighterLiveData;
    }


    /**
     * Date: 23.04.14
     * Function: write info back to database
     * Author: Siyu Liao
     * Version: Week 12
     */
    private void writebackToDatabase(List<int[]> info,String dataBaseName,String dataAvaItem) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(dataBaseName);
        for (int[] in : info) {
            String child = dataBaseName + Integer.toString(in[0]);
            databaseRef.child(child).child(dataAvaItem).setValue(in[1]);
        }
    }

    /**
     * Date: 23.04.14
     * Function: write task infos to database
     * Author: Siyu Liao
     * Version: Week 12
     */
    private void TaskGen(int need_resource,int officerType){
        Log.d("task", "generate "+Integer.toString(need_resource)+" "+Integer.toString(officerType)+" task");
        if(need_resource == 0) { return; }
        DatabaseReference avaOfficer = FirebaseDatabase.getInstance().getReference().child("AvailableOfficer");
        DatabaseReference task = FirebaseDatabase.getInstance().getReference().child("TaskInfo");
        avaOfficer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(count == need_resource) { break; }

                    int type = dataSnapshot.child("type").getValue(int.class);
                    if(type == officerType){
                        Long uid = dataSnapshot.child("uid").getValue(Long.class);
                        Map<String, Object> taskInfo = new HashMap<>();
                        //TODO 将ReportData传入后生成task
                        taskInfo.put("disasterType", reportData.getDisasterType());
                        taskInfo.put("injury", reportData.getInjureNum());
                        taskInfo.put("latitude", reportData.getLatitude());
                        taskInfo.put("longitude", reportData.getLongitude());
                        taskInfo.put("uid", uid);
                        taskInfo.put("otime", reportData.getHappenTime());
                        taskInfo.put("location", reportData.getLocation());
                        taskInfo.put("state", "0");
                        taskInfo.put("task", "Please go to "+ reportData.getLocation());
                        String taskKey = task.push().getKey();
                        task.child(taskKey).setValue(taskInfo);
                        avaOfficer.child(dataSnapshot.getKey()).removeValue();
                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    /**
     * Date: 23.04.14
     * Function: evaluate generator for resource information
     * Author: Siyu Liao
     * Version: Week 12
     */
    public void evaluateAll(double latitude, double longitude, int need_ambulance, int need_car, int need_fireTruck){
        evaluateHosResource(latitude,longitude,need_ambulance);
        evaluateGardaResource(latitude,longitude,need_car);
        evaluateFirebrigadeResource(latitude,longitude,need_fireTruck);
    }

    /**
     * Date: 23.04.14
     * Function: evaluate hospital resources
     * Author: Siyu Liao
     * Version: Week 12
     */
    @TargetApi(Build.VERSION_CODES.N)
    public void evaluateHosResource(double latitude, double longitude, int need_ambulance) {
        Log.d("task", "evaluate hospital resource");
        Map<Integer, Double> disIndex = new HashMap<Integer, Double>();
        double targetLat = Math.toRadians(latitude);
        double targetLong = Math.toRadians(longitude);

        for (int i = 0; i < hosdetails.length; i++) {
            double sourLat = Math.toRadians(hosdetails[i].getLatitude());
            double sourLong = Math.toRadians(hosdetails[i].getLongitude());
            double s = calcDistance(targetLat, targetLong, sourLat, sourLong);
            s = Math.round(s * 10000);
            disIndex.put(i, s);
        }

        Queue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int)(disIndex.get(o1) - disIndex.get(o2));
            }
        });

        for (int i = 0; i < hosdetails.length; i++) {
            queue.add(i);
        }

        List<int[]> res = new ArrayList<>();
        while (need_ambulance > 0 && queue.size() > 0) {
            int index = queue.peek();
            queue.poll();
            int[] temp = new int[3];
            int hid = hosdetails[index].getHid();
            int avaAmbulance = hosdetails[index].getAvaAmbulance();
            if (avaAmbulance == 0) continue;
            if (need_ambulance >= avaAmbulance) {
                need_ambulance -= avaAmbulance;
                temp[0] = hid;
                temp[2] = index;
            }
            else {
                temp[0] = hid;
                temp[1] = hosdetails[index].getAvaAmbulance() - need_ambulance;
                temp[2] = index;
                need_ambulance = 0;
            }
            res.add(temp);
        }
        writebackToDatabase(res,"hospital","n_ava_ambulance");
        TaskGen(need_ambulance,2);
    }

    /**
     * Date: 23.04.14
     * Function: evaluate garda resources
     * Author: Siyu Liao
     * Version: Week 12
     */
    public void evaluateGardaResource(double latitude, double longitude, int need_car) {
        Log.d("task", "evaluate garda resource");
        Map<Integer, Double> disIndex = new HashMap<Integer, Double>();
        double targetLat = Math.toRadians(latitude);
        double targetLong = Math.toRadians(longitude);

        for (int i = 0; i < gardaDetails.length; i++) {
            double sourLat = Math.toRadians(gardaDetails[i].getLatitude());
            double sourLong = Math.toRadians(gardaDetails[i].getLongitude());
            double s = calcDistance(targetLat, targetLong, sourLat, sourLong);
            disIndex.put(i, s);
        }

        Queue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int)(disIndex.get(o1) - disIndex.get(o2));
            }
        });

        for (int i = 0; i < gardaDetails.length; i++) {
            queue.add(i);
        }

        List<int[]> res = new ArrayList<>();
        while (need_car > 0 && queue.size() > 0) {
            int index = queue.peek();
            queue.poll();
            int[] temp = new int[3];
            int gid = gardaDetails[index].getGid();
            int avaCar = gardaDetails[index].getN_ava_car();
            if (avaCar == 0) continue;
            if (need_car >= avaCar) {
                need_car -= avaCar;
                temp[0] = gid;
                temp[2] = index;
            } else {
                temp[0] = gid;
                temp[1] = gardaDetails[index].getN_ava_car() - need_car;
                temp[2] = index;
                need_car = 0;
            }
            res.add(temp);
        }
        writebackToDatabase(res,"Garda","n_ava_car");
        TaskGen(need_car,1);
    }

    /**
     * Date: 23.04.14
     * Function: evaluate fire brigade resources
     * Author: Siyu Liao
     * Version: Week 12
     */
    public void evaluateFirebrigadeResource(double latitude, double longitude, int need_truck) {

        Log.d("task", "evaluate fire resource");
        Map<Integer, Double> disIndex = new HashMap<Integer, Double>();
        double targetLat = Math.toRadians(latitude);
        double targetLong = Math.toRadians(longitude);

        for (int i = 0; i < fireBrigadeDetails.length; i++) {
            double sourLat = Math.toRadians(fireBrigadeDetails[i].getLatitude());
            double sourLong = Math.toRadians(fireBrigadeDetails[i].getLongitude());
            double s = calcDistance(targetLat, targetLong, sourLat, sourLong);
            disIndex.put(i, s);
        }

        Queue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int)(disIndex.get(o1) - disIndex.get(o2));
            }
        });

        for (int i = 0; i < fireBrigadeDetails.length; i++) {
            queue.add(i);
        }

        List<int[]> res = new ArrayList<>();
        while (need_truck > 0 && queue.size() > 0) {
            int index = queue.peek();
            queue.poll();
            int[] temp = new int[3];
            int fid = fireBrigadeDetails[index].getFid();
            int ava_truck = fireBrigadeDetails[index].getN_ava_truck();
            if (ava_truck == 0) continue;
            if (need_truck >= ava_truck) {
                need_truck -= ava_truck;
                temp[0] = fid;
                temp[2] = index;
            } else {
                temp[0] = fid;
                temp[1] = gardaDetails[index].getN_ava_car() - need_truck;
                temp[2] = index;
                need_truck = 0;
            }
            res.add(temp);
        }
        writebackToDatabase(res,"FireBrigade","n_ava_truck");
        TaskGen(need_truck,3);
    }

    /**
     * Date: 23.04.14
     * Function: calculate the distance between two points
     * Author: Siyu Liao
     * Version: Week 12
     */
    private double calcDistance(double targetLat, double targetLong, double sourLat, double sourLong) {
        double a = targetLat - sourLat;
        double b = targetLong - sourLong;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(targetLat)*Math.cos(sourLat)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000);
        return s;
    }
}
