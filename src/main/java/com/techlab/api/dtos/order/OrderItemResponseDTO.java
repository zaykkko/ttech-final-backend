package com.techlab.api.dtos.order;

import com.techlab.api.dtos.product.ProductResponseDTO;

import java.time.LocalDateTime;

public class OrderItemResponseDTO {
    private Integer quantity;
    private ProductResponseDTO product;
    private LocalDateTime createdAt;

    public Integer getQuantity() {
        return this.quantity;
    }

    public ProductResponseDTO getProduct() {
        return this.product;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setProduct(ProductResponseDTO product) {
        this.product = product;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
