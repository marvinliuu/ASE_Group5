package com.example.testdisasterevent.ui.home;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.RerouteDataSource;
import com.example.testdisasterevent.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.DirectionsApi.RouteRestriction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback  {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
// Initialize map fragment
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return root;
    }

    //When map id loaded
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                CircleOptions circleOptions=new CircleOptions();
                circleOptions.center(latLng);
                circleOptions.radius(100);
                circleOptions.strokeColor(Color.RED);
                circleOptions.strokeWidth(2);
                circleOptions.fillColor(Color.argb(70, 255, 0, 0));
                googleMap.addCircle(circleOptions);
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                googleMap.addMarker(markerOptions);
                googleMap.addCircle(circleOptions);
            }
        });
        /**
         * set lat/long here
         */
        LatLng sydney = new LatLng(53.3442016, -6.2544264);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        googleMap.addMarker(new MarkerOptions()
                .position(sydney));

        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setShape(GradientDrawable.RECTANGLE);
        shapeDrawable.setCornerRadii(new float[] { 16, 16, 16, 16, 0, 0, 0, 0 });

        FrameLayout overlay = binding.mapOverlay;
        overlay.setBackground(shapeDrawable);

        map = googleMap;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}