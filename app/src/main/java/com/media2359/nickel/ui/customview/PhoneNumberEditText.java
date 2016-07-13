package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.media2359.nickel.utils.TypefaceHelper;

/**
 * Created by Xijun on 13/7/16.
 */
public class PhoneNumberEditText extends EditText {

    private PhoneNumberEditText instance;

    public PhoneNumberEditText(Context context) {
        super(context);
        init();
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhoneNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        instance = this;
        setTypeface(TypefaceHelper.get(getContext()));
        addTextChangedListener(phoneWatcher);
    }

    // to add a permenant prefix "+" to the phone number
    private TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            instance.removeTextChangedListener(this);

            if (!s.toString().startsWith("+")){
                instance.setText("+"+s.toString());
            }else
                instance.setText(s.toString());

            instance.setSelection(instance.getText().toString().length());
            instance.addTextChangedListener(this);
        }
    };
}
