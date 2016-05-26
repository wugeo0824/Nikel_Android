package com.media2359.nickel.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.CaptureActivity;
import com.media2359.nickel.activities.TransactionActivity;
import com.media2359.nickel.managers.CentralDataManager;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.ui.customview.TransactionProgress;
import com.media2359.nickel.utils.Const;
import com.media2359.nickel.utils.DialogUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Xijun on 10/3/16.
 */
public class TransactionDetailFragment extends BaseFragment {

    private static final String TAG = "DetailFragment";
   // public static final String BUNDLE_TRANSACTION = "transaction";
    private NickelTransfer transaction;
    private TransactionProgress transactionProgress;
    private TextView tvInstruction, tvID, tvSendAmount, tvGetAmount, tvExchangeRate, tvFee, tvTotal, tvRecipient, tvStatus, tvDate;
    private Button btnSubmitReceipt, btnUOB, btnCStore;
    private ImageView ivReceipt;
    private LinearLayout llPaymentOptions;
    private LinearLayout llReceiptUpload;
    private TransactionActivity activity;

    public static TransactionDetailFragment newInstance() {

        Bundle args = new Bundle();
        //args.putParcelable(BUNDLE_TRANSACTION, Parcels.wrap(transaction));
        TransactionDetailFragment fragment = new TransactionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable(BUNDLE_TRANSACTION,Parcels.wrap(transaction));
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null){
//            transaction = savedInstanceState.getParcelable(BUNDLE_TRANSACTION);
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);
        initData();
        initViews(view);
        if (getActivity() instanceof TransactionActivity) {
            activity = (TransactionActivity) getActivity();
        } else {
            Log.d(TAG, "onCreateView: activity should be TransactionActivity");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindData();
        updateUIProgress();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void initData() {
        transaction = CentralDataManager.getCurrentTransaction();
    }

    private void initViews(View view) {

        tvInstruction = (TextView) view.findViewById(R.id.tvInstruction);
        tvID = (TextView) view.findViewById(R.id.tvTransactionIDValue);
        tvSendAmount = (TextView) view.findViewById(R.id.tvSendAmount);
        tvGetAmount = (TextView) view.findViewById(R.id.tvGetAmount);
        tvExchangeRate = (TextView) view.findViewById(R.id.tvExchangeRate);
        tvFee = (TextView) view.findViewById(R.id.tvFeesAmount);
        tvDate = (TextView) view.findViewById(R.id.tvTransactionDate);
        //tvTotal = (TextView) view.findViewById(R.id.tvTotalAmount);
        tvRecipient = (TextView) view.findViewById(R.id.tvRecipientDetail);
        tvStatus = (TextView) view.findViewById(R.id.tvPaymentStatus);
        btnUOB = (Button) view.findViewById(R.id.btnPaymentUOB);
        btnCStore = (Button) view.findViewById(R.id.btnPaymentCStore);
        btnSubmitReceipt = (Button) view.findViewById(R.id.btnSubmitReceipt);
        ivReceipt = (ImageView) view.findViewById(R.id.ivReceipt);
        transactionProgress = (TransactionProgress) view.findViewById(R.id.progressDetail);
        llPaymentOptions = (LinearLayout) view.findViewById(R.id.llPaymentOptions);
        llReceiptUpload = (LinearLayout) view.findViewById(R.id.llReceiptUpload);

        btnUOB.setOnClickListener(OnUOBClick);
        btnCStore.setOnClickListener(OnCStoreClick);
        btnSubmitReceipt.setOnClickListener(OnSubmitClick);
    }

    private void bindData() {

        tvID.setText(transaction.getTransactionID());
        tvDate.setText(transaction.getTransactionDate());
        tvSendAmount.setText(transaction.getTransactionAmount());

        tvRecipient.setText(transaction.getRecipientName() + "\n" + transaction.getRecipientAccountNo());
        tvExchangeRate.setText(transaction.getExchangeRate() + "IDR");
    }

    private View.OnClickListener OnReceiptClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CaptureActivity.startCapturingReceipt(getActivity(), Const.REQUEST_CODE_RECEIPT_PHOTO);
        }
    };

    private View.OnClickListener OnUOBClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.switchFragment(UOBMachinesFragment.newInstance(), true);
        }
    };

    private View.OnClickListener OnCStoreClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.switchFragment(CStoreFragment.newInstance(), true);
        }
    };

    private View.OnClickListener OnSubmitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submitReceipt();
        }
    };

    private void submitReceipt() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting your receipt, please wait...");
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                DialogUtils.showNickelDialog(getActivity(), "Submitted");

                CentralDataManager.getCurrentTransaction().receiptUploaded();
                updateUIProgress();
            }
        }, 1500);
    }

    private void updateUIProgress() {
        transactionProgress.updateProgress(transaction.getTransProgress());

        if (!TextUtils.isEmpty(transaction.getReceiptPhotoUrl())) {
            File image = new File(transaction.getReceiptPhotoUrl());
            Picasso.with(getActivity()).load(image).fit().centerInside().into(ivReceipt);
        }

        switch (transaction.getTransProgress()) {
            case NickelTransfer.TRANS_DRAFT:
                Log.d(TAG, "updateProgress: wrong, should not be new born state");
                Toast.makeText(getActivity(), "something went wrong, check the log", Toast.LENGTH_SHORT).show();
                break;
            case NickelTransfer.TRANS_NEW_BORN:
                llPaymentOptions.setVisibility(View.VISIBLE);
                llReceiptUpload.setVisibility(View.GONE);
                ivReceipt.setClickable(false);
                btnSubmitReceipt.setEnabled(true);
                break;
            case NickelTransfer.TRANS_PAYMENT_MADE:
                llPaymentOptions.setVisibility(View.GONE);
                llReceiptUpload.setVisibility(View.VISIBLE);
                ivReceipt.setOnClickListener(OnReceiptClick);
                ivReceipt.setClickable(true);
                btnSubmitReceipt.setEnabled(true);
                break;
            case NickelTransfer.TRANS_UPLOAD_COMPLETE:
                llPaymentOptions.setVisibility(View.GONE);
                llReceiptUpload.setVisibility(View.VISIBLE);
                btnSubmitReceipt.setVisibility(View.GONE);
                ivReceipt.setClickable(false);
                break;
            case NickelTransfer.TRANS_READY_COLLECTION:
                llPaymentOptions.setVisibility(View.GONE);
                llReceiptUpload.setVisibility(View.VISIBLE);
                btnSubmitReceipt.setVisibility(View.GONE);
                ivReceipt.setClickable(false);
                tvStatus.setText("Funds ready for collection!");
                break;
        }
    }

    @Override
    protected String getPageTitle() {
        return "Confirmation";
    }
}
