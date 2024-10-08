package com.binplus.earnquizmoney.Fragments;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    ImageView btnGoogle, btnFacebook;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;

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
        authGoogle();
        common = new Common((AppCompatActivity) getActivity());
        return view;
    }

    private void authGoogle() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), null)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        String phoneNumber = user.getPhoneNumber();
                        if (phoneNumber == null) {
                           showError(R.string.records_not_found);
                           Toast.makeText(getContext(),"Record not found", Toast.LENGTH_SHORT).show();
                           Fragment fragment = new SignUpFragment();
                           common.switchFragment(fragment);
                        } else {
                            checkEmailRegistered(user.getPhoneNumber());
                        }


                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void checkEmailRegistered(String mobileOrEmail) {
        JsonObject object = new JsonObject();
        object.addProperty("mobile", mobileOrEmail);

        Call<LoginModel> call = apiInterface.getLoginApi(object);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()) {
                    LoginModel resp = response.body();
                    if (resp != null && resp.isResponse()) {
                        callVerifyOtpApi();
                        navigateToHome();
                    } else {
                        showError(R.string.records_not_found);
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



    private void initView(View view) {
        btnGetOtp = view.findViewById(R.id.btnGetOtp);
        ivBack = view.findViewById(R.id.ivBack);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);
        textInputError = view.findViewById(R.id.textinput_error);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        btnGoogle = view.findViewById(R.id.btnGoogle);
        btnFacebook = view.findViewById(R.id.btnFacebook);
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
                    SharedPreferences preferences = getActivity().getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("IsLoggedIn", true);
                    editor.apply();
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
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
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
                    if (resp != null && resp.isResponse()) {
                       showErrorGreen(R.string.otp_sent_successfully);
                        showOtpDialog();
                        otpDialog.setOtp(resp.getOtp(), true);
                    } else {
                        showError(R.string.records_not_found);
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
                        Log.e("VerifyOtpModel", resp.toString());
                        if (resp.isResponse()) {
                            String userName = resp.getData().getName();
                            String userMobile = resp.getData().getMobile();
                            String userId = resp.getData().getId();

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userName", userName);
                            editor.putString("userMobile", userMobile);
                            editor.putString("userId", userId);
                            editor.apply();
                            Log.e( "UserId ssfqwf", userId);
                            showErrorGreen(R.string.Sign_Up_Successful);
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
    }private void showErrorGreen(int resId) {
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
        textInputError.setBackgroundColor(Color.parseColor("#228B22"));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textInputError.setVisibility(View.GONE);
            }
        }, delay);
    }

}
