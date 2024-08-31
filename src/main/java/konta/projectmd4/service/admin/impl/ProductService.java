package konta.projectmd4.service.admin.impl;

import konta.projectmd4.exception.CustomException;
import konta.projectmd4.model.dto.req.FormProduct;
import konta.projectmd4.model.entity.Category;
import konta.projectmd4.model.entity.Product;
import konta.projectmd4.repository.ICategoryRepository;
import konta.projectmd4.repository.IProductRepository;
import konta.projectmd4.service.admin.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Product save(FormProduct formProduct) throws CustomException{
        Category category = categoryRepository.findById(formProduct.getCategoryId())
                .orElseThrow(() -> new CustomException("There is no category with ID " + formProduct.getCategoryId(), HttpStatus.NOT_FOUND));
        Product product = Product.builder()
                .sku(UUID.randomUUID().toString().substring(0,6))
                .name(formProduct.getName())
                .description(formProduct.getDescription())
                .unitPrice(formProduct.getUnitPrice())
                .quantity(formProduct.getQuantity())
                .image(formProduct.getImage())
                .category(category)
                .build();

        return productRepository.save(product);
    }

    @Override
    public Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
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
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }

    @Override
    public void deleteById(Integer id) throws CustomException {
        Product product = findById(id);
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
        product.setImage(productUpdate.getImage());
        product.setCategory(categoryRepository.findById(productUpdate.getCategoryId())
                .orElseThrow(() -> new CustomException("There is no category with ID " + productUpdate.getCategoryId(), HttpStatus.NOT_FOUND)));
        return productRepository.save(product);
    }
}
