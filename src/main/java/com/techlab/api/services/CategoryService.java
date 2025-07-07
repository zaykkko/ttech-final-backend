package com.techlab.api.services;

import com.techlab.api.constants.TokenConstants;
import com.techlab.api.entities.Category;
import com.techlab.api.exceptions.ApiException;
import com.techlab.api.repositories.CategoryRepository;
import com.techlab.api.responses.data.ErrorData;
import com.techlab.api.responses.data.TokenizedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return this.categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                    .addError("categoryId", new TokenizedData<>(TokenConstants.CATEGORY_NOT_FOUND))));
    }

    public List<Category> getCategoriesByName(String name) {
        return this.categoryRepository.findByNameIgnoreCase(name);
    }

    public boolean doesCategoryExistById(Long id) {
        return this.categoryRepository.existsById(id);
    }

    public boolean doesCategoryExistsByName(String name) {
        return this.categoryRepository.existsByNameIgnoreCase(name);
    }

    public Category saveCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id) {
        this.categoryRepository.deleteById(id);
    }

    public void validateCategoryName(String categoryName) {
        if (this.doesCategoryExistsByName(categoryName)) {
            ErrorData<Void> errorData = new ErrorData<>();
            errorData.addError("name", new TokenizedData<>(TokenConstants.CATEGORY_NAME_NOT_UNIQUE));

            throw new ApiException(HttpStatus.CONFLICT, errorData);
        }
    }
}
