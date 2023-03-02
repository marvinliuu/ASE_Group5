package com.example.testdisasterevent.ui.disaster;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.databinding.FragmentDisasterBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
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

//    public void setSharedViewModel(DisaterViewModel disasterViewModel) {
//        this.disaterViewModel = disasterViewModel;
//    }

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

        disaterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
            @Override
            public void onChanged(DisasterDetail[] posts) {
                showPopwindow();
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
                if (posts.length > 0) {
                    // Update the UI with the new data
                    createDisasterPopupWindow(posts);
                } else {
                    // Update the UI when no disaste happen
                    createNoDisasterPopWindow();
                }
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
    }

    public void createNoDisasterPopWindow() {
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = contentView.findViewById(R.id.disasterScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add the LinearLayout to the ScrollView
        scrollView.addView(linearLayout);

        // Create and add a TextView to the RelativeLayout - Title
        TextView title = new TextView(getContext());
        title.setText("No Disaster in Dublin");
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

    public void createDisasterPopupWindow(DisasterDetail[] details) {
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = contentView.findViewById(R.id.disasterScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add the LinearLayout to the ScrollView
        scrollView.addView(linearLayout);
        // Create multiple RelativeLayouts with multiple views and add them to the LinearLayout
        for (int i = 0; i < details.length; i++) {
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
            String titleText = details[i].getDisasterTitle();
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
            location.setText(details[i].getLocation());
            location.setId(View.generateViewId());
            location.setTextColor(Color.BLACK);

            RelativeLayout.LayoutParams locationParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            locationParams.addRule(RelativeLayout.BELOW, title.getId());


            // Create and add a TextView to the RelativeLayout - Time
            TextView time = new TextView(getContext());
            time.setText(details[i].getHappenTime());
            time.setId(View.generateViewId());
            time.setTextColor(Color.BLACK);

            RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeParams.addRule(RelativeLayout.BELOW, location.getId());

            ImageView imageView = createDisIconOnWindow(titleText);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                    70, 70);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            Bitmap bitmap = createDisIconOnMap(titleText);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float scaledWidth = width * 0.5f;
            float scaledHeight = height * 0.5f;
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);


            LatLng center = new LatLng(details[i].getLongitude(), details[i].getLatitude());
            float zoomLevel = 15f;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
            map.setMapType(MAP_TYPE_NORMAL);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(center)
                    .icon(markerIcon);
            map.addMarker(markerOptions);

//            // Create scalable disaster range
//            Bitmap rangeBitmap = createDisRangeOnMap();
//            CameraPosition cameraPosition = map.getCameraPosition();
//            float zoom = cameraPosition.zoom;
//            float rr = details[i].getRadius();
//            double scale = 156543.03392f / (Math.pow(2.0, (double)zoom));
//            //int rangeWidth = rangeBitmap.getWidth();
//            //int rangeHeight = rangeBitmap.getHeight();
//            double scaledRange =  details[i].getRadius();
//            Bitmap scaledRangeBitmap = Bitmap.createScaledBitmap(rangeBitmap, (int) (2 * scaledRange), (int) (2 * scaledRange), false);
//            BitmapDescriptor markerIconRange = BitmapDescriptorFactory.fromBitmap(scaledRangeBitmap);



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
                    disaterViewModel.indexOfDisasterDetails = v.getId();
                    // Get a reference to the child FragmentManager
                    FragmentManager fragmentManager = getChildFragmentManager();

                    // Start a new FragmentTransaction
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Replace the current fragment with the new fragment
                    Fragment newFragment = new DisasterDetailsFragment();
                    newFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.disaster_container, newFragment);

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

    public ImageView createDisIconOnWindow (String title) {
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

//    public Bitmap createDisRangeOnMap () {
//        // Create and add an ImageView to the RelativeLayout - disaster range
//        Bitmap bitmap;
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.disaster_range);
//        return bitmap;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        popupWindow.dismiss();
        disaterViewModel.getDisasterDetails().removeObservers(this);
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