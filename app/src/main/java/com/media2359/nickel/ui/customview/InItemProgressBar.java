package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.utils.Const;

/**
 * Created by Xijun on 13/5/16.
 */
public class InItemProgressBar extends FrameLayout{

    ProgressBar progressBar;
    TextView tvStatus;

    public InItemProgressBar(Context context) {
        super(context);
        init();
    }

    public InItemProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InItemProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_progress_in_item, this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvStatus = (TextView) findViewById(R.id.tvStatus);

        setProgressOne();
    }

    public void setProgressOne(){
        progressBar.setProgress(Const.IN_ITEM_PROGRESS_ONE);
        tvStatus.setText(getContext().getString(R.string.in_progress));
    }

    public void setProgressTwo(){
        progressBar.setProgress(Const.IN_ITEM_PROGRESS_TWO);
        tvStatus.setText(getContext().getString(R.string.in_progress));
    }

    public void setProgressCompleted(){
        progressBar.setProgress(Const.IN_ITEM_MAX_PROGRESS);
        tvStatus.setText(getContext().getString(R.string.completed));
    }

    public void updateProgress(int progress) {
        switch (progress){
            case Const.IN_ITEM_PROGRESS_ONE:
                setProgressOne();
                break;
            case Const.IN_ITEM_PROGRESS_TWO:
                setProgressTwo();
                break;
            case Const.IN_ITEM_MAX_PROGRESS:
                setProgressCompleted();
                break;
            default:
                setProgressOne();
                break;
        }
    }
}
