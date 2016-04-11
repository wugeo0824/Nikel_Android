package com.media2359.nickel.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;
import com.media2359.nickel.event.OnTransactionDetailClickEvent;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.TransactionActivity;
import com.media2359.nickel.ui.adapter.HistoryAdapter;
import com.media2359.nickel.ui.customview.ThemedSwipeRefreshLayout;
import com.media2359.nickel.utils.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xijun on 4/4/16.
 */
public class TransactionHistoryFragment extends BaseFragment {

    private RecyclerView rvHistory;
    private ThemedSwipeRefreshLayout srl;
    private HistoryAdapter historyAdapter;
    List<Transaction> transactionsList = new ArrayList<>();

    public static TransactionHistoryFragment newInstance() {

        Bundle args = new Bundle();

        TransactionHistoryFragment fragment = new TransactionHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initViews(view);
        initData();
        return view;
    }

    private void initViews(View view) {
        srl = (ThemedSwipeRefreshLayout) view.findViewById(R.id.srlHistory);
        srl.setOnRefreshListener(OnRefreshListener);
        rvHistory = (RecyclerView) view.findViewById(R.id.rvHistory);
        historyAdapter = new HistoryAdapter(transactionsList);
        rvHistory.setHasFixedSize(true);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(historyAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private void initData() {
        mockData();
    }

    private void mockData() {
        historyAdapter.notifyItemRangeRemoved(0, transactionsList.size());
        transactionsList.clear();
        Transaction a = new Transaction("1238u9ashjd", "March 2, 2016", "500", "Funds Ready for Collection", Transaction.TRANS_READY_COLLECTION, "Husband", 1235);
        Transaction b = new Transaction("2238u9ashjd", "March 12, 2016", "100", "Please DO xxxxxx", Transaction.TRANS_UPLOAD_COMPLETE, "Husband", 1235);
        Transaction c = new Transaction("3238u9ashjd", "April 2, 2016", "540", "Please DO yyyyy", Transaction.TRANS_PAYMENT_MADE, "Husband", 2354);
        Transaction d = new Transaction("4238u9ashjd", "May 2, 2016", "1300", "Please DO zzzzz", Transaction.TRANS_NEW_BORN, "Husband", 8657);
        transactionsList.add(a);
        transactionsList.add(b);
        transactionsList.add(c);
        transactionsList.add(d);
        transactionsList.add(a);
        transactionsList.add(b);
        transactionsList.add(c);
        transactionsList.add(d);
        historyAdapter.notifyItemRangeInserted(0, transactionsList.size());
        if (srl.isRefreshing()) {
            srl.setRefreshing(false);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // TODO;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mockData();
                }
            }, 1000);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(OnTransactionDetailClickEvent event) {
        //TODO: change to actual ID
        TransactionActivity.startTransactionActivity(getActivity(), transactionsList.get(event.getPosition()));
    }


    @Override
    protected String getPageTitle() {
        return "Transaction History";
    }
}
