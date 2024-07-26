package com.binplus.earnquizmoney.Fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.binplus.earnquizmoney.R.string.error_in_sign_up;
import static com.binplus.earnquizmoney.R.string.response_body_is_null;
import static com.binplus.earnquizmoney.R.string.response_not_successful;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binplus.earnquizmoney.Activity.HomeActivity;
import com.binplus.earnquizmoney.Model.SignUpModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.bottomsheetOtp.OtpVerificationBottomSheetDialog;
import com.binplus.earnquizmoney.common.Common;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {
    TextView btnGetOtp_mobileNumber;
    TextView btnGetOtp_emailId;
    EditText etMobileNumber;
    EditText etEmail;
    EditText etName;
    TextView textinput_error;
    int delay = 2000;
    ImageView ivBack;
    Button btnSignUp;
    Common common;
    Button btnExistingMember;
    private OtpVerificationBottomSheetDialog otpDialog;
    private Api apiInterface;
    private boolean isMobileOtpVerified = false;
    private boolean isEmailOtpVerified = false;
    private String mobileOtp;
    private String emailOtp;

    public SignUpFragment() {

    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initView(view);
        common = new Common((AppCompatActivity) getActivity());
        allClick();
        return view;
    }

    private void initView(View view) {
        btnGetOtp_mobileNumber = view.findViewById(R.id.btnGetOtp_mobileNumber);
        btnGetOtp_emailId = view.findViewById(R.id.btnGetOtp_emailId);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);
        etEmail = view.findViewById(R.id.etEmail);
        etName = view.findViewById(R.id.etName);
        textinput_error = view.findViewById(R.id.textinput_error);
        ivBack = view.findViewById(R.id.ivBack);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnExistingMember = view.findViewById(R.id.btnExistingMember);
    }

    private void allClick() {
        btnGetOtp_mobileNumber.setOnClickListener(v -> {
            String mobileNumber = etMobileNumber.getText().toString().trim();
            if (!isValidMobileNumber(mobileNumber)) {
                showError(R.string.please_fill_valid_mobile_number);
            } else {
                callSignUpApiMobile();
            }
        });

        btnGetOtp_emailId.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                showError(R.string.please_enter_your_email);
            } else if (!isValidEmail(email)) {
                showError(R.string.enter_valid_email);
            } else {
                showOtpDialog(false);
                callSignUpApiEmail();
            }
        });

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String mobileNumber = etMobileNumber.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty()) {
                showError(R.string.name_required_);
            } else if (!isValidMobileNumber(mobileNumber)) {
                showError(R.string.please_provide_mobile_number);
            } else if (email.isEmpty()) {
                showError(R.string.please_provide_email);
            } else if (!isValidEmail(email)) {
                showError(R.string.enter_valid_email);
            } else if (!isMobileOtpVerified) {
                showError(R.string.please_verify_mobile_otp);
            } else if (!isEmailOtpVerified) {
                showError(R.string.please_verify_email_otp);
            } else {
                callSignUpApiButton();
                SharedPreferences preferences = getActivity().getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("IsLoggedIn", true);
                editor.apply();
            }
        });

        btnExistingMember.setOnClickListener(v -> {
            Fragment fragment = new LoginFragment();
            common.switchFragment(fragment);
        });

        ivBack.setOnClickListener(v -> getActivity().onBackPressed());
    }

    private void callSignUpApiMobile() {
        String mobileNumber = etMobileNumber.getText().toString().trim();
        JsonObject object = new JsonObject();
        object.addProperty("token", Constants.USER_ID);
        object.addProperty("imei", Constants.IMEI_ID);
        object.addProperty("mobile", mobileNumber);
        object.addProperty("key", 1);

        Call<SignUpModel> call = apiInterface.getSignUpApi(object);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                if (response.isSuccessful()) {
                    SignUpModel resp = response.body();
                    if (resp != null) {
                        if (resp.isResponse()) {
                            Toast.makeText(getContext(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (otpDialog != null) {
                                        otpDialog.setOtp(resp.getOtp(), true);
                                    } else {
                                        showOtpDialog(true);
                                        otpDialog.setOtp(resp.getOtp(), true);
                                    }
                                }
                            }, 1000);

                        } else {
                            showError(R.string.mobile_number_already_registered);
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
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                Log.e("onFailure", "Yes", t);
            }
        });
    }

    private void callSignUpApiEmail() {
        String email = etEmail.getText().toString().trim();
        JsonObject object = new JsonObject();
        object.addProperty("token", Constants.USER_ID);
        object.addProperty("imei", Constants.IMEI_ID);
        object.addProperty("email", email);
        object.addProperty("key", 2);

        Call<SignUpModel> call = apiInterface.getSignUpApi(object);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                if (response.isSuccessful()) {
                    SignUpModel resp = response.body();
                    if (resp != null) {
                        if (resp.isResponse()) {
                            Toast.makeText(getContext(), "OTP sent successfully", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (otpDialog != null) {
                                        otpDialog.setOtp(resp.getOtp(), false);
                                    } else {
                                        showOtpDialog(false);
                                        otpDialog.setOtp(resp.getOtp(), false);
                                    }
                                }
                            }, 1000);

                        } else {
                            showError(R.string.email_already_registered);
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
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                Log.e("onFailure", "Yes", t);
            }
        });
    }

    private void callSignUpApiButton() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String mobileNumber = etMobileNumber.getText().toString().trim();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", name);
        editor.putString("userMobile", mobileNumber);
        editor.apply();
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("mobile", mobileNumber);
        object.addProperty("email", email);
        object.addProperty("mobile_otp", mobileOtp);
        object.addProperty("email_otp", emailOtp);
        String refer_code = "GMQ51123";
        object.addProperty("refer_code", refer_code);
        object.addProperty("key", 3);

        Call<SignUpModel> call = apiInterface.getSignUpApi(object);
        call.enqueue(new Callback<SignUpModel>() {
            @Override
            public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                if (response.isSuccessful()) {
                    SignUpModel resp = response.body();
                    if (resp != null) {
                        if (resp.isResponse()) {
                            Toast.makeText(getContext(), R.string.sign_up_successfully, Toast.LENGTH_SHORT).show();
                            navigateToHome();
                        } else {
                            Toast.makeText(getContext(), error_in_sign_up, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), response_body_is_null, Toast.LENGTH_SHORT).show();
                        Log.e("API Response", "Response body is null");
                    }
                } else {
                    Toast.makeText(getContext(), response_not_successful, Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API Error", errorBody);
                    } catch (Exception e) {
                        Log.e("API Error", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpModel> call, Throwable t) {
                Log.e("onFailure", "Yes", t);
            }
        });
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.length() == 10 && mobileNumber.charAt(0) >= '6' && mobileNumber.charAt(0) <= '9';
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showOtpDialog(boolean isMobile) {
        otpDialog = new OtpVerificationBottomSheetDialog();
        otpDialog.setOtpVerificationListener((isMobileOtp, otp) -> {
            if (isMobileOtp) {
                mobileOtp = otp;
                isMobileOtpVerified = true;
            } else {
                emailOtp = otp;
                isEmailOtpVerified = true;
            }
            showError(R.string.otp_verified_successfully);
        });
        otpDialog.setResendOtpListener(isMobile ? this::callSignUpApiMobile : this::callSignUpApiEmail);
        otpDialog.show(getChildFragmentManager(), "OtpVerificationBottomSheetDialog");
    }

    private void showError(int resId) {
        textinput_error.setText(resId);
        textinput_error.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> textinput_error.setVisibility(View.GONE), delay);
    }

    private void navigateToHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
