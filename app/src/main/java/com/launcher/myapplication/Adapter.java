package com.launcher.myapplication;

import android.animation.Animator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Point;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import androidx.core.content.res.ResourcesCompat;
import java.util.Collections;
import java.util.List;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static java.lang.Math.hypot;
import static java.lang.Math.max;

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
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            wm.getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;



            View rootView = ((Activity) holder.itemlayout.getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
            int cx = rootView.getWidth() / 2;
            int cy = rootView.getHeight() / 2;
            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = null;
            anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0, finalRadius);

            anim.setDuration(200);
            anim.start();


            // Start the app activity after the animation has finished
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    context.startActivity(i);

                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });


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

        popupWindow.setBackgroundDrawable(null);
        ImageButton Appinfo = popupView.findViewById(R.id.app_info_button);
        ImageButton Uninstall = popupView.findViewById(R.id.Uninstall_button);
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
        int verticalOffset = 100;
        // Show the popup window at the location of the anchor view
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0], location[1]-verticalOffset);
    }

}
