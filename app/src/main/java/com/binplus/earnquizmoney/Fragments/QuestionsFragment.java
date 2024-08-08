package com.binplus.earnquizmoney.Fragments;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.binplus.earnquizmoney.Activity.HomeActivity;
import com.binplus.earnquizmoney.Model.QuestionModel;
import com.binplus.earnquizmoney.Model.QuizDetailModel;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.common.Common;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsFragment extends Fragment {

    private TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvTimer,tv_question_num;
    private CardView cardOptionA, cardOptionB, cardOptionC, cardOptionD;
    private LinearLayout btnNext;
    private ArrayList<QuestionModel.Datum> questions;
    private int currentQuestionIndex = 0;
    private CountDownTimer timer;
    Api apiInterface;
    private String id;
    private ProgressBar progressBar;
    CardView question_card;
    private boolean isOptionSelected = false;
    TextView btn_submit;
    Common common;

    public static QuestionsFragment newInstance() {
        return new QuestionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = RetrofitClient.getRetrofitInstance().create(Api.class);
        common = new Common(getActivity());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("contest_id", "Default Id");
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).hideBottomNavigation();
            ((HomeActivity) getActivity()).hideTopNavigation();
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);

        initView(view);

        fetchQuestions();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingFragment fragment = new RankingFragment();
                common.switchFragmentHome(fragment);
            }
        });
        btnNext.setOnClickListener(v -> {
            if (isOptionSelected) {
                if (questions != null && currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion(questions.get(currentQuestionIndex));
                    resetTimer();
                    resetOptions();
                } else {
                    Toast.makeText(getContext(), "No more questions", Toast.LENGTH_SHORT).show();
                    RankingFragment fragment = new RankingFragment();
                    common.switchFragmentHome(fragment);
                }
                isOptionSelected = false;
            } else {
                Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
            }
        });

        cardOptionA.setOnClickListener(v -> selectOption("a", cardOptionA, tvOptionA));
        cardOptionB.setOnClickListener(v -> selectOption("b", cardOptionB, tvOptionB));
        cardOptionC.setOnClickListener(v -> selectOption("c", cardOptionC, tvOptionC));
        cardOptionD.setOnClickListener(v -> selectOption("d", cardOptionD, tvOptionD));

        return view;
    }
    private void selectOption(String selectedOption, CardView selectedCard, TextView selectedText) {
        if (!isOptionSelected) {
            isOptionSelected = true;
            checkAnswer(selectedOption, selectedCard, selectedText);
        }
    }
    private void checkAnswer(String selectedOption, CardView selectedCard, TextView selectedText) {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex < questions.size()) {
            if (questions.get(currentQuestionIndex).getAnswer().equalsIgnoreCase(selectedOption)) {
                // Correct answer
                selectedCard.setCardBackgroundColor(getResources().getColor(R.color.green));
                selectedText.setTextColor(getResources().getColor(R.color.white));
                Toast.makeText(getContext(), "Correct Answer!", Toast.LENGTH_SHORT).show();
            } else {
                // Wrong answer
                selectedCard.setCardBackgroundColor(getResources().getColor(R.color.red));
                selectedText.setTextColor(getResources().getColor(R.color.white));
                Toast.makeText(getContext(), "Wrong Answer!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No questions available", Toast.LENGTH_SHORT).show();
        }
    }
    private void resetOptions() {
        cardOptionA.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOptionB.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOptionC.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardOptionD.setCardBackgroundColor(getResources().getColor(R.color.white));

        tvOptionA.setTextColor(getResources().getColor(R.color.black));
        tvOptionB.setTextColor(getResources().getColor(R.color.black));
        tvOptionC.setTextColor(getResources().getColor(R.color.black));
        tvOptionD.setTextColor(getResources().getColor(R.color.black));
    }
    private void initView(View view) {
        tvQuestion = view.findViewById(R.id.tv_question);
        tvOptionA = view.findViewById(R.id.tv_option_a);
        tvOptionB = view.findViewById(R.id.tv_option_b);
        tvOptionC = view.findViewById(R.id.tv_option_c);
        tvOptionD = view.findViewById(R.id.tv_option_d);
        tvTimer = view.findViewById(R.id.tv_timer);
        cardOptionA = view.findViewById(R.id.card_option_a);
        cardOptionB = view.findViewById(R.id.card_option_b);
        cardOptionC = view.findViewById(R.id.card_option_c);
        cardOptionD = view.findViewById(R.id.card_option_d);
        btnNext = view.findViewById(R.id.btn_next);
        progressBar = view.findViewById(R.id.progress_bar);
        question_card = view.findViewById(R.id.question_card);
        tv_question_num = view.findViewById(R.id.tv_question_num);
        btn_submit = view.findViewById(R.id.btn_submit);
    }

    private void fetchQuestions() {
        showLoading();
        JsonObject params = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");

        params.addProperty("user_id", authId);
        params.addProperty("contest_id", id);

        Call<QuestionModel> call = apiInterface.getQuestionApi(params);
        call.enqueue(new Callback<QuestionModel>() {
            @Override
            public void onResponse(Call<QuestionModel> call, Response<QuestionModel> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body().getData();
                    if (!questions.isEmpty()) {
                        displayQuestion(questions.get(currentQuestionIndex));
                        startTimer();  // Start timer initially
                    } else {
                        Toast.makeText(getContext(), "No questions available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<QuestionModel> call, Throwable t) {
                hideLoading();
                Toast.makeText(getContext(), "Failed to fetch questions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayQuestion(QuestionModel.Datum question) {
        tvQuestion.setText(question.getQuestion());
        tvOptionA.setText(question.getOptiona());
        tvOptionB.setText(question.getOptionb());
        tvOptionC.setText(question.getOptionc().isEmpty() ? "N/A" : question.getOptionc());
        tvOptionD.setText(question.getOptiond().isEmpty() ? "N/A" : question.getOptiond());

        cardOptionC.setVisibility(question.getOptionc().isEmpty() ? View.GONE : View.VISIBLE);
        cardOptionD.setVisibility(question.getOptiond().isEmpty() ? View.GONE : View.VISIBLE);

        tv_question_num.setText(String.format("Question: %d", currentQuestionIndex + 1));
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0");
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion(questions.get(currentQuestionIndex));
                    resetTimer();  // Reset and start the timer for the new question
                } else {
                    Toast.makeText(getContext(), "Quiz completed", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    private void resetTimer() {
        // Cancel the existing timer if any
        if (timer != null) {
            timer.cancel();
        }
        startTimer();
    }


    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        question_card.setVisibility(View.GONE);
        tvTimer.setVisibility(View.GONE);

    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        question_card.setVisibility(View.VISIBLE);
        tvTimer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
    }
}
