package com.techlab.api.services;

import com.techlab.api.constants.TokenConstants;
import com.techlab.api.entities.Product;
import com.techlab.api.entities.ProductCategory;
import com.techlab.api.exceptions.ApiException;
import com.techlab.api.repositories.ProductCategoryRepository;
import com.techlab.api.repositories.ProductRepository;
import com.techlab.api.responses.data.ErrorData;
import com.techlab.api.responses.data.TokenizedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<Product> getAllProductsWithCategories() {
        return this.productRepository.findAllWithCategories();
    }

    public List<Product> getProductsWithCategoriesByName(String name) {
        return this.productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product getProductById(Long id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                        .addError("productId", new TokenizedData<>(TokenConstants.PRODUCT_NOT_FOUND))));
    }

    public List<Product> getAllById(List<Long> ids) {
        return this.productRepository.findAllById(ids);
    }

    public Product getProductWithCategoriesById(Long id) {
        return this.productRepository.findWithCategoriesById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                        .addError("productId", new TokenizedData<>(TokenConstants.PRODUCT_NOT_FOUND))));
    }

    public boolean doesProductExistById(Long id) { return this.productRepository.existsById(id); }

    public boolean doesProductExistByName(String name) {
        return this.productRepository.existsByNameIgnoreCase(name);
    }

    public boolean doesProductHasCategoryById(Long productId, Long categoryId) {
        return this.productCategoryRepository.existsByCategoryIdAndProductId(categoryId, productId);
    }

    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void saveProductCategory(ProductCategory productCategory) {
        this.productCategoryRepository.save(productCategory);
    }

    public Product updateProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void deleteProductByid(Long id) {
        this.productRepository.deleteById(id);
    }

    public void deleteProductCategory(ProductCategory productCategory) {
        this.productCategoryRepository.delete(productCategory);
    }

    public void validateProductName(String productName) {
        if (this.doesProductExistByName(productName)) {
            ErrorData<Void> errorData = new ErrorData<>();
            errorData.addError("name", new TokenizedData<>(TokenConstants.PRODUCT_NAME_NOT_UNIQUE));

            throw new ApiException(HttpStatus.CONFLICT, errorData);
        }
    }
}
