package com.binplus.earnquizmoney.Adapters;

import static android.app.PendingIntent.getActivity;
import static com.binplus.earnquizmoney.BaseURL.BaseURL.BASE_URL_IMAGE;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Fragments.AddMoneyFragment;
import com.binplus.earnquizmoney.Fragments.QuizDetailFragment;
import com.binplus.earnquizmoney.Model.QuizModel;
import com.binplus.earnquizmoney.R;
import com.binplus.earnquizmoney.common.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<QuizModel.Datum> quizList;
    private FragmentManager fragmentManager;

    public QuizAdapter(List<QuizModel.Datum> quizModelList, FragmentManager fragmentManager) {
        this.quizList = quizModelList;
        this.fragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        QuizModel.Datum quizModel = quizList.get(position);
        String imageUrl = BASE_URL_IMAGE + quizModel.getImage();
        Log.d("QuizAdapter", "Image URL: " + imageUrl);

        Picasso.get()
                .load(imageUrl)
                .into(holder.quizImage);
        holder.quizTitle.setText(quizModel.getName());
        holder.maxEntry.setText(quizModel.getMax_entry() + " " + "People");
        holder.availableSpots.setText(quizModel.getAvailable_spot());
        holder.prizePool.setText("Rs." + quizModel.getPrize_pool() + "/-");
        holder.joiningFees.setText("Rs."+quizModel.getEntry()+"/-");
        holder.quizTime.setText(quizModel.getStart_date());
        holder.view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizDetailFragment quizDetailFragment = QuizDetailFragment.newInstance(quizModel);

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.homeFragment, quizDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return quizList.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {

        ImageView quizImage;
        TextView quizTitle, quizTime, joiningFees, maxEntry, availableSpots, prizePool,view_more;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizImage = itemView.findViewById(R.id.quiz_image);
            quizTitle = itemView.findViewById(R.id.quiz_title);
            quizTime = itemView.findViewById(R.id.quiz_time);
            joiningFees = itemView.findViewById(R.id.joining_fees);
            maxEntry = itemView.findViewById(R.id.max_entry);
            availableSpots = itemView.findViewById(R.id.available_spots);
            prizePool = itemView.findViewById(R.id.prize_pool);
            view_more = itemView.findViewById(R.id.view_more);
        }
    }
}
