package com.example.testdisasterevent.ui.disaster;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
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

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.databinding.FragmentDisasterBinding;
import com.example.testdisasterevent.databinding.FragmentDisasterDetailsBinding;
import com.example.testdisasterevent.databinding.FragmentHomeBinding;
import com.example.testdisasterevent.ui.home.HomeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisasterDetailsFragment extends Fragment implements OnMapReadyCallback {

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

    public void setSharedViewModel(DisaterViewModel disasterViewModel) {
        this.disaterViewModel = disasterViewModel;
    }


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        disaterViewModel = new ViewModelProvider(requireActivity()).get(DisaterViewModel.class);

        binding = FragmentDisasterDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

    public void createDisasterDetailsPopupWindow(DisasterDetail[] details) {
        int i = disaterViewModel.indexOfDisasterDetails;
        String titleText = details[i].getDisasterTitle();
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

        Typeface detailsType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");
        locDetail.setTypeface(detailsType);
        locDetail.setTextSize(15);
        locDetail.setText(details[i].getLocation());
        ftDetail.setTypeface(detailsType);
        ftDetail.setTextSize(15);
        ftDetail.setText(details[i].getHappenTime());
        typeDetail.setTypeface(detailsType);
        typeDetail.setTextSize(15);
        typeDetail.setText("unknown");
        upDetail.setTypeface(detailsType);
        upDetail.setTextSize(15);
        upDetail.setText(details[i].getHappenTime());

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

        LatLng center = new LatLng(details[i].getLatitude(), details[i].getLongtitude());
        float zoomLevel = 13f;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
        System.out.println("get here:" + center);
        map.setMapType(MAP_TYPE_NORMAL);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(markerIcon);
        map.addMarker(markerOptions);
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
        LatLng sydney = new LatLng(-37.812439, 144.972755);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        googleMap.addMarker(new MarkerOptions()
                .position(sydney));

        disaterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
            @Override
            public void onChanged(DisasterDetail[] posts) {
                if (posts.length > 0) {
                    showPopwindow();
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                    createDisasterDetailsPopupWindow(posts);
                }
            }
        });
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

}