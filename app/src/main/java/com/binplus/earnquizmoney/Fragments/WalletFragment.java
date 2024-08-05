package com.binplus.earnquizmoney.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binplus.earnquizmoney.Adapters.ViewPagerAdapterWallet;
import com.binplus.earnquizmoney.R;
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class WalletFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapterWallet viewPagerAdapter;

    public WalletFragment() {
        // Required empty public constructor
    }
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);
        initview(view);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    return  view;
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapterWallet(getChildFragmentManager());
        viewPagerAdapter.addFragment(new AllWalletTransFragment(), "All");
        viewPagerAdapter.addFragment(new AddMoneyWalletTransFragment(), "Add Money");
        viewPagerAdapter.addFragment(new WithdrawalWalletTransFragment(), "Withdrawal");
        viewPagerAdapter.addFragment(new ReferralWalletTransFragment(), "Referral");
        viewPagerAdapter.addFragment(new WinningWalletTransFragment(), "Winning");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void initview(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
    }
}