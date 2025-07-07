package com.techlab.api.annotations;

import com.techlab.api.validation.CategoryExistsValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryExistsValidation.class)
@Documented
public @interface CategoryExists {
    String message() default "Category does not exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
