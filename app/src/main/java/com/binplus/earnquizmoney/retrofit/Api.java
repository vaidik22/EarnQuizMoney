package com.binplus.earnquizmoney.retrofit;


import static com.binplus.earnquizmoney.BaseURL.BaseURL.CONTEST_UPDATE_SCORE;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.GET_BANNER;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.GET_CONFIG;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.GET_CONTEST;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.GET_CONTEST_DETAIL;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.GET_QUESTION_BY_CONTEST;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.LOGIN;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.REFER_EARN;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.SIGN_UP;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.VERIFY_OTP;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.getPrivacy;

import com.binplus.earnquizmoney.Model.BannerModel;
import com.binplus.earnquizmoney.Model.CommonModel;
import com.binplus.earnquizmoney.Model.LoginModel;
import com.binplus.earnquizmoney.Model.QuestionModel;
import com.binplus.earnquizmoney.Model.QuizDetailModel;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.Model.ReferModel;
import com.binplus.earnquizmoney.Model.SignUpModel;
import com.binplus.earnquizmoney.Model.UpdateScoreModel;
import com.binplus.earnquizmoney.Model.VerifyOtpModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Response;
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

    @POST(GET_CONTEST)
    Call<QuizModel> getContestApi(@Body JsonObject postData);

    @POST(REFER_EARN)
    Call<ReferModel> getReferApi(@Body JsonObject postData);

    @GET(GET_BANNER)
    Call<BannerModel> getBannerApi();

    @GET(getPrivacy)
    Call<CommonModel> getPrivacy();

    @GET(GET_CONFIG)
    Call<ConfigModel> getIndexApi();

    @POST(GET_CONTEST_DETAIL)
    Call<QuizDetailModel> getContestDetailApi(@Body JsonObject postData);

    @POST(GET_QUESTION_BY_CONTEST)
    Call<QuestionModel> getQuestionApi(@Body JsonObject postData);

    @POST(CONTEST_UPDATE_SCORE)
    Call<UpdateScoreModel> getContestUpdateScore(@Body JsonObject postData);

}