package com.depth.behavior.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

import com.depth.behavior.behavior.lear.HeaderScrollingViewBehavior;
import com.depth.behavior.utils.LogUtils;

import java.text.MessageFormat;


/**
 * 这个 {@link CoordinatorLayout.Behavior} 用于垂直位于滚动视图上方的视图. See {@link HeaderScrollingViewBehavior}.
 */
public abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V> {

    private static final int INVALID_POINTER = -1;

    @Nullable
    private Runnable flingRunnable;                 //抛出
    OverScroller scroller;

    private boolean isBeingDragged;                 //开始拖拽
    private int activePointerId = INVALID_POINTER;
    private int lastMotionY;
    private int touchSlop = -1;
    private VelocityTracker velocityTracker;        //速度跟踪器

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        if (touchSlop < 0) {
            touchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }

        final int action = ev.getAction();

        // 这是快捷方式，因为我们被拖动了
        if (action == MotionEvent.ACTION_MOVE && isBeingDragged) {
            return true;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                isBeingDragged = false;
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                if (canDragView(child) && parent.isPointInChildBounds(child, x, y)) {
                    lastMotionY = y;
                    this.activePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = this.activePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // 如果我们没有一个有效的id, 这个触摸点击不在内容上
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                final int y = (int) ev.getY(pointerIndex);
                final int yDiff = Math.abs(y - lastMotionY);
                if (yDiff > touchSlop) {
                    isBeingDragged = true;
                    lastMotionY = y;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                isBeingDragged = false;
                this.activePointerId = INVALID_POINTER;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            }
        }

        if (velocityTracker != null) {
            velocityTracker.addMovement(ev);
        }

        return isBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        if (touchSlop < 0) {
            touchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();

                if (parent.isPointInChildBounds(child, x, y) && canDragView(child)) {
                    lastMotionY = y;
                    activePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();
                } else {
                    return false;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = ev.findPointerIndex(activePointerId);
                if (activePointerIndex == -1) {
                    return false;
                }

                final int y = (int) ev.getY(activePointerIndex);
                int dy = lastMotionY - y;

                if (!isBeingDragged && Math.abs(dy) > touchSlop) {
                    isBeingDragged = true;
                    if (dy > 0) {
                        dy -= touchSlop;
                    } else {
                        dy += touchSlop;
                    }
                }

                if (isBeingDragged) {
                    lastMotionY = y;
                    // We're being dragged so scroll the ABL
                    scroll(parent, child, dy, getMaxDragOffset(child), 0);
                }
                break;
            }

            case MotionEvent.ACTION_UP:
                if (velocityTracker != null) {
                    velocityTracker.addMovement(ev);
                    velocityTracker.computeCurrentVelocity(1000);
                    float yvel = velocityTracker.getYVelocity(activePointerId);
                    fling(parent, child, -getScrollRangeForDragFling(child), 0, yvel);
                }
                // $FALLTHROUGH
            case MotionEvent.ACTION_CANCEL: {
                isBeingDragged = false;
                activePointerId = INVALID_POINTER;
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            }
        }

        if (velocityTracker != null) {
            velocityTracker.addMovement(ev);
        }

        return true;
    }

    int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset) {
        return setHeaderTopBottomOffset(                parent, header, newOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);
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

    int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

    final int scroll(CoordinatorLayout coordinatorLayout, V header, int dy, int minOffset, int maxOffset) {
        int i = setHeaderTopBottomOffset(coordinatorLayout, header, getTopBottomOffsetForScrollingSibling() - dy, minOffset, maxOffset);
        LogUtils.e(MessageFormat.format("滚动剩余:{0}", i));
        return i;
    }

    final boolean fling(CoordinatorLayout coordinatorLayout, @NonNull V layout, int minOffset, int maxOffset, float velocityY) {
        LogUtils.e("甩动");
        if (flingRunnable != null) {
            layout.removeCallbacks(flingRunnable);
            flingRunnable = null;
        }

        if (scroller == null) {
            scroller = new OverScroller(layout.getContext());
        }

        scroller.fling(0, getTopAndBottomOffset(), 0, Math.round(velocityY), 0, 0, minOffset, maxOffset); // y

        if (scroller.computeScrollOffset()) {
            flingRunnable = new HeaderBehavior.FlingRunnable(coordinatorLayout, layout);
            ViewCompat.postOnAnimation(layout, flingRunnable);
            return true;
        } else {
            onFlingFinished(coordinatorLayout, layout);
            return false;
        }
    }

    /**
     * Called when a fling has finished, or the fling was initiated but there wasn't enough velocity
     * to start it.
     */
    protected void onFlingFinished(CoordinatorLayout parent, V layout) {
        // no-op
    }

    /**
     * Return true if the view can be dragged.
     */
    boolean canDragView(V view) {
        return false;
    }

    /**
     * Returns the maximum px offset when {@code view} is being dragged.
     */
    int getMaxDragOffset(@NonNull V view) {
        return -view.getHeight();
    }

    int getScrollRangeForDragFling(@NonNull V view) {
        return view.getHeight();
    }

    private void ensureVelocityTracker() {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
    }

    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout parent;
        private final V layout;

        FlingRunnable(CoordinatorLayout parent, V layout) {
            this.parent = parent;
            this.layout = layout;
        }

        @Override
        public void run() {
            if (layout != null && scroller != null) {
                if (scroller.computeScrollOffset()) {
                    setHeaderTopBottomOffset(parent, layout, scroller.getCurrY());
                    // Post ourselves so that we run on the next animation
                    ViewCompat.postOnAnimation(layout, this);
                } else {
                    onFlingFinished(parent, layout);
                }
            }
        }
    }
}
