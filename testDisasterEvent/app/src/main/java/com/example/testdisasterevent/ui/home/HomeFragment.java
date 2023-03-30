package com.example.testdisasterevent.ui.home;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
//import androidx.room.jarjarred.org.stringtemplate.v4.misc.Coordinate;

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.RerouteDataSource;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.ReportInfo;
import com.example.testdisasterevent.databinding.FragmentHomeBinding;
import com.example.testdisasterevent.ui.account.AccountViewModel;
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
import com.google.maps.GeoApiContext;
import com.google.maps.android.data.LineString;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.DirectionsApi.RouteRestriction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class HomeFragment extends Fragment implements OnMapReadyCallback  {

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
    private int index;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        aViewModel =
                new  ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("data_key");
            // Use the data as needed
        }

        showWindowButton = binding.showPopwindow;

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



//        homeViewModel.getReportInfo().observe(getActivity(), new Observer<ReportInfo[]>() {
//
//            @Override
//            public void onChanged(ReportInfo[] posts) {
//                showPopwindow();
//                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
//                if (posts.length > 0) {
//                    // Update the UI with the new data
//                    createReportPopupWindow(posts);
//                } else {
//                    // Update the UI when no disaster happen
//                    createNoReportPopWindow();
//                }}
//        });
//        LatLng centrePoint = new LatLng(53.34453, -6.2542);
//        LatLng[] locations = homeViewModel.calculateDestinationLocations(centrePoint, 0.1);
//        System.out.println(locations);
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
//
        map = googleMap;
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
        if (accountUserInfoData.getUserTypeID() == 0) {
            homeViewModel.getReportInfo().observe(getActivity(), new Observer<ReportInfo[]>() {
                @Override
                public void onChanged(ReportInfo[] posts) {
                    showPopwindow();
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


    public void createReportPopupWindow(ReportInfo[] infos){
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = contentView.findViewById(R.id.disasterReportScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        boolean showReport = false;
        // Create multiple RelativeLayouts with multiple views and add them to the LinearLayout
        for (int i = 0; i < infos.length; i++) {
            //Report state determination
            if (infos[i].getReportState()==0){
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

                ImageView imageView = createReportIconOnWindow(titleText);
                RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                        70, 70);
                imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);

                Bitmap bitmap = createReportIconOnMap(titleText);
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
                        .icon(markerIcon);
                map.addMarker(markerOptions);

                // add view by order
                relativeLayout.addView(title, titleParams);
                relativeLayout.addView(location, locationParams);
                relativeLayout.addView(time, timeParams);
                relativeLayout.addView(imageView, imageParams);


                // set the response event after clicking the RelativeLayout item
                relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = v.getId();
                        Bundle bundle = new Bundle();
                        bundle.putInt("data_key", index);
                        popupWindow.dismiss();
                        homeViewModel.indexOfReportInfo = v.getId();

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



    public ImageView createReportIconOnWindow (String title) {
        // Create and add an ImageView to the RelativeLayout - disaster logo
        ImageView imageView = new ImageView(getContext());
        if (title.equals("Fire")) {
            imageView.setImageResource(R.drawable.fire_logo);
        } else if (title.equals("Water")) {
            imageView.setImageResource(R.drawable.water_logo);
        } else {
            imageView.setImageResource(R.drawable.other_logo);
        }
        return imageView;
    }

    public Bitmap createReportIconOnMap (String title) {
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        popupWindow.dismiss();
        homeViewModel.getReportInfo().removeObservers(this);
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