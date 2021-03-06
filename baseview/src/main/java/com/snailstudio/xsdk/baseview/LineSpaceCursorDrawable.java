/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 * <p>
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.xsdk.baseview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.text.Editable;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.snailstudio.xsdk.utils.DisplayUtils;

/**
 * Created by xuqiqiang on 2016/05/17.
 */
public class LineSpaceCursorDrawable extends ShapeDrawable {

    private Context context;
    private EditText view;

    public LineSpaceCursorDrawable(Context context, EditText view) {
        this.context = context;
        setDither(false);
        Resources res = view.getResources();
        getPaint().setColor(res.getColor(ThemeManager.getInstance().background_color));//R.color.note_edittext_cursor_color));
        setIntrinsicWidth((int) DisplayUtils.dip2px(context, 2));//res.getDimensionPixelSize(R.dimen.detail_notes_text_cursor_width));
        //this.mHeight = res.getDimensionPixelSize(R.dimen.detail_notes_text_cursor_height);
        /*FontMetricsInt fontMetrics = view.getPaint().getFontMetricsInt();
        int textHeight = Math.abs(fontMetrics.top - fontMetrics.bottom);
        this.mPaddingBottom = (int) (((-0.724d * ((double) textHeight)) + 60.0d) + 52.852d);
        this.mPaddingTop = (int) ((((double) textHeight) * 0.3448d) - 25.1704d);*/

        this.view = view;
    }

    public void setBounds(int left, int top, int right, int bottom) {
        Editable s = view.getText();
        ImageSpan[] imageSpans = s.getSpans(0, s.length(),
                ImageSpan.class);
        int selectionStart = view.getSelectionStart();
        for (ImageSpan span : imageSpans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            if (selectionStart >= start && selectionStart <= end) {
                super.setBounds(left, top, right, top - 1);
                return;
            }
        }
        super.setBounds(left, top, right,
                top + view.getLineHeight() - (int) ViewUtils.getLineSpacingExtra(context, view));

    }
}
