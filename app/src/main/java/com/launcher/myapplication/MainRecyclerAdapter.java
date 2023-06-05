package com.launcher.myapplication;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.launcher.myapplication.R;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    private ArrayList<String> list;

    public MainRecyclerAdapter(ArrayList<String> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView city;

        public ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.city.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
