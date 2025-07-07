package com.techlab.api.validation;

import com.techlab.api.annotations.CategoryUnique;
import com.techlab.api.services.CategoryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryUniqueValidation implements ConstraintValidator<CategoryUnique, String> {
    private final CategoryService categoryService;

    @Autowired
    public CategoryUniqueValidation(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean isValid(String category, ConstraintValidatorContext context) {
        return category != null && !this.categoryService.doesCategoryExistsByName(category);
    }
}
