package com.example.stressdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.stressdetection.activityScreens.LoginScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Thread background = new Thread(){
                public void run(){
                    try {
                        sleep(3*1000);
                        startActivity(new Intent(MainActivity.this, LoginScreen.class));
                        finish();
                    } catch (Exception e) {

                    }
                }
            };
            background.start();

    }
}