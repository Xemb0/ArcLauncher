package com.launcher.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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
    List<String> packageNames;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String pakagemanger;
    RecyclerView ReyclerDock;
    private BroadcastReceiver addOrRemoveReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
        DockAdded();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dock_icon_selctor);
        IntentFilter filter = new IntentFilter("com.launcher.myapplication.APP_CHANGE");
        registerReceiver(addOrRemoveReceiver, filter);

        Doclizer();
        DockAdded();
    }

    private void docklist(){
        recyclerDock = findViewById(R.id.recycalDock);
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = pm.queryIntentActivities(mainIntent, 0);
        appList = new ArrayList<>();

        appList.addAll(availableActivities);


    }

    private void DockAdded() {
        ReyclerDock = findViewById(R.id.DockGrid);
        packageNames = new ArrayList<>();
        String packageName = getPackageNameFromSharedPreferences();
        packageNames.add(packageName);

            DockAdderAdapter dockAdderAdapter = new DockAdderAdapter(this,packageNames);
        StaggeredGridLayoutManager dockmanger = new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);

        // Set the adapter to the GridView
        ReyclerDock.setLayoutManager(dockmanger);
        ReyclerDock.setAdapter(dockAdderAdapter);

        // Add the package name app to the grid
        // Here, you can use the package name to perform any specific operations, such as loading the app icon, label, etc.
        // Add the app to the grid based on your implementation and requirements
    }

    private String getPackageNameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String packageName = sharedPreferences.getString("AppName", ""); // Empty string is the default value if the key is not found
        return packageName;
    }



    private void Doclizer(){
        recyclerDock = findViewById(R.id.recycalDock);
        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));


        dockAdapter = new DockAdapter(this, apps );
        StaggeredGridLayoutManager dockmanger = new StaggeredGridLayoutManager(6, LinearLayoutManager.VERTICAL);
        recyclerDock.setLayoutManager(dockmanger);
        recyclerDock.setAdapter(dockAdapter);
        recyclerDock.hasFixedSize();
        recyclerDock.setItemViewCacheSize(100);
    }
}
