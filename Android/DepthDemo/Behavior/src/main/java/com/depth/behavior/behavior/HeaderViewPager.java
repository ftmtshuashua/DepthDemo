package com.depth.behavior.behavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

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

    void init() {
    }


    //查询ViewPager中当前显示页面中的滚动View
    private OnFindScrollView mOnFindScrollView = view -> view instanceof ScrollView || view instanceof ScrollingView;

    @Override
    public boolean canScrollVertically(int direction) {
        View currentScrollerView = findCanScrollView(getCurrentShowView());
        if (currentScrollerView != null) {
            return currentScrollerView.canScrollVertically(direction);
        }
        return super.canScrollVertically(direction);
    }

    //ViewPager中当前显示的View
    private View getCurrentShowView() {
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
        if (mOnFindScrollView.onFinding(view)) return view;
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

    private View getCurrentScrollView() {
        return findCanScrollView(getCurrentShowView());
    }

    public void setOnFindScrollView(OnFindScrollView mOnFindScrollView) {
        this.mOnFindScrollView = mOnFindScrollView;
    }



    /*------------------------------ 保留的高度 ----------------------------------*/

    private int mKeepHeight = 0;

    public int getKeepHeight() {
        return mKeepHeight;
    }

    public void setKeepHeight(int height) {
        mKeepHeight = height;
        requestLayout();
    }

    // 吸顶设置
    private float mSuckTop = 3F / 4F;

    public void setSuckTop(float mSuckTop) {
        this.mSuckTop = MathUtils.clamp(mSuckTop, 0F, 1.0F);
    }

    //吸顶检查
    private void checkSuckTop() {
        if (mSuckTop > 0F) {
            int abs = Math.abs(mCurrentOffset);
            int scrollRange = getScrollRange();
            if (abs == scrollRange) return;
            float scal = abs / (float) scrollRange;

            if (scal >= mSuckTop) {
                scrollToBottom();
            }
        }
    }

    /*------------------------------ 滚动动画 ----------------------------------*/

    private ValueAnimator mValueAnimator;
    private AnimationCall<Integer> mValueCall;

    private void startAnimation(int start, int end, AnimationCall<Integer> call) {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(0, 0);
//            mValueAnimator.setDuration((long) (1000 * (1F - 0.618f)));
            mValueAnimator.setDuration((long) (1000 * 0.2));
            mValueAnimator.setRepeatCount(0);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.setInterpolator(new DecelerateInterpolator());
            mValueAnimator.addUpdateListener(animation -> {
                float animatedFraction = animation.getAnimatedFraction();
                int offset = (int) animation.getAnimatedValue();
                if (mValueCall != null) mValueCall.call(offset, animatedFraction);

            });

        }
        if (mValueAnimator.isRunning()) {
            return;
        }
        mValueCall = call;
        mValueAnimator.setIntValues(start, end);
        mValueAnimator.start();
    }

    //回到顶部
    public void scrollToTop() {
        HeaderViewBehavior behavior = BehaviorUtils.getBehavior(this);
        int start = behavior.getTopAndBottomOffset();
        int end = 0;
        startAnimation(start, end, (integer, fraction) -> {
            behavior.setTopAndBottomOffset(integer);
        });

        //滚动到顶部
        View mScrollView = getCurrentScrollView();
        if (mScrollView instanceof NestedScrollView) {
            ((NestedScrollView) mScrollView).fullScroll(ScrollView.FOCUS_UP);
        } else if (mScrollView instanceof ScrollView) {
            ((ScrollView) mScrollView).fullScroll(ScrollView.FOCUS_UP);
        } else {
            mScrollView.scrollTo(0, 0);
        }

    }

    //滚动到底部
    public void scrollToBottom() {
        HeaderViewBehavior behavior = BehaviorUtils.getBehavior(this);
        int start = behavior.getTopAndBottomOffset();
        int end = -behavior.getScrollRange(this);

        startAnimation(start, end, (integer, fraction) -> behavior.setTopAndBottomOffset(integer));
    }

    /*------------------------------ 偏移监听 ----------------------------------*/

    private List<OnViewChangeListener> mListeners = new ArrayList<>();

    public void addOnOffsetChangeListener(OnViewChangeListener l) {
        if (!mListeners.contains(l)) {
            mListeners.add(l);
            l.onChange(this, mCurrentOffset);
        }
    }

    public interface OnViewChangeListener {
        void onChange(HeaderViewPager view, int offset);
    }

    private int mCurrentOffset;

    //设置偏移量改变
    private void setOffsetChange(int topAndBottomOffset) {
        mCurrentOffset = topAndBottomOffset;
        for (int i = mListeners.size() - 1; i >= 0; i--) {
            mListeners.get(i).onChange(this, topAndBottomOffset);
        }

        checkSuckTop();
    }


    //滚动范围
    int getScrollRange() {
        return getMeasuredHeight() - getKeepHeight();
    }


    //------------------------------ 联动控制 ----------------------------------*/
    public static final class HeaderViewBehavior<V extends HeaderViewPager> extends ViewOffsetBehavior<V> {

        public HeaderViewBehavior() {
        }

        public HeaderViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        V mChild;

        @Override
        public boolean setTopAndBottomOffset(int offset) {
            if (mChild != null) {
                ((HeaderViewPager) mChild).setOffsetChange(offset);
            }
            return super.setTopAndBottomOffset(offset);
        }


        @Override
        public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
            return super.onTouchEvent(parent, child, ev);
        }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
            boolean isStarted = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
            boolean suckTop = !isScrolledTop(child); //吸顶
            return isStarted && suckTop;
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

            if (dyUnconsumed != 0) {
                consumed[1] = scroll(coordinatorLayout, child, dyUnconsumed, -getScrollRange(child), 0);
            }
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
                return ((HeaderViewPager) v).getScrollRange();
            }
            return v.getMeasuredHeight();
        }

        @Override
        public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            mChild = child;
            final CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            if (lp.height == CoordinatorLayout.LayoutParams.WRAP_CONTENT) {
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightUsed);
                return true;
            }
            return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }


        final int scroll(CoordinatorLayout coordinatorLayout, V header, int dy, int minOffset, int maxOffset) {
            if (isScrolledTop(header)) return 0;
            return setHeaderTopBottomOffset(coordinatorLayout, header, getTopAndBottomOffset() - dy, minOffset, maxOffset);
        }

        //判断是否已滚动到顶部了
        final boolean isScrolledTop(V child) {
            return -getScrollRange(child) == getTopAndBottomOffset();
        }

        //获得对象ID
        private String getId(Object obj) {
            return MessageFormat.format("{0}({1})", obj.getClass().getSimpleName(), Integer.toHexString(System.identityHashCode(this)));
        }

        private String get(int[] data) {
            return new Gson().toJson(data);
        }
    }


    public interface OnFindScrollView {
        boolean onFinding(View view);
    }


    @FunctionalInterface
    private interface AnimationCall<T> {
        void call(T t, float raction);
    }


}
