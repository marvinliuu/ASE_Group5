package com.example.testdisasterevent.ui.report;

import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.databinding.FragmentReportMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReportMapFragment extends Fragment implements OnMapReadyCallback {

    private ReportViewModel reportViewModel;
    private FragmentReportMapBinding binding;
    private SupportMapFragment mapFragment;
    private ImageView submitButton;
    private ImageView cancelButton;
    private ImageButton fireType;
    private ImageButton waterType;
    private ImageButton otherType;
    private SeekBar radiusBar;
    private int reportType = 1;
    private int radius = 100;
    private LatLng location;



    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        reportViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);



        binding = FragmentReportMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        submitButton = binding.reportMapSubmit;
        cancelButton = binding.reportMapCancel;
        fireType = binding.fireBtn;
        waterType = binding.waterBtn;
        otherType = binding.otherBtn;
        radiusBar = binding.mSeekBar;

        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, 100);
// Initialize map fragment
        mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.R_map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("Type", reportType);
                bundle.putInt("Radius", radius);
                bundle.putDouble("Longitude", location.longitude);
                bundle.putDouble("Latitude", location.latitude);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment reportFragment = new ReportFragment();
                reportFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.report_map, reportFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment reportFragment = new ReportFragment();
                fragmentTransaction.replace(R.id.report_map, reportFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        fireType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportType = 1;
            }
        });

        waterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportType = 2;
            }
        });

        otherType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportType = 3;
            }
        });


        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return root;
    }

    //When map id loaded
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location = latLng;
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.longitude+" : "+latLng.latitude);
                CircleOptions circleOptions=new CircleOptions();
                circleOptions.center(latLng);
                circleOptions.radius(radius);
                circleOptions.strokeColor(Color.RED);
                circleOptions.strokeWidth(2);
                circleOptions.fillColor(Color.argb(70, 255, 0, 0));
                //googleMap.addCircle(circleOptions);
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
        location = sydney;
        googleMap.addMarker(new MarkerOptions()
                .position(sydney));

        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setShape(GradientDrawable.RECTANGLE);
        shapeDrawable.setCornerRadii(new float[] { 16, 16, 16, 16, 0, 0, 0, 0 });

//        FrameLayout overlay = binding.;
//        overlay.setBackground(shapeDrawable);


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}