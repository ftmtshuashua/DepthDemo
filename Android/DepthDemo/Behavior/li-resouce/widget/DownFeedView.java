package com.depth.behavior.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

@CoordinatorLayout.DefaultBehavior(BehaviorFeed.class)
public class DownFeedView  extends FrameLayout {
    public DownFeedView(@NonNull Context context) {
        super(context);
    }

    public DownFeedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DownFeedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
