package com.example.testdisasterevent;

import android.os.Bundle;

import com.example.testdisasterevent.ui.disaster.DisasterDetailsFragment;
import com.example.testdisasterevent.ui.disaster.DisasterFragment;
import com.example.testdisasterevent.ui.disaster.DisaterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testdisasterevent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DisaterViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        bindViewModel();
    }

    public void bindViewModel() {
        // Create an instance of the SharedViewModel
        sharedViewModel = new ViewModelProvider(this).get(DisaterViewModel.class);

        // Pass the SharedViewModel instance to each of the fragments -- disasterBrief and
        DisasterFragment disasterBrief = new DisasterFragment();
        disasterBrief.setSharedViewModel(sharedViewModel);

        DisasterDetailsFragment disasterDetail = new DisasterDetailsFragment();
        disasterDetail.setSharedViewModel(sharedViewModel);
    }


}