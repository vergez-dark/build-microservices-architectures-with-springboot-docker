package com.jeffrey.POST_SERVICE.services;

import java.util.List;

import com.jeffrey.POST_SERVICE.dto.CategoryRequest;
import com.jeffrey.POST_SERVICE.dto.CategoryResponse;

public interface CategoryService {


    CategoryResponse addCategory(CategoryRequest categoryRequest);

    CategoryResponse getCategory(Long category_id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(CategoryRequest categoryRequest, Long category_id);

    void deleteCategory(Long categoryId);
}

