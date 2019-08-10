package com.unnous.valid.annotation.valid.function;


import java.lang.annotation.*;

/**
 * 校验标注在请求Bean参数上，根据给定的fields字段进行校验，如果为空则声明的字段全部校验
 * author: tangwc
 * date: 2019-08-10
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidParam {
    String[] value() default {};
}
