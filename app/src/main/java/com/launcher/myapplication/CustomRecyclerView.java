package com.launcher.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class CustomRecyclerView extends RecyclerView {
    private static final int DEFAULT_BUFFER_SIZE = 60; // Set a default buffer size

    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }



    @NonNull
    @Override
    public RecycledViewPool getRecycledViewPool() {
        RecycledViewPool recycledViewPool = super.getRecycledViewPool();
        int bufferSize = 2;
        recycledViewPool.setMaxRecycledViews(0, bufferSize); // Set the maximum number of views to be recycled for view type 0

        return recycledViewPool;
    }

    private int getBufferSize() {

        RecyclerView recyclerDrawer = findViewById(R.id.recycalview);
        PackageManager pm = getContext().getPackageManager();
//        mDrawerGridView.setLayoutManager(mGridLayoutManager);
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps,
                new ResolveInfo.DisplayNameComparator(pm));
        com.launcher.myapplication.Adapter adapter = new com.launcher.myapplication.Adapter(getContext(), apps, pm);
        recyclerDrawer.setAdapter(adapter);
        recyclerDrawer.setAdapter(adapter);
        int itemCount = adapter.getItemCount();
        return itemCount > 0 ? itemCount : DEFAULT_BUFFER_SIZE;
    }
}
