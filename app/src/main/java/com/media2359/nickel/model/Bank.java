package com.media2359.nickel.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Xijun on 21/7/16.
 */
public class Bank {

    @SerializedName("id")
    int id;

    @SerializedName("shortName")
    String shortName;

    @SerializedName("name")
    String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
