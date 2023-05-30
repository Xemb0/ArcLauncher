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
    StackTransformer transformer;
    PaddingPageTransformer paddingPageTransformer;

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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                applyTransformation(position, positionOffsetPixels);
            }
        });

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




}

