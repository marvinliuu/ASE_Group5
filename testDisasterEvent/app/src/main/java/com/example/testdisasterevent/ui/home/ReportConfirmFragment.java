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
    //        private TextView report_injuredInfo;
//        private TextView report_injuredDetail;
//        private TextView otherinfoIntro;
//        private TextView otherinfoDetail;
    private int index;


//    public void setSharedViewModel(DisaterViewModel disasterViewModel) {
//        this.disaterViewModel = disasterViewModel;
//    }


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
//            report_injuredInfo = contentView.findViewById(R.id.report_injured_intro);
//            report_injuredDetail =contentView.findViewById(R.id.report_injured_details);
//            otherinfoIntro = contentView.findViewById(R.id.otherinfo_intro);
//            otherinfoDetail = contentView.findViewById(R.id.otherinfo_details);



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
        locIntro.setTextSize(15);
        rtIntro.setTypeface(generalType);
        rtIntro.setTextSize(15);
        htIntro.setTypeface(generalType);
        htIntro.setTextSize(15);
        typeIntro.setTypeface(generalType);
        typeIntro.setTextSize(15);
//            report_injuredInfo.setTypeface(generalType);
//            report_injuredInfo.setTextSize(15);
//            otherinfoIntro.setTypeface(generalType);
//            otherinfoIntro.setTextSize(15);

//            System.out.println(2222);
//            System.out.println(String.valueOf(infos[index].toString()));
        Typeface detailsType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");
        locDetail.setTypeface(detailsType);
        locDetail.setTextSize(15);
        locDetail.setText(infos[index].getLocation());
        rtDetail.setTypeface(detailsType);
        rtDetail.setTextSize(15);
        rtDetail.setText(infos[index].getReportTime());
        htDetail.setTypeface(detailsType);
        htDetail.setTextSize(15);
        htDetail.setText(infos[index].getHappenTime());
        typeDetail.setTypeface(detailsType);
        typeDetail.setTextSize(15);
        typeDetail.setText("unknown");

        report_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Report");
                ref.child(infos[index].getReportNumber()).child("report_state").setValue(1);
                popupWindow.dismiss();

            }
        });

//            report_injuredDetail.setTypeface(detailsType);
//            report_injuredDetail.setTextSize(15);
//            report_injuredDetail.setText(infos[index].getInjured());
//            otherinfoDetail.setTypeface(detailsType);
//            otherinfoDetail.setTextSize(15);
//            otherinfoDetail.setText(infos[index].getDescription());



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

        LatLng center = new LatLng(infos[index].getLongitude(), infos[index].getLatitude());
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

        homeViewModel.getReportInfo().observe(getActivity(), new Observer<ReportInfo[]>() {
            @Override
            public void onChanged(ReportInfo[] posts) {
                if (posts.length > 0) {
                    showPopwindow();
                    popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
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
