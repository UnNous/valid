package com.unnous.valid.annotation.valid.function;


import com.unnous.valid.annotation.valid.Valid;

import java.lang.annotation.*;

/**
 * 标注在请求参数上 可以校验包装数据类型，集合等是否为空
 * author: tangwc
 * date: 2019-08-10
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Valid
public @interface NotNull {
    String value() default "字段不能为空";
}
