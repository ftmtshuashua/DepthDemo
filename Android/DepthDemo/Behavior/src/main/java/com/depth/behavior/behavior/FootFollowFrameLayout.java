package com.depth.behavior.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * 底部跟随FrameLayout
 */
@CoordinatorLayout.DefaultBehavior(FootFrameLayout.FootMeasureBehavior.class)
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


}
