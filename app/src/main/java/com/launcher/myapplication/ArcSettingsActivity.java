package com.launcher.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ArcSettingsActivity extends AppCompatActivity {

    SwitchMaterial DarkmodeSwitch;
    SwitchMaterial Switch_Clock;
    boolean darkMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sEditor;

    LinearLayout clockView;

    private LayoutInflater inflater;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arc_settings);


        inflater = LayoutInflater.from(this);


        // Call onOptionSelected with the initial selected option
        onOptionSelected(findViewById(R.id.option_parent_settings));






        DarkmodeSwitch = findViewById(R.id.darkmode);
        Switch_Clock = findViewById(R.id.Arc_clock_switch);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkMode = sharedPreferences.getBoolean("dark", true);

        if (darkMode) {
            DarkmodeSwitch.setChecked(true);
        }

        DarkmodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sEditor = sharedPreferences.edit();
                    sEditor.putBoolean("dark", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sEditor = sharedPreferences.edit();
                    sEditor.putBoolean("dark", false);
                }
                sEditor.apply(); // Apply the changes to SharedPreferences
            }
        });









    }
    public void onOptionSelected(View view) {
        final int homeOptionId = R.id.home_setting_option;
        final int gestureOptionId = R.id.gesture_setting_option;
        final int drawerOptionId = R.id.drawer_icon_setting_option;
        final int updatesOptionId = R.id.update_and_info_setting_option;

        int optionId = view.getId();
        int layoutId;

        switch (optionId) {
            case homeOptionId:
                layoutId = R.layout.setting_home;
                break;
            case gestureOptionId:
                layoutId = R.layout.gestures_setting;
                break;
            case drawerOptionId:
                layoutId = R.layout.setting_drawer;
                break;
            case updatesOptionId:
                layoutId = R.layout.setting_updat_info;
                break;
            default:
                return;
        }

        // Inflate the selected option layout
        View optionLayout = inflater.inflate(layoutId, null);
        setContentView(optionLayout);
    }




}
