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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.MainActivity;
import com.media2359.nickel.activities.TransactionActivity;
import com.media2359.nickel.adapter.RecipientAdapter;
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.customview.ThemedSwipeRefreshLayout;
import com.media2359.nickel.utils.DialogUtils;
import com.media2359.nickel.utils.DisplayUtils;
import com.media2359.nickel.utils.MistUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Xijun on 10/3/16.
 */
public class HomeFragment extends BaseFragment implements RecipientAdapter.onItemClickListener {

    private MainActivity mainActivity;
    private RecyclerView rvHome;
    private RecipientAdapter recipientAdapter;
    private TextView tvExchangeRate, tvFees, tvMyName, tvMyInfo, tvAddRecipient, tvGetAmount;
    private EditText etSendAmount;
    private RelativeLayout btnMyInfoEdit, btnAddNewRecipient;
    private List<Recipient> recipientList = new ArrayList<>();
    private double exchangeRate = 9679.13d; // 1SGD = [exchangeRate] IDR
    private double getAmount = 0d, fee = 7d, totalAmount = 0d;
    private ThemedSwipeRefreshLayout srl;
    private Transaction currentTransaction;
    private ScrollView scrollView;

    private Realm realm;


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
        recipientAdapter = new RecipientAdapter(getActivity(), recipientList);
        recipientAdapter.setOnItemClickListener(this);
        rvHome.setItemAnimator(new DefaultItemAnimator());
        rvHome.setAdapter(recipientAdapter);
        //rvHome.addOnScrollListener(OnScrollRV);

        srl = (ThemedSwipeRefreshLayout) view.findViewById(R.id.srlHome);
        srl.setOnRefreshListener(OnRefresh);

        tvExchangeRate = (TextView) view.findViewById(R.id.tvExchangeRate);
        //TODO: set the exchange rate
        tvFees = (TextView) view.findViewById(R.id.tvFeesAmount);
        etSendAmount = (EditText) view.findViewById(R.id.etSendAmount);
        //etSendAmount.setText("0");
        tvGetAmount = (TextView) view.findViewById(R.id.tvGetAmount);
        btnMyInfoEdit = (RelativeLayout) view.findViewById(R.id.btnMyInfoEdit);
        btnAddNewRecipient = (RelativeLayout) view.findViewById(R.id.btnAddNewRecipient);
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

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getActivity()).build();
        Realm.deleteRealm(realmConfiguration);
        realm = Realm.getInstance(realmConfiguration);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

//    private RecyclerView.OnScrollListener OnScrollRV = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//
//            if (isScrolledToTop(recyclerView)) {
//                srl.setEnabled(true);
//            } else {
//                srl.setEnabled(false);
//            }
//        }
//    };
//
//    private boolean isScrolledToTop(RecyclerView recyclerView) {
//        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }


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
                .withProgress(Transaction.TRANS_DRAFT)
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
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public void onEditButtonClick(int position) {
        Recipient recipient = recipientList.get(position);
        mainActivity.switchFragment(RecipientDetailFragment.newInstance(recipient), true);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        showDeleteDialog(position, recipientList.get(position).getName());
    }

    @Override
    public void onSendMoneyClick(int position) {
        Recipient recipient = recipientList.get(position);
        //TODO: change to actual value
        if (validTransaction()) {
            makeTransaction(recipient);
            String message = "Proceed to send " + etSendAmount.getText().toString() + " SGD to " + recipient.getName() + "?";
            showPaymentConfirmationDialog(message);
        }
    }

    @Override
    public void onTransactionClick(int position) {
        TransactionActivity.startTransactionActivity(getActivity(), recipientList.get(position).getCurrentTransaction());
    }

    private void showPaymentConfirmationDialog(String message) {
        AlertDialog dialog = DialogUtils.getNickelThemedAlertDialog(getContext(), "Alert", message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmTransaction();
            }
        });

        dialog.show();
    }

    /**
     * When user clicks yes on payment confirmation dialog
     * This should save the transaction in local storage, call the api to upload the open the transaction in server
     */
    private void confirmTransaction() {
        // TODO: call api

        // update the transaction status
        currentTransaction.setTransProgress(Transaction.TRANS_NEW_BORN);

        TransactionActivity.startTransactionActivity(getActivity(), currentTransaction);
    }

    /**
     * when the transaction is successfully created in server
     */
    private void transactionCreated() {

        // save the current transaction into persisted storage
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(currentTransaction);
            }
        });

        TransactionActivity.startTransactionActivity(getActivity(), currentTransaction);
    }


    private void showDeleteDialog(final int position, String contactName) {
        String message = "Do you want to delete " + contactName + "?";
        String title = "Delete recipient information?";

        DialogUtils.getNickelThemedAlertDialog(getActivity(), title, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recipientAdapter.removeItem(position);
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
    }

    private void showMyProfile() {
        tvMyInfo.setVisibility(View.VISIBLE);
        btnMyInfoEdit.setVisibility(View.VISIBLE);
    }

    private void getRecipients() {
        recipientList.clear();

        Recipient a = new Recipient("Husband", "BRI 281973021894", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient b = new Recipient("Mother", "BRI 0123874123", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient c = new Recipient("Sister", "MYI 9012830912", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient d = new Recipient("Han", "SGW 0911298301", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        a.setExpanded(false);
        b.setExpanded(false);
        c.setExpanded(false);
        d.setExpanded(false);

        Transaction aadd = new Transaction("1238u9ashjd", "March 2, 2016", "500.00", "Funds Ready for Collection", Transaction.TRANS_NEW_BORN, "Husband", 1235);
        a.setCurrentTransaction(aadd);

        recipientList.add(a);
        recipientList.add(b);
        recipientList.add(c);
        recipientList.add(d);
        recipientList.add(a);
        recipientList.add(b);
        recipientList.add(c);
        recipientList.add(d);

        recipientAdapter.notifyDataSetChanged();

        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }

        showListOfRecipient(!recipientList.isEmpty());
    }

    private void showListOfRecipient(boolean show) {
        if (show) {
            rvHome.setVisibility(View.VISIBLE);
            btnAddNewRecipient.setVisibility(View.GONE);
            tvAddRecipient.setText(getString(R.string.add_new_recipient));
            tvAddRecipient.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_add, 0, 0, 0);

        } else {
            rvHome.setVisibility(View.GONE);
            btnAddNewRecipient.setVisibility(View.VISIBLE);
            tvAddRecipient.setText(getString(R.string.recipient));
            tvAddRecipient.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_alert, 0, 0, 0);
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
