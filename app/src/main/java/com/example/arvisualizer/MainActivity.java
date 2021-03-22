package com.example.arvisualizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);


        // just iniialize the fragment_container to home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DoodlerFragment()).commit();

    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch(item.getItemId())
                    {
                        case R.id.nav_objplacer:
                            selectedFragment = new ObjplacerFragment();
                            break;

                        case R.id.nav_doodler:
                            selectedFragment = new DoodlerFragment();
                            break;

                        case R.id.nav_filters:
                            selectedFragment = new FiltersFragment();
                            break;

                        case R.id.nav_funmode:
                            selectedFragment = new FunmodeFragment();
                            break;

                    }


                    // it just replace fragment_container to current clicked fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;

                }
            };

}
