package com.binplus.earnquizmoney.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Model.QuizDetailModel;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.R;

import java.util.List;

public class WinningListRankAdapter extends RecyclerView.Adapter<WinningListRankAdapter.QuizViewHolder> {
    private List<QuizDetailModel.CurrentFill> quizList;

    public WinningListRankAdapter(List<QuizDetailModel.CurrentFill> quizModelList) {
        this.quizList = quizModelList;
    }

    @NonNull
    @Override
    public WinningListRankAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ranks, parent, false);
        return new WinningListRankAdapter.QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WinningListRankAdapter.QuizViewHolder holder, int position) {
        QuizDetailModel.CurrentFill quizModel = quizList.get(position);
        holder.rank_number.setText(quizModel.getTop_winner());
        holder.winning.setText(quizModel.getPoints());
    }

    @Override
    public int getItemCount() {
        return quizList != null ? quizList.size() : 0;
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView rank_number, winning;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            rank_number = itemView.findViewById(R.id.rank_number);
            winning = itemView.findViewById(R.id.winning);
        }
    }
}
