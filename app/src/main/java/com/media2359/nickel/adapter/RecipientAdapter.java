package com.media2359.nickel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.ui.viewholder.RecipientViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xijun on 21/3/16.
 */
public class RecipientAdapter extends RecyclerView.Adapter<RecipientViewHolder> implements RecipientViewHolder.ItemExpandCollapseListener{

    Context mContext;
    List<Recipient> dataList = new ArrayList<>();
    int lastExpandedPosition = -1;
    private onItemClickListener onItemClickListener;

    public RecipientAdapter(Context mContext, List<Recipient> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public RecipientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View recipientView = inflater.inflate(R.layout.item_recipient_row_expanded,parent,false);
        RecipientViewHolder viewHolder = new RecipientViewHolder(recipientView);
        viewHolder.setExpandCollapseListener(this);
        return viewHolder;
    }

    @Override
    public void onViewAttachedToWindow(RecipientViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onBindViewHolder(final RecipientViewHolder holder, int position) {
        Recipient recipient = dataList.get(position);
        holder.bindItem(recipient);
        holder.btnEditRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onEditButtonClick(holder.getAdapterPosition());
            }
        });
        holder.btnDeleteRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onDeleteButtonClick(holder.getAdapterPosition());
            }
        });
        holder.btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSendMoneyClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void removeItem(int position){
        if (position == lastExpandedPosition){
            dataList.get(position).setExpanded(false);
            notifyItemChanged(position);
            lastExpandedPosition = -1;
        }
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemExpanded(int position) {
        // if there was an already expanded item, close it
        if (lastExpandedPosition>=0){
            dataList.get(lastExpandedPosition).setExpanded(false);
            notifyItemChanged(lastExpandedPosition);
        }

        //note down the last expanded item
        lastExpandedPosition = position;
    }

    @Override
    public void onItemCollapsed(int position) {
        // if the collapsing item is the only expanded item, reset the last expanded position
        if (position == lastExpandedPosition)
            lastExpandedPosition = -1;
    }

    public RecipientAdapter.onItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(RecipientAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {

        /**
         * User clicks edit button
         */
        void onEditButtonClick(int position);

        /**
         * User clicks delete button
         */
        void onDeleteButtonClick(int position);

        /**
         * User clicks send money button
         */
        void onSendMoneyClick(int position);

    }
}
