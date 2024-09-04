package konta.projectmd4.service.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormProduct;
import konta.projectmd4.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    Product save(List<MultipartFile> fileUploads, @Valid FormProduct formProduct) throws CustomException, JsonProcessingException;
    Page<Product> findAll(Pageable pageable);
    Product findById(Integer id) throws CustomException;
    List<Product> findProductByCategoryId(Integer categoryId);
    List<Product> findProductsByNameIgnoreCaseOrDescriptionIgnoreCase(String name, String description);
    List<Product> findTop10ByOrderByCreatedAtDesc();
    void deleteById(Integer id) throws CustomException;
    Product update(Integer productId,FormProduct product) throws CustomException;
    Product updateP(Integer productId,List<MultipartFile> fileUploads, @Validated FormProduct formProduct) throws CustomException ;
    Product saveNormal(Product product);
}
