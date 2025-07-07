package com.techlab.api.entities.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ProductCategoryId {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "category_id")
    private Long categoryId;

    public ProductCategoryId() {}

    public ProductCategoryId(Long productId, Long categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = Objects.requireNonNull(productId, "productId cannot be null");
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductCategoryId that = (ProductCategoryId) o;
        return Objects.equals(this.productId, that.getProductId()) &&
                Objects.equals(this.categoryId, that.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.productId, this.categoryId);
    }
}
