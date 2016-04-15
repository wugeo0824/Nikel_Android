package com.media2359.nickel.ui.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.media2359.nickel.R;
import com.media2359.nickel.model.Transaction;

/**
 * Created by Xijun on 4/4/16.
 */
public class TransactionProgress extends RelativeLayout {

    private ImageView ivBall1, ivBall2, ivBall3;
    private ProgressBar pb1, pb2;
    private static final int PROGRESS_25 = 25;
    private static final int PROGRESS_50 = 50;
    private static final int PROGRESS_75 = 75;
    private static final int PROGRESS_100 = 100;

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
        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb2 = (ProgressBar) findViewById(R.id.progressBar2);
    }

    public void updateProgress(int progress) {
        switch (progress) {
            case Transaction.TRANS_PAYMENT_MADE:
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
                //pb.setProgress(25);
                animateProgress(PROGRESS_25);
                break;
            case Transaction.TRANS_UPLOAD_COMPLETE:
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_green);
                ivBall3.setImageResource(R.drawable.circle_grey);
                //pb.setProgress(75);
                animateProgress(75);
                break;
            case Transaction.TRANS_READY_COLLECTION:
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_green);
                ivBall3.setImageResource(R.drawable.circle_green);
                //pb.setProgress(100);
                animateProgress(100);
                break;
            case Transaction.TRANS_NEW_BORN:
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
                //pb.setProgress(25);
                animateProgress(PROGRESS_25);
                break;
            default:
                ivBall1.setImageResource(R.drawable.circle_grey);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
                //animateProgress(25);
                break;
        }
    }

    // PROGRESS BAR MAX is 50 each

    private void animateProgress(final int progress) {

        switch (progress){
            case PROGRESS_25:
                animate25();
                break;
            case PROGRESS_75:
                animate75();
                break;
            case PROGRESS_100:
                animate100();
                break;
            default:
                animate0();
                break;
        }
    }

    private void animate0(){
        pb1.setProgress(0);
        pb2.setProgress(0);
        ivBall1.setImageResource(R.drawable.circle_grey);
        ivBall2.setImageResource(R.drawable.circle_grey);
        ivBall3.setImageResource(R.drawable.circle_grey);
    }

    private void animate25(){
        pb1.setProgress(0);
        pb2.setProgress(0);
        ObjectAnimator animation = ObjectAnimator.ofInt(pb1, "progress", 0, 25);
        animation.setDuration(500); // Duration of the animation
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(25);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(25);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }

    private void animate75(){
        pb1.setProgress(0);
        pb2.setProgress(0);
        ObjectAnimator animation1 = ObjectAnimator.ofInt(pb1, "progress", 0, 50);
        animation1.setDuration(500); // Duration of the animation
        animation1.setInterpolator(new LinearInterpolator());
        animation1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(50);
                ivBall2.setImageResource(R.drawable.circle_green);
                animateBar2(25);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(50);
                ivBall2.setImageResource(R.drawable.circle_green);
                animateBar2(25);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation1.start();

    }

    private void animate100(){
        pb1.setProgress(0);
        ObjectAnimator animation1 = ObjectAnimator.ofInt(pb1, "progress", 0, 50);
        animation1.setDuration(500); // Duration of the animation
        animation1.setInterpolator(new LinearInterpolator());


        final ObjectAnimator animation2 = ObjectAnimator.ofInt(pb2, "progress", 0, 50);
        animation2.setDuration(500); // Duration of the animation
        animation2.setInterpolator(new DecelerateInterpolator());


        animation1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivBall1.setImageResource(R.drawable.circle_green);
                ivBall2.setImageResource(R.drawable.circle_grey);
                ivBall3.setImageResource(R.drawable.circle_grey);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(50);
                ivBall2.setImageResource(R.drawable.circle_green);
                animation2.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(50);
                ivBall2.setImageResource(R.drawable.circle_green);
                animation2.start();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animation2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pb2.setProgress(50);
                ivBall3.setImageResource(R.drawable.circle_green);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                pb2.setProgress(50);
                ivBall3.setImageResource(R.drawable.circle_green);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation1.start();
    }

    private void animateBar1(final int progress){
        pb1.setProgress(0);
        ObjectAnimator animation = ObjectAnimator.ofInt(pb1, "progress", 0, progress);
        animation.setDuration(500); // Duration of the animation
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(progress);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //updateProgress(progress);
                pb1.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }

    private void animateBar2(final int progress){
        pb2.setProgress(0);
        ObjectAnimator animation = ObjectAnimator.ofInt(pb2, "progress", 0, progress);
        animation.setDuration(500); // Duration of the animation
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //updateProgress(progress);
                pb2.setProgress(progress);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //updateProgress(progress);
                pb2.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animation.start();
    }
}
