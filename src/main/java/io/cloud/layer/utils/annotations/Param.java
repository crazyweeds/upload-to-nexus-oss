package io.cloud.layer.utils.annotations;

import java.lang.annotation.*;

/**
 * @author RippleChan
 * @date 2019-09-10 23:58
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {

    String name() default "";

    boolean require() default false;

}
