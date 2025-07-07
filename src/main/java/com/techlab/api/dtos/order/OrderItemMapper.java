package com.techlab.api.dtos.order;

import com.techlab.api.dtos.product.ProductMapper;
import com.techlab.api.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {
    @Named("fromItemWithProducts")
    @Mapping(target = "product", qualifiedByName = "fromProductWithoutCategories")
    OrderItemResponseDTO fromItemWithProducts(OrderItem orderItem);
}
