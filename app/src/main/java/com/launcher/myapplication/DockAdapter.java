package com.launcher.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DockAdapter extends RecyclerView.Adapter<DockAdapter.DockViewHolder> {

    private Context context;
    private List<ResolveInfo> appList;
    private Set<String> selectedApps;
    private SharedPreferences sharedPreferences;

    public DockAdapter(Context context, List<ResolveInfo> appList) {
        this.context = context;
        this.appList = appList;
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedApps = loadSelectedApps();
    }

    @NonNull
    @Override
    public DockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new DockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DockViewHolder holder, int position) {
        final ResolveInfo appInfo = appList.get(position);
        holder.iconImageView.setImageDrawable(appInfo.loadIcon(context.getPackageManager()));
        holder.labelTextView.setText(appInfo.loadLabel(context.getPackageManager()));

        final String packageName = appInfo.activityInfo.packageName;
        final boolean isClicked = selectedApps.contains(packageName);

        // Set scale based on clicked state
        float scale = isClicked ? 0.5f : 1f;
        holder.iconImageView.setScaleX(scale);
        holder.iconImageView.setScaleY(scale);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked) {
                    removeFromSharedPreferences();
                    selectedApps.remove(packageName);
                } else {
                    addToSharedPreferences(packageName);
                    saveSelectedApps();
                    selectedApps.add(packageName);
                }


                // Notify the adapter that the data has changed for this item
                notifyItemChanged(position);

            }
        });
    }

    private Set<String> loadSelectedApps() {
        return sharedPreferences.getStringSet("SelectedApps", new HashSet<String>());
    }

    private void saveSelectedApps() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("SelectedApps", selectedApps);
        editor.apply();
    }
    private void addToSharedPreferences(String packageName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("AppName", packageName);
        editor.apply();

        // Broadcast intent to notify the BroadcastReceiver about the app change event
        Intent intent = new Intent("com.launcher.myapplication.APP_CHANGE");
        context.sendBroadcast(intent);
    }

    private void removeFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("AppName");
        editor.apply();

        // Broadcast intent to notify the BroadcastReceiver about the app change event
        Intent intent = new Intent("com.launcher.myapplication.APP_CHANGE");
        context.sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class DockViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView labelTextView;

        DockViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.image);
            labelTextView = itemView.findViewById(R.id.label);
        }
    }
}
