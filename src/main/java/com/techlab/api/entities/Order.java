package com.techlab.api.entities;

import com.fasterxml.jackson.annotation.*;
import com.techlab.api.converters.OrderStatusConverter;
import com.techlab.api.entities.embeddables.OrderItemId;
import com.techlab.api.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status = OrderStatus.Open;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<OrderItem> orderProducts = new HashSet<>();

    @JsonProperty("products")
    public Set<OrderItem> getProducts() {
        return this.orderProducts;
    }

    public void addOrUpdateProduct(Product product, Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        for (OrderItem orderItem : this.orderProducts) {
            if (orderItem.getProduct().getId().equals(product.getId())) {
                orderItem.setQuantity(quantity);
                return;
            }
        }

        OrderItem newItem = new OrderItem();
        newItem.setId(new OrderItemId(this.getId(), product.getId()));
        newItem.setOrder(this);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);

        this.orderProducts.add(newItem);
    }

    public void removeProduct(Product product) {
        this.orderProducts.removeIf(orderItem -> orderItem.getProduct().equals(product));
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isConfirmed() {
        return OrderStatus.isConfirmed(this.status);
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Set<OrderItem> getOrderProducts() {
        return this.orderProducts;
    }

    public void setOrderProducts(Set<OrderItem> products) {
        this.orderProducts = products;
    }
}
