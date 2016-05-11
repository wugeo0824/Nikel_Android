package com.media2359.nickel.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.viewholder.TransactionHistoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xijun on 25/4/16.
 */
public class HistoryMVPAdapter extends RecyclerView.Adapter<TransactionHistoryViewHolder> {

    List<Transaction> transactionsList = new ArrayList<>();
    Context context;

    public interface HistoryItemClickListener {
        void OnItemClick(RecyclerView.ViewHolder viewHolder, Transaction transaction);
    }

    private HistoryItemClickListener historyItemClickListener;

    public HistoryMVPAdapter(Context context, HistoryItemClickListener historyItemClickListener) {
        this.context = context;
        this.historyItemClickListener = historyItemClickListener;
    }

    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View historyView = inflater.inflate(R.layout.item_transaction_history, parent, false);
        TransactionHistoryViewHolder normalArticleViewHolder = new TransactionHistoryViewHolder(historyView);
        return normalArticleViewHolder;
    }

    @Override
    public void onBindViewHolder(final TransactionHistoryViewHolder holder, int position) {
        final Transaction transaction = transactionsList.get(position);
        holder.bindItem(transaction);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyItemClickListener.OnItemClick(holder, transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }
}
