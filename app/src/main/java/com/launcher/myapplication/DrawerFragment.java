package com.launcher.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.util.Collections;
import java.util.List;

public class DrawerFragment extends Fragment {

    private int previousProgress = -1;
    public  int DEFAULT_ICON_SPAN = 5;
    private Adapter appAdapter;
    RecyclerView recyclerView;
    Vibrator vibrator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer, container, false);
        initializeViews(view);

        initializeAppDrawer();
        return view;
    }
    private void initializeViews(View view) {
        // Find and initialize other views as required
        recyclerView = view.findViewById(R.id.recycalDrawerPager);
    }

    private void initializeAppDrawer() {
        PackageManager pm = requireActivity().getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
        appAdapter = new Adapter(requireContext(), apps, pm);
        GridLayoutManager circleLayoutManager = new GridLayoutManager(requireContext(),5,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(circleLayoutManager);
        recyclerView.setAdapter(appAdapter);
        recyclerView.setItemViewCacheSize(100);
        RecyclerView.OnItemTouchListener itemTouchListener = new RecyclerView.OnItemTouchListener() {
            private float startX = 0f;

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        boolean isScrollingRight = event.getX() < startX;
                        boolean scrollItemsToRight = isScrollingRight && canScrollRight(recyclerView);
                        boolean scrollItemsToLeft = !isScrollingRight && canScrollLeft(recyclerView);
                        boolean disallowIntercept = scrollItemsToRight || scrollItemsToLeft;
                        recyclerView.getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = 0f;
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                // No implementation needed
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // No implementation needed
            }

            private boolean canScrollRight(RecyclerView recyclerView) {
                return recyclerView.canScrollHorizontally(RecyclerView.SCROLL_AXIS_HORIZONTAL);
            }

            private boolean canScrollLeft(RecyclerView recyclerView) {
                return recyclerView.canScrollHorizontally(RecyclerView.SCROLL_AXIS_HORIZONTAL);
            }
        };

        recyclerView.addOnItemTouchListener(itemTouchListener);


        // Register broadcast receiver for package install/uninstall events
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        requireActivity().registerReceiver(installUninstallBroadcastReceiver, intentFilter);



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
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("iconHorizontal", DEFAULT_ICON_SPAN);
    }

    // Other methods and implementations

    @Override
    public void onDestroy() {
        requireActivity().unregisterReceiver(installUninstallBroadcastReceiver);
        super.onDestroy();
    }

    void vibrate() {

        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
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


}
