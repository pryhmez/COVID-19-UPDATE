package com.example.covid_19_update;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
            }
        };

        timer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}



