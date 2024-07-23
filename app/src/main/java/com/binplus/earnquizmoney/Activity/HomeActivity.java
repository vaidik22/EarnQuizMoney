package com.binplus.earnquizmoney.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.binplus.earnquizmoney.Fragments.AddMoneyFragment;
import com.binplus.earnquizmoney.Fragments.HomePage;
import com.binplus.earnquizmoney.Fragments.MyQuizFragment;
import com.binplus.earnquizmoney.Fragments.ProfileFragment;
import com.binplus.earnquizmoney.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.navigation_home)
                    selectedFragment = new HomePage();
                if(item.getItemId() == R.id.navigation_add_money)
                    selectedFragment = new AddMoneyFragment();
                if(item.getItemId() == R.id.navigation_my_quiz)
                    selectedFragment = new MyQuizFragment();
                if(item.getItemId() == R.id.navigation_profile)
                    selectedFragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, selectedFragment).commit();
                return true;
            }
        });
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }
}
