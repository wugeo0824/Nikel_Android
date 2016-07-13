package com.media2359.nickel.activities;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.event.OnForgotPasswordEvent;
import com.media2359.nickel.event.OnLoginEvent;
import com.media2359.nickel.event.OnLoginWithOtpEvent;
import com.media2359.nickel.event.OnRegisterConsumerEvent;
import com.media2359.nickel.managers.UserSessionManager;
import com.media2359.nickel.network.RequestHandler;
import com.media2359.nickel.utils.DialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * This handles login and sign up
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private String mobileNumber;

    private static final int ANIMATION_DURATION = 1500;
    private ImageView ivLogo, ivPasswordAgain;
    private TextView tvNeedAccount, tvForgotPassword, tvPrivacyPolicy;
    private Button btnSignIn;
    private EditText etPhone, etPassword, etPasswordAgain;
    private RelativeLayout rlLoginContainer;
    private boolean animationPlayed = false;
    private boolean isLoginShowing = true;

    AlertDialog alertDialog;
    EditText etResetPhone, etOtp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        //ivLogo.setAlpha(0f);
        ivPasswordAgain = (ImageView) findViewById(R.id.ivPasswordAgain);
        tvNeedAccount = (TextView) findViewById(R.id.tvNeedAccount);
        tvNeedAccount.setClickable(true);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPassword();
            }
        });
        tvPrivacyPolicy = (TextView) findViewById(R.id.tvPrivacyPolicy);
        initPrivacyMessage();
        btnSignIn = (Button) findViewById(R.id.btnLogin);
        etPhone = (EditText) findViewById(R.id.etPhoneLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordAgain = (EditText) findViewById(R.id.etPasswordAgain);
        rlLoginContainer = (RelativeLayout) findViewById(R.id.rlLoginContainer);
        rlLoginContainer.setLayoutTransition(new LayoutTransition());
        makeAllElementsHiding(true);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!animationPlayed) {
            playAnimation();
        } else {
            if (isLoginShowing) {
                showLogin();
            } else
                showSignUp();
        }

    }

    private void playAnimation() {
        //int distanceY = DisplayUtils.getDisplayHeight(LoginActivity.this);
        //ivLogo.setY(distanceY);

        ivLogo.animate().rotationY(720f).setDuration(ANIMATION_DURATION).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        makeAllElementsHiding(false);
                        showLogin();
                        animationPlayed = true;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        makeAllElementsHiding(false);
                        showLogin();
                        animationPlayed = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }

    private void showEnterOtp() {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_otp, null);
        builder.setView(dialogView);

        // Create the AlertDialog object and return it
        alertDialog = builder.create();
        TextView btnSubmit = (TextView) dialogView.findViewById(R.id.btnSubmit);
        etOtp = (EditText) dialogView.findViewById(R.id.etOTP);
        btnSubmit.setOnClickListener(onSubmitOTP);
        dialogView.findViewById(R.id.btnCancelReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private View.OnClickListener onSubmitOTP = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO
            if (validOtp()){
                if (alertDialog != null)
                    alertDialog.dismiss();
                progressDialog = showProgressDialog("", "Verifying...");
                RequestHandler.loginWithOtp(mobileNumber, etOtp.getText().toString());
            }
        }
    };

    private boolean validOtp() {
        if (TextUtils.isEmpty(etOtp.getText().toString())){
            etOtp.setError("Please enter otp");
            etOtp.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onStop();
    }

    private void showResetPassword() {

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        // Create the AlertDialog object and return it
        alertDialog = builder.create();
        TextView btnResetPassword = (TextView) dialogView.findViewById(R.id.btnResetPassword);
        etResetPhone = (EditText) dialogView.findViewById(R.id.etPhoneNumber);

        btnResetPassword.setOnClickListener(onResetClick);
        dialogView.findViewById(R.id.btnCancelReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private View.OnClickListener onResetClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (etResetPhone.getText().length() > 5) {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();

                if (validPhone(etResetPhone))
                    forgotPassword();
            } else {
                etResetPhone.setError("Please enter full phone number");
            }

        }
    };

    private void forgotPassword() {
        progressDialog = showProgressDialog("", "Please wait...");
        RequestHandler.forgotPassword(etResetPhone.getText().toString());
    }

    private void makeAllElementsHiding(boolean hide) {
        if (hide) {
            tvNeedAccount.setVisibility(View.INVISIBLE);
            rlLoginContainer.setVisibility(View.INVISIBLE);
            tvForgotPassword.setVisibility(View.INVISIBLE);
            btnSignIn.setVisibility(View.INVISIBLE);
        } else {
            tvNeedAccount.setVisibility(View.VISIBLE);
            rlLoginContainer.setVisibility(View.VISIBLE);
            tvForgotPassword.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
        }

    }

    private boolean validPhone(EditText et) {
        String input = et.getText().toString();
        // TODO:
        if (TextUtils.isEmpty(input)) {
            et.requestFocus();
            et.setError("Please enter your phone number");
            return false;
        } else if (input.length() < 5 || input.charAt(0) != '+') {
            et.requestFocus();
            et.setError("Please enter correct phone number");
            return false;
        }

        return true;
    }

    private boolean validPassword() {
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.requestFocus();
            etPassword.setError("Please enter your password");
            return false;
        }
        return true;
    }

    private boolean validPasswordAgain() {
        String passwordB = etPasswordAgain.getText().toString();
        String passwordA = etPassword.getText().toString();

        if (TextUtils.isEmpty(passwordB) || !passwordA.equals(passwordB)) {
            etPasswordAgain.requestFocus();
            etPasswordAgain.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private void initPrivacyMessage() {

        CharSequence privacy1 = getString(R.string.privacy_message_1);
        final CharSequence userAgreesment = getString(R.string.user_agreement);
        final CharSequence privacypolicy = getString(R.string.privacy_policy);

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        SpannableString spannableString1 = new SpannableString(userAgreesment);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent url = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(url);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        }, 0, spannableString1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString2 = new SpannableString(privacypolicy);
        spannableString2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent url = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yahoo.com"));
                startActivity(url);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                // super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        }, 0, spannableString2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strBuilder.append(privacy1).append("\n")
                .append(spannableString1).append(" & ")
                .append(spannableString2);

        tvPrivacyPolicy.setText(strBuilder);
        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showLogin() {
        tvNeedAccount.setText(getString(R.string.need_account));
        ivPasswordAgain.setVisibility(View.GONE);
        etPasswordAgain.setVisibility(View.GONE);
        tvForgotPassword.setVisibility(View.VISIBLE);
        btnSignIn.setText(getString(R.string.sign_in));
        btnSignIn.setOnClickListener(onSignInClick);
        tvPrivacyPolicy.setVisibility(View.GONE);
        tvNeedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUp();
            }
        });
        isLoginShowing = true;
    }

    private View.OnClickListener onSignInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!validPhone(etPhone))
                return;

            if (!validPassword())
                return;

            //TODO sign in
            progressDialog = showProgressDialog("Signing in", "Please wait...");
            RequestHandler.login(etPhone.getText().toString(), etPassword.getText().toString());

        }
    };

    private ProgressDialog showProgressDialog(String title, String message) {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);

        if (!TextUtils.isEmpty(title))
            progressDialog.setTitle(title);

        if (!TextUtils.isEmpty(message))
            progressDialog.setMessage(message);

        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        return progressDialog;
    }

    private void proceedToMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void showSignUp() {
        tvNeedAccount.setText(getString(R.string.have_account));
        ivPasswordAgain.setVisibility(View.VISIBLE);
        etPasswordAgain.setVisibility(View.VISIBLE);
        tvForgotPassword.setVisibility(View.GONE);
        btnSignIn.setText(getString(R.string.join_nickel));
        btnSignIn.setOnClickListener(onJoinClick);
        tvPrivacyPolicy.setVisibility(View.VISIBLE);
        tvNeedAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });
        isLoginShowing = false;
    }

    private View.OnClickListener onJoinClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!validPhone(etPhone))
                return;

            if (!validPassword())
                return;

            if (!validPasswordAgain())
                return;

            mobileNumber = etPhone.getText().toString();

            progressDialog = showProgressDialog("", "Please wait...");
            RequestHandler.registerConsumer(mobileNumber, etPassword.getText().toString());

        }
    };

    @Subscribe
    public void onEvent(OnRegisterConsumerEvent event) {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

        if (event.isSuccess()){
            showEnterOtp();
        }

        Toast.makeText(LoginActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(OnLoginWithOtpEvent event) {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

        if (event.isSuccess()){
            proceedToMainActivity();
        }else {
            Toast.makeText(LoginActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent(OnForgotPasswordEvent event) {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

        if (event.isSuccess()){
            //showEnterOtp();
            Toast.makeText(LoginActivity.this, "Reset Password request received", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(LoginActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEvent(OnLoginEvent event) {
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

        if (event.isSuccess()){
            proceedToMainActivity();
        }else {
            Toast.makeText(LoginActivity.this, event.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
