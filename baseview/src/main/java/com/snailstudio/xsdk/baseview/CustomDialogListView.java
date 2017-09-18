/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 * <p>
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.xsdk.baseview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.snailstudio.xsdk.utils.DisplayUtils;

/**
 * Created by xuqiqiang on 2016/05/17.
 */
public class CustomDialogListView extends ListView {
    private static final int MAX_WIDTH_PX = 300;
    private static final int MAX_HEIGHT_PX = 400;
    private Context context;

    public CustomDialogListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                (int) DisplayUtils.dip2px(context, MAX_HEIGHT_PX),
                MeasureSpec.AT_MOST));

    }
}