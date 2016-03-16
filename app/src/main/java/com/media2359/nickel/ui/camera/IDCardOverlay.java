package com.media2359.nickel.ui.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.media2359.nickel.R;

/**
 * Created by Xijun on 15/3/16.
 */
public class IDCardOverlay extends View {

    private Bitmap bitmap;
    private Canvas canvas;

    public IDCardOverlay(Context context) {
        super(context);
        //init();
    }

    public IDCardOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init();
    }

    public IDCardOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }

    private void init() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            createWindowFrame();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    protected void createWindowFrame() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.black));
        paint.setAlpha(170);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        //paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = getResources().getDimensionPixelSize(R.dimen.radius);
        float height = getResources().getDimensionPixelSize(R.dimen.card_overlay_height);
        float width = getResources().getDimensionPixelSize(R.dimen.card_overlay_width);

        float top = centerY + height / 2;
        float bottom = centerY - height / 2;
        float left = centerX - width / 2;
        float right = centerX + width / 2;

        RectF card = new RectF(left,top,right,bottom);

        //osCanvas.drawRect(card,paint);
        osCanvas.drawRoundRect(card,0,0,paint);
        //osCanvas.drawRoundRect(left,top,right,bottom,6,6,paint);
        //osCanvas.drawCircle(centerX, centerY, radius, paint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
    }
}
