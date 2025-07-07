package com.techlab.api.controllers;

import com.techlab.api.constants.TokenConstants;
import com.techlab.api.dtos.category.CategoryMapper;
import com.techlab.api.dtos.category.CategoryPayloadDTO;
import com.techlab.api.dtos.category.CategoryResponseDTO;
import com.techlab.api.entities.Category;
import com.techlab.api.exceptions.ApiException;
import com.techlab.api.responses.data.ErrorData;
import com.techlab.api.responses.data.TokenizedData;
import com.techlab.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public List<CategoryResponseDTO> getAllCategories() {
        return this.categoryMapper.fromCategoryList(this.categoryService.getAllCategories());
    }

    @GetMapping("/query")
    public List<CategoryResponseDTO> getCategories(@RequestParam String name) {
        return this.categoryMapper.fromCategoryList(this.categoryService.getCategoriesByName(name));
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO getCategoryById(@PathVariable Long id) {
        return this.categoryMapper.fromCategory(this.categoryService.getCategoryById(id));
    }

    @PostMapping
    public CategoryResponseDTO createCategory(
            @Validated(CategoryPayloadDTO.Create.class) @RequestBody CategoryPayloadDTO payload) {
        this.categoryService.validateCategoryName(payload.getName());

        Category newCategory = new Category(payload.getName());

        return this.categoryMapper.fromCategory(this.categoryService.saveCategory(newCategory));
    }

    @PatchMapping("/{id}")
    public CategoryResponseDTO updateCategory(
            @PathVariable Long id,
            @Validated(CategoryPayloadDTO.Update.class) @RequestBody CategoryPayloadDTO payload) {
        Category savedCategory = this.categoryService.getCategoryById(id);

        if (!savedCategory.getName().equalsIgnoreCase(payload.getName())) {
            this.categoryService.validateCategoryName(payload.getName());
            savedCategory.setName(payload.getName());
        }

        return this.categoryMapper.fromCategory(this.categoryService.saveCategory(savedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (this.categoryService.doesCategoryExistById(id)) {
            this.categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().build();
        }

        throw new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                .addError("id", new TokenizedData<>(TokenConstants.CATEGORY_NOT_FOUND)));
    }
}
