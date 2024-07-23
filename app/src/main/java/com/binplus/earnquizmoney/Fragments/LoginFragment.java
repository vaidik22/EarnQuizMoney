package com.binplus.earnquizmoney.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binplus.earnquizmoney.Activity.HomeActivity;
import com.binplus.earnquizmoney.Model.LoginModel;
import com.binplus.earnquizmoney.Model.SignUpModel;
import com.binplus.earnquizmoney.Model.VerifyOtpModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.bottomsheetOtp.OtpVerificationBottomSheetDialog;
import com.binplus.earnquizmoney.common.Common;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements OtpVerificationBottomSheetDialog.OtpVerificationListener {
    TextView btnGetOtp;
    ImageView ivBack;
    EditText etMobileNumber;
    TextView textInputError;
    Button btnLogin;
    Button btnCreateAccount;
    Common common;
    Api apiInterface ;

    private ArrayList<SignUpModel> mModel;
    private static final long delay = 2000;

    private OtpVerificationBottomSheetDialog otpDialog;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        allClick();
        common = new Common(getActivity());
        return view;
    }

    private void initView(View view) {
        btnGetOtp = view.findViewById(R.id.btnGetOtp);
        ivBack = view.findViewById(R.id.ivBack);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);
        textInputError = view.findViewById(R.id.textinput_error);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        mModel = new ArrayList<>();
    }

    private void allClick() {
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = etMobileNumber.getText().toString().trim();

                if (!isValidMobileNumber(mobileNumber)) {
                    showError(R.string.please_fill_valid_mobile_number);
                } else {
                    callLoginApi();
                    showOtpDialog();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpDialog == null || !otpDialog.isOtpVerified()) {
                    showError(R.string.please_verify_otp);
                } else {
                    callVerifyOtpApi();
                }
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignUpFragment();
                common.switchFragment(fragment);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
    private void callLoginApi() {
        String mobileNumber = etMobileNumber.getText().toString().trim();
        JsonObject object = new JsonObject();
        object.addProperty("mobile", mobileNumber);

        Call<LoginModel> call = apiInterface.getLoginApi(object);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()) {
                    LoginModel resp = response.body();
                    if (resp != null) {
                        if (resp.isResponse()) {
                            Toast.makeText(getContext(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
                            if (otpDialog != null) {
                                otpDialog.setOtp(resp.getOtp(), true);
                            }
                        } else {
                            Toast.makeText(getContext(), "Records Not Found", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                        Log.e("API Response", "Response body is null");
                    }
                } else {
                    Toast.makeText(getContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API Error", errorBody);
                    } catch (Exception e) {
                        Log.e("API Error", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Log.e("onFailure", "Yes", t);
            }
        });
    }

    private void callVerifyOtpApi() {
        String mobileNumber = etMobileNumber.getText().toString().trim();
        String otp = otpDialog != null ? otpDialog.getGeneratedOtp() : "";
        JsonObject object = new JsonObject();
        object.addProperty("is_login", 1);
        object.addProperty("mobile_email", mobileNumber);
        object.addProperty("otp", otp);
        object.addProperty("key", 1);
        object.addProperty("imei", Constants.IMEI_ID);
        object.addProperty("token", Constants.USER_ID);
        Log.e("OTP", otp);

        Call<VerifyOtpModel> call = apiInterface.getVerifyOtpApi(object);
        call.enqueue(new Callback<VerifyOtpModel>() {
            @Override
            public void onResponse(Call<VerifyOtpModel> call, Response<VerifyOtpModel> response) {
                if (response.isSuccessful()) {
                    VerifyOtpModel resp = response.body();
                    if (resp != null) {
                        Log.e("VerifyOtpModel", resp.toString()); // Log the entire model
                        if (resp.isResponse()) {
                            Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            navigateToHome();
                        } else {
                            Toast.makeText(getContext(), "Error in Sign Up", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                        Log.e("API Response", "Response body is null");
                    }
                } else {
                    Toast.makeText(getContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API Error", errorBody);
                    } catch (Exception e) {
                        Log.e("API Error", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpModel> call, Throwable t) {
                Log.e("onFailure", "Yes", t);
            }
        });
    }



    private void showOtpDialog() {
        otpDialog = new OtpVerificationBottomSheetDialog();
        otpDialog.setOtpVerificationListener(this);
        otpDialog.show(getFragmentManager(), "OtpVerificationBottomSheetDialog");
    }

    private void showError(int resId) {
        textInputError.setText(resId);
        textInputError.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textInputError.setVisibility(View.GONE);
            }
        }, delay);
    }

    private void navigateToHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber.length() != 10) {
            return false;
        }

        char firstChar = mobileNumber.charAt(0);
        if (firstChar < '6' || firstChar > '9') {
            return false;
        }

        return true;
    }

    @Override
    public void onOtpVerified(boolean isMobileOtp, String otp) {
        textInputError.setText(getString(R.string.otp_verified_successfully));
        textInputError.setVisibility(View.VISIBLE);
        textInputError.setBackgroundColor(Color.GREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textInputError.setVisibility(View.GONE);
            }
        }, delay);
    }

}
