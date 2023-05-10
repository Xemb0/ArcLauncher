package com.launcher.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    //for install and uninstall behaviour
    private final BroadcastReceiver installBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Send broadcast to notify adapter to refresh app list
            Intent refreshIntent = new Intent();
            refreshIntent.setAction(Intent.ACTION_PACKAGE_ADDED);
            sendBroadcast(refreshIntent);
        }
    };


    ///refresh when unistall



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeDrawer();
        gestureDetector = new GestureDetector(this, new GestureListener());



        /*                                                  install and uninstall behaviour                                               */

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(installBroadcastReceiver, intentFilter);



 /*                          seekbar implimentation                         */
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent,0);
        Collections.sort(apps,new ResolveInfo.DisplayNameComparator(pm));
        Adapter adapter = new Adapter(this, apps, pm);



        ArcSeekBar seekArc = findViewById(R.id.seekArc);
        RecyclerView recyclerDrawer = findViewById(R.id.recycalview);
        recyclerDrawer.setAdapter(adapter);
        int itemCount = adapter.getItemCount();
        CircleLayoutManager layoutManager = new CircleLayoutManager(this);
        recyclerDrawer.setLayoutManager(layoutManager);
        seekArc.setMaxProgress(itemCount-1);


        SeekBar seekBar1 = findViewById(R.id.normalseekbar);


        seekBar1.setMax(itemCount);
        TextView letterTextView = findViewById(R.id.firstletter);
        letterTextView.setVisibility(View.INVISIBLE);
        seekBar1.setMax(itemCount-1);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    letterTextView.setVisibility(View.VISIBLE);
                    seekArc.setProgress(progress);
                    layoutManager.scrollToPosition(progress);


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
            }



            /*                       start tracking apps                  */

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();




                String packageName = apps.get(progress).activityInfo.packageName;

                // Get the first letter of the package name
                String appName = String.valueOf(packageName.charAt(0));
                char appLetter = appName.charAt(0);
                letterTextView.setText(String.valueOf(appLetter));
            }



/*                       stop track to open apps              */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                letterTextView.setVisibility(View.INVISIBLE);

                String packageName = apps.get(seekBar.getProgress()).activityInfo.packageName;

                // Start the desired activity using an Intent with the package name of the app

                Intent appIntent = pm.getLaunchIntentForPackage(packageName);
                startActivity(appIntent);
            }
        });



            /* instanace of viewholder  */









    }
//    ==========================================================================================================================








    /*                                           Above is Oncreate method                                                   */








//    ============================================================================================================================









    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

        if (requestCode == adapter.UNINSTALL_REQUEST_CODE && resultCode == RESULT_OK) {

            adapter.refreshAppList();

            recyclerDrawer.setAdapter(adapter);

            CircleLayoutManager layoutManager = new CircleLayoutManager(this);
            recyclerDrawer.setLayoutManager(layoutManager);

            layoutManager.scrollToPosition(adapter.position);

        }


    }









    /*                                         GESTURE HANDLING                                            */






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

            // Set the content view of the popup window
            View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_layout, null);
            popupWindow.setContentView(popupView);

            // Find the view inside the popup layout and set an onClickListener to it
            Button wallpaper = popupView.findViewById(R.id.Wallpaper);

            Button arcSettingsButton = popupView.findViewById(R.id.ArcSettings);
            Button widgetsButton = popupView.findViewById(R.id.Widgets);
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

            // Show the popup window at the location of the long press event
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (int) x, (int) y);



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

       final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(false);

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

    //refresh when uninstall a aapp

    private final ActivityResultLauncher<Intent> uninstallAppLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            PackageManager pm = this.getPackageManager();
                            Intent main = new Intent(Intent.ACTION_MAIN, null);
                            main.addCategory(Intent.CATEGORY_LAUNCHER);
                            List<ResolveInfo> installedAppList = pm.queryIntentActivities(main,0);
                            Collections.sort(installedAppList,
                                    new ResolveInfo.DisplayNameComparator(pm));
                            Adapter adapter = new Adapter(this, installedAppList, pm);
                            // Refresh the app list when an app is uninstalled
                            adapter.refreshAppList();
                        }
                    });



}