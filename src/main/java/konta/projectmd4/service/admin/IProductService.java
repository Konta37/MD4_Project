package konta.projectmd4.service.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormProduct;
import konta.projectmd4.model.entity.admin.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    Product save(FormProduct formProduct) throws CustomException;
    Page<Product> findAll(Pageable pageable);
    Product findById(Integer id) throws CustomException;
    List<Product> findProductByCategoryId(Integer categoryId);
    List<Product> findProductsByNameIgnoreCaseOrDescriptionIgnoreCase(String name, String description);
}
