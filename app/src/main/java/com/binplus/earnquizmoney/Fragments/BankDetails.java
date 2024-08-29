package com.binplus.earnquizmoney.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.binplus.earnquizmoney.Activity.HomeActivity;
import com.binplus.earnquizmoney.Model.ProfileModel;
import com.binplus.earnquizmoney.Model.UpdateProfileModel;
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
    ArrayList<UpdateProfileModel.Data> updatedProfileList = new ArrayList<>();
    EditText et_googlePay_num,et_phonePe_num,et_paytm_num,et_name_on_bank,et_bank_name,et_account_number,et_ifsc_code;
    Api apiInterface;
    AppCompatButton save_button;
    TextView textInputError;
    long delay = 3000;

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
        //set on the title textView on the toolbar of activity home

        ((HomeActivity) getActivity()).setTitleBank();

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    updateProfile();
                }
            }
        });
        fetchProfileDetails();
        updateUI(profileList);

        return view;
    }

    private boolean validation() {
        String nameOnBank = et_name_on_bank.getText().toString().trim();
        String bankName = et_bank_name.getText().toString().trim();
        String accountNumber = et_account_number.getText().toString().trim();
        String ifscCode = et_ifsc_code.getText().toString().trim();


        if (nameOnBank.isEmpty()) {
            showError(R.string.name_on_bank_required);
            et_name_on_bank.requestFocus();
            return false;
        }

        if (bankName.isEmpty()) {
            showError(R.string.bank_name_required);
            et_bank_name.requestFocus();
            return false;
        }

        if (accountNumber.isEmpty()) {
            showError(R.string.account_number_required);
            et_account_number.requestFocus();
            return false;
        }

        if (ifscCode.isEmpty()) {
            showError(R.string.ifsc_code_required);
            et_ifsc_code.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).hideBottomNavigation();
        }
    }

    private void initView(View view) {
        et_googlePay_num = view.findViewById(R.id.et_googlePay_num);
        et_phonePe_num = view.findViewById(R.id.et_phonePe_num);
        et_paytm_num = view.findViewById(R.id.et_paytm_num);
        et_name_on_bank = view.findViewById(R.id.et_name_on_bank);
        et_bank_name = view.findViewById(R.id.et_bank_name);
        et_account_number = view.findViewById(R.id.et_account_number);
        et_ifsc_code = view.findViewById(R.id.et_ifsc_code);
        save_button = view.findViewById(R.id.save_button);
        textInputError = view.findViewById(R.id.textinput_error);

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
    private void updateProfile() {
        updatedProfileList.clear();
        JsonObject postData = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");
        postData.addProperty("update_profile", "1");
        postData.addProperty("user_id", authId);
        postData.addProperty("googlepay_number", et_googlePay_num.getText().toString());
        postData.addProperty("phonepe_number", et_phonePe_num.getText().toString());
        postData.addProperty("paytm_number", et_paytm_num.getText().toString());
        postData.addProperty("name_on_bank", et_name_on_bank.getText().toString());
        postData.addProperty("bank_name", et_bank_name.getText().toString());
        postData.addProperty("account_number", et_account_number.getText().toString());
        postData.addProperty("ifsc_code", et_ifsc_code.getText().toString());


        Call<UpdateProfileModel> call = apiInterface.getUpdateProfileApi(postData);
        call.enqueue(new Callback<UpdateProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateProfileModel> call, @NonNull Response<UpdateProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateProfileModel updateProfileModel = response.body();
                    String message = updateProfileModel.getMessage();
                    showErrorGreen(message);
                    updatedProfileList.add(updateProfileModel.getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateProfileModel> call, @NonNull Throwable t) {
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

    private void showErrorGreen(String resId) {
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