package com.media2359.nickel.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.model.TransactionManager;
import com.media2359.nickel.ui.TransactionActivity;
import com.media2359.nickel.ui.customview.TransactionProgress;
import com.media2359.nickel.utils.Const;
import com.media2359.nickel.utils.DialogUtils;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;

/**
 * Created by Xijun on 10/3/16.
 */
public class TransactionDetailFragment extends BaseFragment {

    private static final String TAG = "DetailFragment";
    //public static final String BUNDLE_BARCODE_CONTENT = "barcode_content";
    public static final String BUNDLE_TRANSACTION = "transaction";
    //private String barcodeContent = "";
    private int progress;
    private Transaction transaction;
    //private ImageView ivBarcode;
    private TransactionProgress transactionProgress;
    private TextView tvInstruction, tvID, tvSendAmount, tvGetAmount, tvExchangeRate, tvFee, tvTotal, tvRecipient, tvStatus;
    private Button btnSubmitReceipt, btnUOB, btnCStore;
    private ImageView ivReceipt;
    private LinearLayout llPaymentOptions;
    private FrameLayout flReceiptUpload;
    private TransactionActivity activity;

    public static TransactionDetailFragment newInstance(Transaction transaction) {

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
        updateUIProgress();
    }

    private void initData() {
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            transaction = Parcels.unwrap(bundle.getParcelable(BUNDLE_TRANSACTION));
//            Log.d(TAG, "initData: transaction progress is " + transaction.getTransProgress());
//        } else {
//            Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "initData: bundle is null");
//        }
        transaction = TransactionManager.getManager().getCurrentTransaction();
    }

    private void initViews(View view) {

        tvInstruction = (TextView) view.findViewById(R.id.tvInstruction);
        tvID = (TextView) view.findViewById(R.id.tvTransactionID);
        tvSendAmount = (TextView) view.findViewById(R.id.tvSendAmount);
        tvGetAmount = (TextView) view.findViewById(R.id.tvGetAmount);
        tvExchangeRate = (TextView) view.findViewById(R.id.tvExchangeRate);
        tvFee = (TextView) view.findViewById(R.id.tvFeesAmount);
        tvTotal = (TextView) view.findViewById(R.id.tvTotalAmount);
        tvRecipient = (TextView) view.findViewById(R.id.tvRecipientDetail);
        tvStatus = (TextView) view.findViewById(R.id.tvPaymentStatus);
        btnUOB = (Button) view.findViewById(R.id.btnPaymentUOB);
        btnCStore = (Button) view.findViewById(R.id.btnPaymentCStore);
        btnSubmitReceipt = (Button) view.findViewById(R.id.btnSubmitReceipt);
        ivReceipt = (ImageView) view.findViewById(R.id.ivReceipt);
        transactionProgress = (TransactionProgress) view.findViewById(R.id.progressDetail);
        llPaymentOptions = (LinearLayout) view.findViewById(R.id.llPaymentOptions);
        flReceiptUpload = (FrameLayout) view.findViewById(R.id.rlReceiptUpload);

        tvRecipient.setText(transaction.getRecipientName());

        btnUOB.setOnClickListener(OnUOBClick);
        btnCStore.setOnClickListener(OnCStoreClick);
        btnSubmitReceipt.setOnClickListener(OnSubmitClick);
    }

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

                TransactionManager.getManager().receiptUploaded();
                updateUIProgress();
            }
        }, 1500);
    }

    private void updateUIProgress() {
        transactionProgress.updateProgress(transaction.getTransProgress());
        Transaction transaction = TransactionManager.getManager().getCurrentTransaction();

        if (!TextUtils.isEmpty(transaction.getReceiptPhotoUrl())){
            File image = new File(transaction.getReceiptPhotoUrl());
            Picasso.with(getActivity()).load(image).fit().centerInside().into(ivReceipt);
        }

        switch (transaction.getTransProgress()) {
            case Transaction.TRANS_DRAFT:
                Log.d(TAG, "updateProgress: wrong, should not be new born state");
                Toast.makeText(getActivity(), "something went wrong, check the log", Toast.LENGTH_SHORT).show();
                break;
            case Transaction.TRANS_NEW_BORN:
                llPaymentOptions.setVisibility(View.VISIBLE);
                flReceiptUpload.setVisibility(View.GONE);
                btnSubmitReceipt.setEnabled(true);
                break;
            case Transaction.TRANS_PAYMENT_MADE:
                llPaymentOptions.setVisibility(View.GONE);
                flReceiptUpload.setVisibility(View.VISIBLE);
                btnSubmitReceipt.setEnabled(true);
                break;
            case Transaction.TRANS_UPLOAD_COMPLETE:
                llPaymentOptions.setVisibility(View.GONE);
                flReceiptUpload.setVisibility(View.VISIBLE);
                btnSubmitReceipt.setVisibility(View.GONE);
                break;
            case Transaction.TRANS_READY_COLLECTION:
                llPaymentOptions.setVisibility(View.GONE);
                flReceiptUpload.setVisibility(View.VISIBLE);
                btnSubmitReceipt.setVisibility(View.GONE);
                tvStatus.setText("Funds ready for collection!");
                break;
        }
    }

    @Override
    protected String getPageTitle() {
        return "Confirmation";
    }
}
