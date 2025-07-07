package com.techlab.api.validation;

import com.techlab.api.annotations.ProductExists;
import com.techlab.api.services.ProductService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductExistsValidation implements ConstraintValidator<ProductExists, Long> {
    private final ProductService productService;

    @Autowired
    public ProductExistsValidation(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return this.productService.doesProductExistById(id);
    }
}
