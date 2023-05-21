package com.launcher.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class Gesture extends GestureDetector.SimpleOnGestureListener {

        private Context context;

        public Gesture(Context context) {
            this.context = context;
        }


        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event1.getY() < event2.getY()) {
                Intent intent = new Intent(context, HomeScreen.class);
                context.startActivity(intent);
            }
            else if (event1.getY() > event2.getY()) {
                // User swiped up
                Intent intent = new Intent(context, HomeScreen.class);
                context.startActivity(intent);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }
    }

