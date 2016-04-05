package com.media2359.nickel.ui.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.media2359.nickel.R;
import com.media2359.nickel.utils.Const;

/**
 * Created by Xijun on 4/4/16.
 */
public class TransactionProgress extends RelativeLayout {

    private ImageView ivBall1, ivBall2, ivBall3;
    private ProgressBar pb;

    public TransactionProgress(Context context) {
        super(context);
        init(context);
    }

    public TransactionProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TransactionProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TransactionProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.progress_bar_transaction, this);
        ivBall1 = (ImageView) findViewById(R.id.ivProgress1);
        ivBall2 = (ImageView) findViewById(R.id.ivProgress2);
        ivBall3 = (ImageView) findViewById(R.id.ivProgress3);
        pb = (ProgressBar) findViewById(R.id.progressBarTran);
    }

    public void updateProgress(int progress) {
        switch (progress) {
            case Const.TRANS_PENDING_PAYMENT:
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
                pb.setProgress(0);
                break;
            case Const.TRANS_UPLOAD_COMPLETE:
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_green);
                ivBall3.setImageResource(R.drawable.circle_grey);
                pb.setProgress(1);
                break;
            case Const.TRANS_READY_COLLECTION:
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_green);
                ivBall3.setImageResource(R.drawable.circle_green);
                pb.setProgress(2);
                break;
            case Const.TRANS_NEW_BORN:
                ivBall1.setImageResource(R.drawable.circle_grey);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
                pb.setProgress(0);
                break;
            default:
                ivBall1.setImageResource(R.drawable.circle_grey);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
                pb.setProgress(0);
                break;
        }
    }

    public void animateProgress(final int progress) {
        pb.setProgress(0);
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", 0, progress);
        animation.setDuration(1000); // Duration of the animation
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateProgress(progress);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                updateProgress(progress);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }
}
