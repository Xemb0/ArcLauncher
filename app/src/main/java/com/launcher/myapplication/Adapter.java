package com.launcher.myapplication;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.launcher.myapplication.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<ResolveInfo> lapps;
    Context context;
    PackageManager pm=null;
    public Adapter(Context context, List<ResolveInfo> apps, PackageManager pn) {
        this.context = context;
        lapps=apps;
        pm=pn;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.images.setImageDrawable(lapps.get(position).loadIcon(pm));
        holder.text.setText(lapps.get(position).loadLabel(pm));
        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResolveInfo launchable = lapps.get(position);
                ActivityInfo activity = launchable.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                        activity.name);
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                i.setComponent(name);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lapps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView text;
        RelativeLayout itemlayout;
        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.label);
            itemlayout=view.findViewById(R.id.item);

        }
    }
}
