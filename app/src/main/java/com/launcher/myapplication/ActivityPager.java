package com.launcher.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class ActivityPager extends AppCompatActivity {
    private int previousProgress = -1;
    public  int DEFAULT_ICON_SPAN = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.ViewPager);
        SnapHelper snapHelper = new PagerSnapHelper(); // or any other SnapHelper you prefer
        snapHelper.attachToRecyclerView(recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        VerticalViewAdapter adapter = new VerticalViewAdapter();
        recyclerView.setAdapter(adapter);


    }


}
