package konta.projectmd4.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/product")
@Validated
public class ProductController {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ProductService productService;


    private final ObjectMapper mapper;
    private final Validator validator;

    public ProductController(ProductService productService, ObjectMapper mapper, Validator validator) {
        this.productService = productService;
        this.mapper = mapper;
        this.validator = validator;
    }

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
    public ResponseEntity<?> addProduct(
            @RequestParam("imageFile") List<MultipartFile> imageFile,
            @RequestParam("data") String dataProduct) throws CustomException, JsonProcessingException {

        // Deserialize the JSON string into a FormProduct object
        FormProduct formProduct = mapper.readValue(dataProduct, FormProduct.class);

        //validate image url
        if (imageFile.get(0).getOriginalFilename().equals("")){
            throw new CustomException("Image file is empty",HttpStatus.BAD_REQUEST);
        }
        // Manually validate the FormProduct object
        Set<ConstraintViolation<FormProduct>> violations = validator.validate(formProduct);

        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<FormProduct> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // Proceed with saving the product if no validation errors
        Product savedProduct = productService.save(imageFile, formProduct);

        return new ResponseEntity<>(new DataResponse<>(savedProduct, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<DataResponse<String>> deleteProduct(@PathVariable Integer productId) throws CustomException {
        productService.deleteById(productId);
        return new ResponseEntity<>(new DataResponse<>("Success delete", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable Integer productId,
                                                            @RequestParam("imageFile") List<MultipartFile> imageFile,
                                                            @RequestParam("data") String dataProduct) throws CustomException, JsonProcessingException {
        // Deserialize the JSON string into a FormProduct object
        FormProduct formProduct = mapper.readValue(dataProduct, FormProduct.class);

        // Manually validate the FormProduct object
        Set<ConstraintViolation<FormProduct>> violations = validator.validate(formProduct);

        if (!violations.isEmpty()) {
            Map<String, String> errors = new HashMap<>();
            for (ConstraintViolation<FormProduct> violation : violations) {
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new DataResponse<>(productService.updateP(productId,imageFile,formProduct),HttpStatus.OK),HttpStatus.OK);
    }


}
