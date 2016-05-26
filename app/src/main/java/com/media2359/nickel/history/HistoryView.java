package com.media2359.nickel.history;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.media2359.nickel.model.NickelTransfer;

import java.util.List;

/**
 * Created by Xijun on 25/4/16.
 */
public interface HistoryView extends MvpLceView<List<NickelTransfer>> {

    void showEmptyView();

}
