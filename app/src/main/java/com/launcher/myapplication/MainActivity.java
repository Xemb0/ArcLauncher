package com.launcher.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawer();
        // Get the view that represents your launcher app

        gestureDetector = new GestureDetector(this, new GestureListener());
        // Get the current wallpaper
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();

// Set the wallpaper as the background of your launcher app


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private GestureDetector gestureDetector;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            // Open the dialer application
                            Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
                            startActivity(dialerIntent);
                        } else {  Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                            startActivity(intent);

                        }
                        result = true;
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
//                            ExpandNotificationBar();
                            try{
                                @SuppressLint("WrongConstant") Object service = getSystemService("statusbar");
                                Class<?> statusBarManager = Class.forName("android.app.StatusBarManager"); // Fix typo in class name
                                Method expand = statusBarManager.getMethod("expandNotificationsPanel");
                                expand.invoke(service);
                            }
                            catch (Exception e) {
                                String errorMessage = "Notification panel swipe down error: " + e.getMessage();
                                Log.e("StatusBar", errorMessage);
                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            View mBottomSheet = findViewById(R.id.bottomSheet);
                            final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        result = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }



// add this to your onCreate method



    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        RecyclerView mDrawerGridView = findViewById(R.id.recycalview);
        mDrawerGridView.setLayoutManager(new CircleLayoutManager(this));
//        mDrawerGridView.setLayoutManager(mGridLayoutManager);
        PackageManager pm = this.getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedAppList = pm.queryIntentActivities(main,0);
        Collections.sort(installedAppList,
                new ResolveInfo.DisplayNameComparator(pm));
        Adapter adapter = new Adapter(this, installedAppList, pm);
        mDrawerGridView.setAdapter(adapter);
//        mDrawerGridView.setLayoutManager(mGridLayoutManager);

       final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(false);
     float x=  mBottomSheetBehavior.calculateSlideOffset();
        mBottomSheetBehavior.setDraggable(true);
        mBottomSheetBehavior.setPeekHeight(400);




    }


}