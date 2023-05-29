package com.launcher.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_VERTICAL_VIEW_1 = 0;
    private static final int VIEW_TYPE_VERTICAL_VIEW_2 = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_VERTICAL_VIEW_1:
                View view1 = inflater.inflate(R.layout.circular_drawer, parent, false);
                return new VerticalView1ViewHolder(view1, parent.getContext());
            case VIEW_TYPE_VERTICAL_VIEW_2:
                View view2 = inflater.inflate(R.layout.drawer, parent, false);
                return new VerticalView2ViewHolder(view2, parent.getContext());
            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind data to views based on the view type
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_VERTICAL_VIEW_1:
                // Bind data for Vertical View 1
                break;
            case VIEW_TYPE_VERTICAL_VIEW_2:
                // Bind data for Vertical View 2
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Total number of vertical views
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the view type based on position
        switch (position) {
            case 0:
                return VIEW_TYPE_VERTICAL_VIEW_1;
            case 1:
                return VIEW_TYPE_VERTICAL_VIEW_2;
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

}
