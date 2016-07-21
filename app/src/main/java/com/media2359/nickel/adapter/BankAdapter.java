package com.media2359.nickel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.media2359.nickel.model.Bank;

import java.util.List;

/**
 * Created by Xijun on 21/7/16.
 */
public class BankAdapter extends ArrayAdapter<Bank> implements Filterable {

    private List<Bank> bankList;
    private int resource;

    public BankAdapter(Context context, int resource, List<Bank> objects) {
        super(context, resource, objects);
        bankList = objects;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return bankList.size();
    }

    @Override
    public Bank getItem(int position) {
        return bankList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView tv;
        if (convertView != null) {
            tv = (TextView) convertView;
        } else {
            tv = (TextView) LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        tv.setText(getItem(position).getName());
        return tv;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        final TextView tv;
        if (convertView != null) {
            tv = (TextView) convertView;
        } else {
            tv = (TextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        tv.setText(getItem(position).getName());
        return tv;
    }
}
