package com.media2359.nickel.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.event.OnSendMoneyClickEvent;
import com.media2359.nickel.event.OnTransactionDetailClickEvent;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.customview.TransactionProgress;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Xijun on 4/4/16.
 */
public class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTranID,tvTranDate,tvTranAmount,tvTranStatus;
    public TransactionProgress transactionProgress;

    public TransactionHistoryViewHolder(View itemView) {
        super(itemView);
        init();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }

    private void init() {
        tvTranID = (TextView) itemView.findViewById(R.id.tvTransactionID);
        tvTranDate = (TextView) itemView.findViewById(R.id.tvTranDate);
        tvTranAmount = (TextView) itemView.findViewById(R.id.tvTranAmount);
        tvTranStatus = (TextView) itemView.findViewById(R.id.tvTranStatus);
        transactionProgress = (TransactionProgress) itemView.findViewById(R.id.tpHistory);
    }

    public void bindItem(Transaction transaction){
        tvTranID.setText("Transaction ID: " + transaction.getTransactionID());
        tvTranDate.setText(transaction.getTransactionDate());
        tvTranAmount.setText(transaction.getTransactionAmount() + "SGD");
        tvTranStatus.setText(transaction.getTransactionStatus());
        //TODO
        transactionProgress.updateProgress(transaction.getTransProgress());
        //itemView.setOnClickListener(OnDetailClick);
    }

    private View.OnClickListener OnDetailClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new OnTransactionDetailClickEvent(getAdapterPosition()));
        }
    };


}
