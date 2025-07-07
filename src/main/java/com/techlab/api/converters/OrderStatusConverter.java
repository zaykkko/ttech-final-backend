package com.techlab.api.converters;

import com.techlab.api.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderStatus status) {
        return (status != null) ? status.getCode() : null;
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer dbData) {
        return (dbData != null) ? OrderStatus.fromCode(dbData) : null;
    }
}
