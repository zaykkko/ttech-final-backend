package com.techlab.api.entities;

import com.fasterxml.jackson.annotation.*;
import com.techlab.api.entities.embeddables.OrderItemId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
public class OrderItem {
    @EmbeddedId
    @JsonIgnore
    private OrderItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(
        name = "product_id",
        foreignKey = @ForeignKey(
            name = "fk_oi_product",
            foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE"
        )
    )
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(
        name = "order_id",
        foreignKey = @ForeignKey(
            name = "fc_oi_category",
            foreignKeyDefinition = "FOREIGN KEY (order_id) REFERENCES order(id) ON DELETE CASCADE ON UPDATE CASCADE"
        )
    )
    @JsonIgnore
    private Order order;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotNull
    @Column
    private Integer quantity;

    public OrderItemId getId() {
        return this.id;
    }

    public void setId(OrderItemId id) {
        this.id = id;
    }

    @JsonProperty("product")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
