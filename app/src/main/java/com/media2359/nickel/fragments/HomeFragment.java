package com.media2359.nickel.fragments;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.MainActivity;
import com.media2359.nickel.activities.TransactionActivity;
import com.media2359.nickel.adapter.RecipientAdapter;
import com.media2359.nickel.event.OnRecipientsChangedEvent;
import com.media2359.nickel.managers.CentralDataManager;
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.ui.customview.ThemedSwipeRefreshLayout;
import com.media2359.nickel.utils.DialogUtils;
import com.media2359.nickel.utils.DisplayUtils;
import com.media2359.nickel.utils.MistUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Xijun on 10/3/16.
 */
public class HomeFragment extends BaseFragment implements RecipientAdapter.onItemClickListener {

    private static final String TAG = "HomeFragment";

    private MainActivity mainActivity;
    private RecyclerView rvHome;
    private RecipientAdapter recipientAdapter;
    private TextView tvExchangeRate, tvFees, tvMyName, tvMyInfo, tvAddRecipient, tvGetAmount;
    private EditText etSendAmount;
    private RelativeLayout btnMyInfoEdit, btnAddNewRecipient;
    private double exchangeRate = 9679.13d; // 1SGD = [exchangeRate] IDR
    private double getAmount = 0d, fee = 7d, totalAmount = 0d;
    private ThemedSwipeRefreshLayout srl;
    private NickelTransfer currentTransaction;

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
        recipientAdapter = new RecipientAdapter(getActivity());
        recipientAdapter.setOnItemClickListener(this);
        recipientAdapter.setData(CentralDataManager.getInstance().getAllRecipients());
        rvHome.setAdapter(recipientAdapter);
        rvHome.setItemAnimator(new DefaultItemAnimator());

        srl = (ThemedSwipeRefreshLayout) view.findViewById(R.id.srlHome);
        srl.setOnRefreshListener(OnRefresh);

        tvExchangeRate = (TextView) view.findViewById(R.id.tvExchangeRate);
        //TODO: set the exchange rate
        tvFees = (TextView) view.findViewById(R.id.tvFeesAmount);
        etSendAmount = (EditText) view.findViewById(R.id.etSendAmount);
        tvGetAmount = (TextView) view.findViewById(R.id.tvGetAmount);
        btnMyInfoEdit = (RelativeLayout) view.findViewById(R.id.btnMyInfoEdit);
        btnAddNewRecipient = (RelativeLayout) view.findViewById(R.id.btnAddNewRecipient);
        tvMyInfo = (TextView) view.findViewById(R.id.tvMyInformation);
        tvAddRecipient = (TextView) view.findViewById(R.id.tvAddRecipient);
        //tvMyInfo.setClickable(true);
        tvAddRecipient.setClickable(true);
        tvAddRecipient.setOnClickListener(onNewRecipientClick);

        btnMyInfoEdit.setOnClickListener(onMyInfoClick);
        btnAddNewRecipient.setOnClickListener(onNewRecipientClick);
        etSendAmount.addTextChangedListener(onAmountChangedWatcher);

        // hide the keyboard when user clicks done button
        etSendAmount.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etSendAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                    DisplayUtils.hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadMyProfile();
        getRecipients(false);
    }

    private View.OnClickListener onNewRecipientClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivity.switchFragment(RecipientDetailFragment.newInstance(RecipientDetailFragment.NO_RECIPIENT), true);
        }
    };

    private View.OnClickListener onMyInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showProfileFragment();
        }
    };

    private void showProfileFragment() {
        mainActivity.switchFragmentAndSyncDrawer(new ProfileFragment(), R.id.nav_profile);
    }

    /**
     * Adds thousands separator to the amount ","
     */
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

            if (srl.isFocusable()) {
                srl.setFocusable(false);
                srl.setFocusableInTouchMode(false);
            }
            //update the get amount
            if (!TextUtils.isEmpty(s.toString())) {
                double sendAmount = Double.parseDouble(s.toString().replaceAll(",", ""));
                getAmount = Math.round(sendAmount * exchangeRate * 100.0) / 100.0;
                tvGetAmount.setText(MistUtils.getFormattedString(getAmount));
                totalAmount = sendAmount + fee;
            } else {
                tvGetAmount.setText("");
            }

            // add thousand separators
            if (!TextUtils.isEmpty(s.toString())) {
                try {
                    String formattedString = MistUtils.getFormattedString(s.toString());
                    etSendAmount.setText(formattedString);
                    etSendAmount.setSelection(etSendAmount.getText().length());
                    // to place the cursor at the end of text
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            etSendAmount.addTextChangedListener(this);

        }
    };

    private SwipeRefreshLayout.OnRefreshListener OnRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getRecipients(true);
        }
    };

    private NickelTransfer makeTransaction(Recipient recipient) {

        NickelTransfer.Builder builder = new NickelTransfer.Builder();

        String today = MistUtils.getTodayStringInFormat();
        //TODO: real ID
        currentTransaction = builder.withAmount(etSendAmount.getText().toString())
                .withDate(today)
                .withExchangeRate(exchangeRate)
                .withID("asijdaopkf")
                .withRecipientName(recipient.getName())
                .withRecipientAccount(recipient.getBankAccount())
                .withStatus("This is payment status")
                .withProgress(NickelTransfer.TRANS_DRAFT)
                .build();

        return currentTransaction;
    }

    private boolean validTransaction() {
        if (TextUtils.isEmpty(etSendAmount.getText().toString())) {
            etSendAmount.setError("Please enter proper amount");
            etSendAmount.requestFocus();
            return false;
        }

        if (MyProfile.getCurrentProfile(getContext()) == null) {
            String message = getString(R.string.complete_profile_first);
            DialogUtils.getNickelThemedAlertDialog(getContext(), "Alert", message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showProfileFragment();
                }
            }).show();
            return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void OnEvent(OnRecipientsChangedEvent onRecipientsChangedEvent) {

        Log.d(TAG, "OnEvent: " + CentralDataManager.getInstance().getAllRecipients().size());

        if (onRecipientsChangedEvent.isSuccess()) {
            recipientAdapter.notifyDataSetChanged();
            showListOfRecipient(!CentralDataManager.getInstance().getAllRecipients().isEmpty());
        } else {
            Toast.makeText(getActivity(), "Sorry, internet connection is poor, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onEditButtonClick(int position) {
        mainActivity.switchFragment(RecipientDetailFragment.newInstance(position), true);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        Recipient recipient = CentralDataManager.getInstance().getRecipientAtPosition(position);
        showDeleteDialog(position, recipient.getName());
    }

    @Override
    public void onSendMoneyClick(int position) {
        Recipient recipient = CentralDataManager.getInstance().getRecipientAtPosition(position);
        //TODO: change to actual value
        if (validTransaction()) {
            makeTransaction(recipient);
            String message = "Proceed to send " + etSendAmount.getText().toString() + " SGD to " + recipient.getName() + "?";
            showPaymentConfirmationDialog(message, position);
        }
    }

    @Override
    public void onTransactionClick(int position) {
        Recipient recipient = CentralDataManager.getInstance().getRecipientAtPosition(position);
        TransactionActivity.startTransactionActivity(getActivity(), recipient.getCurrentTransaction(), position);
    }

    private void showPaymentConfirmationDialog(String message, final int position) {
        AlertDialog dialog = DialogUtils.getNickelThemedAlertDialog(getContext(), "Alert", message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmTransaction(position);
            }
        });

        dialog.show();
    }

    /**
     * When user clicks yes on payment confirmation dialog
     * This should save the transaction in local storage, call the api to upload the open the transaction in server
     */
    private void confirmTransaction(int position) {
        // TODO: call api

        // update the transaction status
        currentTransaction.transactionConfirmed();

        TransactionActivity.startTransactionActivity(getActivity(), currentTransaction, position);
    }

    /**
     * when the transaction is successfully created in server
     */
    private void transactionCreated() {

    }


    private void showDeleteDialog(final int position, String contactName) {
        String message = "Do you want to delete " + contactName + "?";
        String title = "Alert";

        DialogUtils.getNickelThemedAlertDialog(getActivity(), title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CentralDataManager.getInstance().deleteRecipientAtPosition(position);
                recipientAdapter.notifyItemRemoved(position);
            }
        }).show();
    }

    private void loadMyProfile() {
        //TODO get my profile data
        if (MyProfile.getCurrentProfile(getContext()) != null) {
            hideMyProfile();
        } else {
            showMyProfile();
        }
    }

    private void hideMyProfile() {
        tvMyInfo.setVisibility(View.GONE);
        btnMyInfoEdit.setVisibility(View.GONE);
        srl.setEnabled(true);
    }

    private void showMyProfile() {
        tvMyInfo.setVisibility(View.VISIBLE);
        btnMyInfoEdit.setVisibility(View.VISIBLE);
        srl.setEnabled(false);
    }

    private void getRecipients(boolean pullToRefresh) {
        if (pullToRefresh)
            CentralDataManager.getInstance().fetchRecipientsFromServer();

        if (pullToRefresh) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (srl.isRefreshing()) {
                        srl.setRefreshing(false);
                    }
                }
            }, 500);
        }

        recipientAdapter.notifyDataSetChanged();
        showListOfRecipient(!CentralDataManager.getInstance().getAllRecipients().isEmpty());
    }

    private void showListOfRecipient(boolean show) {
        if (show) {
            rvHome.setVisibility(View.VISIBLE);
            btnAddNewRecipient.setVisibility(View.GONE);
            tvAddRecipient.setText(getString(R.string.add_new_recipient));
            tvAddRecipient.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_add, 0, 0, 0);
            srl.setEnabled(true);
        } else {
            rvHome.setVisibility(View.GONE);
            btnAddNewRecipient.setVisibility(View.VISIBLE);
            tvAddRecipient.setText(getString(R.string.recipient));
            tvAddRecipient.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_alert, 0, 0, 0);
            srl.setEnabled(false);
        }
    }


    @Override
    protected String getPageTitle() {
        return getString(R.string.home);
    }

}
