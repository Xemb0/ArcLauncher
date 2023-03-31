package com.launcher.myapplication;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScrollListener extends RecyclerView.OnScrollListener {
    private List<RecyclerView> recyclerViews;
    private int totalScroll;

    public ScrollListener(List<RecyclerView> recyclerViews) {
        this.recyclerViews = recyclerViews;
        this.totalScroll = 0;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // Calculate the total scroll distance
        totalScroll += dy;

        // Update the scroll position of each RecyclerView
        for (RecyclerView rv : recyclerViews) {
            rv.scrollBy(dx, dy);
        }
    }

    public int getTotalScroll() {
        return totalScroll;
    }
}

