package com.depth.behavior.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * 跟随 FootFrameLayout 变化
 */
@CoordinatorLayout.DefaultBehavior(FootFollowFrameLayout.FootFollowFrameLayoutBehavior.class)
public class FootFollowFrameLayout extends FrameLayout {
    public FootFollowFrameLayout(@NonNull Context context) {
        super(context);
    }

    public FootFollowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FootFollowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static class FootFollowFrameLayoutBehavior extends FootFrameLayout.FootViewMeasureAndFollow<View> {
        public FootFollowFrameLayoutBehavior() {
        }

        public FootFollowFrameLayoutBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
            return false;
        }
    }
}
