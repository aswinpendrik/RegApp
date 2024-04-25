package com.example.regapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        assert getSupportActionBar() != null;
        getSupportActionBar().hide();

        //tahan splashcreen selama 3 detiks (3000 milisecond)
        new Handler(Looper.myLooper()).postDelayed(() -> {
            //kemudian jalan activity LoginActivity
            startActivity(new Intent(this, LoginActivity.class));
            //hancurkan SplashActivity
            finish();

        }, 3000);
    }
}