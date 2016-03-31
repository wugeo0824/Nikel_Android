package com.media2359.nickel.ui.ViewHolder;

import android.animation.LayoutTransition;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.event.OnRecipientDeleteClickEvent;
import com.media2359.nickel.event.OnRecipientEditClickEvent;
import com.media2359.nickel.event.OnSendMoneyClickEvent;
import com.media2359.nickel.model.DummyRecipient;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Xijun on 21/3/16.
 */
public class RecipientViewHolder extends RecyclerView.ViewHolder{

    public TextView tvRecipientName, tvRecipientBank, tvSendMoney;
    public Button btnEditRecipient, btnDeleteRecipient;
    public ImageButton btnRecipientOptions;
    public FrameLayout btnSendMoney;
    public boolean expanded = false;
    public RelativeLayout topHolder;
    private ItemExpandCollapseListener listener;
    private boolean optionsShown = false;

    public RecipientViewHolder(View itemView) {
        super(itemView);

        ((LinearLayout) itemView).setLayoutTransition(new LayoutTransition());
        tvRecipientName = (TextView) itemView.findViewById(R.id.tvRecipientName);
        tvRecipientBank = (TextView) itemView.findViewById(R.id.tvRecipientBank);
        tvSendMoney = (TextView) itemView.findViewById(R.id.tvSendMoney);
        btnEditRecipient = (Button) itemView.findViewById(R.id.btnRecipientEdit);
        btnDeleteRecipient = (Button) itemView.findViewById(R.id.btnRecipientDelete);
        btnRecipientOptions = (ImageButton) itemView.findViewById(R.id.btnRecipientOptions);
        btnSendMoney = (FrameLayout) itemView.findViewById(R.id.btnSendMoney);
        topHolder = (RelativeLayout) itemView.findViewById(R.id.topHolder);
    }

    public void bindItem(DummyRecipient dummyRecipient) {
        tvRecipientName.setText(dummyRecipient.getName());
        tvRecipientBank.setText(dummyRecipient.getBank());
        btnEditRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: edit recipient
                EventBus.getDefault().post(new OnRecipientEditClickEvent(getAdapterPosition()));
            }
        });

        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send money
                EventBus.getDefault().post(new OnSendMoneyClickEvent(getAdapterPosition()));
            }
        });

        btnRecipientOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionsShown){
                    hideOptionsButtons();
                }else{
                    showOptionsButtons();
                }
            }
        });

        btnDeleteRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: delete recipient
                EventBus.getDefault().post(new OnRecipientDeleteClickEvent(getAdapterPosition()));
            }
        });

        tvSendMoney.setText(String.format(itemView.getContext().getString(R.string.send_money_to), dummyRecipient.getName()));

        itemView.setOnClickListener(switchLayout);

        if (dummyRecipient.isExpanded()){
            expand();
        }else{
            collapse();
        }
    }

    private void showOptionsButtons(){
        btnRecipientOptions.setVisibility(View.GONE);
        btnEditRecipient.setVisibility(View.VISIBLE);
        btnDeleteRecipient.setVisibility(View.VISIBLE);
        optionsShown = true;
    }

    private void hideOptionsButtons(){
        btnRecipientOptions.setVisibility(View.VISIBLE);
        btnEditRecipient.setVisibility(View.GONE);
        btnDeleteRecipient.setVisibility(View.GONE);
        optionsShown = false;
    }

    public ItemExpandCollapseListener getListener() {
        return listener;
    }

    public void setListener(ItemExpandCollapseListener listener) {
        this.listener = listener;
    }

    private View.OnClickListener switchLayout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            expandOrCollapse();
        }
    };

    public void expand() {
        topHolder.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.nav_bar_color));
        tvRecipientName.setTextColor(itemView.getContext().getResources().getColor(R.color.white));
        if (optionsShown){
            hideOptionsButtons();
        }
        btnRecipientOptions.setVisibility(View.VISIBLE);
        btnSendMoney.setVisibility(View.VISIBLE);
        expanded = true;
        listener.onItemExpanded(getAdapterPosition());
    }

    public void collapse() {
        topHolder.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.card));
        tvRecipientName.setTextColor(itemView.getContext().getResources().getColor(R.color.text_color_normal));
        if (optionsShown){
            hideOptionsButtons();
        }
        btnRecipientOptions.setVisibility(View.INVISIBLE);
        btnSendMoney.setVisibility(View.GONE);
        expanded = false;
        listener.onItemCollapsed(getAdapterPosition());
    }

    private void expandOrCollapse() {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
    }


    /**
     * Empowers adapter
     * implementations to be notified of expand/collapse state change events.
     */
    public interface ItemExpandCollapseListener {

        /**
         * Called when a list item is expanded.
         *
         * @param position The index of the item in the list being expanded
         */
        void onItemExpanded(int position);

        /**
         * Called when a list item is collapsed.
         *
         * @param position The index of the item in the list being collapsed
         */
        void onItemCollapsed(int position);
    }
}
