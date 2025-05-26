package com.jeffrey.POST_SERVICE.repository;

import com.jeffrey.POST_SERVICE.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository <Category, Long> {
    
}
