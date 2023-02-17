package com.example.testdisasterevent.ui.disaster;

import android.Manifest;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.databinding.FragmentDisasterBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Date: 23.02.07
 * Function: Disaster Show Fragment - including the map & disaster popup window
 * Author: Siyu Liao
 * Version: Week 3
 */
public class DisasterFragment extends Fragment implements OnMapReadyCallback {

    private DisaterViewModel disaterViewModel;
    private FragmentDisasterBinding binding;
    private PopupWindow popupWindow;
    private View contentView;
    private ImageButton showWindowButton;
    private TextView txt_show;
    private GoogleMap map;
    private ImageButton closeBtn;



    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        disaterViewModel =
                new ViewModelProvider(this).get(DisaterViewModel.class);

        binding = FragmentDisasterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        showWindowButton = binding.showPopwindow;

        // Map API
        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, 100);

        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft= fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        // click the button, show the popup window
        showWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
            }
        });

        return root;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        /**
         * SET LAT / LONG VALUE
         */
        map = googleMap;
        LatLng sydney = new LatLng(-37.812439, 144.972755);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));

        googleMap.addMarker(new MarkerOptions()
                .position(sydney));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //load popup window layout
        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.message_popupwindow, null);

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

        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = contentView.findViewById(R.id.disasterScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add the LinearLayout to the ScrollView
        scrollView.addView(linearLayout);

        // Create multiple RelativeLayouts with multiple views and add them to the LinearLayout
        for (int i = 1; i <= 10; i++) {
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW, relativeLayout.getId());
            params2.setMargins(0, 0, 0, 16); // set a top margin of 16dp
            relativeLayout.setLayoutParams(params2);

            // Create and add a TextView to the RelativeLayout - Title
            TextView title = new TextView(getContext());
            title.setText("Fire");
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
            location.setText("Trinity College Dublin");
            location.setId(View.generateViewId());
            location.setTextColor(Color.BLACK);

            RelativeLayout.LayoutParams locationParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            locationParams.addRule(RelativeLayout.BELOW, title.getId());


            // Create and add a TextView to the RelativeLayout - Time
            TextView time = new TextView(getContext());
            time.setText("Item " + i);
            time.setId(View.generateViewId());
            time.setTextColor(Color.BLACK);

            RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeParams.addRule(RelativeLayout.BELOW, location.getId());

            // Create and add an ImageView to the RelativeLayout - disaster logo
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.fire_logo);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                    70, 70);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fire_logo);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float scaledWidth = width * 0.5f;
            float scaledHeight = height * 0.5f;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

            // add view by order
            relativeLayout.addView(title, titleParams);
            relativeLayout.addView(location, locationParams);
            relativeLayout.addView(time, timeParams);
            relativeLayout.addView(imageView, imageParams);

            // set the response event after clicking the RelativeLayout item
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent disaster_detail_intent = new Intent(getActivity(), DisasterDetailsActivity.class);
//                    startActivity(disaster_detail_intent);

                    LatLng center = new LatLng(37.7749, -122.4194);
                    float zoomLevel = 15f;
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(center)
                            .title("San Francisco")
                            .snippet("A beautiful city!")
                            .icon(markerIcon);
                    map.addMarker(markerOptions);
                }
            });

            // Add the RelativeLayout to the LinearLayout
            linearLayout.addView(relativeLayout);
        }

        showPopwindow();
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
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