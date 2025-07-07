package com.techlab.api.annotations;

import com.techlab.api.validation.ProductExistsValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductExistsValidation.class)
@Documented
public @interface ProductExists {
    String message() default "Product does not exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
