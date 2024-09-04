package konta.projectmd4.repository;

import konta.projectmd4.model.entity.ImageProduct;
import konta.projectmd4.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IImageProductRepository extends JpaRepository<ImageProduct, Integer> {
    List<ImageProduct> findImageProductsByProduct_Id(Integer productId);
    void deleteByProduct_Id(Integer productId);
}
