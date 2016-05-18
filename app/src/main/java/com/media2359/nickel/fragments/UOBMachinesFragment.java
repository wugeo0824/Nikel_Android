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
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.model.TransactionManager;
import com.media2359.nickel.activities.CaptureActivity;
import com.media2359.nickel.utils.Const;

/**
 * Created by Xijun on 1/4/16.
 */
public class UOBMachinesFragment extends BaseFragment {

    private TextView tvInstruction;
    private Button btnGuide, btnPhoto;
    private LinearLayout btnMap;
    private Transaction transaction;

    public static UOBMachinesFragment newInstance() {

        Bundle args = new Bundle();

        UOBMachinesFragment fragment = new UOBMachinesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uob, container, false);
        transaction = TransactionManager.getManager().getCurrentTransaction();
        initViews(view);
        return view;
    }

    private void initViews(View view) {

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
