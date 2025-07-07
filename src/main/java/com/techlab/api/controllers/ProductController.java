package com.techlab.api.controllers;

import com.techlab.api.constants.TokenConstants;
import com.techlab.api.dtos.product.ProductMapper;
import com.techlab.api.dtos.product.ProductPayloadDTO;
import com.techlab.api.dtos.product.ProductResponseDTO;
import com.techlab.api.entities.Category;
import com.techlab.api.entities.Product;
import com.techlab.api.exceptions.ApiException;
import com.techlab.api.responses.data.ErrorData;
import com.techlab.api.responses.data.TokenizedData;
import com.techlab.api.services.CategoryService;
import com.techlab.api.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, CategoryService categoryService, ProductMapper productMapper) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return this.productMapper
                .fromProductWithCategoriesList(this.productService.getAllProductsWithCategories());
    }

    @GetMapping("/query")
    public List<ProductResponseDTO> getProducts(@RequestParam String name) {
        return this.productMapper
                .fromProductWithCategoriesList(this.productService.getProductsWithCategoriesByName(name));
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return this.productMapper.fromProductWithCategories(this.productService.getProductWithCategoriesById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(
            @Validated(ProductPayloadDTO.Create.class) @RequestBody ProductPayloadDTO product) {
        this.productService.validateProductName(product.getName());

        Product newProduct = Product.builder()
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .setQuantity(product.getQuantity())
                .setImageUrl(product.getImageUrl())
                .build();

        return this.productMapper.fromProductWithoutCategories(this.productService.saveProduct(newProduct));
    }

    @PatchMapping("/{id}")
    public ProductResponseDTO updateProduct(
            @PathVariable Long id,
            @Validated(ProductPayloadDTO.Update.class) @RequestBody ProductPayloadDTO payload) {
        Product product = this.productService.getProductById(id);

        if (payload.getName() != null && !product.getName().equals(payload.getName())) {
            this.productService.validateProductName(payload.getName());
            product.setName(payload.getName());
        }

        if (payload.getDescription() != null && !product.getDescription().equals(payload.getDescription())) {
            product.setDescription(payload.getDescription());
        }

        if (payload.getPrice() != null && !product.getPrice().equals(payload.getPrice())) {
            product.setPrice(payload.getPrice());
        }

        if (payload.getImageUrl() != null && !product.getImageUrl().equals(payload.getImageUrl())) {
            product.setImageUrl(payload.getImageUrl());
        }

        if (payload.getQuantity() != null && !product.getQuantity().equals(payload.getQuantity())) {
            product.setQuantity(payload.getQuantity());
        }

        return this.productMapper.fromProductWithoutCategories(this.productService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (this.productService.doesProductExistById(id)) {
            this.productService.deleteProductByid(id);
            return ResponseEntity.ok().build();
        }

        throw new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                .addError("id", new TokenizedData<>(TokenConstants.PRODUCT_NOT_FOUND)));
    }

    @PutMapping("/{productId}/categories/{categoryId}")
    public ProductResponseDTO createProductCategory(
        @PathVariable Long productId,
        @PathVariable Long categoryId
    ) {
        Product product = this.productService.getProductWithCategoriesById(productId);
        Category category = this.categoryService.getCategoryById(categoryId);

        product.addCategory(category);
        this.productService.saveProduct(product);

        return this.productMapper.fromProductWithCategories(product);
    }

    @DeleteMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<Void> deleteProductCategory(
            @PathVariable Long productId,
            @PathVariable Long categoryId
    ) {
        Product product = this.productService.getProductWithCategoriesById(productId);
        Category category = this.categoryService.getCategoryById(categoryId);

        boolean categoryRemoved = product.removeCategory(category);
        if (categoryRemoved) {
            this.productService.saveProduct(product);
        }

        return ResponseEntity.ok().build();
    }
}
