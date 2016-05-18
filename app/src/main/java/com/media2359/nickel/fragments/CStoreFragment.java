package com.media2359.nickel.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.CaptureActivity;
import com.media2359.nickel.utils.BarcodeUtils;
import com.media2359.nickel.utils.Const;

/**
 * Created by Xijun on 5/4/16.
 */
public class CStoreFragment extends BaseFragment {


    private ImageView ivBarcode;
    private TextView tvInstruction;
    private Button btnGuide, btnPhoto;
    private LinearLayout btnMap;

    public static CStoreFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CStoreFragment fragment = new CStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_store, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        // TODO change the content
        Bitmap barcodeImage = BarcodeUtils.encodeAsBitmapWithDisplayWidth(getActivity(), "www.google.com");
        ivBarcode = (ImageView) view.findViewById(R.id.ivBarcode);
        ivBarcode.setImageBitmap(barcodeImage);

        btnGuide = (Button) view.findViewById(R.id.btnStepGuide);
        btnMap = (LinearLayout) view.findViewById(R.id.btnCStoreMap);
        btnPhoto = (Button) view.findViewById(R.id.btnTakeReceipt);

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGuide();
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.startCapturingReceipt(getActivity(), Const.REQUEST_CODE_RECEIPT_PHOTO);
            }
        });
    }

    private void openMap() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.WEB_LINK_CSTORE_MAP));
        startActivity(i);
    }

    private void openGuide() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.WEB_LINK_GUIDE));
        startActivity(i);
    }

    @Override
    protected String getPageTitle() {
        return "Convenience Store";
    }
}
