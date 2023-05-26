package com.launcher.myapplication;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PreCachingGridLayoutManager extends GridLayoutManager {
    private int extraLayoutSpace = -1;
    private Context context;

    public PreCachingGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        this.context = context;
    }

    public PreCachingGridLayoutManager(Context context, int spanCount, int extraLayoutSpace) {
        super(context, spanCount);
        this.context = context;
        this.extraLayoutSpace = extraLayoutSpace;
    }

    public PreCachingGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        this.context = context;
    }

    public void setExtraLayoutSpace(int extraLayoutSpace) {
        this.extraLayoutSpace = extraLayoutSpace;
    }

    @Override
    public void setInitialPrefetchItemCount(int itemCount) {
        super.setInitialPrefetchItemCount(itemCount + getExtraLayoutSpace());
    }

    private int getExtraLayoutSpace() {
        if (extraLayoutSpace > 0) {
            return extraLayoutSpace;
        } else {
            return 6;
        }
    }
}
