package com.launcher.myapplication;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {





    //for install and uninstall behaviour


    ///refresh when unistall

    private GestureDetector gestureDetector1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawer();
        gestureDetector = new GestureDetector(this, new GestureListener());


        gestureDetector1 = new GestureDetector(this, new MyGestureListener2());



        RecyclerView recyclerDrawer = findViewById(R.id.recycalview);
//        mDrawerGridView.setLayoutManager(mGridLayoutManager);
        PackageManager pm = this.getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
        Collections.sort(apps,
                new ResolveInfo.DisplayNameComparator(pm));
        Adapter adapter = new Adapter(this, apps, pm);
        recyclerDrawer.setAdapter(adapter);
        recyclerDrawer.setLayoutManager(new CircleLayoutManager(this));

        final BroadcastReceiver installBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String packageName = intent.getData().getSchemeSpecificPart();
                if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                    adapter.refreshAppList();
                } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                    adapter.refreshAppList();
                }
            }
        };


        /*                                                  install and uninstall behaviour                                               */

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(installBroadcastReceiver, intentFilter);



        /*                          seekbar implimentation                         */


        ArcSeekBar seekArc = findViewById(R.id.seekArc);
        recyclerDrawer.setAdapter(adapter);
        int itemCount = adapter.getItemCount();
        CircleLayoutManager layoutManager = new CircleLayoutManager(this);
        recyclerDrawer.setLayoutManager(layoutManager);
        seekArc.setMaxProgress(itemCount - 1);


        TextView letterTextView = findViewById(R.id.firstletter);
        letterTextView.setVisibility(View.VISIBLE);
        View mBottomSheet2 = findViewById(R.id.bottomSheet2);
        final BottomSheetBehavior<View> mBottomSheetBehavior2 = BottomSheetBehavior.from(mBottomSheet2);

        seekArc.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int progress) {

                mBottomSheetBehavior2.setDraggable(false);
                layoutManager.scrollToPosition(progress);
                letterTextView.setVisibility(View.VISIBLE);


                String packageName = apps.get(progress).activityInfo.packageName;
                String appName;
                try {
                    appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }

                String alphabet = String.valueOf(appName.charAt(0));

                letterTextView.setText(alphabet);


            }




        });
       seekArc.setOnStopTrackingTouch(new ProgressListener() {
           @Override
           public void invoke(int i) {
               mBottomSheetBehavior2.setDraggable(true);
           }
       });










        recyclerDrawer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                letterTextView.setVisibility(View.INVISIBLE);

            }
        });



        /*                       start tracking apps                  */


    }



/*                       stop track to open apps              */



            /* instanace of viewholder  */










//    ==========================================================================================================================








    /*                                           Above is Oncreate method                                                   */








//    ============================================================================================================================


















    /*                                         GESTURE HANDLING                                            */








////////////////////////////////////////////






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


            float x =e.getRawX();
            float y = e.getRawY();


            // Create a new popup window
            PopupWindow popupWindow = new PopupWindow(MainActivity.this);
            popupWindow.setBackgroundDrawable(null);
            // Set the content view of the popup window
            View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_layout, null);
            popupWindow.setContentView(popupView);

            // Find the view inside the popup layout and set an onClickListener to it
            ImageButton wallpaper = popupView.findViewById(R.id.wallpaper);

            ImageButton arcSettingsButton = popupView.findViewById(R.id.Widgets);
            ImageButton widgetsButton = popupView.findViewById(R.id.ArcSettings);
            wallpaper.setOnClickListener(v -> {

                //popup setwallpaper

                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                popupWindow.dismiss();


            });

            arcSettingsButton.setOnClickListener(v -> {

                //popup ArcSettingsbutton

                Intent intent = new Intent(MainActivity.this, ArcSettingsActivity.class);
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
                    View mBottomSheet2 = findViewById(R.id.bottomSheet2);
                    final BottomSheetBehavior<View> mBottomSheetBehavior1 = BottomSheetBehavior.from(mBottomSheet2);

                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
//                            ExpandNotificationBar();

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
                            if((mBottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED)){
                                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }else {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            }
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


        // Implement onFling for view2
        private class MyGestureListener2 extends GestureDetector.SimpleOnGestureListener {
            // Implement onFling for view2
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
            private BottomSheetBehavior bottomSheetBehavior;
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY < 0) {
                                View mBottomSheet2 = findViewById(R.id.bottomSheet2);
                                final BottomSheetBehavior<View> mBottomSheetBehavior1 = BottomSheetBehavior.from(mBottomSheet2);
                            }
                            result = true;
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }

            public void onSwipeRight() {
                // do something when swiped right
            }

            public void onSwipeLeft() {
                // do something when swiped left
            }

            public void onSwipeTop() {
                // do something when swiped up
            }

            public void onSwipeBottom() {
                // do something when swiped down
            }
        }


// add this to your onCreate method



    private void initializeDrawer() {
        View mBottomSheet = findViewById(R.id.bottomSheet);
        View mBottomSheet1 = findViewById(R.id.bottomSheet2);
        RecyclerView recyclerDrawer = findViewById(R.id.recycalview);
        recyclerDrawer.setLayoutManager(new CircleLayoutManager(this));
//        mDrawerGridView.setLayoutManager(mGridLayoutManager);
        PackageManager pm = this.getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> installedAppList = pm.queryIntentActivities(main,0);
        Collections.sort(installedAppList,
                new ResolveInfo.DisplayNameComparator(pm));
        Adapter adapter = new Adapter(this, installedAppList, pm);
        recyclerDrawer.setAdapter(adapter);
//        mDrawerGridView.setLayoutManager(mGridLayoutManager);



        RecyclerView recyclerView = findViewById(R.id.recycalDrawer);
        recyclerView.setLayoutManager(new GridLayoutManager(this,6,RecyclerView.VERTICAL,false));
        Adapter adapter1 = new Adapter(this,installedAppList,pm);
        recyclerView.setAdapter(adapter1);


        final BottomSheetBehavior<View> mBottomSheetBehavior1 = BottomSheetBehavior.from(mBottomSheet1);

       final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);


        mBottomSheetBehavior1.setHideable(false);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.setPeekHeight(550);

        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet1, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet1, float slideOffset) {
                if (slideOffset > 0.5f) {
                    // Disable dragging when the bottom sheet is more than 50% expanded
                    mBottomSheetBehavior1.setDraggable(true);
                } else {
                    // Enable dragging when the bottom sheet is less than 50% expanded
                    mBottomSheetBehavior1.setDraggable(true);
                }
            }
        });



        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setPeekHeight(0);

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
                    mBottomSheetBehavior.setDraggable(false);
                }
            }
        });








        mBottomSheet1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector1.onTouchEvent(event);
            }
        });




    }





}