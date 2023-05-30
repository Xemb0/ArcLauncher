package com.launcher.myapplication;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class PaddingPageTransformer implements ViewPager2.PageTransformer {
    private int paddingBottom;

    public PaddingPageTransformer(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageHeight = page.getHeight();
        float pageOffset = position * -pageHeight;

        if (position < 0) {
            page.setTranslationY(pageOffset);
            page.setPadding(0, 0, 0, paddingBottom);
        } else {
            page.setTranslationY(pageOffset);
            page.setPadding(0, 0, 0, 0);
        }
    }
}
