package com.depth.behavior.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.depth.behavior.utils.LogUtils;
import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.List;

/**
 * CoordinatorLayout中的脚部View
 */
@CoordinatorLayout.DefaultBehavior(FootFrameLayout.FootViewBehavior.class)
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

    //测量与跟随
    public static class FootViewMeasureAndFollow<V extends View> extends ViewOffsetBehavior<V> {
        public FootViewMeasureAndFollow() {
        }

        public FootViewMeasureAndFollow(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            final HeaderViewPager header = getHeadLayout(parent, child);
            if (header != null) {
                HeaderViewPager.HeaderViewBehavior headLayoutBehavior = getHeadLayoutBehavior(header);
                int height = parent.getMeasuredHeight() - (header.getHeight() - headLayoutBehavior.getScrollRange(header));

                final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
                return true;
            }
            return false;
        }

        @Override
        protected void layoutChild(@NonNull CoordinatorLayout parent, @NonNull V child, int layoutDirection) {
            super.layoutChild(parent, child, layoutDirection);
            child.setTranslationY(getHeadLayout(parent, child).getMeasuredHeight());
        }

        HeaderViewPager getHeadLayout(CoordinatorLayout parent, V child) {
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
            return BehaviorUtils.getBehavior(abl);
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
        public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
            return dependency instanceof HeaderViewPager;
        }

        @Override
        public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
            int headerLayoutOffset = getHeaderLayoutOffset((HeaderViewPager) dependency);
            setTopAndBottomOffset(headerLayoutOffset);
            return false;
        }


    }

    //底部View动作
    public static final class FootViewBehavior extends FootViewMeasureAndFollow<View> {

        public FootViewBehavior() {
        }

        public FootViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
            boolean isStarted = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
            return isStarted && directTargetChild instanceof FootFrameLayout;
//            return isStarted ;
        }

        @Override
        public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
            LogUtils.i(MessageFormat.format("onNestedPreScroll -> ( {0} , {1} , {2,number,0} , {3,number,0} , {4} , {5} )", getId(child), getId(target), dx, dy, get(consumed), type));
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
            LogUtils.i(MessageFormat.format("onNestedScroll -> ( {0} , {1} , 已消耗({2,number,0},{3,number,0}) ,剩余({4,number,0},{5,number,0}) , {6} , {7} )", getId(child), getId(target), dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, get(consumed)));
//            consumed[1] = dyUnconsumed;
        }

        @Override
        public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
            LogUtils.e(MessageFormat.format("onNestedFling -> ( {0} , {1} , {2,number,0} , {3,number,0} , {4} )", getId(child), getId(target), velocityX, velocityY, consumed));

            return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        }

        //获得对象ID
        private String getId(Object obj) {
            return MessageFormat.format("{0}({1})", obj.getClass().getSimpleName(), Integer.toHexString(System.identityHashCode(this)));
        }

        private String get(int[] data) {
            return new Gson().toJson(data);
        }
    }


}
