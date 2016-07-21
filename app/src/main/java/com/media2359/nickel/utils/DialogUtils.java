package com.media2359.nickel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.media2359.nickel.R;

/**
 * Created by Xijun on 31/3/16.
 */
public class DialogUtils {

    public static void showNickelDialog(Activity context, String content) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_nickel_logo, null);
        dialogBuilder.setView(dialogView);
        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvDialogContent);
        tvMessage.setText(content);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }

    public static AlertDialog getNickelThemedAlertDialog(final Context context, String title, String message, DialogInterface.OnClickListener onYesClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.PaymentAlterDialog);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setNegativeButton(context.getString(R.string.no), null);
        builder.setPositiveButton(context.getString(R.string.yes), onYesClickListener);
        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.pink));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.text_color_inactive));
            }
        });

        return dialog;
    }

}
