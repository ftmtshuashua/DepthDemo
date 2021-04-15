package com.depth.behavior.behavior;

import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class BehaviorUtils {

    public static final <T extends CoordinatorLayout.Behavior> T getBehavior(View v) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams cparams = (CoordinatorLayout.LayoutParams) layoutParams;
            CoordinatorLayout.Behavior behavior = cparams.getBehavior();
            return (T) behavior;
        }
        return null;
    }
}
