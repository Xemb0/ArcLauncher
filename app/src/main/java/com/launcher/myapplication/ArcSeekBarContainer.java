package com.launcher.myapplication;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.marcinmoskala.arcseekbar.ArcSeekBar;

public class ArcSeekBarContainer extends LinearLayout {

    private ArcSeekBar mArcSeekBar;

    public ArcSeekBarContainer(Context context) {
        super(context);
        init();
    }

    public ArcSeekBarContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcSeekBarContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mArcSeekBar != null) {
            Rect rect = new Rect();
            mArcSeekBar.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mArcSeekBar = findViewById(R.id.seekArc);
    }
}
