package com.binplus.earnquizmoney.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.binplus.earnquizmoney.Model.TransactionModel;
import com.binplus.earnquizmoney.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final ArrayList<TransactionModel.Datum> transactionList;
    private String fragmentType;

    public TransactionAdapter(ArrayList<TransactionModel.Datum> transactionList,String fragmentType) {
        this.transactionList = transactionList;
        this.fragmentType = fragmentType;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_transaction_row, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionModel.Datum transaction = transactionList.get(position);
        if ("withdrawal".equals(fragmentType)) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FFA500"));
            holder.tvTransactionAmount.setTextColor(Color.RED);
        } else {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#008000"));
            holder.tvTransactionAmount.setTextColor(Color.parseColor("#008000"));
        }
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvTransactionId.setText("ID: #" + transaction.getId());
        holder.tvTransactionAmount.setText(transaction.getType().equals("Add") ? "+ Rs." + transaction.getAmount() : "- Rs." + transaction.getAmount());
        holder.tvTransactionDate.setText(transaction.getDate());
        holder.tvTransactionType.setText(transaction.getType());
        holder.tvStatus.setText(transaction.getStatus());
        holder.tvPaymentMethod.setText(transaction.getPayment_mode());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView tvIndex, tvTransactionId, tvTransactionAmount, tvTransactionDate, tvTransactionType, tvStatus, tvPaymentMethod;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
            tvTransactionId = itemView.findViewById(R.id.tv_transaction_id);
            tvTransactionAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvTransactionDate = itemView.findViewById(R.id.tv_transaction_date);
            tvTransactionType = itemView.findViewById(R.id.tv_transaction_type);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
        }
    }
}
