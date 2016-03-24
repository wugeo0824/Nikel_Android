package com.media2359.nickel.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;

/**
 * Created by Xijun on 17/3/16.
 */
public class RecipientFragment extends BaseFragment {

    private static final String BUNDLE_RECIPIENT_ID = "recipient_id";
    private int recipientID;

    public static RecipientFragment newInstance(int recipientID){
        RecipientFragment instance = new RecipientFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_RECIPIENT_ID,recipientID);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipient, container, false);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            recipientID = bundle.getInt(BUNDLE_RECIPIENT_ID);
        }
        initViews(view);
        return view;
    }

    private void initViews(View view) {
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.my_recipients);
    }

}
