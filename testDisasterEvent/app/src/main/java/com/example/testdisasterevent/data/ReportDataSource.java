package com.example.testdisasterevent.data;

import android.util.Log;

import com.example.testdisasterevent.data.Result;
import com.example.testdisasterevent.data.model.DisasterDetail;
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
    private static DatabaseReference  UserDatabase;

    public Result<String> SubmitCitizenReport(ReportFromCitizen reportData) {
        try {
            UserDatabase = FirebaseDatabase.getInstance().getReference().child("Report");

            UserDatabase.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                                    String name="report";
                                    long currentTimeMillis = System.currentTimeMillis();
                                    Date currentDate = new Date(currentTimeMillis);

                                    Timestamp timestamp = new Timestamp(currentDate.getTime());

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                                    String timestampString = dateFormat.format(timestamp);


                                    Map<String, String> userData = new HashMap<>();
                                    userData.put("type", reportData.getDisasterType());
                                    userData.put("description", reportData.getOtherInfo());
                                    userData.put("rtime",timestampString);
                                    userData.put("injury", Integer.toString(reportData.getInjuredNum()));
                                    userData.put("latitude",Float.toString(reportData.getLatitude()));
                                    userData.put("longitude", Float.toString(reportData.getLongitude()));
                                    userData.put("radius",Float.toString(reportData.getRadius()));


                                    name += timestampString;
                                    UserDatabase.child(name).setValue(userData);
                                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("report", "report database error", databaseError.toException());
                }
            });


            return new Result.Success<>("report from citizen push Success" );
        } catch (Exception e) {
            return new Result.Error(new IOException("Error report submit", e));
        }
    }

    public DisasterDetail Report2Disaster(ReportFromCitizen reportData){

        String DisasterLocation=getLocationString(reportData.getLatitude(), reportData.getLongitude());



        DisasterDetail disasterData=new DisasterDetail(
                reportData.getRadius(),
                DisasterLocation,
                "time",
                reportData.getLatitude(),
                reportData.getLongitude(),
                reportData.getDisasterType(),
                "uid",
                1
        );

        return disasterData;

    }

    public static String getLocationString(float lat, float lng) {
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
