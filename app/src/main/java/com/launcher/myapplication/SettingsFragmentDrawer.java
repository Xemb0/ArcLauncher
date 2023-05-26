package com.launcher.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class SettingsFragmentDrawer extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();


        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        SharedPreferences sharedPreferences;
        SharedPreferences.Editor sEditor;
        Boolean size;
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);





    }


    public static class SettingsFragment extends Fragment {
        private int iconSize;
        private int seekBarProgress;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;

        private int DEFAULT_VERTICAL_SPAN=5;
        private int DEFAULT_HORIZONTAL_SPAN=6;


        private int numCol;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




            sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            View rootView = inflater.inflate(R.layout.settings_drawer, container, false);

            Button drawerPopup = rootView.findViewById(R.id.drawer_layout);

            View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_drawer_layout, null);

            PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            drawerPopup.setOnClickListener(v -> {
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            });



            NumberPicker numPickerVertical = popupView.findViewById(R.id.numpickvertical);
            NumberPicker numPickerHorizontal = popupView.findViewById(R.id.numpickhorizotal);
            Button doneButton = popupView.findViewById(R.id.drawer_popup_done);


            GridView gridView = popupView.findViewById(R.id.drawerGrid_layout);
            int prevNumPickerVerticalValue = sharedPreferences.getInt("iconVertical", DEFAULT_VERTICAL_SPAN);
            int prevNumPickerHorizontalValue = sharedPreferences.getInt("iconHorizontal", DEFAULT_HORIZONTAL_SPAN);
            numPickerVertical.setValue(prevNumPickerVerticalValue);
            numPickerHorizontal.setValue(prevNumPickerHorizontalValue);
            gridView.setNumColumns(numPickerHorizontal.getValue());

            numPickerVertical.setOnValueChangedListener((picker, oldVal, newVal) -> {
                updateGridView(gridView, numPickerVertical.getValue(), numPickerHorizontal.getValue());
            });

            numPickerHorizontal.setOnValueChangedListener((picker, oldVal, newVal) -> {
                gridView.setNumColumns(numPickerHorizontal.getValue());
                updateGridView(gridView, numPickerVertical.getValue(), numPickerHorizontal.getValue());
            });



            doneButton.setOnClickListener(v -> {
                editor.putInt("iconHorizontal", numPickerHorizontal.getValue());
                editor.putInt("iconVertical", numPickerVertical.getValue());
                editor.apply();
                popupWindow.dismiss();
            });

            // Set initial grid size
            updateGridView(gridView, numPickerVertical.getValue(), numPickerHorizontal.getValue());

// Set a click listener on the content view of the popup window to dismiss it when tapped anywhere inside
            popupView.setOnClickListener(v -> {
                popupWindow.dismiss();
            });



            HomeWatcher mHomeWatcher = new HomeWatcher(requireContext());
            mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
                @Override
                public void onHomePressed() {
                    Intent intent = new Intent(requireContext(), ActivityHome.class);
                    startActivity(intent);

                }
                @Override
                public void onHomeLongPressed() {
                    Intent intent = new Intent(requireContext(), ActivityHome.class);
                    startActivity(intent);

                    // Finish the current activity

                }
            });








            ArcSeekBar seekBar = rootView.findViewById(R.id.IconseekArc);
            LinearLayout rootViewLayout = rootView.findViewById(R.id.rootlayout);
            ImageView icon1 = rootView.findViewById(R.id.icon1);
            ImageView icon2 = rootView.findViewById(R.id.icon2);
            ImageView icon3 = rootView.findViewById(R.id.icon3);
            ImageView icon4 = rootView.findViewById(R.id.icon4);

            iconSize = sharedPreferences.getInt("iconSize", 0);
            seekBarProgress = sharedPreferences.getInt("seekBarProgress", 10);


            if (iconSize != 0) {
                ViewGroup.LayoutParams layoutParams = icon1.getLayoutParams();
                layoutParams.width = iconSize;
                layoutParams.height = iconSize;
                icon1.setLayoutParams(layoutParams);
                icon2.setLayoutParams(layoutParams);
                icon3.setLayoutParams(layoutParams);
                icon4.setLayoutParams(layoutParams);
            }

            seekBar.setProgress(seekBarProgress);
            seekBar.setMaxProgress(100);

            seekBar.setOnProgressChangedListener(new ProgressListener() {
                @Override
                public void invoke(int i) {
                    // Update the size of the icon
                    ViewGroup.LayoutParams layoutParams = icon1.getLayoutParams();
                    layoutParams.width = i + 110;
                    layoutParams.height = i + 110;
                    icon1.setLayoutParams(layoutParams);
                    icon2.setLayoutParams(layoutParams);
                    icon3.setLayoutParams(layoutParams);
                    icon4.setLayoutParams(layoutParams);

                    editor.putInt("iconSize", i + 110);
                    editor.putInt("seekBarProgress", i);
                    editor.apply();


                }
            });

            seekBar.setOnTouchListener((view, motionEvent) -> {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    // Disable touch events for other views
                    rootViewLayout.requestDisallowInterceptTouchEvent(true);
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    // Enable touch events for other views
                    rootViewLayout.requestDisallowInterceptTouchEvent(false);
                }
                // Return false to allow the ArcSeekBar to handle its own touch events
                return false;
            });

            return rootView;
        }

    }

    @Override
    public void onBackPressed() {
    }

    private static void updateGridView(GridView gridView, int verticalValue, int horizontalValue) {
        int itemCount = verticalValue * horizontalValue;
        AdapterDrawerPopupLayout adapter = new AdapterDrawerPopupLayout(gridView.getContext(), itemCount);
        gridView.setAdapter(adapter);
    }
}




