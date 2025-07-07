package com.techlab.api.dtos.order;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private int status;
    private LocalDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderItemResponseDTO> products;

    public Long getId() {
        return this.id;
    }

    public int getStatus() {
        return this.status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public List<OrderItemResponseDTO> getProducts() {
        return this.products;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setProducts(List<OrderItemResponseDTO> products) {
        this.products = products;
    }
}
