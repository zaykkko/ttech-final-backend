package com.techlab.api.annotations;

import com.techlab.api.validation.CategoryUniqueValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryUniqueValidation.class)
@Documented
public @interface CategoryUnique {
    String message() default "Category already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
