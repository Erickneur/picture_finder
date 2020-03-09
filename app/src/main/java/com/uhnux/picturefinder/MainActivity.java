package com.uhnux.picturefinder;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uhnux.picturefinder.util.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements MainView{

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        progressDialog = new ProgressDialog(this, getLayoutInflater());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bnm_navigation_home, R.id.bnm_navigation_artists, R.id.bnm_navigation_favorites)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void showProgress(boolean show) {
        progressDialog.show(show);
    }

    @Override
    public void showMessage(String title, String message, int type) {
        //new MessageDialog(this, getLayoutInflater()).showMessage(title, message, type);
    }
}

interface MainView {
    void showProgress(boolean show);
    void showMessage(String title, String message, int type);
}
