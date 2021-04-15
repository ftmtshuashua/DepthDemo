package com.depth.behavior.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.util.List;

/**
 * CoordinatorLayout中的脚部View
 */
@CoordinatorLayout.DefaultBehavior(FootFrameLayout.FootViewBehavior.class)
//public class FootBehaviorLayout extends FrameLayout implements NestedScrollingParent3, NestedScrollingChild3 , ScrollingView {
public class FootFrameLayout extends FrameLayout {
    public FootFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public FootFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FootFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public static final class FootViewBehavior extends ViewOffsetBehavior<View> {

        public FootViewBehavior() {
        }

        public FootViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            final HeaderViewPager header = getHeadLayout(parent, child);
            if (header != null) {
                HeaderViewPager.HeaderViewBehavior headLayoutBehavior = getHeadLayoutBehavior(header);
                int height = parent.getMeasuredHeight() - (header.getHeight() - headLayoutBehavior.getScrollRange(header));

                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                return true;
            }
            return false;
        }

        @Override
        protected void layoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
            super.layoutChild(parent, child, layoutDirection);
            child.setTranslationY(getHeadLayout(parent, child).getMeasuredHeight());
        }


        HeaderViewPager getHeadLayout(CoordinatorLayout parent, View child) {
            @NonNull List<View> views = parent.getDependencies(child);
            for (int i = 0, z = views.size(); i < z; i++) {
                View view = views.get(i);
                if (view instanceof HeaderViewPager) {
                    return (HeaderViewPager) view;
                }
            }
            return null;
        }

        HeaderViewPager.HeaderViewBehavior getHeadLayoutBehavior(HeaderViewPager abl) {
            final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) abl.getLayoutParams()).getBehavior();
            if (behavior instanceof HeaderViewPager.HeaderViewBehavior) {
                return (HeaderViewPager.HeaderViewBehavior) behavior;
            }
            return null;
        }

        //获得HeaderLayout的偏移量
        private int getHeaderLayoutOffset(@NonNull HeaderViewPager abl) {
            HeaderViewPager.HeaderViewBehavior headLayoutBehavior = getHeadLayoutBehavior(abl);
            if (headLayoutBehavior != null) {
                return headLayoutBehavior.getTopAndBottomOffset();
            }
            return 0;
        }

        @Override
        public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
            return dependency instanceof HeaderViewPager;
        }

        @Override
        public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
            int headerLayoutOffset = getHeaderLayoutOffset((HeaderViewPager) dependency);
            setTopAndBottomOffset(headerLayoutOffset);
            return false;
        }




    }


}
