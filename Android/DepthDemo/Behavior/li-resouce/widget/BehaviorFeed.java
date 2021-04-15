package com.depth.behavior.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.depth.behavior.utils.LogUtils;
import com.google.gson.Gson;

import java.text.MessageFormat;

/**
 * 使用在信息流上的Behavior
 */
public class BehaviorFeed extends CoordinatorLayout.Behavior<View> {

    public BehaviorFeed() {
    }

    public BehaviorFeed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //获得对象ID
    private String getId(Object obj) {
        return MessageFormat.format("{0}({1})", obj.getClass().getSimpleName(), Integer.toHexString(System.identityHashCode(this)));
    }

    private String get(int[] data) {
        return new Gson().toJson(data);
    }


    private View mV_Dependency;

    //关联一个控件(这个常用)
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (dependency instanceof PassthroughViewPager) {
            mV_Dependency = dependency;
            return true;
        }

        return super.layoutDependsOn(parent, child, dependency);
    }

    //当关联的控件变化时的回调
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        if (dependency == mV_Dependency) {
            float bottom = dependency.getTranslationY() + dependency.getHeight();
            child.setTranslationY(bottom);
        }


        LogUtils.e(MessageFormat.format("View位置变化:{0,number,0} , {1,number,0} , {2,number,0} , {3,number,0}", child.getLeft(), child.getTop(), child.getRight(), child.getBottom()));

        return super.onDependentViewChanged(parent, child, dependency);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (directTargetChild instanceof DownFeedView) {
            return true;
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (mV_Dependency != null) {
            int limit = coordinatorLayout.getHeight() - child.getHeight();  //控制值
            float translationY = child.getTranslationY();

            if (translationY > limit) {
                int offset = (int) (translationY - limit);

                if (dy > offset) {
                    consumed[1] = offset;
                } else {
                    consumed[1] = dy;
                }
            }

            if (consumed[1] != 0) {
                float y = mV_Dependency.getTranslationY() - consumed[1];
                if (y >= 0) {
                    mV_Dependency.setTranslationY(0);
                } else {
                    mV_Dependency.setTranslationY(y);
                }
            }

        }


        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }

}
