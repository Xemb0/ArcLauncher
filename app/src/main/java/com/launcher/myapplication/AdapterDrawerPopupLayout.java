package com.launcher.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterDrawerPopupLayout extends BaseAdapter {

    private Context context;
    private int itemCount;

    public AdapterDrawerPopupLayout(Context context, int itemCount) {
        this.context = context;
        this.itemCount = itemCount;
    }

    @Override
    public int getCount() {
        return itemCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.drawer_grid_items, parent, false);
        }

        // Customize the grid item view as needed

        return convertView;
    }

}
