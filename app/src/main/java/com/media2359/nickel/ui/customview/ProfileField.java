package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.utils.DisplayUtils;

/**
 * Created by Xijun on 10/3/16.
 */
public class ProfileField extends RelativeLayout {

//    TextView tvFieldTitle;
    EditText etInputLayout;
    ImageView ivFieldStatus;
    TextInputLayout inputLayout;


    public ProfileField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProfileField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        inflate(context, R.layout.item_profile_field, this);
        inputLayout = (TextInputLayout) findViewById(R.id.inputLayout);
        etInputLayout = (EditText) findViewById(R.id.etInputLayout);
        ivFieldStatus = (ImageView) findViewById(R.id.ivFieldStatus);
        ivFieldStatus.setVisibility(GONE);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProfileField);

            String hint = a.getString(R.styleable.ProfileField_tvHint);
            inputLayout.setHint(hint);

            boolean enabled = a.getBoolean(R.styleable.ProfileField_isCompleted, true);
            setEnabledEditing(enabled);
        }

        etInputLayout.setOnEditorActionListener(enterListener);
    }

    private final TextView.OnEditorActionListener enterListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){

                //DisplayUtils.hideKeyboard(v);
                ivFieldStatus.setVisibility(VISIBLE);
            }
            return false;
        }
    };

    public void setEnabledEditing(boolean enabled) {
        etInputLayout.setFocusable(enabled);
        etInputLayout.setFocusableInTouchMode(enabled);
        if (!enabled) {
            etInputLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
