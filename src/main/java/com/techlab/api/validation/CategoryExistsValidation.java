package com.techlab.api.validation;

import com.techlab.api.annotations.CategoryExists;
import com.techlab.api.services.CategoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryExistsValidation implements ConstraintValidator<CategoryExists, Long> {
    private final CategoryService categoryService;

    @Autowired
    public CategoryExistsValidation(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return this.categoryService.doesCategoryExistById(id);
    }
}
