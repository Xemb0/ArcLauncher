package com.launcher.myapplication;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

public class StackTransformer implements ViewPager2.PageTransformer {
    private static final float SWIPE_UP_FACTOR = 0.3f;

    @Override
    public void transformPage(View view, float position) {
        view.setAlpha(0f);

        if (position >= -1 && position <= 1) {
            view.setAlpha(1f);
            view.setTranslationY(0f);
            ViewCompat.setTranslationZ(view, 0f);
        } else {
            view.setAlpha(0f);
        }

        if (position <= 0) {
            view.setAlpha(1 - Math.abs(position) * 4);
            view.setTranslationY(view.getHeight() * -position);
            ViewCompat.setTranslationZ(view, -1f);
        }

        if (position < 0) {
            view.setTranslationY(view.getTranslationY() + position * view.getHeight() * SWIPE_UP_FACTOR);
        }
    }
}
