package com.media2359.nickel.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;
import com.media2359.nickel.ui.MainActivity;

/**
 * Created by Xijun on 10/3/16.
 */
public class HomeFragment extends BaseFragment {

    private MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //initViews(view);
        if (getActivity() instanceof MainActivity)
            mainActivity = (MainActivity) getActivity();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.home);
    }


}
