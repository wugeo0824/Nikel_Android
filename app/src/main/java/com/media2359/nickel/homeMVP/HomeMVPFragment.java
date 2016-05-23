package com.media2359.nickel.homeMVP;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.media2359.nickel.R;
import com.media2359.nickel.activities.MainActivity;
import com.media2359.nickel.adapter.RecipientAdapter;
import com.media2359.nickel.fragments.ProfileFragment;
import com.media2359.nickel.fragments.RecipientDetailFragment;
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.customview.ThemedSwipeRefreshLayout;
import com.media2359.nickel.utils.DialogUtils;
import com.media2359.nickel.utils.DisplayUtils;
import com.media2359.nickel.utils.MistUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Xijun on 20/5/16.
 */
public class HomeMVPFragment extends MvpLceFragment<SwipeRefreshLayout, List<Recipient>, HomeView, HomePresenter>
        implements HomeView, SwipeRefreshLayout.OnRefreshListener, RecipientAdapter.onItemClickListener {

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

    private Realm realm;


    public static HomeMVPFragment newInstance() {

        Bundle args = new Bundle();

        HomeMVPFragment fragment = new HomeMVPFragment();
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
        rvHome.addOnScrollListener(OnScrollRV);

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
            showMyProfileScreen();
        }
    };

    private SwipeRefreshLayout.OnRefreshListener OnRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData(true);
        }
    };


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

    @Override
    public boolean validTransaction() {

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
                    showMyProfileScreen();
                }
            }).show();
            return false;
        }

        return true;
    }

    @Override
    public void showMyProfileScreen() {
        mainActivity.switchFragmentAndSyncDrawer(new ProfileFragment(), R.id.nav_profile);
    }

    @Override
    public void showEditMyProfileButton() {

    }

    @Override
    public void showNewRecipientScreen() {

    }

    @Override
    public void showAddNewRecipientButton() {

    }

    @Override
    public void showRecipientDetailScreen(Recipient recipient) {

    }

    @Override
    public void updateRates(double exchangeRate, double fee) {

    }

    @Override
    public void recipientDeleted() {

    }

    @Override
    public void showTransactionDetailScreen(Transaction transaction) {

    }

    @Override
    public void shoePaymentConfirmationAlertDialog(String message) {
        AlertDialog dialog = DialogUtils.getNickelThemedAlertDialog(getContext(), "Alert", message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.makeNewTransaction(currentTransaction);
            }
        });

        dialog.show();

    }

    ProgressDialog progressDialog;

    @Override
    public void showLoadingProgressDialog(boolean show) {

        if (show) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.show();

        } else {
            if (progressDialog != null)
                progressDialog.dismiss();
        }


    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        if (e instanceof IOException){
            return "You are offline.";
        }else{
            return e.getLocalizedMessage();
        }
    }

    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void setData(List<Recipient> data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.getRecipients(pullToRefresh);
        presenter.refreshRates();
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onEditButtonClick(int position) {

    }

    @Override
    public void onDeleteButtonClick(int position) {

    }

    @Override
    public void onSendMoneyClick(int position) {

    }

    @Override
    public void onTransactionClick(int position) {

    }
}
