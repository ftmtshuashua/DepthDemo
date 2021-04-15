package com.depth.behavior.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.depth.behavior.utils.LogUtils;
import com.google.gson.Gson;

import java.text.MessageFormat;

/**
 * 使用在ViewPager上的Behavior
 */
public class BehaviorViewPager extends CoordinatorLayout.Behavior<View> {

    public BehaviorViewPager() {
    }

    public BehaviorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //获得对象ID
    private String getId(Object obj) {
        return MessageFormat.format("{0}({1})", obj.getClass().getSimpleName(), Integer.toHexString(System.identityHashCode(obj)));
    }

    private String get(int[] data) {
        return new Gson().toJson(data);
    }

    @Override
    public void onRestoreInstanceState(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull Parcelable state) {
        super.onRestoreInstanceState(parent, child, state);
    }

    @Nullable
    @Override
    public Parcelable onSaveInstanceState(@NonNull CoordinatorLayout parent, @NonNull View child) {
        return super.onSaveInstanceState(parent, child);
    }

    //当View设置回避属性时,回调(回避就是,当一个view移动过来,他需要让位置)
    @Override
    public boolean getInsetDodgeRect(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull Rect rect) {
        return super.getInsetDodgeRect(parent, child, rect);
    }


    //被绑定到一个layoutParams的回调
    @Override
    public void onAttachedToLayoutParams(@NonNull CoordinatorLayout.LayoutParams params) {
        super.onAttachedToLayoutParams(params);
    }

    //解绑layoutParams的回调
    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
    }


    View mV_DownFeedView;


    //关联一个控件(这个常用)
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (dependency instanceof DownFeedView) {
            mV_DownFeedView = dependency;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    //判断是否接管子View的滑动
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        LogUtils.e(MessageFormat.format("滑动信号 -> ( {0} , {1} , {2} , {3} , {4})", getId(child), getId(directTargetChild), getId(target), axes, type));
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            if (directTargetChild instanceof PassthroughViewPager) {
                return true;
            }
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


    //当NSChild即将被滑动 - 处理滚动的消耗问题
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //设置当前控制器消耗的值,target只能使用余下的部分值
        boolean canDown = child.canScrollVertically(1);

        if (dy != 0 && !canDown) {
            if (mV_DownFeedView != null) {
                int tran_y_max = coordinatorLayout.getHeight(); //平移位置的最大值
                int tran_y_min = tran_y_max - mV_DownFeedView.getHeight();

                int translationY = (int) mV_DownFeedView.getTranslationY();

                if (dy < 0) {  //下拉
                    if (translationY < tran_y_max) {
                        int offset_y = (int) (tran_y_max - translationY); //差距
                        if (offset_y > Math.abs(dy)) {
                            consumed[1] = dy;
                        } else {
                            consumed[1] = -offset_y;
                        }
                    }
                } else {        //上滑
                    if (translationY > tran_y_min) {
                        int offset_y = translationY - tran_y_min;
                        if (offset_y < dy) {
                            consumed[1] = offset_y;
                        } else {
                            consumed[1] = dy;
                        }

                    }
                }
                if (consumed[1] != 0) {
                    child.setTranslationY(child.getTranslationY() - consumed[1]);
                }
            }
        }


        LogUtils.i(MessageFormat.format("NSChild即将被滑动 -> ( {0} , {1} , {2,number,0} , {3,number,0} , {4} , {5} )", getId(child), getId(target), dx, dy, get(consumed), type));
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

   /* @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        LogUtils.i(MessageFormat.format("NSChild滚动结束 -> ( {0} , {1} , NSChild处理 {2,number,0} , {3,number,0} ,NSChild剩余 {4,number,0} , {5,number,0} , {6} , {7} )", getId(child), getId(target), dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, get(consumed)));

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }*/


}
