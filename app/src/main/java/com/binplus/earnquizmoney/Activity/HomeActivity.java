package com.binplus.earnquizmoney.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.binplus.earnquizmoney.Fragments.AddMoneyFragment;
import com.binplus.earnquizmoney.Fragments.HomePage;
import com.binplus.earnquizmoney.Fragments.ProfileFragment;
import com.binplus.earnquizmoney.Fragments.HowToPlayFragment;
import com.binplus.earnquizmoney.Fragments.PrivacyPolicyFragment;
import com.binplus.earnquizmoney.Fragments.RankingFragment;
import com.binplus.earnquizmoney.Fragments.ReferralFragment;
import com.binplus.earnquizmoney.Fragments.SupportFragment;
import com.binplus.earnquizmoney.Fragments.TermsAndConditionsFragment;
import com.binplus.earnquizmoney.Fragments.WalletFragment;
import com.binplus.earnquizmoney.Fragments.WithdrawFragment;
import com.binplus.earnquizmoney.Fragments.WithdrawalWalletTransFragment;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.common.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private ImageView menu_icon;
    private ImageView wallet_icon;
    private DrawerLayout drawerLayout;
    private TextView user_name, user_phone;
    private ImageView settings;
    private Common common;
    NavigationView navigationView;
    private Toolbar topNavigation;
    private Toolbar navigationViewToolbar;
    private TextView toolbarTitle;
    ImageView back_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Default Name");
        String userMobile = sharedPreferences.getString("userMobile", "Default Mobile");


        View headerView = navigationView.getHeaderView(0);

        user_name = headerView.findViewById(R.id.user_name);
        user_phone = headerView.findViewById(R.id.user_phone);
        settings = headerView.findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(navigationView);
                topNavigation.setVisibility(View.GONE);
                navigationViewToolbar.setVisibility(View.VISIBLE);
                Fragment selectedFragment = new ProfileFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeFragment, selectedFragment)
                        .commit();
            }
        });
        user_name.setText(userName);
        user_phone.setText(userMobile);

        common = new Common(this);
        bottomNavigationHandler();

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(this);

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });

        wallet_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new AddMoneyFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeFragment, selectedFragment)
                        .commit();
            }
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            topNavigation.setVisibility(View.VISIBLE);
            navigationViewToolbar.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment selectedFragment = new HomePage();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.homeFragment, selectedFragment)
                        .commit();
                topNavigation.setVisibility(View.VISIBLE);
                navigationViewToolbar.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void bottomNavigationHandler() {
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new HomePage();
                    topNavigation.setVisibility(View.VISIBLE);
                    navigationViewToolbar.setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }else if (item.getItemId() == R.id.navigation_add_money) {
                    selectedFragment = new AddMoneyFragment();
                    topNavigation.setVisibility(View.GONE);
                    navigationViewToolbar.setVisibility(View.VISIBLE);
                }
                else if (item.getItemId() == R.id.navigation_my_quiz) {
                    selectedFragment = new RankingFragment();
                    topNavigation.setVisibility(View.GONE);
                    navigationViewToolbar.setVisibility(View.VISIBLE);
                }
                else if (item.getItemId() == R.id.navigation_profile) {
                    selectedFragment = new ProfileFragment();
                    topNavigation.setVisibility(View.GONE);
                    navigationViewToolbar.setVisibility(View.VISIBLE);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, selectedFragment).commit();
                return true;
            }
        });
    }
    public void showBottomNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigation() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    public void showTopNavigation() {
        topNavigation.setVisibility(View.VISIBLE);
        navigationViewToolbar.setVisibility(View.GONE);
    }

    public void hideTopNavigation() {
        topNavigation.setVisibility(View.GONE);
        navigationViewToolbar.setVisibility(View.VISIBLE);
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        menu_icon = findViewById(R.id.menu_icon);
        wallet_icon = findViewById(R.id.wallet_icon);
        navigationView = findViewById(R.id.nav_view);
        topNavigation = findViewById(R.id.top_navigation);
        navigationViewToolbar = findViewById(R.id.navigation_view_toolbar);
        toolbarTitle = navigationViewToolbar.findViewById(R.id.title);
        back_icon = findViewById(R.id.back_icon);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String title = "";

        if (item.getItemId() == R.id.live_games) {
            selectedFragment = new HomePage();
            title = "Live Games";
        }
        if (item.getItemId() == R.id.add_money) {
            selectedFragment = new AddMoneyFragment();
            title = "Add Money";
        }
        if (item.getItemId() == R.id.Wallet) {
            selectedFragment = new WalletFragment();
            title = "Wallet";
        }
        if (item.getItemId() == R.id.Withdraw) {
            selectedFragment = new WithdrawFragment();
            title = "Withdraw";
        }
        if (item.getItemId() == R.id.how_to_play) {
            selectedFragment = new HowToPlayFragment();
            title = "How to Play";
        }
        if (item.getItemId() == R.id.Support) {
            selectedFragment = new SupportFragment();
            title = "Support";
        }
        if (item.getItemId() == R.id.Terms_and_Conditions) {
            selectedFragment = new TermsAndConditionsFragment();
            title = "Terms and Conditions";
        }
        if (item.getItemId() == R.id.Privacy_Policy) {
            selectedFragment = new PrivacyPolicyFragment();
            title = "Privacy Policy";
        }
        if (item.getItemId() == R.id.Refer_and_Earn) {
            selectedFragment = new ReferralFragment();
            title = "Refer and Earn";
        }
        if (item.getItemId() == R.id.Logout) {
            SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();
            finish();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        }

        toolbarTitle.setText(title);
        topNavigation.setVisibility(View.GONE);
        navigationViewToolbar.setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, selectedFragment).commit();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
