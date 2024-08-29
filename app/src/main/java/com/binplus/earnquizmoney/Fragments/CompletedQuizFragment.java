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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.binplus.earnquizmoney.Adapters.QuizAdapterPast;
import com.binplus.earnquizmoney.Model.BannerModel;
import com.binplus.earnquizmoney.Model.PastModel;
import com.binplus.earnquizmoney.Model.PastModel;
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

public class CompletedQuizFragment extends Fragment {

    private Api apiInterface;
    private ImageSlider image_slider;
    private RecyclerView recyclerView;
    private QuizAdapterPast quizAdapter;
    private List<PastModel.Datum> pastModelItemList;
    private ProgressBar progressBar;
    LinearLayout no_upcoming_contest_layout;
    TextView tv_message;

    public CompletedQuizFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_quiz, container, false);
        initViews(view);
        progressBar.setVisibility(View.VISIBLE);
        callUpcomingQuizApi("past");
        recyclerView = view.findViewById(R.id.completed_Quiz_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pastModelItemList = new ArrayList<>();
        quizAdapter = new QuizAdapterPast(pastModelItemList, getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(quizAdapter);
        return view;
    }

    private void callUpcomingQuizApi(String type) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObject params = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");

        if (type.equalsIgnoreCase("live")) {
            params.addProperty("key", "1");
        }  if (type.equalsIgnoreCase("upcoming")) {
            params.addProperty("key", "0");
        }  if (type.equalsIgnoreCase("past")) {
            params.addProperty("key", "2");
        }
        params.addProperty("user_id", authId);
        params.addProperty("authId", authId);

        Call<PastModel> call = apiInterface.getContestApiPast(params);
        call.enqueue(new Callback<PastModel>() {
            @Override
            public void onResponse(Call<PastModel> call, Response<PastModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PastModel pastModel = response.body();
                    List<PastModel.Datum> data = pastModel.past_contest.data;
                    if(pastModel.getPast_contest().error){
                        no_upcoming_contest_layout.setVisibility(View.VISIBLE);
                        tv_message.setText(pastModel.getPast_contest().getMessage());
                    }
                    else if (data != null) {
                        no_upcoming_contest_layout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        pastModelItemList.clear();
                        pastModelItemList.addAll(data);
                        quizAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        for (PastModel.Datum datum : data) {
                            Log.d("HomePagewefwef", "Datum: " + data);
                        }
                    } else {
                        Log.e("HomePage", "No data available");
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("HomePage", "Response not successful or body is null");
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PastModel> call, Throwable t) {
                Log.e("HomePage", "API call failed: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initViews(View view) {
        image_slider = view.findViewById(R.id.image_slider);
        progressBar = view.findViewById(R.id.progressBar);
        no_upcoming_contest_layout = view.findViewById(R.id.no_upcoming_contest_layout);
        tv_message = view.findViewById(R.id.tv_message);
    }

}
