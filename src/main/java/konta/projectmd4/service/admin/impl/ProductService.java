package konta.projectmd4.service.admin.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormProduct;
import konta.projectmd4.model.entity.*;
import konta.projectmd4.repository.*;
import konta.projectmd4.service.admin.IProductService;
import konta.projectmd4.service.impl.UploadFileImpl;
import konta.projectmd4.service.user.impl.CartServiceImpl;
import konta.projectmd4.service.user.impl.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private UploadFileImpl uploadFile;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private IImageProductRepository imageProductRepository;
    @Autowired
    private ICartRepository cartRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @Override
    public Product save(List<MultipartFile> fileUploads, @Validated FormProduct formProduct)
            throws CustomException, JsonProcessingException {

        // Step 1: Fetch the Category entity from the database
        Category category = categoryRepository.findById(formProduct.getCategoryId())
                .orElseThrow(() -> new CustomException("There is no category with ID " + formProduct.getCategoryId(), HttpStatus.NOT_FOUND));

        // Step 2: Create the Product entity
        Product product = Product.builder()
                .sku(UUID.randomUUID().toString().substring(0, 6))
                .name(formProduct.getName())
                .description(formProduct.getDescription())
                .unitPrice(formProduct.getUnitPrice())
                .quantity(formProduct.getQuantity())
                .category(category)
                .status(true)
                .build();

        // Step 3: Save the Product entity first to make it persistent
        product = productRepository.save(product);

        // Step 4: Upload image files and associate them with the product
        List<ImageProduct> imageNames = new ArrayList<>();
        for (MultipartFile fileUpload : fileUploads) {
            String imageName = uploadFile.uploadLocal(fileUpload);  // Assuming this method returns the file URL or name
            ImageProduct image = ImageProduct.builder()
                    .imageUrl(imageName)
                    .product(product)
                    .build();
            imageNames.add(image);
        }

        // Step 5: Save all ImageProduct entities
        imageProductRepository.saveAll(imageNames);

        // Step 6: Optionally, set the images list in the product and update the product if needed
        product.setImages(imageNames);

        return product;
    }

    @Override
    public Page<Product> findAll(Pageable pageable){
        return productRepository.findProductsByStatusTrue(pageable);
    }

    @Override
    public Product findById(Integer id) throws CustomException{
        return productRepository.findProductById(id).orElseThrow(() -> new CustomException("There is no product with ID " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Product> findProductByCategoryId(Integer categoryId) {
        return productRepository.findProductByCategoryId(categoryId);
    }

    @Override
    public List<Product> findProductsByNameIgnoreCaseOrDescriptionIgnoreCase(String name, String description) {
        return productRepository.findProductsByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(name,description);
    }

    @Override
    public List<Product> findTop10ByOrderByCreatedAtDesc() {
//        return productRepository.findTop10ByOrderByCreatedAtDesc();
        return productRepository.findTop10ByStatusTrueOrderByCreatedAtDesc();
    }

    @Override
    public void deleteById(Integer id) throws CustomException {
        Product product = findById(id);

        //check in cart if null -> continue
        List<CartItem> cartItemList = cartRepository.findCartItemsByProduct(product);
        if (!cartItemList.isEmpty()){
            throw new CustomException("Product: " + product.getId() + " is exist in cart -> can' delete", HttpStatus.BAD_REQUEST);
        }

        //check in order detail if null -> continue
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailsByProduct(product);
        if (!orderDetails.isEmpty()){
            throw new CustomException("Product: " + product.getId() + " is exist in order detail -> can't delete", HttpStatus.BAD_REQUEST);
        }

        productRepository.delete(product);
        //check if there are any product in wishlist, cart, order,...
    }

    @Override
    public Product update(Integer productId, FormProduct productUpdate) throws CustomException {
        Product product = findById(productId);
        product.setName(productUpdate.getName());
        product.setDescription(productUpdate.getDescription());
        product.setUnitPrice(productUpdate.getUnitPrice());
        product.setQuantity(productUpdate.getQuantity());
//        product.setImage(productUpdate.getImage());
        product.setCategory(categoryRepository.findById(productUpdate.getCategoryId())
                .orElseThrow(() -> new CustomException("There is no category with ID " + productUpdate.getCategoryId(), HttpStatus.NOT_FOUND)));
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateP(Integer productId, List<MultipartFile> fileUploads, FormProduct formProduct) throws CustomException {
        // Step 1: Fetch the existing product from the database
        Product product = findById(productId);

        // Step 2: Fetch the Category entity from the database
        Category category = categoryRepository.findById(formProduct.getCategoryId())
                .orElseThrow(() -> new CustomException("There is no category with ID " + formProduct.getCategoryId(), HttpStatus.NOT_FOUND));

        // Step 3: Update product fields
        product.setName(formProduct.getName());
        product.setDescription(formProduct.getDescription());
        product.setUnitPrice(formProduct.getUnitPrice());
        product.setQuantity(formProduct.getQuantity());
        product.setCategory(category);
        product.setUpdatedAt(new Date());

        // Step 4: Check if there are any file uploads
        if (fileUploads != null && !fileUploads.isEmpty() && !fileUploads.get(0).getOriginalFilename().isEmpty()) {
            // Step 5: Clear old images from the product entity
            product.getImages().clear(); // Clear the current images collection

            // Step 6: Upload new image files and associate them with the product
            List<ImageProduct> newImages = new ArrayList<>();
            for (MultipartFile fileUpload : fileUploads) {
                String imageName = uploadFile.uploadLocal(fileUpload); // Assuming this method returns the file URL or name
                ImageProduct image = ImageProduct.builder()
                        .imageUrl(imageName)
                        .product(product)
                        .build();
                newImages.add(image);
            }

            // Step 7: Add new images to the product
            product.getImages().addAll(newImages); // Add the new images to the product
        }

        // Step 8: Save the updated product
        return productRepository.save(product);
    }

    @Override
    public Product saveNormal(Product product) {
        return productRepository.save(product);
    }
}
