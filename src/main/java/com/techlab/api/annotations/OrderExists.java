package com.techlab.api.annotations;

import com.techlab.api.validation.OrderExistsValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderExistsValidation.class)
@Documented
public @interface OrderExists {
    String message() default "Order does not exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
