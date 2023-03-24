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
import android.widget.TextView;

import android.text.InputType;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.testdisasterevent.MainActivity;
import com.example.testdisasterevent.R;
import com.example.testdisasterevent.ui.account.AccountViewModel;
import com.google.android.gms.maps.model.LatLng;

public class ReportFragment extends Fragment {

    private ReportViewModel reportViewModel;



    public View onCreateView( LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report,container,false);

        Bundle bundle = getArguments();
        if(bundle != null) {
            LatLng location = new LatLng(bundle.getDouble("Longitude"), bundle.getDouble("Latitude"));
            int reportType = bundle.getInt("Type");
            int radius = bundle.getInt("Radius");
//            Log.d("Longitude",Double.toString(location.longitude));
//            Log.d("Latitude",Double.toString(location.latitude));
//            Log.d("Report",Integer.toString(reportType));
        }


        /**
         * enter injuredNum string
         * */
        EditText injuredNumEditText = rootView.findViewById(R.id.report_injured);
        String injuredNum=injuredNumEditText.getText().toString();


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                reportViewModel.editTextChanged(injuredNum);
            }
        };

        injuredNumEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d("input", "other info num enter");
                }
                return false;
            }
        });



        /**
         * pulldown Button click
         * */

        ImageView pulldownIcon = rootView.findViewById(R.id.report_pulldown_icon);
        pulldownIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button click", "Button clicked!");
            }
        });

        /**
         * map Button click
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
                fragmentTransaction.replace(R.id.report_content, reportMapFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        /**
         * camera Button click
         * */
        ImageView cameraIcon = rootView.findViewById(R.id.report_camera);
        cameraIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView){
                Log.d("Button click", "camera clicked!");
                NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                String channelId = "AAA";
                Notification notification = new Notification.Builder(getActivity(), channelId)
                        .setContentTitle("This is a title")
                        .setContentText("This is a text")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .build();
                NotificationChannel channel = new NotificationChannel(channelId, "channelName", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
                manager.notify(1123, notification);
            }
        });


        /**
         * Radius Seekbar
         */
        /**
         * submit Button click
         * */
        ImageView submitButton = rootView.findViewById(R.id.report_submit);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView){
                Log.d("Button click", "submit clicked!");
            }
        });


        /**
         * cancel Button click
         * */
        ImageView cancelButton = rootView.findViewById(R.id.report_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View rootView){
                Log.d("Button click", "cancel clicked!");
            }
        });



        return rootView;
    }








    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

    }

}