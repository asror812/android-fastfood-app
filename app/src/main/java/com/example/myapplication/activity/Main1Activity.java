package com.example.myapplication.activity;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.fragment.FavoritesFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.MenuFragment;
import com.example.myapplication.fragment.ProfileFragment;
import com.example.myapplication.fragment.RestaurantsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Main1Activity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_1);

        bottomNav = findViewById(R.id.bottomNav);
        loadFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {

            Fragment fragment = switch (item.getItemId()) {
                case R.id.nav_home -> new HomeFragment();
                case R.id.nav_favorites -> new FavoritesFragment();
                case R.id.nav_menu -> new MenuFragment();
                case R.id.nav_restaurants -> new RestaurantsFragment();
                case R.id.nav_profile -> new ProfileFragment();
                default -> null;
            };

            if (fragment != null) {
                loadFragment(fragment);
            }

            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}
