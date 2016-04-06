package com.media2359.nickel.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Xijun on 17/3/16.
 */
public abstract class BaseFragment extends Fragment {

    private FragmentVisibleListener mListener;

    protected abstract String getPageTitle();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListener != null) {
            mListener.setPageTitle(getPageTitle());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentVisibleListener) {
            mListener = (FragmentVisibleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = NickelApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow this fragment to update the toolbar layout which will be in accordance with the one this fragment needs.
     */
    public interface FragmentVisibleListener {

        /**
         * This will be the title of the collapsed toolbar
         *
         * @param title
         */
        void setPageTitle(String title);

    }
}
