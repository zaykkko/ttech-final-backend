package com.techlab.api.controllers;

import com.techlab.api.constants.TokenConstants;
import com.techlab.api.dtos.order.OrderItemPayloadDTO;
import com.techlab.api.dtos.order.OrderMapper;
import com.techlab.api.dtos.order.OrderResponseDTO;
import com.techlab.api.entities.Order;
import com.techlab.api.exceptions.ApiException;
import com.techlab.api.responses.data.ErrorData;
import com.techlab.api.responses.data.TokenizedData;
import com.techlab.api.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public List<OrderResponseDTO> getOrders() {
        return this.orderMapper.fromOrderWithoutItemsList(this.orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getOrderById(@PathVariable Long id) {
        return this.orderMapper.fromOrderWithItems(this.orderService.getOrderWithProductsById(id));
    }

    @PostMapping
    public OrderResponseDTO createOrder() {
        return this.orderMapper.fromOrderWithoutItems(this.orderService.saveOrder(new Order()));
    }

    @PatchMapping("/{id}/confirm")
    public OrderResponseDTO confirmOrder(@PathVariable Long id) {
        return this.orderMapper.fromOrderWithItems(this.orderService.confirmOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (this.orderService.doesOrderExistById(id)) {
            this.orderService.deleteOrderById(id);
            return ResponseEntity.ok().build();
        }

        throw new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                .addError("id", new TokenizedData<>(TokenConstants.ORDER_NOT_FOUND)));
    }

    @PutMapping("/{orderId}/products")
    public ResponseEntity<Void> addProductToOrder(
            @PathVariable Long orderId,
            @RequestBody @Validated(OrderItemPayloadDTO.Create.class) List<@Valid OrderItemPayloadDTO> orderItemDtos) {
        this.orderService.addProductsToOrder(orderId, orderItemDtos);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderId}/products/{productId}")
    public ResponseEntity<Void> updateOrderProduct(
            @PathVariable Long orderId,
            @PathVariable Long productId,
            @RequestBody @Validated(OrderItemPayloadDTO.Update.class) OrderItemPayloadDTO orderItemDto) {
        this.orderService.updateOrderById(orderId, productId, orderItemDto.getQuantity());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{orderId}/products/{productId}")
    public ResponseEntity<Void> deleteOrderProduct(@PathVariable Long orderId, @PathVariable Long productId) {
        this.orderService.removeProductFromOrder(orderId, productId);

        return ResponseEntity.ok().build();
    }
}
