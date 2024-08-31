package konta.projectmd4.controller.general;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.resp.DataResponse;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.service.admin.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/product")
public class ProductGeneralController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<DataResponse<Page<Product>>> getAllProducts(@PageableDefault(page = 0, size = 2) Pageable pageable) {
        // Use the pageable object to fetch paginated data
        Page<Product> productPage = productService.findAll(pageable);

//        return ResponseEntity.ok().body(productPage);
        return new ResponseEntity<>(new DataResponse<>(productPage, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{proId}")
    public ResponseEntity<DataResponse<Product>> getProductById(@PathVariable Integer proId) throws CustomException {
        return new ResponseEntity<>(new DataResponse<>(productService.findById(proId), HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/categories/{cateId}")
    public ResponseEntity<DataResponse<List<Product>>> getProductByCateId(@PathVariable Integer cateId) throws CustomException {
        List<Product> productList = productService.findProductByCategoryId(cateId);
        if (productList.isEmpty()) {
            throw new CustomException("Can't find list product with category id " + cateId, HttpStatus.NOT_FOUND);
        }
//        return ResponseEntity.ok().body(productList);
        return new ResponseEntity<>(new DataResponse<>(productList, HttpStatus.OK), HttpStatus.OK);
    }

    //search by name or description
    @GetMapping("/search")
    public ResponseEntity<DataResponse<List<Product>>> getProductBySearch(@RequestParam String search) throws CustomException {
        List<Product> productList = productService.findProductsByNameIgnoreCaseOrDescriptionIgnoreCase(search, search);
        return new ResponseEntity<>(new DataResponse<>(productList, HttpStatus.OK), HttpStatus.OK);
    }

    //new products
    @GetMapping("/new-products")
    public ResponseEntity<DataResponse<List<Product>>> getNewProducts() {
        return new ResponseEntity<>(new DataResponse<>(productService.findTop10ByOrderByCreatedAtDesc(), HttpStatus.OK), HttpStatus.OK);
    }


}
