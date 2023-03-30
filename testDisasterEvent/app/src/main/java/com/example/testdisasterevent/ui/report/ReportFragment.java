package com.example.testdisasterevent.ui.report;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.text.InputType;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.ui.account.AccountViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.example.testdisasterevent.data.ReportDataSource;
import com.example.testdisasterevent.data.model.ReportFromCitizen;
import com.example.testdisasterevent.ui.disaster.DisasterFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;


public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;
    public ReportFromCitizen reportData=new ReportFromCitizen();
    public int AccountType;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        /**
         * AccountType
         * 0 Garda
         * 1 citizen
         * 2 Doctor
         * 3 Fireman
         *
         *
         * */
        MainActivity mainActivity = (MainActivity) getActivity();
        AccountUserInfo accountUserInfoData = mainActivity.getAccountUserInfo();
        AccountType=accountUserInfoData.getUserTypeID();



    }


    public View onCreateView( LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report_garda, container, false);

        if(AccountType!=0)
            rootView = inflater.inflate(R.layout.fragment_report, container, false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            LatLng location = new LatLng(bundle.getDouble("Longitude"), bundle.getDouble("Latitude"));
            //int reportType = bundle.getInt("Type");
            int radius = bundle.getInt("Radius");
            Log.d("Longitude",Double.toString(location.longitude));
            Log.d("Latitude", Double.toString(location.latitude));

            Log.d("Radius", Integer.toString(radius));
            reportData.setLatitude((float)location.latitude);
            reportData.setLongitude((float)location.longitude);
            reportData.setRadius(radius);




        }




        /**
         * disaster single choice click
         * */
        RadioGroup disasterChosen=rootView.findViewById(R.id.report_radioGroup);
        disasterChosen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup disasterChosen, int checkedId) {
                int selectedId = disasterChosen.getCheckedRadioButtonId();
                if (selectedId == R.id.report_fire) {
                    reportData.setDisasterType("Fire");
                    Log.d("Button click", "fire clicked!");
                } else if (selectedId == R.id.report_water) {
                    reportData.setDisasterType("Water");
                    Log.d("Button click", "water clicked!");
                } else if (selectedId == R.id.report_otherevent) {
                    reportData.setDisasterType("Other");
                    Log.d("Button click", "other clicked!");
                }
            }
        });


        /**
         * map Button click
         * default mock geo info and radius
         * ----------
         * for yilun to combine interface
         * */

        ImageView mapIcon = rootView.findViewById(R.id.report_mark_map_icon);
        mapIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView){

                Log.d("Button click", "map clicked!");
                int index = rootView.getId();
                Bundle bundle = new Bundle();
                bundle.putInt("data_key", index);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment reportMapFragment = new ReportMapFragment();
                reportMapFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.report_container, reportMapFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        if(AccountType!=0){
            ImageView cameraIcon = rootView.findViewById(R.id.report_camera);
            cameraIcon.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View rootView){
                    Log.d("Button click", "camera clicked!");

                }
            });
        }
        /**
         * camera Button click
         * ---------
         * to do
         * get the picture, related to image compression
         * */



        /**
         * find injuredNum string
         * */
        EditText injuredNumEditText = rootView.findViewById(R.id.report_injured);


        /**
         * find other info
         * */
        EditText otherInfoEditText = rootView.findViewById(R.id.report_otherinfo);

        /**
         * cnacel Button click
         * clear input string and button
         *
         * */
        ImageView cancelButton = rootView.findViewById(R.id.report_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView) {

                Log.d("Button click", "cancel clicked!");
                injuredNumEditText.setText("");
                otherInfoEditText.setText("");
                disasterChosen.clearCheck();
                //set disasterType as 0 to show not choose state
                reportData.setDisasterType("");
                reportData.setLatitude(0);
                reportData.setLongitude(0);



            }


        });







        /**
         * submit Button click
         * -------------
         * decide whether the form is completed
         * if not
         * get alert message
         * if so
         * transfer to repoertData source and send to database
         * and navigate to report submit successfully page
         * */
        ImageView submitButton = rootView.findViewById(R.id.report_submit);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView){
                Log.d("Button click", "submit clicked!");
                String notification="";
                boolean submitCompleted=true;

                /**
                 * need to refractory?
                 * */


                if(reportData.getDisasterType().toString().trim().length()==0){
                    notification=notification+"DISASTER TYPE ";
                    submitCompleted=false;
                }

                if(reportData.getLatitude()==0||reportData.getLongitude()==0){
                    notification=notification+"LOCATION ";
                    submitCompleted=false;
                }


                if((injuredNumEditText.getText().toString().trim().length() == 0)){
                    Log.d("Button click", "injure problem!");
                    notification=notification+"INJURIED NUMBER ";
                    submitCompleted=false;
                }


                if(otherInfoEditText.getText().toString().trim().length() == 0){
                    notification=notification+"OTHER INFORMATION ";
                    submitCompleted=false;

                }

                if(!submitCompleted){
                    ConfirmationDialogFragment dialog=ConfirmationDialogFragment.newInstance(notification);
                    dialog.show(getFragmentManager(), "checkDialog");
                }

                if(submitCompleted){
                    reportData.setInjuredNum(Integer.parseInt((injuredNumEditText.getText().toString())));
                    reportData.setOtherInfo(otherInfoEditText.getText().toString());
                    if(AccountType!=0){
                        reportViewModel.CitizenSubmit(reportData);
                        replaceFragment(new SubmitSucessFragment());
                    }
                    else{
                        reportViewModel.GardaSubmit(reportData);
                        replaceFragment(new GardaSubmitSucessFragment());
                    }
                }








            }
        });

        return rootView;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);



    }

// navigate to submitSuccess Fragment
    public void replaceFragment(Fragment fragment){

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.report_container, fragment);

        ft.addToBackStack(null);
        ft.commit();


    }





}
