package com.depth.behavior.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ScrollingView;
import androidx.viewpager.widget.ViewPager;


@CoordinatorLayout.DefaultBehavior(BehaviorViewPager.class)
public class PassthroughViewPager extends ViewPager {
    public PassthroughViewPager(@NonNull Context context) {
        super(context);
    }

    public PassthroughViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        View currentScrollerView = findCanScrollView(getCurrentScrollerView());
        if (currentScrollerView != null) {
            return currentScrollerView.canScrollVertically(direction);
        }
        return super.canScrollVertically(direction);
    }


    private View getCurrentScrollerView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view.getX() == getScrollX() + getPaddingLeft()) {
                return view;
            }
        }
        return this;
    }

    private View findCanScrollView(View view) {
        if (view == null) return null;

        if (view instanceof ScrollView) return view;
        if (view instanceof ScrollingView) return view;


        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View canScrollView = findCanScrollView(group.getChildAt(i));
                if (canScrollView != null) return canScrollView;
            }
        }
        return null;
    }

}
