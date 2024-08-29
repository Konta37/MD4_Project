package konta.projectmd4.controller.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormProduct;
import konta.projectmd4.model.entity.admin.Product;
import konta.projectmd4.repository.admin.IProductRepository;
import konta.projectmd4.service.admin.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/product")
public class ProductController {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(@PageableDefault(page = 0, size = 2) Pageable pageable) {
        // Use the pageable object to fetch paginated data
        Page<Product> productPage = productRepository.findAll(pageable);

        return ResponseEntity.ok().body(productPage);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Validated @RequestBody FormProduct formProduct) throws CustomException {
        Product savedProduct = productService.save(formProduct);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED); // Use 201 Created status
    }
}
