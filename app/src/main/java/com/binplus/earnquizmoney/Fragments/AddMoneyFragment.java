package com.binplus.earnquizmoney.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Activity.HomeActivity;
import com.binplus.earnquizmoney.Adapters.AddMoneyAdapter;
import com.binplus.earnquizmoney.Interfaces.OnMoneySelectedListener;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.common.Common;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.ConfigModel;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMoneyFragment extends Fragment implements OnMoneySelectedListener, PaymentResultListener {

    private RecyclerView recyclerView;
    private AddMoneyAdapter addMoneyAdapter;
    Api apiService;
    EditText et_money;
    TextView tv_open_wallet;
    Common common;
    TextView tv_add_money;
    TextView available_balance;

    public AddMoneyFragment() {
        // Required empty public constructor
    }

    public AddMoneyFragment newInstance() {
        return new AddMoneyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        common = new Common(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_money, container, false);

        recyclerView = view.findViewById(R.id.rev_money);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        initView(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String balance = sharedPreferences.getString("wallet_balance", "0");
        available_balance.setText("Rs."+balance);
        allClicks();
        fetchAddMoneyValue();

        return view;
    }

    private void allClicks() {
        tv_open_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replace with wallet fragment
                WalletFragment fragment = new WalletFragment();
                common.switchFragmentHome(fragment);

            }
        });
        tv_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    private void initView(View view) {
        et_money = view.findViewById(R.id.et_money);
        tv_open_wallet = view.findViewById(R.id.tv_open_wallet);
        tv_add_money = view.findViewById(R.id.tv_add_money);
        available_balance = view.findViewById(R.id.available_balance);
        Checkout.preload(getContext());
    }
    private void startPayment() {
        String money = et_money.getText().toString().trim();

        if (money.isEmpty()) {
            Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_z8Ua5t06etqliE");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Earn Quiz Money");
            options.put("description", "Add Money to Wallet");
            options.put("currency", "INR");
            options.put("amount", money + "00");
            checkout.open(getActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getContext(), "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
        // Handle successful payment here
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(), "Payment Failed: " + s, Toast.LENGTH_SHORT).show();
        // Handle failed payment here
    }
    private void fetchAddMoneyValue() {
        apiService = RetrofitClient.getRetrofitInstance().create(Api.class);
        Call<ConfigModel> call = apiService.getIndexApi();

        call.enqueue(new Callback<ConfigModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigModel> call, @NonNull Response<ConfigModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfigModel configModel = response.body();
                    String addMoneyValue = configModel.getData().getAdd_money_value();
                    try {
                        List<String> moneyList = parseAddMoneyValue(addMoneyValue);
                        updateAdapter(moneyList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ConfigModel> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    private List<String> parseAddMoneyValue(String addMoneyValue) throws JSONException {
        JSONArray jsonArray = new JSONArray(addMoneyValue);
        List<String> moneyList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            moneyList.add(jsonArray.getString(i));
        }
        return moneyList;
    }

    private void updateAdapter(List<String> moneyList) {
        addMoneyAdapter = new AddMoneyAdapter(moneyList, this);
        recyclerView.setAdapter(addMoneyAdapter);
    }

    @Override
    public void onMoneySelected(String money) {
        et_money.setText(money);
    }
}
