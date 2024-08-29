package konta.projectmd4.service.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormCategory;
import konta.projectmd4.model.entity.admin.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICategoryService {
    Category save(FormCategory formCategory) throws CustomException;
    Optional<Category> findByName(String name); // Method to find a category by name
    Page<Category> findAll(Pageable pageable);
    void deleteById(Integer id) throws CustomException;
    Category findCategoryById(Integer id) throws CustomException;
}
