package com.jeffrey.POST_SERVICE.config;

import com.jeffrey.POST_SERVICE.models.Category;
import com.jeffrey.POST_SERVICE.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(Arrays.asList(
                new Category(null, "Technologie", "technologie", null),
                new Category(null, "Voyage", "voyage", null),
                new Category(null, "Cuisine", "cuisine", null),
                new Category(null, "Santé", "sante", null),
                new Category(null, "Éducation", "education", null)
            ));
        }
    }
}