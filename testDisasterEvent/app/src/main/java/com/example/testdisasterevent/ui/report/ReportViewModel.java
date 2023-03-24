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
    private MutableLiveData<ReportFromCitizen> ReportFromC = new MutableLiveData<>();
    public ReportDataSource reportData=new ReportDataSource();



    public void submit(ReportFromCitizen report){

        reportData.reportSubmit(report);

        Log.d("num", "submit suc!");

    }





}