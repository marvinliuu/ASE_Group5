package com.example.testdisasterevent.ui.home;


import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.model.ReportInfo;
import com.example.testdisasterevent.databinding.FragmentHomeReportdetailsBinding;
import com.example.testdisasterevent.utils.IconSettingUtils;
import com.example.testdisasterevent.utils.PopupwindowUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportConfirmFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private FragmentHomeReportdetailsBinding binding;
    private SupportMapFragment mapFragment;
    private PopupWindow popupWindow;
    private View contentView;
    private GoogleMap map;
    private ImageButton backBriefBtn;
    private TextView txt_show;
    private ImageView disaster_logo;
    private ImageButton closeBtn;
    private ImageButton report_confirm;
    private TextView locIntro;
    private TextView locDetail;
    private TextView typeIntro;
    private TextView typeDetail;
    private TextView rtIntro;
    private TextView rtDetail;
    private TextView htIntro;
    private TextView htDetail;
    private int index;
    private PopupwindowUtils popupwindowUtils;
    private IconSettingUtils iconSettingUtils;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding = FragmentHomeReportdetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("data_key");
            // Use the data as needed
        }

        backBriefBtn = binding.backBrief;

        // init Utils
        popupwindowUtils = new PopupwindowUtils();
        iconSettingUtils = new IconSettingUtils();

        //load popup window layout
        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.reportdetails_popupwindow, null);

        // bind view
        txt_show = contentView.findViewById(R.id.tv_pop_name);
        disaster_logo = contentView.findViewById(R.id.disaster_logo);
        closeBtn = contentView.findViewById(R.id.close_btn);
        report_confirm = contentView.findViewById(R.id.report_confirmed);
        locIntro = contentView.findViewById(R.id.location_intro);
        locDetail = contentView.findViewById(R.id.location_details);
        rtIntro = contentView.findViewById(R.id.rtime_intro);
        rtDetail = contentView.findViewById(R.id.rtime_details);
        htIntro = contentView.findViewById(R.id.htime_intro);
        htDetail = contentView.findViewById(R.id.htime_details);
        typeIntro = contentView.findViewById(R.id.type_intro);
        typeDetail = contentView.findViewById(R.id.type_details);


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
                Fragment newFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.reportdetails_container, newFragment);

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

    public void createReportDetailPopupWindow(ReportInfo[] infos){
        String titleText = infos[index].getReportTitle();
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
        rtIntro.setTypeface(generalType);
        htIntro.setTypeface(generalType);
        typeIntro.setTypeface(generalType);

        Typeface detailsType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");
        locDetail.setTypeface(detailsType);
        locDetail.setText(infos[index].getLocation());
        rtDetail.setTypeface(detailsType);
        rtDetail.setText(infos[index].getReportTime());
        htDetail.setTypeface(detailsType);
        htDetail.setText(infos[index].getHappenTime());
        typeDetail.setTypeface(detailsType);
        typeDetail.setText("unknown");

        report_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Report");
                ref.child(infos[index].getReportNumber()).child("report_state").setValue(1);
                popupWindow.dismiss();

            }
        });

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

        LatLng center = new LatLng(infos[index].getLatitude(), infos[index].getLongitude());
        float zoomLevel;
        zoomLevel = 20f;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
        System.out.println("get here:" + center);
        map.setMapType(MAP_TYPE_NORMAL);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(markerIcon);
        map.addMarker(markerOptions);
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

        homeViewModel.getReportInfo().observe(getActivity(), new Observer<ReportInfo[]>() {
            @Override
            public void onChanged(ReportInfo[] posts) {
                if (posts.length > 0) {
                    popupWindow = popupwindowUtils.showPopwindow(contentView);
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                    popupWindow.setHeight(400);
                    createReportDetailPopupWindow(posts);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

