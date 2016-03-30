package com.media2359.nickel.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.media2359.nickel.R;
import com.media2359.nickel.utils.BarcodeUtils;

/**
 * Created by Xijun on 10/3/16.
 */
public class ConfirmationFragment extends BaseFragment {

    public static final String BUNDLE_BARCODE_CONTENT = "barcode_content";
    private String barcodeContent = "";
    private ImageView ivBarcode;

    public static ConfirmationFragment newInstance(String barcodeContent) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_BARCODE_CONTENT, barcodeContent);
        ConfirmationFragment fragment = new ConfirmationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        initData();
        initViews(view);
        return view;
    }

    private void initData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            barcodeContent = bundle.getString(BUNDLE_BARCODE_CONTENT);
        }
    }

    private void initViews(View view) {

        // TODO change the content
        Bitmap barcodeImage = BarcodeUtils.encodeAsBitmapWithDisplayWidth(getActivity(), barcodeContent);
        ivBarcode = (ImageView) view.findViewById(R.id.ivBarcode);
        ivBarcode.setImageBitmap(barcodeImage);
    }

    @Override
    protected String getPageTitle() {
        return "Confirmation";
    }
}
