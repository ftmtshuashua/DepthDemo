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
 * 底部浮动按钮
 */
@CoordinatorLayout.DefaultBehavior(BottomFlowFrameLayout.BottomFlowFrameLayoutBehavior.class)
public class BottomFlowFrameLayout extends FrameLayout {

    public BottomFlowFrameLayout(@NonNull Context context) {
        super(context);
    }

    public BottomFlowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomFlowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //显示和隐藏
//    private void setShow(boolean isShow) {
//
//    }


    public static class BottomFlowFrameLayoutBehavior extends CoordinatorLayout.Behavior<BottomFlowFrameLayout> {
        public BottomFlowFrameLayoutBehavior() {
        }

        public BottomFlowFrameLayoutBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull BottomFlowFrameLayout child, int layoutDirection) {
            return super.onLayoutChild(parent, child, layoutDirection);
        }

        @Override
        public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull BottomFlowFrameLayout child, @NonNull View dependency) {
            return dependency instanceof DownFeedView;
        }

        @Override
        public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull BottomFlowFrameLayout child, @NonNull View dependency) {
            LogUtils.e("onDependentViewChanged:{0}" + dependency);

            int tran_max = dependency.getHeight() - child.getHeight();
            child.setTranslationY(tran_max + dependency.getTranslationY());
            return true;
        }
    }


}
