package com.jeffrey.POST_SERVICE.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeffrey.POST_SERVICE.dto.CategoryRequest;
import com.jeffrey.POST_SERVICE.dto.CategoryResponse;
import com.jeffrey.POST_SERVICE.exceptions.ResourceNotFoundException;
import com.jeffrey.POST_SERVICE.mappers.CategoryMapper;
import com.jeffrey.POST_SERVICE.models.Category;
import com.jeffrey.POST_SERVICE.repository.CategoryRepository;
import com.jeffrey.POST_SERVICE.services.CategoryService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CategoryServiceImpl  implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse addCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toCategory(categoryRequest);
        category.setSlug(categoryRequest.getName().replaceAll(" ", "-").toLowerCase());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategory(Long category_id) {
      Category category = categoryRepository.findById(category_id).orElseThrow(() -> new  ResourceNotFoundException("Category", "id", category_id));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> categoryMapper.toCategoryResponse(category))
                .toList();
        return categoryResponses;
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, Long category_id) {
        Category category = categoryRepository.findById(category_id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", category_id));
        category.setName(categoryRequest.getName());
        category.setSlug(categoryRequest.getName().replaceAll(" ", "-").toLowerCase());
        category.setLastUpdateOn(new Date());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepository.delete(category);
    }

   
    
}
