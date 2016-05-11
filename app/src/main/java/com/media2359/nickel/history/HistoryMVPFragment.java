package com.media2359.nickel.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.media2359.nickel.R;
import com.media2359.nickel.activities.TransactionActivity;
import com.media2359.nickel.model.Transaction;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xijun on 25/4/16.
 */
public class HistoryMVPFragment extends MvpLceFragment<SwipeRefreshLayout, List<Transaction>, HistoryView, HistoryPresenter>
        implements HistoryView, SwipeRefreshLayout.OnRefreshListener, HistoryMVPAdapter.HistoryItemClickListener {

    @Bind(R.id.rvHistory)
    RecyclerView rvHistory;

    HistoryMVPAdapter historyAdapter;

    public static HistoryMVPFragment newInstance() {
        
        Bundle args = new Bundle();
        
        HistoryMVPFragment fragment = new HistoryMVPFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        contentView.setOnRefreshListener(this);
        historyAdapter = new HistoryMVPAdapter(getActivity(),this);
        rvHistory.setHasFixedSize(true);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(historyAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData(false);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return "Sorry, something wrong happened. Please retry.\n" + e.getLocalizedMessage();
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
    }

    @Override
    public HistoryPresenter createPresenter() {
        return new HistoryPresenter();
    }

    @Override
    public void setData(List<Transaction> data) {
        historyAdapter.setTransactionsList(data);
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadHistory(pullToRefresh);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void OnItemClick(RecyclerView.ViewHolder viewHolder, Transaction transaction) {
        TransactionActivity.startTransactionActivity(getActivity(), transaction);
    }
}
