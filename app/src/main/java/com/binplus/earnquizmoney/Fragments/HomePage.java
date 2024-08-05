package com.binplus.earnquizmoney.Fragments;

import static com.binplus.earnquizmoney.BaseURL.BaseURL.BASE_URL_IMAGE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.binplus.earnquizmoney.Adapters.QuizAdapter;
import com.binplus.earnquizmoney.Model.BannerModel;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends Fragment {

    private Api apiInterface;
    private ImageSlider image_slider;
    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private List<QuizModel.Datum> quizModelItemList;
    private ProgressBar progressBar;

    public HomePage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        initViews(view);
        progressBar.setVisibility(View.VISIBLE);
        callImageSliderApi();
        callUpcomingQuizApi("upcoming");
        recyclerView = view.findViewById(R.id.upcoming_Quiz_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        quizModelItemList = new ArrayList<>();
        quizAdapter = new QuizAdapter(quizModelItemList, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(quizAdapter);
        return view;
    }

    private void callUpcomingQuizApi(String type) {
        JsonObject params = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");

        if (type.equalsIgnoreCase("live")) {
            params.addProperty("key", "1");
        } else if (type.equalsIgnoreCase("upcoming")) {
            params.addProperty("key", "0");
        } else if (type.equalsIgnoreCase("past")) {
            params.addProperty("key", "2");
        }
        params.addProperty("user_id", authId);
        params.addProperty("authId", authId);

        Call<QuizModel> call = apiInterface.getContestApi(params);
        call.enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    QuizModel quizModel = response.body();
                    List<QuizModel.Datum> data = quizModel.upcoming_contest.data;
                    if (data != null) {
                        quizModelItemList.clear();
                        quizModelItemList.addAll(data);
                        quizAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar when data is loaded
                        for (QuizModel.Datum datum : data) {
                            Log.d("HomePagewefwef", "Datum: " + data);
                        }
                    } else {
                        Log.e("HomePage", "No data available");
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar if no data available
                    }
                } else {
                    Log.e("HomePage", "Response not successful or body is null");
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar on unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                Log.e("HomePage", "API call failed: " + t.getMessage());
                progressBar.setVisibility(View.GONE); // Hide ProgressBar on failure
            }
        });
    }

    private void initViews(View view) {
        image_slider = view.findViewById(R.id.image_slider);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void callImageSliderApi() {
        Call<BannerModel> call = apiInterface.getBannerApi();
        call.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BannerModel bannerModel = response.body();
                    ArrayList<BannerModel.Datum> data = bannerModel.getData();
                    if (bannerModel.isResponse() && data != null) {
                        List<SlideModel> slideModels = new ArrayList<>();
                        for (BannerModel.Datum datum : data) {
                            String imageUrl = BASE_URL_IMAGE + datum.getImage();
                            Log.d("HomePage", "Image URL: " + imageUrl);
                            slideModels.add(new SlideModel(imageUrl, ScaleTypes.FIT));
                        }
                        image_slider.setImageList(slideModels, ScaleTypes.FIT);
                    } else {
                        Log.e("HomePage", "Response data is not valid");
                    }
                } else {
                    Log.e("HomePage", "Response not successful or body is null");
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Log.e("HomePage", "API call failed: " + t.getMessage());
            }
        });
    }
}
