package com.launcher.myapplication;//package com.launcher.myapplication;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Color;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.preference.PreferenceCategory;
//import androidx.preference.PreferenceViewHolder;
//
//import com.launcher.myapplication.R;
//
//public class MyPreference extends PreferenceCategory {
//
//    private int textColor;
//    private int backgroundColor;
//
//    public MyPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        initialize(context, attrs);
//    }
//
//    public MyPreference(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initialize(context, attrs);
//    }
//
//    public MyPreference(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initialize(context, attrs);
//    }
//
//    public MyPreference(Context context) {
//        super(context);
//        initialize(context, null);
//    }
//
//    private void initialize(Context context, AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyPreference);
//            textColor = typedArray.getResourceId(R.styleable.MyPreference_textColor, R.color.white);
//            backgroundColor = typedArray.getResourceId(R.styleable.MyPreference_backgroundColor, R.color.Transparent);
//            typedArray.recycle();
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(PreferenceViewHolder holder) {
//        super.onBindViewHolder(holder);
//        View itemView = holder.itemView;
////        itemView.setBackgroundColor(backgroundColor);
//
//        View titleView = itemView.findViewById(android.R.id.title);
//        if (titleView instanceof TextView) {
//            ((TextView) titleView).setTextColor(textColor);
//        }
//    }
//}
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import com.launcher.myapplication.R;

public class MyPreference extends PreferenceCategory {

    private int textColor;
    private int backgroundColor;

    public MyPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs);
    }

    public MyPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public MyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MyPreference(Context context) {
        super(context);
        initialize(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyPreference);
            textColor = typedArray.getResourceId(R.styleable.MyPreference_textColor,R.color.textColor);
            backgroundColor = typedArray.getColor(R.styleable.MyPreference_backgroundColor, Color.TRANSPARENT);
            typedArray.recycle();
        }
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View itemView = holder.itemView;
        itemView.setBackgroundColor(backgroundColor);

        TextView titleView = itemView.findViewById(android.R.id.title);
        if (titleView != null) {
            if (textColor != 0) {
                titleView.setTextColor(getContext().getResources().getColor(textColor));
            } else {
                titleView.setTextColor(Color.BLUE);
            }
        }
    }
}
