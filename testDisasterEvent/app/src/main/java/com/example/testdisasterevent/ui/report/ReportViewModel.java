package com.example.testdisasterevent.ui.report;

import android.text.Editable;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.testdisasterevent.data.HosAllocationDataSource;
import com.example.testdisasterevent.data.ReportDataSource;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.data.model.ReportFromCitizen;

public class ReportViewModel extends ViewModel {
    //public ReportFromCitizen ReportFromC = new ReportFromCitizen();
    public ReportDataSource reportData=new ReportDataSource();
    public HosAllocationDataSource AllocationData = new HosAllocationDataSource();


    public void CitizenSubmit(ReportFromCitizen report){

        reportData.SubmitCitizenReport(report);

        Log.d("num", "citizen submit suc!");

    }


    public void GardaSubmit(ReportFromCitizen report){



        DisasterDetail disasterData=reportData.Report2Disaster(report);
        Log.d("num", "disaster generate suc!");
        reportData.SubmitGardaReport(disasterData);


        Log.d("num", "garda submit suc!");

    }

    public void AllocationSubmit(ReportFromCitizen report){
        AllocationData.AllocationSubmit(report);
        AllocationData.evaluateHosResource(report.getLatitude(), report.getLongitude(), 1);
    }







}