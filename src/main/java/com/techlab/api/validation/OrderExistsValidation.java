package com.techlab.api.validation;

import com.techlab.api.annotations.OrderExists;
import com.techlab.api.repositories.OrderRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderExistsValidation implements ConstraintValidator<OrderExists, Long> {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderExistsValidation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        return this.orderRepository.existsById(id);
    }
}
