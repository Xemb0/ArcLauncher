package com.launcher.myapplication;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private NestedScrollView nsd;
    private TabLayout tabLayout;
    private TabLayout newTabLayout;
    private final List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newTabLayout = view.findViewById(R.id.new_tabs);
        tabLayout = view.findViewById(R.id.tabs);
        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);
        nsd = view.findViewById(R.id.nested_id);

        fragments.add(new _2AppDrawer());

        FragmentActivity fragmentActivity = requireActivity();
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentActivity, fragments);
        viewPager2.setAdapter(adapter);

        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText("Tab " + (position + 1)));
        mediator.attach();

        TabLayoutMediator newMediator = new TabLayoutMediator(newTabLayout, viewPager2,
                (tab, position) -> tab.setText("Tab " + (position + 1)));
        newMediator.attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View view1 = fragments.get(position).getView();
                if (view1 != null) {
                    view1.post(() -> {
                        int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view1.getWidth(), View.MeasureSpec.EXACTLY);
                        int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        view1.measure(wMeasureSpec, hMeasureSpec);

                        ViewGroup.LayoutParams layoutParams = viewPager2.getLayoutParams();
                        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
                            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) layoutParams;
                            if (lp.height != view1.getMeasuredHeight()) {
                                lp.height = view1.getMeasuredHeight();
                                viewPager2.setLayoutParams(lp);
                            }
                        }
                    });
                }
            }
        });

        hideShowNewTabLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideShowNewTabLayout() {
        boolean isVisible = isViewVisible(nsd, tabLayout);
        newTabLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        nsd.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean isVisible = isViewVisible(nsd, tabLayout);
                newTabLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private boolean isViewVisible(NestedScrollView scrollView, View view) {
        Rect scrollBounds = new Rect();
        scrollView.getDrawingRect(scrollBounds);
        float top = view.getY();
        return scrollBounds.top > top;
    }

}
