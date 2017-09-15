/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 * <p>
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.library.baseview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout.LayoutParams;

import com.snailstudio.library.utils.ScreenInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by xuqiqiang on 2016/05/17.
 */
public class LargeImageView extends AppCompatImageView {

    public static final String TAG = LargeImageView.class.getName();
    static final int mDrawableBitmapHeight = 1000;
    private final Rect mRect = new Rect();
    Bitmap[] mBitmaps;
    Context cxt;
    int screenWidth;
    int screenHeight;
    float mBitmapWidth;
    float mBitmapHeight;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            Log.d(TAG, "get notify...");
            postInvalidate();
        }
    };
    private BitmapRegionDecoder mDecoder;
    private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private float minScale = 1;

    public LargeImageView(Context context) {
        this(context, null);
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        cxt = context;
        if (context instanceof Activity) {
            screenWidth = ScreenInfo.getInstance().getWidthPixels();
            screenHeight = ScreenInfo.getInstance().getWidthPixels();
        }
    }

    public void setMinScale1(float s) {
        this.minScale = s;
    }

    /**
     * 每次绘制图片的长度高度
     */
    public void setWH(int reqW, int reqH) {
        this.screenWidth = reqW;
        this.screenHeight = reqH;
    }

    public void setImageBitmap1(final Bitmap bm) {
        Log.d(TAG, "getPaddingLeft" + this.getPaddingLeft());

        final int absBitmapWidth = bm.getWidth();
        final int absBitmapHeight = bm.getHeight();

        mBitmapWidth = absBitmapWidth;
        mBitmapHeight = absBitmapHeight;

        int width = absBitmapWidth + getPaddingLeft() + getPaddingRight();
        int height = absBitmapHeight + getPaddingTop() + getPaddingBottom();

        if (width > screenWidth) {

            mBitmapWidth = screenWidth - getPaddingLeft() - getPaddingRight();
            mBitmapHeight = mBitmapWidth / (float) absBitmapWidth
                    * (float) absBitmapHeight;
            width = screenWidth;
            height = (int) mBitmapHeight + getPaddingTop() + getPaddingBottom();
            minScale = mBitmapWidth / absBitmapWidth;
        }
        Log.d(TAG, "minScale" + minScale);
        LayoutParams params = new LayoutParams(width, height);
        setLayoutParams(params);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int count = absBitmapHeight / mDrawableBitmapHeight;
                    count = absBitmapHeight % mDrawableBitmapHeight == 0 ? count
                            : count + 1;

                    mBitmaps = new Bitmap[count];

                    if (Build.VERSION.SDK_INT > 1000) {// 因兼容低版本问题，暂不使用此切图方式

                        ByteArrayOutputStream baos = new
                                ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        InputStream is = new ByteArrayInputStream(baos
                                .toByteArray());

                        mDecoder = BitmapRegionDecoder.newInstance(is, true);

                        int sh = bm.getHeight();
                        for (int i = 0; i < count; i++) {
                            mRect.set(0, sh *
                                    i, absBitmapWidth, (i + 1) * sh);
                            mBitmaps[i] =
                                    mDecoder.decodeRegion(mRect, null);
                        }
                    } else {
                        for (int i = 0; i < count; i++) {
                            int height = i == (count - 1) ? absBitmapHeight - i
                                    * mDrawableBitmapHeight
                                    : mDrawableBitmapHeight;
                            mBitmaps[i] = Bitmap.createBitmap(bm, 0,
                                    mDrawableBitmapHeight * i, absBitmapWidth,
                                    height);
                            if (mBitmaps[i] == null) {
                                throw new IllegalArgumentException(
                                        "bitmap is null,pos at " + i);
                            }
                        }
                    }
                    bm.recycle();
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            int top = 0;
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            for (int i = 0; mBitmaps != null && i < mBitmaps.length; i++) {
                canvas.save();
                if (minScale != 1) {
                    canvas.scale(minScale, minScale);
                }
                if (mBitmaps[i] == null) {
                    return;
                }
                canvas.drawBitmap(mBitmaps[i], paddingLeft, paddingTop + top,
                        mPaint);
                canvas.restore();
                top += mBitmaps[i].getHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
