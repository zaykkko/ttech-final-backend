package com.techlab.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.techlab.api.entities.embeddables.ProductCategoryId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50)
    private String name;

    @NotNull
    private String description;

    @NotNull
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    private String imageUrl;

    @NotNull
    @Column
    private Integer quantity;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<ProductCategory> productCategories = new HashSet<>();

    public Product() {}

    private Product(String name, String description, BigDecimal price, String imageUrl, Integer quantity) {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setImageUrl(imageUrl);
        this.setQuantity(quantity);
    }

    @JsonProperty("categories")
    public Set<Category> getCategories() {
        return this.productCategories.stream().map(ProductCategory::getCategory).collect(Collectors.toSet());
    }

    public void addCategory(Category category) {
        ProductCategory productCategory = new ProductCategory();

        productCategory.setId(new ProductCategoryId(this.getId(), category.getId()));
        productCategory.setProduct(this);
        productCategory.setCategory(category);

        this.productCategories.add(productCategory);
    }

    public Boolean removeCategory(Category category) {
        return this.productCategories.removeIf(
                productCategory -> productCategory.getCategory().equals(category));
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Product name is required");
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description, "Product description is required");
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = Objects.requireNonNull(price, "Product price is required");
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = Objects.requireNonNull(imageUrl, "Product imageUrl is required");
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = Objects.requireNonNull(quantity, "Product quantity is required");
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ProductCategory> getProductCategories() {
        return this.productCategories;
    }

    public void setProductCategories(Set<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    // https://stackoverflow.com/a/41778582/10942774
    public static class ProductBuilder {
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Integer quantity;

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductBuilder setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Product build() {
            return new Product(this.name, this.description, this.price, this.imageUrl, this.quantity);
        }
    }
}
