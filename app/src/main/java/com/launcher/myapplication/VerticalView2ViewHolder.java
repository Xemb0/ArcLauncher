package com.launcher.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class VerticalView2ViewHolder extends RecyclerView.ViewHolder {
    // Define the views and variables for Vertical View 2
    private Context context;
    private Adapter appAdapter;
    private Vibrator vibrator;

    public VerticalView2ViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;

        // Initialize the views and variables for Vertical View 2
        RecyclerView recyclerView = itemView.findViewById(R.id.recycalDrawerPager);
        initializeAppDrawer(recyclerView);

        }

    private void initializeAppDrawer(RecyclerView recyclerView) {
        PackageManager pm = context.getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
        appAdapter = new Adapter(context, apps, pm);
        GridLayoutManager circleLayoutManager = new GridLayoutManager(context, 7, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(circleLayoutManager);
        recyclerView.setAdapter(appAdapter);
        recyclerView.setItemViewCacheSize(100);

        // Register broadcast receiver for package install/uninstall events
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        context.registerReceiver(installUninstallBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver installUninstallBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_PACKAGE_ADDED.equals(action) || Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                appAdapter.refreshAppList();
            }
        }
    };

    private int getIconSpanFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("iconHorizontal", 5);
    }

    void vibrate() {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For devices with API level Q and above
                vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
            } else {
                // For devices with API level Oreo (API 26) to Pie (API 28)
                vibrationEffect = VibrationEffect.createOneShot(1, VibrationEffect.DEFAULT_AMPLITUDE);
            }
            vibrator.vibrate(vibrationEffect);
        } else {
            // For devices with API level 21 to 25
            vibrator.vibrate(50);
        }
    }
    // Add other methods and logic specific to Vertical View 2
}
