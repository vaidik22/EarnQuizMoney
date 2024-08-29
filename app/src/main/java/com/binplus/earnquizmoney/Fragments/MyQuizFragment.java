package com.binplus.earnquizmoney.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binplus.earnquizmoney.Adapters.ViewPagerAdapterWallet;
import com.binplus.earnquizmoney.R;
import com.google.android.material.tabs.TabLayout;

public class MyQuizFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapterWallet viewPagerAdapter;

    public MyQuizFragment() {
        // Required empty public constructor
    }

    public static MyQuizFragment newInstance(String param1, String param2) {
        MyQuizFragment fragment = new MyQuizFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_my_quiz, container, false);
        initview(view);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapterWallet(getChildFragmentManager());
        viewPagerAdapter.addFragment(new UpcomingFragment(), "UPCOMING QUIZZES");
        viewPagerAdapter.addFragment(new CompletedQuizFragment(), "COMPLETED QUIZZES");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void initview(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
    }
}