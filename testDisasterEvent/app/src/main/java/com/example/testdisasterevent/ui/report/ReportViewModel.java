package com.example.testdisasterevent.ui.report;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.testdisasterevent.algorithms.ResearchAllocation;
import com.example.testdisasterevent.data.HosAllocationDataSource;
import com.example.testdisasterevent.data.ReportDataSource;
import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.AllocationDetail;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.example.testdisasterevent.data.model.ReportFromCitizen;

public class ReportViewModel extends ViewModel {
    //public ReportFromCitizen ReportFromC = new ReportFromCitizen();
    public ReportDataSource reportData=new ReportDataSource();
    public HosAllocationDataSource AllocationData = new HosAllocationDataSource();
    public AllocationDetail allocationData=new AllocationDetail();
    private Context context;

    public ReportViewModel(Context context) {
        this.context = context;
    }


    public void CitizenSubmit(ReportFromCitizen report){

        reportData.SubmitCitizenReport(report);


        Log.d("num", "citizen submit suc!");

    }


    public void GardaSubmit(ReportFromCitizen report){



        DisasterDetail disasterData=reportData.Report2Disaster(report);
        Log.d("num", "disaster generate suc!");

        allocationData=GetMLAllocation(disasterData);

        AllocationSubmit(disasterData);

        reportData.SubmitGardaReport(disasterData);


        Log.d("num", "garda submit suc!");

    }

    public AllocationDetail GetMLAllocation(DisasterDetail data){
//        ResearchAllocation researchAllocation = new ResearchAllocation();
//        double[][] input = new double[1][2];
//        input[0][0] = data.getRadius();
//        input[0][1] = data.getInjureNum();
//        researchAllocation.getResource(context, input);
        AllocationDetail singleAllocation=new AllocationDetail();

        return singleAllocation;
    }


    public void AllocationSubmit(DisasterDetail report){
        AllocationData.AllocationSubmit(report);
        Log.d("allocation", "prepare allocation");
        AllocationData.evaluateAll(report.getLongitude(), report.getLatitude(), 2,2,2);
    }







}