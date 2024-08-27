package com.binplus.earnquizmoney.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.binplus.earnquizmoney.Model.ProfileModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BankDetails extends Fragment {
    ArrayList<ProfileModel.Data> profileList = new ArrayList<>();
    EditText et_googlePay_num,et_phonePe_num,et_paytm_num,et_name_on_bank,et_bank_name,et_account_number,et_ifsc_code;
    Api apiInterface;

    public BankDetails() {

    }


    public static BankDetails newInstance(String param1, String param2) {
        BankDetails fragment = new BankDetails();
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

        View view = inflater.inflate(R.layout.fragment_bank_details, container, false);
        initView(view);
        fetchProfileDetails();
        updateUI(profileList);

        return view;
    }

    private void initView(View view) {
        et_googlePay_num = view.findViewById(R.id.et_googlePay_num);
        et_phonePe_num = view.findViewById(R.id.et_phonePe_num);
        et_paytm_num = view.findViewById(R.id.et_paytm_num);
        et_name_on_bank = view.findViewById(R.id.et_name_on_bank);
        et_bank_name = view.findViewById(R.id.et_bank_name);
        et_account_number = view.findViewById(R.id.et_account_number);
        et_ifsc_code = view.findViewById(R.id.et_ifsc_code);

    }
    private void fetchProfileDetails() {
        profileList.clear();
        JsonObject postData = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");
        postData.addProperty("user_id", authId);

        Call<ProfileModel> call = apiInterface.getProfileApi(postData);
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfileModel> call, @NonNull Response<ProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    profileList.add(response.body().getData());
                    updateUI(profileList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileModel> call, @NonNull Throwable t) {
                // Handle failure
            }
        });
    }

    private void updateUI(ArrayList<ProfileModel.Data> profile) {
        if (profile != null && !profile.isEmpty()) {
            ProfileModel.Data profileData = profile.get(0);
            et_googlePay_num.setText(profileData.getGooglepay_number());
            et_phonePe_num.setText(profileData.getPhonepe_number());
            et_paytm_num.setText(profileData.getPaytm_number());
            et_name_on_bank.setText(profileData.getName_on_bank());
            et_bank_name.setText(profileData.getBank_name());
            et_account_number.setText(profileData.getAccount_number());
            et_ifsc_code.setText(profileData.getIfsc_code());
        } else {
            et_googlePay_num.setText("");
            et_phonePe_num.setText("");
            et_paytm_num.setText("");
            et_name_on_bank.setText("");
            et_bank_name.setText("");
            et_account_number.setText("");
            et_ifsc_code.setText("");
        }
    }

}