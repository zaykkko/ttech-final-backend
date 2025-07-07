package com.techlab.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.techlab.api.entities.embeddables.ProductCategoryId;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class ProductCategory {
    @EmbeddedId
    @JsonIgnore
    private ProductCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(
        name = "product_id",
        foreignKey = @ForeignKey(
            name = "fk_pc_product",
            foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE"
        )
    )
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(
        name = "category_id",
        foreignKey = @ForeignKey(
            name = "fc_pc_category",
            foreignKeyDefinition = "FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE ON UPDATE CASCADE"
        )
    )
    @JsonIgnore
    private Category category;

    private Boolean isPrimary = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public ProductCategory() {}

    public ProductCategoryId getId() {
        return this.id;
    }

    public void setId(ProductCategoryId id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (this.category != null) {
            this.id = new ProductCategoryId(this.product.getId(), this.category.getId());
        }
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
        if (this.product != null) {
            this.id = new ProductCategoryId(this.product.getId(), this.category.getId());
        }
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Boolean getIsPrimary() {
        return this.isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = Objects.requireNonNull(isPrimary, "isPrimary cannot be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductCategory that = (ProductCategory) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
