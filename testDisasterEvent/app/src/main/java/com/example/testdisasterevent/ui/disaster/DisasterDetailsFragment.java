package com.example.testdisasterevent.ui.disaster;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.algorithms.ResearchAllocation;
import com.example.testdisasterevent.data.RerouteDataSource;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.databinding.FragmentDisasterDetailsBinding;
//import com.example.testdisasterevent.ui.home.HomeViewModel;
import com.example.testdisasterevent.utils.IconSettingUtils;
import com.example.testdisasterevent.utils.LocationTracker;
import com.example.testdisasterevent.utils.PopupwindowUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisasterDetailsFragment extends Fragment implements OnMapReadyCallback, RerouteDataSource.RouteCallback, LocationTracker.LocationUpdateListener {
    private DisasterViewModel disasterViewModel;  // Type: DisasterViewModel, used for managing disaster-related data
    private FragmentDisasterDetailsBinding binding;  // Type: FragmentDisasterDetailsBinding, used for binding data to the UI
    private SupportMapFragment mapFragment;  // Type: SupportMapFragment, used for displaying maps
    private PopupWindow popupWindow;  // Type: PopupWindow, used for displaying pop-up windows
    private View contentView;  // Type: View, used for displaying UI elements
    private GoogleMap map;  // Type: GoogleMap, used for displaying maps
    private ImageButton backBriefBtn;  // Type: ImageButton, used for displaying buttons
    private ImageView disaster_logo;  // Type: ImageView, used for displaying images
    private ImageButton closeBtn;  // Type: ImageButton, used for displaying buttons
    private TextView locIntro, locDetail, ftIntro, ftDetail, txt_show;  // Type: TextView, used for displaying text
    private TextView typeIntro, typeDetail, upIntro, upDetail;  // Type: TextView, used for displaying text
    private TextView radiusIntro, radiusDetail;  // Type: TextView, used for displaying text
    private ImageButton firstBtn, exitBtn;  // Type: ImageButton, used for displaying buttons
    private int index;  // Type: int, used for storing an index value
    private Set<LatLng> roadSet;  // Type: Set<LatLng>, used for storing a set of LatLng objects
    private List<LatLng> selectedRoad;  // Type: List<LatLng>, used for storing a list of LatLng objects
    private float zoomLevel;  // Type: float, used for storing the zoom level of the map
    private LatLng test;  // Type: LatLng, used for storing the user's location
    private int clickType;  // Type: int, used for storing the type of click event
    private Polyline prePolyLine;  // Type: Polyline, used for displaying a polyline on the map
    private String showingRoute;  // Type: String, used for storing a string value
    private List<LatLng> exitsFirst = new ArrayList<LatLng>();  // Type: List<LatLng>, used for storing a list of LatLng objects
    private List<LatLng> exitsExit = new ArrayList<LatLng>();  // Type: List<LatLng>, used for storing a list of LatLng objects
    private PopupwindowUtils popupwindowUtils;  // Type: PopupwindowUtils, used for displaying pop-up windows
    private IconSettingUtils iconSettingUtils;  // Type: IconSettingUtils, used for setting icons
    private double currentLatitude;
    private double currentLongitude;
    private Marker marker;
    private LocationTracker locationTracker;



    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        disasterViewModel = new ViewModelProvider(requireActivity()).get(DisasterViewModel.class);

        binding = FragmentDisasterDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get the transport info
        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("data_key");
            // Use the data as needed
        }

        // init Utils
        popupwindowUtils = new PopupwindowUtils();
        iconSettingUtils = new IconSettingUtils();

        // binding view
        backBriefBtn = binding.backBrief;
        firstBtn = binding.firstAidBtn;
        exitBtn = binding.exitBtn;

        //load popup window layout
        contentView = LayoutInflater.from(getActivity()).inflate(R.layout.disasterdetails_popupwindow, null);

        // contentView init subview
        bindSubView();

        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, 100);

        // init map fragment
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.add(R.id.detailsMap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationTracker = new LocationTracker(requireContext(), this);
        locationTracker.startLocationUpdates();
        // set Button Click
        setClickListeners();

        return root;
    }


    /**
     * Date: 23.04.13
     * Function: Get user current location
     * Author: Haoxian Liu
     * Version: Week 12
     */
    @Override
    public void onLocationUpdated(Location location) {
        Log.i(TAG, String.format("Updated location: Latitude = %f, Longitude = %f",
                location.getLatitude(), location.getLongitude()));
        currentLongitude = location.getLongitude();
        currentLatitude = location.getLatitude();
        LatLng currentPosition = new LatLng(currentLatitude, currentLongitude);
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

    public boolean isWithinRadius(double centerLat, double centerLon, double pointLat, double pointLon, double radius) {
        radius /= 1000;
        final double R = 6371; // earth radius

        double latDistance = Math.toRadians(pointLat - centerLat);
        double lonDistance = Math.toRadians(pointLon - centerLon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(centerLat)) * Math.cos(Math.toRadians(pointLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // calculate the distance
        return distance <= radius;
    }

    /**
     * Date: 23.04.06
     * Function: bind all the subViews
     * Author: Siyu Liao
     * Version: Week 11
     */
    private void bindSubView() {
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
    }

    /**
     * Date: 23.04.06
     * Function: set all the button click listeners
     * Author: Siyu Liao
     * Version: Week 11
     */
    private void setClickListeners () {

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
    }


    public void createDisasterDetailsPopupWindow(DisasterDetail[] details) {
        if (index >= details.length) {
            return;
        }
        String titleText = details[index].getDisasterType();
        // Load the custom font from the assets folder
        Typeface topTitleType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        txt_show.setTypeface(topTitleType);

        // SET THE INTRODUCTION WORD TEXT STYLE
        setDisasterGeneralType();

        locDetail.setText(details[index].getLocation());
//        ftDetail.setText(Long.toString(details[index].getHappenTime()));
        ftDetail.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.toString(details[index].getHappenTime()))));
        typeDetail.setText(details[index].getDisasterType());
        upDetail.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.toString(details[index].getHappenTime()))));
//        upDetail.setText(Long.toString(details[index].getHappenTime()));
        radiusDetail.setText(Integer.toString(details[index].getRadius()) + " m");
        setDisasterDetailsType();

        // set the title icon resource
        iconSettingUtils.setDisIconResource(titleText, disaster_logo);

        // set the title color & text
        setDisTitle(titleText);

        Bitmap bitmap = iconSettingUtils.createDisIconOnMap(titleText, getResources());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaledWidth = width * 0.5f;
        float scaledHeight = height * 0.5f;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);


        LatLng center = new LatLng(details[index].getLatitude(), details[index].getLongitude());
        if (isWithinRadius(currentLatitude, currentLongitude, details[index].getLatitude(), details[index].getLongitude(), details[index].getRadius())) {
            test = new LatLng(currentLatitude, currentLongitude);
            if (marker == null) {
                marker = map.addMarker(new MarkerOptions().position(test).title("Your Position"));
            }
        } else {
            test = center;

        }


        // add marker on the map
        zoomLevel = calZoomLevel(details[index].getRadius());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
        map.setMapType(MAP_TYPE_NORMAL);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(markerIcon)
                .anchor(0.5f, 0.5f);
        map.addMarker(markerOptions);
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(details[index].getRadius())
                .fillColor(0x40FF0000)
                .strokeWidth(0f);
        map.addCircle(circleOptions);
    }

    /**
     * Date: 23.04.06
     * Function: set Disaster Details Popup Window Text Type
     * Author: Siyu Liao
     * Version: Week 11
     */
    private void setDisasterDetailsType() {
        Typeface detailsType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");
        locDetail.setTypeface(detailsType);
        ftDetail.setTypeface(detailsType);
        typeDetail.setTypeface(detailsType);
        upDetail.setTypeface(detailsType);
        radiusDetail.setTypeface(detailsType);
    }

    private void setDisasterGeneralType() {
        Typeface generalType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Load the custom font from the assets folder
        // Set the font of the TextView to the custom font
        locIntro.setTypeface(generalType);
        ftIntro.setTypeface(generalType);
        typeIntro.setTypeface(generalType);
        upIntro.setTypeface(generalType);
        radiusIntro.setTypeface(generalType);
    }

    /**
     * Date: 23.04.06
     * Function: calculate zoom level
     * Author: Siyu Liao
     * Version: Week 11
     */
    private float calZoomLevel (int radius) {
        float zoomLevel;
        if(radius >= 100) {
            zoomLevel = 17f;
        }
        else if(radius >= 20) {
            zoomLevel = 19f;
        }
        else {
            zoomLevel = 20f;
        }
        return zoomLevel;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        disasterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
            @Override
            public void onChanged(DisasterDetail[] posts) {
                if (posts.length > 0) {
                    popupWindow = popupwindowUtils.showPopwindow(contentView, 700);
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                    createDisasterDetailsPopupWindow(posts);
                    selectRoadFromData(posts[index].getLatitude(), posts[index].getLongitude(), posts[index].getRadius());
                }
            }
        });
    }

    void resetUserLocationToRoad() {
        disasterViewModel.getUserNearbyRoads(test.latitude, test.longitude).observe(getActivity(), new Observer<List<LatLng>>() {
            @Override
            public void onChanged(List<LatLng> latLngs) {
                if (latLngs.size() > 0) {
                    test = latLngs.get(0);
                }
                observeButtonClick(selectedRoad);
            }
        });
    }


    /**
     * Date: 23.04.06
     * Function: set disaster detials click listener
     * Author: Siyu Liao
     * Version: Week 11
     */
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
                    route = disasterViewModel.findOptimalRoute(exitsFirst, test);
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
                    route = disasterViewModel.findOptimalRoute(exitsExit, test);
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

    /**
     * Date: 23.04.06
     * Function: set the click map marker - trigger reroute setting logic
     * Author: Siyu Liao
     * Version: Week 11
     */
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

    void selectRoadFromData(double latitude, double longitude, double radius) {
        disasterViewModel.getNearbyRoads(latitude, longitude, radius).observe(getActivity(), new Observer<List<LatLng>>() {
            @Override
            public void onChanged(List<LatLng> latLngs) {
                if (latLngs.size() > 0) {
                    roadSet = new HashSet<>(latLngs);
                } else {
                    roadSet = new HashSet<>(disasterViewModel.getSelectedPoints(latitude, longitude, radius));
                }
                selectedRoad = disasterViewModel.selectEntries(roadSet, radius);
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

    /**
     * Date: 23.04.06
     * Function: when the route ready, plot on the map
     * Author: Siyu Liao
     * Version: Week 11
     */
    public void onOptimalRouteReady(DirectionsRoute route) {
        if (route == null) {
            return;
        }
        if (route.overviewPolyline == null) {
            return;
        }

        List<LatLng> points = new ArrayList<>();
        List<com.google.maps.model.LatLng> path = route.overviewPolyline.decodePath();
        for (com.google.maps.model.LatLng latLng : path) {
            points.add(new LatLng(latLng.lat, latLng.lng));
        }

        PolylineOptions polylineOptions = setPolyLineOptions(points);
        if (prePolyLine != null) {
            prePolyLine.remove();
        }
        prePolyLine = map.addPolyline(polylineOptions);
    }

    /**
     * Date: 23.04.06
     * Function: set the polyline options info for polyline shows
     * Author: Siyu Liao
     * Version: Week 11
     */
    private PolylineOptions setPolyLineOptions(List<LatLng> points) {
        PolylineOptions polylineOptions = new PolylineOptions();
        if (clickType == 1) {
            polylineOptions.color(Color.RED);
        } else {
            polylineOptions.color(Color.GREEN);
        }
        polylineOptions.addAll(points);
        polylineOptions.width(20f);
        polylineOptions.startCap(new RoundCap());
        polylineOptions.endCap(new RoundCap());
        polylineOptions.jointType(JointType.ROUND);
        polylineOptions.geodesic(true);
        return polylineOptions;
    }

}