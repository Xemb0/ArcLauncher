package com.launcher.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ArcSettingsActivity extends AppCompatActivity {

    SwitchMaterial DarkmodeSwitch;
    SwitchMaterial Switch_Clock;
    boolean darkMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sEditor;

    LinearLayout clockView;
private boolean arcSetting;
    private LayoutInflater inflater;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arc_settings);


        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SettingsFragmentHome.SettingsFragment());
        fragments.add(new SettingsFragmentDrawer.SettingsFragment());
        fragments.add(new SettingsFragmentGesture.SettingsFragment());
        fragments.add(new SetttingsFragmentUpdatInfo.MyPreferenceFragment());

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);

        // Attach the ViewPager to the TabLayout
        tabLayout.setupWithViewPager(viewPager);

        // Set the titles for the tabs (options)
        tabLayout.getTabAt(0).setText("HOME").setIcon(R.drawable.home_icon_settings);
        tabLayout.getTabAt(1).setText("DRAWER").setIcon(R.drawable.drawer_icon);
        tabLayout.getTabAt(2).setText("GESTURES").setIcon(R.drawable.gesture_icon);
        tabLayout.getTabAt(3).setText("UPDATES & INFO").setIcon(R.drawable.update_and_info);


         class MyPagerAdapter extends FragmentStateAdapter {
            private List<Fragment> fragments;

            public MyPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
                super(fragmentManager, lifecycle);
                this.fragments = fragments;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        }

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


}
