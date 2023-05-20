package com.launcher.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ArcSettingsActivity extends AppCompatActivity {

    SwitchMaterial DarkmodeSwitch;
    SwitchMaterial Switch_Clock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arc_settings);
        DarkmodeSwitch = findViewById(R.id.darkmode);
        Switch_Clock = findViewById(R.id.Arc_clock_switch);

        DarkmodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        Switch_Clock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                  showClock();
                }
                else
                    hideClock();
            }
        });

    }
    private void hideClock() {
        // Hide the clock view
        LinearLayout clockView = findViewById(R.id.clock_date);
        if (clockView != null) {
            clockView.setVisibility(View.GONE);
        }
    }

    private void showClock() {
        // Show the clock view
        LinearLayout clockView = findViewById(R.id.clock_date);
        if (clockView != null)
            clockView.setVisibility(View.VISIBLE);
        }
    }