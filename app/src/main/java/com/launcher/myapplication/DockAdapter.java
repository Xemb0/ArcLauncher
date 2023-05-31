package com.launcher.myapplication;

import android.content.Context;
import android.content.Intent;
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
                launchApp(appInfo.activityInfo.packageName);
            }
        });
    }

    private void launchApp(String packageName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        }
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
