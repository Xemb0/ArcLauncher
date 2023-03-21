package com.launcher.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawer();
    }
    List<AppObject> installedAppList = new ArrayList<>();
    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        final GridView mDrawerGridView = findViewById(R.id.drawerGrid);
       final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(300);

        installedAppList = getInstalledAppList();


        mDrawerGridView.setAdapter(new AppAdapter(getApplicationContext(), installedAppList));
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==BottomSheetBehavior.STATE_HIDDEN && mDrawerGridView.getChildAt(0).getY()!=0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(newState==BottomSheetBehavior.STATE_DRAGGING && mDrawerGridView.getChildAt(0).getY()!=0)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }private List<AppObject> getInstalledAppList() {
        List<AppObject> list = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

        for(ResolveInfo untreatedApp : untreatedAppList){
            String appName = untreatedApp.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = untreatedApp.activityInfo.packageName;
            Drawable appImage = untreatedApp.activityInfo.loadIcon(getPackageManager());

            AppObject app = new AppObject(appPackageName, appName, appImage, true);
            list.add(app); // Add the app to the list
        }
        return list;
    }


}