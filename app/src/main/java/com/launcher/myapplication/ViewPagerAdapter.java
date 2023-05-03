package com.launcher.nova;


import android.content.Context;

import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.launcher.myapplication.Adapter;
import com.launcher.myapplication.PagerObject;
import com.launcher.myapplication.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<PagerObject> pagerAppList;
    int cellHeight, numColumn;
    PackageManager pm=null;
    ArrayList<Adapter> appAdapterList = new ArrayList<>();

    public ViewPagerAdapter(Context context, ArrayList<PagerObject> pagerAppList,  PackageManager pm,int cellHeight, int numColumn){
        this.context = context;
        this.pagerAppList = pagerAppList;
        this.cellHeight = cellHeight;
        this.numColumn = numColumn;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.pager_layout, container, false);

        final GridView mGridView = layout.findViewById(R.id.grid);
        mGridView.setNumColumns(numColumn);

        Adapter mGridAdapter = new Adapter(context, pagerAppList.get(position).getAppList(), pm);
        mGridView.setAdapter((ListAdapter) mGridAdapter);

        appAdapterList.add(mGridAdapter);

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pagerAppList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void notifyGridChanged(){
        for(int i = 0; i < appAdapterList.size();i++){
            appAdapterList.get(i).notifyDataSetChanged();
        }
    }
}
