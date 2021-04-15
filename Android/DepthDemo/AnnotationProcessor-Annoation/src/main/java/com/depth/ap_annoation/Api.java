package com.depth.ap_annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * Tip:
 *      Retention 表示注解的保留范围。值用RetentionPolicy枚举类型表示，分为CLASS 、RUNTIME 、 SOURCE。
 *      Target 表示注解的使用范围。值用ElementType枚举类型表示，有TYPE（作用于类）、FIELD（作用于属性）、METHOD（作用于方法）等。
 *
 * Created by ACap on 2021/4/15 11:53
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Api {
    String method();
}
