package com.binplus.earnquizmoney.Fragments;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.binplus.earnquizmoney.Activity.HomeActivity;
import com.binplus.earnquizmoney.Activity.MainActivity;
import com.binplus.earnquizmoney.Model.QuestionModel;
import com.binplus.earnquizmoney.Model.QuizDetailModel;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.Model.UpdateScoreModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.common.Common;
import com.binplus.earnquizmoney.retrofit.Api;
import com.binplus.earnquizmoney.retrofit.RetrofitClient;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    String startDate;
    String endDate;
    int delay = 2000;
    TextView textinput_error;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int totalQuestions = 0;
    private int score = 0;
    private int questionsAttended = 0;




    public static QuestionsFragment newInstance() {
        return new QuestionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        startDate = df.format(Calendar.getInstance().getTime());
        //log start date
        Log.d("startDate...", startDate);
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
                openDialogBox();
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
                    showError(R.string.no_more_questions);
                    openDialogBox();
                }
                isOptionSelected = false;
            } else {
                showError(R.string.select_an_answer);
            }
        });

        cardOptionA.setOnClickListener(v -> selectOption("a", cardOptionA, tvOptionA));
        cardOptionB.setOnClickListener(v -> selectOption("b", cardOptionB, tvOptionB));
        cardOptionC.setOnClickListener(v -> selectOption("c", cardOptionC, tvOptionC));
        cardOptionD.setOnClickListener(v -> selectOption("d", cardOptionD, tvOptionD));

        return view;
    }
    private void openDialogBox() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_maintain);
        Button btn_ok;
        Button btn_no;

        btn_ok = dialog.findViewById(R.id.btn_yes);
        btn_no = dialog.findViewById(R.id.btn_no);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDialogBoxCongrats();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          dialog.dismiss();
                                      }
                                  });

        dialog.show();
    }
    private void openDialogBoxCongrats() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        endDate = df.format(Calendar.getInstance().getTime());
        Log.d("endDate...", endDate);
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_congrats);
        Button btn_ok;

        btn_ok = dialog.findViewById(R.id.btn_yes);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUpdateScoreApi();
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
                showErrorgreen(R.string.quiz_completed);
            }
        });
        dialog.show();
    }
    private void selectOption(String selectedOption, CardView selectedCard, TextView selectedText) {
        if (!isOptionSelected) {
            isOptionSelected = true;
            checkAnswer(selectedOption, selectedCard, selectedText);
        }
    }
    private void checkAnswer(String selectedOption, CardView selectedCard, TextView selectedText) {
        if (questions != null && !questions.isEmpty() && currentQuestionIndex < questions.size()) {
            totalQuestions++;
            if (questions.get(currentQuestionIndex).getAnswer().equalsIgnoreCase(selectedOption)) {
                correctAnswers++;
                score += 1;
                selectedCard.setCardBackgroundColor(getResources().getColor(R.color.green));
                selectedText.setTextColor(getResources().getColor(R.color.white));
                showErrorgreen(R.string.correct_answer);
            } else {
                incorrectAnswers++;
                selectedCard.setCardBackgroundColor(getResources().getColor(R.color.red));
                selectedText.setTextColor(getResources().getColor(R.color.white));
                showError(R.string.wrong_answer);
            }
        } else {

            showError(R.string.no_more_questions);
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
        textinput_error = view.findViewById(R.id.textinput_error);
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
                        startTimer();
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

    private void callUpdateScoreApi(){
        JsonObject params = new JsonObject();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String authId = sharedPreferences.getString("userId", "Default Id");

        params.addProperty("user_id", authId);
        params.addProperty("contest_id", id);
        params.addProperty("start_time", startDate);
        params.addProperty("end_time", endDate);
        params.addProperty("questions_answered", totalQuestions);
        params.addProperty("questions_attended", questionsAttended);
        params.addProperty("correct_answers", correctAnswers);
        params.addProperty("incorrect_answers", incorrectAnswers);
        params.addProperty("score", score);

        Call<UpdateScoreModel> call = apiInterface.getContestUpdateScore(params);
        call.enqueue(new Callback<UpdateScoreModel>() {
            @Override
            public void onResponse(Call<UpdateScoreModel> call, Response<UpdateScoreModel> response) {
                if (response.isSuccessful()) {
                    if (getActivity() != null) {
                        showErrorgreen(R.string.score_updated);
                    }
                } else {
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "Failed to update score", Toast.LENGTH_SHORT).show();
                        showError(R.string.failed_to_update_score);
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateScoreModel> call, Throwable t) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
        questionsAttended++;
        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
    }


    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(30000, 1000){
            public void onTick(long millisUntilFinished) {
                // Update the timer UI
                tvTimer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                // 2. Handle timer completion
                showTimeExceededDialog();
            }
        }.start();
    }

    // 3. Display the dialog box
    private void showTimeExceededDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Time's up!")
                .setMessage("The time for this question has expired. Moving to the next question.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 4. Automatically move to the next question
                        moveToNextQuestion();
                    }
                })
                .show();
    }

    // 4. Method to move to the next question
    private void moveToNextQuestion() {
        // Logic to move to the next question
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(questions.get(currentQuestionIndex));
            resetTimer();
        } else {
            Toast.makeText(getContext(), "Quiz completed", Toast.LENGTH_SHORT).show();
            openDialogBoxCongrats();
        }
    }

    private void resetTimer() {
        // Cancel the existing timer if any
        if (timer != null) {
            timer.cancel();
        }
        startTimer();
    }

    private void showError(int resId) {
        textinput_error.setText(resId);
        textinput_error.setVisibility(View.VISIBLE);
        textinput_error.setBackgroundColor(Color.RED);
        new Handler().postDelayed(() -> textinput_error.setVisibility(View.GONE), delay);
    }
    private void showErrorgreen(int resId) {
        textinput_error.setText(resId);
        textinput_error.setVisibility(View.VISIBLE);
        textinput_error.setBackgroundColor(Color.parseColor("#228B22"));
        new Handler().postDelayed(() -> textinput_error.setVisibility(View.GONE), delay);
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
