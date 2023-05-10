package com.launcher.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {

    public  final int UNINSTALL_REQUEST_CODE = 1;
    public int position;
     List<ResolveInfo> lapps;
     Context context;
    PackageManager pm;

    public Adapter(Context context, List<ResolveInfo> apps, PackageManager pn) {
        this.context = context;
        lapps=apps;
        pm=pn;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        holder.images.setImageDrawable(lapps.get(position).loadIcon(pm));
        holder.text.setText(lapps.get(position).loadLabel(pm));

        holder.itemlayout.setOnClickListener(view -> {
            ResolveInfo launchable = lapps.get(position);
            ActivityInfo activity = launchable.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName,activity.name);
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);
            context.startActivity(i);
        });
        ResolveInfo resolveInfo = lapps.get(position);
        holder.itemlayout.setOnLongClickListener(view -> {
            // Show the popup window for this app
            showPopupWindowForApp(resolveInfo, view,pm);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lapps.size();
    }
    public void refreshAppList() {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);

        // Sort activities by label
        Collections.sort(activities, (a, b) -> a.loadLabel(pm).toString().compareToIgnoreCase(b.loadLabel(pm).toString()));

        lapps = activities;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        TextView text;
        RelativeLayout itemlayout;
        public ViewHolder(View view) {
            super(view);
            images = view.findViewById(R.id.image);
            text = view.findViewById(R.id.label);
            itemlayout=view.findViewById(R.id.item);

        }
    }
    private void showPopupWindowForApp(ResolveInfo resolveInfo, View anchorView,PackageManager pm) {
        // Create a new popup window
        PopupWindow popupWindow = new PopupWindow(context);

        // Set the content view of the popup window
        View popupView = LayoutInflater.from(context).inflate(R.layout.app_popup, null);
        popupWindow.setContentView(popupView);


        Button Appinfo = popupView.findViewById(R.id.button_app_info);
        Button Uninstall = popupView.findViewById(R.id.button_uninstall_app);
        Appinfo.setOnClickListener(v -> {  String packageName = resolveInfo.activityInfo.packageName;

            // Create an intent to show the app info screen
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));

            // Launch the app info screen
            context.startActivity(intent);
            popupWindow.dismiss();



        });

        //pop uninstall when the uninstall button is clicked
        Uninstall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            intent.setData(Uri.parse("package:" + resolveInfo.activityInfo.packageName));
            context.startActivity(intent);

            popupWindow.dismiss();
            position = lapps.indexOf(resolveInfo);

        });




        // Set the size of the popup window
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // Make the popup window dismiss when the user taps outside of it or presses the back button
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        // Show the popup window at the location of the anchor view
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[1], location[1]);
    }

}
