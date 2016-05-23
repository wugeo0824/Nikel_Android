package com.media2359.nickel.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.ui.customview.InItemProgressBar;

/**
 * Created by Xijun on 13/5/16.
 */
public class InProgressViewHolder extends RecyclerView.ViewHolder {

    TextView tvRecipientName, tvRecipientBank, tvTransactionAmount;
    InItemProgressBar progressBar;
    //public ImageView btnRecipientNext;

    public InProgressViewHolder(View itemView) {
        super(itemView);

        tvRecipientName = (TextView) itemView.findViewById(R.id.tvRecipientName);
        tvRecipientBank = (TextView) itemView.findViewById(R.id.tvRecipientBank);
        tvTransactionAmount = (TextView) itemView.findViewById(R.id.tvTransactionAmount);
        progressBar = (InItemProgressBar) itemView.findViewById(R.id.pbInItemProgress);
        //btnRecipientNext = (ImageButton) itemView.findViewById(R.id.btnRecipientNext);
    }

    public void bind(Recipient recipient) {
        tvRecipientName.setText(recipient.getName());
        tvRecipientBank.setText(recipient.getBankAccount());
        tvTransactionAmount.setText(recipient.getCurrentTransaction().getTransactionAmount() + " SGD");
        progressBar.updateProgress(recipient.getCurrentTransaction().getInItemProgress());
    }


}
