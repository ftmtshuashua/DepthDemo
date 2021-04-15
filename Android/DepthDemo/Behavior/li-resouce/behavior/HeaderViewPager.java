package com.depth.behavior.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import com.depth.behavior.utils.LogUtils;

import java.text.MessageFormat;

/**
 * CoordinatorLayout中的脚部View
 */
@CoordinatorLayout.DefaultBehavior(HeaderViewPager.HeaderViewBehavior.class)
public class HeaderViewPager extends ViewPager {
    public HeaderViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public HeaderViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
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


    void init() {
        post(() -> {
            setKeepHeight(100);
        });
    }

    private int mKeepHeight = 0;

    //保留的高度
    public int getKeepHeight() {
        return mKeepHeight;
    }


    public void setKeepHeight(int height) {
        mKeepHeight = height;
        requestLayout();
    }


    public static final class HeaderViewBehavior<V extends HeaderViewPager> extends HeaderBehavior<V> {

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
            boolean isStarted = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;

//            if (isStarted) {
//                if (!child.canScrollVertically(1)) return false;
//            }

            return isStarted;
        }

        @Override
        public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
            if (dy != 0) {
                int min = -getScrollRange(child);
                int max = 0;
                if (child.getTop() != 0) {
                    consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
                } else {
                    boolean canDown = child.canScrollVertically(1);
                    if (!canDown) {
                        consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
                    }
                }
            }
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
            LogUtils.e(MessageFormat.format("剩余：{0}", dyUnconsumed));

            if (dyUnconsumed < 0) {
                // 如果滚动视图向下滚动，但没有消耗，它可能在内容的顶部
                consumed[1] = scroll(coordinatorLayout, child, dyUnconsumed, -getScrollRange(child), 0);
            }
        }

        @Override
        public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
            LogUtils.e("滚动停止");
        }

        @Override
        public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, float velocityX, float velocityY) {
            
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        }

        int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset, int minOffset, int maxOffset) {
            final int curOffset = getTopAndBottomOffset();
            int consumed = 0;
            if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
                // If we have some scrolling range, and we're currently within the min and max
                // offsets, calculate a new offset
                newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);

                if (curOffset != newOffset) {
                    setTopAndBottomOffset(newOffset);
                    // Update how much dy we have consumed
                    consumed = curOffset - newOffset;
                }
            }
            return consumed;
        }

        /*获得滚动范围*/
        int getScrollRange(@NonNull View v) {
            if (v instanceof HeaderViewPager) {
                return v.getMeasuredHeight() - ((HeaderViewPager) v).getKeepHeight();
            }
            return v.getMeasuredHeight();
        }


        @Override
        public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            final CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (lp.height == CoordinatorLayout.LayoutParams.WRAP_CONTENT) {
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightUsed);
                return true;
            }
            return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }

    }

}
