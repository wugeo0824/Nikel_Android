package com.media2359.nickel.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.event.OnRecipientDeleteClickEvent;
import com.media2359.nickel.event.OnRecipientEditClickEvent;
import com.media2359.nickel.event.OnSendMoneyClickEvent;
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.model.TransactionManager;
import com.media2359.nickel.ui.MainActivity;
import com.media2359.nickel.ui.adapter.RecipientAdapter;
import com.media2359.nickel.ui.customview.ThemedSwipeRefreshLayout;
import com.media2359.nickel.utils.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Xijun on 10/3/16.
 */
public class HomeFragment extends BaseFragment {

    private MainActivity mainActivity;
    private RecyclerView rvHome;
    private RecipientAdapter recipientAdapter;
    private TextView tvExchangeRate, tvFees, tvTotal, tvMyName, tvMyInfo, tvAddRecipient, tvGetAmount;
    private EditText etSendAmount;
    private Button btnMyInfoEdit;
    private List<Recipient> dataList = new ArrayList<>();
    private float exchangeRate = 9679.13f; // 1SGD = [exchangeRate] IDR
    private float getAmount = 0, fee = 7f, totalAmount = 0;
    private ThemedSwipeRefreshLayout srl;
    private Transaction currentTransaction;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (getActivity() instanceof MainActivity)
            mainActivity = (MainActivity) getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rvHome = (RecyclerView) view.findViewById(R.id.rvRecipients);
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHome.setHasFixedSize(true);
        recipientAdapter = new RecipientAdapter(getActivity(), dataList);
        rvHome.setItemAnimator(new DefaultItemAnimator());
        rvHome.setAdapter(recipientAdapter);
        rvHome.addOnScrollListener(OnScrollRV);

        srl = (ThemedSwipeRefreshLayout) view.findViewById(R.id.srlHome);
        srl.setOnRefreshListener(OnRefresh);

        tvExchangeRate = (TextView) view.findViewById(R.id.tvExchangeRate);
        //TODO: set the exchange rate
        tvFees = (TextView) view.findViewById(R.id.tvFeesAmount);
        tvTotal = (TextView) view.findViewById(R.id.tvTotalAmount);
        etSendAmount = (EditText) view.findViewById(R.id.etSendAmount);
        //etSendAmount.setText("0");
        tvGetAmount = (TextView) view.findViewById(R.id.tvGetAmount);
        btnMyInfoEdit = (Button) view.findViewById(R.id.btnMyInfoEdit);
        tvMyInfo = (TextView) view.findViewById(R.id.tvMyInformation);
        tvAddRecipient = (TextView) view.findViewById(R.id.tvAddRecipient);
        //tvMyInfo.setClickable(true);
        tvAddRecipient.setClickable(true);
        tvAddRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.switchFragment(RecipientDetailFragment.newInstance(null), true);
            }
        });

        btnMyInfoEdit.setOnClickListener(onMyInfoClick);
        etSendAmount.addTextChangedListener(onAmountChangedWatcher);
    }

    private RecyclerView.OnScrollListener OnScrollRV = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (isScrolledToTop(recyclerView)) {
                srl.setEnabled(true);
            } else {
                srl.setEnabled(false);
            }
        }
    };

    private boolean isScrolledToTop(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private View.OnClickListener onMyInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            mainActivity.overridePendingTransition(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left);
//            mainActivity.switchFragment(new ProfileFragment(), true);
            mainActivity.switchFragmentAndSyncDrawer(new ProfileFragment(), R.id.nav_profile);
        }
    };

    private TextWatcher onAmountChangedWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            etSendAmount.removeTextChangedListener(this);

            //update the get amount
            if (!TextUtils.isEmpty(s.toString())) {
                long sendAmount = Long.parseLong(s.toString().replaceAll(",", ""));
                getAmount = sendAmount * exchangeRate;
                tvGetAmount.setText("" + getAmount);
                totalAmount = sendAmount + fee;
                tvTotal.setText("" + totalAmount);
            } else {
                tvGetAmount.setText("");
                tvTotal.setText("" + fee);
            }

            // add thousand separators
            try {
                String givenstring = s.toString();
                Long longval;
                if (givenstring.contains(",")) {
                    givenstring = givenstring.replaceAll(",", "");
                }
                longval = Long.parseLong(givenstring);
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String formattedString = formatter.format(longval);
                etSendAmount.setText(formattedString);
                etSendAmount.setSelection(etSendAmount.getText().length());
                // to place the cursor at the end of text
            } catch (Exception e) {
                e.printStackTrace();
            }

            etSendAmount.addTextChangedListener(this);

        }
    };

    private SwipeRefreshLayout.OnRefreshListener OnRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //TODO;
                    getRecipients();
                }
            }, 1000);
        }
    };

    private Transaction makeTransaction(Recipient recipient) {

        Transaction.Builder builder = new Transaction.Builder();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a", Locale.getDefault());
        String today = sdf.format(new Date().getTime());
        //TODO: real ID
        currentTransaction = builder.withAmount(tvGetAmount.getText().toString())
                .withDate(today)
                .withExchangeRate(exchangeRate)
                .withID("asijdaopkf")
                .withRecipientName(recipient.getName())
                .withStatus("This is payment status")
                .withProgress(Const.TRANS_NEW_BORN)
                .build();

        TransactionManager.getManager().setCurrentTransaction(currentTransaction);
        return currentTransaction;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(OnRecipientEditClickEvent onRecipientEditClickEvent) {
        //TODO: change to actual ID
        Recipient recipient = dataList.get(onRecipientEditClickEvent.getPosition());
        mainActivity.switchFragment(RecipientDetailFragment.newInstance(recipient), true);
    }

    @Subscribe
    public void onEvent(OnSendMoneyClickEvent onSendMoneyClickEvent) {
        //TODO: change to actual value
        makeTransaction(dataList.get(onSendMoneyClickEvent.getPosition()));
        //TODO: change status after api successfully called
        currentTransaction.setTransProgress(Const.TRANS_PENDING_PAYMENT);
        mainActivity.showPaymentConfirmationDialog(currentTransaction);
    }

    @Subscribe
    public void onEvent(OnRecipientDeleteClickEvent onDeleteEvent) {
        //TODO: change to actual value
        //recipientAdapter.removeItem(onDeleteEvent.getPosition());
        showDeleteDialog(onDeleteEvent.getPosition(),dataList.get(onDeleteEvent.getPosition()).getName());
    }

    private void showDeleteDialog(final int position, String contactName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete?");
        builder.setMessage("Do you want to delete " + contactName + "?");
        builder.setCancelable(false);
        builder.setNegativeButton("No",null);
        builder.setPositiveButton("Yes, delete it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recipientAdapter.removeItem(position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadMyProfile() {
        //TODO get my profile data
        if (MyProfile.getCurrentProfile(getContext()) != null){
            hideMyProfile();
        }else{
            showMyProfile();
        }
    }

    private void hideMyProfile() {
        tvMyInfo.setVisibility(View.GONE);
        btnMyInfoEdit.setVisibility(View.GONE);
    }

    private void showMyProfile() {
        tvMyInfo.setVisibility(View.VISIBLE);
        btnMyInfoEdit.setVisibility(View.VISIBLE);
    }

    private void getRecipients() {
        dataList.clear();
        Recipient a = new Recipient("Husband", "BRI 281973021894", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient b = new Recipient("Mother", "BRI 0123874123", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient c = new Recipient("Sister", "MYI 9012830912", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient d = new Recipient("Han", "SGW 0911298301", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        a.setExpanded(false);
        b.setExpanded(false);
        c.setExpanded(false);
        d.setExpanded(false);
        dataList.add(a);
        dataList.add(b);
        dataList.add(c);
        dataList.add(d);
        dataList.add(a);
        dataList.add(b);
        dataList.add(c);
        dataList.add(d);
        dataList.add(a);
        dataList.add(b);
        dataList.add(c);
        dataList.add(d);
        recipientAdapter.notifyDataSetChanged();
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        loadMyProfile();
        getRecipients();
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.home);
    }


}
