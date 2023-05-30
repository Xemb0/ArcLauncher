package com.launcher.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.launcher.myapplication.VerticalViewAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ActivityPager extends AppCompatActivity implements View.OnTouchListener {

    private ViewPager2 viewPager;
    private VerticalViewAdapter adapter;
    StackTransformer transformer;
    PaddingPageTransformer paddingPageTransformer;
    private  GestureDetector gestureDetector;
    private Vibrator vibrator;
    private ArrayList<String> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.ViewPager);
        adapter = new VerticalViewAdapter();
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        transformer = new StackTransformer();
        paddingPageTransformer = new PaddingPageTransformer(getResources().getDimensionPixelSize(R.dimen.padding_start));
        viewPager.setPageTransformer(transformer);
        gestureDetector = new GestureDetector(viewPager.getContext(), new ActivityPager.GestureListener());
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                applyTransformation(position, positionOffsetPixels);

            }
            public void onPageSelected(int position) {
                super.onPageSelected(position);



            }
        });

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffY) > Math.abs(diffX)) {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (viewPager.getCurrentItem() == 0 && diffY > 0) {

                    ExpandNotificationBar();
                }
                    else {
                        vibrate();
                    }

                }
            }
            return true;
        }


        private void ExpandNotificationBar() {
            try {
                @SuppressLint("WrongConstant") Object service = viewPager.getContext().getSystemService("statusbar");
                Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
                Method expand = statusBarManager.getMethod("expandNotificationsPanel");
                expand.invoke(service);
            } catch (Exception e) {
                String errorMessage = "Notification panel swipe down error: " + e.getMessage();
                Log.e("StatusBar", errorMessage);
                Toast.makeText(viewPager.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void ExpandNotificationBar() {
        try {
            @SuppressLint("WrongConstant") Object service = viewPager.getContext().getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("expandNotificationsPanel");
            expand.invoke(service);
        } catch (Exception e) {
            String errorMessage = "Notification panel swipe down error: " + e.getMessage();
            Log.e("StatusBar", errorMessage);
            Toast.makeText(viewPager.getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    //@Override
//    public void  onDestroy() {
//        super.onDestroy();
//        viewPager.unregisterOnPageChangeCallback();
//    }
    private void applyTransformation(int position, int positionOffsetPixels) {
        float pixelsPerPosition = 1f / viewPager.getWidth(); // Calculate the number of pixels per position

        float offsetInPixels = positionOffsetPixels + position * viewPager.getWidth();
        float offset = offsetInPixels * pixelsPerPosition;


        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);

        // Use the 'offset' float value in your custom transformation logic
        // ...
    }

    void vibrate() {
        vibrator = (Vibrator) viewPager.getContext().getSystemService(Context.VIBRATOR_SERVICE);
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

