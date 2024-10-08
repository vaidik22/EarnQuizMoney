package com.binplus.earnquizmoney.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.binplus.earnquizmoney.Adapters.TransactionAdapter;
import com.binplus.earnquizmoney.Model.TransactionModel;
import com.binplus.earnquizmoney.Model.UpdateProfileModel;
import com.binplus.earnquizmoney.Model.WithdrawModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WithdrawFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    TextView textInputError;
    long delay = 3000;
    private ArrayList<TransactionModel.Datum> transactionList;
    private ArrayList<WithdrawModel> withdrawalList;
    Api apiInterface;
    String key = "2";
    TextView available_balance;;
    EditText et_money;
    TextView tv_add_money;

    public WithdrawFragment() {
        // Required empty public constructor
    }

    public static WithdrawalWalletTransFragment newInstance(String param1, String param2) {
        WithdrawalWalletTransFragment fragment = new WithdrawalWalletTransFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
        transactionList = new ArrayList<>();
        withdrawalList = new ArrayList<>();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        available_balance = view.findViewById(R.id.available_balance);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String balance = sharedPreferences.getString("wallet_balance", "0");
        available_balance.setText("Rs."+balance);
        textInputError = view.findViewById(R.id.textinput_error);
        et_money = view.findViewById(R.id.et_money);
        tv_add_money = view.findViewById(R.id.tv_add_money);
        recyclerView = view.findViewById(R.id.rev_withdraw);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter(transactionList, "withdrawal");
        recyclerView.setAdapter(adapter);
        tv_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_money.getText().toString().isEmpty()) {
                    showError("Please Enter Amount");
                }else {
                    sendWithdrawRequest();
                }
            }
        });
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
                        showError("No Data Found");
                    } else {
                        transactionList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionModel> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }private void sendWithdrawRequest() {
        withdrawalList.clear();
        JsonObject postData = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");
        postData.addProperty("user_id", authId);
        postData.addProperty("request_amount",et_money.getText().toString());

        Call<WithdrawModel> call = apiInterface.getWithdrawalRequestApi(postData);
        call.enqueue(new Callback<WithdrawModel>() {
            @Override
            public void onResponse(@NonNull Call<WithdrawModel> call, @NonNull Response<WithdrawModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WithdrawModel updateProfileModel = response.body();
                    String message = updateProfileModel.getMessage();
                     showError(message);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WithdrawModel> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }
    public void showError(String resId) {
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
