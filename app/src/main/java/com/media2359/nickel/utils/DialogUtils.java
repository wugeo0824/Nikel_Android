package com.media2359.nickel.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.media2359.nickel.R;

/**
 * Created by Xijun on 31/3/16.
 */
public class DialogUtils {

    public static void showNickelDialog(Activity context, String content){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_nickel_logo, null);
        dialogBuilder.setView(dialogView);
        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvDialogContent);
        tvMessage.setText(content);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        },1000);
    }
}
