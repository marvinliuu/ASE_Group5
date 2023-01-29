package com.example.testdisasterevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testdisasterevent.databinding.ActivityHomeBinding;
import com.example.testdisasterevent.ui.login.LoginActivity;


public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button signInButton = binding.signIn;
        final TextView registerButton = binding.register;
        registerButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        final ImageView welcome_gif = (ImageView) findViewById(R.id.welcome);
        Glide.with(this).load(R.drawable.test).into(welcome_gif);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(login_intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: NEED TO BE CHANGE TO REGISTER PAGE
                Intent login_intent = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(login_intent);
            }
        });
    }


}