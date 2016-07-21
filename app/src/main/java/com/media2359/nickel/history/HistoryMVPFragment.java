package com.media2359.nickel.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import com.media2359.nickel.R;
import com.media2359.nickel.activities.MainActivity;
import com.media2359.nickel.activities.TransactionActivity;
import com.media2359.nickel.model.NickelTransfer;

import java.util.List;

/**
 * Created by Xijun on 25/4/16.
 */
public class HistoryMVPFragment extends MvpLceFragment<SwipeRefreshLayout, List<NickelTransfer>, HistoryView, HistoryPresenter>
        implements HistoryView, SwipeRefreshLayout.OnRefreshListener, HistoryMVPAdapter.HistoryItemClickListener {

    RecyclerView rvHistory;
    TextView emptyView;

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
        contentView.setOnRefreshListener(this);
        emptyView = (TextView) view.findViewById(R.id.emptyView);
        historyAdapter = new HistoryMVPAdapter(getActivity(), this);
        rvHistory = (RecyclerView) view.findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(historyAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainActivity){
            ((MainActivity) getActivity()).setPageTitle(getString(R.string.transaction_history));
        }
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return "Sorry, something wrong happened. Please retry.\n" + e.getLocalizedMessage();
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void setData(List<NickelTransfer> data) {
        historyAdapter.setTransactionsList(data);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        contentView.setRefreshing(pullToRefresh);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public HistoryPresenter createPresenter() {
        return new HistoryPresenter();
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
    public void OnItemClick(RecyclerView.ViewHolder viewHolder, NickelTransfer transaction) {
        TransactionActivity.startTransactionActivity(getActivity(), transaction, -1);
    }

    @Override
    public void showEmptyView() {
        contentView.setRefreshing(false);
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        contentView.setRefreshing(false);
        emptyView.setVisibility(View.GONE);
    }
}
