package com.launcher.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragmentHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);



        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings_home, rootKey);

            CheckBoxPreference defaultHomePreference = findPreference("Default_Home");
            assert defaultHomePreference != null;
            defaultHomePreference.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(Settings.ACTION_HOME_SETTINGS);
                startActivity(intent);
                return true;
            });
            // Get the package name of the app
            String packageName = requireActivity().getPackageName();

// Get the package name of the default launcher
            PackageManager packageManager = requireActivity().getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            ResolveInfo resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            String defaultLauncherPackage = resolveInfo.activityInfo.packageName;

// Update the checkbox preference based on the default launcher package
            if (packageName.equals(defaultLauncherPackage)) {
                defaultHomePreference.setChecked(true);
            } else {
                defaultHomePreference.setChecked(false);
            }


        }

    }

}