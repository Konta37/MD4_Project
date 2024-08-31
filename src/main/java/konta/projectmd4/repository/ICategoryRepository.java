package konta.projectmd4.repository;

import konta.projectmd4.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name); // Method to find a category by name
    Page<Category> findAll(Pageable pageable);
    void deleteById(Integer id);
    Category findCategoryById(Integer id);
}
