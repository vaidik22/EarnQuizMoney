package com.binplus.earnquizmoney.retrofit;


import static com.binplus.earnquizmoney.BaseURL.BaseURL.LOGIN;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.SIGN_UP;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.VERIFY_OTP;

import com.binplus.earnquizmoney.Model.LoginModel;
import com.binplus.earnquizmoney.Model.SignUpModel;
import com.binplus.earnquizmoney.Model.VerifyOtpModel;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @POST(SIGN_UP)
    Call<SignUpModel> getSignUpApi(@Body JsonObject postData);

    @POST(LOGIN)
    Call<LoginModel> getLoginApi(@Body JsonObject postData);

    @POST(VERIFY_OTP)
    Call<VerifyOtpModel> getVerifyOtpApi(@Body JsonObject postData);
}