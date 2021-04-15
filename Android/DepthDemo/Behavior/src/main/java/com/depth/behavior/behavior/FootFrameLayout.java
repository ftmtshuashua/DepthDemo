package com.depth.behavior.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

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

    //测量和布置
    public static class FootMeasureBehavior<V extends View> extends ViewOffsetBehavior<V> {
        public FootMeasureBehavior() {
        }

        public FootMeasureBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
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

        @Override
        public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            final HeaderViewPager header = getHeadLayout(parent, child);
            if (header != null) {
                HeaderViewPager.HeaderViewBehavior behavior = BehaviorUtils.getBehavior(header);

                int height_total = parent.getMeasuredHeight();
                int height_header = header.getMeasuredHeight();
                int scroll_range = behavior.getScrollRange(header);
                int height = height_total - (height_header - scroll_range);
//                LogUtils.e(MessageFormat.format("{0,number,0} - ( {1,number,0} - {2,number,0} ) = {3,number,0}", height_total, height_header, scroll_range, height));

                final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.max(0, height), MeasureSpec.EXACTLY);
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


        @Override
        public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
            return dependency instanceof HeaderViewPager;
        }

        @Override
        public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull View dependency) {
            setTopAndBottomOffset(((ViewOffsetBehavior<View>) BehaviorUtils.getBehavior(dependency)).getTopAndBottomOffset());
            return false;
        }

    }

    public static final class FootViewBehavior<V extends View> extends FootMeasureBehavior<V> {

        public FootViewBehavior() {
        }

        public FootViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }


        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
            boolean isStarted = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
            return isStarted && directTargetChild instanceof FootFrameLayout;
        }

        @Override
        public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//            LogUtils.i(MessageFormat.format("onNestedPreScroll -> ( {0} , {1} , {2,number,0} , {3,number,0} , {4} , {5} )", getId(child), getId(target), dx, dy, get(consumed), type));
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
//            LogUtils.i(MessageFormat.format("onNestedScroll -> ( {0} , {1} , 已消耗({2,number,0},{3,number,0}) ,剩余({4,number,0},{5,number,0}) , {6} , {7} )", getId(child), getId(target), dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, get(consumed)));

        }

        @Override
        public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
//            LogUtils.e(MessageFormat.format("onNestedFling -> ( {0} , {1} , {2,number,0} , {3,number,0} , {4} )", getId(child), getId(target), velocityX, velocityY, consumed));
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
