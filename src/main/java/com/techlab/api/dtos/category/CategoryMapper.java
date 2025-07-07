package com.techlab.api.dtos.category;

import com.techlab.api.entities.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Named("fromCategory")
    CategoryResponseDTO fromCategory(Category category);

    @Named("fromCategoryList")
    @IterableMapping(qualifiedByName = "fromCategory")
    List<CategoryResponseDTO> fromCategoryList(List<Category> categoryList);
}
