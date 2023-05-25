package com.launcher.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DrawerAdapter extends BaseAdapter {

    Context context;
    String[] item;
    int[] image;

    public DrawerAdapter(Context context, String[] item, int[] image) {
        this.context = context;
        this.item = item;
        this.image = image;
    }

    @Override
    public int getCount() {

        return image.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {



        return null;
    }
}
