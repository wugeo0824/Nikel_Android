package com.media2359.nickel.history;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.network.NikelService;
import com.media2359.nickel.network.RequestHandler;
import com.media2359.nickel.network.responses.TransfersResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xijun on 25/4/16.
 */
public class HistoryPresenter extends MvpBasePresenter<HistoryView> {

    List<NickelTransfer> allTransactions = new ArrayList<>();
    boolean pullToRefresh = false;

    public void loadHistory(final boolean pullToRefresh) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        loadTransactionsFromServer();
    }

    private void notifyLoadingComplete() {
        if (allTransactions == null || allTransactions.isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().setData(allTransactions);
            getView().showContent();
        }
    }


    public void loadTransactionsFromServer() {

        // only get the first page
        Call<TransfersResponse> call = NikelService.getApiManager().getTransfers(1);
        call.enqueue(new Callback<TransfersResponse>() {
            @Override
            public void onResponse(Call<TransfersResponse> call, Response<TransfersResponse> response) {
                if (response.isSuccessful()) {
                    allTransactions.clear();
                    allTransactions.addAll(response.body().getTransfers());
                    notifyLoadingComplete();
                } else {
                    getView().showErrorMessage(RequestHandler.convert400Response(response));
                }
            }

            @Override
            public void onFailure(Call<TransfersResponse> call, Throwable t) {
                getView().showError(t, pullToRefresh);
            }
        });
    }

}
