package com.launcher.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.launcher.myapplication.VerticalViewAdapter;

public class ActivityPager extends AppCompatActivity {

    private ViewPager2 viewPager;
    private VerticalViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.ViewPager);
        adapter = new VerticalViewAdapter();

        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                applyTransformation(position, positionOffset);
            }
        });

    }
//@Override
//    public void  onDestroy() {
//        super.onDestroy();
//        viewPager.unregisterOnPageChangeCallback();
//    }
    private void applyTransformation(int position, float positionOffset) {
        RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            float scaleFactor = Math.max(.9f, 1 - Math.abs(positionOffset));
            child.setScaleX(scaleFactor);
            child.setScaleY(scaleFactor);
        }
    }
}
