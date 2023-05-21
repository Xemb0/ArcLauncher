package com.launcher.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class HomeScreenArcSettings extends HomeScreen {


    SwitchMaterial DarkmodeSwitch;
    SwitchMaterial Switch_Clock;
    boolean darkMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sEditor;

    TextClock clockView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Switch_Clock = findViewById(R.id.Arc_clock_switch);
        clockView = findViewById(R.id.clock);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkMode = sharedPreferences.getBoolean("visible", false);

        if (darkMode) {
            DarkmodeSwitch.setChecked(false);
        }

        Switch_Clock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    showClock();
                    sEditor = sharedPreferences.edit();
                    sEditor.putBoolean("visible", true);
                } else {
                        hideClock();
                    sEditor = sharedPreferences.edit();
                    sEditor.putBoolean("visible", false);
                }
                sEditor.apply(); // Apply the changes to SharedPreferences
            }
        });



    }

    private void hideClock() {
        // Hide the clock view
        if (clockView != null) {
            clockView.setVisibility(View.GONE);
        }
    }

    private void showClock() {
        // Show the clock view
        if (clockView != null) {
            clockView.setVisibility(View.VISIBLE);
        }
    }
}
