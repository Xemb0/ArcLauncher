package com.launcher.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Collections;
import java.util.List;

public class Drawer extends AppCompatActivity {

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        initializeDrawer();

        BottomSheetBehaviour();


        gestureDetector = new GestureDetector(this, new Gesture(this));


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass the touch event to the GestureDetector to handle
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    private  void initializeDrawer(){




    View mBottomSheet = findViewById(R.id.DrawerSheet);
    RecyclerView recyclerDrawer = findViewById(R.id.recycalDrawer);
        recyclerDrawer.setLayoutManager(new GridLayoutManager(this, 6, RecyclerView.HORIZONTAL, false));

        //        mDrawerGridView.setLayoutManager(mGridLayoutManager);
    PackageManager pm = this.getPackageManager();
    Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
    List<ResolveInfo> installedAppList = pm.queryIntentActivities(main,0);
        Collections.sort(installedAppList,
            new ResolveInfo.DisplayNameComparator(pm));
    Adapter adapter = new Adapter(this, installedAppList, pm);
        recyclerDrawer.setAdapter(adapter);
    }





    void BottomSheetBehaviour(){
        View Drawer = findViewById(R.id.Drawer);
        View bottomSheet = findViewById(R.id.DrawerSheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {


            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset == 0) {
                    // Bottom sheet is fully expanded and user started to swipe down
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    overridePendingTransition(0, R.anim.slide_down);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else if (slideOffset == 1) {
                    // Bottom sheet is fully collapsed and user started to swipe up
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    overridePendingTransition(R.anim.slide_up, 0);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                }
            }


        });


    }

}
