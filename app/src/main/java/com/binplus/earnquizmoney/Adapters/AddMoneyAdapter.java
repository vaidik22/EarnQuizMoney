package com.binplus.earnquizmoney.Adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Interfaces.OnMoneySelectedListener;
import com.binplus.earnquizmoney.R;

import java.util.List;

public class AddMoneyAdapter extends RecyclerView.Adapter<AddMoneyAdapter.MoneyViewHolder> {

    private List<String> moneyList;
    private OnMoneySelectedListener onMoneySelectedListener;

    public AddMoneyAdapter(List<String> moneyList, OnMoneySelectedListener onMoneySelectedListener) {
        this.moneyList = moneyList;
        this.onMoneySelectedListener = onMoneySelectedListener;
    }

    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_money, parent, false);
        return new MoneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyViewHolder holder, int position) {
        String money = moneyList.get(position);
        holder.tvMoney.setText("Rs."+money);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMoneySelectedListener.onMoneySelected(money);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moneyList.size();
    }

    static class MoneyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMoney;

        public MoneyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }
    }


}
