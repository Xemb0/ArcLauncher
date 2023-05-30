package com.launcher.myapplication;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class VerticalView1ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    private Context context;
    private static final int DEFAULT_ICON_SPAN = 5;

    private int previousProgress = -1;
    private Adapter appAdapter;
    RecyclerView recyclerView;
    ArcSeekBar seekArc;
    Vibrator vibrator;
    GestureDetector gestureDetector;

    public VerticalView1ViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;

        // Initialize the views and variables for Vertical View 2
        recyclerView = itemView.findViewById(R.id.CircularDrawerPager);
        seekArc = itemView.findViewById(R.id.seekArcPager);
        initializeAppDrawer();


         gestureDetector = new GestureDetector(context, new GestureListener());

        itemView.setOnTouchListener((view, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return super.itemView.onTouchEvent(motionEvent);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        @Override
        public void onLongPress(MotionEvent e) {


            View view = itemView.getRootView();
            vibrate();


            float x =e.getRawX();
            float y = e.getRawY();


            // Create a new popup window
            PopupWindow popupWindow = new PopupWindow(context);
            popupWindow.setBackgroundDrawable(null);
            // Set the content view of the popup window
            View popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);
            popupWindow.setContentView(popupView);

            // Find the view inside the popup layout and set an onClickListener to it
            ImageButton wallpaper = popupView.findViewById(R.id.wallpaper);

            ImageButton arcSettingsButton = popupView.findViewById(R.id.ArcSettings);
            ImageButton widgetsButton = popupView.findViewById(R.id.Widgets);
            wallpaper.setOnClickListener(v -> {

                //popup setwallpaper

                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                context.startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                popupWindow.dismiss();


            });

            arcSettingsButton.setOnClickListener(v -> {

                //popup ArcSettingsbutton

                Intent intent = new Intent(context, ArcSettingsActivity.class);
                context.startActivity(intent);
                popupWindow.dismiss();
            });


            widgetsButton.setOnClickListener(v -> {


                //popup Widgets

                Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                context.startActivity(Intent.createChooser(intent, "Select Widget"));
                popupWindow.dismiss();
            });
            // Set the size of the popup window
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            // Make the popup window dismiss when the user taps outside of it or presses the back button
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            int verticalOffset = 200;

            // Show the popup window at the location of the long press event
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (int) x, (int) y-verticalOffset);



            // Set a click listener on the content view of the popup window to dismiss it when the user taps anywhere inside it
            popupView.setOnClickListener(v -> popupWindow.dismiss());

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    vibrate();
                    if (diffX > 0) {
                        Intent RightSwipe = new Intent(Intent.ACTION_DIAL);
                        context.startActivity(RightSwipe);
                    } else {
                        Intent LeftSwipe = new Intent(Intent.ACTION_MAIN);
                        LeftSwipe.addCategory(Intent.CATEGORY_APP_MESSAGING);
                        context.startActivity(LeftSwipe);
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        ExpandNotificationBar();
                    }
                    else {
                        vibrate();
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent event) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {

            Intent Viewpager = new Intent(context, ActivityPager.class);
            context.startActivity(Viewpager);

            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            return true;
        }

        private void ExpandNotificationBar() {
            try {
                @SuppressLint("WrongConstant") Object service = context.getSystemService("statusbar");
                Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
                Method expand = statusBarManager.getMethod("expandNotificationsPanel");
                expand.invoke(service);
            } catch (Exception e) {
                String errorMessage = "Notification panel swipe down error: " + e.getMessage();
                Log.e("StatusBar", errorMessage);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }

    }



    // Rest of the class implementation...
}
