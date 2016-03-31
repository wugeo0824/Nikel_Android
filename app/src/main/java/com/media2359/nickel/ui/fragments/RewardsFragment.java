package com.media2359.nickel.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;

/**
 * Created by Xijun on 31/3/16.
 */
public class RewardsFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
    }

    @Override
    protected String getPageTitle() {
        return "Rewards";
    }
}
