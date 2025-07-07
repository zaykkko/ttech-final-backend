package com.techlab.api.services;

import com.techlab.api.constants.TokenConstants;
import com.techlab.api.dtos.order.OrderItemPayloadDTO;
import com.techlab.api.entities.Order;
import com.techlab.api.entities.OrderItem;
import com.techlab.api.entities.Product;
import com.techlab.api.enums.OrderStatus;
import com.techlab.api.exceptions.ApiException;
import com.techlab.api.repositories.OrderRepository;
import com.techlab.api.responses.data.ErrorData;
import com.techlab.api.responses.data.TokenizedData;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public List<Order> getAllOrders() {
        return this.orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(Long id) {
        return this.orderRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                        .addError("orderId", new TokenizedData<>(TokenConstants.ORDER_NOT_FOUND))));
    }

    public Order getOrderWithProductsById(Long id) {
        return this.orderRepository.findOrderWithProductsById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, new ErrorData<>()
                        .addError("orderId", new TokenizedData<>(TokenConstants.ORDER_NOT_FOUND))));
    }

    public Order saveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    public boolean doesOrderExistById(Long id) {
        return this.orderRepository.existsById(id);
    }

    public void deleteOrderById(Long id) {
        this.orderRepository.deleteById(id);
    }

    @Transactional
    public Order confirmOrderById(Long id) {
        Order order = this.getOrderWithProductsById(id);

        if (order.isConfirmed()) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    new ErrorData<>()
                            .addError("orderStatus", new TokenizedData<>(TokenConstants.ORDER_ALREADY_CONFIRMED))
            );
        }

        for (OrderItem orderItem : order.getOrderProducts()) {
            Product product = orderItem.getProduct();

            this.validateProductStock(product, orderItem.getQuantity());

            product.setQuantity(product.getQuantity() - orderItem.getQuantity());
        }

        order.setStatus(OrderStatus.Confirmed);

        return this.orderRepository.save(order);
    }

    @Transactional
    public void addProductsToOrder(Long orderId, List<OrderItemPayloadDTO> orderItemDTOs) {
        Order order = this.getOrderWithProductsById(orderId);

        List<Long> requestedIds = orderItemDTOs.stream().map(OrderItemPayloadDTO::getProductId).toList();
        Map<Long, Product> productsById = this.productService.getAllById(requestedIds)
                .stream().collect(Collectors.toMap(Product::getId, p -> p));

        this.validateAllProductsExists(requestedIds, productsById.keySet());

        for (OrderItemPayloadDTO dto : orderItemDTOs) {
            Product product = productsById.get(dto.getProductId());

            this.validateProductStock(product, dto.getQuantity());

            order.addOrUpdateProduct(product, dto.getQuantity());
        }

        this.orderRepository.save(order);
    }

    public void validateProductStock(Product product, Integer quantity) {
        if (quantity > product.getQuantity()) {
            ErrorData<List<Long>> notEnoughStockErrorData = new ErrorData<>();
            notEnoughStockErrorData.addError(
                    "quantity",
                    new TokenizedData<>(TokenConstants.NOT_ENOUGH_STOCK_ERROR, List.of(product.getId())));

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    notEnoughStockErrorData
            );
        }
    }

    public void updateOrderById(Long orderId, Long productId, Integer quantity) {
        Order order = this.orderRepository.findOrderWithProductsById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order with id " + orderId + " not found"));

        Product product = this.productService.getProductById(productId);

        if (quantity > product.getQuantity()) {
            ErrorData<List<Long>> notEnoughStockErrorData = new ErrorData<>();
            notEnoughStockErrorData.addError(
                    "quantity",
                    new TokenizedData<>(TokenConstants.NOT_ENOUGH_STOCK_ERROR, List.of(productId)));

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    notEnoughStockErrorData
            );
        }

        order.addOrUpdateProduct(product, quantity);

        this.orderRepository.save(order);
    }

    public void removeProductFromOrder(Long orderId, Long productId) {
        Order order = this.getOrderWithProductsById(orderId);

        boolean productExists = order.getOrderProducts()
                .removeIf(item -> item.getProduct().getId().equals(productId));

        if (productExists) {
            this.orderRepository.save(order);
        }
    }

    private void validateAllProductsExists(List<Long> productIds, Set<Long> foundsIds) {
        List<Long> missingIds = productIds.stream()
                .filter(id -> !foundsIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            ErrorData<List<Long>> missingProductIdErrorData = new ErrorData<>();
            missingProductIdErrorData.addError(
                    "productIds", new TokenizedData<>(TokenConstants.PRODUCT_NOT_FOUND, missingIds));

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    missingProductIdErrorData
            );
        }
    }
}
