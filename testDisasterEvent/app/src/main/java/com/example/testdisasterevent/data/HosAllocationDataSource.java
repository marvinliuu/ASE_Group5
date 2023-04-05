package com.example.testdisasterevent.data;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testdisasterevent.data.model.DisasterDetail;
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
    public HospitalDetails[] details;
    private static double EARTH_RADIUS = 6378.137;	//earth radius
    private ReportFromCitizen reportData;

    public HosAllocationDataSource() {
        getHospitalData();
    }

    public void AllocationSubmit(ReportFromCitizen data){
        reportData = data;
    }
    public LiveData<HospitalDetails[]> getHospitalData() {

        final MutableLiveData<HospitalDetails[]> hospitalLiveData = new MutableLiveData<>();
        if (details != null) {
            hospitalLiveData.setValue(details);
            return hospitalLiveData;
        }

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Hospital");

        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int len = (int) dataSnapshot.getChildrenCount();
                details = new HospitalDetails[len];
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
                    details[count++] = new HospitalDetails(hid, name, n_ambulance, n_ava_ambulance, n_doctor, n_ava_doctor, latitude, longitude);
                }
                hospitalLiveData.setValue(details);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
        return hospitalLiveData;
    }

    private void writebackToDatabase(List<int[]> info) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Hospital");
        for (int[] in : info) {
            String child = "Hospital" + Integer.toString(in[0]);
            databaseRef.child(child).child("n_ava_ambulance").setValue(in[1]);
        }

    }

    private void doctorTaskGen(int need_ambulance){
        if(need_ambulance == 0) { return; }
        DatabaseReference avaOfficer = FirebaseDatabase.getInstance().getReference().child("AvailableOfficer");
        DatabaseReference task = FirebaseDatabase.getInstance().getReference().child("TaskInfo");
        avaOfficer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int count = 0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(count == need_ambulance) { break; }
                    long type = dataSnapshot.child("type").getValue(Long.class);
                    if(type == 2){
                        long uid = dataSnapshot.child("uid").getValue(Long.class);
                        Map<String, Object> taskInfo = new HashMap<>();
                        //TODO 将ReportData传入后生成task
//                        String location = getLocationString(reportData.getLatitude(), reportData.getLongitude());
                        taskInfo.put("disasterType", reportData.getDisasterType());
                        taskInfo.put("injury", reportData.getInjuredNum());
                        taskInfo.put("latitude", reportData.getLatitude());
                        taskInfo.put("longitude", reportData.getLongitude());
                        taskInfo.put("uid", uid);
                        taskInfo.put("otime", System.currentTimeMillis());
                        taskInfo.put("location", reportData.getLocation());
                        taskInfo.put("task", "Please go to "+ reportData.getLocation());
                        String taskKey = task.push().getKey();
                        task.child(taskKey).setValue(taskInfo);
                        avaOfficer.child(dataSnapshot.getKey()).removeValue();
                        count++;
                        Log.d("AAA",dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    @TargetApi(Build.VERSION_CODES.N)
    public void evaluateHosResource(double latitude, double longitude, int need_ambulance) {
        Map<Integer, Double> disIndex = new HashMap<Integer, Double>();
        double targetLat = Math.toRadians(latitude);
        double targetLong = Math.toRadians(longitude);

        for (int i = 0; i < details.length; i++) {
            double sourLat = Math.toRadians(details[i].getLatitude());
            double sourLong = Math.toRadians(details[i].getLongitude());
            double a = targetLat - sourLat;
            double b = targetLong - sourLong;
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                    Math.cos(targetLat)*Math.cos(sourLat)*Math.pow(Math.sin(b/2),2)));
            s = s * EARTH_RADIUS;
            s = Math.round(s * 10000);
            disIndex.put(i, s);
        }

        Queue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return (int)(disIndex.get(o1) - disIndex.get(o2));
            }
        });

        for (int i = 0; i < details.length; i++) {
            queue.add(i);
        }

        List<int[]> res = new ArrayList<>();
        while (need_ambulance > 0 && queue.size() > 0) {
            int index = queue.peek();
            queue.poll();
            int[] temp = new int[3];
            int hid = details[index].getHid();
            int avaAmbulance = details[index].getAvaAmbulance();
            if (avaAmbulance == 0) {
                continue;
            }
            if (need_ambulance >= avaAmbulance) {
                need_ambulance -= avaAmbulance;
                temp[0] = hid;
                temp[1] = 0;
                temp[2] = index;
            } else {
                temp[0] = hid;
                temp[1] = details[index].getAvaAmbulance() - need_ambulance;
                temp[2] = index;
                need_ambulance = 0;
            }
            res.add(temp);
        }
        writebackToDatabase(res);
        doctorTaskGen(need_ambulance);
    }

    public static String getLocationString(double lat, double lng) {
        String apiKey = "YOUR_API_KEY";
        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + apiKey;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            JSONObject jsonObject = new JSONObject(result.toString());
            JSONArray results = jsonObject.getJSONArray("results");
            if (results.length() > 0) {
                return results.getJSONObject(0).getString("formatted_address");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
