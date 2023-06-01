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

import java.util.List;

public class DockAdapter extends RecyclerView.Adapter<DockAdapter.DockViewHolder> {

    private Context context;
    private List<ResolveInfo> appList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public DockAdapter(Context context, List<ResolveInfo> appList) {
        this.context = context;
        this.appList = appList;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Set initial scale
                final ResolveInfo appInfo = appList.get(position);
                final String packageName = appInfo.activityInfo.packageName;




                    holder.iconImageView.setScaleX(0.5f);
                    holder.iconImageView.setScaleY(0.5f);

// Set pivot point
                    holder.iconImageView.setPivotX(holder.iconImageView.getWidth() / 2f);
                    holder.iconImageView.setPivotY(holder.iconImageView.getHeight() / 2f);

// Create and start the animation
                    holder.iconImageView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .start();


               addToSharedPreferences(packageName);

                onBindViewHolder(holder,position);

                }

        });
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



    private  void refresh(){
        notifyDataSetChanged();
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
