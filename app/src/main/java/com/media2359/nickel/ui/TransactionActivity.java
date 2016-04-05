package com.media2359.nickel.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.fragments.BaseFragment;
import com.media2359.nickel.ui.fragments.TransactionDetailFragment;

import org.parceler.Parcels;

/**
 * Created by Xijun on 1/4/16.
 */
public class TransactionActivity extends BaseActivity implements BaseFragment.FragmentVisibleListener {

    private TextView tvTitle;
    private FragmentManager manager;
    private CoordinatorLayout coordinatorLayout;
    //private Transaction transaction;
    //private int transactionProgress = -1;
    private Transaction transaction;

    private static final String EXTRA_TRANSACTION = "extra_item";

    public static void startTransactionActivity(Activity startingActivity, Transaction transaction) {
        Intent intent = new Intent(startingActivity, TransactionActivity.class);
        //intent.putExtra(EXTRA_TRANSACTION, transaction.getTransactionID());
        intent.putExtra(EXTRA_TRANSACTION, Parcels.wrap(transaction));
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        transaction = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_TRANSACTION));
        initViews();
        switchFragment(TransactionDetailFragment.newInstance(transaction), false);

    }

    private void initViews() {

        manager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tvTitle = (TextView) findViewById(R.id.tvToolbarTitle);
    }

    void clearFragmentStack() {
        for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
            manager.popBackStack();
    }

    Fragment getCurrentFragment() {
        return manager.findFragmentById(R.id.flTranContainer);
    }


    public void switchFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.flTranContainer, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setPageTitle(String title) {
        tvTitle.setText(title);
    }

}
