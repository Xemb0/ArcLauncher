//package com.launcher.myapplication;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.transition.ChangeBounds;
//import androidx.transition.Transition;
//import androidx.transition.TransitionManager;
//
//import android.animation.ValueAnimator;
//import android.annotation.SuppressLint;
//import android.content.ActivityNotFoundException;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.TransitionDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.VibrationEffect;
//import android.os.Vibrator;
//import android.util.Log;
//import android.view.GestureDetector;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowInsets;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import com.marcinmoskala.arcseekbar.ArcSeekBar;
//import com.marcinmoskala.arcseekbar.ProgressListener;
//
//import java.lang.reflect.Method;
//import java.util.Collections;
//import java.util.List;
//
//public class HomeScreen extends AppCompatActivity {
//
//    private static final int DEFAULT_ICON_SPAN = 5;
//    private BottomSheetBehavior<View> mBottomSheetBehavior1;
//    private Vibrator vibrator;
//    private VibrationEffect vibrationEffect;
//    private RelativeLayout DrawerSheet;
//
//
//
//
//    private int previousProgress = -1;
//    //for install and uninstall behaviour
//
//
//    ///refresh when unistall
//
//    private GestureDetector gestureDetector1;
//
//
//    public HomeScreen() {
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        LinearLayout clockView= findViewById(R.id.clock_date);
//
//
//
//
//        // This callback will only be called when MyFragment is at least S
//
//
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//
//
//
//
//
//        View listView = findViewById(android.R.id.content);
//
//        final int bottomPadding = listView.getPaddingBottom();
//        listView.setOnApplyWindowInsetsListener((v, insets) -> {
//            v.setPadding(
//                    v.getPaddingLeft(),
//                    v.getPaddingTop(),
//                    v.getPaddingRight(),
//                    bottomPadding + insets.getSystemWindowInsetBottom());
//            return insets.consumeSystemWindowInsets();
//        });
//
//        initializeDrawer();
//        gestureDetector = new GestureDetector(this, new GestureListener());
//
//
//
//
//
//
//
//
//
//
//
//
//
//        View mBottomSheet = findViewById(R.id.bottomSheet);
//        View mBottomSheet1 = findViewById(R.id.DrawerSheet);
//        mBottomSheetBehavior1 = BottomSheetBehavior.from(mBottomSheet1);
//
//
//        final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
//
//
//        mBottomSheetBehavior1.setHideable(false);
//        mBottomSheetBehavior.setHideable(false);
//        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
//
//        DrawerSheet = findViewById(R.id.DrawerSheet);
//        transitionDrawable = (TransitionDrawable) getResources().getDrawable(R.drawable.bottom_sheet_transition);
//        transitionDrawable.setAlpha(0);
//        DrawerSheet.setBackground(transitionDrawable);
//        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet1, int newState) {
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    vibrate();
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//
//                    vibrate();
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
//            }
//
//            private void animatePadding(final View view, final int left, final int top, final int right, final int bottom) {
//                ValueAnimator animator = ValueAnimator.ofInt(view.getPaddingLeft(), left);
//                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int animatedValue = (int) animation.getAnimatedValue();
//                        view.setPadding(animatedValue, top, right, bottom);
//                    }
//                });
//                Transition transition = new ChangeBounds();
//
//                transition.setDuration(300); // Set the desired duration
//                TransitionManager.beginDelayedTransition(DrawerSheet, transition);
//                animator.start();
//            }
//
//
//
//
//
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet1, float slideOffset) {
//                int transitionAlpha = (int) (slideOffset * 255);
//                transitionDrawable.setAlpha(transitionAlpha);
//                if (slideOffset > 0.5f) {
//                    // Disable dragging when the bottom sheet is more than 50% expanded
//                    mBottomSheetBehavior1.setDraggable(true);
//                    animatePadding(DrawerSheet, 0, 70, 0, 0);
//                } else {
//                    // Enable dragging when the bottom sheet is less than 50% expanded
//                    mBottomSheetBehavior1.setDraggable(true);
//                    animatePadding(DrawerSheet, 0, 0, 0, 0);
//                }
//            }
//        });
//
//
//
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        mBottomSheetBehavior.setPeekHeight(0);
//
//        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if(newState == BottomSheetBehavior.STATE_EXPANDED)
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//            }
//
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                if (slideOffset > 0.5f) {
//                    // Disable dragging when the bottom sheet is more than 50% expanded
//                    mBottomSheetBehavior.setDraggable(false);
//                } else {
//                    // Enable dragging when the bottom sheet is less than 50% expanded
//                    mBottomSheetBehavior.setDraggable(false);
//                }
//            }
//        });
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//        HomeWatcher mHomeWatcher = new HomeWatcher(this);
//        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
//            @Override
//            public void onHomePressed() {
//                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
//
//            }
//            @Override
//            public void onHomeLongPressed() {
//                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
//
//            }
//        });
//        mHomeWatcher.startWatch();
//
//
//
//        ImageButton lenceIcon = findViewById(R.id.lence_icon);
//        lenceIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open Google Lens application
//                PackageManager packageManager = getPackageManager();
//                Intent lensIntent = packageManager.getLaunchIntentForPackage("com.google.ar.lens");
//
//                if (lensIntent != null) {
//                    // Google Lens is installed, open the application
//                    startActivity(lensIntent);
//                } else {
//                    // Google Lens is not installed
//                    Toast.makeText(getApplicationContext(), "Google Lens is not installed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        ImageButton assistanceButton = findViewById(R.id.google_assistance);
//        assistanceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open Google Assistant listener
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VOICE_COMMAND);
//                intent.setPackage("com.google.android.googlequicksearchbox");
//
//                PackageManager packageManager = getPackageManager();
//                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
//
//                if (activities.size() > 0) {
//                    // Google Assistant is available, open the Assistant listener
//                    startActivity(intent);
//                } else {
//                    // Google Assistant is not available, prompt the user to install it
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.googlequicksearchbox")));
//                    } catch (ActivityNotFoundException e) {
//                        // Google Play Store app is not available on the device, open the Google Play Store website
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox")));
//                    }
//                }
//            }
//        });
//
//        SearchView searchView = findViewById(R.id.search_view);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Reset the search view
//                searchView.setQuery("", false);
//                searchView.clearFocus();
//
//                // Close the search view
//                searchView.setIconified(true);
//
//                // Perform search using the query
//                String url = "https://www.google.com/search?q=" + query;
//
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                intent.setPackage("com.android.chrome"); // Specify Chrome package name
//
//                PackageManager packageManager = getPackageManager();
//                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
//
//                if (activities.size() > 0) {
//                    // Chrome is installed, open the URL in Chrome
//                    startActivity(intent);
//                } else {
//                    // Chrome is not installed, handle the error or open in a different browser
//                    Toast.makeText(getApplicationContext(), "Chrome is not installed", Toast.LENGTH_SHORT).show();
//                }
//
//                return false;
//            }
//
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Handle text change in the search view if needed
//                return false;
//            }
//        });
//
//        /*                       start tracking apps                  */
//
//
//    }
//
//
//
//    /*                       stop track to open apps              */
//
//
//
//    /* instanace of viewholder  */
//
//
//
//
//
//
//
//
//
//
////    ==========================================================================================================================
//
//
//
//
//
//
//
//
//    /*                                           Above is Oncreate method                                                   */
//
//
//
//
//
//
//
//
////    ============================================================================================================================
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    /*                                         GESTURE HANDLING                                            */
//
//
//
//
//
//
//
//
//////////////////////////////////////////////
//
//
//
//
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
//        return super.onTouchEvent(event);
//    }
//
//    private GestureDetector gestureDetector;
//
//    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        private static final int SWIPE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public void onLongPress(MotionEvent e) {
//
//
//            View view = findViewById(R.id.homescreen);
//            vibrate();
//
//
//            float x =e.getRawX();
//            float y = e.getRawY();
//
//
//            // Create a new popup window
//            PopupWindow popupWindow = new PopupWindow(HomeScreen.this);
//            popupWindow.setBackgroundDrawable(null);
//            // Set the content view of the popup window
//            View popupView = LayoutInflater.from(HomeScreen.this).inflate(R.layout.popup_layout, null);
//            popupWindow.setContentView(popupView);
//
//            // Find the view inside the popup layout and set an onClickListener to it
//            ImageButton wallpaper = popupView.findViewById(R.id.wallpaper);
//
//            ImageButton arcSettingsButton = popupView.findViewById(R.id.ArcSettings);
//            ImageButton widgetsButton = popupView.findViewById(R.id.Widgets);
//            wallpaper.setOnClickListener(v -> {
//
//                //popup setwallpaper
//
//                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
//                startActivity(Intent.createChooser(intent, "Select Wallpaper"));
//                popupWindow.dismiss();
//
//
//            });
//
//            arcSettingsButton.setOnClickListener(v -> {
//
//                //popup ArcSettingsbutton
//
//                Intent intent = new Intent(HomeScreen.this, ArcSettingsActivity.class);
//                startActivity(intent);
//                popupWindow.dismiss();
//            });
//
//
//            widgetsButton.setOnClickListener(v -> {
//
//
//                //popup Widgets
//
//                Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
//                startActivity(Intent.createChooser(intent, "Select Widget"));
//                popupWindow.dismiss();
//            });
//            // Set the size of the popup window
//            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            // Make the popup window dismiss when the user taps outside of it or presses the back button
//            popupWindow.setOutsideTouchable(true);
//            popupWindow.setFocusable(true);
//            int verticalOffset = 200;
//
//            // Show the popup window at the location of the long press event
//            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (int) x, (int) y-verticalOffset);
//
//
//
//            // Set a click listener on the content view of the popup window to dismiss it when the user taps anywhere inside it
//            popupView.setOnClickListener(v -> popupWindow.dismiss());
//
//        }
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//
//        View mBottomSheet = findViewById(R.id.bottomSheet);
//        final BottomSheetBehavior<View> mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
//        View mBottomSheet2 = findViewById(R.id.DrawerSheet);
//        final BottomSheetBehavior<View> mBottomSheetBehavior1 = BottomSheetBehavior.from(mBottomSheet2);
//
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            boolean result = false;
//            try {
//                float diffY = e2.getY() - e1.getY();
//                float diffX = e2.getX() - e1.getX();
//                if (Math.abs(diffX) > Math.abs(diffY)) {
//                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffX > 0) {
//                            // Open the dialer application
//                            Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
//                            vibrate();
//                            startActivity(dialerIntent);
//                        } else {  Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
//                            vibrate();
//                            startActivity(intent);
//
//                        }
//                        result = true;
//                    }
//                } else {
//
//                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                        if (diffY > 0) {
////                            ExpandNotificationBar();
//
////                            ExpandNotificationBar();
//                            try{
//                                @SuppressLint("WrongConstant") Object service = getSystemService("statusbar");
//                                Class<?> statusBarManager = Class.forName("android.app.StatusBarManager"); // Fix typo in class name
//                                Method expand = statusBarManager.getMethod("expandNotificationsPanel");
//                                expand.invoke(service);
//                            }
//                            catch (Exception e) {
//                                String errorMessage = "Notification panel swipe down error: " + e.getMessage();
//                                Log.e("StatusBar", errorMessage);
//                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                            }
//
//                        } else {
//                            if((mBottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED)){
//                                mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
//                            }else {
//                                vibrate();
//                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//                            }
//                        }
//                        result = true;
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//    }
//
//
//    // add this to your onCreate method
//    private TransitionDrawable transitionDrawable;
//    private RelativeLayout bottomSheetLayout;
//
//    @SuppressLint("UseCompatLoadingForDrawables")
//    private void initializeDrawer() {
//
//
//
//        RecyclerView recyclerDrawer = findViewById(R.id.recycalview);
////        mDrawerGridView.setLayoutManager(mGridLayoutManager);
//        PackageManager pm = this.getPackageManager();
//        Intent main = new Intent(Intent.ACTION_MAIN, null);
//        main.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> apps = pm.queryIntentActivities(main, 0);
//        Collections.sort(apps,
//                new ResolveInfo.DisplayNameComparator(pm));
//        Adapter adapter = new Adapter(this, apps, pm);
//        recyclerDrawer.setAdapter(adapter);
//        adapter.refreshAppList();
//        recyclerDrawer.setLayoutManager(new CircleLayoutManager(this));
//
//        final BroadcastReceiver installBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//                String packageName = intent.getData().getSchemeSpecificPart();
//                if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
//                    adapter.refreshAppList();
//
//                } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
//                    adapter.refreshAppList();
//                }
//            }
//        };
//
//
//        /*                                                  install and uninstall behaviour                                               */
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
//        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
//        intentFilter.addDataScheme("package");
//        registerReceiver(installBroadcastReceiver, intentFilter);
//
//
//
//        /*                          seekbar implimentation                         */
//
//
//        ArcSeekBar seekArc = findViewById(R.id.seekArc);
//        recyclerDrawer.setAdapter(adapter);
//        int itemCount = adapter.getItemCount();
//        CircleLayoutManager layoutManager = new CircleLayoutManager(this);
//        recyclerDrawer.setLayoutManager(layoutManager);
//        seekArc.setMaxProgress(itemCount - 1);
//
//
//        TextView letterTextView = findViewById(R.id.firstletter);
//        View IconShadow = findViewById(R.id.Icon_shadow);
//        letterTextView.setVisibility(View.INVISIBLE);
//        View mBottomSheet2 = findViewById(R.id.DrawerSheet);
//        final BottomSheetBehavior<View> mBottomSheetBehavior2 = BottomSheetBehavior.from(mBottomSheet2);
//        IconShadow.setVisibility(View.INVISIBLE);
//
//        seekArc.setOnProgressChangedListener(new ProgressListener() {
//            @Override
//            public void invoke(int progress) {
//                if (progress != previousProgress) { // check if progress has changed
//                    vibrate();
//                    previousProgress = progress; // update previous progress value
//                }
//
//                mBottomSheetBehavior2.setDraggable(false);
//                layoutManager.scrollToPosition(progress);
//                letterTextView.setVisibility(View.VISIBLE);
//                IconShadow.setVisibility(View.VISIBLE);
//
//
//                String packageName = apps.get(progress).activityInfo.packageName;
//                String appName;
//                try {
//                    appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
//                } catch (PackageManager.NameNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//
//                String alphabet = String.valueOf(appName.charAt(0));
//
//                letterTextView.setText(alphabet);
//            }
//        });
//
//        seekArc.setOnStopTrackingTouch(new ProgressListener() {
//            @Override
//            public void invoke(int i) {
//                letterTextView.setVisibility(View.INVISIBLE);
//                IconShadow.setVisibility(View.INVISIBLE);
//                mBottomSheetBehavior2.setDraggable(true);
//            }
//        });
//
//
//
//
//
//
//
//
//
//
//
//
//        int iconSpan = getIconSizeFromSharedPreferences();
//        RecyclerView recyclerView = findViewById(R.id.recycalDrawer);
//        recyclerView.setLayoutManager(new GridLayoutManager(this,iconSpan,RecyclerView.HORIZONTAL,false));
//        recyclerView.setAdapter(adapter);
//
//
//
//// Get a reference to your bottom sheet view
//        View bottomSheetView = findViewById(R.id.homescreen);
//
//// Set an OnClickListener on the bottom sheet view
//
//
//
//
//    }
//
//
//
//
//
//    private int getIconSizeFromSharedPreferences() {
//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        return sharedPreferences.getInt("iconSpan", DEFAULT_ICON_SPAN);
//    }
//
//
//
//
//
//
//
//    @Override
//    public void onBackPressed() {
//
//
//        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
//    }
//
//
//    private void vibrate() {
//        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            VibrationEffect vibrationEffect = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // For devices with API level Q and above
//                vibrationEffect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK);
//            } else {
//                // For devices with API level Oreo (API 26) to Pie (API 28)
//                vibrationEffect = VibrationEffect.createOneShot(1, VibrationEffect.DEFAULT_AMPLITUDE);
//            }
//            vibrator.vibrate(vibrationEffect);
//        } else {
//            // For devices with API level 21 to 25
//            vibrator.vibrate(50);
//        }
//    }
//
//
//}
