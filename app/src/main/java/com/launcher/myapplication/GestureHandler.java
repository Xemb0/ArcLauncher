package com.launcher.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.lang.reflect.Method;

public class GestureHandler extends AppCompatActivity implements View.OnTouchListener {
    private GestureDetector gestureDetector;

    public GestureHandler(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        Vibrate vibrate = new Vibrate();

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            vibrate.vibrate();
            HomescreenPopup(motionEvent);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    vibrate.vibrate();
                    if (diffX > 0) {
                        Intent RightSwipe = new Intent(Intent.ACTION_DIAL);
                        startActivity(RightSwipe);
                    } else {
                        Intent LeftSwipe = new Intent(Intent.CATEGORY_APP_MESSAGING);
                        startActivity(LeftSwipe);
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        ExpandNotificationBar();
                    } else {
                        vibrate.vibrate();
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

        private void HomescreenPopup(MotionEvent e) {
            View homescreen = findViewById(R.id.homescreen);
            float x = e.getRawX();
            float y = e.getRawY();

            PopupWindow popupWindow = new PopupWindow(getApplicationContext());
            popupWindow.setBackgroundDrawable(null);
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_layout, (ViewGroup) homescreen);
            popupWindow.setContentView(popupView);

            ImageButton wallpaper = popupView.findViewById(R.id.wallpaper);
            ImageButton arcSettingsButton = popupView.findViewById(R.id.ArcSettings);
            ImageButton widgetsButton = popupView.findViewById(R.id.Widgets);

            wallpaper.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                startActivity(Intent.createChooser(intent, "Select Wallpaper"));
                popupWindow.dismiss();
            });

            arcSettingsButton.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ArcSettingsActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            });

            widgetsButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
                startActivity(Intent.createChooser(intent, "Select Widget"));
                popupWindow.dismiss();
            });

            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            int verticalOffset = 200;

            popupWindow.showAtLocation(homescreen, Gravity.NO_GRAVITY, (int) x, (int) y - verticalOffset);

            popupView.setOnClickListener(v -> popupWindow.dismiss());
        }
    }
}
