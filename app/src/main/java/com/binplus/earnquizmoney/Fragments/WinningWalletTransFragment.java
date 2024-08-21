package com.binplus.earnquizmoney.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Adapters.TransactionAdapter;
import com.binplus.earnquizmoney.Model.TransactionModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinningWalletTransFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    TextView textInputError;
    long delay = 3000;
    private ArrayList<TransactionModel.Datum> transactionList;
    Api apiInterface;
    String key = "4";

    public WinningWalletTransFragment() {
        // Required empty public constructor
    }

    public static WinningWalletTransFragment newInstance(String param1, String param2) {
        WinningWalletTransFragment fragment = new WinningWalletTransFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
        transactionList = new ArrayList<>();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_winning_wallet_trans, container, false);
        textInputError = view.findViewById(R.id.textinput_error);
        recyclerView = view.findViewById(R.id.winning_wallet_trans_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter(transactionList,"");
        recyclerView.setAdapter(adapter);
        fetchTransactions();

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
                        showError(R.string.no_data_found);
                    } else {
                        transactionList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionModel> call, @NonNull Throwable t) {

            }
        });
    }
    public void showError(int resId) {
        textInputError.setText(resId);
        textInputError.setVisibility(View.VISIBLE);
        textInputError.setBackgroundColor(Color.RED);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textInputError.setVisibility(View.GONE);
            }
        }, delay);
    }

    private void showErrorGreen(int resId) {
        textInputError.setText(resId);
        textInputError.setVisibility(View.VISIBLE);
        textInputError.setBackgroundColor(Color.parseColor("#228B22"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textInputError.setVisibility(View.GONE);
            }
        }, delay);
    }
}
