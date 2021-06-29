package com.example.stressdetection.activityScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.stressdetection.R;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void signUp(View view) {
        startActivity(new Intent(LoginScreen.this,SignUpScreen.class));
    }

    public void homeScreen(View view) {
        startActivity(new Intent(LoginScreen.this,HomeScreen.class));
    }
}