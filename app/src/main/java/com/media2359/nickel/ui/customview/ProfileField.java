package com.media2359.nickel.ui.customview;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.media2359.nickel.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Xijun on 10/3/16.
 */
public class ProfileField extends RelativeLayout {

    private static final String TAG = "ProfileField";
    private boolean shouldIntercept = false;

    public final static int POPUP_TYPE_CALENDAR = 10;
    public final static int POPUP_TYPE_LIST_DIALOG = 11;

    private DatePickerDialog dateOfBirth;
    private SimpleDateFormat dateFormatter;

    //    TextView tvFieldTitle;
    TextInputEditText etInputLayout;
    ImageView ivFieldStatus;
    TextInputLayout inputLayout;
    ImageView ivLeftImage;
    String errorMessage = "Please check here";

    int popupType = -1;


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
        etInputLayout = (TextInputEditText) findViewById(R.id.etInputLayout);
        ivFieldStatus = (ImageView) findViewById(R.id.ivFieldStatus);
        ivFieldStatus.setVisibility(INVISIBLE);
        ivLeftImage = (ImageView) findViewById(R.id.ivFieldLeft);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProfileField);

            String hint = a.getString(R.styleable.ProfileField_tvHint);
            inputLayout.setHint(hint);

            final int inputType = a.getInt(R.styleable.ProfileField_inputType, 1); // default value is 1, TEXT
            switch (inputType) {
                case 0:
                    // name
                    etInputLayout.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    break;
                case 1:
                    // text
                    etInputLayout.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    break;
                case 2:
                    // number
                    etInputLayout.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
                case 3:
                    // date
                    etInputLayout.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                    break;
                default:
                    etInputLayout.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }

            boolean enabled = a.getBoolean(R.styleable.ProfileField_isCompleted, true);
            setEnabledEditing(enabled);

            errorMessage = a.getString(R.styleable.ProfileField_errorMessage);

            //@DrawableRes int drawableRes = a.getInt(R.styleable.ProfileField_ivLeftImageRes,R.drawable.ic_person_black_24dp);
            Drawable drawable = a.getDrawable(R.styleable.ProfileField_ivLeftImageRes);
            ivLeftImage.setImageDrawable(drawable);

            etInputLayout.setOnKeyListener(onNextListener);
            etInputLayout.addTextChangedListener(textValidationWatcher);

        }
    }

    private TextWatcher textValidationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateInput();
        }
    };

    private OnKeyListener onNextListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NAVIGATE_NEXT)) {
                validateInput();
                return true;
            }
            return false;
        }
    };

    public boolean validateInput() {
        if (etInputLayout.getText().toString().length() > 3) {
            showCompletedStatus(true);
            return true;
        } else {
            showCompletedStatus(false);
            showErrorMessage(true, errorMessage);
            return false;
        }
    }

    public void showCompletedStatus(boolean completed) {
        if (completed)
            ivFieldStatus.setVisibility(VISIBLE);
        else
            ivFieldStatus.setVisibility(INVISIBLE);


    }

    public void showErrorMessage(boolean show, String text) {
        if (show) {
            inputLayout.setErrorEnabled(true);
            etInputLayout.setError(text);
        } else {
            inputLayout.setErrorEnabled(false);
        }
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        etInputLayout.addTextChangedListener(textWatcher);
    }

    public void setOnFocusChangedListener(OnFocusChangeListener onFocusChangedListener) {
        etInputLayout.setOnFocusChangeListener(onFocusChangedListener);
    }

    public void setEnabledEditing(boolean enabled) {
        etInputLayout.setFocusable(enabled);
        etInputLayout.setEnabled(enabled);
        etInputLayout.setFocusableInTouchMode(enabled);
        if (!enabled) {
            etInputLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public String getInput() {
        return etInputLayout.getText().toString();
    }

    public EditText getEditText() {
        return etInputLayout;
    }

    public void setInputAndLock(String input) {
        setInput(input);
        setEnabledEditing(false);
        if (shouldIntercept) {
            setShouldIntercept(false, popupType);
        }
    }

    public void setInput(String input) {
        etInputLayout.setText(input);
    }

    public void setFocusChangedListener(@NonNull OnFocusChangeListener onFocusChangeListener) {
        etInputLayout.setOnFocusChangeListener(onFocusChangeListener);
    }

    public void showCalendar() {

        Calendar newCalendar = Calendar.getInstance();

        int year = newCalendar.get(Calendar.YEAR) - 3; // 3 years earlier than current year
        int month = 0; // Jan
        int dayOfMonth = 1; // 1st

        //Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        dateOfBirth = new DatePickerDialog(getContext(), R.style.MyDatePicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etInputLayout.setText(dateFormatter.format(newDate.getTime()));
            }
            //initial year month day to be displayed
        }, year, month, dayOfMonth);

        dateOfBirth.setTitle(getContext().getString(R.string.date_of_birth));
        dateOfBirth.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());

        dateOfBirth.show();
    }

    public boolean isShouldIntercept() {
        return shouldIntercept;
    }

    public void setShouldIntercept(boolean shouldIntercept, int popupType) {
        this.shouldIntercept = shouldIntercept;
        this.popupType = popupType;

        if (shouldIntercept) {
            if (popupType == POPUP_TYPE_CALENDAR) {
                etInputLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus && etInputLayout.getText().toString().length() <= 0) {
                            showCalendar();
                        }
                    }
                });
            }

            if (popupType == POPUP_TYPE_LIST_DIALOG) {
                etInputLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus && etInputLayout.getText().toString().length() <= 0) {
                            showDocTypesDialog();
                        }
                    }
                });
            }

        } else {
            etInputLayout.setOnFocusChangeListener(null);
        }
    }

    private void showDocTypesDialog() {

        final String[] items = getContext().getResources().getStringArray(R.array.document_types);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Document Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                etInputLayout.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_DOWN && shouldIntercept)
            return true;

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && shouldIntercept) {
            if (popupType == POPUP_TYPE_CALENDAR) {
                showCalendar();
                return true;
            }
            if (popupType == POPUP_TYPE_LIST_DIALOG) {
                showDocTypesDialog();
                return true;
            }
        }
        return false;
    }
}
