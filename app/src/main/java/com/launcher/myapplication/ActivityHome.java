package com.launcher.myapplication;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ActivityHome extends AppCompatActivity {

    GestureDetector gestureDetector;
    Adapter appAdapter;
    private TransitionDrawable transitionDrawable;
    BottomSheetBehavior<View> mDrawerSheetBehaviour;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //gesture listener for whole activity when creates
        View rootView = findViewById(android.R.id.content);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        final int bottomPadding = rootView.getPaddingBottom();
        rootView.setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    bottomPadding + insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });
        gestureDetector = new GestureDetector(this, new GestureListener());
        AppDrawer();
        BottomSheet();

    }


    /*                                 Oncreate Over  */


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        @Override
        public void onLongPress(MotionEvent e) {


            View view = findViewById(R.id.homescreen);
            vibrate();


            float x =e.getRawX();
            float y = e.getRawY();


            // Create a new popup window
            PopupWindow popupWindow = new PopupWindow(ActivityHome.this);
            popupWindow.setBackgroundDrawable(null);
            // Set the content view of the popup window
            View popupView = LayoutInflater.from(ActivityHome.this).inflate(R.layout.popup_layout, null);
            popupWindow.setContentView(popupView);

            // Find the view inside the popup layout and set an onClickListener to it
            ImageButton wallpaper = popupView.findViewById(R.id.wallpaper);

            ImageButton arcSettingsButton = popupView.findViewById(R.id.ArcSettings);
            ImageButton widgetsButton = popupView.findViewById(R.id.Widgets);
            wallpaper.setOnClickListener(v -> {

                //popup setwallpaper

                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                popupWindow.dismiss();


            });

            arcSettingsButton.setOnClickListener(v -> {

                //popup ArcSettingsbutton

                Intent intent = new Intent(ActivityHome.this, ArcSettingsActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            });


            widgetsButton.setOnClickListener(v -> {


                //popup Widgets

                Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                startActivity(Intent.createChooser(intent, "Select Widget"));
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
                        startActivity(RightSwipe);
                    } else {
                        Intent LeftSwipe = new Intent(Intent.ACTION_MAIN);
                        LeftSwipe.addCategory(Intent.CATEGORY_APP_MESSAGING);
                        startActivity(LeftSwipe);
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        ExpandNotificationBar();
                    } else {
                        vibrate();
                        View mDrawerSheet = findViewById(R.id.DrawerSheet);
                        final BottomSheetBehavior<View> mDrawerSheetBehaviour = BottomSheetBehavior.from(mDrawerSheet);
                        mDrawerSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                @SuppressLint("WrongConstant") Object service = getSystemService("statusbar");
                Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
                Method expand = statusBarManager.getMethod("expandNotificationsPanel");
                expand.invoke(service);
            } catch (Exception e) {
                String errorMessage = "Notification panel swipe down error: " + e.getMessage();
                Log.e("StatusBar", errorMessage);
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }

    }


    private static final int DEFAULT_ICON_SPAN = 5;

    private int previousProgress = -1;

    void AppDrawer() {

        RecyclerView CircularDrawer = findViewById(R.id.recycalview);
        PackageManager pm = this.getPackageManager();

        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
        appAdapter = new Adapter(this, apps, pm);
        CircularDrawer.setAdapter(appAdapter);
        CircleLayoutManager circleLayoutManager = new CircleLayoutManager(this);
        CircularDrawer.setLayoutManager(circleLayoutManager);

        final BroadcastReceiver installUninstallBrodcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                    appAdapter.refreshAppList();
                } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                    appAdapter.refreshAppList();

                }
            }
        };


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(installUninstallBrodcastReciver, intentFilter);

        ArcSeekBar seekArc = findViewById(R.id.seekArc);
        int AppCount = appAdapter.getItemCount();
        seekArc.setMaxProgress(AppCount - 1);

        TextView fisrtletter = findViewById(R.id.firstletter);
        View focusAppBackground = findViewById(R.id.Icon_shadow);
        fisrtletter.setVisibility(View.INVISIBLE);
        focusAppBackground.setVisibility(View.INVISIBLE);


        seekArc.setOnProgressChangedListener(progress -> {
            if (progress != previousProgress) {
                vibrate();
                previousProgress = progress;
            }
            circleLayoutManager.scrollToPosition(progress);
            fisrtletter.setVisibility(View.VISIBLE);
            focusAppBackground.setVisibility(View.VISIBLE);

            String packageName = apps.get(progress).activityInfo.packageName;
            String appName;
            try {
                appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();

            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
            String alphabet = String.valueOf(appName.charAt(0));
            fisrtletter.setText(alphabet);
        });
        seekArc.setOnStopTrackingTouch(i -> {
            fisrtletter.setVisibility(View.INVISIBLE);
            focusAppBackground.setVisibility(View.INVISIBLE);
        });

        int iconSpan = getIconSizeFromSharedPreferences();
        RecyclerView recyclerDrawer = findViewById(R.id.recycalDrawer);
        recyclerDrawer.setLayoutManager(new PreCachingGridLayoutManager(this, iconSpan, RecyclerView.VERTICAL, false));
        recyclerDrawer.setAdapter(appAdapter);
        recyclerDrawer.setHasFixedSize(true);
        recyclerDrawer.setItemViewCacheSize(100);

    }

    private int getIconSizeFromSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("iconSpan", DEFAULT_ICON_SPAN);
    }


    void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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

    private void BottomSheet() {

        View mCircularSheet = findViewById(R.id.CircularSheet);
        final BottomSheetBehavior<View> mCircularSheetBehaviour = BottomSheetBehavior.from(mCircularSheet);
        mCircularSheetBehaviour.setPeekHeight(0);
        mCircularSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        mCircularSheetBehaviour.setDraggable(false);

        mCircularSheetBehaviour.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        View mDrawerSheet = findViewById(R.id.DrawerSheet);
        mDrawerSheetBehaviour = BottomSheetBehavior.from(mDrawerSheet);
        mDrawerSheetBehaviour.setDraggable(true);
        mDrawerSheetBehaviour.setHideable(false);
        mDrawerSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        transitionDrawable = (TransitionDrawable) getResources().getDrawable(R.drawable.bottom_sheet_transition);
        transitionDrawable.setAlpha(0);
        mDrawerSheet.setBackground(transitionDrawable);

// Define the desired padding increase and the maximum padding value
        int desiredPaddingIncrease = 100; // Adjust this value as needed
        int maxPaddingTop = 200; // Adjust this value as needed

        mDrawerSheetBehaviour.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            private int originalPaddingTop = mDrawerSheet.getPaddingTop();

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mCircularSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mCircularSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    vibrate();
                    mCircularSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    // Reset the padding to its original value
                    bottomSheet.setPadding(
                            bottomSheet.getPaddingLeft(),
                            originalPaddingTop,
                            bottomSheet.getPaddingRight(),
                            bottomSheet.getPaddingBottom()
                    );
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                int transitionAlpha = (int) (slideOffset * 255);
                transitionDrawable.setAlpha(transitionAlpha);

                // Calculate the new padding based on the slide offset
                int newPadding = (int) (slideOffset * desiredPaddingIncrease);
                newPadding = Math.min(newPadding, maxPaddingTop - originalPaddingTop); // Limit the new padding

                // Set the new padding for top, left, right, and bottom
                bottomSheet.setPadding(
                        bottomSheet.getPaddingLeft(),
                        originalPaddingTop + newPadding,
                        bottomSheet.getPaddingRight(),
                        bottomSheet.getPaddingBottom()
                );

                if (slideOffset > 0.4f) {
                    // Do something when slide offset is greater than 0.4f
                } else {
                    // Do something else when slide offset is less than or equal to 0.4f
                }
            }
        });
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                mDrawerSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            @Override
            public void onHomeLongPressed() {
                mDrawerSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }
    @Override
    public void onBackPressed() {
        mDrawerSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    @Override
    protected void onResume() {
        appAdapter.refreshAppList();
        super.onResume();
    }
}