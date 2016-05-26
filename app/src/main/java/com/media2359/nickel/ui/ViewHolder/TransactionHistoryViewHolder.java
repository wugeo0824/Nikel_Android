package com.media2359.nickel.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.ui.customview.TransactionProgress;

/**
 * Created by Xijun on 4/4/16.
 */
public class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTranID, tvTranDate, tvTranAmount, tvTranStatus;
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

    public void bindItem(NickelTransfer transaction) {
        String prefixID = itemView.getContext().getString(R.string.trans_id);
        SpannableString transID = new SpannableString(prefixID + " " + transaction.getTransactionID());
        transID.setSpan(new ForegroundColorSpan(itemView.getContext().getResources().getColor(R.color.pink)), prefixID.length(), transID.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        tvTranID.setText(transID);
        tvTranDate.setText(transaction.getTransactionDate());
        tvTranAmount.setText(transaction.getTransactionAmount() + "SGD");
        tvTranStatus.setText(transaction.getTransactionStatus());
        //TODO
        transactionProgress.updateProgress(transaction.getTransProgress());
    }
}
