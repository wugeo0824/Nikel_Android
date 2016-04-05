package com.media2359.nickel.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.viewholder.TransactionHistoryViewHolder;

import java.util.List;

/**
 * Created by Xijun on 4/4/16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<TransactionHistoryViewHolder> {

    List<Transaction> transactionsList;

    public HistoryAdapter(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View historyView = inflater.inflate(R.layout.item_transaction_history, parent, false);
        TransactionHistoryViewHolder normalArticleViewHolder = new TransactionHistoryViewHolder(historyView);
        return normalArticleViewHolder;
    }

    @Override
    public void onBindViewHolder(TransactionHistoryViewHolder holder, int position) {
        holder.bindItem(transactionsList.get(position));
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }
}
