package com.media2359.nickel.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.event.OnProfileChangedEvent;
import com.media2359.nickel.fragments.BaseFragment;
import com.media2359.nickel.fragments.HomeFragment;
import com.media2359.nickel.fragments.ProfileFragment;
import com.media2359.nickel.fragments.RewardsFragment;
import com.media2359.nickel.fragments.SpinnerFragment;
import com.media2359.nickel.help.HelpFragment;
import com.media2359.nickel.history.HistoryMVPFragment;
import com.media2359.nickel.managers.CentralDataManager;
import com.media2359.nickel.managers.UserSessionManager;
import com.media2359.nickel.model.MyProfile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * This handles the transaction logic
 */
public class MainActivity extends BaseActivity implements BaseFragment.FragmentVisibleListener {

    private static final int MY_PERMISSION_CAMERA = 91;

    private FragmentManager manager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView tvTitle, tvHeaderView;
    private Fragment mSpinnerFragment;
    private NavigationView navigationView;
    private RelativeLayout btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (checkCameraHardware(getApplicationContext()))
            checkCameraPermission();
        else {
            Toast.makeText(getApplicationContext(), "Sorry, this app needs a camera to work.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        initViews();
        // pre-load the profile information
        MyProfile.getCurrentProfile(getApplicationContext());
        CentralDataManager.getInstance();

        if (null == savedInstanceState) {
            switchFragment(HomeFragment.newInstance(), false);
        }

    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void checkCameraPermission() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        // Here, thisActivity is the current activity
        if (permissionCheck
                != PackageManager.PERMISSION_GRANTED) {
            // explanation needed, we can request the permission.
            showCameraRationale();
        }
    }

    private void showCameraRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires your phone camera to capture photos of your ID card and receipt. \n\nPlease grant permission for this app to work properly.");
        builder.setTitle("Alert");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_CAMERA);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

//    @Override
//    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//        mDrawerToggle.syncState();
//    }

    private void initViews() {
        manager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        btnLogout = (RelativeLayout) findViewById(R.id.btnLogout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_closed  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //invalidateOptionsMenu();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //mDrawerToggle.syncState();
        tvHeaderView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvHeaderView);
        tvTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutDialog();
            }
        });

    }

    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            // mDrawerToggle.onDrawerStateChanged(DrawerLayout.STATE_SETTLING);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            //mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.syncState();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.nav_home); // default checked item is the first one
        navigationView.setNavigationItemSelectedListener(itemListener);

    }

    private NavigationView.OnNavigationItemSelectedListener itemListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Fragment newFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    if (getCurrentFragment() instanceof HomeFragment) {
                        newFragment = null;
                    } else {
                        clearFragmentStack();
                        switchFragment(new HomeFragment(), false);
                    }
                    break;
                case R.id.nav_profile:
                    if (getCurrentFragment() instanceof ProfileFragment) {
                        newFragment = null;
                    } else {
                        newFragment = new ProfileFragment();
                    }
                    break;
//                case R.id.nav_recipients:
//                    newFragment = new RecipientListFragment();
//                    break;
                case R.id.nav_history:
                    if (getCurrentFragment() instanceof HistoryMVPFragment) {
                        newFragment = null;
                    } else {
                        newFragment = HistoryMVPFragment.newInstance();
                    }
                    break;
                case R.id.nav_rewards:
                    newFragment = new RewardsFragment();
                    break;
                case R.id.nav_help:
                    newFragment = new HelpFragment();
                    break;
//                case R.id.nav_sign_out:
//                    showSignOutDialog();
//                    return false;
                default:
                    return false;
            }

            if (newFragment != null) {
                switchFragmentAndSyncDrawer(newFragment, item.getItemId());
            }

            mDrawerLayout.closeDrawers();
            return true;
        }
    };

    public void switchFragmentAndSyncDrawer(Fragment fragment, @IdRes int itemId) {
        if (manager.getBackStackEntryCount() >= 1) //only maintain one entry on backStack
            manager.popBackStack();

        overridePendingTransition(R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_left);
        switchFragment(fragment, true);
        //item.setChecked(true);
        navigationView.setCheckedItem(itemId);
    }

    AlertDialog dialog;

    private void showSignOutDialog() {

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //TODO sign out
                UserSessionManager.SignOut();
                backToLogin();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties
        builder.setCancelable(false);
        builder.setTitle("Want to sign out?");
        builder.setMessage("Click yes to sign out");

        // Create the AlertDialog
        dialog = builder.create();
        dialog.show();

    }

    private void backToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    void clearFragmentStack() {
        for (int i = 0; i < manager.getBackStackEntryCount(); ++i)
            manager.popBackStack();
    }

    Fragment getCurrentFragment() {
        return manager.findFragmentById(R.id.fl_container);
    }


    public void switchFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.show(fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getCurrentFragment() != null) {
            updateDrawerItem(getCurrentFragment());
        }
    }

    private void updateDrawerItem(Fragment fragment) {
        // TODO
        if (fragment instanceof HomeFragment) {
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(OnProfileChangedEvent onProfileChangedEvent) {
        if (MyProfile.getCurrentProfile(this) != null)
            tvHeaderView.setText(MyProfile.getCurrentProfile(this).getFullName());
    }

    public void showLoadingSpinner() {
        mSpinnerFragment = new SpinnerFragment();
        manager.beginTransaction().add(R.id.fl_container, mSpinnerFragment).commit();
    }

    public void dismissLoadingSpinner() {
        if (mSpinnerFragment == null)
            return;

        manager.beginTransaction().remove(mSpinnerFragment).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();

        if (MyProfile.getCurrentProfile(getApplicationContext()) == null) {
            //tvHeaderView.setVisibility(View.INVISIBLE);
            tvHeaderView.setText("Please complete your profile");
        } else {
            tvHeaderView.setText(MyProfile.getCurrentProfile(getApplicationContext()).getFullName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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
    protected void onDestroy() {
        super.onDestroy();
        CentralDataManager.getInstance().close();
    }

    @Override
    public void setPageTitle(String title) {
        tvTitle.setText(title);
    }

}
