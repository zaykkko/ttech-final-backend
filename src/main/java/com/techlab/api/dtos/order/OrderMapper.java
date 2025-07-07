package com.techlab.api.dtos.order;

import com.techlab.api.entities.Order;
import com.techlab.api.enums.OrderStatus;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Named("fromOrderWithoutItems")
    @Mapping(target = "products", ignore = true)
    OrderResponseDTO fromOrderWithoutItems(Order order);

    @Named("fromOrderWithoutItemsList")
    @IterableMapping(qualifiedByName = "fromOrderWithoutItems")
    @Mapping(target = "products", ignore = true)
    List<OrderResponseDTO> fromOrderWithoutItemsList(List<Order> orders);

    @Named("fromOrderWithItems")
    @Mapping(target = "products", qualifiedByName = "fromItemWithProducts")
    @Mapping(target = "status", qualifiedByName = "statusToInteger")
    OrderResponseDTO fromOrderWithItems(Order order);

    @Named("statusToInteger")
    default Integer statusToInteger(OrderStatus status) {
        return status != null ? status.getCode() : null;
    }
}
