package konta.projectmd4.controller.admin;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormProduct;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.repository.IProductRepository;
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
    public ResponseEntity<DataResponse<Page<Product>>> getAllProducts(@PageableDefault(page = 0, size = 2) Pageable pageable) {
        // Use the pageable object to fetch paginated data
        Page<Product> productPage = productRepository.findAll(pageable);

        return new ResponseEntity<>(new DataResponse<>(productPage,HttpStatus.OK),HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<DataResponse<Product>> getProductById(@PathVariable Integer productId) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(productService.findById(productId),HttpStatus.OK),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse<Product>> addProduct(@Validated @RequestBody FormProduct formProduct) throws CustomException {
        Product savedProduct = productService.save(formProduct);
        return new ResponseEntity<>(new DataResponse<>(savedProduct, HttpStatus.CREATED),HttpStatus.CREATED); // Use 201 Created status
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<DataResponse<String>> deleteProduct(@PathVariable Integer productId) throws CustomException {
        productService.deleteById(productId);
        return new ResponseEntity<>(new DataResponse<>("Success delete", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<DataResponse<Product>> getProduct(@PathVariable Integer productId,@RequestBody FormProduct productUpdate) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(productService.update(productId,productUpdate),HttpStatus.OK),HttpStatus.OK);
    }
}
