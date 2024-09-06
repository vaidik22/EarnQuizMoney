package com.binplus.earnquizmoney.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.binplus.earnquizmoney.Adapters.DetailQuizAdapter;
import com.binplus.earnquizmoney.Adapters.WinningListRankAdapter;
import com.binplus.earnquizmoney.Model.QuizDetailModel;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizDetailFragment extends Fragment implements DetailQuizAdapter.OnFillButtonClickListener {

    private Api apiInterface;
    private RecyclerView recyclerView;
    private DetailQuizAdapter quizAdapter;
    private List<QuizDetailModel.Datum> quizModelItemList;
    private static final String ARG_ID = "id";
    private List<QuizDetailModel.CurrentFill> quizList2;
    private RecyclerView recyclerViewWinning;
    private WinningListRankAdapter quizAdapterWinning;
    private LinearLayout progressBar;
    private LinearLayout main;
    private String id;

    public QuizDetailFragment() {
        // Required empty public constructor
    }
    private void saveIdToPreferences(String id) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("contest_id", id);
        editor.apply();
    }
    public static QuizDetailFragment newInstance(QuizModel.Datum quizModel) {
        QuizDetailFragment fragment = new QuizDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, quizModel.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            saveIdToPreferences(id);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_detail, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        main = view.findViewById(R.id.main);
        recyclerViewWinning = view.findViewById(R.id.rev_winning_list);
        recyclerViewWinning.setLayoutManager(new LinearLayoutManager(recyclerViewWinning.getContext()));
        quizList2 = new ArrayList<>();
        quizAdapterWinning = new WinningListRankAdapter(quizList2);
        recyclerViewWinning.setAdapter(quizAdapterWinning);
        callUpcomingQuizDetailApi();
        quizModelItemList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rev_detail_contest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        quizAdapter = new DetailQuizAdapter(quizModelItemList, this,getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(quizAdapter);
        return view;
    }
    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        main.setVisibility(View.GONE);

    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        main.setVisibility(View.VISIBLE);
    }

    private void callUpcomingQuizDetailApi() {
        showLoading();
        JsonObject params = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");

        params.addProperty("user_id", authId);
        params.addProperty("contest_id", id);

        Call<QuizDetailModel> call = apiInterface.getContestDetailApi(params);
        call.enqueue(new Callback<QuizDetailModel>() {
            @Override
            public void onResponse(Call<QuizDetailModel> call, Response<QuizDetailModel> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    QuizDetailModel quizModel = response.body();
                    ArrayList<QuizDetailModel.Datum> data = quizModel.getData();
                    quizModelItemList.clear();
                    quizModelItemList.addAll(data);
                    quizAdapter.notifyDataSetChanged();

                    // Extract only the "max fill" data
                    List<QuizDetailModel.CurrentFill> maxFillList = extractMaxFillList(data);
                    quizList2.clear();
                    quizList2.addAll(maxFillList);
                    quizAdapterWinning.notifyDataSetChanged();
                } else {
                    Log.e("QuizDetailFragment", "API call unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<QuizDetailModel> call, Throwable t) {
                hideLoading();
                Log.e("QuizDetailFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    private List<QuizDetailModel.CurrentFill> extractMaxFillList(List<QuizDetailModel.Datum> datumList) {
        List<QuizDetailModel.CurrentFill> maxFillList = new ArrayList<>();
        for (QuizDetailModel.Datum datum : datumList) {
            if (datum.getPoints() != null) {
                for (QuizDetailModel.Point point : datum.getPoints()) {
                    QuizDetailModel.CurrentFill currentFill = new QuizDetailModel.CurrentFill();
                    currentFill.setTop_winner(point.getTop_winner());
                    currentFill.setPoints(point.getPoints());
                    maxFillList.add(currentFill);
                }
            }
        }
        return maxFillList;
    }


    private List<QuizDetailModel.CurrentFill> extractCurrentFillList(List<QuizDetailModel.Datum> datumList) {
        List<QuizDetailModel.CurrentFill> currentFillList = new ArrayList<>();
        for (QuizDetailModel.Datum datum : datumList) {
            if (datum.getPoints() != null) {
                for (QuizDetailModel.Point point : datum.getPoints()) {
                    QuizDetailModel.CurrentFill currentFill = new QuizDetailModel.CurrentFill();
                    currentFill.setTop_winner(point.getTop_winner());
                    currentFill.setPoints(point.getPoints());
                    currentFillList.add(currentFill);
                }
            }

            if (datum.getCurrent_fill() != null) {
                currentFillList.addAll(datum.getCurrent_fill());
            }
        }
        return currentFillList;
    }

    @Override
    public void onMaxFillClick(List<QuizDetailModel.Point> points) {
        List<QuizDetailModel.CurrentFill> maxFillList = new ArrayList<>();
        for (QuizDetailModel.Point point : points) {
            QuizDetailModel.CurrentFill currentFill = new QuizDetailModel.CurrentFill();
            currentFill.setTop_winner(point.getTop_winner());
            currentFill.setPoints(point.getPoints());
            maxFillList.add(currentFill);
        }
        quizList2.clear();
        quizList2.addAll(maxFillList);
        quizAdapterWinning.notifyDataSetChanged();
    }

    @Override
    public void onCurrentFillClick(List<QuizDetailModel.CurrentFill> currentFills) {
        quizList2.clear();
        quizList2.addAll(currentFills);
        quizAdapterWinning.notifyDataSetChanged();
    }
}
