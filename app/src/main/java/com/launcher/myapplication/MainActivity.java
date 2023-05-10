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
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

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


        //seekbar

//        mDrawerGridView.setLayoutManager(mGridLayoutManager);
        PackageManager pm = this.getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedAppList = pm.queryIntentActivities(main,0);
        Collections.sort(installedAppList,
                new ResolveInfo.DisplayNameComparator(pm));
        Adapter adapter = new Adapter(this, installedAppList, pm);

//

        ArcSeekBar seekBar = findViewById(R.id.seekArc);
        RecyclerView recyclerView = findViewById(R.id.recycalview);
        recyclerView.setAdapter(adapter);
        int itemCount = adapter.getItemCount();
        CircleLayoutManager layoutManager = new CircleLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        seekBar.setMaxProgress(itemCount-1);


        SeekBar seekBar1 = findViewById(R.id.normalseekbar);
        ArcSeekBar seekArc = findViewById(R.id.seekArc);

        seekBar1.setMax(itemCount);
        TextView letterTextView = findViewById(R.id.firstletter); // assuming you have a TextView with this id in your layout
        letterTextView.setVisibility(View.INVISIBLE);
        seekBar1.setMax(itemCount-1);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    letterTextView.setVisibility(View.VISIBLE);
                    seekArc.setProgress(progress);
                    layoutManager.scrollToPosition(progress);

                    PackageManager pm = getPackageManager();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

                    Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
                    String packageName = apps.get(progress).activityInfo.packageName;
                    String appName = null;
                    try {
                        appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
                    } catch (PackageManager.NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    String alphabet = String.valueOf(appName.charAt(0));

                    letterTextView.setText(alphabet);
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                PackageManager pm = getPackageManager();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

                // Sort the app list by app name
                Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));

                String packageName = apps.get(progress).activityInfo.packageName;

                // Get the first letter of the package name
                String appName = String.valueOf(packageName.charAt(0));
                char appLetter = appName.charAt(0);
                letterTextView.setText(String.valueOf(appLetter));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PackageManager pm = getPackageManager();
                letterTextView.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

                // Sort the app list by app name
                Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));

                String packageName = apps.get(seekBar.getProgress()).activityInfo.packageName;

                // Start the desired activity using an Intent with the package name of the app

                Intent appIntent = pm.getLaunchIntentForPackage(packageName);
                startActivity(appIntent);
            }
        });







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
        public void onLongPress(MotionEvent e) {


            View view = findViewById(R.id.homescreen);

            // This method is called when the user has touched the screen and not released their finger for an extended period of time.
            // You can use this method to display a context menu or perform a more complex action.
            // Get the x and y coordinates of the long press event
            float x =e.getRawX();
            float y = e.getRawY();


            // Create a new popup window
            PopupWindow popupWindow = new PopupWindow(MainActivity.this);

            // Set the content view of the popup window
            View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_layout, null);
            popupWindow.setContentView(popupView);

            // Find the view inside the popup layout and set an onClickListener to it
            Button popupButton = popupView.findViewById(R.id.Wallpaper);

            Button arcSettingsButton = popupView.findViewById(R.id.ArcSettings);
            Button widgetsButton = popupView.findViewById(R.id.Widgets);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                    startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                    popupWindow.dismiss();

                    // Do something when the popup button is clicked
                }
            });
            // Set onClickListener for arc settings button to open a new activity
            arcSettingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ArcSettingsActivity.class);
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });

// Set onClickListener for widgets button to open default widget app
            widgetsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                    startActivity(Intent.createChooser(intent, "Select Widget"));
                    popupWindow.dismiss();
                }
            });
            // Set the size of the popup window
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            // Make the popup window dismiss when the user taps outside of it or presses the back button
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            // Show the popup window at the location of the long press event
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (int) x, (int) y);
             Button Wallpaper = findViewById(R.id.Wallpaper);


            // Set a click listener on the content view of the popup window to dismiss it when the user taps anywhere inside it
            popupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

        }

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
                    View mBottomSheet = findViewById(R.id.bottomSheet);
                    final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
//                            ExpandNotificationBar();
                            if ((mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)) {
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                            }else{
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
                                }}

                        } else {
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

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setPeekHeight(700);

        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.5f) {
                    // Disable dragging when the bottom sheet is more than 50% expanded
                    mBottomSheetBehavior.setDraggable(false);
                } else {
                    // Enable dragging when the bottom sheet is less than 50% expanded
                    mBottomSheetBehavior.setDraggable(true);
                }
            }
        });





    }



}