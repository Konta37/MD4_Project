package konta.projectmd4.service.admin.impl;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCategory;
import konta.projectmd4.model.entity.admin.Category;
import konta.projectmd4.model.entity.admin.Product;
import konta.projectmd4.repository.admin.ICategoryRepository;
import konta.projectmd4.service.admin.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;

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

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Integer id) throws CustomException{
        List<Product> productList = productService.findProductByCategoryId(id);
        if (!productList.isEmpty()) {
            throw new CustomException("This category has products so can't delete",HttpStatus.BAD_REQUEST);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findCategoryById(Integer id) throws CustomException{
        return categoryRepository.findById(id).orElseThrow(() -> new CustomException("There is no category with ID " + id, HttpStatus.NOT_FOUND));
    }
}
