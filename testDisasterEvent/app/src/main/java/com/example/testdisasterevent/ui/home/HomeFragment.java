package com.example.testdisasterevent.ui.home;

import static android.os.Looper.getMainLooper;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.RerouteDataSource;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.ReportInfo;
import com.example.testdisasterevent.databinding.FragmentHomeBinding;
import com.example.testdisasterevent.ui.account.AccountViewModel;
import com.example.testdisasterevent.data.HereRerouteDataSource;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.databinding.FragmentHomeBinding;
import com.example.testdisasterevent.ui.disaster.DisasterViewModel;
import com.example.testdisasterevent.utils.IconSettingUtils;
import com.example.testdisasterevent.utils.LocationTracker;
import com.example.testdisasterevent.utils.PopupwindowUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.here.sdk.core.GeoBox;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.engine.SDKNativeEngine;
import com.here.sdk.core.engine.SDKOptions;
import com.here.sdk.core.errors.InstantiationErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.*;
import android.location.Location;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;


public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationTracker.LocationUpdateListener {

    private PopupWindow popupWindow;
    private View contentView;
    private ImageButton showWindowButton;
    private TextView txt_show;
    private GoogleMap map;
    private ImageButton closeBtn;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private SupportMapFragment mapFragment;
    private AccountViewModel aViewModel;
    private EditText  startLocation, desLocation;
    private ImageButton enterBtn;
    private DisasterViewModel disaterViewModel;
    private DisasterDetail[] details;
    private LatLng startPoint, endPoint;
    private String start, end;
    private String[] stop_ids;
    private ImageButton busInfoBtn;
    private HereRerouteDataSource hereRerouteDataSource;
    private int count;
    private boolean isShowDisasterCircle;
    private IconSettingUtils iconSettingUtils;
    private PopupwindowUtils popupwindowUtils;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double currentLatitude;
    private double currentLongitude;
    private Marker marker;
    private Polyline reroutePolyline;
    private LocationTracker locationTracker;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        aViewModel =
                new  ViewModelProvider(this).get(AccountViewModel.class);
        disaterViewModel =
                new ViewModelProvider(this).get(DisasterViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // init utils
        iconSettingUtils = new IconSettingUtils();
        popupwindowUtils = new PopupwindowUtils();

        isShowDisasterCircle = false;

        showWindowButton = binding.showPopwindow;

        startLocation = binding.startLocation;
        desLocation = binding.desLocation;
        enterBtn = binding.enterBtn;
        busInfoBtn = binding.getBusInfo;

        initializeHERESDK();

        hereRerouteDataSource = new HereRerouteDataSource();

        setClickListeners();

        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, 100);

        locationTracker = new LocationTracker(requireContext(), this);
        locationTracker.startLocationUpdates();
        // Initialize map fragment
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);



        disaterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
            @Override
            public void onChanged(DisasterDetail[] posts) {
                if (posts.length > 0) {
                    details = new DisasterDetail[posts.length];
                    details = posts;
                    if (!isShowDisasterCircle) createDisasterCircleOnMap();
                }
            }
        });
        return root;
    }

    @Override
    public void onLocationUpdated(Location location) {
        Log.i(TAG, String.format("Updated location: Latitude = %f, Longitude = %f",
                location.getLatitude(), location.getLongitude()));
        currentLongitude = location.getLongitude();
        currentLatitude = location.getLatitude();
        LatLng currentPosition = new LatLng(currentLatitude, currentLongitude);
        if (marker == null) {
            marker = map.addMarker(new MarkerOptions().position(currentPosition).title("Marker in Target Location"));
        } else {
            marker.setPosition(currentPosition);

        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
        // 在此处执行其他操作，例如更新UI或将位置发送到服务器
    }


    @Override
    public void onPause() {
        super.onPause();
        locationTracker.stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        locationTracker.startLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationTracker.stopLocationUpdates();
    }


    /**
     * Date: 23.03.31
     * Function: set all the button click listeners
     * Author: Siyu Liao
     * Version: Week 10
     */
    private void setClickListeners () {
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start = startLocation.getText().toString();
                end = desLocation.getText().toString();
                try {
                    getRouteLatLngInfo(start, end);
                } catch (InstantiationErrorException e) {
                    e.printStackTrace();
                }
            }
        });

        busInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRealTimeData();
                Toast.makeText(getContext(),"Request Real Time Bus Information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDisasterCircleOnMap() {
        if (details != null) {
            isShowDisasterCircle = true;
            for (DisasterDetail detail : details) {
                LatLng center = new LatLng(detail.getLatitude(), detail.getLongitude());
                Bitmap bitmap = iconSettingUtils.createDisIconOnMap(detail.getDisasterType(), getResources());
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float scaledWidth = width * 0.5f;
                float scaledHeight = height * 0.5f;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
                BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(center)
                        .icon(markerIcon)
                        .anchor(0.5f, 0.5f);;
                map.addMarker(markerOptions);

                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(center);
                circleOptions.radius(detail.getRadius());
                circleOptions.strokeColor(Color.RED);
                circleOptions.strokeWidth(2);
                circleOptions.fillColor(Color.argb(70, 255, 0, 0));
                map.addCircle(circleOptions);
            }
        }
    }

    private void initializeHERESDK() {
        // Set your credentials for the HERE SDK.
        String accessKeyID = "PTBf8nCFZijyTZMsRTrUfQ";
        String accessKeySecret = "9B0xK9nSuAM7jTeoCiZWwUlKjtWEhCMw7PdHmoxrLN4ZSUrvx-B8lxvb-QBFwJZlRLiQjCFbO1iZ7DPWCxtd_w";
        SDKOptions options = new SDKOptions(accessKeyID, accessKeySecret);
        try {
            Context context = getContext();
            SDKNativeEngine.makeSharedInstance(context, options);
        } catch (InstantiationErrorException e) {
            throw new RuntimeException("Initialization of HERE SDK failed: " + e.error.name());
        }
    }

    void getRouteLatLngInfo (String start, String end) throws InstantiationErrorException {
        startPoint = homeViewModel.getLocLatLng(start);
        endPoint = homeViewModel.getLocLatLng(end);
        List<GeoBox> geoBoxes = calDisAreaGeoInfo();
        hereRerouteDataSource.addRoute(startPoint, endPoint, geoBoxes);
    }

    List<GeoBox> calDisAreaGeoInfo() {
        List<GeoBox> geoBoxes = new ArrayList<GeoBox>();
        if (details == null)
            return geoBoxes;
        for (DisasterDetail detail : details) {
            LatLng[] latLng = homeViewModel.calculateDestinationLocations(new LatLng(detail.getLatitude(), detail.getLongitude()), detail.getRadius());
            geoBoxes.add(new GeoBox(new GeoCoordinates(latLng[0].latitude, latLng[0].longitude), new GeoCoordinates(latLng[1].latitude, latLng[1].longitude)));
        }
        return geoBoxes;
    }


    //When map id loaded
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setShape(GradientDrawable.RECTANGLE);
        shapeDrawable.setCornerRadii(new float[] { 16, 16, 16, 16, 0, 0, 0, 0 });

        FrameLayout overlay = binding.mapOverlay;
        overlay.setBackground(shapeDrawable);

        map = googleMap;

        hereRerouteDataSource.livePointData.observe(getActivity(), new Observer<List<LatLng>>() {
            @Override
            public void onChanged(List<LatLng> latLngs) {
                if (reroutePolyline != null)
                    reroutePolyline.remove();
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.YELLOW);

                polylineOptions.addAll(latLngs);
                polylineOptions.width(20f);

                polylineOptions.startCap(new RoundCap());
                polylineOptions.endCap(new RoundCap());
                polylineOptions.jointType(JointType.ROUND);
                polylineOptions.geodesic(true);
                reroutePolyline = map.addPolyline(polylineOptions);
            }
        });
        createDisasterCircleOnMap();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.message_home_report_popupwindow, null);

        txt_show = contentView.findViewById(R.id.tv_pop_name);
        closeBtn = contentView.findViewById(R.id.close_btn);

        // click the close btn, dismiss the popup window
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // Load the custom font from the assets folder
        Typeface topTitleType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        txt_show.setTypeface(topTitleType);
        txt_show.setTextSize(25);
        showWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
            }
        });
        //load popup window layout
        MainActivity mainActivity = (MainActivity) getActivity();
        AccountUserInfo accountUserInfoData = mainActivity.getAccountUserInfo();
        if (accountUserInfoData != null && accountUserInfoData.getUserTypeID() == 0) {
            homeViewModel.getReportInfo().observe(getActivity(), new Observer<ReportInfo[]>() {
                @Override
                public void onChanged(ReportInfo[] posts) {
                    popupWindow = popupwindowUtils.showPopwindow(contentView);
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                    if (posts.length > 0) {
                        // Update the UI with the new data
                        createReportPopupWindow(posts);
                    } else {
                        // Update the UI when no disaster happen
                        createNoReportPopWindow();
                    }}
            });
        }
    }


    public void createNoReportPopWindow() {
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = contentView.findViewById(R.id.disasterReportScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add the LinearLayout to the ScrollView
        scrollView.addView(linearLayout);

        // Create and add a TextView to the RelativeLayout - Title
        TextView title = new TextView(getContext());
        title.setText("No Disaster Report");
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);

        // Load the custom font from the assets folder
        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");

        // Set the font of the TextView to the custom font
        title.setTypeface(customFont);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        linearLayout.addView(title, titleParams);
    }


    public void createReportPopupWindow(ReportInfo[] infos) {
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = contentView.findViewById(R.id.disasterReportScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        boolean showReport = false;
        // Create multiple RelativeLayouts with multiple views and add them to the LinearLayout
        for (int i = 0; i < infos.length; i++) {
            //Report state determination
            if (infos[i].getReportState() == 0) {
                showReport = true;
                RelativeLayout relativeLayout = new RelativeLayout(getContext());
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                relativeLayout.setId(i);
                params2.addRule(RelativeLayout.BELOW, relativeLayout.getId());
                params2.setMargins(0, 0, 0, 16); // set a top margin of 16dp
                relativeLayout.setLayoutParams(params2);
                relativeLayout.setId(i);

                // Create and add a TextView to the RelativeLayout - Title
                TextView title = new TextView(getContext());
                String titleText = infos[i].getReportTitle();
                title.setText(titleText);
                title.setId(View.generateViewId());
                title.setTextColor(Color.BLACK);
                title.setTextSize(20);

                // Load the custom font from the assets folder
                Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");

                // Set the font of the TextView to the custom font
                title.setTypeface(customFont);
                RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                // Create and add a TextView to the RelativeLayout - Location
                TextView location = new TextView(getContext());
                location.setText(infos[i].getLocation());
                location.setId(View.generateViewId());
                location.setTextColor(Color.BLACK);

                RelativeLayout.LayoutParams locationParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                locationParams.addRule(RelativeLayout.BELOW, title.getId());

                // Create and add a TextView to the RelativeLayout - Time
                TextView time = new TextView(getContext());
                time.setText(infos[i].getHappenTime());
                time.setId(View.generateViewId());
                time.setTextColor(Color.BLACK);

                RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                timeParams.addRule(RelativeLayout.BELOW, location.getId());

                ImageView imageView = iconSettingUtils.createDisIconOnWindow(titleText, getContext());
                RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                        70, 70);
                imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);

                Bitmap bitmap = iconSettingUtils.createDisIconOnMap(titleText, getResources());
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float scaledWidth = width * 0.5f;
                float scaledHeight = height * 0.5f;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
                BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);


                LatLng center = new LatLng(infos[i].getLatitude(), infos[i].getLongitude());
                float zoomLevel = 13f;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
                map.setMapType(MAP_TYPE_NORMAL);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(center)
                        .icon(markerIcon)
                        .anchor(0.5f, 0.5f);
                map.addMarker(markerOptions);

                // add view by order
                relativeLayout.addView(title, titleParams);
                relativeLayout.addView(location, locationParams);
                relativeLayout.addView(time, timeParams);
                relativeLayout.addView(imageView, imageParams);

                setDisasterItemClickListener(relativeLayout);

                // Add the RelativeLayout to the LinearLayout
                linearLayout.addView(relativeLayout);
            }
        }
        if (!showReport) {
            createNoReportPopWindow();
        } else {
            // Add the LinearLayout to the ScrollView
            scrollView.addView(linearLayout);
        }
    }

    /**
     * Date: 23.03.31
     * Function: set disaster item click listener
     * Author: Siyu Liao
     * Version: Week 10
     */
    private void setDisasterItemClickListener(RelativeLayout relativeLayout) {
        // set the response event after clicking the RelativeLayout item
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = v.getId();
                Bundle bundle = new Bundle();
                bundle.putInt("data_key", index);
                popupWindow.dismiss();

                // Get a reference to the child FragmentManager
                FragmentManager fragmentManager = getChildFragmentManager();

                // Start a new FragmentTransaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the current fragment with the new fragment
                Fragment newFragment = new ReportConfirmFragment();
                newFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.report_container, newFragment);

                // Add the transaction to the back stack
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();
            }
        });
    }

    private void parseRealTimeData() {
        if (stop_ids.length == 0) return;
        count = 0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference stopsRef = database.getReference("BusStopInfo");
        for (int i = 0; i < stop_ids.length; i++) {
            String stopId = stop_ids[i];
            if (stopId == null) {
                continue;
            }
            if (count > 30) {
                return;
            }
            stopsRef.child(stopId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (count > 30) {
                            return;
                        }
                        count++;
                        // Get the latitude and longitude values from the snapshot
                        double latitude = snapshot.child("stop_lat").getValue(Double.class);
                        double longitude = snapshot.child("stop_lon").getValue(Double.class);
                        // Update the UI with the vehicle's location
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus_icon);
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                float scaledWidth = width * 0.4f;
                                float scaledHeight = height * 0.4f;
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
                                BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);
                                // Add a marker to the map for the vehicle's location
                                LatLng location = new LatLng(latitude, longitude);
                                MarkerOptions options = new MarkerOptions()
                                        .position(location)
                                        .title("vehicleId")
                                        .icon(markerIcon)
                                        .anchor(0.5f, 0.5f);
                                map.addMarker(options);
                            }
                        });
                    } else {
                        System.out.println("No data available for stop_id: " + stopId);
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("Database query cancelled: " + error.getMessage());
                }
            });
        }
    }


    private void readBusContent (JsonObject jsonObject) {
        if (jsonObject == null) {
            try {
                AssetManager assetManager = getContext().getAssets();
                InputStream inputStream = assetManager.open("defaultBusInfo.txt");
                // Convert the InputStream to a String using a BufferedReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                // Convert the String to a JSON object using Gson
                Gson gson = new Gson();
                jsonObject = gson.fromJson(stringBuilder.toString(), JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject == null) return;
        JsonArray array = jsonObject.getAsJsonArray("entity");
        stop_ids = new String[array.size()];
        int loop = Math.min(800, array.size());
        for (int i = 1; i < loop; i++) {
            JsonElement innerObj = array.get(i);
            JsonArray stop_time_update = innerObj.getAsJsonObject().getAsJsonObject("trip_update").getAsJsonArray("stop_time_update");
            if (stop_time_update != null) {
                JsonObject info = stop_time_update.get(0).getAsJsonObject();
                if (info != null && info.getAsJsonPrimitive("stop_id") != null) {
                    String stop_id = info.getAsJsonPrimitive("stop_id").toString();
                    stop_ids[i] = stop_id.substring(1, stop_id.length() - 1);
                }
            }
//            String stop_id = innerObj.getAsJsonObject().getAsJsonObject("trip_update").getAsJsonArray("stop_time_update").get(0).getAsJsonObject().getAsJsonPrimitive("stop_id").toString();
        }
    }

    private void getRealTimeData() {
        // Create a new Thread to handle the API request and response
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Build the URL for the GTFS Realtime API request
                    String url = "https://api.nationaltransport.ie/gtfsr/v2/gtfsr?format=json";
                    URL requestUrl = new URL(url);

                    // Open a connection to the API and set request headers
                    HttpsURLConnection connection = (HttpsURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Cache-Control", "no-cache");
                    connection.setRequestProperty("x-api-key", "770a598030974f42a7f0adf379244ffc");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    JsonObject jsonObject = new JsonObject();
                    // Convert the String to a JSON object using Gson
                    Gson gson = new Gson();
                    jsonObject = gson.fromJson(content.toString(), JsonObject.class);

                    readBusContent(jsonObject);
                    connection.disconnect();
                } catch (Exception e) {
                    readBusContent(null);
                    e.printStackTrace();
                }
                parseRealTimeData();
            }
        });
        // Start the Thread
        thread.start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        homeViewModel.getReportInfo().removeObservers(this);
    }

}