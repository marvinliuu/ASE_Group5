package com.example.testdisasterevent.ui.disaster;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.RerouteDataSource;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.databinding.FragmentDisasterBinding;
import com.example.testdisasterevent.databinding.FragmentDisasterDetailsBinding;
import com.example.testdisasterevent.databinding.FragmentHomeBinding;
//import com.example.testdisasterevent.ui.home.HomeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisasterDetailsFragment extends Fragment implements OnMapReadyCallback, RerouteDataSource.RouteCallback  {

    private DisaterViewModel disaterViewModel;
    private FragmentDisasterDetailsBinding binding;
    private SupportMapFragment mapFragment;
    private PopupWindow popupWindow;
    private View contentView;
    private GoogleMap map;
    private ImageButton backBriefBtn;
    private TextView txt_show;
    private ImageView disaster_logo;
    private ImageButton closeBtn;
    private TextView locIntro;
    private TextView locDetail;
    private TextView ftIntro;
    private TextView ftDetail;
    private TextView typeIntro;
    private TextView typeDetail;
    private TextView upIntro;
    private TextView upDetail;
    private TextView radiusIntro;
    private TextView radiusDetail;
    private ImageButton firstBtn;
    private ImageButton exitBtn;
    private int index;
    private Set<LatLng> roadSet;
    private List<LatLng> selectedRoad;
    private float zoomLevel;
    private LatLng test;
    private int clickType;
    private Polyline prePolyLine;
    private String showingRoute;
    private List<LatLng> exitsFirst = new ArrayList<LatLng>();
    private  List<LatLng> exitsExit = new ArrayList<LatLng>();



    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        disaterViewModel = new ViewModelProvider(requireActivity()).get(DisaterViewModel.class);

        binding = FragmentDisasterDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("data_key");
            // Use the data as needed
        }

        backBriefBtn = binding.backBrief;

        //load popup window layout
        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.disasterdetails_popupwindow, null);

        // bind view
        txt_show = contentView.findViewById(R.id.tv_pop_name);
        disaster_logo = contentView.findViewById(R.id.disaster_logo);
        closeBtn = contentView.findViewById(R.id.close_btn);
        locIntro = contentView.findViewById(R.id.location_intro);
        locDetail = contentView.findViewById(R.id.location_details);
        ftIntro = contentView.findViewById(R.id.ftime_intro);
        ftDetail = contentView.findViewById(R.id.ftime_details);
        typeIntro = contentView.findViewById(R.id.type_intro);
        typeDetail = contentView.findViewById(R.id.type_details);
        upIntro = contentView.findViewById(R.id.update_intro);
        upDetail = contentView.findViewById(R.id.update_details);
        radiusIntro = contentView.findViewById(R.id.radius_intro);
        radiusDetail = contentView.findViewById(R.id.radius_details);


        firstBtn = binding.firstAidBtn;
        exitBtn = binding.exitBtn;


        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, 100);

        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.add(R.id.detailsMap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        backBriefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // Get a reference to the child FragmentManager
                FragmentManager fragmentManager = getChildFragmentManager();

                // Start a new FragmentTransaction
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace the current fragment with the new fragment
                Fragment newFragment = new DisasterFragment();
                fragmentTransaction.replace(R.id.disasterdetails_container, newFragment);

                // Add the transaction to the back stack
                fragmentTransaction.addToBackStack(null);

                // Commit the transaction
                fragmentTransaction.commit();

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        return root;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 获取当前位置
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            test = new LatLng(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };


    public void createDisasterDetailsPopupWindow(DisasterDetail[] details) {
        String titleText = details[index].getDisasterTitle();
        // Load the custom font from the assets folder
        Typeface topTitleType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        txt_show.setTypeface(topTitleType);
        txt_show.setTextSize(25);

        // SET THE INTRODUCTION WORD TEXT STYLE
        // Load the custom font from the assets folder
        Typeface generalType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        locIntro.setTypeface(generalType);
        locIntro.setTextSize(15);
        ftIntro.setTypeface(generalType);
        ftIntro.setTextSize(15);
        typeIntro.setTypeface(generalType);
        typeIntro.setTextSize(15);
        upIntro.setTypeface(generalType);
        upIntro.setTextSize(15);
        radiusIntro.setTypeface(generalType);
        radiusIntro.setTextSize(15);

        Typeface detailsType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");
        locDetail.setTypeface(detailsType);
        locDetail.setTextSize(15);
        locDetail.setText(details[index].getLocation());
        ftDetail.setTypeface(detailsType);
        ftDetail.setTextSize(15);
        ftDetail.setText(details[index].getHappenTime());
        typeDetail.setTypeface(detailsType);
        typeDetail.setTextSize(15);
        typeDetail.setText("unknown");
        upDetail.setTypeface(detailsType);
        upDetail.setTextSize(15);
        upDetail.setText(details[index].getHappenTime());
        radiusDetail.setTypeface(detailsType);
        radiusDetail.setTextSize(15);
        radiusDetail.setText(Integer.toString(details[index].getRadius()) + " m");

        // set the title icon resource
        setDisIconResource(titleText);

        // set the title color & text
        setDisTitle(titleText);


        Bitmap bitmap = createDisIconOnMap(titleText);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaledWidth = width * 0.5f;
        float scaledHeight = height * 0.5f;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);


        LatLng center = new LatLng(details[index].getLatitude(), details[index].getLongitude());
        test = center;


        int radius = details[index].getRadius();
        if(radius >= 100){
            zoomLevel = 17f;
        }
        else if(radius >= 20){
            zoomLevel = 19f;
        }
        else{
            zoomLevel = 20f;
        }


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));

        map.setMapType(MAP_TYPE_NORMAL);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(markerIcon);
        map.addMarker(markerOptions);
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(details[index].getRadius())
                .fillColor(0x40FF0000)
                .strokeWidth(0f);
        //.fillOpacity();
        map.addCircle(circleOptions);
    }

    public void setDisIconResource (String title) {
        if (title.equals("Fire")) {
            disaster_logo.setImageResource(R.drawable.fire_logo);
        } else if (title.equals("Water")) {
            disaster_logo.setImageResource(R.drawable.water_logo);
        } else {
            disaster_logo.setImageResource(R.drawable.other_logo);
        }
    }

    public void setDisTitle (String title) {
        if (title.equals("Fire")) {
            txt_show.setText(title);
            txt_show.setTextColor(Color.RED);
        } else if (title.equals("Water")) {
            txt_show.setText(title);
            txt_show.setTextColor(Color.BLUE);
        } else {
            txt_show.setText(title);
            txt_show.setTextColor(Color.RED);
        }
    }

    public Bitmap createDisIconOnMap (String title) {
        // Create and add an ImageView to the RelativeLayout - disaster logo
        Bitmap bitmap;
        if (title.equals("Fire")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fire_logo);
        } else if (title.equals("Water")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water_logo);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.other_logo);
        }
        return bitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /**
         * set lat/long here
         */
        map = googleMap;

        disaterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
            @Override
            public void onChanged(DisasterDetail[] posts) {
                if (posts.length > 0) {
                    showPopwindow();
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                    createDisasterDetailsPopupWindow(posts);
                    selectRoadFromData(posts[index].getLatitude(), posts[index].getLongitude(), posts[index].getRadius());
                }
            }
        });
    }

    void resetUserLocationToRoad() {
        disaterViewModel.getUserNearbyRoads(test.latitude, test.longitude).observe(getActivity(), new Observer<List<LatLng>>() {
            @Override
            public void onChanged(List<LatLng> latLngs) {
                if (latLngs.size() > 0) {
                    test = latLngs.get(0);
                }
                observeButtonClick(selectedRoad);
            }
        });
    }

    void observeButtonClick(List<LatLng> selectedRoad) {
        firstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRoad.size() == 0) {
                    return;
                }
                clickType = 1;
                if (exitsFirst.size() <= 0) {
                    for (int i = 0; i < selectedRoad.size(); i = i + 2) {
                        exitsFirst.add(selectedRoad.get(i));
                    }
                }
                DirectionsRoute route = null;
                try {
                    route = disaterViewModel.findOptimalRoute(exitsFirst, test);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onOptimalRouteReady(route);
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRoad.size() == 0) {
                    return;
                }
                clickType = 2;
                if (exitsExit.size() <= 0) {
                    for (int i = 1; i < selectedRoad.size(); i = i + 2) {
                        exitsExit.add(selectedRoad.get(i));
                    }
                }
                DirectionsRoute route = null;
                try {
                    route = disaterViewModel.findOptimalRoute(exitsExit, test);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onOptimalRouteReady(route);
            }
        });
    }

    void rerouteSetting(LatLng start, LatLng end) {
        // Set up the GeoApiContext with your API key
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyDrYjvowVSGRHTyi5vO7CZx2Py32G1BoaY")
                .build();

        // Set the travel mode
        TravelMode travelMode = TravelMode.WALKING;

        // Set the route restrictions
        DirectionsApi.RouteRestriction[] restrictions = { DirectionsApi.RouteRestriction.FERRIES };

        // Get the route information
        RerouteDataSource rerouteDataDataSource = new RerouteDataSource(context);
        rerouteDataDataSource.getRoute(start, end, travelMode, restrictions, this);
    }

    void selectRoadFromData(double latitute, double longitude, double radius) {
        disaterViewModel.getNearbyRoads(latitute, longitude, radius).observe(getActivity(), new Observer<List<LatLng>>() {
            @Override
            public void onChanged(List<LatLng> latLngs) {
                if (latLngs.size() > 0) {
                    roadSet = new HashSet<>(latLngs);
                } else {
                    roadSet = new HashSet<>(disaterViewModel.getSelectedPoints(latitute, longitude, radius));
                }
                selectedRoad = disaterViewModel.selectEntries(roadSet, radius);
                createEntriesView(selectedRoad);
                resetUserLocationToRoad();
            }
        });
    }

    void createEntriesView(List<LatLng> selectedRoad) {
        int count = 0;
        Marker[] markers = new Marker[selectedRoad.size()];
        for (LatLng ll : selectedRoad) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(ll)
                    .title(Integer.toString(count))
                    .icon(createMakerIcon((count++)))
                    .anchor(0.5f, 0.5f);
            map.addMarker(markerOptions);

        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (int i = 0; i < selectedRoad.size(); i++) {
                    if (marker.getTitle().equals(Integer.toString(i))) {
                        if (marker.getTitle().equals(showingRoute) && prePolyLine != null) {
                            prePolyLine.remove();
                            showingRoute = "0";
                            return true;
                        }
                        if (i % 2 == 0) {
                            clickType = 1;
                        } else {
                            clickType = 2;
                        }
                        showingRoute = marker.getTitle();
                        rerouteSetting(test, selectedRoad.get(i));
                    }
                }
                return true;
            }
        });
    }

    BitmapDescriptor createMakerIcon(int count) {
        // Create and add an ImageView to the RelativeLayout - disaster logo
        Bitmap bitmap;
        if (count % 2 == 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.firstaid);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.exit);
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaledWidth = width * 0.2f;
        float scaledHeight = height * 0.2f;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);
        return markerIcon;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Date: 23.02.07
     * Function: show popupWindow
     * Author: Siyu Liao
     * Version: Week 3
     */
    private void showPopwindow() {
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                700);
        // set SelectPicPopupWindow height
        popupWindow.setHeight(700);
        // get focus point
        popupWindow.setFocusable(true);
        // set background color of blank area
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // Click outside to disappear
        popupWindow.setOutsideTouchable(true);
        // Settings can be clicked
        popupWindow.setTouchable(true);
        // hidden animation
        popupWindow.setAnimationStyle(R.style.ipopwindow_anim_style);
    }

    @Override
    public void onRouteReady(DirectionsResult result) {
        PolylineOptions polylineOptions = new PolylineOptions();
        if (clickType == 1) {
            polylineOptions.color(Color.RED);
        } else {
            polylineOptions.color(Color.GREEN);
        }
        List<LatLng> points = new ArrayList<>();
        if (result == null) {
            return;
        }
        List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
        for (com.google.maps.model.LatLng latLng : path) {
            points.add(new LatLng(latLng.lat, latLng.lng));
        }
        polylineOptions.addAll(points);
        polylineOptions.width(20f);

        polylineOptions.startCap(new RoundCap());
        polylineOptions.endCap(new RoundCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.geodesic(true);
        if (prePolyLine != null) {
            prePolyLine.remove();
        }
        prePolyLine = map.addPolyline(polylineOptions);
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(getContext(),
                "Error: " + errorMessage,
                Toast.LENGTH_LONG).show();
    }

    public void onOptimalRouteReady(DirectionsRoute route) {
        PolylineOptions polylineOptions = new PolylineOptions();
        if (clickType == 1) {
            polylineOptions.color(Color.RED);
        } else {
            polylineOptions.color(Color.GREEN);
        }
        List<LatLng> points = new ArrayList<>();
        if (route == null) {
            return;
        }
        if (route.overviewPolyline == null) {
            return;
        }
        List<com.google.maps.model.LatLng> path = route.overviewPolyline.decodePath();
        for (com.google.maps.model.LatLng latLng : path) {
            points.add(new LatLng(latLng.lat, latLng.lng));
        }
        polylineOptions.addAll(points);
        polylineOptions.width(20f);

        polylineOptions.startCap(new RoundCap());
        polylineOptions.endCap(new RoundCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.geodesic(true);
        if (prePolyLine != null) {
            prePolyLine.remove();
        }
        prePolyLine = map.addPolyline(polylineOptions);
    }


}