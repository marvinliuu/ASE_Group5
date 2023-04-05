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

    public Result<String> SubmitCitizenReport(ReportFromCitizen reportData) {
        try {
            UserDatabase = FirebaseDatabase.getInstance().getReference().child("Report");

            UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    String name = "report";
                    String timestampString = getCurrentTime();


                    Map<String, String> userData = new HashMap<>();
                    userData.put("type", reportData.getDisasterType());
                    userData.put("description", reportData.getOtherInfo());
                    userData.put("rtime", timestampString);
                    userData.put("injury", Integer.toString(reportData.getInjuredNum()));
                    userData.put("latitude", Double.toString(reportData.getLatitude()));
                    userData.put("longitude", Double.toString(reportData.getLongitude()));
                    userData.put("state", Integer.toString(reportData.getReportState()));


                    timestampString += "000";
                    name += timestampString;

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

    public Result<String> SubmitGardaReport(DisasterDetail disasterData) {
        try {
            UserDatabase = FirebaseDatabase.getInstance().getReference().child("DisasterInfo");

            UserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = "Disaster";


                    String timestampString = getCurrentTime();


                    Map<String, String> userData = new HashMap<>();
                    userData.put("type", disasterData.getDisasterType());
                    userData.put("GardaID", disasterData.getGardaUID());
                    userData.put("happenTime", timestampString);
                    userData.put("isUpdate", Integer.toString(disasterData.getUpdate()));
                    userData.put("latitude", Double.toString(disasterData.getLatitude()));
                    userData.put("longitude", Double.toString(disasterData.getLongitude()));
                    userData.put("radius", Integer.toString(disasterData.getRadius()));
                    userData.put("location", disasterData.getLocation());


                    name += timestampString;
                    UserDatabase.child(name).setValue(userData);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("disaster", "disaster database error", databaseError.toException());
                }
            });


            return new Result.Success<>("report from garda push Success");
        } catch (Exception e) {
            return new Result.Error(new IOException("Error garda report submit", e));
        }
    }


    public DisasterDetail Report2Disaster(ReportFromCitizen reportData) {


        DisasterDetail disasterData = new DisasterDetail(
                reportData.getRadius(),
                reportData.getLocation(),
                getCurrentTime(),
                reportData.getLatitude(),
                reportData.getLongitude(),
                reportData.getDisasterType(),
                reportData.getAccountUID(),
                1
        );

        return disasterData;

    }


    public String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);

        Timestamp timestamp = new Timestamp(currentDate.getTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestampString = dateFormat.format(timestamp);
        return timestampString;
    }
}

