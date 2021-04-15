package com.depth.behavior.behavior;

import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * 用于移动 {@link View} 的工具助手，使用 {@link View#offsetLeftAndRight(int)} 和
 * {@link View#offsetTopAndBottom(int)}.
 *
 * <p>还有绝对偏移量的设置 (类似于 translationX/Y), 而不是添加的偏移量.
 */
public class ViewOffsetHelper {

    private final View view;

    private int layoutTop;
    private int layoutLeft;
    private int offsetTop;
    private int offsetLeft;
    private boolean verticalOffsetEnabled = true;
    private boolean horizontalOffsetEnabled = true;

    public ViewOffsetHelper(View view) {
        this.view = view;
    }

    //初始化偏移量
    public void onViewLayout() {
        // Grab the original top and left
        layoutTop = view.getTop();
        layoutLeft = view.getLeft();
    }

    //应用偏移量
    public void applyOffsets() {
        ViewCompat.offsetTopAndBottom(view, offsetTop - (view.getTop() - layoutTop));
        ViewCompat.offsetLeftAndRight(view, offsetLeft - (view.getLeft() - layoutLeft));
    }

    /**
     * Set the top and bottom offset for this {@link ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    public boolean setTopAndBottomOffset(int offset) {
        if (verticalOffsetEnabled && offsetTop != offset) {
            offsetTop = offset;
            applyOffsets();
            return true;
        }
        return false;
    }

    /**
     * Set the left and right offset for this {@link ViewOffsetHelper}'s view.
     *
     * @param offset the offset in px.
     * @return true if the offset has changed
     */
    public boolean setLeftAndRightOffset(int offset) {
        if (horizontalOffsetEnabled && offsetLeft != offset) {
            offsetLeft = offset;
            applyOffsets();
            return true;
        }
        return false;
    }

    public int getTopAndBottomOffset() {
        return offsetTop;
    }

    public int getLeftAndRightOffset() {
        return offsetLeft;
    }

    public int getLayoutTop() {
        return layoutTop;
    }

    public int getLayoutLeft() {
        return layoutLeft;
    }

    public void setVerticalOffsetEnabled(boolean verticalOffsetEnabled) {
        this.verticalOffsetEnabled = verticalOffsetEnabled;
    }

    public boolean isVerticalOffsetEnabled() {
        return verticalOffsetEnabled;
    }

    public void setHorizontalOffsetEnabled(boolean horizontalOffsetEnabled) {
        this.horizontalOffsetEnabled = horizontalOffsetEnabled;
    }

    public boolean isHorizontalOffsetEnabled() {
        return horizontalOffsetEnabled;
    }
}