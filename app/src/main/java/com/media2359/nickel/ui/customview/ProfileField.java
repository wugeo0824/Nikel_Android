package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextWatcher;
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
    ImageView ivLeftImage;


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
        ivFieldStatus.setVisibility(INVISIBLE);
        ivLeftImage = (ImageView) findViewById(R.id.ivFieldLeft);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProfileField);

            String hint = a.getString(R.styleable.ProfileField_tvHint);
            inputLayout.setHint(hint);

            int inputType = a.getInt(R.styleable.ProfileField_inputType,1); // default value is 1, TEXT
            switch (inputType){
                case 0:
                    // name
                    etInputLayout.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    break;
                case 1:
                    // text
                    etInputLayout.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                    break;
                case 2:
                    // number
                    etInputLayout.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                    break;
                case 3:
                    // date
                    etInputLayout.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                    break;
                default:
                    etInputLayout.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                    break;
            }

            boolean enabled = a.getBoolean(R.styleable.ProfileField_isCompleted, true);
            setEnabledEditing(enabled);

            @DrawableRes int drawableRes = a.getInt(R.styleable.ProfileField_ivLeftImageRes,R.drawable.ic_person_black_24dp);
            ivLeftImage.setImageDrawable(getResources().getDrawable(drawableRes));
        }
    }

    public void setShowCompletedStatus(boolean show){
        if (show){
            ivFieldStatus.setVisibility(VISIBLE);
        }else{
            ivFieldStatus.setVisibility(INVISIBLE);
        }
    }

    public void showErrorMessage(boolean show, @Nullable String text){
        if (show){
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(text);
        }else{
            inputLayout.setErrorEnabled(false);
        }
    }

    public void setTextWatcher(TextWatcher textWatcher){
        etInputLayout.addTextChangedListener(textWatcher);
    }

    public void setOnFocusChangedListener(OnFocusChangeListener onFocusChangedListener){
        etInputLayout.setOnFocusChangeListener(onFocusChangedListener);
    }

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
