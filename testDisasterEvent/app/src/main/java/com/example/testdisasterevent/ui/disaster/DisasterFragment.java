package com.example.testdisasterevent.ui.disaster;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.content.pm.PackageManager;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.data.model.HospitalDetails;
import com.example.testdisasterevent.data.model.TaskDetail;
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

    private DisasterViewModel disasterViewModel;
    private FragmentDisasterBinding binding;
    private PopupWindow popupWindow_task, popupWindow_disaster;
    private View disasterView, taskView, toastView;
    private ImageButton showDisasterButton, showTaskButton;
    private TextView txt_show_disaster, txt_show_task;
    private GoogleMap map;
    private ImageButton closeBtn_disaster, closeBtn_task;
    private ImageView disaster_logo;
    private TextView typeIntro;
    private TextView typeDetail;
    private TextView locIntro;
    private TextView locDetail;
    private TextView ftIntro;
    private TextView ftDetail;
    private TextView injuryIntro;
    private TextView injuryDetail;
    private TextView taskIntro;
    private TextView taskDetail;
    private int index;
    private boolean isPopupWindowShown = false;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        disasterViewModel =
                new ViewModelProvider(this).get(DisasterViewModel.class);

        binding = FragmentDisasterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        showDisasterButton = binding.showPopwindow;
        showTaskButton = binding.showTaskdetails;

        // Map API
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        toastView = LayoutInflater.from(getActivity()).inflate(
                R.layout.view_toast_custom, null);

        disasterView = LayoutInflater.from(getActivity()).inflate(
                R.layout.message_popupwindow, null);
        txt_show_disaster = disasterView.findViewById(R.id.tv_pop_name);
        closeBtn_disaster = disasterView.findViewById(R.id.close_btn);

        // click the close btn, dismiss the popup window
        closeBtn_disaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_disaster.dismiss();
            }
        });
        // click the show btn, open the popup window
        showDisasterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_disaster.showAtLocation(disasterView, Gravity.BOTTOM, 0, 0);
            }
        });
        // Load the custom font from the assets folder
        Typeface topTitleType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        txt_show_disaster.setTypeface(topTitleType);
        txt_show_disaster.setTextSize(25);
        // click the button, show the popup window


        //add second view
        taskView = LayoutInflater.from(getActivity()).inflate(
                R.layout.taskdetails_popupwindow, null);

        txt_show_task = taskView.findViewById(R.id.tv_pop_name);
        disaster_logo = taskView.findViewById(R.id.disaster_logo);
        closeBtn_task = taskView.findViewById(R.id.close_btn1);
        locIntro = taskView.findViewById(R.id.location_intro);
        locDetail = taskView.findViewById(R.id.location_details);
        ftIntro = taskView.findViewById(R.id.ftime_intro);
        ftDetail = taskView.findViewById(R.id.ftime_details);
        typeIntro = taskView.findViewById(R.id.type_intro);
        typeDetail = taskView.findViewById(R.id.type_details);
        injuryIntro = taskView.findViewById(R.id.injury_intro);
        injuryDetail = taskView.findViewById(R.id.injury_details);
        taskIntro = taskView.findViewById(R.id.task_intro);
        taskDetail = taskView.findViewById(R.id.task_details);

        showTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDetail[] posts = disasterViewModel.getTaskDetails().getValue();
                if (posts.length > 0) {
                    popupWindow_task.showAtLocation(taskView, Gravity.BOTTOM, 0, 0);
                } else {
                    String notask =  "No Task Now.";
                    midToast(notask);
                }
            }
        });


        closeBtn_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_task.dismiss();
            }
        });

//        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION }, 100);

        disasterViewModel.getHospitalDetails().observe(getActivity(), new Observer<HospitalDetails[]>() {
            @Override
            public void onChanged(HospitalDetails[] hospitalDetails) {
                disasterViewModel.evaluateHosResource(53.3442016, -6.2544264, 5);
            }
        });

        disasterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
            @Override
            public void onChanged(DisasterDetail[] posts) {
                showDisasterPopwindow();
                if (posts.length > 0) {
                    // Update the UI with the new data
                    createDisasterPopupWindow(posts);
                } else {
                    // Update the UI when no disaster happen
                    createNoDisasterPopWindow();
                }
            }
        });
        disasterViewModel.getTaskDetails().observe(getActivity(), new Observer<TaskDetail[]>() {
            @Override
            public void onChanged(TaskDetail[] posts) {
                if (posts.length > 0) {
                    showTaskPopwindow();
                    popupWindow_task.showAtLocation(taskView, Gravity.BOTTOM, 0, 0);
                    // Update the UI with the new data
                    createTaskDetailsPopupWindow(posts);

                } else {
                    disasterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
                        @Override
                        public void onChanged(DisasterDetail[] posts) {
                            showDisasterPopwindow();
                            popupWindow_disaster.showAtLocation(disasterView, Gravity.BOTTOM, 0, 0);
                            if (posts.length > 0) {
                                // Update the UI with the new data
                                createDisasterPopupWindow(posts);
                            } else {
                                // Update the UI when no disaster happen
                                createNoDisasterPopWindow();
                            }
                        }
                    });

                    String notask =  "No Task Now";
                    midToast(notask);

                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Permission has already been granted
                        // Call setMyLocationEnabled() method here
                        map.setMyLocationEnabled(true);
                    } else {
                        // Request permission from the user
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
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
    

    public void createNoDisasterPopWindow() {
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = disasterView.findViewById(R.id.disasterScrollView);
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
        ScrollView scrollView = disasterView.findViewById(R.id.disasterScrollView);
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
            String titleText = details[i].getDisasterType();
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

                    popupWindow_disaster.dismiss();
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

    public void createTaskDetailsPopupWindow(TaskDetail[] details) {
        String titleText = details[index].getDisasterTitle();
        // Load the custom font from the assets folder
        Typeface topTitleType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        txt_show_task.setTypeface(topTitleType);
        txt_show_task.setTextSize(25);
        txt_show_task.setText("Task");

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
        injuryIntro.setTypeface(generalType);
        injuryIntro.setTextSize(15);
        taskIntro.setTypeface(generalType);
        taskIntro.setTextSize(15);

        Typeface detailsType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");
        locDetail.setTypeface(detailsType);
        locDetail.setTextSize(15);
        locDetail.setText(details[index].getLocation());
        ftDetail.setTypeface(detailsType);
        ftDetail.setTextSize(15);
        ftDetail.setText(details[index].getHappenTime());
        injuryDetail.setTypeface(detailsType);
        injuryDetail.setTextSize(15);
        injuryDetail.setText(details[index].getInjury());
        injuryDetail.setTextColor(Color.RED);

        typeDetail.setTypeface(detailsType);
        typeDetail.setTextSize(15);
        typeDetail.setText(titleText);

        taskDetail.setTypeface(detailsType);
        taskDetail.setTextSize(15);
        taskDetail.setText("unknown");

        // set the title icon resource
        setDisIconResource(titleText);

        // set the title color & text
        setDisTitle(titleText);


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
            txt_show_task.setText("Task");
            txt_show_task.setTextColor(Color.RED);
        } else if (title.equals("Water")) {
            txt_show_task.setText("Task");
            txt_show_task.setTextColor(Color.BLUE);
        } else {
            txt_show_task.setText("Task");
            txt_show_task.setTextColor(Color.BLACK);
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

    private void midToast(String str)
    {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_toast_custom,
                toastView.findViewById(R.id.lly_toast));
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(str);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (popupWindow_disaster != null) {
            popupWindow_disaster.dismiss();
        }
        disasterViewModel.getDisasterDetails().removeObservers(this);
        disasterViewModel.getHospitalDetails().removeObservers(this);
    }

    /**
     * Date: 23.02.07
     * Function: show popupWindow
     * Author: Siyu Liao
     * Version: Week 3
     */
    private void showDisasterPopwindow() {
        popupWindow_disaster = new PopupWindow(disasterView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                700);
        // set SelectPicPopupWindow height
        popupWindow_disaster.setHeight(700);
        // get focus point
        popupWindow_disaster.setFocusable(true);
        // set background color of blank area
        popupWindow_disaster.setBackgroundDrawable(new BitmapDrawable());
        // Click outside to disappear
        popupWindow_disaster.setOutsideTouchable(true);
        // Settings can be clicked
        popupWindow_disaster.setTouchable(true);
        // hidden animation
        popupWindow_disaster.setAnimationStyle(R.style.ipopwindow_anim_style);
    }
    private void showTaskPopwindow() {
        popupWindow_task = new PopupWindow(taskView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                700);
        // set SelectPicPopupWindow height
        popupWindow_task.setHeight(700);
        // get focus point
        popupWindow_task.setFocusable(true);
        // set background color of blank area
        popupWindow_task.setBackgroundDrawable(new BitmapDrawable());
        // Click outside to disappear
        popupWindow_task.setOutsideTouchable(true);
        // Settings can be clicked
        popupWindow_task.setTouchable(true);
        // hidden animation
        popupWindow_task.setAnimationStyle(R.style.ipopwindow_anim_style);
    }
}
