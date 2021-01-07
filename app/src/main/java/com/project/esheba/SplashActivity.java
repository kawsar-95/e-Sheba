package com.project.esheba;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tt.whorlviewlibrary.WhorlView;

public class SplashActivity extends AppCompatActivity {
    public static int splashTimeOut = 2200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        WhorlView whorlView = (WhorlView) this.findViewById(R.id.whorl);
        whorlView.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, splashTimeOut);
    }
}