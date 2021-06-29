package com.example.stressdetection.activityScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.stressdetection.R;

public class SignUpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
    }


    public void signIn(View view) {

        startActivity(new Intent(SignUpScreen.this,LoginScreen.class));
    }
}