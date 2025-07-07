package com.techlab.api.dtos.product;

import com.techlab.api.dtos.category.CategoryMapper;
import com.techlab.api.entities.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

// https://stackoverflow.com/a/52164478/10942774
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    @Named("fromProductWithoutCategories")
    @Mapping(target = "categories", ignore = true)
    ProductResponseDTO fromProductWithoutCategories(Product product);

    @Named("fromProductWithoutCategoriesList")
    @IterableMapping(qualifiedByName = "fromProductWithoutCategories")
    @Mapping(target = "categories", ignore = true)
    List<ProductResponseDTO> fromProductWithoutCategoriesList(List<Product> products);

    @Named("fromProductWithCategories")
    @Mapping(target = "categories", qualifiedByName = "fromCategory")
    ProductResponseDTO fromProductWithCategories(Product product);

    @Named("fromProductWithCategoriesList")
    @IterableMapping(qualifiedByName = "fromProductWithCategories")
    @Mapping(target = "categories", qualifiedByName = "fromCategory")
    List<ProductResponseDTO> fromProductWithCategoriesList(List<Product> products);
}
