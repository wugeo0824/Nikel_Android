package com.media2359.nickel.ui.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Transaction;
import com.media2359.nickel.ui.MainActivity;

import org.parceler.Parcels;

/**
 * Created by Xijun on 28/3/16.
 */
public class PaymentConfirmationDialog extends DialogFragment {

    private static final String TAG = "PaymentConfirmation";

    public static final String BUNDLE_TRANSACTION = "transaction";
    private String sendTo;
    private float sendAmount;
    private float exchangeRate;

    // Use this instance of the interface to deliver action events
    ConfirmationDialogListener mListener;
    Transaction transaction;

    public static PaymentConfirmationDialog newInstance(Transaction transaction) {

        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_TRANSACTION, Parcels.wrap(transaction));
        PaymentConfirmationDialog fragment = new PaymentConfirmationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            transaction = Parcels.unwrap(bundle.getParcelable(BUNDLE_TRANSACTION));
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
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }



}


