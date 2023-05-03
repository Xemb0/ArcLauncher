package com.launcher.myapplication;


import android.content.pm.ResolveInfo;

import com.launcher.myapplication.AppObject;

import java.util.ArrayList;

public class PagerObject {
    private final ArrayList<ResolveInfo> appList;

    public PagerObject(ArrayList<ResolveInfo> appList){
        this.appList = appList;
    }

    public ArrayList<ResolveInfo> getAppList() {
        return appList;
    }
}
