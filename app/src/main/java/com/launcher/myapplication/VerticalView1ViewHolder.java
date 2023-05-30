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
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.util.Collections;
import java.util.List;

public class VerticalView1ViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private static final int DEFAULT_ICON_SPAN = 5;

    private int previousProgress = -1;
    private Adapter appAdapter;
    RecyclerView recyclerView;
    ArcSeekBar seekArc;
    Vibrator vibrator;

    public VerticalView1ViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;

        // Initialize the views and variables for Vertical View 2
        recyclerView = itemView.findViewById(R.id.CircularDrawerPager);
        seekArc = itemView.findViewById(R.id.seekArcPager);
        initializeAppDrawer();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            seekArc.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                }
            });
        }

        // Disable RecyclerView's touch interception for seekArc
        seekArc.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                recyclerView.requestDisallowInterceptTouchEvent(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                recyclerView.requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
    }


    private void initializeAppDrawer() {
        PackageManager pm = context.getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
        appAdapter = new Adapter(context, apps, pm);
        CircleLayoutManager circleLayoutManager = new CircleLayoutManager(context);
        recyclerView.setLayoutManager(circleLayoutManager);
        recyclerView.setAdapter(appAdapter);
        recyclerView.setItemViewCacheSize(100);

        // Register broadcast receiver for package install/uninstall events
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        context.registerReceiver(installUninstallBroadcastReceiver, intentFilter);
        int AppCount = appAdapter.getItemCount();
        seekArc.setMaxProgress(AppCount - 1);
        seekArc.setOnProgressChangedListener(progress -> {
            if (progress != previousProgress) {
                vibrate();
                previousProgress = progress;
            }
            circleLayoutManager.scrollToPosition(progress);
        });
        seekArc.setOnStopTrackingTouch(i -> {
        });



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



    // Other methods and implementations



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

    // Rest of the class implementation...
}
