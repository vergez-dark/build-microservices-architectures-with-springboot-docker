package com.jeffrey.POST_SERVICE.mappers;

import org.mapstruct.Mapper;
import com.jeffrey.POST_SERVICE.dto.CategoryRequest;
import com.jeffrey.POST_SERVICE.dto.CategoryResponse;
import com.jeffrey.POST_SERVICE.models.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
   
    CategoryResponse toCategoryResponse(Category category);
    Category toCategory(CategoryRequest categoryRequest);
}
