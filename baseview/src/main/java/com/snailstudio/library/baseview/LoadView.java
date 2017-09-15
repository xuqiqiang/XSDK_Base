/**
 * Copyright (C) 2016 Snailstudio. All rights reserved.
 * <p>
 * https://xuqiqiang.github.io/
 *
 * @author xuqiqiang (the sole member of Snailstudio)
 */
package com.snailstudio.library.baseview;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xuqiqiang on 2016/05/17.
 */
public abstract class LoadView {

    public static final int SCENE_INITIAL = 0, SCENE_LOADING = 1,
            SCENE_COMPLETE = 2, SCENE_ERROR = 3;
    private static final String TAG = LoadView.class.getSimpleName();
    protected final int MESSAGE_COMPLETE = 1, MESSAGE_ERROR = 2;
    public int scene;
    private Activity context;
    private View view;
    private ImageView loadAnimation;
    private Animation mAnimation;
    private TextView tv_prompt;
    private TextView bt_retry;
    private String load_message;

    public LoadView(Activity context, String message) {
        this.context = context;
        scene = SCENE_INITIAL;

        initView();

        load_message = message;

    }

    public LoadView(Activity context, int messageId) {
        this(context, context.getString(messageId));
    }

    private void initView() {
        view = context.getLayoutInflater().inflate(R.layout.load_layout, null);

        loadAnimation = (ImageView) view.findViewById(R.id.loadAnimation);

        tv_prompt = (TextView) (view.findViewById(R.id.tv_prompt));

        bt_retry = (TextView) (view.findViewById(R.id.bt_retry));

        bt_retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startLoad();
            }
        });

        mAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate); // 旋转动画
        mAnimation.setInterpolator(new LinearInterpolator());
    }

    public View getView() {
        return view;
    }

    public void reset() {
        if (scene != SCENE_INITIAL) {
            scene = SCENE_INITIAL;
            tv_prompt.setText(load_message);
            loadAnimation.clearAnimation(); // 加载完成时调用
        }
    }

    public void startLoad() {

        scene = SCENE_LOADING;

        onLoadEvent();

        tv_prompt.setText(load_message);
        loadAnimation.setBackgroundResource(R.drawable.loading_gif);

        loadAnimation.startAnimation(mAnimation); // 初始化动画

        bt_retry.setVisibility(View.GONE);

    }

    protected abstract void onLoadEvent(); // 重试

    public void onCompleted() {
        Log.d(TAG, "onCompleted");
        loadAnimation.clearAnimation(); // 加载完成时调用

        scene = SCENE_COMPLETE;
    }

    protected void showMessage(String message) {
        TextView tvWait = (TextView) (view.findViewById(R.id.tv_prompt));
        tvWait.setText(message);
        loadAnimation.clearAnimation();
        loadAnimation.setVisibility(View.GONE);
    }

    public void onError(String message) {

        scene = SCENE_ERROR;

        tv_prompt.setText(message);// "无法连接到服务器");

        loadAnimation.clearAnimation();
        loadAnimation.setBackgroundResource(R.drawable.icon_cry);

        bt_retry.setVisibility(View.VISIBLE);

    }

    public void onError(int resId) {
        onError(context.getString(resId));
    }
}