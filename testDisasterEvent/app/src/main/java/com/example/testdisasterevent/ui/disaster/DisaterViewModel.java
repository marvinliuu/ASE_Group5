package com.example.testdisasterevent.ui.disaster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testdisasterevent.data.DisasterDataSource;
import com.example.testdisasterevent.data.LoginRepository;
import com.example.testdisasterevent.data.model.DisasterDetail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DisaterViewModel extends ViewModel {
    private DisasterDataSource disasterDataSource;
    public int indexOfDisasterDetails;


    public LiveData<DisasterDetail[]> getDisasterDetails() {
        return disasterDataSource.getDisasterDetails();
    }

    public DisaterViewModel() {
        disasterDataSource = new DisasterDataSource();
    }

}