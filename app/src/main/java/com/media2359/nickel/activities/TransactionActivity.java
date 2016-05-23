package com.media2359.nickel.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.model.TransactionManager;
import com.media2359.nickel.fragments.BaseFragment;
import com.media2359.nickel.fragments.TransactionDetailFragment;
import com.media2359.nickel.utils.Const;
import com.media2359.nickel.utils.TransactionHistoryUtils;

import org.parceler.Parcels;

import java.io.File;

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
        setContentView(R.layout.activity_transaction);
        setCurrentTransaction();
        initViews();
        switchFragment(TransactionDetailFragment.newInstance(transaction), false);

    }

    private void setCurrentTransaction(){
        transaction = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_TRANSACTION));

        if (transaction.getTransProgress() == Transaction.TRANS_DRAFT){
            finish();
        }else{
            TransactionHistoryUtils.saveTransaction(getApplicationContext(),transaction);
            TransactionManager.getManager().setCurrentTransaction(transaction);
        }

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
    protected void onPause() {
        super.onPause();
        TransactionHistoryUtils.saveTransaction(getApplicationContext(),TransactionManager.getManager().getCurrentTransaction());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Const.REQUEST_CODE_RECEIPT_PHOTO) && (resultCode == RESULT_OK)) {

            String filePath = data.getStringExtra(Const.DATA_PHOTO_FILE);
            File result = new File(filePath);
            //Bitmap thumbImage = BitmapUtils.getThumbnail(this, result);
            TransactionManager.getManager().paymentMadeAndPhotoTaken(result.getAbsolutePath());

            // if image is successfully taken, bring user back to transaction detail screen
            clearFragmentStack();
            switchFragment(TransactionDetailFragment.newInstance(TransactionManager.getManager().getCurrentTransaction()),false);
        }
    }

    @Override
    public void setPageTitle(String title) {
        tvTitle.setText(title);
    }

}
