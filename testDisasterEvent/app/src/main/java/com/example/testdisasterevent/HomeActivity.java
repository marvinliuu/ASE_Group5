package com.example.testdisasterevent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testdisasterevent.databinding.ActivityHomeBinding;
import com.example.testdisasterevent.ui.login.LoginActivity;
import com.example.testdisasterevent.ui.register.RegisterActivity;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConstraintLayout constraintLayout = findViewById(R.id.container);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        final Button loginButton = binding.logIn;
        final TextView registerButton = binding.signUp;
        final ImageView welcome_gif = (ImageView) findViewById(R.id.welcome);
        Glide.with(this).load(R.drawable.test).into(welcome_gif);
        final ImageView logo = (ImageView) findViewById(R.id.app_logo);
        Glide.with(this).load(R.drawable.disaster_fire_logo).into(logo);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(HomeActivity.this, RegisterActivity.class);
                startActivity(register_intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: NEED TO BE CHANGE TO REGISTER PAGE
                Intent login_intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(login_intent);
            }
        });
    }


}