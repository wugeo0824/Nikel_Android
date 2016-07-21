package com.media2359.nickel.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.CaptureActivity;
import com.media2359.nickel.managers.CentralDataManager;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.utils.Const;

import java.util.Locale;

/**
 * Created by Xijun on 1/4/16.
 */
public class UOBMachinesFragment extends BaseFragment {

    //public static final String BUNDLE_TRANSACTION = "transaction";

    private TextView tvInstruction, tvTransferTo, tvAccountNo;
    private Button btnGuide, btnPhoto;
    private LinearLayout btnMap;
    private NickelTransfer transaction;

    public static UOBMachinesFragment newInstance() {

        Bundle args = new Bundle();
        //args.putParcelable(BUNDLE_TRANSACTION, Parcels.wrap(transaction));
        UOBMachinesFragment fragment = new UOBMachinesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uob, container, false);
        initData();
        return view;
    }

    private void initData() {
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            transaction = Parcels.unwrap(bundle.getParcelable(BUNDLE_TRANSACTION));
//        }
        transaction = CentralDataManager.getCurrentTransaction();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvInstruction = (TextView) view.findViewById(R.id.tvInstruction);
        tvTransferTo = (TextView) view.findViewById(R.id.tvTransferTo);
        tvAccountNo = (TextView) view.findViewById(R.id.tvAccountNo);

        btnGuide = (Button) view.findViewById(R.id.btnStepGuide);
        btnMap = (LinearLayout) view.findViewById(R.id.btnUOBMachines);
        btnPhoto = (Button) view.findViewById(R.id.btnTakeReceipt);

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGuide();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.startCapturingReceipt(getActivity(), Const.REQUEST_CODE_RECEIPT_PHOTO);
            }
        });

        bindData();
    }

    private void bindData() {
        tvInstruction.setText(String.format(Locale.getDefault(), getString(R.string.uob_instruction), transaction.getAmountSent()));
        tvTransferTo.setText(String.format(Locale.getDefault(), getString(R.string.transfer_to), "Nickel"));
        tvAccountNo.setText(String.format(Locale.getDefault(), getString(R.string.account_no), transaction.getRecipientAccountNo()));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void openMap() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.WEB_LINK_UOB_MAP));
        startActivity(i);
    }

    private void openGuide() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.WEB_LINK_GUIDE));
        startActivity(i);
    }


    @Override
    protected String getPageTitle() {
        return "Payment";
    }
}
