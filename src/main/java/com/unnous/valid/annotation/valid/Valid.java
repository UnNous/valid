package com.unnous.valid.annotation.valid;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {
}
