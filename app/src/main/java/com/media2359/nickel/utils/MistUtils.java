package com.media2359.nickel.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Xijun on 20/5/16.
 */
public class MistUtils {

    public static String getFormattedString(String input) {
        Long longval;
        if (input.contains(",")) {
            input = input.replaceAll(",", "");
        }
        longval = Long.parseLong(input);
        return getFormattedString(longval);
    }

    public static String getFormattedString(Long input) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedString = formatter.format(input);
        return formattedString;
    }

    public static String getFormattedString(double input) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        formatter.setDecimalSeparatorAlwaysShown(true);
        String formattedString = formatter.format(input);
        return formattedString;
    }

    public static String getTodayStringInFormat(){
        return getDateStringInFormat(new Date().getTime());
    }

    public static String getDateStringInFormat(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a", Locale.getDefault());
        return sdf.format(date);
    }
}
