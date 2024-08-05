package com.binplus.earnquizmoney.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.widget.AppCompatButton;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class QuizDetailFragment extends Fragment {

    private AppCompatButton btnMaxFill;
    private AppCompatButton btnCurrentFill;
    ImageView quizImage;
    TextView quizTitle, quizTime, joiningFees, maxEntryDetail, availableSpots, prizePoolDetail,view_more,end_time;

    private static final String ARG_NAME = "name";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_MAX_ENTRY = "max_entry";
    private static final String ARG_AVAILABLE_SPOT = "available_spot";
    private static final String ARG_PRIZE_POOL = "prize_pool";
    private static final String ARG_START_DATE = "start_date";
    private static final String ARG_END_DATE = "end_date";

    private String name;
    private String image;
    private String maxEntry;
    private String availableSpot;
    private String prizePool;
    private String startDate;
    private String endDate;

    public QuizDetailFragment() {
        // Required empty public constructor
    }

    public static QuizDetailFragment newInstance(QuizModel.Datum quizModel) {
        QuizDetailFragment fragment = new QuizDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, quizModel.getName());
        args.putString(ARG_IMAGE, quizModel.getImage());
        args.putString(ARG_MAX_ENTRY, quizModel.getMax_entry());
        args.putString(ARG_AVAILABLE_SPOT, quizModel.getAvailable_spot());
        args.putString(ARG_PRIZE_POOL, quizModel.getPrize_pool());
        args.putString(ARG_START_DATE, quizModel.getStart_date());
        args.putString(ARG_END_DATE, quizModel.getEnd_date());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            image = getArguments().getString(ARG_IMAGE);
            maxEntry = getArguments().getString(ARG_MAX_ENTRY);
            availableSpot = getArguments().getString(ARG_AVAILABLE_SPOT);
            prizePool = getArguments().getString(ARG_PRIZE_POOL);
            startDate = getArguments().getString(ARG_START_DATE);
            endDate = getArguments().getString(ARG_END_DATE);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_detail, container, false);

        initView(view);
        btnMaxFill.setBackgroundColor(getResources().getColor(R.color.black));
        btnMaxFill.setTextColor(getResources().getColor(R.color.white));
        btnCurrentFill.setBackgroundColor(getResources().getColor(R.color.grey));
        btnCurrentFill.setTextColor(getResources().getColor(R.color.black));
        allClicks();
        setViews();


        return view;
    }

    private void setViews() {
        if (image != null && !image.isEmpty()) {
            Picasso.get()
                    .load(image)
                    .placeholder(R.drawable.ic_piggy_bank) // Add a placeholder image in drawable folder
                    .error(R.drawable.ic_piggy_bank) // Add an error image in drawable folder
                    .into(quizImage);
        } else {
            quizImage.setImageResource(R.drawable.ic_piggy_bank); // Set an error image if URL is null
        }

        quizTitle.setText(name);
        maxEntryDetail.setText(maxEntry + " " + "People");
        availableSpots.setText(availableSpot);
        prizePoolDetail.setText("Rs." + prizePool + "/-");
        joiningFees.setText("Rs.2/-");
        quizTime.setText(startDate);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

        LocalDateTime startTime = LocalDateTime.parse(startDate, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(endDate, dateTimeFormatter);

        long durationMillis = ChronoUnit.MILLIS.between(LocalDateTime.now(), endTime);

        if (durationMillis > 0) {
            new CountDownTimer(durationMillis, 1000) {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    long hours = millisUntilFinished / (1000 * 60 * 60);
                    long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                    long seconds = (millisUntilFinished % (1000 * 60)) / 1000;
                    end_time.setText(String.format("%02d : %02d : %02d", hours, minutes, seconds));
                }

                @Override
                public void onFinish() {
                    end_time.setText("Time's up!");
                }
            }.start();
        } else {
            end_time.setText("End time must not be before start time");
        }
    }

    private void allClicks() {
        btnMaxFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMaxFill.setBackgroundColor(getResources().getColor(R.color.black));
                btnMaxFill.setTextColor(getResources().getColor(R.color.white));
                btnCurrentFill.setBackgroundColor(getResources().getColor(R.color.grey));
                btnCurrentFill.setTextColor(getResources().getColor(R.color.black));
            }
        });

        btnCurrentFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCurrentFill.setBackgroundColor(getResources().getColor(R.color.black));
                btnCurrentFill.setTextColor(getResources().getColor(R.color.white));
                btnMaxFill.setBackgroundColor(getResources().getColor(R.color.grey));
                btnMaxFill.setTextColor(getResources().getColor(R.color.black));
            }
        });
    }

    private void initView(View view) {
        btnMaxFill = view.findViewById(R.id.btnMaxFill);
        btnCurrentFill = view.findViewById(R.id.btnCurrentFill);
        quizImage = view.findViewById(R.id.quiz_image);
        quizTitle = view.findViewById(R.id.quiz_title);
        quizTime = view.findViewById(R.id.quiz_time);
        joiningFees = view.findViewById(R.id.joining_fees);
        maxEntryDetail = view.findViewById(R.id.max_entry);
        availableSpots = view.findViewById(R.id.available_spots);
        prizePoolDetail = view.findViewById(R.id.prize_pool);
        view_more = view.findViewById(R.id.view_more);
        end_time = view.findViewById(R.id.end_time);
    }
}
