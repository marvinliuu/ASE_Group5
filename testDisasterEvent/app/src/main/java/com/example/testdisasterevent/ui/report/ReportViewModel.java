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


    public void CitizenSubmit(ReportFromCitizen report){
        reportData.SubmitCitizenReport(report);
        Log.d("num", "citizen submit suc!");
    }


    public void GardaSubmit(ReportFromCitizen report, Context context){
        DisasterDetail disasterData=reportData.Report2Disaster(report);
        Log.d("num", "disaster generate suc!");
        allocationData = GetMLAllocation(disasterData, context);
        if (allocationData == null) return;
        AllocationSubmit(disasterData, allocationData);
        reportData.SubmitGardaReport(disasterData);
        Log.d("num", "garda submit suc!");
    }

    public AllocationDetail GetMLAllocation(DisasterDetail data, Context context){
        ResearchAllocation researchAllocation = new ResearchAllocation();
        float[][] inputData = new float[1][2];
        inputData[0][0] = data.getRadius();
        inputData[0][1] = data.getInjureNum();
        try{
            // (int ambulance, int bus, int police, int fireTruck)
            AllocationDetail singleAllocation = new AllocationDetail(
                    researchAllocation.getAllocation(context, inputData, 1),
                    researchAllocation.getAllocation(context, inputData, 4),
                    researchAllocation.getAllocation(context, inputData,2),
                    researchAllocation.getAllocation(context, inputData, 3));
            return singleAllocation;
        } catch (Exception exception) {
            Log.d("allocation error:" , String.valueOf(exception));
        }
        return null;
    }


    public void AllocationSubmit(DisasterDetail report, AllocationDetail allocationDetail){
        AllocationData.AllocationSubmit(report);
        if (report.getDisasterType() == "fire" && allocationDetail.getFireTruck() == 0) {
            allocationDetail.fireTruck = 1;
        }
        Log.d("allocation", "prepare allocation");
        AllocationData.evaluateAll(report.getLongitude(), report.getLatitude(), allocationDetail.getAmbulance(),allocationDetail.getPolice(),allocationDetail.getFireTruck());
    }
}