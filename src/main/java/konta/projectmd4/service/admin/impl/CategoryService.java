package konta.projectmd4.service.admin.impl;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCategory;
import konta.projectmd4.model.entity.admin.Category;
import konta.projectmd4.repository.admin.ICategoryRepository;
import konta.projectmd4.service.admin.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Category save(FormCategory formCategory) throws CustomException {
        // Check if a category with the same name already exists
        Optional<Category> existingCategory = categoryRepository.findByName(formCategory.getName());
        if (existingCategory.isPresent()) {
            throw new CustomException("Category name already exists", HttpStatus.CONFLICT); // You might want to create a custom exception
        }

        Category category = Category.builder()
                .name(formCategory.getName())
                .description(formCategory.getDescription())
                .status(true)
                .build();
        return categoryRepository.save(category);
    }
}
