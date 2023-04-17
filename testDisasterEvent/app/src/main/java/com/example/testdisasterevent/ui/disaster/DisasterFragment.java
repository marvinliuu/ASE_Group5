package com.example.testdisasterevent.ui.disaster;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.CaseMap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.data.model.HospitalDetails;
import com.example.testdisasterevent.data.model.TaskDetail;
import com.example.testdisasterevent.databinding.FragmentDisasterBinding;
import com.example.testdisasterevent.utils.IconSettingUtils;
import com.example.testdisasterevent.utils.LocationTracker;
import com.example.testdisasterevent.utils.LayoutUtils;
import com.example.testdisasterevent.utils.PopupwindowUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * Date: 23.02.07
 * Function: Disaster Show Fragment - including the map & disaster popup window
 * Author: Siyu Liao
 * Version: Week 3
 */
public class DisasterFragment extends Fragment implements OnMapReadyCallback, LocationTracker.LocationUpdateListener {
    public DisasterViewModel disasterViewModel;  // Type: DisasterViewModel, used for managing disaster-related data
    private FragmentDisasterBinding binding;  // Type: FragmentDisasterBinding, used for binding data to the UI
    private PopupWindow popupWindow_task, popupWindow_disaster;  // Type: PopupWindow, used for displaying pop-up windows
    private View disasterView, taskView, toastView;  // Type: View, used for displaying UI elements
    private ImageButton showDisasterButton, showTaskButton;  // Type: ImageButton, used for displaying buttons
    private TextView txt_show_disaster, txt_show_task;  // Type: TextView, used for displaying text
    private GoogleMap map;  // Type: GoogleMap, used for displaying maps
    private ImageButton closeBtn_disaster, closeBtn_task;  // Type: ImageButton, used for displaying buttons
    private ImageView disaster_logo;  // Type: ImageView, used for displaying images
    private TextView typeIntro, typeDetail, locIntro, locDetail;  // Type: TextView, used for displaying text
    private TextView ftIntro, ftDetail;  // Type: TextView, used for displaying text
    private TextView injuryIntro, injuryDetail;  // Type: TextView, used for displaying text
    private TextView taskIntro, taskDetail;  // Type: TextView, used for displaying text
    private IconSettingUtils iconSettingUtils;  // Type: IconSettingUtils, used for setting icons
    private double currentLatitude;  // Type: double, used for storing the current latitude
    private double currentLongitude;  // Type: double, used for storing the current longitude
    private Marker marker;  // Type: Marker, used for displaying markers on the map
    private LocationTracker locationTracker;  // Type: LocationTracker, used for tracking the user's location
    private PopupwindowUtils popupwindowUtils;  // Type: PopupwindowUtils, used for displaying pop-up windows
    private LayoutUtils layoutUtils;  // Type: LayoutUtils, used for managing the layout of UI elements
    private ImageView task_complete;
    private ImageView task_direction;
    public AccountUserInfo accountUserInfoData;
    public boolean foundNonNullElement = false;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        disasterViewModel =
                new ViewModelProvider(this).get(DisasterViewModel.class);
        String res;

        MainActivity mainActivity = (MainActivity) getActivity();
        accountUserInfoData = mainActivity.getAccountUserInfo();

        // init utils
        iconSettingUtils = new IconSettingUtils();
        popupwindowUtils = new PopupwindowUtils();
        layoutUtils = new LayoutUtils();

        // init layout inflater
        binding = FragmentDisasterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        toastView = LayoutInflater.from(getActivity()).inflate(
                R.layout.view_toast_custom, null);
        disasterView = LayoutInflater.from(getActivity()).inflate(
                R.layout.message_popupwindow, null);
        taskView = LayoutInflater.from(getActivity()).inflate(
                R.layout.taskdetails_popupwindow, null);

        showDisasterButton = binding.showPopwindow;


        showTaskButton = binding.showTaskdetails;
        popupWindow_disaster = popupwindowUtils.showPopwindow(disasterView);

        if(accountUserInfoData!=null && accountUserInfoData.getUserTypeID() == 0){
            showTaskButton.setVisibility(View.INVISIBLE);
        }

        // Map API initialize
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        locationTracker = new LocationTracker(requireContext(), this);
        locationTracker.startLocationUpdates();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        bindSubView();
        //initViewConfig();

        setClickListeners();
        setDataObserver();

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

        return root;
    }


    /**
     * Date: 23.04.06
     * Function: bind all the subViews
     * Author: Siyu Liao
     * Version: Week 11
     */
    private void bindSubView() {
        txt_show_disaster = disasterView.findViewById(R.id.tv_pop_name);
        closeBtn_disaster = disasterView.findViewById(R.id.close_btn);
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
        task_complete=taskView.findViewById(R.id.task_complete);
        task_direction=taskView.findViewById(R.id.task_direction);
    }



    @Override
    public void onLocationUpdated(Location location) {
        Log.i(TAG, String.format("Updated location: Latitude = %f, Longitude = %f",
                location.getLatitude(), location.getLongitude()));
        currentLongitude = location.getLongitude();
        currentLatitude = location.getLatitude();
        LatLng currentPosition = new LatLng(currentLatitude, currentLongitude);
        if (marker == null) {
            marker = map.addMarker(new MarkerOptions().position(currentPosition).title("Your Position"));
        } else {
            marker.setPosition(currentPosition);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
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
    /**
     * Date: 23.03.31
     * Function: set all the button click listeners - task, disaster
     * Author: Siyu Liao
     * Version: Week 10
     */
    private void setClickListeners () {
        // click the show btn, open the task popup window
        showTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDetail[] posts = disasterViewModel.getTaskDetails().getValue();

                if (accountUserInfoData != null) {
                    if (foundNonNullElement == true) {
                        popupWindow_task.showAtLocation(taskView, Gravity.BOTTOM, 0, 0);
                    } else {
                        String notask =  "No Task Now.";
                        midToast(notask);
                    }
                }
            }
        });

        // click the close btn, dismiss the task popup window
        closeBtn_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_task.dismiss();
            }
        });

        // click the close btn, dismiss the disaster popup window
        closeBtn_disaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_disaster.dismiss();
            }
        });

        // click the show btn, open the disaster popup window
        showDisasterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // disaster details observer
                disasterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
                    @Override
                    public void onChanged(DisasterDetail[] posts) {
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
            }
        });

        /**button clicked to complete task and write this officer back to available database
         *and dismiss the popupwindow and give a midtoast
         * and delete the task
         * */

        task_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Task", "task complete success");
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("AvailableOfficer");

                String newNodeKey = myRef.push().getKey();

                Map<String, String> avaOfficerData = new HashMap<>();
                avaOfficerData.put("type", Integer.toString(accountUserInfoData.getUserTypeID()));
                avaOfficerData.put("uid", Long.toString(accountUserInfoData.getUid()));

                myRef.child(newNodeKey).setValue(avaOfficerData);
                Log.d("Task", "task officer write back success");

                popupWindow_task.dismiss();
                midToast("Task completed!");

            }
        });

        /**button clicked to navigate map to the disaster zone
        *
        * */
        task_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Task", "task direction click");
            }
        });


    }

    /**
     * Date: 23.03.31
     * Function: set all the viewmodel data observer
     * Author: Siyu Liao
     * Version: Week 10
     */
    private void setDataObserver() {


        // details observer - task & disaster
        if (accountUserInfoData != null && accountUserInfoData.getUserTypeID() != 0) {
            disasterViewModel.getTaskDetails().observe(getActivity(), new Observer<TaskDetail[]>() {
                @Override
                public void onChanged(TaskDetail[] posts) {
                    for (TaskDetail element : posts) {
                        if (element != null) {
                            foundNonNullElement = true;
                            popupWindow_task = popupwindowUtils.showPopwindow(taskView);
                            popupWindow_task.showAtLocation(taskView, Gravity.BOTTOM, 0, 0);
                            // Update the UI with the new data
                            createTaskDetailsPopupWindow(posts);
                            break;
                        }
                    }

                    if (!foundNonNullElement){
                        String notask = "No Task Now";
                        midToast(notask);
                        disasterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
                            @Override
                            public void onChanged(DisasterDetail[] posts) {
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
                        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            map.setMyLocationEnabled(true);
                        } else {
                            // Request permission from the user
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }
                    }
                }
            });
        }

        else {
            // disaster details observer
            disasterViewModel.getDisasterDetails().observe(getActivity(), new Observer<DisasterDetail[]>() {
                @Override
                public void onChanged(DisasterDetail[] posts) {
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
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    /**
     * Date: 23.04.06
     * Function: create no disaster popup window
     * Author: Siyu Liao
     * Version: Week 11
     */
    public void createNoDisasterPopWindow() {
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = disasterView.findViewById(R.id.disasterScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add the LinearLayout to the ScrollView
        if(scrollView.getChildCount() == 1){
            return;
        }
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


    /**
     * Date: 23.04.06
     * Function: create disasters listing popup window
     * Author: Siyu Liao
     * Version: Week 11
     */
    public void createDisasterPopupWindow(DisasterDetail[] details) {
        // Find the ScrollView in the layout and add content to it
        ScrollView scrollView = disasterView.findViewById(R.id.disasterScrollView);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Add the LinearLayout to the ScrollView
        if(scrollView.getChildCount() == 1){
            return;
        }
        scrollView.addView(linearLayout);

        // Create multiple RelativeLayouts with multiple views and add them to the LinearLayout
        for (int i = 0; i < details.length; i++) {
            // create single RelativeLayout
            RelativeLayout relativeLayout = layoutUtils.createSingleRelativeLayout(i, getContext());

            // Create subview insides the relative layout view
            TextView title = createTitleView();
            TextView location = createLocationView();
            // Create and add a TextView to the RelativeLayout - Time
            TextView time = new TextView(getContext());

            String titleText = details[i].getDisasterType();
            title.setText(titleText);

            location.setText(details[i].getLocation());

            time.setText(details[i].getHappenTime());
            time.setId(View.generateViewId());
            time.setTextColor(Color.BLACK);

            ImageView imageView = iconSettingUtils.createDisIconOnWindow(titleText, getContext());

            // Params Settings
            RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            titleParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            RelativeLayout.LayoutParams locationParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            locationParams.addRule(RelativeLayout.BELOW, title.getId());

            RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeParams.addRule(RelativeLayout.BELOW, location.getId());

            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(70, 70);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_IN_PARENT);

            // add map marker for disaster
            setMapMarkerAboutDisaster(titleText, i, details);

            // add view by order
            relativeLayout.addView(title, titleParams);
            relativeLayout.addView(location, locationParams);
            relativeLayout.addView(time, timeParams);
            relativeLayout.addView(imageView, imageParams);

            // set the response event after clicking the RelativeLayout item
            setDisasterItemClickListener(relativeLayout);

            // Add the RelativeLayout to the LinearLayout
            linearLayout.addView(relativeLayout);
        }
    }

    /**
     * Date: 23.04.05
     * Following Function: create Text View for the popupwindow item - Title, Location
     * Author: Siyu Liao
     * Version: Week 11
     */

    // Create and add a TextView to the RelativeLayout - Title
    private TextView createTitleView() {
        TextView title = new TextView(getContext());
        title.setTextColor(Color.BLACK);
        title.setId(View.generateViewId());
        title.setTextSize(20);
        // Load the custom font from the assets folder
        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        title.setTypeface(customFont);
        return title;
    }

    // Create and add a TextView to the RelativeLayout - Location
    private TextView createLocationView() {
        TextView location = new TextView(getContext());
        location.setId(View.generateViewId());
        location.setTextColor(Color.BLACK);
        return location;
    }

    /**
     * Date: 23.03.31
     * Function: set disaster item click listener
     * Author: Siyu Liao
     * Version: Week 10
     */
    private void setDisasterItemClickListener(RelativeLayout relativeLayout) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = v.getId();
                Bundle bundle = new Bundle();
                bundle.putInt("data_key", index);

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

                popupWindow_disaster.dismiss();
            }
        });

    }

    /**
     * Date: 23.04.06
     * Function: add Disaster popupWindow
     * Author: Siyu Liao
     * Version: Week 11
     */
    private void setMapMarkerAboutDisaster (String titleText, int i, DisasterDetail[] details) {
        Bitmap bitmap = iconSettingUtils.createDisIconOnMap(titleText, getResources());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaledWidth = width * 0.5f;
        float scaledHeight = height * 0.5f;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) scaledWidth, (int) scaledHeight, false);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

        LatLng center = new LatLng(details[i].getLatitude(), details[i].getLongitude());
        float zoomLevel = 15f;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
        map.setMapType(MAP_TYPE_NORMAL);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(markerIcon)
                .anchor(0.5f, 0.5f);
        map.addMarker(markerOptions);
    }

    public void createTaskDetailsPopupWindow(TaskDetail[] details) {
        String titleText = details[0].getDisasterTitle();
        // Load the custom font from the assets folder
        Typeface topTitleType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        // Set the font of the TextView to the custom font
        txt_show_task.setTypeface(topTitleType);
        txt_show_task.setTextSize(25);
        txt_show_task.setText("Task");

        // SET THE INTRODUCTION WORD TEXT STYLE
        setTaskGeneralType();

        locDetail.setText(details[0].getLocation());
        ftDetail.setText(details[0].getHappenTime());
        injuryDetail.setText(Integer.toString(details[0].getInjury()));
        injuryDetail.setTextColor(Color.RED);
        typeDetail.setText(titleText);
        taskDetail.setText("unknown");

        // set the task textview text type
        setTaskDetailsType();

        // set the title icon resource
        iconSettingUtils.setDisIconResource(titleText, disaster_logo);

        // set the title color & text
        iconSettingUtils.setTaskTitle(titleText, txt_show_task);
    }


    /**
     * Date: 23.04.06
     * Function: set Task Popup Window Text Type
     * Author: Siyu Liao
     * Version: Week 11
     */
    private void setTaskDetailsType() {
        Typeface detailsType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_regular.ttf");
        locDetail.setTypeface(detailsType);
        ftDetail.setTypeface(detailsType);
        injuryDetail.setTypeface(detailsType);
        typeDetail.setTypeface(detailsType);
        taskDetail.setTypeface(detailsType);
    }

    private void setTaskGeneralType() {
        // Load the custom font from the assets folder
        // Set the font of the TextView to the custom font
        Typeface generalType = Typeface.createFromAsset(getContext().getAssets(), "alibaba_extrabold.ttf");
        locIntro.setTypeface(generalType);
        ftIntro.setTypeface(generalType);
        typeIntro.setTypeface(generalType);
        injuryIntro.setTypeface(generalType);
        taskIntro.setTypeface(generalType);
    }


    private void midToast(String str) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_toast_custom,
                (ViewGroup) getView().findViewById(R.id.lly_toast));
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(str);
        Toast toast = new Toast(getActivity().getApplicationContext());
        //toast.setGravity(Gravity.CENTER, 0, 10);
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
    }
}
