package com.techlab.api.repositories;

import com.techlab.api.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    boolean existsByCategoryIdAndProductId(Long categoryId, Long productId);
}
