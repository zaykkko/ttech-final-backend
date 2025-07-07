package com.techlab.api.annotations;

import com.techlab.api.formatters.ResponseFormatterInterface;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomResponseFormatter {
    /**
     * Class which implements ResponseFormatterInterface to format the response.
     */
    Class<? extends ResponseFormatterInterface> value();

    /**
     * If true, the format is applied even there are more formatters.
     */
    boolean override() default false;
}
