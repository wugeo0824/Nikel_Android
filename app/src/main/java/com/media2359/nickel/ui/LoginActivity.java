package com.media2359.nickel.ui;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.media2359.nickel.R;

/**
 * This handles login and sign up
 */
public class LoginActivity extends AppCompatActivity {

    private ImageView ivLogo,ivPasswordAgain;
    private TextView tvNeedAccount,tvForgotPassword,tvPrivacyPolicy;
    private Button btnSignIn;
    private EditText etPhone,etPassword,etPasswordAgain;
    private RelativeLayout rlLoginContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        showLogin();
    }

    private void initViews() {
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivPasswordAgain = (ImageView) findViewById(R.id.ivPasswordAgain);
        tvNeedAccount = (TextView) findViewById(R.id.tvNeedAccount);
        tvNeedAccount.setClickable(true);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvPrivacyPolicy = (TextView) findViewById(R.id.tvPrivacyPolicy);
        initPrivacyMessage();
        btnSignIn = (Button) findViewById(R.id.btnLogin);
        etPhone = (EditText) findViewById(R.id.etPhoneLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordAgain = (EditText) findViewById(R.id.etPasswordAgain);
        rlLoginContainer = (RelativeLayout) findViewById(R.id.rlLoginContainer);
        rlLoginContainer.setLayoutTransition(new LayoutTransition());
    }

    private boolean validPhone(){
        String input = etPhone.getText().toString();
        // TODO:
        if(TextUtils.isEmpty(input)){
            etPhone.setError("Please enter your phone number");
            return false;
        }else if(input.length() < 8){
            etPhone.setError("Please enter full phone number");
            return false;
        }

        return true;
    }

    private boolean validPassword(){
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(password)){
            etPassword.setError("Please enter your password");
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

    private void showLogin(){

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
    }

    private View.OnClickListener onSignInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            if (!validPhone())
//                return;
//
//            if (!validPassword())
//                return;

            //TODO sign in
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    };

    private void showSignUp(){
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
    }

    private View.OnClickListener onJoinClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            if (!validPhone())
//                return;
//
//            if (!validPassword())
//                return;

            //TODO join nickel
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    };
}
