package com.example.testdisasterevent;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.testdisasterevent.data.NotificationFilter;
import com.example.testdisasterevent.data.model.AccountUserInfo;


import com.example.testdisasterevent.ui.disaster.DisasterDetailsFragment;
import com.example.testdisasterevent.ui.disaster.DisasterFragment;
import com.example.testdisasterevent.ui.disaster.DisasterViewModel;
import com.example.testdisasterevent.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testdisasterevent.databinding.ActivityMainBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DisasterViewModel sharedViewModel;
    private HomeViewModel homeViewModel;
    private NotificationFilter notificationFilter;
    public String accountUserInfoJson;
    Gson gson = new Gson();
    public AccountUserInfo accountUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Context context=this;
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        getSupportActionBar().hide();
//              Intent intent;
//        if (savedInstanceState != null) {
//            accountUserInfoJson = savedInstanceState.getString("my_key");
//            Gson gson = new Gson();
//            accountUserInfo = gson.fromJson(accountUserInfoJson, AccountUserInfo.class);
//        } else {
//            intent = getIntent();
//            accountUserInfoJson = intent.getStringExtra("accountUserInfoJson");
//            Gson gson = new Gson();
//            accountUserInfo = gson.fromJson(accountUserInfoJson, AccountUserInfo.class);
//        }

        Intent intent = getIntent();
        accountUserInfoJson = intent.getStringExtra("accountUserInfoJson");
        Gson gson = new Gson();
        accountUserInfo = gson.fromJson(accountUserInfoJson, AccountUserInfo.class);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("TAG", msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
//        bindViewModel();
        notificationFilter = new NotificationFilter();
        Long uid = accountUserInfo.getUid();
        if(uid==null){
            System.out.println("UID is null");
        }
        Log.d("uid",Long.toString(uid));
        notificationFilter.addTaskListener(getApplicationContext(), uid);
//        addReportListener();
    }

//    public void bindViewModel() {
//        // Create an instance of the SharedViewModel
//        sharedViewModel = new ViewModelProvider(this).get(DisasterViewModel.class);
//
//        // Pass the SharedViewModel instance to each of the fragments -- disasterBrief and
//        DisasterFragment disasterBrief = new DisasterFragment();
//        disasterBrief.setSharedViewModel(sharedViewModel);
//
//        DisasterDetailsFragment disasterDetail = new DisasterDetailsFragment();
//        disasterDetail.setSharedViewModel(sharedViewModel);
//    }

    public AccountUserInfo getAccountUserInfo() {
        return accountUserInfo;
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}