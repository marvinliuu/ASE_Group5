package com.example.testdisasterevent.ui.report;

import android.text.Editable;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.testdisasterevent.data.ReportDataSource;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.ReportFromCitizen;

public class ReportViewModel extends ViewModel {
    //public ReportFromCitizen ReportFromC = new ReportFromCitizen();
    public ReportDataSource reportData=new ReportDataSource();



    public void CitizenSubmit(ReportFromCitizen report){

        reportData.reportSubmit(report);

        Log.d("num", "citizen submit suc!");

    }
    public void GardaSubmit(ReportFromCitizen report){

        reportData.reportSubmit(report);

        Log.d("num", "garda submit suc!");

    }






}