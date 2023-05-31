package com.launcher.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DockAdder extends AppCompatActivity {
    Adapter appAdapter;
    RecyclerView recyclerDock;

    private  DockAdapter dockAdapter;

    List<ResolveInfo> appList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.dock_icon_selctor);

        Doclizer();
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//
//        List<ResolveInfo> selectedApps = new ArrayList<>();
//        for (ResolveInfo resolveInfo : appList) {
//            String packageName = resolveInfo.activityInfo.packageName;
//            boolean isSelected = sharedPreferences.getBoolean(packageName, false);
//
//            if (isSelected) {
//                selectedApps.add(resolveInfo);
//            }
//        }

//        docklist();
//        dockAdapter = new DockAdapter(this,docklist);


    }
    private void docklist(){

        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = pm.queryIntentActivities(mainIntent, 0);
        appList = new ArrayList<>();

        appList.addAll(availableActivities);


    }
    private void Doclizer(){
        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));


        appAdapter = new Adapter(this, apps, pm);
        GridLayoutManager dockmanger = new GridLayoutManager(this,6, RecyclerView.VERTICAL,false);
        recyclerDock.setLayoutManager(dockmanger);
        recyclerDock.setAdapter(appAdapter);
        recyclerDock.hasFixedSize();
        recyclerDock.setItemViewCacheSize(100);
    }
}
