package com.example.testdisasterevent.data;

import android.util.Log;

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.data.Result;

import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.data.model.HospitalDetails;
import com.example.testdisasterevent.data.model.ReportFromCitizen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;


public class ReportDataSource {
    private static DatabaseReference UserDatabase;

    /**
     * Date: 23.04.14
     * Function: submit the citizen report into database
     * Author: Siyu Liao
     * Version: Week 12
     */
    public Result<String> SubmitCitizenReport(ReportFromCitizen reportData) {
        try {
            UserDatabase = FirebaseDatabase.getInstance().getReference().child("Report");

            UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = "report";

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("type", reportData.getDisasterType());
                    userData.put("description", reportData.getOtherInfo());
                    userData.put("rtime", reportData.getTimestamp());
                    userData.put("injury", reportData.getInjuredNum());
                    userData.put("latitude", reportData.getLatitude());
                    userData.put("longitude", reportData.getLongitude());
                    userData.put("state", reportData.getReportState());
                    userData.put("imageURL",reportData.getImageURL());
                    userData.put("location", reportData.getLocation());

                    name += reportData.getTimestamp();
                    UserDatabase.child(name).setValue(userData);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("report", "report database error", databaseError.toException());
                }
            });
            return new Result.Success<>("report from citizen push Success");
        } catch (Exception e) {
            return new Result.Error(new IOException("Error report submit", e));
        }
    }

    /**
     * Date: 23.04.14
     * Function: submit the garda report into database
     * Author: Siyu Liao
     * Version: Week 12
     */
    public Result<String> SubmitGardaReport(DisasterDetail disasterData) {
        try {
            UserDatabase = FirebaseDatabase.getInstance().getReference().child("DisasterInfo");

            String name = "Disaster";

            Map<String, Object> userData = new HashMap<>();
            userData.put("type", disasterData.getDisasterType());
            userData.put("GardaID", disasterData.getGardaUID());
            userData.put("happenTime", disasterData.getHappenTime());
            userData.put("injury", disasterData.getInjureNum());
            userData.put("isUpdate", disasterData.getUpdate());
            userData.put("latitude", disasterData.getLatitude());
            userData.put("longitude", disasterData.getLongitude());
            userData.put("radius", disasterData.getRadius());
            userData.put("location", disasterData.getLocation());

            name += disasterData.getHappenTime();
            UserDatabase.child(name).setValue(userData);

//            UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    String name = "Disaster";
//
//                    Map<String, Object> userData = new HashMap<>();
//                    userData.put("type", disasterData.getDisasterType());
//                    userData.put("GardaID", disasterData.getGardaUID());
//                    userData.put("happenTime", disasterData.getHappenTime());
//                    userData.put("injury", disasterData.getInjureNum());
//                    userData.put("isUpdate", disasterData.getUpdate());
//                    userData.put("latitude", disasterData.getLatitude());
//                    userData.put("longitude", disasterData.getLongitude());
//                    userData.put("radius", disasterData.getRadius());
//                    userData.put("location", disasterData.getLocation());
//
//                    name += disasterData.getHappenTime();
//                    UserDatabase.child(name).setValue(userData);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.w("disaster", "disaster database error", databaseError.toException());
//                }
//            });
            return new Result.Success<>("report from garda push Success");
        } catch (Exception e) {
            return new Result.Error(new IOException("Error garda report submit", e));
        }
    }

    /**
     * Date: 23.04.14
     * Function: transfer report to disaster
     * Author: Siyu Liao
     * Version: Week 12
     */
    public DisasterDetail Report2Disaster(ReportFromCitizen reportData) {
        DisasterDetail disasterData = new DisasterDetail(
                reportData.getRadius(),
                reportData.getLocation(),
                reportData.getTimestamp(),
                reportData.getLatitude(),
                reportData.getLongitude(),
                reportData.getInjuredNum(),
                reportData.getDisasterType(),
                reportData.getAccountUID(),
                1
        );
        return disasterData;
    }
}

