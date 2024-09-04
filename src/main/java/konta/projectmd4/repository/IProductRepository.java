package konta.projectmd4.repository;

import konta.projectmd4.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String name); // Method to find a product by name
    Page<Product> findAll(Pageable pageable);
    List<Product> findProductByCategoryId(Integer categoryId);
    Optional<Product> findProductById(Integer id);
    List<Product> findProductsByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description); // General search by name or description
    List<Product> findTop10ByOrderByCreatedAtDesc();
    List<Product> findTop10ByStatusTrueOrderByCreatedAtDesc();
    Page<Product> findProductsByStatusTrue(Pageable pageable);
}
