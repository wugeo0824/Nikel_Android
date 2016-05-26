package com.media2359.nickel.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.fragments.BaseFragment;
import com.media2359.nickel.fragments.TransactionDetailFragment;
import com.media2359.nickel.managers.CentralDataManager;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.utils.Const;

import java.io.File;

/**
 * Created by Xijun on 1/4/16.
 */
public class TransactionActivity extends BaseActivity implements BaseFragment.FragmentVisibleListener {

    private static final String TAG = "TransactionActivity";

    private TextView tvTitle;
    private FragmentManager manager;

//    private static final String EXTRA_TRANSACTION = "extra_transaction";
//    private static final String EXTRA_RECIPIENT = "extra_recipient";

    public static void startTransactionActivity(Activity startingActivity, NickelTransfer transaction, int recipientPosition) {
        Intent intent = new Intent(startingActivity, TransactionActivity.class);
        //intent.putExtra(EXTRA_TRANSACTION, Parcels.wrap(transaction));
        //intent.putExtra(EXTRA_RECIPIENT, recipientPosition);
        CentralDataManager.setCurrentTransaction(transaction, recipientPosition);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        //initCurrentTransaction();
        initViews();

        if (savedInstanceState == null)
            switchFragment(TransactionDetailFragment.newInstance(), false);
    }

    private void initCurrentTransaction() {
        //transaction = CentralDataManager.getCurrentTransaction();

//        Intent intent = getIntent();
//        if (intent != null) {
//            recipientPosition = intent.getIntExtra(EXTRA_RECIPIENT, -1);
//        }
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
        //TODO save the transaction locally
        CentralDataManager.getInstance().saveCurrentTransactionToRecipient();
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
            CentralDataManager.getCurrentTransaction().paymentMadeAndPhotoTaken(result.getAbsolutePath());

            // if image is successfully taken, bring user back to transaction detail screen
            clearFragmentStack();
            switchFragment(TransactionDetailFragment.newInstance(), false);
        }
    }

    @Override
    public void setPageTitle(String title) {
        tvTitle.setText(title);
    }

}
