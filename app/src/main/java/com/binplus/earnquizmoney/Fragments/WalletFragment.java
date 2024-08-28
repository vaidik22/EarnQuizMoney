package com.binplus.earnquizmoney.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binplus.earnquizmoney.Adapters.ViewPagerAdapterWallet;
import com.binplus.earnquizmoney.Model.ProfileModel;
import com.binplus.earnquizmoney.Model.TransactionModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapterWallet viewPagerAdapter;
    ArrayList<TransactionModel.Datum> transactionList = new ArrayList<>();
    Api apiInterface;
    String key = "2";
    ArrayList<ProfileModel.Data> profile = new ArrayList<>();
    TextView available_balance;

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
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);
        initview(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String balance = sharedPreferences.getString("wallet_balance", "0");
        available_balance.setText("Rs."+balance);
       // fetchTransactions();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
    private void fetchTransactions() {
        transactionList.clear();
        JsonObject postData = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");
        postData.addProperty("user_id", authId);
        postData.addProperty("page", "1");
        postData.addProperty("key", key);

        Call<TransactionModel> call = apiInterface.getTransactionApi(postData);
        call.enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(@NonNull Call<TransactionModel> call, @NonNull Response<TransactionModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getData().isEmpty()) {

                    } else {
                        transactionList.addAll(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionModel> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
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
        available_balance = view.findViewById(R.id.available_balance);
    }
}