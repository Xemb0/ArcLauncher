package com.launcher.myapplication;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fragmentStart();
    }

    private void fragmentStart() {
        FragmentManager manager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.afragment, new HomeFragment(), "YOUR_FRAGMENT_STRING_TAG");
        transaction.commit();
    }
}

