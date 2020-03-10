package com.uhnux.picturefinder;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uhnux.picturefinder.ui.artists.ArtistsFragment;
import com.uhnux.picturefinder.ui.favorites.FavoritesFragment;
import com.uhnux.picturefinder.ui.home.HomeFragment;
import com.uhnux.picturefinder.util.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.bnm_navigation_home:
                        Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                        break;
                    case R.id.bnm_navigation_artists:
                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new ArtistsFragment()).commit();
                        break;
                    case R.id.bnm_navigation_favorites:
                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        manager.beginTransaction().replace(R.id.nav_host_fragment, new FavoritesFragment()).commit();
                        break;
                }
                return true;
            }
        });
        progressDialog = new ProgressDialog(this, getLayoutInflater());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.bnm_navigation_home, R.id.bnm_navigation_artists, R.id.bnm_navigation_favorites)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void showProgress(boolean show) {
        progressDialog.show(show);
    }

    @Override
    public void showMessage(String title, String message, int type) {
        //new MessageDialog(this, getLayoutInflater()).showMessage(title, message, type);
    }

    public void popView(){
        getSupportFragmentManager().popBackStack();
    }

    public void pushView(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }

}

interface MainView {
    void showProgress(boolean show);
    void showMessage(String title, String message, int type);
}
