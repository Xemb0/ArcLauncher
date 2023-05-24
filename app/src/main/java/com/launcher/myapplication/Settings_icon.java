package com.launcher.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Settings_icon extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_app);

        ImageView icon = findViewById(R.id.image);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int size = sharedPreferences.getInt("iconSize",0);

        if (size != 0) {
            ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
            layoutParams.width = size;
            layoutParams.height = size;
            icon.setLayoutParams(layoutParams);
        }
    }
}
