package com.media2359.nickel.ui.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.media2359.nickel.R;
import com.media2359.nickel.ui.MainActivity;

/**
 * Created by Xijun on 28/3/16.
 */
public class PaymentConfirmationDialog extends DialogFragment {

    private static final String TAG = "PaymentConfirmation";

    public static final String BUNDLE_SEND_TO = "send_to";
    public static final String BUNDLE_SEND_AMOUNT = "send_amount";
    public static final String BUNDLE_EXCHANGE_RATE = "exchange_rate";
    private String sendTo;
    private float sendAmount;
    private float exchangeRate;

    // Use this instance of the interface to deliver action events
    ConfirmationDialogListener mListener;

    public static PaymentConfirmationDialog newInstance(String sendTo, float sendAmount, float exchangeRate) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_SEND_TO,sendTo);
        args.putFloat(BUNDLE_SEND_AMOUNT,sendAmount);
        args.putFloat(BUNDLE_EXCHANGE_RATE,exchangeRate);
        PaymentConfirmationDialog fragment = new PaymentConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            sendTo = bundle.getString(BUNDLE_SEND_TO);
            sendAmount = bundle.getFloat(BUNDLE_SEND_AMOUNT);
            exchangeRate = bundle.getFloat(BUNDLE_EXCHANGE_RATE);
        }else{
            Log.d(TAG,"bundle info is null");
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.dialog_signin, null));

        // Change the string
        builder.setMessage("You are sending xx to xx, current exchange rate is")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User confirmed the transaction
                        mListener.onDialogPositiveClick(PaymentConfirmationDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.onDialogNegativeClick(PaymentConfirmationDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MainActivity) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ConfirmationDialogListener");
        }
    }

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ConfirmationDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }



}


