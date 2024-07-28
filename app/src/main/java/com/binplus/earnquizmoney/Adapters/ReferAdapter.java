package com.binplus.earnquizmoney.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Model.ReferModel;
import com.binplus.earnquizmoney.R;

import java.util.List;

public class ReferAdapter extends RecyclerView.Adapter<ReferAdapter.ViewHolder> {

    private List<ReferModel.Data> referList;

    public ReferAdapter(List<ReferModel.Data> referList) {
        this.referList = referList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_refer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReferModel.Data referData = referList.get(position);
        holder.nameTextView.setText(referData.getName());
        holder.idTextView.setText("ID: #" + referData.getId());
    }

    @Override
    public int getItemCount() {
        return referList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView idTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            idTextView = itemView.findViewById(R.id.id);
        }
    }
}
