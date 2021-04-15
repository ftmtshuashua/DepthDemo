package com.depth.behavior.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.depth.behavior.utils.LogUtils;

/**
 * 浮动在顶部的View
 */
@CoordinatorLayout.DefaultBehavior(TopFloatView.TopFloatViewBehavior.class)
public class TopFloatView extends FrameLayout {
    public TopFloatView(@NonNull Context context) {
        super(context);
    }

    public TopFloatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TopFloatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    static class TopFloatViewBehavior extends CoordinatorLayout.Behavior<View> {
        @Override
        public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
            if (dependency instanceof PassthroughViewPager) {
                return true;
            }
            return super.layoutDependsOn(parent, child, dependency);
        }

        @Override
        public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {

            LogUtils.e("绑定的View变化:" + dependency.getTranslationY());

            return super.onDependentViewChanged(parent, child, dependency);
        }
    }

}
