package com.binplus.earnquizmoney.Adapters;


import static com.binplus.earnquizmoney.BaseURL.BaseURL.BASE_URL_IMAGE;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Fragments.QuestionsFragment;
import com.binplus.earnquizmoney.Fragments.QuizDetailFragment;
import com.binplus.earnquizmoney.Model.QuizDetailModel;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.R;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DetailQuizAdapter extends RecyclerView.Adapter<DetailQuizAdapter.QuizViewHolder> {

    private List<QuizDetailModel.Datum> quizList;
    private OnFillButtonClickListener onFillButtonClickListener;
    private FragmentManager fragmentManager;

    public DetailQuizAdapter(List<QuizDetailModel.Datum> quizModelList, OnFillButtonClickListener listener,FragmentManager fragmentManager) {
        this.quizList = quizModelList;
        this.onFillButtonClickListener = listener;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        QuizDetailModel.Datum quizModel = quizList.get(position);
        holder.btnMaxFill.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        holder.btnMaxFill.setTextColor(Color.WHITE);
        holder.quizTitle.setText(quizModel.getName());
        holder.description.setText(quizModel.getDescription());
        String image = quizModel.getImage();
        if (image != null && !image.isEmpty()) {
            Picasso.get()
                    .load(BASE_URL_IMAGE + image)
                    .placeholder(R.drawable.ic_piggy_bank)
                    .error(R.drawable.ic_piggy_bank)
                    .into(holder.quizImage);
        } else {
            holder.quizImage.setImageResource(R.drawable.ic_piggy_bank);
        }
        holder.maxEntryDetail.setText(quizModel.getMax_entry() + " People");
        holder.availableSpots.setText(quizModel.getAvailable_spot());
        holder.prizePoolDetail.setText("Rs." + quizModel.getPrize_pool() + "/-");
        holder.joiningFees.setText("Rs." + quizModel.getEntry() + "/-");
        holder.quizTime.setText(quizModel.getStart_date());

        holder.btnMaxFill.setOnClickListener(v -> {
            if (onFillButtonClickListener != null) {
                onFillButtonClickListener.onMaxFillClick(quizModel.getPoints());
                holder.btnMaxFill.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                holder.btnCurrentFill.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                holder.btnMaxFill.setTextColor(Color.WHITE);
                holder.btnCurrentFill.setTextColor(Color.BLACK);
            }
        });

        holder.btnCurrentFill.setOnClickListener(v -> {
            if (onFillButtonClickListener != null) {
                onFillButtonClickListener.onCurrentFillClick(quizModel.getCurrent_fill());
                holder.btnMaxFill.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                holder.btnCurrentFill.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                holder.btnMaxFill.setTextColor(Color.BLACK);
                holder.btnCurrentFill.setTextColor(Color.WHITE);
            }
        });
        holder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionsFragment questionsFragment = new QuestionsFragment();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.homeFragment, questionsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        holder.btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionsFragment questionsFragment = new QuestionsFragment();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.homeFragment, questionsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        String startDate = quizModel.getStart_date();
        String endDate = quizModel.getEnd_date();
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

                LocalDateTime startTime = LocalDateTime.parse(startDate, dateTimeFormatter);
                LocalDateTime endTime = LocalDateTime.parse(endDate, dateTimeFormatter);

                long durationMillis = ChronoUnit.MILLIS.between(LocalDateTime.now(), startTime);

                if (durationMillis > 0) {
                    new CountDownTimer(durationMillis, 1000) {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long hours = millisUntilFinished / (1000 * 60 * 60);
                            long minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                            long seconds = (millisUntilFinished % (1000 * 60)) / 1000;
                            holder.end_time.setText(String.format("%02d : %02d : %02d", hours, minutes, seconds));
                        }

                        @Override
                        public void onFinish() {
                            holder.end_time.setText("Time's up!");
                        }
                    }.start();
                } else {
                    holder.end_time.setText("End time must not be before start time");
                }
            } catch (Exception e) {
                Log.e("QuizDetailFragment", "Error parsing dates: " + e.getMessage());
                holder.end_time.setText("Invalid date format");
            }
        } else {
            Log.e("QuizDetailFragment", "Start date or end date is null or empty");
            holder.end_time.setText("Date information is missing");
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        private AppCompatButton btnMaxFill,btn_join,btn_play;
        private AppCompatButton btnCurrentFill;
        private ImageView quizImage;
        private TextView quizTitle, quizTime, joiningFees, maxEntryDetail, description, availableSpots, prizePoolDetail, view_more, end_time;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            btnMaxFill = itemView.findViewById(R.id.btnMaxFill);
            btnCurrentFill = itemView.findViewById(R.id.btnCurrentFill);
            quizImage = itemView.findViewById(R.id.quiz_image);
            quizTitle = itemView.findViewById(R.id.quiz_title);
            description = itemView.findViewById(R.id.discription);
            quizTime = itemView.findViewById(R.id.quiz_time);
            joiningFees = itemView.findViewById(R.id.joining_fees);
            maxEntryDetail = itemView.findViewById(R.id.max_entry);
            availableSpots = itemView.findViewById(R.id.available_spots);
            prizePoolDetail = itemView.findViewById(R.id.prize_pool);
            view_more = itemView.findViewById(R.id.view_more);
            end_time = itemView.findViewById(R.id.end_time);
            btn_join = itemView.findViewById(R.id.btn_join);
            btn_play = itemView.findViewById(R.id.btn_play);
        }
    }

    public interface OnFillButtonClickListener {
        void onMaxFillClick(List<QuizDetailModel.Point> points);
        void onCurrentFillClick(List<QuizDetailModel.CurrentFill> currentFills);
    }
}
