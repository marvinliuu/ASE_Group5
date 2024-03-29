package com.example.testdisasterevent.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.testdisasterevent.R;


public class SubmitSucessFragment extends Fragment {





    public SubmitSucessFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_submit_success,container,false);


        ImageView submitBackButton = root.findViewById(R.id.report_submit_back);

        submitBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View secondView){
                Log.d("Button click", "back clicked!");

                ReportFragment reportFragment = new ReportFragment();
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.report_success, reportFragment);

                ft.addToBackStack(null);
                ft.commit();

            }
        });


        return root;
    }



}

