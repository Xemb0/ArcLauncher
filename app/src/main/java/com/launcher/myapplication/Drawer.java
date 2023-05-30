package com.launcher.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.util.Collections;
import java.util.List;

public class Drawer extends AppCompatActivity {

    private static final int DEFAULT_ICON_SPAN = 5;

    private int previousProgress = -1;
    public Drawer() {
    }

    public Drawer(int previousProgress) {
        this.previousProgress = previousProgress;
    }

    public Drawer(int contentLayoutId, int previousProgress) {
        super(contentLayoutId);
        this.previousProgress = previousProgress;
    }


    void AppDrawer(){
        Vibrate vibrate = new Vibrate();
        RecyclerView CircularDrawer = findViewById(R.id.recycalview);
        PackageManager pm = this.getPackageManager();

        Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(main,0);
        Collections.sort(apps,new ResolveInfo.DisplayNameComparator(pm));
        Adapter appAdapter = new Adapter(this, apps, pm);
        CircularDrawer.setAdapter(appAdapter);
        CircleLayoutManager circleLayoutManager = new CircleLayoutManager(this);
        CircularDrawer.setLayoutManager(circleLayoutManager);

        final BroadcastReceiver installUninstallBrodcastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                String packageName = intent.getData().getSchemeSpecificPart();
                if(Intent.ACTION_PACKAGE_ADDED.equals(action)){
                    appAdapter.refreshAppList();
                } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)){
                    appAdapter.refreshAppList();

                }
            }
        };


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(installUninstallBrodcastReciver,intentFilter);

        ArcSeekBar seekArc = findViewById(R.id.seekArc);
        int AppCount = appAdapter.getItemCount();
        seekArc.setMaxProgress(AppCount-1);

        TextView fisrtletter = findViewById(R.id.firstletter);
        View focusAppBackground = findViewById(R.id.Icon_shadow);
        fisrtletter.setVisibility(View.INVISIBLE);
        focusAppBackground.setVisibility(View.INVISIBLE);


        seekArc.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int progress) {
                if(progress!=previousProgress){
                    vibrate.vibrate();
                    previousProgress = progress;
                }
                circleLayoutManager.scrollToPosition(progress);
                fisrtletter.setVisibility(View.VISIBLE);
                focusAppBackground.setVisibility(View.VISIBLE);

                String packageName = apps.get(progress).activityInfo.packageName;
                String appName;
                try {
                    appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA)).toString();

                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String alphabet = String.valueOf(appName.charAt(0));
                fisrtletter.setText(alphabet);
            }
        });
        seekArc.setOnStopTrackingTouch(new ProgressListener() {
            @Override
            public void invoke(int i) {
                fisrtletter.setVisibility(View.INVISIBLE);
                focusAppBackground.setVisibility(View.INVISIBLE);
            }
        });

        int iconSpan = getIconSizeFromSharedPreferences();
        RecyclerView recyclerDrawer = findViewById(R.id.recycalDrawer);
        recyclerDrawer.setLayoutManager(new GridLayoutManager(this,iconSpan,RecyclerView.HORIZONTAL,false));
        recyclerDrawer.setAdapter(appAdapter);

    }

    private int getIconSizeFromSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("iconSpan", DEFAULT_ICON_SPAN);
    }
}
