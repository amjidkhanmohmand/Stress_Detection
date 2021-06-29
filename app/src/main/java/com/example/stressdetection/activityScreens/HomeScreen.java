package com.example.stressdetection.activityScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.stressdetection.R;
import com.example.stressdetection.fragments.HomeFragment;
import com.example.stressdetection.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //bottom navigation
        BottomNavigationView navView = findViewById(R.id.bottomNav_view);

//
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
//
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);




        //end of bottom navigation

        NavigationView navigationView = findViewById(R.id.cusNav);
        navigationView.setNavigationItemSelectedListener(this);


        setUpToolbar();
    }

    private void setUpToolbar() {

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //to change color of drawer icon in toolbar
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home: {
                startActivity(new Intent(HomeScreen.this,HomeScreen.class));
                break;
            }
            case R.id.logout: {
                startActivity(new Intent(HomeScreen.this,LoginScreen.class));
                break;
            }
        }

        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchToFragment1() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.homefragment, new HomeFragment()).commit();
    }

//    public void switchToFragment2(){
//        FragmentManager manager = getSupportFragmentManager();
//        manager.beginTransaction().replace(R.id.settingFragment, new SettingFragment()).commit();
//    }

}