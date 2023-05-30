package com.launcher.myapplication;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class PaddingPageTransformer implements ViewPager2.PageTransformer {
    private int paddingStart;

    public PaddingPageTransformer(int paddingStart) {
        this.paddingStart = paddingStart;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageWidth = page.getWidth();
        float pageOffset = position * -pageWidth;

        if (position < 0) {
            page.setTranslationX(pageOffset);
        } else {
            page.setTranslationX(pageOffset + paddingStart);
        }
    }
}

