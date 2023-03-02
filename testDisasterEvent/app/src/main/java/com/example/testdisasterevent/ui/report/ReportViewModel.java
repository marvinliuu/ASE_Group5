package com.example.testdisasterevent.ui.report;

import android.text.Editable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.testdisasterevent.data.model.AccountUserInfo;
import com.example.testdisasterevent.data.model.ReportFromCitizen;

public class ReportViewModel extends ViewModel {
    private MutableLiveData<ReportFromCitizen> ReportFromC = new MutableLiveData<>();
    public ReportFromCitizen report;



    public void editTextChanged(String injuredNum){
        report.injuredNum=injuredNum;
        //report.otherInfo=otherInfo;


    }

}