package com.techlab.api.dtos.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.techlab.api.dtos.category.CategoryResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private int quantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CategoryResponseDTO> categories;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public List<CategoryResponseDTO> getCategories() {
        return this.categories;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategories(List<CategoryResponseDTO> categories) {
        this.categories = categories;
    }
}
